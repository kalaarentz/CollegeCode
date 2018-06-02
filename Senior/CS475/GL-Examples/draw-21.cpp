#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "colors.h"
#include "trackball.h"
#include "Matrices.h"
#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "OBJModel.h"
#include "modelVAO.h"
#include "shaderUtil.h"
#include "matrixUtil.h"
#include "scene.h"

#include "draw.h"

/* define several colors */
GLfloat BlueClr[] = {0.2, 0.2, 0.8, 1.0};
GLfloat YeloClr[] = {0.8, 0.8, 0.0, 1.0};
GLfloat GrenClr[] = {0.0, 0.8, 0.0, 1.0};
GLfloat RedClr[]  = {0.8, 0.0, 0.0, 1.0};
GLfloat BrwnClr[] = {0.5, 0.3, 0.2, 1.0};
GLfloat PrplClr[] = {0.7, 0.2, 0.7, 1.0};
GLfloat BlakClr[] = {0.0, 0.0, 0.0, 1.0};
GLfloat WhitClr[] = {1.0, 1.0, 1.0, 1.0};
GLfloat GrayClr[] = {0.3, 0.3, 0.3, 1.0};

GLfloat LightPos[] = {1.0, 1.0, 1.0, 0.0};
GLfloat BasicMaterial[4] = {0.2, 0.7, 1.0, 50.0};


// shader program data
struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint modelviewUniform;
  GLint projUniform;
  GLint selectmodeUniform;
  GLint selecttagUniform;
  GLint targetmodeUniform;
  GLint mouseUniform;
} selectProgram;


// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO cube;

void initShaders() {
  selectProgram.id = shader_Compile("select");
  shader_Link(selectProgram.id);
  selectProgram.lightposUniform = glGetUniformLocation(selectProgram.id,"lightpos");
  selectProgram.materialUniform = glGetUniformLocation(selectProgram.id,"material");
  selectProgram.colorUniform = glGetUniformLocation(selectProgram.id,"color");
  selectProgram.modelviewUniform = glGetUniformLocation(selectProgram.id,"modelview");
  selectProgram.projUniform = glGetUniformLocation(selectProgram.id,"proj");
  selectProgram.selectmodeUniform = glGetUniformLocation(selectProgram.id,"selectmode");
  selectProgram.selecttagUniform = glGetUniformLocation(selectProgram.id,"selecttag");
  selectProgram.targetmodeUniform = glGetUniformLocation(selectProgram.id,"targetmode");
  selectProgram.mouseUniform = glGetUniformLocation(selectProgram.id,"mouse");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d %d\n", selectProgram.lightposUniform, selectProgram.materialUniform, selectProgram.colorUniform, selectProgram.modelviewUniform, selectProgram.projUniform, selectProgram.selectmodeUniform, selectProgram.selecttagUniform, selectProgram.targetmodeUniform, selectProgram.mouseUniform);
}

void draw_Init() {
  initShaders();

  axisTetra = makeVAOForModel(faceCoordTetraTex());
  sphere = makeVAOForModel(faceSphereTex(24,48));
  cube = makeVAOForModel(readOBJArrays("OBJModels/Cube-Cyl.obj"));

  struct uniforms uniforms = {selectProgram.colorUniform,selectProgram.materialUniform,selectProgram.modelviewUniform,
    selectProgram.selecttagUniform};

  scene_Init(uniforms);

  struct drawable drawable;
  drawable.selectTag = 25;
  drawable.color = Vector4(0.9,0.9,0.3,1.0);
  drawable.material = Vector4(0.2,0.8,1.0,10.0);
  drawable.highlightColor = Vector4(1,0,1,1);
  drawable.transforms = mutil_Translate(Vector3(1,1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selected = false;
  drawable.animating = false;
  drawable.model = sphere;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(1,-1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 50;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-1,-1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 75;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-1,1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 100;
  scene_AddDrawable(drawable);
  drawable.model = cube;
  drawable.color = Vector4(0.2,0.8,0.7,1.0);
  drawable.transforms = mutil_Translate(Vector3(1,-1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 125;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-1,1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 150;
  scene_AddDrawable(drawable);

  glEnable(GL_SCISSOR_TEST);
}

void draw_Contents(json state, Matrix4 mv) {
  // draw coordinate frame
  glUniform3fv(selectProgram.lightposUniform,1,LightPos);
  glUniform4fv(selectProgram.materialUniform,1,BasicMaterial);

  glUniform1i(selectProgram.selecttagUniform,0);

  glBindVertexArray(axisTetra.VAO);
  glUniformMatrix4fv(selectProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0.5,0,0)) * mutil_Scale(Vector3(2,0.5,0.5))).get());
  glUniform3fv(selectProgram.colorUniform,1,RedClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(selectProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,0.5,0)) * mutil_Scale(Vector3(0.5,2,0.5))).get());
  glUniform3fv(selectProgram.colorUniform,1,GrenClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(selectProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,0,0.5)) * mutil_Scale(Vector3(0.5,0.5,2))).get());
  glUniform3fv(selectProgram.colorUniform,1,BlueClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);

  // draw scene
  scene_Draw(mv);
}

float aspect;
float nearZ = 5.0;
float farZ = 100.0;
float backOff = -20.0;

void draw_Scene(json state) {
  glUseProgram(selectProgram.id);
  glUniform1i(selectProgram.selectmodeUniform,state["selectmode"]);

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv;

  // support two different display modes
  // first specifices a single viewport and corresponding frustum (left,right,bottom,top),
  //   optionally, the viewport can be determined from the frustum
  // second take an array of viewports arranged in a grid and calculate the corresponding
  //   frustum parameters
  if (state.find("viewport") != state.end()) {
    if (state["viewportFromFrustum"]) {
      GLfloat x = state["frustum"][0].get<double>() / 2.0 + 0.5;
      GLfloat y = state["frustum"][2].get<double>() / 2.0 + 0.5;
      GLfloat w = (state["frustum"][1].get<double>() - state["frustum"][0].get<double>()) / 2.0;
      GLfloat h = (state["frustum"][3].get<double>() - state["frustum"][2].get<double>()) / 2.0;
      glViewport(x*WinWidth,y*WinHeight,w*WinWidth,h*WinHeight);
    }
    else {
      GLfloat x = state["viewport"][0];
      GLfloat y = state["viewport"][1];
      GLfloat w = state["viewport"][2];
      GLfloat h = state["viewport"][3];
      glViewport(x*WinWidth,y*WinHeight,w*WinWidth,h*WinHeight);
    }
    glClearColor(.2,.2,.2,1.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    Matrix4 proj = mutil_Frustum(state["frustum"][0],state["frustum"][1],state["frustum"][2],state["frustum"][3],nearZ,farZ);
    glUniformMatrix4fv(selectProgram.projUniform,1,GL_FALSE,proj.get());
    mv = mutil_Translate(Vector3(0,0,backOff)) * tbMatrix;
    draw_Contents(state,mv);
  }
  else {
    for (int i = 0; i < state["viewports"].size(); i++) {
      float x = state["viewports"][i][0];
      float y = state["viewports"][i][1];
      float w = state["viewportWidth"];
      float h = state["viewportHeight"];
      glViewport(x*WinWidth,y*WinHeight,w*WinWidth,h*WinHeight);
      glScissor(x*WinWidth,y*WinHeight,w*WinWidth,h*WinHeight);
      glClearColor(.2,.2,.2,1.0);
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      GLfloat left = -1 + 2*x;
      GLfloat right = -1 + 2*(x+w);
      GLfloat bottom = aspect * (-1 + 2*y);
      GLfloat top = aspect * (-1 + 2*(y+h));
      Matrix4 proj = mutil_Frustum(left,right,bottom,top,nearZ,farZ);
      glUniformMatrix4fv(selectProgram.projUniform,1,GL_FALSE,proj.get());
      mv = mutil_Translate(Vector3(0,0,backOff)) * tbMatrix;
      draw_Contents(state,mv);
    }
  }
}

void draw_Resize(int width, int height) {
  aspect = width / (float) height;
}

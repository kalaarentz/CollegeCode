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

Matrix4 leftProjMatrix;
Matrix4 rightProjMatrix;

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

float width, height;
float aspect;
float nearZ = 5.0;
float farZ = 100.0;
float backOff = -20.0;
float eyeSep = 1.3;
float horzShift = 0.4;

void draw_Scene(json state) {
  glUseProgram(selectProgram.id);
  glUniform1i(selectProgram.selectmodeUniform,state["selectmode"]);

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv;

  glViewport(width/2.0,0.0,width/2.0,height);
  glUniformMatrix4fv(selectProgram.projUniform,1,GL_FALSE,leftProjMatrix.get());
  mv = mutil_Translate(Vector3(eyeSep,0.0,backOff)) * tbMatrix;
  draw_Contents(state,mv);

  glViewport(0.0,0.0,width/2.0,height);
  glUniformMatrix4fv(selectProgram.projUniform,1,GL_FALSE,rightProjMatrix.get());
  mv = mutil_Translate(Vector3(-eyeSep,0.0,backOff)) * tbMatrix;
  draw_Contents(state,mv);
}

void draw_Resize(int w, int h) {
  width = w;
  height = h;
  aspect = width / (float) height;
  //make left, right frustum
  leftProjMatrix = mutil_Frustum(-1.0+horzShift,1.0+horzShift,-aspect,aspect,nearZ,farZ);
  rightProjMatrix = mutil_Frustum(-1.0-horzShift,1.0-horzShift,-aspect,aspect,nearZ,farZ);
}

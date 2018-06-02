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

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

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
  fprintf(stderr,"sphere %d %u %u\n",sphere.VAO,sphere.vCnt,sphere.iCnt);

  struct uniforms uniforms = {selectProgram.colorUniform,selectProgram.materialUniform,selectProgram.modelviewUniform,
    selectProgram.selecttagUniform};

  scene_Init(uniforms);

  struct drawable drawable;
  drawable.selectTag = 50;
  drawable.color = Vector4(0.9,0.9,0.3,1.0);
  drawable.material = Vector4(0.2,0.8,1.0,10.0);
  drawable.highlightColor = Vector4(1,0,1,1);
  drawable.transforms = mutil_Translate(Vector3(1,1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selected = false;
  drawable.animating = false;
  drawable.model = sphere;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(1,-1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 100;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-1,-1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 150;
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-1,1,1)) * mutil_Scale(Vector3(0.7,0.7,0.7));
  drawable.selectTag = 200;
  scene_AddDrawable(drawable);
}

void draw_Scene(json state) {
  glUseProgram(selectProgram.id);
  glUniform1i(selectProgram.selectmodeUniform,state["selectmode"]);

  glUniform1i(selectProgram.targetmodeUniform,state["targetmode"]);
  glUniform2f(selectProgram.mouseUniform,state["mousex"],state["mousey"]);

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-8)) * tbMatrix;

  // draw coordinate frame
  glUniform3fv(selectProgram.lightposUniform,1,LightPos);
  glUniform4fv(selectProgram.materialUniform,1,BasicMaterial);
  glUniformMatrix4fv(selectProgram.projUniform,1,GL_FALSE,projMatrix.get());

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

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

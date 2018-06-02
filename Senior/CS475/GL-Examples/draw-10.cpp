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
#include "shaderUtil.h"
#include "matrixUtil.h"

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

// shader names
const char * shaderNames[] = {"reflect-vrt", "reflect-frg", "halfangle1-vrt", "halfangle1-frg", "halfangle2-vrt", "halfangle2-frg"};
// shader program data
struct programInfo {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint modelviewUniform;
  GLint projUniform;
};
struct programInfo programs[6];
struct programInfo currentProgram;
// Vertex Attribute Indices
GLuint positionIndex = 0;
GLuint normalIndex = 1;
// Models
struct faceModel tetra;
struct faceModel chicken;
struct faceModel torus;
// Vertex Array and Vertex Buffer Object References
GLuint tetraVAO, tetraVBO[2];
GLuint chickenVAO, chickenVBO[2];
GLuint torusVAO, torusVBO[2];

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

void InitShader(int i, const char * basename) {
  programs[i].id = shader_Compile(basename);
  glBindAttribLocation(programs[i].id,positionIndex,"position");
  glBindAttribLocation(programs[i].id,normalIndex,"normal");
  shader_Link(programs[i].id);
  programs[i].lightposUniform = glGetUniformLocation(programs[i].id,"lightpos");
  programs[i].materialUniform = glGetUniformLocation(programs[i].id,"material");
  programs[i].colorUniform = glGetUniformLocation(programs[i].id,"color");
  programs[i].modelviewUniform = glGetUniformLocation(programs[i].id,"modelview");
  programs[i].projUniform = glGetUniformLocation(programs[i].id,"proj");
  fprintf(stderr,"uniform: %d %d %d %d %d\n", programs[i].lightposUniform, programs[i].materialUniform, programs[i].colorUniform, programs[i].modelviewUniform, programs[i].projUniform);
}

void ConfigModelBuffers(struct faceModel model, GLuint *VAO, GLuint *VBO) {
  glGenVertexArrays(1,VAO);
  glBindVertexArray(*VAO);
  glGenBuffers(2,VBO);
  glBindBuffer(GL_ARRAY_BUFFER,VBO[0]);
  glBufferData(GL_ARRAY_BUFFER,model.vrts.size()*3*sizeof(GLfloat),model.vrts.data(),GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,0,0);
  glEnableVertexAttribArray(positionIndex);
  glBindBuffer(GL_ARRAY_BUFFER,VBO[1]);
  glBufferData(GL_ARRAY_BUFFER,model.nmls.size()*3*sizeof(GLfloat),model.nmls.data(),GL_STATIC_DRAW);
  glVertexAttribPointer(normalIndex,3,GL_FLOAT,GL_FALSE,0,0);
  glEnableVertexAttribArray(normalIndex);
}

void draw_Init() {
  for (int i = 0; i < 6; i++) InitShader(i,shaderNames[i]);

  tetra = faceCoordTetra();
  ConfigModelBuffers(tetra,&tetraVAO,tetraVBO);
  chicken = readOBJArrays("OBJModels/chicken.obj");
  ConfigModelBuffers(chicken,&chickenVAO,chickenVBO);
  torus = readOBJArrays("OBJModels/lumpytorus.obj");
  ConfigModelBuffers(torus,&torusVAO,torusVBO);
}

int lastProg = -1;
void SetProgram(json state) {
  int program = state["program"].get<int>();
  if (program != lastProg) {
    fprintf(stderr,"using %s\n",shaderNames[program]);
    lastProg = program;
  }
  currentProgram = programs[program];
}

void draw_Scene(json state) {
  SetProgram(state);

  glUseProgram(currentProgram.id);
  glUniform3fv(currentProgram.lightposUniform,1,LightPos);
  glUniform4f(currentProgram.materialUniform, state["ambient"].get<float>(), state["diffuse"].get<float>(), state["specular"].get<float>(), state["specexp"].get<float>());
  glUniformMatrix4fv(currentProgram.projUniform,1,GL_FALSE,projMatrix.get());

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-5)) * tbMatrix;
  Matrix4 mvo = mv * mutil_Translate(Vector3(-1,-1,-1));

  glBindVertexArray(tetraVAO);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0.5,0,0)) * mutil_Scale(Vector3(2,0.5,0.5))).get());
  glUniform3fv(currentProgram.colorUniform,1,RedClr);
  glDrawArrays(GL_TRIANGLES,0,tetra.vrts.size());
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0.5,0)) * mutil_Scale(Vector3(0.5,2,0.5))).get());
  glUniform3fv(programs[0].colorUniform,1,GrenClr);
  glDrawArrays(GL_TRIANGLES,0,tetra.vrts.size());
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0,0.5)) * mutil_Scale(Vector3(0.5,0.5,2))).get());
  glUniform4fv(currentProgram.colorUniform,1,BlueClr);
  glDrawArrays(GL_TRIANGLES,0,tetra.vrts.size());

  glBindVertexArray(chickenVAO);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Rotate(state["angle"],Vector3(0,0,1)) * mutil_Scale(Vector3(0.5,0.5,0.5))).get());
  glUniform3fv(currentProgram.colorUniform,1,YeloClr);
  glDrawArrays(GL_TRIANGLES,0,chicken.vrts.size());

  glBindVertexArray(torusVAO);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,0,-1))).get());
  glUniform3fv(currentProgram.colorUniform,1,PrplClr);
  glDrawArrays(GL_TRIANGLES,0,torus.vrts.size());
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

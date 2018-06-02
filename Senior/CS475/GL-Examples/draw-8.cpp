#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "trackball.h"
#include "Matrices.h"
#include "Vectors.h"
#include "shaderUtil.h"
#include "matrixUtil.h"

#include "draw.h"


GLfloat tetraVrts[]     = {0,0,0, 0,1,0, 1,0,0,     0,0,0, 0,0,1, 0,1,0,     0,0,0, 1,0,0, 0,0,1,     1,0,0, 0,1,0, 0,0,1};
GLfloat tetraFaceClrs[] = {0,0,1, 0,0,1, 0,0,1,     1,0,0, 1,0,0, 1,0,0,     0,1,0, 0,1,0, 0,1,0,     1,1,1, 1,1,1, 1,1,1};
GLfloat tetraVrtsClrs[] = {1,1,1, 0,1,0, 1,0,0,     1,1,1, 0,0,1, 0,1,0,     1,1,1, 1,0,0, 0,0,1,     1,0,0, 0,1,0, 0,0,1};

// the shader program
GLuint basicProgram;
// Vertex Attribute Indices
GLuint positionIndex = 0;
GLuint colorIndex = 1;
// Uniform Variable Locations
GLint pvmUniform;
// Vertex Array and Vertex Buffer Object References
GLuint faceVAO, faceVBO[2];
GLuint vertVAO, vertVBO[2];

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

void InitShaders() {
  basicProgram = shader_Compile("basic-color");

  glBindAttribLocation(basicProgram,positionIndex,"position");
  glBindAttribLocation(basicProgram,colorIndex,"color");

  shader_Link(basicProgram);

  pvmUniform = glGetUniformLocation(basicProgram,"pvmmatrix");
}

void draw_Init() {
  InitShaders();

  // ==
  glGenVertexArrays(1,&faceVAO);
  glBindVertexArray(faceVAO);

  glGenBuffers(2,faceVBO);

  glBindBuffer(GL_ARRAY_BUFFER,faceVBO[0]);
  glBufferData(GL_ARRAY_BUFFER, 12 * 3 * sizeof(GLfloat), tetraVrts, GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,0,(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);

  glBindBuffer(GL_ARRAY_BUFFER,faceVBO[1]);
  glBufferData(GL_ARRAY_BUFFER, 12 * 3 * sizeof(GLfloat), tetraFaceClrs, GL_STATIC_DRAW);
  glVertexAttribPointer(colorIndex,3,GL_FLOAT,GL_FALSE,0,0);
  glEnableVertexAttribArray(colorIndex);

  // ==
  glGenVertexArrays(1,&vertVAO);
  glBindVertexArray(vertVAO);

  glGenBuffers(2,vertVBO);

  glBindBuffer(GL_ARRAY_BUFFER,vertVBO[0]);
  glBufferData(GL_ARRAY_BUFFER, 12 * 3 * sizeof(GLfloat), tetraVrts, GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,0,(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);

  glBindBuffer(GL_ARRAY_BUFFER,vertVBO[1]);
  glBufferData(GL_ARRAY_BUFFER, 12 * 3 * sizeof(GLfloat), tetraVrtsClrs, GL_STATIC_DRAW);
  glVertexAttribPointer(colorIndex,3,GL_FLOAT,GL_FALSE,0,0);
  glEnableVertexAttribArray(colorIndex);

  glUseProgram(basicProgram);
}

void draw_Scene(json state) {
  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);
  Matrix4 pvm = projMatrix * tbMatrix;

  glBindVertexArray(faceVAO);
  glUniformMatrix4fv(pvmUniform,1,GL_FALSE,pvm.get());
  glDrawArrays(GL_TRIANGLES,0,12);

  pvm = projMatrix * tbMatrix * mutil_Rotate(state["angle"],Vector3(1,1,1)) * mutil_Scale(Vector3(-1,-1,-1));

  glBindVertexArray(vertVAO);
  glUniformMatrix4fv(pvmUniform,1,GL_FALSE,pvm.get());
  glDrawArrays(GL_TRIANGLES,0,12);
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(30,aspect,0.1,100.0);
  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

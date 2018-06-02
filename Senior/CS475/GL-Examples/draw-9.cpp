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

GLfloat LightPos[] = {1.0, 1.0, 1.0, 0.0};

struct vertex {
  GLfloat pos[3];
  GLfloat nml[3];
};

struct vertex faceTetra[] = {
  { {1,0,0}, {.5773,.5773,.5773} },
  { {0,1,0}, {.5773,.5773,.5773} },
  { {0,0,1}, {.5773,.5773,.5773} },
  { {0,0,0}, {0,0,-1} },
  { {0,1,0}, {0,0,-1} },
  { {1,0,0}, {0,0,-1} },
  { {0,0,0}, {0,-1,0} },
  { {1,0,0}, {0,-1,0} },
  { {0,0,1}, {0,-1,0} },
  { {0,0,0}, {-1,0,0} },
  { {0,0,1}, {-1,0,0} },
  { {0,1,0}, {-1,0,0} },
};

struct vertex bentTetra[] = {
  { {1,0,0}, {1,0,0} },
  { {0,1,0}, {0,1,0} },
  { {0,0,1}, {0,0,1} },
  { {0,0,0}, {0,0,-1} },
  { {0,1,0}, {0,0,-1} },
  { {1,0,0}, {0,0,-1} },
  { {0,0,0}, {0,-1,0} },
  { {1,0,0}, {0,-1,0} },
  { {0,0,1}, {0,-1,0} },
  { {0,0,0}, {-1,0,0} },
  { {0,0,1}, {-1,0,0} },
  { {0,1,0}, {-1,0,0} },
};

// the shader program
GLuint basicProgram;
// Vertex Attribute Indices
GLuint positionIndex = 0;
GLuint normalIndex = 1;
// Uniform Variable Locations
GLint lightosUniform;
GLint modelviewUniform;
GLint projUniform;
GLint colorUniform;
// Vertex Array and Vertex Buffer Object References
GLuint faceVAO, faceVBO[1];
GLuint bentVAO, bentVBO[1];

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

void InitShaders() {
  basicProgram = shader_Compile("basic-light");

  glBindAttribLocation(basicProgram,positionIndex,"position");
  glBindAttribLocation(basicProgram,normalIndex,"normal");

  shader_Link(basicProgram);

  lightosUniform = glGetUniformLocation(basicProgram,"lightpos");
  modelviewUniform = glGetUniformLocation(basicProgram,"modelview");
  projUniform = glGetUniformLocation(basicProgram,"proj");
  colorUniform = glGetUniformLocation(basicProgram,"vcolor");
}

void draw_Init() {
  InitShaders();

  //==
  glGenVertexArrays(1,&faceVAO);
  glBindVertexArray(faceVAO);

  glGenBuffers(1,faceVBO);
  glBindBuffer(GL_ARRAY_BUFFER,faceVBO[0]);

  glBufferData(GL_ARRAY_BUFFER, 12 * sizeof(struct vertex), faceTetra, GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct vertex),(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);
  glVertexAttribPointer(normalIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct vertex),(const GLvoid*) (3 * sizeof(GLfloat)));
  glEnableVertexAttribArray(normalIndex);

  //==
  glGenVertexArrays(1,&bentVAO);
  glBindVertexArray(bentVAO);

  glGenBuffers(1,bentVBO);
  glBindBuffer(GL_ARRAY_BUFFER,bentVBO[0]);

  glBufferData(GL_ARRAY_BUFFER, 12 * sizeof(struct vertex), bentTetra, GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct vertex),(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);
  glVertexAttribPointer(normalIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct vertex),(const GLvoid*) (3 * sizeof(GLfloat)));
  glEnableVertexAttribArray(normalIndex);

  glUseProgram(basicProgram);
}

void draw_Scene(json state) {
  glUniform3fv(lightosUniform,1,LightPos);
  glUniformMatrix4fv(projUniform,1,GL_FALSE,projMatrix.get());

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);
  Matrix4 mv = tbMatrix;

  glUniform3f(colorUniform,0.2,1,1);
  glUniformMatrix4fv(modelviewUniform,1,GL_FALSE,mv.get());
  glBindVertexArray(faceVAO);
  glDrawArrays(GL_TRIANGLES,0,12);

  mv = tbMatrix * mutil_Rotate(state["angle"],Vector3(1,1,1)) * mutil_Scale(Vector3(-1,-1,-1));

  glUniform3f(colorUniform,1,1,0.2);
  glUniformMatrix4fv(modelviewUniform,1,GL_FALSE,mv.get());
  glBindVertexArray(bentVAO);
  glDrawArrays(GL_TRIANGLES,0,12);
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(30,aspect,0.1,100.0);
  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

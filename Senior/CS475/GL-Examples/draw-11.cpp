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

struct tagVertex {
  GLfloat pos[3];
  GLfloat nml[3];
  GLfloat tag;
};

// tetrahedron with per face normals
struct tagVertex tagTetra[12] = {
  { {1,0,0}, {.5773,.5773,.5773}, 0.0 },
  { {0,1,0}, {.5773,.5773,.5773}, 1.0 },
  { {0,0,1}, {.5773,.5773,.5773}, 2.0 },
  { {0,0,0}, {0,0,-1}, 0.0 },
  { {0,1,0}, {0,0,-1}, 1.0 },
  { {1,0,0}, {0,0,-1}, 2.0 },
  { {0,0,0}, {0,-1,0}, 0.0 },
  { {1,0,0}, {0,-1,0}, 1.0 },
  { {0,0,1}, {0,-1,0}, 2.0 },
  { {0,0,0}, {-1,0,0}, 0.0 },
  { {0,0,1}, {-1,0,0}, 1.0 },
  { {0,1,0}, {-1,0,0}, 2.0 },
};

// icosohedron
#define C 0.85065080835203993218
#define A 0.52573111211913360602
struct tagVertex tagIcos[] = {
      { {-A,C,0}, {-A,C,0}, 0.0},      { {-C,0,A}, {-C,0,A}, 1.0},      { {0,A,C}, {0,A,C}, 2.0},
      { {-A,C,0}, {-A,C,0}, 0.0},      { {0,A,C}, {0,A,C}, 1.0},        { {A,C,0}, {A,C,0}, 2.0},
      { {-A,C,0}, {-A,C,0}, 0.0},      { {A,C,0}, {A,C,0}, 1.0},        { {0,A,-C}, {0,A,-C}, 2.0},
      { {-A,C,0}, {-A,C,0}, 0.0},      { {0,A,-C}, {0,A,-C}, 1.0},      { {-C,0,-A}, {-C,0,-A}, 2.0},
      { {-A,C,0}, {-A,C,0}, 0.0},      { {-C,0,-A}, {-C,0,-A}, 1.0},    { {-C,0,A}, {-C,0,A}, 2.0},
      { {A,C,0}, {A,C,0}, 0.0},        { {0,A,C}, {0,A,C}, 1.0},        { {C,0,A}, {C,0,A}, 2.0},
      { {0,A,C}, {0,A,C}, 0.0},        { {-C,0,A}, {-C,0,A}, 1.0},      { {0,-A,C}, {0,-A,C}, 2.0},
      { {-C,0,A}, {-C,0,A}, 0.0},      { {-C,0,-A}, {-C,0,-A}, 1.0},    { {-A,-C,0}, {-A,-C,0}, 2.0},
      { {-C,0,-A}, {-C,0,-A}, 0.0},    { {0,A,-C}, {0,A,-C}, 1.0},      { {0,-A,-C}, {0,-A,-C}, 2.0},
      { {0,A,-C}, {0,A,-C}, 0.0},      { {A,C,0}, {A,C,0}, 1.0},        { {C,0,-A}, {C,0,-A}, 2.0},
      { {A,-C,0}, {A,-C,0}, 0.0},      { {C,0,A}, {C,0,A}, 1.0},        { {0,-A,C}, {0,-A,C}, 2.0},
      { {A,-C,0}, {A,-C,0}, 0.0},      { {0,-A,C}, {0,-A,C}, 1.0},      { {-A,-C,0}, {-A,-C,0}, 2.0},
      { {A,-C,0}, {A,-C,0}, 0.0},      { {-A,-C,0}, {-A,-C,0}, 1.0},    { {0,-A,-C}, {0,-A,-C}, 2.0},
      { {A,-C,0}, {A,-C,0}, 0.0},      { {0,-A,-C}, {0,-A,-C}, 1.0},    { {C,0,-A}, {C,0,-A}, 2.0},
      { {A,-C,0}, {A,-C,0}, 0.0},      { {C,0,-A}, {C,0,-A}, 1.0},      { {C,0,A}, {C,0,A}, 2.0},
      { {0,-A,C}, {0,-A,C}, 0.0},      { {C,0,A}, {C,0,A}, 1.0},        { {0,A,C}, {0,A,C}, 2.0},
      { {-A,-C,0}, {-A,-C,0}, 0.0},    { {0,-A,C}, {0,-A,C}, 1.0},      { {-C,0,A}, {-C,0,A}, 2.0},
      { {0,-A,-C}, {0,-A,-C}, 0.0},    { {-A,-C,0}, {-A,-C,0}, 1.0},    { {-C,0,-A}, {-C,0,-A}, 2.0},
      { {C,0,-A}, {C,0,-A}, 0.0},      { {0,-A,-C}, {0,-A,-C}, 1.0},    { {0,A,-C}, {0,A,-C}, 2.0},
      { {C,0,A}, {C,0,A}, 0.0},        { {C,0,-A}, {C,0,-A}, 1.0},      { {A,C,0}, {A,C,0}, 2.0}
};

// set icosohedron normals for facets
void makeIcosFacets() {
  for (int i = 0; i < 60; i += 3) {
    Vector3 a = Vector3(tagIcos[i].pos[0],tagIcos[i].pos[1],tagIcos[i].pos[2]);
    Vector3 b = Vector3(tagIcos[i+1].pos[0],tagIcos[i+1].pos[1],tagIcos[i+1].pos[2]);
    Vector3 c = Vector3(tagIcos[i+2].pos[0],tagIcos[i+2].pos[1],tagIcos[i+2].pos[2]);
    Vector3 n = (b - a).cross(c - a);
    tagIcos[i].nml[0] = n.x;   tagIcos[i].nml[1] = n.y;   tagIcos[i].nml[2] = n.z;
    tagIcos[i+1].nml[0] = n.x; tagIcos[i+1].nml[1] = n.y; tagIcos[i+1].nml[2] = n.z;
    tagIcos[i+2].nml[0] = n.x; tagIcos[i+2].nml[1] = n.y; tagIcos[i+2].nml[2] = n.z;
  }
}

// shader program data
struct programInfo {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint modelviewUniform;
  GLint projUniform;
  GLint shaderoptionUniform;
};
struct programInfo tagProgram;
struct programInfo currentProgram;
// Vertex Attribute Indices
GLuint positionIndex = 0;
GLuint normalIndex = 1;
GLuint tagIndex = 2;
// Vertex Array and Vertex Buffer Object References
GLuint tetraVAO, tetraVBO;
GLuint icosVAO, icosVBO;

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

void InitTagShader() {
  tagProgram.id = shader_Compile("tag");
  // use layout option in shader so this is unnecessary
//  glBindAttribLocation(tagProgram.id,positionIndex,"position");
//  glBindAttribLocation(tagProgram.id,normalIndex,"normal");
  shader_Link(tagProgram.id);
  tagProgram.lightposUniform = glGetUniformLocation(tagProgram.id,"lightpos");
  tagProgram.materialUniform = glGetUniformLocation(tagProgram.id,"material");
  tagProgram.colorUniform = glGetUniformLocation(tagProgram.id,"color");
  tagProgram.modelviewUniform = glGetUniformLocation(tagProgram.id,"modelview");
  tagProgram.projUniform = glGetUniformLocation(tagProgram.id,"proj");
  tagProgram.shaderoptionUniform = glGetUniformLocation(tagProgram.id,"shaderoption");
  fprintf(stderr,"uniform: %d %d %d %d %d %d\n", tagProgram.lightposUniform, tagProgram.materialUniform, tagProgram.colorUniform, tagProgram.modelviewUniform, tagProgram.projUniform, tagProgram.shaderoptionUniform);
}

void configTagTetraBuffers() {
  glGenVertexArrays(1,&tetraVAO);
  glBindVertexArray(tetraVAO);
  glGenBuffers(1,&tetraVBO);
  glBindBuffer(GL_ARRAY_BUFFER,tetraVBO);
  glBufferData(GL_ARRAY_BUFFER,12 * sizeof(struct tagVertex),tagTetra,GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);
  glVertexAttribPointer(normalIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) (3 * sizeof(GLfloat)));
  glEnableVertexAttribArray(normalIndex);
  glVertexAttribPointer(tagIndex,1,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) (6 * sizeof(GLfloat)));
  glEnableVertexAttribArray(tagIndex);
}

void configTagIcosBuffers() {
  glGenVertexArrays(1,&icosVAO);
  glBindVertexArray(icosVAO);
  glGenBuffers(1,&icosVBO);
  glBindBuffer(GL_ARRAY_BUFFER,icosVBO);
  glBufferData(GL_ARRAY_BUFFER,60 * sizeof(struct tagVertex),tagIcos,GL_STATIC_DRAW);
  glVertexAttribPointer(positionIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) 0);
  glEnableVertexAttribArray(positionIndex);
  glVertexAttribPointer(normalIndex,3,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) (3 * sizeof(GLfloat)));
  glEnableVertexAttribArray(normalIndex);
  glVertexAttribPointer(tagIndex,1,GL_FLOAT,GL_FALSE,sizeof(struct tagVertex),(const GLvoid*) (6 * sizeof(GLfloat)));
  glEnableVertexAttribArray(tagIndex);
}

void draw_Init() {
  InitTagShader();

//  makeIcosFacets();
  configTagTetraBuffers();
  configTagIcosBuffers();

  currentProgram = tagProgram;
}

void draw_Scene(json state) {
  glUseProgram(currentProgram.id);
  glUniform3fv(currentProgram.lightposUniform,1,LightPos);
  glUniform4f(currentProgram.materialUniform, state["ambient"].get<float>(), state["diffuse"].get<float>(), state["specular"].get<float>(), state["specexp"].get<float>());
  glUniformMatrix4fv(currentProgram.projUniform,1,GL_FALSE,projMatrix.get());
  glUniform1i(currentProgram.shaderoptionUniform,state["shaderOption"]);

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-5)) * tbMatrix;
  Matrix4 mvo = mv * mutil_Translate(Vector3(-1,-1,-1));

  glBindVertexArray(tetraVAO);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0.5,0,0)) * mutil_Scale(Vector3(2,0.5,0.5))).get());
  glUniform3fv(currentProgram.colorUniform,1,RedClr);
  glDrawArrays(GL_TRIANGLES,0,12);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0.5,0)) * mutil_Scale(Vector3(0.5,2,0.5))).get());
  glUniform3fv(currentProgram.colorUniform,1,GrenClr);
  glDrawArrays(GL_TRIANGLES,0,12);
  glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0,0.5)) * mutil_Scale(Vector3(0.5,0.5,2))).get());
  glUniform3fv(currentProgram.colorUniform,1,BlueClr);
  glDrawArrays(GL_TRIANGLES,0,12);

  if (state["rotating"]) {
    glBindVertexArray(icosVAO);
    glUniformMatrix4fv(currentProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Rotate(state["angle"].get<int>(),Vector3(0,0,1))).get());
    glUniform3f(currentProgram.colorUniform,0.8,0.8,0.2);
    glDrawArrays(GL_TRIANGLES,0,60);
  }
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

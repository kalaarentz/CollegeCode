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


// shader program data
struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint modelviewUniform;
  GLint projUniform;
  GLint stripewidthUniform;
  GLint stripecntUniform;
  GLint vstripeUniform;
} uvProgram;

struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint usetexUniform;
  GLint texidUniform;
  GLint modelviewUniform;
  GLint projUniform;
} texProgram;

struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint usetexUniform;
  GLint usemattexUniform;
  GLint texidUniform;
  GLint mattexidUniform;
  GLint modelviewUniform;
  GLint projUniform;
} mattexProgram;


// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO chicken;
GLuint chickenTexId;
GLchar * chickenTex = NULL;
#define CHICKEN_TEX_WIDTH 512
#define CHICKEN_TEX_HEIGHT 512
GLuint globeTexId;
GLuint globeMatTexId;
GLchar * globeTex = NULL;
GLchar * globeMatTex = NULL;
#define GLOBE_TEX_WIDTH 1024
#define GLOBE_TEX_HEIGHT 512

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

bool ReadTexture(const char * name, int width, int height, GLchar **tex) {
  FILE *fd;

  if ( (fd = fopen(name,"r")) != 0) {
    if (*tex == NULL) *tex = (char *) malloc(width*height*3);
    int ret = fread(*tex,1,width*height*3,fd);
    fclose(fd);
    return true;
  }
  fprintf(stderr,"texture read failed\n");
  exit(1);
}

void initShaders() {
  uvProgram.id = shader_Compile("uv");
  shader_Link(uvProgram.id);
  uvProgram.lightposUniform = glGetUniformLocation(uvProgram.id,"lightpos");
  uvProgram.materialUniform = glGetUniformLocation(uvProgram.id,"material");
  uvProgram.colorUniform = glGetUniformLocation(uvProgram.id,"color");
  uvProgram.modelviewUniform = glGetUniformLocation(uvProgram.id,"modelview");
  uvProgram.projUniform = glGetUniformLocation(uvProgram.id,"proj");
  uvProgram.stripewidthUniform = glGetUniformLocation(uvProgram.id,"stripewidth");
  uvProgram.stripecntUniform = glGetUniformLocation(uvProgram.id,"stripecnt");
  uvProgram.vstripeUniform = glGetUniformLocation(uvProgram.id,"vstripe");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d\n", uvProgram.lightposUniform, uvProgram.materialUniform, uvProgram.colorUniform, uvProgram.modelviewUniform, uvProgram.projUniform, uvProgram.stripewidthUniform, uvProgram.stripecntUniform, uvProgram.vstripeUniform);

  texProgram.id = shader_Compile("tex");
  shader_Link(texProgram.id);
  texProgram.lightposUniform = glGetUniformLocation(texProgram.id,"lightpos");
  texProgram.materialUniform = glGetUniformLocation(texProgram.id,"material");
  texProgram.colorUniform = glGetUniformLocation(texProgram.id,"color");
  texProgram.usetexUniform = glGetUniformLocation(texProgram.id,"usetex");
  texProgram.texidUniform = glGetUniformLocation(texProgram.id,"texid");
  texProgram.modelviewUniform = glGetUniformLocation(texProgram.id,"modelview");
  texProgram.projUniform = glGetUniformLocation(texProgram.id,"proj");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d\n", texProgram.lightposUniform, texProgram.materialUniform, texProgram.colorUniform, texProgram.modelviewUniform, texProgram.projUniform, texProgram.usetexUniform, texProgram.texidUniform);

  mattexProgram.id = shader_Compile("mattex");
  shader_Link(mattexProgram.id);
  mattexProgram.lightposUniform = glGetUniformLocation(mattexProgram.id,"lightpos");
  mattexProgram.materialUniform = glGetUniformLocation(mattexProgram.id,"material");
  mattexProgram.colorUniform = glGetUniformLocation(mattexProgram.id,"color");
  mattexProgram.usetexUniform = glGetUniformLocation(mattexProgram.id,"usetex");
  mattexProgram.usemattexUniform = glGetUniformLocation(mattexProgram.id,"usemattex");
  mattexProgram.texidUniform = glGetUniformLocation(mattexProgram.id,"texid");
  mattexProgram.mattexidUniform = glGetUniformLocation(mattexProgram.id,"mattexid");
  mattexProgram.modelviewUniform = glGetUniformLocation(mattexProgram.id,"modelview");
  mattexProgram.projUniform = glGetUniformLocation(mattexProgram.id,"proj");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d %d\n", mattexProgram.lightposUniform, mattexProgram.materialUniform, mattexProgram.colorUniform, mattexProgram.modelviewUniform, mattexProgram.projUniform, mattexProgram.usetexUniform, mattexProgram.usemattexUniform, mattexProgram.texidUniform, mattexProgram.mattexidUniform);
}

void draw_Init() {
  initShaders();

  axisTetra = makeVAOForModel(faceCoordTetraTex());

  sphere = makeVAOForModel(faceSphereTex(24,48));
  fprintf(stderr,"sphere %d %u %u\n",sphere.VAO,sphere.vCnt,sphere.iCnt);

  chicken = makeVAOForModel(readOBJArrays("OBJModels/chicken-tex.obj"));
  ReadTexture("Textures/chicken-auv.rgb",CHICKEN_TEX_WIDTH, CHICKEN_TEX_HEIGHT,&chickenTex);
  glGenTextures(1,&chickenTexId);
  glBindTexture(GL_TEXTURE_2D,chickenTexId);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
  glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,CHICKEN_TEX_WIDTH,CHICKEN_TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,chickenTex);
  fprintf(stderr,"chicken %d %u %u\n",chicken.VAO,chicken.vCnt,chicken.iCnt);

  ReadTexture("Textures/1_earth_1k.rgb",GLOBE_TEX_WIDTH,GLOBE_TEX_HEIGHT,&globeTex);
  glGenTextures(1,&globeTexId);
  glBindTexture(GL_TEXTURE_2D,globeTexId);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
  glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,GLOBE_TEX_WIDTH,GLOBE_TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,globeTex);

  ReadTexture("Textures/1_earth_1k_mat.rgb",GLOBE_TEX_WIDTH,GLOBE_TEX_HEIGHT,&globeMatTex);
  glGenTextures(1,&globeMatTexId);
  glBindTexture(GL_TEXTURE_2D,globeMatTexId);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
  glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,GLOBE_TEX_WIDTH,GLOBE_TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,globeMatTex);
}

void draw_Scene(json state) {
  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-5)) * tbMatrix;
  Matrix4 mvo = mv * mutil_Translate(Vector3(-1,-1,-1));

  // draw coordinate frame
  glUseProgram(uvProgram.id);
  glUniform3fv(uvProgram.lightposUniform,1,LightPos);
  glUniform4f(uvProgram.materialUniform, state["ambient"].get<float>(), state["diffuse"].get<float>(), state["specular"].get<float>(), state["specexp"].get<float>());
  glUniformMatrix4fv(uvProgram.projUniform,1,GL_FALSE,projMatrix.get());

  glUniform1f(uvProgram.stripewidthUniform,0.8);
  glUniform1i(uvProgram.stripecntUniform,10);
  glUniform1i(uvProgram.vstripeUniform,0);

  glBindVertexArray(axisTetra.VAO);
  glUniformMatrix4fv(uvProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0.5,0,0)) * mutil_Scale(Vector3(2,0.5,0.5))).get());
  glUniform3fv(uvProgram.colorUniform,1,RedClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(uvProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0.5,0)) * mutil_Scale(Vector3(0.5,2,0.5))).get());
  glUniform3fv(uvProgram.colorUniform,1,GrenClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(uvProgram.modelviewUniform,1,GL_FALSE,(mvo * mutil_Translate(Vector3(0,0,0.5)) * mutil_Scale(Vector3(0.5,0.5,2))).get());
  glUniform3fv(uvProgram.colorUniform,1,BlueClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);

  // draw sphere
  if (state["uvsphere"]) {
    glUniform1f(uvProgram.stripewidthUniform,0.8);
    glUniform1i(uvProgram.stripecntUniform,20);
    glUniform1i(uvProgram.vstripeUniform,1);
    glUniformMatrix4fv(uvProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Rotate(state["angle"],Vector3(0,0,1))).get());
    glUniform3fv(uvProgram.colorUniform,1,PrplClr);
    glBindVertexArray(sphere.VAO);
    glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);
  }

  if (state["chicken"]) {
    glUseProgram(texProgram.id);
    glUniform3fv(texProgram.lightposUniform,1,LightPos);
    glUniform4f(texProgram.materialUniform, state["ambient"].get<float>(), state["diffuse"].get<float>(), state["specular"].get<float>(), state["specexp"].get<float>());
    glUniformMatrix4fv(texProgram.projUniform,1,GL_FALSE,projMatrix.get());
    glUniformMatrix4fv(texProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Rotate(state["angle"],Vector3(0,0,1)) * mutil_Scale(Vector3(0.65,0.65,0.65))).get());
    glUniform1i(texProgram.usetexUniform,1);
    glUniform1i(texProgram.texidUniform,1);
    glUniform3fv(texProgram.colorUniform,1,PrplClr);
    glBindVertexArray(chicken.VAO);
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D,chickenTexId);
    glDrawArrays(GL_TRIANGLES,0,chicken.vCnt);
  }

  if (state["globe"]) {
    glUseProgram(mattexProgram.id);
    glUniform3fv(mattexProgram.lightposUniform,1,LightPos);
    glUniform4f(mattexProgram.materialUniform, state["ambient"].get<float>(), state["diffuse"].get<float>(), state["specular"].get<float>(), state["specexp"].get<float>());
    glUniformMatrix4fv(mattexProgram.projUniform,1,GL_FALSE,projMatrix.get());
    glUniformMatrix4fv(mattexProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Rotate(state["angle"],Vector3(0,0,1))).get());
    glUniform1i(mattexProgram.usetexUniform,1);
    glUniform1i(mattexProgram.usemattexUniform,state["mattex"].get<bool>());
    glUniform1i(mattexProgram.texidUniform,0);
    glUniform1i(mattexProgram.mattexidUniform,1);
    glBindVertexArray(sphere.VAO);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D,globeTexId);
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D,globeMatTexId);
    glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);
  }
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

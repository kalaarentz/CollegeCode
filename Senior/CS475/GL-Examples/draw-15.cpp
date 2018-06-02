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
#ifdef Windows
#define _USE_MATH_DEFINES
#endif
#include <math.h>

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
GLfloat FlatMaterial[4] = {1.0, 0.0, 0.0, 1.0};

// shader program data
struct {
  GLuint id;
  GLint lightposUniform;
  GLint lightmatrixUniform;
  GLint colorUniform;
  GLint materialUniform;
  GLint usespotUniform;
  GLint spotdirUniform;
  GLint usetexUniform;
  GLint texidUniform;
  GLint modelviewUniform;
  GLint projUniform;
} localspotProgram;

Matrix4 projMatrix;
Matrix4 modelviewMatrix;

void initShaders() {
  localspotProgram.id = shader_Compile("localspot");
  shader_Link(localspotProgram.id);
  localspotProgram.lightposUniform = glGetUniformLocation(localspotProgram.id,"lightpos");
  localspotProgram.lightmatrixUniform = glGetUniformLocation(localspotProgram.id,"lightmatrix");
  localspotProgram.colorUniform = glGetUniformLocation(localspotProgram.id,"color");
  localspotProgram.materialUniform = glGetUniformLocation(localspotProgram.id,"material");
  localspotProgram.usespotUniform = glGetUniformLocation(localspotProgram.id,"usespot");
  localspotProgram.spotdirUniform = glGetUniformLocation(localspotProgram.id,"spotdir");
  localspotProgram.usetexUniform = glGetUniformLocation(localspotProgram.id,"usetex");
  localspotProgram.texidUniform = glGetUniformLocation(localspotProgram.id,"texid");
  localspotProgram.modelviewUniform = glGetUniformLocation(localspotProgram.id,"modelview");
  localspotProgram.projUniform = glGetUniformLocation(localspotProgram.id,"proj");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d %d %d\n", localspotProgram.lightposUniform, localspotProgram.lightmatrixUniform, localspotProgram.colorUniform, localspotProgram.materialUniform, localspotProgram.usespotUniform, localspotProgram.spotdirUniform, localspotProgram.usetexUniform, localspotProgram.texidUniform, localspotProgram.modelviewUniform, localspotProgram.projUniform);
}

// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO chicken;
struct faceModelVAO plane;
struct faceModelVAO cone;
GLuint sphereTexId;
GLchar * sphereTex;
#define SPHERE_TEX_WIDTH 1024
#define SPHERE_TEX_HEIGHT 512
GLuint chickenTexId;
GLchar * chickenTex;
#define CHICKEN_TEX_WIDTH 512
#define CHICKEN_TEX_HEIGHT 512

void initSceneObjects() {
  axisTetra = makeVAOForModel(faceCoordTetraTex());
  sphere = makeVAOForModel(faceSphereTex(24,48));
  chicken = makeVAOForModel(readOBJArrays("OBJModels/chicken-tex.obj"));
  plane = makeVAOForModel(facePlane());
  cone = makeVAOForModel(faceCone(16));
  FILE *fd;
  if ( (fd = fopen("Textures/1_earth_1k.rgb","r")) != 0) {
    sphereTex = (char *) malloc(SPHERE_TEX_WIDTH*SPHERE_TEX_HEIGHT*3);
    int ret = fread(sphereTex,1,SPHERE_TEX_WIDTH*SPHERE_TEX_HEIGHT*3,fd);
    fclose(fd);
    glGenTextures(1,&sphereTexId);
    glBindTexture(GL_TEXTURE_2D,sphereTexId);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,SPHERE_TEX_WIDTH,SPHERE_TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,sphereTex);
  }
  else {
    printf("texture read failed\n");
  }

  if ( (fd = fopen("Textures/chicken-auv.rgb","r")) != 0) {
    chickenTex = (char *) malloc(CHICKEN_TEX_WIDTH*CHICKEN_TEX_HEIGHT*3);
    int ret = fread(chickenTex,1,CHICKEN_TEX_WIDTH*CHICKEN_TEX_HEIGHT*3,fd);
    fclose(fd);
    glGenTextures(1,&chickenTexId);
    glBindTexture(GL_TEXTURE_2D,chickenTexId);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,CHICKEN_TEX_WIDTH,CHICKEN_TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,chickenTex);
  }
  else {
    printf("texture read failed\n");
  }

}

void draw_Init() {
  initShaders();
  initSceneObjects();
}

float elapsedTime = 0.0;
Vector3 spotDir;

void draw_Scene(json state) {

  elapsedTime += 0.001;
  LightPos[0] = 2*cos(elapsedTime*M_PI); LightPos[1] = 0; LightPos[2] = 0;
  spotDir = Vector3(cos(5*elapsedTime*M_PI),sin(5*elapsedTime*M_PI),0.0);

  glUseProgram(localspotProgram.id);

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-8)) * tbMatrix;

  glUniform1i(localspotProgram.usespotUniform,1);
  glUniform3f(localspotProgram.spotdirUniform,spotDir[0],spotDir[1],spotDir[2]);

  // draw coordinate frame
  glUniform3fv(localspotProgram.lightposUniform,1,LightPos);
  glUniform4fv(localspotProgram.materialUniform,1,BasicMaterial);
  glUniformMatrix4fv(localspotProgram.lightmatrixUniform,1,GL_FALSE,mv.get());
  glUniformMatrix4fv(localspotProgram.projUniform,1,GL_FALSE,projMatrix.get());

  glBindVertexArray(axisTetra.VAO);
  Matrix4 mvf = mv * mutil_Translate(Vector3(-2,-2,-1));
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mvf * mutil_Translate(Vector3(0.25,0,0)) * mutil_Scale(Vector3(1,0.25,0.25))).get());
  glUniform3fv(localspotProgram.colorUniform,1,RedClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mvf * mutil_Translate(Vector3(0,0.25,0)) * mutil_Scale(Vector3(0.25,1,0.25))).get());
  glUniform3fv(localspotProgram.colorUniform,1,GrenClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mvf * mutil_Translate(Vector3(0,0,0.25)) * mutil_Scale(Vector3(0.25,0.25,1))).get());
  glUniform3fv(localspotProgram.colorUniform,1,BlueClr);
  glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);

  // 2 planes
  glBindVertexArray(plane.VAO);
  glUniform3fv(localspotProgram.colorUniform,1,BrwnClr);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,0,-1)) * mutil_Scale(Vector3(2,2,2))).get());
  glDrawArrays(GL_TRIANGLES,0,plane.vCnt);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(-2,0,.5)) * mutil_Scale(Vector3(2,2,1.5)) * mutil_Rotate(90,Vector3(0,1,0))).get());
  glDrawArrays(GL_TRIANGLES,0,plane.vCnt);

  // two globes
  glBindVertexArray(sphere.VAO);
  glUniform1i(localspotProgram.usetexUniform,1);
  glUniform1i(localspotProgram.texidUniform,0);
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D,sphereTexId);
  glUniform4fv(localspotProgram.materialUniform,1,BasicMaterial);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,1,0)) * mutil_Scale(Vector3(0.3,0.3,0.3))).get());
  glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(0,-1,0)) * mutil_Scale(Vector3(0.3,0.3,0.3))).get());
  glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);

  // light source
  glUniform1i(localspotProgram.usetexUniform,0);
  glUniform4fv(localspotProgram.materialUniform,1,FlatMaterial);
  Matrix4 mvl = mv * mutil_Translate(Vector3(LightPos[0],LightPos[1],LightPos[2]));
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mvl * mutil_Scale(Vector3(0.02,0.02,0.02))).get());
  glUniform3fv(localspotProgram.colorUniform,1,WhitClr);
  glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);

  // light cone
  glBindVertexArray(cone.VAO);
  Vector3 rotaxis = Vector3(0,0,1) + spotDir;
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mvl * mutil_Rotate(180,rotaxis) * mutil_Scale(Vector3(0.08,0.08,0.1)) * mutil_Rotate(180,Vector3(1,0,0)) * mutil_Translate(Vector3(0,0,-1))).get());
  glDrawArrays(GL_TRIANGLES,0,cone.vCnt);

  // chickens
  glBindVertexArray(chicken.VAO);
  glUniform1i(localspotProgram.usetexUniform,1);
  glUniform1i(localspotProgram.texidUniform,0);
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D,chickenTexId);
  glUniform4fv(localspotProgram.materialUniform,1,BasicMaterial);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(-1,0.5,0)) * mutil_Scale(Vector3(0.3,0.3,0.3))).get());
  glDrawArrays(GL_TRIANGLES,0,chicken.vCnt);
  glUniform1i(localspotProgram.usetexUniform,0);
  glUniformMatrix4fv(localspotProgram.modelviewUniform,1,GL_FALSE,(mv * mutil_Translate(Vector3(-1,-0.5,0)) * mutil_Scale(Vector3(0.3,0.3,0.3))).get());
  glUniform3fv(localspotProgram.colorUniform,1,PrplClr);
  glDrawArrays(GL_TRIANGLES,0,chicken.vCnt);
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

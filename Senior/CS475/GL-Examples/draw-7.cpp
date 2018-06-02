#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "OBJModel.h"

#include "draw.h"

#define FRAME_SIZE 10

struct faceModel axisModel;

std::vector<Vector3> cubeVrts;
std::vector<Vector3> cubeNmls;
std::vector<Vector2> cubeUVs;
std::vector<Face3> cubeIndx;

#define TEX_WIDTH 400
#define TEX_HEIGHT 300
unsigned char texMap[TEX_WIDTH*TEX_HEIGHT*3];
GLuint texId;

void draw_Init() {
  // texture
  // T
  // F R R L
  // B

  axisModel = faceCoordTetra();

  // bottom 0,1,2,3
  cubeVrts.push_back(Vector3(0,0,0)); cubeNmls.push_back(Vector3(0,0,-1)); cubeUVs.push_back(Vector2(0,1));
  cubeVrts.push_back(Vector3(0,1,0)); cubeNmls.push_back(Vector3(0,0,-1)); cubeUVs.push_back(Vector2(0.25,1));
  cubeVrts.push_back(Vector3(1,1,0)); cubeNmls.push_back(Vector3(0,0,-1)); cubeUVs.push_back(Vector2(0.25,0.666));
  cubeVrts.push_back(Vector3(1,0,0)); cubeNmls.push_back(Vector3(0,0,-1)); cubeUVs.push_back(Vector2(0,0.666));

  // top 4,5,6,7
  cubeVrts.push_back(Vector3(0,0,1)); cubeNmls.push_back(Vector3(0,0,1)); cubeUVs.push_back(Vector2(0,0));
  cubeVrts.push_back(Vector3(1,0,1)); cubeNmls.push_back(Vector3(0,0,1)); cubeUVs.push_back(Vector2(0.0,0.333));
  cubeVrts.push_back(Vector3(1,1,1)); cubeNmls.push_back(Vector3(0,0,1)); cubeUVs.push_back(Vector2(0.25,0.333));
  cubeVrts.push_back(Vector3(0,1,1)); cubeNmls.push_back(Vector3(0,0,1)); cubeUVs.push_back(Vector2(0.25,0));

  // left 8,9,10,11
  cubeVrts.push_back(Vector3(0,0,0)); cubeNmls.push_back(Vector3(0,-1,0)); cubeUVs.push_back(Vector2(0.75,0.666));
  cubeVrts.push_back(Vector3(1,0,0)); cubeNmls.push_back(Vector3(0,-1,0)); cubeUVs.push_back(Vector2(1,0.666));
  cubeVrts.push_back(Vector3(1,0,1)); cubeNmls.push_back(Vector3(0,-1,0)); cubeUVs.push_back(Vector2(1,0.333));
  cubeVrts.push_back(Vector3(0,0,1)); cubeNmls.push_back(Vector3(0,-1,0)); cubeUVs.push_back(Vector2(0.75,0.333));

  // right 12,13,14,15
  cubeVrts.push_back(Vector3(0,1,0)); cubeNmls.push_back(Vector3(0,1,0)); cubeUVs.push_back(Vector2(0.5,0.666));
  cubeVrts.push_back(Vector3(0,1,1)); cubeNmls.push_back(Vector3(0,1,0)); cubeUVs.push_back(Vector2(0.5,0.333));
  cubeVrts.push_back(Vector3(1,1,1)); cubeNmls.push_back(Vector3(0,1,0)); cubeUVs.push_back(Vector2(0.25,0.333));
  cubeVrts.push_back(Vector3(1,1,0)); cubeNmls.push_back(Vector3(0,1,0)); cubeUVs.push_back(Vector2(0.25,0.666));

  // rear 16,17,18,19
  cubeVrts.push_back(Vector3(0,0,0)); cubeNmls.push_back(Vector3(-1,0,0)); cubeUVs.push_back(Vector2(0.75,0.666));
  cubeVrts.push_back(Vector3(0,0,1)); cubeNmls.push_back(Vector3(-1,0,0)); cubeUVs.push_back(Vector2(0.75,0.333));
  cubeVrts.push_back(Vector3(0,1,1)); cubeNmls.push_back(Vector3(-1,0,0)); cubeUVs.push_back(Vector2(0.5,0.333));
  cubeVrts.push_back(Vector3(0,1,0)); cubeNmls.push_back(Vector3(-1,0,0)); cubeUVs.push_back(Vector2(0.5,0.666));

  // front 20,21,22,23
  cubeVrts.push_back(Vector3(1,0,0)); cubeNmls.push_back(Vector3(1,0,0)); cubeUVs.push_back(Vector2(0,0.666));
  cubeVrts.push_back(Vector3(1,1,0)); cubeNmls.push_back(Vector3(1,0,0)); cubeUVs.push_back(Vector2(0.25,0.666));
  cubeVrts.push_back(Vector3(1,1,1)); cubeNmls.push_back(Vector3(1,0,0)); cubeUVs.push_back(Vector2(0.25,0.333));
  cubeVrts.push_back(Vector3(1,0,1)); cubeNmls.push_back(Vector3(1,0,0)); cubeUVs.push_back(Vector2(0,0.333));

  // faces
  cubeIndx.push_back(Face3(0,1,2)); cubeIndx.push_back(Face3(2,3,0));
  cubeIndx.push_back(Face3(4,5,6)); cubeIndx.push_back(Face3(6,7,4));
  cubeIndx.push_back(Face3(8,9,10)); cubeIndx.push_back(Face3(10,11,8));
  cubeIndx.push_back(Face3(12,13,14)); cubeIndx.push_back(Face3(14,15,12));
  cubeIndx.push_back(Face3(16,17,18)); cubeIndx.push_back(Face3(18,19,16));
  cubeIndx.push_back(Face3(20,21,22)); cubeIndx.push_back(Face3(22,23,20));

  FILE * fd = fopen("Textures/CubeTexture.rgb","r");
  if (fd) {
    fread(texMap,1,TEX_WIDTH*TEX_HEIGHT*3,fd);
    fclose(fd);
    glGenTextures(1,&texId);
    glBindTexture(GL_TEXTURE_2D,texId);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,TEX_WIDTH,TEX_HEIGHT,0,GL_RGB,GL_UNSIGNED_BYTE,texMap);
  }
  else {
    fprintf(stderr,"failed to open CubeTexture.rgb");
  }
}

void drawCube() {
  glEnable(GL_TEXTURE_2D);
  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3,GL_FLOAT,0,(GLfloat *)cubeVrts.data());
  glEnableClientState(GL_NORMAL_ARRAY);
  glNormalPointer(GL_FLOAT,0,(GLfloat *)cubeNmls.data());
  glEnableClientState(GL_TEXTURE_COORD_ARRAY);
  glTexCoordPointer(2,GL_FLOAT,0,(GLfloat *)cubeUVs.data());
  glDrawElements(GL_TRIANGLES,3*cubeIndx.size(),GL_UNSIGNED_INT,(GLint *)cubeIndx.data());
  glDisableClientState(GL_NORMAL_ARRAY);
  glDisableClientState(GL_VERTEX_ARRAY);
  glDisable(GL_TEXTURE_2D);
}

void drawCoordFrame(float size) {
  glPushMatrix();
  glColor3f(1,0.2,0.2);
  glScalef(size,size/10.0,size/10.0);
  axisModel.draw();
  glPopMatrix();

  glPushMatrix();
  glColor3f(0.2,1,0.2);
  glScalef(size/10.0,size,size/10.0);
  axisModel.draw();
  glPopMatrix();

  glPushMatrix();
  glColor3f(0.2,0.2,1);
  glScalef(size/10.0,size/10.0,size);
  axisModel.draw();
  glPopMatrix();
}

void draw_Scene(json state) {
  glPushMatrix();
  glTranslatef(-5,-5,-5);
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();

  glPushMatrix();
  glColor3f(1,1,1);
  glScalef(3,3,3);
  glRotatef(state["angle"],0,0,1);
  glTranslatef(-0.5,-0.5,-0.5);
  drawCube();
  glPopMatrix();

}

void draw_Resize(int width, int height) {
}

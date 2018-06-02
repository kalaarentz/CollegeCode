#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "draw.h"

#define FRAME_SIZE 10

struct faceModel axisModel;
struct faceModel originPoint;

void draw_Init() {
  axisModel = faceCoordTetra();
  originPoint = refineFaceModel(faceIcosahedron());
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

void performTransform(char t) {
  switch (t) {
    case 'x':
      glTranslatef(-1.0,0.0,0.0);
      break;
    case 'X':
      glTranslatef(1.0,0.0,0.0);
      break;
    case 'y':
      glTranslatef(0.0,-1.0,0.0);
      break;
    case 'Y':
      glTranslatef(0.0,1.0,0.0);
      break;
    case 'z':
      glTranslatef(0.0,0.0,-1.0);
      break;
    case 'Z':
      glTranslatef(0.0,0.0,1.0);
      break;
    case 'a':
      glRotatef(-30.0,1.0,0.0,0.0);
      break;
    case 'A':
      glRotatef(30.0,1.0,0.0,0.0);
      break;
    case 'b':
      glRotatef(-30.0,0.0,1.0,0.0);
      break;
    case 'B':
      glRotatef(30.0,0.0,1.0,0.0);
      break;
    case 'c':
      glRotatef(-30.0,0.0,0.0,1.0);
      break;
    case 'C':
      glRotatef(30.0,0.0,0.0,1.0);
      break;
    case 's':
      glScalef(0.5,0.5,0.5);
      break;
    case 'S':
      glScalef(2,2,2);
      break;
  }
}

void performTransforms(json state) {
  std::string transforms = state["transforms"];
//  fprintf(stdout,"trans: %s\n",transforms.c_str());
  if (state["evalOrder"] == 0) {
    for (int i = 0; i < transforms.length(); i++) performTransform(transforms[i]);
  }
  else {
    for (int i = transforms.length()-1; i >= 0; i--) performTransform(transforms[i]);
  }
}

void displayMatrix(json state, GLfloat * m) {
  std::string transforms = state["transforms"];
  if (state["evalOrder"] == 1) reverse(transforms.begin(),transforms.end());
  fprintf(stdout,"trans: %s\n",transforms.c_str());

  fprintf(stdout,"%+6.3f %+6.3f %+6.3f %+6.3f\n",m[0],m[4],m[8],m[12]);
  fprintf(stdout,"%+6.3f %+6.3f %+6.3f %+6.3f\n",m[1],m[5],m[9],m[13]);
  fprintf(stdout,"%+6.3f %+6.3f %+6.3f %+6.3f\n",m[2],m[6],m[10],m[14]);
  fprintf(stdout,"%+6.3f %+6.3f %+6.3f %+6.3f\n",m[3],m[7],m[11],m[15]);
}

void draw_Scene(json state) {
  glPushMatrix();
  glTranslatef(-5.0,-5.0,-5.0);
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();

  glPushMatrix();
  glScalef(0.2,0.2,0.2);
  glColor3f(1,1,-0.2);
  originPoint.draw();
  glPopMatrix();

  glPushMatrix();

  // matrix stack currently has trackball rotations on it
  // need to perform transformation on clean matrix so we
  // can display it separate from trackball, once we have it
  // pop and multiply by transform matrix
  glPushMatrix();
  glLoadIdentity();
  performTransforms(state);
  GLfloat m[16];
  glGetFloatv(GL_MODELVIEW_MATRIX,m);
  if (state["displayMatrix"]) displayMatrix(state,m);
  glPopMatrix();
  glMultMatrixf(m);

  drawCoordFrame(FRAME_SIZE/2.0);
  glPopMatrix();
}

void draw_Resize(int width, int height) {
}

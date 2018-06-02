#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "OBJModel.h"

#include "draw.h"

#define FRAME_SIZE 7

struct faceModel axisModel;
struct faceModel planeModel;
struct faceModel chickenModel;

void draw_Init() {
  axisModel = faceCoordTetra();
  chickenModel = readOBJArrays("OBJModels/chicken.obj");
  planeModel = facePlane();
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

void drawCoordinatePlanes(float scale) {
  glPushMatrix();
  glScalef(scale,scale,scale);
  planeModel.draw();
  glRotatef(90,0,1,0);
  planeModel.draw();
  glRotatef(90,1,0,0);
  planeModel.draw();
  glPopMatrix();
}

void draw_Scene(json state) {
  glPushMatrix();
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();

  glPushMatrix();
  if (state["circle"]) glRotatef(state["angle"],0,0,1);
  glColor3f(1,1,0.2);
  glTranslatef(3,3,1.9);
  if (state["pirouette"]) glRotatef(state["angle"],0,0,1);
  glScalef(1.5,1.5,1.5);
  chickenModel.draw();
  glPopMatrix();

  glPushMatrix();
  glRotatef(180,0,0,1);
  if (state["circle"]) glRotatef(state["angle"],0,0,1);
  glColor3f(1,1,0.2);
  glTranslatef(3,3,1.9);
  if (state["pirouette"]) glRotatef(state["angle"],0,0,1);
  glScalef(1.5,1.5,1.5);
  glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
  chickenModel.draw();
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  glPopMatrix();

  glDisable(GL_LIGHTING);
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  glColor4f(1.0,1.0,1.0,0.1);
  drawCoordinatePlanes(FRAME_SIZE);
  glDisable(GL_BLEND);
  glEnable(GL_LIGHTING);
}

void draw_Resize(int width, int height) {
}

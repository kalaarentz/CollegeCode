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
struct faceModel objectModel;

void draw_Init() {
  axisModel = faceCoordTetra();
  objectModel = readOBJArrays("OBJModels/Thing1.obj");
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
  glRotatef(state["angle"],0,0,1);
  glScalef(2,2,2);

  glPushMatrix();
  glColor3f(.8,.8,0);
  objectModel.draw();
  glPopMatrix();

  glPushMatrix();
  glDisable(GL_LIGHTING);
  glColor3f(.8,0,.8);
  glLineWidth(5.0);
  glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
  glEnable(GL_POLYGON_OFFSET_LINE);
  glPolygonOffset(state["offsetFactor"],state["offsetUnits"]);
  objectModel.draw();
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  glDisable(GL_POLYGON_OFFSET_LINE);
  glEnable(GL_LIGHTING);
  glPopMatrix();

  glPopMatrix();

}

void draw_Resize(int width, int height) {
}

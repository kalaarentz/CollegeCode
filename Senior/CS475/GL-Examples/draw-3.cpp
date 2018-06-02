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
struct faceModel tetrahedronModel;
struct lineModel tetrahedronVertexNormalModel;
struct lineModel tetrahedronFaceNormalModel;
struct faceModel coneModel;
struct faceModel sphereModel;
struct lineModel sphereVertexNormalModel;
struct lineModel sphereFaceNormalModel;

void draw_Init() {
  axisModel = faceCoordTetra();
  tetrahedronModel = faceTetrahedron();
  tetrahedronVertexNormalModel = makeVertexNormalModel(tetrahedronModel,1/10.0);
  tetrahedronFaceNormalModel = makeFaceNormalModel(tetrahedronModel,1/10.0);
  coneModel = faceCone(15);
  sphereModel = refineFaceModel(faceIcosahedron());
  sphereVertexNormalModel = makeVertexNormalModel(sphereModel,1/10.0);
  sphereVertexNormalModel.lineWidth = 3.0;
  sphereFaceNormalModel = makeFaceNormalModel(sphereModel,1/10.0);
  sphereFaceNormalModel.lineWidth = 3.0;
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

void drawAntiFrame(float size) {
  glPushMatrix();
  glRotatef(-90,0,1,0);
  glColor3f(1,0.2,0.2);
  glScalef(size/10.0,size/10.0,size);
  coneModel.draw();
  glPopMatrix();

  glPushMatrix();
  glRotatef(90,1,0,0);
  glColor3f(0.2,1,0.2);
  glScalef(size/10.0,size/10.0,size);
  coneModel.draw();
  glPopMatrix();

  glPushMatrix();
  glRotatef(180,1,0,0);
  glColor3f(0.2,0.2,1);
  glScalef(size/10.0,size/10.0,size);
  coneModel.draw();
  glPopMatrix();
}

void draw_Scene(json state) {
  glPushMatrix();
  glTranslatef(-5.0,-5.0,-5.0);
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();
  glPushMatrix();
  glTranslatef(5.0,5.0,5.0);
  drawAntiFrame(FRAME_SIZE);
  glPopMatrix();
  if (state["displayTetra"]) {
    glPushMatrix();
    glColor3f(1,0.2,1);
    glScalef(5,5,5);
    glRotatef(state["angle"],0,0,1);
    tetrahedronModel.draw();
    glDisable(GL_LIGHTING);
    glColor3f(0.5,0.5,1);
    tetrahedronVertexNormalModel.draw();
    glColor3f(0.5,1,0.5);
    tetrahedronFaceNormalModel.draw();
    glEnable(GL_LIGHTING);
    glPopMatrix();
  }
  if (state["displaySphere"]) {
    glPushMatrix();
    glScalef(5,5,5);
    glRotatef(state["angle"],0,0,1);
    glColor3f(1,1,0.2);
    sphereModel.draw();
    glDisable(GL_LIGHTING);
    glColor3f(0.5,0.5,1);
    sphereVertexNormalModel.draw();
    glColor3f(0.5,1,0.5);
    sphereFaceNormalModel.draw();
    glEnable(GL_LIGHTING);
    glPopMatrix();
  }
}

void draw_Resize(int width, int height) {
}

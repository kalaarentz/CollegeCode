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
struct faceModel coneModel;
struct lineModel coneNormalModel;
struct faceModel coneFacetModel;
struct lineModel coneFacetNormalModel;
struct faceModel torusModel;

void draw_Init() {
  axisModel = faceCoordTetra();
  coneModel = faceCone(10);
  coneNormalModel = makeVertexNormalModel(coneModel,1/5.0);
  coneNormalModel.mode = GL_LINES;
  coneNormalModel.lineWidth = 1;
  coneFacetModel = faceFacetCone(10);
  coneFacetNormalModel = makeVertexNormalModel(coneFacetModel,1/5.0);
  coneFacetNormalModel.mode = GL_LINES;
  coneFacetNormalModel.lineWidth = 1;
  torusModel = faceTorus(3,10,0.5,10);
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
  glTranslatef(-5.0,-5.0,-5.0);
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();

  glPushMatrix();
  glColor3f(1,0.2,1);
  glScalef(3,3,3);
  glRotatef(state["angle"],0,0,1);
  if (state["displayCone"]) {
    coneModel.draw();
    glColor3f(1,1,1);
    coneNormalModel.draw();
  }
  if (state["displayFacetCone"]) {
    coneFacetModel.draw();
    glColor3f(1,1,1);
    coneFacetNormalModel.draw();
  }
  glPopMatrix();

  if (state["displayTorus"]) {
    glPushMatrix();
    glTranslatef(0,0,-5);
    glColor3f(1,1,-.2);
    torusModel.draw();
    glPopMatrix();
  }
}

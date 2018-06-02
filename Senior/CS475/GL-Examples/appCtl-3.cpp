#include <stdio.h>
#include <stdbool.h>
#include "platformGL.h"

#include "appCtl.h"
#include "draw.h"
#include "trackball.h"
#include "colors.h"
#include "model.h"
#include "stdModels.h"
#include "json.hpp"

json state = json({});

/* define lighing model data */
GLfloat lightPos[] = { 1.0, 1.0, 1.0, 0.0};
GLfloat lightClr[] = { 1.0, 1.0, 1.0, 1.0};
GLfloat lightAmb[] = { 0.25, 0.25, 0.25};

int WinWidth, WinHeight;

bool isTracking = false;

struct faceModel trackingSphere;

void app_Init(int width, int height, int argc, char *argv[]) {
  tb_Init();

  glClearDepth(1.0);
  glEnable(GL_DEPTH_TEST);
  glClearColor(.2,.2,.2,1.0);
  glShadeModel(GL_SMOOTH);

  glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
  glLightfv(GL_LIGHT0, GL_DIFFUSE, lightClr);
  glEnable(GL_LIGHT0);
  glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lightAmb);
  glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, 1);
  glEnable(GL_LIGHTING);
  glColorMaterial(GL_FRONT_AND_BACK,GL_AMBIENT_AND_DIFFUSE);
  glEnable(GL_COLOR_MATERIAL);
  glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, colors[whiteColor]);
  glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 10.0);
  glEnable(GL_NORMALIZE);

  app_FrameResize(width,height);

  state["displayTetra"] = false;
  state["displaySphere"] = false;
  state["rotating"] = false;
  state["angle"] = 0;
  state["wireframeTrackball"] = false;

  trackingSphere = refineFaceModel(faceIcosahedron());

  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 3", false, false};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glPushMatrix();
  Matrix4 M;
  tb_Matrix(M);
  glMultMatrixf((GLfloat *)M.get());

  if (state["rotating"]) state["angle"] = (float) state["angle"] + 1;

  draw_Scene(state);

  if (isTracking) {
    glPushMatrix();
    glScalef(12,12,12);
    glColor4f(0.5,0.5,0.8,0.3);
    glEnable(GL_CULL_FACE);
//    glCullFace(GL_FRONT);
    glCullFace(GL_BACK);
    if (state["wireframeTrackball"]) glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    trackingSphere.draw();
    if (state["wireframeTrackball"]) glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
    glDisable(GL_CULL_FACE);
    glPopMatrix();
  }

  glPopMatrix();
}

void app_FrameResize(int width, int height) {
  WinWidth = width;
  WinHeight = height;
  float aspect = width / (float) height;
  glViewport(0,0,width,height);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
//  glFrustum(-1.0*aspect,1.0*aspect,-1.0,1.0,10.0,200.0);
//  glTranslatef(0.0,0.0,-150.0);
  glFrustum(-1.0*aspect,1.0*aspect,-1.0,1.0,2.0,50.0);
  glTranslatef(0.0,0.0,-25.0);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
}

#define UNITIZE(v,m) ( 2 * ((v / (float) m) - 0.5) )

void app_LeftMouseDown(double x, double y) {
  isTracking = true;
  tb_BeginMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
}

void app_LeftMouseMotion(double x, double y) {
  tb_ContinueMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
}

void app_LeftMouseUp(double x, double y) {
  isTracking = false;
}

void app_LeftMouseClick(double x, double y) {

}

void app_MouseMotion(double x, double y) {

}

void app_KeyEvent(int event) {
  switch (event) {
    case 't':
      state["displayTetra"] = ! state["displayTetra"];
      break;
    case 's':
      state["displaySphere"] = ! state["displaySphere"];
      break;
    case 'r':
      state["rotating"] = ! state["rotating"];
      break;
    case 'w':
      state["wireframeTrackball"] = ! state["wireframeTrackball"];
      break;
    default:
      break;
  }
}

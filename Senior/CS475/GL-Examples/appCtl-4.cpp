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

#define MAX_TRANSFORMS 32

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

  state["transforms"] = "";
  state["evalOrder"] = 0;
  state["displayMatrix"] = true;

  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 4", false, false};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glPushMatrix();
  Matrix4 M;
  tb_Matrix(M);
  glMultMatrixf((GLfloat *)M.get());

  draw_Scene(state);

  state["displayMatrix"] = false;

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
  tb_BeginMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
}

void app_LeftMouseMotion(double x, double y) {
  tb_ContinueMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
}

void app_LeftMouseUp(double x, double y) {
}

void app_LeftMouseClick(double x, double y) {

}

void app_MouseMotion(double x, double y) {

}

void app_KeyEvent(int key) {
  switch (key) {
    case ' ':
      state["transforms"] = "";
      break;
    case 'l':
      state["evalOrder"] = 0;
      break;
    case 'r':
      state["evalOrder"] = 1;
      break;
    case 'x':
    case 'X':
    case 'y':
    case 'Y':
    case 'z':
    case 'Z':
    case 'a':
    case 'A':
    case 'b':
    case 'B':
    case 'c':
    case 'C':
    case 's':
    case 'S': {
      std::string s = state["transforms"];
      if (s.length() < MAX_TRANSFORMS) s.push_back(key);
      state["transforms"] = s;
    } break;
    case 'R':
      tb_Reset();
      break;
    default:
      break;
  }
  state["displayMatrix"] = true;
}

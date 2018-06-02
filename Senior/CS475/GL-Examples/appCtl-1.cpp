#include <stdio.h>
#include <stdbool.h>
#include "platformGL.h"

#include "appCtl.h"
#include "draw.h"
#include "trackball.h"
#include "colors.h"

json state = json({});

int WinWidth, WinHeight;

bool isTracking = false;

void app_Init(int width, int height, int argc, char *argv[]) {
  tb_Init();

  glClearDepth(1.0);
  glEnable(GL_DEPTH_TEST);
  glClearColor(.2,.2,.2,1.0);
  glShadeModel(GL_SMOOTH);

  app_FrameResize(width,height);

  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 1", false, false};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glPushMatrix();
  Matrix4 M;
  tb_Matrix(M);
  glMultMatrixf((GLfloat *)M.get());

  draw_Scene(state);

  glPopMatrix();
}

void app_FrameResize(int width, int height) {
  WinWidth = width;
  WinHeight = height;
  float aspect = width / (float) height;
  glViewport(0,0,width,height);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glFrustum(-1.0*aspect,1.0*aspect,-1.0,1.0,10.0,200.0);
  glTranslatef(0.0,0.0,-150.0);

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
    default:
      break;
  }
}

#include <stdio.h>
#include <stdbool.h>
#include "platformGL.h"

#include "appCtl.h"
#include "draw.h"
#include "trackball.h"
#include "json.hpp"
#include "scene.h"
#include "matrixUtil.h"

#define CLAMP(v,l,u) ( (v < l) ? (l) : ((v > u) ? (u) : (v)) )

json state = json({});

/* define lighing model data */
GLfloat lightPos[] = { 1.0, 1.0, 1.0, 0.0};
GLfloat lightClr[] = { 1.0, 1.0, 1.0, 1.0};
GLfloat lightAmb[] = { 0.25, 0.25, 0.25};

int WinWidth, WinHeight;

void app_Init(int width, int height, int argc, char *argv[]) {
  tb_Init();

  glClearDepth(1.0);
  glEnable(GL_DEPTH_TEST);
  glDepthFunc(GL_LEQUAL);
  glClearColor(.2,.2,.2,1.0);
  glEnable(GL_CULL_FACE);

  app_FrameResize(width,height);

  state["showVertexNormals"] = true;
  state["showFaceNormals"] = true;
  state["picking"] = false;
  state["innerviewer"] = false;
  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 19", true, false};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  draw_Scene(state);
  state.erase("command");
}

void app_FrameResize(int width, int height) {
  WinWidth = width;
  WinHeight = height;
  float aspect = width / (float) height;
  glViewport(0,0,width,height);
  draw_Resize(width,height);
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
  state["picking"] = true;
  state["pickingX"] = x;
  state["pickingY"] = y;
  draw_Scene(state);
  state["picking"] = false;
}

void app_MouseMotion(double x, double y) {
}

void app_KeyEvent(int event) {
  switch (event) {
    case 'a':
      state["command"] = 'a';
      break;
    case 'c':
      state["command"] = 'c';
      break;
    case 'f':
      state["showFaceNormals"] = ! state["showFaceNormals"];
      break;
    case 'i':
      state["innerviewer"] = !state["innerviewer"];
      break;
    case 'l':
      state["command"] = 'l';
      break;
    case 'p':
      state["command"] = 'p';
      break;
    case 's':
      state["command"] = 's';
      break;
    case 't':
      state["command"] = 't';
      break;
    case 'v':
      state["showVertexNormals"] = ! state["showVertexNormals"];
      break;
    default:
      break;
  }
}

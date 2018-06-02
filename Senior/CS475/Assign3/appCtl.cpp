#include <stdio.h>
#include <stdbool.h>
#include "platformGL.h"

#include "appCtl.h"
#include "draw.h"
#include "trackball.h"
#include "json.hpp"

#define CLAMP(v,l,u) ( (v < l) ? (l) : ((v > u) ? (u) : (v)) )

json state = json({});

/* define lighing model data */
GLfloat lightPos[] = { 1.0, 1.0, 1.0, 0.0};
GLfloat lightClr[] = { 1.0, 1.0, 1.0, 1.0};
GLfloat lightAmb[] = { 0.25, 0.25, 0.25};
GLfloat BasicMaterial[4] = {0.2, 0.7, 1.0, 50.0};

int WinWidth, WinHeight;

void app_Init(int width, int height, int argc, char *argv[]) {
  tb_Init();

  glClearDepth(1.0);
  glEnable(GL_DEPTH_TEST);
  glClearColor(.2,.2,.2,1.0);

  app_FrameResize(width,height);

  state["rotating"] = false;
  state["angle"] = 0;
  state["chicken"] = false;
  state["globe"] = false;
  state["uvsphere"] = false;
  state["mattex"] = false;
  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 12", true};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  state["ambient"] = BasicMaterial[0];
  state["diffuse"] = BasicMaterial[1];
  state["specular"] = BasicMaterial[2];
  state["specexp"] = BasicMaterial[3];
  if (state["rotating"]) state["angle"] = (float) state["angle"] + 1;

  draw_Scene(state);
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
}

void app_KeyEvent(int event) {
  switch (event) {
    case 'r':
      state["rotating"] = ! state["rotating"];
      break;
    case 'A':
      BasicMaterial[0] = CLAMP(BasicMaterial[0] + 0.1, 0, 1);
      break;
    case 'a':
      BasicMaterial[0] = CLAMP(BasicMaterial[0] - 0.1, 0, 1);
      break;
    case 'D':
      BasicMaterial[1] = CLAMP(BasicMaterial[1] + 0.1, 0, 1);
      break;
    case 'd':
      BasicMaterial[1] = CLAMP(BasicMaterial[1] - 0.1, 0, 1);
      break;
    case 'S':
      BasicMaterial[2] = CLAMP(BasicMaterial[2] + 0.1, 0, 1);
      break;
    case 's':
      BasicMaterial[2] = CLAMP(BasicMaterial[2] - 0.1, 0, 1);
      break;
    case 'E':
      BasicMaterial[3] = CLAMP(BasicMaterial[3] + 1, 1, 100);
      break;
    case 'e':
      BasicMaterial[3] = CLAMP(BasicMaterial[3] - 1, 1, 100);
      break;
    case 'c':
      state["chicken"] = ! state["chicken"];
      break;
    case 'g':
      state["globe"] = ! state["globe"];
      break;
    case 'u':
      state["uvsphere"] = ! state["uvsphere"];
      break;
    case 'm':
      state["mattex"] = ! state["mattex"];
      break;
    default:
      break;
  }
}

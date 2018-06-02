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
  glClearColor(.2,.2,.2,1.0);

  app_FrameResize(width,height);

  state["selectmode"] = false;
  state["targetmode"] = false;
  state["mousex"] = -1.0;
  state["mousey"] = -1.0;
  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 13", true, true};
  return attr;
}

void app_Draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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
  fprintf(stderr,"left mouse click\n");
  state["selectmode"] = true;
  float clearClr[4];
  glGetFloatv(GL_COLOR_CLEAR_VALUE,clearClr);
  glClearColor(0,0,0,1);
  app_Draw();
  glClearColor(clearClr[0],clearClr[1],clearClr[2],clearClr[3]);
  unsigned char pixel[4];
  glReadPixels(x,WinHeight - y,1,1,GL_RGB,GL_UNSIGNED_BYTE,pixel);
  int tag = pixel[0];
  fprintf(stderr,"select %d\n",tag);
  state["selectmode"] = false;
  struct drawable& drawable = scene_DrawableWithID(tag);
  if (drawable.selectTag == tag) drawable.selected = ! drawable.selected;
}

void app_MouseMotion(double x, double y) {
//  fprintf(stderr,"mouse motion %f %f\n",x,y);
  if (x < 0 || x > WinWidth || y < 0 || y > WinHeight) return;
  state["mousex"] = UNITIZE(x,WinWidth);
  state["mousey"] = -UNITIZE(y,WinHeight);
}

void translateSelected(Vector3 v) {
  for (int i = 0; i < scene.size(); i++) {
    if (scene[i].selected) {
      scene[i].transforms = scene[i].transforms * mutil_Translate(v);
    }
  }
}

void app_KeyEvent(int event) {
  switch (event) {
    case ' ':
      state["selectmode"] = ! state["selectmode"];
      break;
    case 't':
      state["targetmode"] = ! state["targetmode"];
      break;
    case 'X':
      translateSelected(Vector3(0.1,0,0));
      break;
    case 'x':
      translateSelected(Vector3(-0.1,0,0));
      break;
    case 'Y':
      translateSelected(Vector3(0,0.1,0));
      break;
    case 'y':
      translateSelected(Vector3(0,-0.1,0));
      break;
    case 'Z':
      translateSelected(Vector3(0,0,0.1));
      break;
    case 'z':
      translateSelected(Vector3(0,0,-0.1));
      break;
    default:
      break;
  }
}

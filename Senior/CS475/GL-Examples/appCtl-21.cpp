#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <unistd.h>

#include "platformGL.h"

#include "appCtl.h"
#include "draw.h"
#include "trackball.h"
#include "json.hpp"
#include "scene.h"
#include "matrixUtil.h"

#define SQR(x) ( (x) * (x) )
#define CLAMP(v,l,u) ( (v < l) ? (l) : ((v > u) ? (u) : (v)) )

json state = json({});

/* define lighing model data */
GLfloat lightPos[] = { 1.0, 1.0, 1.0, 0.0};
GLfloat lightClr[] = { 1.0, 1.0, 1.0, 1.0};
GLfloat lightAmb[] = { 0.25, 0.25, 0.25};

int WinWidth, WinHeight;
bool moveMode = false;

void usage() {
  fprintf(stderr,"usage\n./ex-21 -x 3 -y 3\n./ex-21 -x 3 -y 3 -b 0.5\n./ex-21 -f -1,1,-1,1\n");
  exit(1);
}

void app_Init(int width, int height, int argc, char *argv[]) {
  tb_Init();

  glClearDepth(1.0);
  glEnable(GL_DEPTH_TEST);
  glClearColor(.2,.2,.2,1.0);

  app_FrameResize(width,height);

  state["selectmode"] = false;

  state["viewports"] = {{0,0}};
  state["viewportFromFrustum"] = false;

  float bezelFraction = 0.95;
  int horzViewportCnt = 1;
  int vertViewportCnt = 1;

  bool haveGrid = false;
  bool haveFrustum = false;

  opterr = 0;
  int c;
  while ((c = getopt(argc,argv,"hx:y:b:f:")) != -1) {
    switch(c) {
      case 'h':
        usage();
        break;
      case 'x':
        horzViewportCnt = atoi(optarg);
        haveGrid = true;
        break;
      case 'y':
        vertViewportCnt = atoi(optarg);
        haveGrid = true;
        break;
      case 'b':
        bezelFraction = atof(optarg);
        haveGrid = true;
        break;
      case 'f': {
        float l,r,b,t;
        int cnt;
        cnt = sscanf(optarg,"%f,%f,%f,%f",&l,&r,&b,&t);
        if (cnt != 4) usage();
        state["frustum"] = {l,r,b,t};
        state["viewport"] = {0,0,1,1};
        haveFrustum = true;
        }break;
      case '?':
        usage();
        break;
    }
  }

  if ((haveGrid && haveFrustum) || (!haveGrid && !haveFrustum)) usage();

  if (haveGrid) {
    if (horzViewportCnt < 0 || horzViewportCnt > 6 || vertViewportCnt < 0 || vertViewportCnt > 6) usage();

    if (horzViewportCnt > 1 || vertViewportCnt > 1) {
      state["viewports"].clear();
      for (int i = 0; i < horzViewportCnt; i++) {
        for (int j = 0; j < vertViewportCnt; j++) {
          float x = i / (float) horzViewportCnt;
          float y = j / (float) vertViewportCnt;
          state["viewports"].push_back({x,y});
        }
      }
    }
    // for (int i = 0; i < state["viewports"].size(); i++) {
    //   fprintf(stderr,"viewport %d  %f,%f\n",i,state["viewports"][i][0].get<double>(),state["viewports"][i][1].get<double>());
    // }

    if (bezelFraction < 0.1 || bezelFraction > 1.0) usage();
    state["viewportWidth"] = 1 / (float) horzViewportCnt * bezelFraction;
    state["viewportHeight"] = 1 / (float) vertViewportCnt * bezelFraction;
  }

  draw_Init();
}

struct appAttr app_Attributes() {
  struct appAttr attr = {"Ex 21", true, true};
  return attr;
}

void app_Draw() {
  glViewport(0,0,WinWidth,WinHeight);
  glScissor(0,0,WinWidth,WinHeight);
  glClearColor(0,0,0,1);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  draw_Scene(state);
}

void app_FrameResize(int width, int height) {
  WinWidth = width;
  WinHeight = height;
  float aspect = width / (float) height;
//  glViewport(0,0,width,height);
  draw_Resize(width,height);
}

#define UNITIZE(v,m) ( 2 * ((v / (float) m) - 0.5) )

int targetFrame;

void app_LeftMouseDown(double x, double y) {
  if (!moveMode) {
    tb_BeginMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
  }
  else {
    GLfloat winX = x / (float) WinWidth;
    GLfloat winY = 1.0 - y / (float) WinHeight;
    int mini = state["viewports"].size();
    float mind = 2.0;
    for (int i = 0; i < state["viewports"].size(); i++) {
      float d = SQR(winX - state["viewports"][i][0].get<double>()) + SQR(winY - state["viewports"][i][1].get<double>());
      if (d < mind) {
        mini = i;
        mind = d;
      }
    }
    targetFrame = mini;
  }
}

void app_LeftMouseMotion(double x, double y) {
  if (!moveMode) {
    tb_ContinueMovement(UNITIZE(x,WinWidth),-UNITIZE(y,WinHeight));
  }
  else {
    fprintf(stderr,"pre %f,%f\n",state["viewports"][targetFrame][0].get<double>(),state["viewports"][targetFrame][1].get<double>());
    state["viewports"][targetFrame][0] = x / (float) WinWidth;
    state["viewports"][targetFrame][1] = 1 - y / (float) WinHeight;
    fprintf(stderr,"post %f,%f\n",state["viewports"][targetFrame][0].get<double>(),state["viewports"][targetFrame][1].get<double>());
  }
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
    case 'l':
      if (state.find("frustum") != state.end()) state["frustum"][0] = state["frustum"][0].get<double>() - 0.1;
      break;
    case 'L':
      if (state.find("frustum") != state.end()) state["frustum"][0] = state["frustum"][0].get<double>() + 0.1;
      break;
    case 'r':
      if (state.find("frustum") != state.end()) state["frustum"][1] = state["frustum"][1].get<double>() - 0.1;
      break;
    case 'R':
      if (state.find("frustum") != state.end()) state["frustum"][1] = state["frustum"][1].get<double>() + 0.1;
      break;
    case 'b':
      if (state.find("frustum") != state.end()) state["frustum"][2] = state["frustum"][2].get<double>() - 0.1;
      break;
    case 'B':
      if (state.find("frustum") != state.end()) state["frustum"][2] = state["frustum"][2].get<double>() + 0.1;
      break;
    case 't':
      if (state.find("frustum") != state.end()) state["frustum"][3] = state["frustum"][3].get<double>() - 0.1;
      break;
    case 'T':
      if (state.find("frustum") != state.end()) state["frustum"][3] = state["frustum"][3].get<double>() + 0.1;
      break;
    case 'm':
      moveMode = !moveMode;
      break;
    case 'v':
      state["viewportFromFrustum"] = ! state["viewportFromFrustum"];
      break;
    case ' ':
      state["selectmode"] = ! state["selectmode"];
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

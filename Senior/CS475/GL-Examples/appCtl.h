#ifndef APP_CTL_H
#define APP_CTL_H

#include <map>
#include <string>

#include "json.hpp"
using json = nlohmann::json;

#include <stdbool.h>

extern json state;
extern int WinWidth, WinHeight;

struct appAttr {
  const char * title;
  bool coreProfile;
  bool wantsMouseTracking;
};

void    app_Init(int width, int height, int argc, char *argv[]);
struct appAttr app_Attributes();
void    app_Draw();
void    app_FrameResize(int width, int height);
void    app_LeftMouseDown(double x, double y);
void    app_LeftMouseMotion(double x, double y);
void    app_LeftMouseUp(double x, double y);
void    app_LeftMouseClick(double x, double y);
void    app_MouseMotion(double x, double y);
void    app_KeyEvent(int key);

#endif

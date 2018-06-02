#ifndef APP_CTL_H
#define APP_CTL_H

#include <map>
#include <string>

#include "json.hpp"
using json = nlohmann::json;

#include <stdbool.h>

//typedef std::map<std::string,std::string> dict;

void    app_Init(int width, int height);
const char *  app_Title();
void    app_Draw();
void    app_FrameResize(int width, int height);
void    app_LeftMouseDown(double x, double y);
void    app_LeftMouseMotion(double x, double y);
void    app_LeftMouseUp(double x, double y);
void    app_KeyEvent(int key);

#endif

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "platformGL.h"
#include "platformSDL.h"

#include "appCtl.h"

//#define GL_PROFILE SDL_GL_CONTEXT_PROFILE_CORE
#define GL_PROFILE SDL_GL_CONTEXT_PROFILE_COMPATIBILITY

bool running = true;
bool trackingMouse = false;

SDL_Window * init() {
  float winWidth = 300.0;
  float winHeight = 300.0;

  if (SDL_Init(SDL_INIT_VIDEO) < 0) {
    fprintf(stderr,"init failed\n");
    exit(-1);
  }

  SDL_version version;
  SDL_GetVersion(&version);
  fprintf(stderr,"SDL %d.%d.%d\n",version.major,version.minor,version.patch);

  SDL_Window * window = SDL_CreateWindow(app_Title(),SDL_WINDOWPOS_CENTERED,SDL_WINDOWPOS_CENTERED,winWidth,winHeight,SDL_WINDOW_OPENGL | SDL_WINDOW_RESIZABLE);
  if (!window) {
    fprintf(stderr,"SDL_CreateWindow failed: %s\n",SDL_GetError());
    return NULL;
  }

  SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,GL_PROFILE);
  //  SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,3);
  //  SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION,2);
  SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE,16);
  SDL_GL_SetAttribute(SDL_GL_DOUBLEBUFFER, 1);

  SDL_GLContext context = SDL_GL_CreateContext(window);

  SDL_GL_SetSwapInterval(1);

  fprintf(stderr,"gl: %s\n",glGetString(GL_VERSION));

  int r, g, b, d;
  SDL_GL_GetAttribute(SDL_GL_RED_SIZE,&r);
  SDL_GL_GetAttribute(SDL_GL_GREEN_SIZE,&g);
  SDL_GL_GetAttribute(SDL_GL_BLUE_SIZE,&b);
  SDL_GL_GetAttribute(SDL_GL_DEPTH_SIZE,&d);
  fprintf(stderr,"Buffer Sizes (r,g,b,d) %d,%d,%d,%d\n",r,g,b,d);

  app_Init(winWidth,winHeight);

  return window;
}

void handleMouseMotion(double xpos, double ypos) {
//  fprintf(stderr,"%lf,%lf\n",xpos,ypos);
  if (trackingMouse) app_LeftMouseMotion(xpos,ypos);
}

void handleWindowResize(int width, int height) {
//  fprintf(stderr,"resize %d %d\n",width,height);
  app_FrameResize(width,height);
}

void handleMouseButton(int button, int action) {
//  fprintf(stderr,"button %d, %d\n",button,action);
  if (button != SDL_BUTTON_LEFT) return;
  int x, y;
  switch (action) {
    case 0:
      trackingMouse = false;
      SDL_GetMouseState(&x,&y);
      app_LeftMouseUp(x,y);
      break;
    case 1:
      trackingMouse = true;
      SDL_GetMouseState(&x,&y);
      app_LeftMouseDown(x,y);
      break;
  }
}

const char * keys  = "abcdefghijklmnopqrstuvwxyz1234567890-=[]\\;',./ ";
const char * upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}|:\"<>? ";

void handleKeyPress(int key, int mod, int action) {
//  fprintf(stderr,"key %d %d %d\n",key,mod,action);
  // only process pressed printable keys, shift to upper case
  const char * p = strchr(keys,key);
  if (action == SDL_PRESSED && p) {
    if (mod & (KMOD_LSHIFT | KMOD_RSHIFT | KMOD_CAPS)) {
      key = upper[p - keys];
    }
    app_KeyEvent(key);
  }
}

void pollEvents() {
  SDL_Event event;
  while (SDL_PollEvent(&event)) {
    switch (event.type) {
      case SDL_QUIT:
        running = false;
        break;
      case SDL_WINDOWEVENT:
        switch (event.window.event) {
          case SDL_WINDOWEVENT_RESIZED:
            handleWindowResize(event.window.data1,event.window.data2);
            break;
          default:
            break;
        }
        break;
      case SDL_MOUSEBUTTONDOWN:
      case SDL_MOUSEBUTTONUP:
        handleMouseButton(event.button.button,event.button.state);
        break;
      case SDL_MOUSEMOTION:
        handleMouseMotion(event.motion.x,event.motion.y);
        break;
      case SDL_KEYDOWN:
      case SDL_KEYUP:
        switch (event.key.keysym.sym) {
          case SDLK_ESCAPE:
            running = false;
            break;
          default:
            handleKeyPress(event.key.keysym.sym,event.key.keysym.mod,event.key.state);
          break;
        }
        break;
      default:
        break;
    }
  }
}

void eventLoop(SDL_Window * window) {
  while (running) {
    app_Draw();
    SDL_GL_SwapWindow(window);
    pollEvents();
  }
}

int main(int argc, char * argv[]) {
  SDL_Window * window = init();
  eventLoop(window);

  SDL_DestroyWindow(window);
  SDL_Quit();

  return 0;
}

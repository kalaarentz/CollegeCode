#ifndef PLATFORM_GL_H
#define GL_H

#ifdef Darwin
#include <OpenGL/gl.h>
#include <OpenGL/gl3.h>
#include <OpenGL/gl3ext.h>
#endif

#ifdef Linux
#include <GL/gl.h>
#include <GL/glext.h>
#endif

#ifdef Windows
#include <SDL_opengl.h>
#endif

#endif

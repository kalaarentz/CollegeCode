#ifndef PLATFORM_GL_H
#define GL_H

#ifdef Darwin
#include <OpenGL/gl.h>
#include <OpenGL/gl3.h>
#include <OpenGL/gl3ext.h>
#define GLEW_INIT
#endif

#ifdef Linux
#include <GL/gl.h>
#include <GL/glext.h>
#define GLEW_INIT
#endif

#ifdef Windows
#include <GL/glew.h>
#include <SDL_opengl.h>
#define GLEW_INIT fprintf(stderr,"glewInit %d\n",glewInit());
#endif

#endif

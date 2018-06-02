#ifndef SHADER_UTIL_H
#define SHADER_UTIL_H
#include "platformGL.h"

GLuint shader_Compile(const char * basename);
void   shader_Link(GLuint program);

#endif

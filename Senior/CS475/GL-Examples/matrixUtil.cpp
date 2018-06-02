#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#ifdef Windows
#define _USE_MATH_DEFINES
#endif
#include <math.h>

#include "platformGL.h"
#include "Vectors.h"
#include "MatrixUtil.h"



Matrix4 mutil_PerspectiveMatrix(GLfloat fov, GLfloat aspect, GLfloat nearZ, GLfloat farZ) {
  GLfloat f = 1 / tan(fov * M_PI / 360.0);
  Matrix4 m = Matrix4();
  m[0] = f / aspect;
  m[5] = f;
  m[10] = (farZ + nearZ) / (nearZ - farZ);
  m[11] = -1;
  m[14] = (2 * farZ * nearZ) / (nearZ - farZ);
  m[15] = 0;
  return m;
}

Matrix4 mutil_OrthographicMatrix(GLfloat left, GLfloat right, GLfloat bottom, GLfloat top, GLfloat nearZ, GLfloat farZ) {
  Matrix4 m = Matrix4();
  m[0] = 2.0 / (right - left);
  m[5] = 2.0 / (top - bottom);
  m[10] = -2.0 / (farZ - nearZ);
  m[12] = - (right + left) / (right - left);
  m[13] = - (top + bottom) / (top - bottom);
  m[14] =  - (farZ + nearZ) / (farZ - nearZ);
  m[15] = 1.0;
  return m;
}

Matrix4 mutil_Frustum(GLfloat left, GLfloat right, GLfloat bottom, GLfloat top, GLfloat nearZ, GLfloat farZ) {
  Matrix4 m = Matrix4();
  m[0] = 2 * nearZ / (right - left);
  m[5] = 2 * nearZ / (top - bottom);
  m[8] = (right + left) / (right - left);
  m[9] = (top + bottom) / (top - bottom);
  m[10] = - (farZ + nearZ) / (farZ - nearZ);
  m[11] = -1;
  m[14] = -2 * farZ * nearZ / (farZ - nearZ);
  m[15] = 0;
  return m;
}

Matrix4 mutil_Translate(Vector3 t) {
  Matrix4 m = Matrix4();
  m[12] = t.x;
  m[13] = t.y;
  m[14] = t.z;

  return m;
}

Matrix4 mutil_Scale(Vector3 s) {
  Matrix4 m = Matrix4();
  m[0] = s.x;
  m[5] = s.y;
  m[10] = s.z;

  return m;
}

Matrix4 mutil_Rotate(GLfloat angle, Vector3 axis) {
  Matrix4 m = Matrix4();
  GLfloat theta = angle / 180.0 * M_PI;
  GLfloat c = cos(theta);
  GLfloat s = sin(theta);
  axis.normalize();
  GLfloat x = axis[0];
  GLfloat y = axis[1];
  GLfloat z = axis[2];

  m[0] = x*x*(1-c) + c;
  m[1] = y*x*(1-c) + z*s;
  m[2] = x*z*(1-c) - y*s;
  m[3] = 0.0;
  m[4] = x*y*(1-c) - z*s;
  m[5] = y*y*(1-c) + c;
  m[6] = y*z*(1-c) + x*s;
  m[7] = 0.0;
  m[8] = x*z*(1-c) + y*s;
  m[9] = y*z*(1-c) - x*s;
  m[10] = z*z*(1-c) + c;
  m[11] = 0.0;
  m[12] = 0.0;
  m[13] = 0.0;
  m[14] = 0.0;
  m[15] = 1.0;

  return m;
}

Matrix4 mutil_ForFrame(Vector3 X, Vector3 Y, Vector3 Z) {
  Matrix4 m = Matrix4();
  m[0] = X.x;
  m[1] = X.y;
  m[2] = X.z;
  m[4] = Y.x;
  m[5] = Y.y;
  m[6] = Y.z;
  m[8] = Z.x;
  m[9] = Z.y;
  m[10] = Z.z;
  return m;
}

void mutil_Print4x4(int cond, const char * name, Matrix4 mat) {
  if (!cond) return;

  fprintf(stderr,"%s:\n%f %f %f %f\n%f %f %f %f\n%f %f %f %f\n%f %f %f %f\n",
         name,
         mat[0],mat[4],mat[8],mat[12],
         mat[1],mat[5],mat[9],mat[13],
         mat[2],mat[6],mat[10],mat[14],
         mat[3],mat[7],mat[11],mat[15]);
}
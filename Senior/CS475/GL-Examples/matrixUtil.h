#ifndef MATRIX_UTIL_H
#define MATRIX_UTIL_H

#include "Matrices.h"

Matrix4 mutil_PerspectiveMatrix(GLfloat fov, GLfloat aspect, GLfloat nearZ, GLfloat farZ);
Matrix4 mutil_OrthographicMatrix(GLfloat left, GLfloat right, GLfloat bottom, GLfloat top, GLfloat nearZ, GLfloat farZ);
Matrix4 mutil_Frustum(GLfloat left, GLfloat right, GLfloat bottom, GLfloat top, GLfloat nearZ, GLfloat farZ);

Matrix4 mutil_Translate(Vector3 t);
Matrix4 mutil_Scale(Vector3 s);
Matrix4 mutil_Rotate(GLfloat angle, Vector3 axis);
Matrix4 mutil_ForFrame(Vector3 X, Vector3 Y, Vector3 Z);

void    mutil_Print4x4(int cond, const char * name, Matrix4 mat);

#endif

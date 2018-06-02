#ifndef MATRIX_UTIL_H
#define MATRIX_UTIL_H

#include "Matrices.h"

Matrix4 mutil_PerspectiveMatrix(GLfloat fov, GLfloat aspect, GLfloat nearZ, GLfloat farZ);
Matrix4 mutil_OrthographicMatrix(GLfloat left, GLfloat right, GLfloat bottom, GLfloat top, GLfloat nearZ, GLfloat farZ);

Matrix4 mutil_Translate(Vector3 t);
Matrix4 mutil_Scale(Vector3 s);
Matrix4 mutil_Rotate(GLfloat angle, Vector3 axis);

#endif

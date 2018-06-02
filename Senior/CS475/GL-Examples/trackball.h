#ifndef TRACKBALL_H
#define TRACKBALL_H

#include <stdbool.h>

#include "Vectors.h"
#include "Matrices.h"

void    tb_Init();
void    tb_BeginMovement(float x, float y);
void    tb_ContinueMovement(float x, float y);
void    tb_Reset();
void    tb_Matrix(Matrix4& M);
Matrix4 tb_MatrixForQuat(Vector4 q);
Vector4 tb_Quat();

void tb_Test();

// internal
Vector4 motionQuat(float x, float y);
Vector4 combineQuats(Vector4 q1, Vector4 q2);


#endif

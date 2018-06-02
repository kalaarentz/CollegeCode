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

void tb_Test();

#endif

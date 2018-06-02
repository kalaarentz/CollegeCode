#pragma once

#include "Matrices.h"

extern float backgroundColor[];

void draw_Init();
void draw_Scene(Matrix4 pMat,Matrix4 vMat);
void draw_Shadow();
void draw_Update();
void draw_UpdateDevicePose(unsigned int device, Matrix4 mat);
void draw_UpdateDeviceState(unsigned int device, bool trigger);

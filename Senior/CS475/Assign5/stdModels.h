#ifndef STD_MODELS_H
#define STD_MODELS_H

#include "model.h"

struct faceModel faceCoordTetra();
struct faceModel faceCoordTetraTex();
struct faceModel faceTetrahedron();
struct faceModel faceCone(int faceCnt);
struct faceModel faceIcosahedron();
struct faceModel facePlane();
struct faceModel facePlaneTex();
struct faceModel faceSphereTex(int latCnt, int lonCnt);

#endif

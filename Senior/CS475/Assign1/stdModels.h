#ifndef STD_MODELS_H
#define STD_MODELS_H

#include "model.h"

struct faceModel faceCoordTetra();
struct faceModel faceTetrahedron();
struct faceModel faceCone(int faceCnt);
struct faceModel faceFacetCone(int faceCnt);
struct faceModel faceTorus(float majorRadius, int majorCnt, float minorRadius, int minorCnt);
struct faceModel faceIcosahedron();

#endif

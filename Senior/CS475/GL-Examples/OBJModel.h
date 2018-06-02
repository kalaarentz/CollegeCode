#ifndef OBJMODEL_H
#define OBJMODEL_H

#include "model.h"

struct faceModel readOBJArrays(const char * filename);
struct faceModel readOBJElements(const char * filename);
struct faceModel createUVs(struct faceModel model);

#endif

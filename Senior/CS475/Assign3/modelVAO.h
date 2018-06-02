#ifndef MODELVAO_H
#define MODELVAO_H

#include "Vectors.h"
#include "platformGL.h"
#include "model.h"

#define POSITION_INDEX 0
#define NORMAL_INDEX 1
#define UVS_INDEX 2

struct faceModelVAO {
  GLuint VAO;
  GLuint VBO[2];
  GLuint vCnt;
  GLuint iCnt;
  std::vector<GLfloat> attr;
  std::vector<GLuint> indx;
};

struct faceModelVAO makeVAOForModel(struct faceModel model);

#endif

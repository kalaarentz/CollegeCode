#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "modelVAO.h"


void reportFailure(const char * msg) {
  fprintf(stderr,"makeVAOForModel failed: %s\n",msg);
  exit(1);
}

struct faceModelVAO makeVAOForModel(struct faceModel model) {
  struct faceModelVAO newmodel = faceModelVAO();
  // check sanity of model
  int vrtsSize = model.vrts.size();
  int nmlsSize = model.nmls.size();
  int uvsSize = model.uvs.size();
  int indxSize = model.indx.size();
  if (vrtsSize == 0) reportFailure("no vertices");
  if (vrtsSize != model.nmls.size()) reportFailure("vertices, normals unequal in size");
  if (uvsSize > 0 && uvsSize != vrtsSize) reportFailure("uvs, vertices unequal in size");
  //if (indxSize > 0) reportFailure("draw elements model not yet implemented");
  // convert to single array attributes
  for (int i = 0; i < vrtsSize; i++) {
    newmodel.attr.push_back(model.vrts[i].x);
    newmodel.attr.push_back(model.vrts[i].y);
    newmodel.attr.push_back(model.vrts[i].z);
    newmodel.attr.push_back(model.nmls[i].x);
    newmodel.attr.push_back(model.nmls[i].y);
    newmodel.attr.push_back(model.nmls[i].z);
    if (uvsSize > 0) {
      newmodel.attr.push_back(model.uvs[i].x);
      newmodel.attr.push_back(model.uvs[i].y);
    }
  }

  newmodel.vCnt = vrtsSize;
  newmodel.stride = (uvsSize > 0) ? (8) : (6);

  // copy Indices
  for (int i = 0; i < indxSize; i++) {
    newmodel.indx.push_back(model.indx[i].p);
    newmodel.indx.push_back(model.indx[i].q);
    newmodel.indx.push_back(model.indx[i].r);
  }
  newmodel.iCnt = indxSize * 3;
  // create VAO and VBOs
  glGenVertexArrays(1,&(newmodel.VAO));
  glBindVertexArray(newmodel.VAO);
  glGenBuffers(1,&(newmodel.VBO[0]));
  glBindBuffer(GL_ARRAY_BUFFER,newmodel.VBO[0]);
  glBufferData(GL_ARRAY_BUFFER,newmodel.attr.size() * sizeof(GLfloat),newmodel.attr.data(),GL_STATIC_DRAW);
  glVertexAttribPointer(POSITION_INDEX,3,GL_FLOAT,GL_FALSE,newmodel.stride*sizeof(GLfloat),(const GLvoid*)0);
  glEnableVertexAttribArray(POSITION_INDEX);
  glVertexAttribPointer(NORMAL_INDEX,3,GL_FLOAT,GL_FALSE,newmodel.stride*sizeof(GLfloat),(const GLvoid*)(3*sizeof(GLfloat)));
  glEnableVertexAttribArray(NORMAL_INDEX);
  if (uvsSize > 0) {
    glVertexAttribPointer(UVS_INDEX,3,GL_FLOAT,GL_FALSE,newmodel.stride*sizeof(GLfloat),(const GLvoid*)(6*sizeof(GLfloat)));
    glEnableVertexAttribArray(UVS_INDEX);
  }
  if (indxSize > 0) {
    glGenBuffers(1,&(newmodel.VBO[1]));
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,newmodel.VBO[1]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,newmodel.indx.size() * sizeof(GLuint),newmodel.indx.data(),GL_STATIC_DRAW);
    fprintf(stderr,"makeVAO %u %ld\n",newmodel.VBO[1],newmodel.indx.size());
  }
  return newmodel;
}

Vector3 makeV3(GLfloat * p) {
  Vector3 v = Vector3(p[0],p[1],p[2]);
  return v;
}

GLfloat * positionFor(struct faceModelVAO * model, int vertex) {
  return &(model->attr[vertex * model->stride]);
}

GLfloat * normalFor(struct faceModelVAO * model, int vertex) {
  return &(model->attr[vertex * model->stride + 3]);
}

GLfloat * uvsFor(struct faceModelVAO * model, int vertex) {
  return &(model->attr[vertex * model->stride + 6]);
}

GLuint * faceFor(struct faceModelVAO * model, int face) {
  return &(model->indx[face*3]);
}

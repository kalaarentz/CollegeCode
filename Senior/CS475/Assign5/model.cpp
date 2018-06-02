#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <map>

typedef std::map<int,int> edgeTable;

#include "model.h"

void lineModel::draw() {
  glEnableClientState(GL_VERTEX_ARRAY);
  glLineWidth(this->lineWidth);
  glVertexPointer(3,GL_FLOAT,0,this->vrts.data());
  glDrawArrays(this->mode,0,this->vrts.size());
  glDisableClientState(GL_VERTEX_ARRAY);
}

void faceModel::draw() {
  int vcnt = this->vrts.size();
  if (vcnt == 0) return;
  int icnt = this->indx.size();

  GLfloat * vrts = (GLfloat *) this->vrts.data();
  GLfloat * nmls = (GLfloat *) this->nmls.data();

  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3,GL_FLOAT,0,vrts);
  glEnableClientState(GL_NORMAL_ARRAY);
  glNormalPointer(GL_FLOAT,0,nmls);
  if (icnt > 0) {
    glDrawElements(GL_TRIANGLES,3*icnt,GL_UNSIGNED_INT,(GLint *)this->indx.data());
  }
  else {
    glDrawArrays(GL_TRIANGLES,0,vcnt);
  }
  glDisableClientState(GL_NORMAL_ARRAY);
  glDisableClientState(GL_VERTEX_ARRAY);
}

struct lineModel makeVertexNormalModel(struct faceModel model, float scale) {
  struct lineModel normalModel = lineModel();
  for (int i = 0; i < model.vrts.size(); i++) {
    Vector3 v = model.vrts[i];
    Vector3 n = model.nmls[i];
    n.normalize();
    normalModel.vrts.push_back(v);
    normalModel.vrts.push_back(v + scale * n);
  }
  normalModel.mode = GL_LINES;
  return normalModel;
}

struct lineModel makeFaceNormalModel(struct faceModel model, float scale) {
  struct lineModel normalModel = lineModel();
  // faces may be specified with or without indices, handle both cases
  if (model.indx.size() == 0) {
    for (int i = 0; i < model.vrts.size(); i += 3) {
      Vector3 A = model.vrts[i];
      Vector3 B = model.vrts[i+1];
      Vector3 C = model.vrts[i+2];
      Vector3 faceCenter = (A + B + C) / 3.0;
      Vector3 faceNormal = (B - A).cross(C - B);
      faceNormal.normalize();
      normalModel.vrts.push_back(faceCenter);
      normalModel.vrts.push_back(faceCenter + scale * faceNormal);
    }
  }
  else {
    for (int i = 0; i < model.indx.size(); i++) {
      Vector3 A = model.vrts[model.indx[i].p];
      Vector3 B = model.vrts[model.indx[i].q];
      Vector3 C = model.vrts[model.indx[i].r];
      Vector3 faceCenter = (A + B + C) / 3.0;
      Vector3 faceNormal = (B - A).cross(C - A);
      faceNormal.normalize();
      normalModel.vrts.push_back(faceCenter);
      normalModel.vrts.push_back(faceCenter + scale * faceNormal);
    }
  }
  normalModel.mode = GL_LINES;
  return normalModel;
}

int vertexForEdge(struct faceModel& model, edgeTable& edges, int A, int B) {
  if (A > B) { int t = A; A = B; B = t; }
  int abKey = 0.5 * (A + B + 1) * (A + B) + B; // pairing function
   if (edges.find(abKey) == edges.end()) {
     Vector3 AB = (model.vrts[A] + model.vrts[B]) / 2.0;
     AB.normalize();
     int iAB = model.vrts.size();
     model.vrts.push_back(AB);
     edges[abKey] = iAB;
   }
   return edges[abKey];
}

struct faceModel refineFaceModel(struct faceModel model) {
    // triangle A/B/C is replaced by A/AB/CA, AB/B/BC, CA/BC/C, AB/BC/CA
    // assumes vrts on unit sphere so vrts are their own nmls
    // for general manifold need way to position edge vertices and est nmls
    if (model.indx.size() == 0) return model;

    fprintf(stderr,"model %ld %ld\n",model.vrts.size(),model.indx.size());
    struct faceModel newModel = faceModel();
    for (int v = 0; v < model.vrts.size(); v++) newModel.vrts.push_back(model.vrts[v]);

    edgeTable edges;

    for (int f = 0; f < model.indx.size(); f++) {
      int iA = model.indx[f].p;
      int iB = model.indx[f].q;
      int iC = model.indx[f].r;
      int iAB = vertexForEdge(newModel,edges,iA,iB);
      int iBC = vertexForEdge(newModel,edges,iB,iC);
      int iCA = vertexForEdge(newModel,edges,iC,iA);
      newModel.indx.push_back(Face3(iA,iAB,iCA));
      newModel.indx.push_back(Face3(iAB,iB,iBC));
      newModel.indx.push_back(Face3(iCA,iBC,iC));
      newModel.indx.push_back(Face3(iAB,iBC,iCA));
    }

    for (int i = 0; i < newModel.vrts.size(); i++) {
      Vector3 n = newModel.vrts[i];
      newModel.nmls.push_back(n.normalize());
    }

    return newModel;
}

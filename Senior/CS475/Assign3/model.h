#ifndef MODEL_H
#define MODEL_H

#include "platformGL.h"
#include "Vectors.h"

#include <vector>

struct Edge {
  GLint a;
  GLint b;
  Edge() : a(-1), b(-1) {};
  Edge(int a, int b) : a(a), b(b) {};
};

struct Face3 {
  GLint p;
  GLint q;
  GLint r;
  Face3() : p(-1), q(-1), r(-1) {};
  Face3(int p, int q, int r) : p(p), q(q), r(r) {};
};

struct lineModel {
  int mode;
  float lineWidth;
  std::vector<Vector3> vrts;

  void draw();
};

struct faceModel {
  std::vector<Vector3> vrts;
  std::vector<Vector3> nmls;
  std::vector<Vector2> uvs;
  std::vector<Face3> indx;

  void draw();
};

struct lineModel makeVertexNormalModel(struct faceModel model, float scale);
struct lineModel makeFaceNormalModel(struct faceModel model, float scale);
struct faceModel refineFaceModel(struct faceModel model);

#endif

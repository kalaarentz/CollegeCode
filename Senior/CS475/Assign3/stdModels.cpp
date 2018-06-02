#include <stdio.h>
#include <stdlib.h>
#ifdef Windows
#define _USE_MATH_DEFINES
#endif
#include <math.h>

#include <map>
#include <vector>

#include "Vectors.h"
#include "stdModels.h"

struct faceModel faceCoordTetra() {
  struct faceModel model = faceModel();
  model.vrts.push_back(Vector3(0,0,0)); model.nmls.push_back(Vector3(0,0,-1));
  model.vrts.push_back(Vector3(0,1,0)); model.nmls.push_back(Vector3(0,0,-1));
  model.vrts.push_back(Vector3(1,0,0)); model.nmls.push_back(Vector3(0,0,-1));
  model.vrts.push_back(Vector3(0,0,0)); model.nmls.push_back(Vector3(-1,0,0));
  model.vrts.push_back(Vector3(0,0,1)); model.nmls.push_back(Vector3(-1,0,0));
  model.vrts.push_back(Vector3(0,1,0)); model.nmls.push_back(Vector3(-1,0,0));
  model.vrts.push_back(Vector3(0,0,0)); model.nmls.push_back(Vector3(0,-1,0));
  model.vrts.push_back(Vector3(1,0,0)); model.nmls.push_back(Vector3(0,-1,0));
  model.vrts.push_back(Vector3(0,0,1)); model.nmls.push_back(Vector3(0,-1,0));
  model.vrts.push_back(Vector3(1,0,0)); model.nmls.push_back(Vector3(1,1,1));
  model.vrts.push_back(Vector3(0,1,0)); model.nmls.push_back(Vector3(1,1,1));
  model.vrts.push_back(Vector3(0,0,1)); model.nmls.push_back(Vector3(1,1,1));
  return model;
}

struct faceModel faceCoordTetraTex() {
  struct faceModel model = faceCoordTetra();
  model.uvs.push_back(Vector2(0,0));
  model.uvs.push_back(Vector2(0,1));
  model.uvs.push_back(Vector2(1,0));
  model.uvs.push_back(Vector2(0,0));
  model.uvs.push_back(Vector2(0,1));
  model.uvs.push_back(Vector2(1,0));
  model.uvs.push_back(Vector2(0,0));
  model.uvs.push_back(Vector2(0,1));
  model.uvs.push_back(Vector2(1,0));
  model.uvs.push_back(Vector2(0,0));
  model.uvs.push_back(Vector2(0,1));
  model.uvs.push_back(Vector2(1,0));
  return model;
}

struct faceModel faceTetrahedron() {
  struct faceModel model = faceModel();
  //    Z                          v4
  //    |__Y                     / |  \
  //   /                      v3 --|-- v2
  //  X                          \ |  /
  //                              v1
  Vector3 v1 = Vector3(sqrt(8/9.0),0,-1/3.0);
  Vector3 v2 = Vector3(-sqrt(2/9.0),sqrt(2/3.0),-1/3.0);
  Vector3 v3 = Vector3(-sqrt(2/9.0),-sqrt(2/3.0),-1/3.0);
  Vector3 v4 = Vector3(0,0,1);

  //
  model.vrts.push_back(v1); model.nmls.push_back(-v4);
  model.vrts.push_back(v3); model.nmls.push_back(-v4);
  model.vrts.push_back(v2); model.nmls.push_back(-v4);
  //
  model.vrts.push_back(v1); model.nmls.push_back(-v3);
  model.vrts.push_back(v2); model.nmls.push_back(-v3);
  model.vrts.push_back(v4); model.nmls.push_back(-v3);
  //
  model.vrts.push_back(v2); model.nmls.push_back(-v1);
  model.vrts.push_back(v3); model.nmls.push_back(-v1);
  model.vrts.push_back(v4); model.nmls.push_back(-v1);
  //
  model.vrts.push_back(v3); model.nmls.push_back(-v2);
  model.vrts.push_back(v1); model.nmls.push_back(-v2);
  model.vrts.push_back(v4); model.nmls.push_back(-v2);
  return model;
}

struct faceModel faceCone(int faceCnt) {
  struct faceModel model = faceModel();
  for (int i = 0; i < faceCnt; i++) {
    float angle = 2 * M_PI * i / (float) faceCnt;
    float nangle = 2 * M_PI * (i+1) / (float) faceCnt;
    model.nmls.push_back(Vector3(cos(angle) / M_SQRT2, sin(angle) / M_SQRT2, 1.0  / M_SQRT2));
    model.vrts.push_back(Vector3(cos(angle),sin(angle), 0.0));
    model.nmls.push_back(Vector3(cos(nangle) / M_SQRT2, sin(nangle) / M_SQRT2, 1.0  / M_SQRT2));
    model.vrts.push_back(Vector3(cos(nangle),sin(nangle), 0.0));
    model.nmls.push_back(Vector3(cos((angle + nangle)/2.0) / M_SQRT2, sin((angle + nangle)/2.0) / M_SQRT2, 1.0  / M_SQRT2));
    model.vrts.push_back(Vector3(0.0, 0.0, 1.0));
  }
  return model;
}

struct faceModel faceIcosahedron() {
  // gives icosohedron with vertices on sphere of radius 2 sin(2pi/5) and edge length 2
  // vertex direction is normal direction
  struct faceModel model = faceModel();
  float c = (1.0 + sqrt(5.0)) / 2.0;
  float m = sqrt(1.0 + c*c);
  float a = 1 / m;
  c = c / m;

  model.vrts.push_back(Vector3(-a,c,0)); model.vrts.push_back(Vector3(a,c,0)); model.vrts.push_back(Vector3(-a,-c,0));
  model.vrts.push_back(Vector3(a,-c,0)); model.vrts.push_back(Vector3(0,-a,c)); model.vrts.push_back(Vector3(0,a,c));
  model.vrts.push_back(Vector3(0,-a,-c)); model.vrts.push_back(Vector3(0,a,-c)); model.vrts.push_back(Vector3(c,0,-a));
  model.vrts.push_back(Vector3(c,0,a)); model.vrts.push_back(Vector3(-c,0,-a)); model.vrts.push_back(Vector3(-c,0,a));

  for (int i = 0; i < model.vrts.size(); i++) {
    Vector3 n = model.vrts[i];
    model.nmls.push_back(n.normalize());
  }

  model.indx.push_back(Face3(0,11,5)); model.indx.push_back(Face3(0,5,1)); model.indx.push_back(Face3(0,1,7)); model.indx.push_back(Face3(0,7,10));
  model.indx.push_back(Face3(0,10,11)); model.indx.push_back(Face3(1,5,9)); model.indx.push_back(Face3(5,11,4)); model.indx.push_back(Face3(11,10,2));
  model.indx.push_back(Face3(10,7,6)); model.indx.push_back(Face3(7,1,8)); model.indx.push_back(Face3(3,9,4)); model.indx.push_back(Face3(3,4,2));
  model.indx.push_back(Face3(3,2,6)); model.indx.push_back(Face3(3,6,8)); model.indx.push_back(Face3(3,8,9)); model.indx.push_back(Face3(4,9,5));
  model.indx.push_back(Face3(2,4,11)); model.indx.push_back(Face3(6,2,10)); model.indx.push_back(Face3(8,6,7)); model.indx.push_back(Face3(9,8,1));
  return model;
}

struct faceModel facePlane() {
  struct faceModel model = faceModel();
  model.vrts.push_back(Vector3(1,1,0)); model.nmls.push_back(Vector3(0,0,1));
  model.vrts.push_back(Vector3(-1,1,0)); model.nmls.push_back(Vector3(0,0,1));
  model.vrts.push_back(Vector3(-1,-1,0)); model.nmls.push_back(Vector3(0,0,1));
  model.vrts.push_back(Vector3(-1,-1,0)); model.nmls.push_back(Vector3(0,0,1));
  model.vrts.push_back(Vector3(1,-1,0)); model.nmls.push_back(Vector3(0,0,1));
  model.vrts.push_back(Vector3(1,1,0)); model.nmls.push_back(Vector3(0,0,1));
  return model;
}

struct faceModel facePlaneTex() {
  struct faceModel model = facePlane();
  model.uvs.push_back(Vector2(0,1));
  model.uvs.push_back(Vector2(0,0));
  model.uvs.push_back(Vector2(1,0));
  model.uvs.push_back(Vector2(1,0));
  model.uvs.push_back(Vector2(1,1));
  model.uvs.push_back(Vector2(0,1));
  return model;
}

#define SI(i,j,s) ( (i) * (s) + (j) )
struct faceModel faceSphereTex(int latCnt, int lonCnt) {
  struct faceModel model = faceModel();
  for (int i = 0; i < lonCnt; i++) {
    float theta = 2 * M_PI * i / (float) (lonCnt-1);
    for (int j = 0; j < latCnt; j++) {
      float phi = M_PI * j / (float) (latCnt-1);
      Vector3 V = Vector3(sin(phi)*cos(theta),sin(phi)*sin(theta),cos(phi));
      model.vrts.push_back(V); model.nmls.push_back(V); model.uvs.push_back(Vector2(theta / (2 * M_PI),phi / M_PI));
    }
  }
  for (int i = 0; i < lonCnt - 1; i++) {
    for (int j = 0; j < latCnt - 1; j++) {
      model.indx.push_back(Face3(SI(i,j,latCnt),SI(i+1,j,latCnt),SI(i+1,j+1,latCnt)));
      model.indx.push_back(Face3(SI(i+1,j+1,latCnt),SI(i,j+1,latCnt),SI(i,j,latCnt)));
    }
  }
  return model;
}

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

struct faceModel faceFacetCone(int faceCnt) {
  struct faceModel model = faceModel();
  for (int i = 0; i < faceCnt; i++) {
    float angle = 2 * M_PI * i / (float) faceCnt;
    float nangle = 2 * M_PI * (i+1) / (float) faceCnt;

    Vector3 A = Vector3(cos(nangle), sin(nangle), 0.0);
    Vector3 B = Vector3(0.0,0.0,1.0);
    Vector3 C = Vector3(cos(angle), sin(angle), 0.0);

    model.nmls.push_back((B - C).cross((B - A)));
    model.vrts.push_back(B);

    model.nmls.push_back((B - C).cross((B - A)));
    model.vrts.push_back(C);

    model.nmls.push_back((B - C).cross((B - A)));
    model.vrts.push_back(A);
  }
  return model;
}

struct faceModel faceTorus(float majorRadius, int majorCnt, float minorRadius, int minorCnt) {
  struct faceModel model = faceModel();
  Vector3 Z = Vector3(0.0,0.0,1.0);
  for(int i = 0; i < majorCnt;i++ ) {
    float theta = 2 * M_PI * i/ (float) majorCnt;
    float ntheta = 2 * M_PI * (i + 1)/ (float) majorCnt;
    // outer looper, outer radius
    for(int j = 0; j < minorCnt; j++ ) {
      float phi = 2 * M_PI * j/ (float) minorCnt;
      float nphi = 2 * M_PI * (j+ 1)/ (float) minorCnt;
      // inner looper, inner radius
      // P_T is normal p, P_NT is consecutive p;
      // Q is normal q, Q_N is consecutive q
      Vector3 P_T = Vector3(cos(theta),sin(theta),0.0);
      Vector3 P_NT = Vector3(cos(ntheta),sin(ntheta), 0.0);
      Vector3 Q = (cos(phi) * P_T) + (sin(phi) * Z);
      Vector3 Q_N = (cos(nphi) * P_NT) + (sin(nphi) * Z);

      Vector3 X = (cos(phi) * P_NT) + (sin(phi) * Z);
      Vector3 Y = (cos(nphi) * P_T) + (sin(nphi) * Z);

      Vector3 A = (majorRadius * P_T) + (minorRadius * Q);
      Vector3 B = (majorRadius * P_NT) + (minorRadius * X);
      Vector3 C = (majorRadius * P_NT) + (minorRadius * Q_N);
      Vector3 D = (majorRadius * P_T) + (minorRadius * Y);

      model.vrts.push_back(A); model.nmls.push_back(Q);
      model.vrts.push_back(B); model.nmls.push_back(X);
      model.vrts.push_back(C); model.nmls.push_back(Q_N);

      model.vrts.push_back(A); model.nmls.push_back(Q);
      model.vrts.push_back(C); model.nmls.push_back(Q_N);
      model.vrts.push_back(D); model.nmls.push_back(Y);

    }
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

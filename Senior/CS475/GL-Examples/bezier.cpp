#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "Vectors.h"

#include "model.h"
#include "bezier.h"

float intPow(float x, int e) {
  float r = 1;
  for (int i = 1; i <= e; i++) {
    r *= x;
  }
  return r;
}

int binomialCoef(int n, int k) {
  // n! / (n-k)! k! = n(n-1)...(n-k+1) / k!
  int num = 1;
  for (int i = n; i > n - k; i--) num *= i;
  int denom = 1;
  for (int i = 2; i <= k; i++) denom *= i;
  return num / denom;
}

Vector3 curveAt(int n, std::vector<Vector3> ctlPts, float u) {
  Vector3 pt = Vector3(0,0,0);
  for (int i = 0; i < n; i++) {
//    fprintf(stderr,"n %d i %d coef %d (1-u) %d (u) %d\n",n,i,binomialCoef(n-1,i),n-i-1,i);
    pt += binomialCoef(n-1,i) * intPow(1-u,n-i-1) * intPow(u,i) * ctlPts[i];
  }
  return pt;
}

struct lineModel bezier_Curve(int n, std::vector<Vector3> ctlPts, int uCnt) {
  struct lineModel curve;

  for (int i = 0; i <= uCnt; i++) {
    float u = i / (float) uCnt;
    Vector3 pt = curveAt(n,ctlPts,u);
    curve.vrts.push_back(pt);
    fprintf(stderr,"u %f pt %f,%f,%f\n",u,pt.x,pt.y,pt.z);
  }
  curve.mode = GL_LINE_STRIP;
  return curve;
}

Vector3 patchAt(int n, int m, std::vector<Vector3> ctlPts, float u, float v) {
  Vector3 pt = Vector3(0,0,0);
  for (int j = 0; j < m; j++) {
    float coefj = binomialCoef(m-1,j) * intPow(1-v,m-j-1) * intPow(v,j);
    for (int i = 0; i < n; i++) {
      float coefi = binomialCoef(n-1,i) * intPow(1-u,n-i-1) * intPow(u,i);
      pt +=  coefj * coefi * ctlPts[j*n + i];
    }
  }
  return pt;
}

struct faceModel bezier_Patch(int n, int m, std::vector<Vector3> ctlPts, std::vector<Vector3> ctlNmls, int uCnt, int vCnt) {
  struct faceModel patch;

  // calculate points and normals
  for (int j = 0; j <= vCnt; j++) {
    for (int i = 0; i <= uCnt; i++) {
      float u = i / (float) uCnt;
      float v = j / (float) vCnt;
      Vector3 pt = patchAt(n,m,ctlPts,u,v);
      Vector3 nml = patchAt(n,m,ctlNmls,u,v);
      patch.vrts.push_back(pt);
      patch.nmls.push_back(nml);
      patch.uvs.push_back(Vector2(u,v));
    }
  }

  // create index face data
  //   j+1 d  -  c
  //       |  /  |
  //   j   a  -  b
  //       i    i+1
  for (int j = 0; j < vCnt; j++) {
    for (int i = 0; i < uCnt; i++) {
      int a = j * (uCnt+1) + i;
      int b = j * (uCnt+1) + i + 1;
      int c = (j+1) * (uCnt+1) + i + 1;
      int d = (j+1) * (uCnt+1) + i;
      patch.indx.push_back(Face3(a,b,c));
      patch.indx.push_back(Face3(c,d,a));
    }
  }
  return patch;
}

struct lineModel bezier_PatchCtl(int n, int m, std::vector<Vector3> ctlPts, std::vector<Vector3> ctlNmls) {
  struct lineModel ctl;

  // create grid of lines between control points
  for (int j = 0; j < m-1; j++) {
    for (int i = 0; i < n-1; i++) {
      ctl.vrts.push_back(ctlPts[j*n+i]);
      ctl.vrts.push_back(ctlPts[j*n+i+1]);
      ctl.vrts.push_back(ctlPts[j*n+i]);
      ctl.vrts.push_back(ctlPts[(j+1)*n+i]);
    }
    ctl.vrts.push_back(ctlPts[j*n+n-1]);
    ctl.vrts.push_back(ctlPts[(j+1)*n+n-1]);
  }
  for (int i = 0; i < n-1; i++) {
    ctl.vrts.push_back(ctlPts[(m-1)*n+i]);
    ctl.vrts.push_back(ctlPts[(m-1)*n+i+1]);
  }

  // create lines for normals
  for (int j = 0; j < m; j++) {
    for (int i = 0; i < n; i++) {
      ctl.vrts.push_back(ctlPts[j*n+i]);
      ctl.vrts.push_back(ctlPts[j*n+i] + ctlNmls[j*n+i].normalize());
    }
  }

  ctl.mode = GL_LINES;
  return ctl;
}

#ifdef TEST
int main() {
  for (int i = 0; i <= 3; i++) fprintf(stderr,"3^%d %f\n",i,intPow(3.0,i));
  for (int i = 0; i <= 3; i++) fprintf(stderr,"3,%d %d \n",i,binomialCoef(3,i));
  for (int i = 0; i <= 4; i++) fprintf(stderr,"4,%d %d \n",i,binomialCoef(4,i));
  std::vector<Vector3> ctlpts;
  ctlpts.push_back(Vector3(0,0,0));
  ctlpts.push_back(Vector3(2,0,0.33));
  ctlpts.push_back(Vector3(0,2,0.66));
  ctlpts.push_back(Vector3(0,0,1));
  Vector3 pt = curveAt(4,ctlpts,0.0);
  struct lineModel c = bezier_Curve(4,ctlpts,10);
}
#endif

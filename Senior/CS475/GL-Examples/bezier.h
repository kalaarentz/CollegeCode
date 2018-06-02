#ifndef BEZIER_H
#define BEZIER_H

#include "model.h"

struct lineModel bezier_Curve(int n, std::vector<Vector3> ctlPts, int uCnt);

struct faceModel bezier_Patch(int n, int m, std::vector<Vector3> ctlPts, std::vector<Vector3> ctlNmls, int uCnt, int vCnt);
struct lineModel bezier_PatchCtl(int n, int m, std::vector<Vector3> ctlPts, std::vector<Vector3> ctlNmls);

#endif

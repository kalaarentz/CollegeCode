/* Trackball style user interface. Converts mouse motion to quaternions, accumulates quaternions
  and produces transformation matrix for use in drawing.
  Coordinates are in [-1,1] range relative to window.
*/

#include <stdio.h>
#include <stdlib.h>
#ifdef Windows
#define _USE_MATH_DEFINES
#endif
#include <math.h>

#include "trackball.h"


#define TRACKBALL_SIZE 0.8
#define CLAMP(V,Min,Max) ( (V < Min) ? (Min) : ( (V > Max) ? (Max) : (V) ) )

Vector4 Q;
Matrix4 M;
float lastX, lastY;

//==================================

float projToSphere(float rad, float x, float y) {
  float d, t, z;

  d = sqrt(x*x + y*y);
  if (d < rad*M_SQRT1_2) {
    z = sqrt(rad*rad - d*d);
  }
  else {
    t = rad / M_SQRT2;
    z = t*t / d;
  }
  return z;
}

Vector4 axisToQuat(Vector3 axis, float phi) {
  axis.normalize();
  axis *= sin(phi/2.0);
//  q.set(axis.x,axis.y,axis.z,cos(phi/2.0));
  return Vector4(axis.x,axis.y,axis.z,cos(phi/2.0));
}

Vector4 computeQuat(float sx, float sy, float ex, float ey) {
  Vector3 p1, p2, d, rotAxis;
  float t, rotAngle;

  if (sx == ex && sy == ey) {
    return Vector4(0,0,0,1);
  }

  p1.set(sx,sy,projToSphere(TRACKBALL_SIZE,sx,sy));
  p2.set(ex,ey,projToSphere(TRACKBALL_SIZE,ex,ey));
  rotAxis = p2.cross(p1);
  d = p1 - p2;

  t = CLAMP(d.length() / (2.0 * TRACKBALL_SIZE),-1,1);
  rotAngle = 2.0 * asin(t);

  return axisToQuat(rotAxis,rotAngle);
}

Vector4 combineQuats(Vector4 q1, Vector4 q2) {
  Vector3 t1, t2, t3, s;

  t1.set(q1.x,q1.y,q1.z);
  t2.set(q2.x,q2.y,q2.z);
  t3 = t2.cross(t1);
  t1 = q2.w * t1;
  t2 = q1.w * t2;
  s = t1 + t2 + t3;
  Vector4 q = Vector4(s.x,s.y,s.z,2 * q1.w * q2.w - q1.dot(q2));
  //q.normalize(); // excludes w from calc which is not what we want here
  q *= 1.0f / q.length();
  return q;
}

//==================================

void tb_Init() {
  Q.set(0,0,0,1);
}

void tb_BeginMovement(float x, float y) {
  if ( x < -1 || x > 1 || y < -1 || y > 1) return;
  lastX = x;
  lastY = y;
}

void tb_ContinueMovement(float x, float y) {
  Vector4 q;
  if ( x < -1 || x > 1 || y < -1 || y > 1) return;
  q = computeQuat(lastX,lastY,x,y);
  Q = combineQuats(q,Q);
  lastX = x;
  lastY = y;
}

void tb_Reset() {
  Q.set(0,0,0,1);
}

void tb_Matrix(Matrix4& m) {
    m[0] = 1.0 - 2.0 * (Q[1] * Q[1] + Q[2] * Q[2]);
  	m[1] = 2.0 * (Q[0] * Q[1] - Q[2] * Q[3]);
  	m[2] = 2.0 * (Q[2] * Q[0] + Q[1] * Q[3]);
  	m[3] = 0.0;

  	m[4] = 2.0 * (Q[0] * Q[1] + Q[2] * Q[3]);
  	m[5] = 1.0 - 2.0 * (Q[2] * Q[2] + Q[0] * Q[0]);
  	m[6] = 2.0 * (Q[1] * Q[2] - Q[0] * Q[3]);
  	m[7] = 0.0;

  	m[8] = 2.0 * (Q[2] * Q[0] - Q[1] * Q[3]);
  	m[9] = 2.0 * (Q[1] * Q[2] + Q[0] * Q[3]);
  	m[10] = 1.0 - 2.0 * (Q[1] * Q[1] + Q[0] * Q[0]);
  	m[11] = 0.0;

  	m[12] = 0.0;
  	m[13] = 0.0;
  	m[14] = 0.0;
  	m[15] = 1.0;
}

void tb_Test() {
  fprintf(stdout,"testing\n");
  fprintf(stdout,"projToSphere 0.8 -0.2 -0.113333 -- %f \n",projToSphere(0.8, -0.2, -0.113333));
  // result is 0.766261
  Vector3 v = Vector3(0.000229, -0.005554, -0.000711);
  Vector4 q;
  q = axisToQuat(v,0.008756);
  fprintf(stdout,"axisToQuat 0.000229 -0.005554 -0.000711  0.008756 -- %f %f %f %f\n",q.x,q.y,q.z,q.w);
  // result is 0.000179 -0.004339 -0.000556 0.999990
  q.set(0,0,0,0);
  q = computeQuat(0.086667, -0.053333, 0.086667, -0.046667);
  fprintf(stdout,"computeQuat 0.086667 -0.053333 0.086667 -0.046667 -- %f %f %f %f\n",q.x,q.y,q.z,q.w);
  // result is 0.004150 0.000028 -0.000451 0.999991
  Vector4 q1 = Vector4(0.000537, -0.003941, 0.001771, 0.999991);
  Vector4 q2 = Vector4(0.291764, -0.316037, 0.067462, 0.900246);
  q.set(0,0,0,0);
  q = combineQuats(q1,q2);
  fprintf(stdout,"combineQuats 0.000537 -0.003941 0.001771 0.999991  0.291764 -0.316037 0.067462 0.900246  -- %f %f %f %f\n",q.x,q.y,q.z,q.w);
  // result is 0.291955 -0.320067 0.068076 0.898713
}

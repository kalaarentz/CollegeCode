#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "draw.h"
#include "Vectors.h"

#define FRAME_SIZE 10

GLfloat axis[] = {0,0,0, 0,0,1, 0.2,0,0.8, 0,0.2,0.8, 0,0,1};

void draw_Init() {
}

void drawAxis(float size) {
  glPushMatrix();
  glScalef(size,size,size);

  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3,GL_FLOAT,0,axis);
  glDrawArrays(GL_LINE_STRIP,0,5);
  glDisableClientState(GL_VERTEX_ARRAY);

  glPopMatrix();
}

void drawCoordFrame(float size) {
  glPushMatrix();
  glRotatef(90,0,1,0);
  glRotatef(90,0,0,1);
  glColor3f(1,0.2,0.2);
  drawAxis(size);
  glPopMatrix();

  glPushMatrix();
  glRotatef(-90,1,0,0);
  glRotatef(-90,0,0,1);
  glColor3f(0.2,1,0.2);
  drawAxis(size);
  glPopMatrix();

  glPushMatrix();
  glColor3f(0.2,0.2,1);
  drawAxis(size);
  glPopMatrix();
}

void draw_Scene(json state) {
  glPushMatrix();
  glTranslatef(-5.0,-5.0,-5.0);
  drawCoordFrame(FRAME_SIZE);
  glPopMatrix();
}

void draw_Resize(int width, int height) {
}

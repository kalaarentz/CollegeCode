#include <stdio.h>
#include <math.h>
#include "platformGL.h"

#include "draw.h"

#define FRAME_SIZE 10

                     // XY                       YZ                       ZX                       XZY
GLfloat tetra_vrts[] = {0,0,0, 0,1,0, 1,0,0,     0,0,0, 0,0,1, 0,1,0,     0,0,0, 1,0,0, 0,0,1,     1,0,0, 0,1,0, 0,0,1};
GLfloat tetra_nmls[] = {0,0,-1, 0,0,-1, 0,0,-1,  -1,0,0, -1,0,0, -1,0,0,  0,-1,0, 0,-1,0, 0,-1,0,  1,1,1, 1,1,1, 1,1,1};
GLfloat tetra_clrs[] = {0,0,0, 0,1,0, 1,0,0,     0,0,0, 0,0,1, 0,1,0,     0,0,0, 1,0,0, 0,0,1,     1,0,0, 0,1,0, 0,0,1};

void draw_Init() {
}

void drawIlluminatedTetra() {
  glEnableClientState(GL_VERTEX_ARRAY);
  glEnableClientState(GL_NORMAL_ARRAY);
  glVertexPointer(3,GL_FLOAT,0,tetra_vrts);
  glNormalPointer(GL_FLOAT,0,tetra_nmls);
  glDrawArrays(GL_TRIANGLES,0,12);
  glDisableClientState(GL_NORMAL_ARRAY);
  glDisableClientState(GL_VERTEX_ARRAY);
}

void drawTetra() {
  glEnableClientState(GL_VERTEX_ARRAY);
  glEnableClientState(GL_COLOR_ARRAY);
  glVertexPointer(3,GL_FLOAT,0,tetra_vrts);
  glColorPointer(3,GL_FLOAT,0,tetra_clrs);
  glDrawArrays(GL_TRIANGLES,0,12);
  glDisableClientState(GL_COLOR_ARRAY);
  glDisableClientState(GL_VERTEX_ARRAY);
}

void drawAxis(float size) {
  glPushMatrix();
  glScalef(size/10.0,size/10.0,size);
  drawIlluminatedTetra();
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

  if (state["displayTetra"] && state["illuminated"]) {
    glPushMatrix();
    glColor3f(1,1,0.2);
    glRotatef(state["angleTetra"],1,1,1);
    glScalef(5,5,5);
    drawIlluminatedTetra();
    glPopMatrix();
  }
  if (state["displayTetra"] && !state["illuminated"]) {
    glPushMatrix();
    glColor3f(1,1,0.2);
    glRotatef(state["angleTetra"],1,1,1);
    glScalef(5,5,5);
    drawTetra();
    glPopMatrix();
  }
}

void draw_Resize(int width, int height) {
}

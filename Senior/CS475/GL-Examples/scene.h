#ifndef SCENE_H
#define SCENE_H

#include <stdbool.h>

#include "Vectors.h"
#include "Matrices.h"

#include "modelVAO.h"

extern std::vector<struct drawable> scene;

struct uniforms {
  GLint colorUniform;
  GLint materialUniform;
  GLint modelviewUniform;
  GLint selecttagUniform;
};

struct drawable {
  GLuint selectTag;
  Vector4 color;
  Vector4 material;
  Vector4 highlightColor;
  Matrix4 transforms;
  bool selected;
  bool animating;
  struct faceModelVAO model;

  void draw();
};

void              scene_Init(struct uniforms uniforms);
void              scene_Draw(Matrix4 modelview);
void              scene_AddDrawable(struct drawable drawable);
struct drawable&  scene_DrawableWithID(int tag);
struct drawable&  scene_NextSelected(bool first);

#endif

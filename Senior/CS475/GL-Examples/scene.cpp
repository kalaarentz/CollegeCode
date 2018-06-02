
#include "scene.h"

std::vector<struct drawable> scene;
struct drawable notFound;
struct uniforms theUniforms;
Matrix4 theModelView;
int enumIndx = -1;


void drawable::draw() {
  glBindVertexArray(this->model.VAO);
  glUniform1i(theUniforms.selecttagUniform,this->selectTag);
  Vector4 color = (this->selected) ? (this->highlightColor) : (this->color);
  glUniform3f(theUniforms.colorUniform,color[0],color[1],color[2]);
  glUniform4f(theUniforms.materialUniform,this->material[0],this->material[1],this->material[2],this->material[3]);
  glUniformMatrix4fv(theUniforms.modelviewUniform,1,GL_FALSE,(theModelView * this->transforms).get());
  if (this->model.indx.size() > 0) {
    glDrawElements(GL_TRIANGLES,this->model.indx.size(),GL_UNSIGNED_INT,(const GLvoid*) 0);
  }
  else {
    glDrawArrays(GL_TRIANGLES,0,this->model.vCnt);
  }
  glBindVertexArray(0);
}

void scene_Init(struct uniforms uniforms) {
  theUniforms = uniforms;
  notFound = drawable();
  notFound.selectTag = -1;
}

void scene_Draw(Matrix4 modelview) {
  theModelView = modelview;
  for (int i = 0; i < scene.size(); i++) {
    scene[i].draw();
  }
}

void scene_AddDrawable(struct drawable drawable) {
  scene.push_back(drawable);
}

struct drawable&  scene_DrawableWithID(int tag) {
  for (int i = 0; i < scene.size(); i++) {
    if (scene[i].selectTag == tag) return scene[i];
  }
  return notFound;
}

struct drawable&  scene_NextSelected(bool first) {
  if (first) enumIndx = -1;
  enumIndx++;
  while (enumIndx < scene.size()) {
    if (scene[enumIndx].selected) return scene[enumIndx];
    enumIndx++;
  }
  return notFound;
}

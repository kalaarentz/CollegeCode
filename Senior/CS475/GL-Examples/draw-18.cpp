#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "colors.h"
#include "trackball.h"
#include "Matrices.h"
#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "OBJModel.h"
#include "modelVAO.h"
#include "shaderUtil.h"
#include "matrixUtil.h"
#include "dijkstra.h"
#include "meshTables.h"
#ifdef Windows
#define _USE_MATH_DEFINES
#endif
#include <math.h>

#include "draw.h"

/* define several colors */
GLfloat BlueClr[] = {0.2, 0.2, 0.8, 1.0};
GLfloat YeloClr[] = {0.8, 0.8, 0.0, 1.0};
GLfloat GrenClr[] = {0.0, 0.8, 0.0, 1.0};
GLfloat RedClr[]  = {0.8, 0.0, 0.0, 1.0};
GLfloat BrwnClr[] = {0.5, 0.3, 0.2, 1.0};
GLfloat PrplClr[] = {0.7, 0.2, 0.7, 1.0};
GLfloat BlakClr[] = {0.0, 0.0, 0.0, 1.0};
GLfloat WhitClr[] = {1.0, 1.0, 1.0, 1.0};
GLfloat GrayClr[] = {0.3, 0.3, 0.3, 1.0};
GLfloat PuttyClr[] = {1.0,1.0,0.8,1.0};

GLfloat LightPos[] = {1.0, 1.0, 1.0, 0.0};
GLfloat BasicMaterial[4] = {0.2, 0.7, 1.0, 50.0};
GLfloat AmbientMaterial[4] = {1.0, 0.0, 0.0, 0.0};

#define D2R(x)  ( ( (x) / 180.0) * M_PI)
#define R2D(x)  ( ( (x) / M_PI) * 180.0)
#define COSd(x)  ( cos(D2R(x)) )
#define SINd(x)  ( sin(D2R(x)) )
#define SQR(x)   ( (x) * (x) )
#define CLAMP(v,l,u) ( (v < l) ? (l) : ((v > u) ? (u) : (v)) )

// Shaders
struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint projUniform;
  GLint modelviewUniform;
  GLint srcposUniform;
  GLint sinkposUniform;
} mainProgram;

Matrix4 projMatrix;

void initShaders() {
  mainProgram.id = shader_Compile("dijkstra");
  shader_Link(mainProgram.id);
  mainProgram.lightposUniform = glGetUniformLocation(mainProgram.id,"lightpos");
  mainProgram.materialUniform = glGetUniformLocation(mainProgram.id,"material");
  mainProgram.colorUniform = glGetUniformLocation(mainProgram.id,"color");
  mainProgram.projUniform = glGetUniformLocation(mainProgram.id,"proj");
  mainProgram.modelviewUniform = glGetUniformLocation(mainProgram.id,"modelview");
  mainProgram.srcposUniform = glGetUniformLocation(mainProgram.id,"srcpos");
  mainProgram.sinkposUniform = glGetUniformLocation(mainProgram.id,"sinkpos");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d \n", mainProgram.lightposUniform, mainProgram.materialUniform, mainProgram.colorUniform, mainProgram.projUniform, mainProgram.modelviewUniform, mainProgram.srcposUniform, mainProgram.sinkposUniform);
}

// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO manifold;

void initObjects() {
  axisTetra = makeVAOForModel(faceCoordTetraTex());
  manifold = makeVAOForModel(createUVs(readOBJElements("OBJModels/Manifold-2.obj")));
  fprintf(stderr,"manifold stride %d vCnt %d size %ld\n",manifold.stride,manifold.vCnt,manifold.attr.size());
  mesh_Init(&manifold);
}

void draw_Init() {
  initShaders();
  initObjects();

  dijkstra_Init(&manifold);
}

void animateDijkstra() {
  // To animate the algorithm it is performed in small steps on each frame update. A small state
  // machine is used to control the operation. In the IDLE state nothing is done. While EXPANDING, do
  // a few cycles of expanding the boundary one vertex at a time. When sink is reached shift to PATHING
  // state. In PATHING create the wall for the path from src to sink.
  // If animating is not true then do expansion all at once.

  switch (dijkstraMode) {
    case IDLE:
      break;
    case EXPANDING:
      do { dijkstra_ExpandBoundary(); } while (dijkstraMode == EXPANDING && !animating);
      break;
    case PATHING:
      //MakePath();
      break;
  }
}

void draw_Scene(json state) {
  animateDijkstra();

  glUseProgram(mainProgram.id);
  glUniformMatrix4fv(mainProgram.projUniform,1,GL_FALSE,projMatrix.get());
  glUniform3fv(mainProgram.lightposUniform,1,LightPos);
  glUniform4fv(mainProgram.materialUniform,1,BasicMaterial);

  glUniform3fv(mainProgram.srcposUniform,1,&(manifold.attr[srcVertex*8]));
  glUniform3fv(mainProgram.sinkposUniform,1,&(manifold.attr[sinkVertex*8]));

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = mutil_Translate(Vector3(0,0,-6)) * tbMatrix;
  glUniformMatrix4fv(mainProgram.modelviewUniform,1,GL_FALSE,mv.get());

  // glBindVertexArray(axisTetra.VAO);
  // glUniform3fv(mainProgram.colorUniform,1,BlueClr);
  // glDrawArrays(GL_TRIANGLES,0,axisTetra.vCnt);

  glBindVertexArray(manifold.VAO);
  glUniform3fv(mainProgram.colorUniform,1,PuttyClr);
//  glDrawArrays(GL_TRIANGLES,0,manifold.vCnt);
  glDrawElements(GL_TRIANGLES,manifold.iCnt,GL_UNSIGNED_INT,(const GLvoid*) 0);
}

void draw_Resize(int width, int height) {
  float aspect = width / (float) height;
  projMatrix = mutil_PerspectiveMatrix(45,aspect,0.1,100.0);
}

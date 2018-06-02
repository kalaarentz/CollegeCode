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
#include "scene.h"
#include "bezier.h"
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

GLfloat LightPos[] = {1.0, 1.0, 1.0, 0.0};
GLfloat BasicMaterial[4] = {0.2, 0.7, 1.0, 50.0};
GLfloat AmbientMaterial[4] = {1.0, 0.0, 0.0, 0.0};

#define D2R(x)  ( ( (x) / 180.0) * M_PI)
#define R2D(x)  ( ( (x) / M_PI) * 180.0)
#define COSd(x)  ( cos(D2R(x)) )
#define SINd(x)  ( sin(D2R(x)) )
#define SQR(x)   ( (x) * (x) )
#define CLAMP(v,l,u) ( (v < l) ? (l) : ((v > u) ? (u) : (v)) )

GLfloat DrawWidth, DrawHeight;
GLfloat DrawAspect;

// Shaders
struct {
  GLuint id;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
  GLint depthonlyUniform;
  GLint shadowmapUniform;
  GLint projVSUniform;
  GLint viewVSUniform;
  GLint projLSUniform;
  GLint viewLSUniform;
  GLint modelUniform;
  GLint selecttagUniform;
} shadowProgram;


void initShaders() {
  shadowProgram.id = shader_Compile("shadow");
  shader_Link(shadowProgram.id);
  shadowProgram.lightposUniform = glGetUniformLocation(shadowProgram.id,"lightpos");
  shadowProgram.materialUniform = glGetUniformLocation(shadowProgram.id,"material");
  shadowProgram.colorUniform = glGetUniformLocation(shadowProgram.id,"color");
  shadowProgram.depthonlyUniform = glGetUniformLocation(shadowProgram.id,"depthonly");
  shadowProgram.shadowmapUniform = glGetUniformLocation(shadowProgram.id,"shadowMap");
  shadowProgram.projVSUniform = glGetUniformLocation(shadowProgram.id,"projVS");
  shadowProgram.viewVSUniform = glGetUniformLocation(shadowProgram.id,"viewVS");
  shadowProgram.projLSUniform = glGetUniformLocation(shadowProgram.id,"projLS");
  shadowProgram.viewLSUniform = glGetUniformLocation(shadowProgram.id,"viewLS");
  shadowProgram.modelUniform = glGetUniformLocation(shadowProgram.id,"model");
  shadowProgram.selecttagUniform = glGetUniformLocation(shadowProgram.id,"selecttag");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d %d %d %d\n", shadowProgram.lightposUniform, shadowProgram.materialUniform, shadowProgram.colorUniform, shadowProgram.depthonlyUniform, shadowProgram.shadowmapUniform, shadowProgram.projVSUniform, shadowProgram.viewVSUniform, shadowProgram.projLSUniform, shadowProgram.viewLSUniform, shadowProgram.modelUniform, shadowProgram.selecttagUniform);
}

// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO cubecyl;
struct faceModelVAO plane;

GLuint curveCtlPtsVAO, curveCtlPtsVBO;
std::vector<Vector3> curvePts = {Vector3(0,0,0), Vector3(2,0,0.33), Vector3(0,2,0.66), Vector3(0,0,1) };
struct lineModel curveCtlPts;
GLuint curveVAO, curveVBO;
struct lineModel curve;

std::vector<Vector3> patchPts
  = {Vector3(-1,-1,0), Vector3(0,-1,0),  Vector3(1,-1,0),   Vector3(2,-1,0),
     Vector3(-1,0,0),  Vector3(0.5,0,0.5), Vector3(0.5,0,-0.5), Vector3(2,0,0),
     Vector3(-1,1,0),  Vector3(1.5,1,1), Vector3(-0.5,1,-1), Vector3(2,1,0),
     Vector3(-1,2,0),  Vector3(2,2,1),   Vector3(-1,2,-1),   Vector3(2,2,0) };
std::vector<Vector3> patchNmls
  = {Vector3(0,0,1),   Vector3(0,0,1),   Vector3(0,0,1),   Vector3(0,0,1),
     Vector3(0,0,1),   Vector3(0.25,0,1),   Vector3(-0.25,0,-1),   Vector3(0,0,1),
     Vector3(0,0,1),   Vector3(0.5,0,1), Vector3(-0.5,0,-1), Vector3(0,0,1),
     Vector3(0,0,1),   Vector3(1,0,1),   Vector3(-1,0,-1),   Vector3(0,0,1) };
struct faceModelVAO patch;
GLuint patchCtlVAO, patchCtlVBO;
struct lineModel patchCtl;

void initSceneObjects() {
  axisTetra = makeVAOForModel(faceCoordTetraTex());
  sphere = makeVAOForModel(faceSphereTex(24,48));
  cubecyl = makeVAOForModel(readOBJArrays("OBJModels/Cube-Cyl.obj"));
  plane = makeVAOForModel(facePlane());

  // can't use lineModel::draw() since requires compatability profile
  // must use VAO
  curveCtlPts.mode = GL_LINE_STRIP;
  curveCtlPts.vrts = curvePts;

  glGenVertexArrays(1,&curveCtlPtsVAO);
  glBindVertexArray(curveCtlPtsVAO);
  glGenBuffers(1,&curveCtlPtsVBO);
  glBindBuffer(GL_ARRAY_BUFFER,curveCtlPtsVBO);
  glBufferData(GL_ARRAY_BUFFER,curveCtlPts.vrts.size() * 3 * sizeof(GLfloat), curveCtlPts.vrts.data(), GL_STATIC_DRAW);
  glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,(const GLvoid*) 0);
  glEnableVertexAttribArray(0);

  curve = bezier_Curve(4,curveCtlPts.vrts,10);

  glGenVertexArrays(1,&curveVAO);
  glBindVertexArray(curveVAO);
  glGenBuffers(1,&curveVBO);
  glBindBuffer(GL_ARRAY_BUFFER,curveVBO);
  glBufferData(GL_ARRAY_BUFFER,curve.vrts.size() * 3 * sizeof(GLfloat), curve.vrts.data(), GL_STATIC_DRAW);
  glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,(const GLvoid*) 0);
  glEnableVertexAttribArray(0);

  patch = makeVAOForModel(bezier_Patch(4,4,patchPts,patchNmls,10,10));
  fprintf(stderr,"patch attr %ld indx %ld\n",patch.attr.size(),patch.indx.size());

  patchCtl = bezier_PatchCtl(4,4,patchPts,patchNmls);

  glGenVertexArrays(1,&patchCtlVAO);
  glBindVertexArray(patchCtlVAO);
  glGenBuffers(1,&patchCtlVBO);
  glBindBuffer(GL_ARRAY_BUFFER,patchCtlVBO);
  glBufferData(GL_ARRAY_BUFFER,patchCtl.vrts.size() * 3 * sizeof(GLfloat), patchCtl.vrts.data(), GL_STATIC_DRAW);
  glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,0,(const GLvoid*) 0);
  glEnableVertexAttribArray(0);

  // in shadow shader matrices are seperated, e.g. proj * view * model
  // scene assume view and model are together, can work around this by
  // providing modelUniform
  struct uniforms uniforms = {shadowProgram.colorUniform,shadowProgram.materialUniform,shadowProgram.modelUniform,
    shadowProgram.selecttagUniform};

  scene_Init(uniforms);

  Vector3 cubesize = Vector3(0.3,0.3,0.3);

  struct drawable drawable;
  drawable.selected = false;
  drawable.animating = false;
  drawable.color = Vector4(0.8,0.0,0.0,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(-2,-2,0)) * mutil_Translate(Vector3(0.5,0,-1)) * mutil_Scale(Vector3(2,0.5,0.5));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);
  drawable.color = Vector4(0.0,0.8,0.0,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(-2,-2,0)) * mutil_Translate(Vector3(0,0.5,-1)) * mutil_Scale(Vector3(0.5,2,0.5));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);
  drawable.color = Vector4(0.0,0.0,0.8,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(-2,-2,0)) * mutil_Translate(Vector3(0,0,-0.5)) * mutil_Scale(Vector3(0.5,0.5,2));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);

  drawable.color = Vector4(0.8,0.8,1,1);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(0,0,-1)) * mutil_Scale(Vector3(3,3,3));
  drawable.model = plane;
  scene_AddDrawable(drawable);
  drawable.color = Vector4(1,0.8,0.8,1);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(-3,0,0)) * mutil_Scale(Vector3(3,3,3))
    * mutil_Rotate(90,Vector3(0,1,0));
  drawable.model = plane;
  scene_AddDrawable(drawable);
}

// Shadow Map
#define SHADOW_WIDTH 1024
#define SHADOW_HEIGHT 1024
GLuint shadowFBO;
GLuint shadowTexID;

void initShadow() {
  glGenFramebuffers(1,&shadowFBO);

  glGenTextures(1,&shadowTexID);
  glBindTexture(GL_TEXTURE_2D, shadowTexID);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

  glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
  glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowTexID, 0);
  glDrawBuffer(GL_NONE);
  glReadBuffer(GL_NONE);

  if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
    fprintf(stderr,"framebuffer failure\n");
    exit(1);
  }

  glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

// Projection and View Matrices
Matrix4 projVSMatrix;
Matrix4 viewVSMatrix;
Matrix4 projLSMatrix;
Matrix4 viewLSMatrix;

Vector3 cameraPos;
Vector3 lightCameraPos;
Vector3 viewerCameraPos;
GLfloat cameraBackoff = -8;

void initLightTransform() {
  Vector3 Y = Vector3(0,1,0);
  Vector3 X = Vector3(1,0,0);
  Vector3 lightDir = Vector3(LightPos[0],LightPos[1],LightPos[2]);
  lightDir.normalize();
  projLSMatrix = mutil_OrthographicMatrix(-4,4,-4,4,0.1,20.0);
  //mutil_PerspectiveMatrix(45,DrawAspect,0.1,100.0);
  GLfloat xAngle = R2D(atan2(lightDir.y,sqrt(SQR(lightDir.x) + SQR(lightDir.z))));
  GLfloat yAngle = -R2D(atan2(lightDir[0],lightDir[2]));
  projLSMatrix = projLSMatrix * mutil_Rotate(xAngle,X) * mutil_Rotate(yAngle,Y);
  lightCameraPos = cameraBackoff * lightDir;
  viewLSMatrix = mutil_Translate(lightCameraPos);
  mutil_Print4x4(true,"projLS",projLSMatrix);
  mutil_Print4x4(true,"viewLS",viewLSMatrix);
}

void initViewerTransform() {
  projVSMatrix = mutil_PerspectiveMatrix(45,DrawAspect,0.1,20.0);
  viewerCameraPos = cameraBackoff * Vector3(0,0,1);
  viewVSMatrix = mutil_Translate(viewerCameraPos);
  mutil_Print4x4(true,"projVS",projVSMatrix);
  mutil_Print4x4(true,"viewVS",viewVSMatrix);
}

void draw_Init() {
  initShaders();
  initSceneObjects();
  initShadow();
  initLightTransform();
  initViewerTransform();
}

void drawSceneContents(json state) {
  glUseProgram(shadowProgram.id);

  glUniform1i(shadowProgram.depthonlyUniform,state["depthonly"]);
  // if (state["showcull"]) {
  //   glCullFace(GL_FRONT);
  // }

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = tbMatrix;

  // draw coordinate frame
  glUniform3fv(shadowProgram.lightposUniform,1,LightPos);
  glUniform4fv(shadowProgram.materialUniform,1,BasicMaterial);

  // draw scene
  scene_Draw(mv);

  if (state["curve"]) {
    glEnable(GL_LINE_SMOOTH);
    glUniformMatrix4fv(shadowProgram.modelUniform,1,GL_FALSE,mv.get());
    glUniform4fv(shadowProgram.materialUniform,1,AmbientMaterial);
    glUniform3f(shadowProgram.colorUniform,1.0,1.0,0.0);
    glBindVertexArray(curveCtlPtsVAO);
    glLineWidth(3.0);
    glDrawArrays(GL_LINE_STRIP,0,curveCtlPts.vrts.size());
    glUniform3f(shadowProgram.colorUniform,1.0,0.0,1.0);
    glBindVertexArray(curveVAO);
    glLineWidth(5.0);
    glDrawArrays(GL_LINE_STRIP,0,curve.vrts.size());
    glDisable(GL_LINE_SMOOTH);
  }

  if (state["patch"]) {
    glUniformMatrix4fv(shadowProgram.modelUniform,1,GL_FALSE,mv.get());
    glUniform4fv(shadowProgram.materialUniform,1,BasicMaterial);
    glUniform3f(shadowProgram.colorUniform,1.0,0.0,1.0);
//    glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glBindVertexArray(patch.VAO);
    glDrawElements(GL_TRIANGLES,patch.indx.size(),GL_UNSIGNED_INT,(const GLvoid*)0);
//    glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
    glEnable(GL_LINE_SMOOTH);
    glUniform4fv(shadowProgram.materialUniform,1,AmbientMaterial);
    glUniform3f(shadowProgram.colorUniform,1.0,1.0,0.0);
    glBindVertexArray(patchCtlVAO);
    glLineWidth(3.0);
    glDrawArrays(patchCtl.mode,0,patchCtl.vrts.size());
    glDisable(GL_LINE_SMOOTH);
  }
}

void setProjViewFromLight() {
  // light space and viewer space are the same - view from light source
  // when rendering only to depth texture (LightDepth is true) only use light space
  // when render to window for visualizing (LightDepth is false) use both spaces
  glUniformMatrix4fv(shadowProgram.projLSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(shadowProgram.viewLSUniform,1,GL_FALSE,viewLSMatrix.get());
  glUniformMatrix4fv(shadowProgram.projVSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(shadowProgram.viewVSUniform,1,GL_FALSE,viewLSMatrix.get());
}

void setProjViewFromViewer() {
  // supply shaders with all four matricies
  glUniformMatrix4fv(shadowProgram.projVSUniform,1,GL_FALSE,projVSMatrix.get());
  glUniformMatrix4fv(shadowProgram.viewVSUniform,1,GL_FALSE,viewVSMatrix.get());
  glUniformMatrix4fv(shadowProgram.projLSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(shadowProgram.viewLSUniform,1,GL_FALSE,viewLSMatrix.get());
}

// serves as wrapper to provide two stage shadow work
void draw_Scene(json state) {
  // pass 1 - draw from point of view of light direction, only retain depth information
  // in FBO's depth texture
  glViewport(0,0,SHADOW_WIDTH,SHADOW_HEIGHT);
  glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
  glClear(GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_FRONT);
  setProjViewFromLight();
  glUniform1i(shadowProgram.depthonlyUniform,true);
  drawSceneContents(state);
  glBindFramebuffer(GL_FRAMEBUFFER,0);

  // pass 2 - draw scene for viewer using captured depth information
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, shadowTexID);
  glUniform1i(shadowProgram.shadowmapUniform,0);
  glViewport(0,0,DrawWidth,DrawHeight);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_BACK);
  (state["viewlight"]) ? (setProjViewFromLight()) : (setProjViewFromViewer());
  glUniform1i(shadowProgram.depthonlyUniform,false);
  drawSceneContents(state);
}

void draw_Resize(int width, int height) {
  DrawWidth = width;
  DrawHeight = height;
  DrawAspect = width / (float) height;
  initViewerTransform();
//  projMatrix = mutil_PerspectiveMatrix(45,DrawAspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

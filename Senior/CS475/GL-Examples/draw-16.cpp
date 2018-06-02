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

// animated objects
GLint innerViewer = -1;
GLint targetSphere = -1;
GLint ticks = 0;

Matrix4 matrixForPose(Vector3 pos, Vector3 dir, Vector3 up) {
  // assume starting pose is pointing along Z with Y up
  Vector3 Z = Vector3(0,0,1);
  Vector3 Y = Vector3(0,1,0);
  Vector3 half = (Z + dir.normalize());
  Matrix4 dirMat = mutil_Rotate(180,half);
  Vector3 rotY = (dirMat * Y).normalize();
  Vector3 upp = dir.cross(dir.cross(up)).normalize(); // cross order?
  GLfloat sinAngle = (rotY.cross(upp)).length();
  GLfloat twistAngle = R2D(asin(sinAngle));
  Matrix4 twistMat = mutil_Rotate(twistAngle,dir);
  return mutil_Translate(pos) * twistMat * dirMat;
}

Vector3 positionOfPose(Matrix4 mat) {
  Vector4 pos = mat * Vector4(0,0,0,1);
//  fprintf(stderr,"pos %f,%f,%f,%f\n",pos.x,pos.y,pos.z,pos.w);
  Vector4 poseZ = mat * Vector4(0,0,1,0);
//  fprintf(stderr,"poseZ %f,%f,%f,%f\n",poseZ.x,poseZ.y,poseZ.z,poseZ.w);
  Vector4 poseY = mat * Vector4(0,1,0,0);
//  fprintf(stderr,"poseY %f,%f,%f,%f\n",poseY.x,poseY.y,poseY.z,poseY.w);
  Vector4 poseX = mat * Vector4(1,0,0,0);
//  fprintf(stderr,"poseX %f,%f,%f,%f\n",poseX.x,poseX.y,poseX.z,poseX.w);
  return Vector3(pos.x,pos.y,pos.z);
}

void setInnerViewerPose(Vector3 pos, Vector3 dir, Vector3 up) {
  scene[innerViewer].transforms = matrixForPose(pos,dir,up) * mutil_Scale(Vector3(0.1,0.1,0.1));
}

void setTargetSpherePos(Vector3 pos) {
  scene[targetSphere].transforms = mutil_Translate(pos) * mutil_Scale(Vector3(0.1,0.1,0.1));
}

void updateObjects() {
  float angle = ticks / 1000.0;
  Vector3 spherePos = Vector3(cos(angle),sin(angle),cos(10*angle));
  setTargetSpherePos(spherePos);
  Vector3 innerViewerPos = positionOfPose(scene[innerViewer].transforms);
  setInnerViewerPose(innerViewerPos,spherePos - innerViewerPos,Vector3(0,0,1));
  ticks++;
}

// Models
struct faceModelVAO axisTetra;
struct faceModelVAO sphere;
struct faceModelVAO cubecyl;
struct faceModelVAO plane;
struct faceModelVAO frustum;

void initSceneObjects() {
  axisTetra = makeVAOForModel(faceCoordTetraTex());
  sphere = makeVAOForModel(faceSphereTex(24,48));
  cubecyl = makeVAOForModel(readOBJArrays("OBJModels/Cube-Cyl.obj"));
  plane = makeVAOForModel(facePlane());
  frustum = makeVAOForModel(readOBJArrays("OBJModels/frustum2.obj"));

  // in shadow shader matrices are seperated, e.g. proj * view * model
  // scene assume view and model are together, can work around this by
  // providing modelUniform
  struct uniforms uniforms = {shadowProgram.colorUniform,shadowProgram.materialUniform,shadowProgram.modelUniform,
    shadowProgram.selecttagUniform};

  scene_Init(uniforms);

  Vector3 cubesize = Vector3(0.3,0.3,0.3);

  // coord frame
  struct drawable drawable;
  drawable.selected = false;
  drawable.animating = false;
  drawable.color = Vector4(0.8,0.0,0.0,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(0.5,0,-1)) * mutil_Scale(Vector3(2,0.5,0.5));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);
  drawable.color = Vector4(0.0,0.8,0.0,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(0,0.5,-1)) * mutil_Scale(Vector3(0.5,2,0.5));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);
  drawable.color = Vector4(0.0,0.0,0.8,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(0,0,-0.5)) * mutil_Scale(Vector3(0.5,0.5,2));
  drawable.model = axisTetra;
  scene_AddDrawable(drawable);

  // ground and back planes
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

  // cubes
  drawable.color = Vector4(0.9,0.9,0.3,1.0);
  drawable.material = Vector4(0.3,0.8,1.0,30.0);
  drawable.transforms = mutil_Translate(Vector3(1,1,-0.55)) * mutil_Scale(cubesize);
  drawable.model = cubecyl;
  scene_AddDrawable(drawable);

  drawable.transforms = mutil_Translate(Vector3(0.7,-1.3,-0.55)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(1,-1,0.35)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);

  drawable.transforms = mutil_Translate(Vector3(-1,-1,-0.55)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-0.9,-0.7,0.35)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);

  drawable.transforms = mutil_Translate(Vector3(-1,1,-0.55)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-0.8,0.7,0.35)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);
  drawable.transforms = mutil_Translate(Vector3(-0.7,0,1.25)) * mutil_Scale(cubesize);
  scene_AddDrawable(drawable);

  // inner viewerCameraPos
  drawable.color = Vector4(0.9,0.2,0.9,1.0);
  drawable.model = frustum;
  scene_AddDrawable(drawable);
  innerViewer = scene.size() - 1;
  Vector3 pos = Vector3(2,-2,1);
  Vector3 dir = Vector3(0,0,0) - pos;
  setInnerViewerPose(pos,dir,Vector3(0,0,1));

  // target sphere
  drawable.color = Vector4(1,1,1,1);
  drawable.model = sphere;
  scene_AddDrawable(drawable);
  targetSphere = scene.size() - 1;
  setTargetSpherePos(Vector3(0,0,0));
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
Matrix4 innerprojVSMatrix;
Matrix4 innerviewVSMatrix;

Vector3 cameraPos;
Vector3 lightCameraPos;
Vector3 viewerCameraPos;
GLfloat cameraBackoff = -8;

void initLightTransforms() {
  Vector3 Z = Vector3(0,0,1);
  Vector3 lightDir = Vector3(LightPos[0],LightPos[1],LightPos[2]);
  lightDir.normalize();
  projLSMatrix = mutil_OrthographicMatrix(-4,4,-4,4,0.1,20.0);
  Vector3 half = Z + lightDir;
  projLSMatrix = projLSMatrix;
  lightCameraPos = cameraBackoff * lightDir;
  viewLSMatrix =  mutil_Rotate(180,half) * mutil_Translate(lightCameraPos);
  mutil_Print4x4(true,"projLS",projLSMatrix);
  mutil_Print4x4(true,"viewLS",viewLSMatrix);
}

void initViewerTransforms() {
  projVSMatrix = mutil_PerspectiveMatrix(45,DrawAspect,0.1,20.0);
  viewerCameraPos = cameraBackoff * Vector3(0,0,1);
  viewVSMatrix = mutil_Translate(viewerCameraPos);
  mutil_Print4x4(true,"projVS",projVSMatrix);
  mutil_Print4x4(true,"viewVS",viewVSMatrix);
}

void initInnerTransforms() {
  innerprojVSMatrix = mutil_PerspectiveMatrix(60,DrawAspect,0.1,20.0);
  Matrix4 mat = scene[innerViewer].transforms * mutil_Scale(Vector3(10,10,10));
  innerviewVSMatrix = mutil_Rotate(180,Vector3(0,1,0)) * mat.invert(); // why rot on Y
//  mutil_Print4x4(true,"innerprojVS",innerprojVSMatrix);
//  mutil_Print4x4(true,"innerviewVS",innerviewVSMatrix);
}

void draw_Init() {
  initShaders();
  initSceneObjects();
  initShadow();
  initLightTransforms();
  initViewerTransforms();
}

void drawSceneContents(json state) {
  glUseProgram(shadowProgram.id);

  glUniform1i(shadowProgram.depthonlyUniform,state["depthonly"]);
  if (state["showcull"]) {
    glCullFace(GL_FRONT);
  }

  Matrix4 tbMatrix = Matrix4();
  tb_Matrix(tbMatrix);

  Matrix4 mv = tbMatrix;

  // draw coordinate frame
  glUniform3fv(shadowProgram.lightposUniform,1,LightPos);
  glUniform4fv(shadowProgram.materialUniform,1,BasicMaterial);

  // draw scene, trackball only applies when external viewer
  if (state["innerviewer"]) {
    Matrix4 identity = Matrix4();
    scene_Draw(identity);
  }
  else {
    scene_Draw(mv);
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

void setProjViewFromViewer(json state) {
  // supply shaders with all four matricies
  if (state["innerviewer"]) {
    glUniformMatrix4fv(shadowProgram.projVSUniform,1,GL_FALSE,innerprojVSMatrix.get());
    glUniformMatrix4fv(shadowProgram.viewVSUniform,1,GL_FALSE,innerviewVSMatrix.get());
  }
  else {
    glUniformMatrix4fv(shadowProgram.projVSUniform,1,GL_FALSE,projVSMatrix.get());
    glUniformMatrix4fv(shadowProgram.viewVSUniform,1,GL_FALSE,viewVSMatrix.get());
  }
  glUniformMatrix4fv(shadowProgram.projLSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(shadowProgram.viewLSUniform,1,GL_FALSE,viewLSMatrix.get());
}

// serves as wrapper to provide two stage shadow work
void draw_Scene(json state) {
  if (!state["paused"]) updateObjects();
  initInnerTransforms();

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
  (state["viewlight"]) ? (setProjViewFromLight()) : (setProjViewFromViewer(state));
  glUniform1i(shadowProgram.depthonlyUniform,false);
  drawSceneContents(state);
}

void draw_Resize(int width, int height) {
  DrawWidth = width;
  DrawHeight = height;
  DrawAspect = width / (float) height;
  initViewerTransforms();
//  projMatrix = mutil_PerspectiveMatrix(45,DrawAspect,0.1,100.0);
// put into model view;
//  projMatrix = projMatrix * mutil_Translate(Vector3(0,0,-5));
}

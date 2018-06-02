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
GLfloat OrngClr[] = {0.9, 0.3, 0.0, 1.0};

GLfloat LightPos[] = {1.0, 1.0, 1.0, 0.0};
GLfloat BasicMaterial[4] = {0.2, 0.7, 1.0, 50.0};
GLfloat ManifoldMaterial[4] = {0.3, 0.5, 0.5, 50.0};

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
  GLint pickingUniform;
} mainProgram;

bool picking = false;
int selectedFace = -1;
bool showVertexNormals = true;
bool showFaceNormals = true;
bool innverViewer = false;

void initShaders() {
  mainProgram.id = shader_Compile("shadow-pick");
  shader_Link(mainProgram.id);
  mainProgram.lightposUniform = glGetUniformLocation(mainProgram.id,"lightpos");
  mainProgram.materialUniform = glGetUniformLocation(mainProgram.id,"material");
  mainProgram.colorUniform = glGetUniformLocation(mainProgram.id,"color");
  mainProgram.depthonlyUniform = glGetUniformLocation(mainProgram.id,"depthonly");
  mainProgram.shadowmapUniform = glGetUniformLocation(mainProgram.id,"shadowMap");
  mainProgram.projVSUniform = glGetUniformLocation(mainProgram.id,"projVS");
  mainProgram.viewVSUniform = glGetUniformLocation(mainProgram.id,"viewVS");
  mainProgram.projLSUniform = glGetUniformLocation(mainProgram.id,"projLS");
  mainProgram.viewLSUniform = glGetUniformLocation(mainProgram.id,"viewLS");
  mainProgram.modelUniform = glGetUniformLocation(mainProgram.id,"model");
  mainProgram.pickingUniform = glGetUniformLocation(mainProgram.id,"picking");

  fprintf(stderr,"uniform: %d %d %d %d %d %d %d %d %d %d %d\n", mainProgram.lightposUniform, mainProgram.materialUniform, mainProgram.colorUniform, mainProgram.depthonlyUniform, mainProgram.shadowmapUniform, mainProgram.projVSUniform, mainProgram.viewVSUniform, mainProgram.projLSUniform, mainProgram.viewLSUniform, mainProgram.modelUniform, mainProgram.pickingUniform);
}

// Models
struct faceModelVAO manifold;
struct faceModelVAO pointer;
Vector3 pointerAxis = Vector3(0,0,1);
struct faceModelVAO sphere;
struct faceModelVAO delta;

void initObjects() {
  manifold = makeVAOForModel(readOBJElements("OBJModels/Manifold3.obj"));
  mesh_Init(&manifold);
  // mesh_PrintV2V();
  // mesh_PrintV2F();
  pointer = makeVAOForModel(readOBJElements("OBJModels/Pointer.obj"));
  sphere = makeVAOForModel(readOBJElements("OBJModels/Sphere.obj"));
  delta = makeVAOForModel(readOBJElements("OBJModels/delta.obj"));
}

// Shadow Map
#define SHADOW_WIDTH 2048
#define SHADOW_HEIGHT 2048
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

// Data for manifold vertex and face normals
// Manifold3 VAO contains vertex and normal info only

std::vector<Vector3> vertexNmlRotAxes;
std::vector<Vector3> faceCenters;
std::vector<Vector3> faceNormals;
std::vector<Vector3> faceNmlRotAxes;

void initVertexNormals() {
  for (int i = 0; i < manifold.vCnt; i++) {
    Vector3 nml = makeV3(normalFor(&manifold,i)).normalize();
    vertexNmlRotAxes.push_back((nml + pointerAxis)/2.0);
  }
}

void initFaceNormals() {
  for (int i = 0; i < manifold.indx.size(); i += 3) {
    GLuint A = manifold.indx[i];
    GLuint B = manifold.indx[i+1];
    GLuint C = manifold.indx[i+2];
    Vector3 Apos = makeV3(positionFor(&manifold,A));
    Vector3 Bpos = makeV3(positionFor(&manifold,B));
    Vector3 Cpos = makeV3(positionFor(&manifold,C));
    Vector3 center = (Apos + Bpos + Cpos) / 3.0;
    Vector3 nml = (Bpos - Apos).cross(Cpos - Apos).normalize();
    Vector3 axis = (nml + pointerAxis) / 2.0;
    faceCenters.push_back(center);
    faceNormals.push_back(nml);
    faceNmlRotAxes.push_back(axis);
  }
}

// Projection and View Matrices
Matrix4 projVSMatrix;
Matrix4 viewVSMatrix;
Matrix4 projLSMatrix;
Matrix4 viewLSMatrix;

Vector3 cameraPos;
Vector3 lightCameraPos;
Vector3 viewerCameraPos;
GLfloat cameraBackoff = -5;

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
  initObjects();
  initShadow();
  initLightTransform();
  initViewerTransform();
  initVertexNormals();
  initFaceNormals();
}

// path management
struct pathState {
  GLuint face;          // current face index
  Vector3 faceCenter;
  Vector3 faceNml;
  GLuint aIndx;         // index of vertex on outbound edge
  Vector3 aPos;
  Vector3 aNml;
  GLuint bIndx;         // index of vertex on outbound edge
  Vector3 bPos;
  Vector3 bNml;
  Vector3 pathPos;      // endpoint of path on outbound edge
  Vector3 pathNml;
  Vector3 pathDir;      // direction of path within face
};

bool leaveTrail = true;
struct pathState pathState;
struct pathState nextState;

// Markers
#define MARKER_MAX 5000
struct marker {
  Vector3 pos;
  Vector3 nml;
  Vector3 nmlAxis;
  float * clr;
};

std::vector<struct marker> markers;

void paths_ClearMarkers() {
  markers.clear();
}

void paths_AddMarker(Vector3 pos, Vector3 nml, float * clr) {
  if (markers.size() > MARKER_MAX) return;
  struct marker marker;
  marker.pos = pos;
  marker.nml = nml;
  marker.nmlAxis = (Vector3(0,0,1) + nml)/2.0;
  marker.clr = clr;
  markers.push_back(marker);
}

void paths_Start(struct pathState * currState) {
  if (selectedFace < 0) return;

  fprintf(stderr,"paths_Start\n");

  currState->face = selectedFace;
  currState->faceCenter = faceCenters[selectedFace];
  currState->faceNml = faceNormals[selectedFace];
  if (leaveTrail) paths_AddMarker(currState->faceCenter,currState->faceNml,YeloClr);

  GLuint * face = faceFor(&manifold,currState->face);
  currState->aIndx = face[0];
  currState->bIndx = face[1];
  currState->aPos = makeV3(positionFor(&manifold,currState->aIndx));
  currState->bPos = makeV3(positionFor(&manifold,currState->bIndx));
  currState->aNml = makeV3(normalFor(&manifold,currState->aIndx));
  currState->bNml = makeV3(normalFor(&manifold,currState->bIndx));
  if (leaveTrail) paths_AddMarker(currState->aPos,currState->aNml,GrenClr);
  if (leaveTrail) paths_AddMarker(currState->bPos,currState->bNml,GrenClr);

  currState->pathPos = (currState->aPos + currState->bPos) / 2.0;
  currState->pathNml = (currState->aNml + currState->bNml) / 2.0;
  currState->pathDir = currState->pathPos - currState->faceCenter;
  currState->pathDir.normalize();
  if (leaveTrail) paths_AddMarker(currState->pathPos,currState->pathNml,PrplClr);
  if (leaveTrail) paths_AddMarker(currState->faceCenter,currState->pathDir,PrplClr);
}

float solve(Vector3 e, Vector3 f, Vector3 b, Vector3 p) {
  float denom = (e-f).dot(p);
  if (denom == 0.0) return -1.0;
  float numer = (f-b).dot(p);
  return - numer / denom;
}

#define IN_INTERVAL(X,L,U) ( (X) >= (L) && (X) <= (U) )

void paths_Advance(struct pathState * currState, struct pathState * nextState) {
  printf("paths_Advance\n");

  nextState->face = mesh_AdjF(currState->face,currState->aIndx,currState->bIndx);
  nextState->faceCenter = faceCenters[nextState->face];
  nextState->faceNml = faceNormals[nextState->face];
  if (leaveTrail) paths_AddMarker(nextState->faceCenter,nextState->faceNml,BlueClr);

  GLuint cIndx = mesh_F2rV(nextState->face,currState->aIndx,currState->bIndx);
  Vector3 cPos = makeV3(positionFor(&manifold,cIndx));
  Vector3 cNml = makeV3(normalFor(&manifold,cIndx));
  if (leaveTrail) paths_AddMarker(cPos,cNml,GrenClr);

  Vector3 p = currState->pathDir.cross(currState->pathNml);
  p.normalize();
  if (leaveTrail) paths_AddMarker(currState->pathPos,p,RedClr);

  nextState->pathDir = nextState->faceNml.cross(p);
  nextState->pathDir.normalize();
  if (leaveTrail) paths_AddMarker(currState->pathPos,nextState->pathDir,PrplClr);

  float u = solve(currState->aPos,cPos,currState->pathPos,p);
  float v = solve(currState->bPos,cPos,currState->pathPos,p);

  // both solving is not necessarilty an error
  // passing through a vertex, the outbound direction may be an edge in which
  // case solution for one edge will be the existing vertex and solution for the
  // other edge will be the far vertex, in this case we pick far vertex as
  // next step
  if (IN_INTERVAL(u,0.0,1.0) && IN_INTERVAL(v,0.0,1.0)) {
    printf("both solve u %f v %f\n",u,v);
    return;
  }

  if (!IN_INTERVAL(u,0.0,1.0) && !IN_INTERVAL(v,0.0,1.0)) {
    printf("neither solve u %f v %f\n",u,v);
    return;
  }

  if (IN_INTERVAL(u,0.0,1.0)) {
    nextState->pathPos = u * pathState.aPos + (1-u) * cPos;
    nextState->pathNml = u * pathState.aNml + (1-u) * cNml;
    nextState->pathNml.normalize();
    nextState->aIndx = currState->aIndx;
    nextState->aPos = currState->aPos;
    nextState->aNml = currState->aNml;
    nextState->bIndx = cIndx;
    nextState->bPos = cPos;
    nextState->bNml = cNml;
    if (leaveTrail) paths_AddMarker(nextState->pathPos,nextState->pathNml,PrplClr);
  }
  else {
    nextState->pathPos = v * pathState.bPos + (1-v) * cPos;
    nextState->pathNml = v * pathState.bNml + (1-v) * cNml;
    nextState->pathNml.normalize();
    nextState->aIndx = cIndx;
    nextState->aPos = cPos;
    nextState->aNml = cNml;
    nextState->bIndx = currState->bIndx;
    nextState->bPos = currState->bPos;
    nextState->bNml = currState->bNml;
    if (leaveTrail) paths_AddMarker(nextState->pathPos,nextState->pathNml,PrplClr);
  }
  * currState = * nextState;
}

// annimation
struct animPos {
  Vector3 currentPos;
  Vector3 currentNml;
  Vector3 targetPos;
  Vector3 targetNml;
  Matrix4 pose;
};

struct animPos particle;
bool animating = false;
bool paused = true;
float distancePerCycle = 0.003;

void launch() {
  fprintf(stderr,"launch\n");
  leaveTrail = false;
  paths_Start(&pathState);
  particle.currentPos = pathState.pathPos;
  particle.currentNml = pathState.pathNml;
  paths_Advance(&pathState,&nextState);
  particle.targetPos = pathState.pathPos;
  particle.targetNml = pathState.pathNml;
  animating = true;
}

void animate(float distance) {
  if (!animating) return;
  if (paused) return;
  float d = particle.currentPos.distance(particle.targetPos);
  while (d <= distance) {
    distance = distance - d;
    paths_Advance(&pathState,&nextState);
    particle.currentPos = particle.targetPos;
    particle.currentNml = particle.targetNml;
    particle.targetPos = pathState.pathPos;
    particle.targetNml = pathState.pathNml;
    d = particle.currentPos.distance(particle.targetPos);
  }
  float u = distance / d;
  particle.currentPos = u * particle.targetPos + (1-u) * particle.currentPos;
  particle.currentNml = u * particle.targetNml + (1-u) * particle.currentNml;
  particle.currentNml.normalize();
}

void performCommands(json state) {
  if (state.find("command") == state.end()) return;
  fprintf(stderr,"command %c\n",state["command"].get<char>());
  if (state["command"] == 'c') paths_ClearMarkers();
  if (state["command"] == 's') paths_Start(&pathState);
  if (state["command"] == 't') leaveTrail = ! leaveTrail;
  if (state["command"] == 'a') paths_Advance(&pathState,&nextState);
  if (state["command"] == 'l') launch();
  if (state["command"] == 'p') paused = !paused;
}

Matrix4 matrixForPose(Vector3 pos, Vector3 dir, Vector3 up) {
  Vector3 left = up.cross(dir).normalize();
  up = dir.cross(left).normalize();
  return mutil_Translate(pos) * mutil_ForFrame(left,up,dir);
}

void setParticlePose() {
  // smooth path direction a little using particle normal
  Vector3 dir = pathState.pathDir.cross(particle.currentNml);
  dir = particle.currentNml.cross(dir);
  dir.normalize();
  particle.pose = matrixForPose(particle.currentPos + 0.02 * particle.currentNml,dir,particle.currentNml);
  //paths_AddMarker(particle.currentPos,particle.currentNml,OrngClr);
}

void drawSceneContents(json state) {
  glUseProgram(mainProgram.id);

  Matrix4 tb = Matrix4();
  if (!state["innerviewer"]) {
    tb_Matrix(tb);
  }

  glUniform3fv(mainProgram.lightposUniform,1,LightPos);
  glUniform4fv(mainProgram.materialUniform,1,BasicMaterial);

  // draw manifold
  glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,tb.get());
  glUniform3fv(mainProgram.colorUniform,1,PuttyClr);
  glBindVertexArray(manifold.VAO);
  glDrawElements(GL_TRIANGLES,manifold.indx.size(),GL_UNSIGNED_INT,0);

  if (state["picking"]) return;

  glUniform3fv(mainProgram.colorUniform,1,BlakClr);
  glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
  glDrawElements(GL_TRIANGLES,manifold.indx.size(),GL_UNSIGNED_INT,0);
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

  if (state["showVertexNormals"]) {
    glBindVertexArray(pointer.VAO);
    glUniform3fv(mainProgram.colorUniform,1,GrenClr);
    for (int i = 0; i < manifold.vCnt; i++) {
      Vector3 pos = makeV3(positionFor(&manifold,i));
      Matrix4 m = tb * mutil_Translate(pos) * mutil_Rotate(180,vertexNmlRotAxes[i]) * mutil_Scale(Vector3(0.05,0.05,0.05));
      glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
      glDrawElements(GL_TRIANGLES,pointer.indx.size(),GL_UNSIGNED_INT,0);
    }
  }

  if (state["showFaceNormals"]) {
    glBindVertexArray(pointer.VAO);
    for (int i = 0; i < manifold.iCnt/3; i++) {
      Matrix4 m = tb * mutil_Translate(faceCenters[i]) * mutil_Rotate(180,faceNmlRotAxes[i]) * mutil_Scale(Vector3(0.05,0.05,0.05));
      (i == selectedFace) ? glUniform3fv(mainProgram.colorUniform,1,YeloClr) : glUniform3fv(mainProgram.colorUniform,1,BlueClr);
      glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
      glDrawElements(GL_TRIANGLES,pointer.indx.size(),GL_UNSIGNED_INT,0);
    }
  }

  // draw trail of pointers
  glBindVertexArray(pointer.VAO);
  for (int i = 0; i < markers.size(); i++) {
    Matrix4 m = tb * mutil_Translate(markers[i].pos) * mutil_Rotate(180,markers[i].nmlAxis) * mutil_Scale(Vector3(0.05,0.05,0.05));
    glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
    glUniform3fv(mainProgram.colorUniform,1,markers[i].clr);
    glDrawElements(GL_TRIANGLES,pointer.indx.size(),GL_UNSIGNED_INT,0);
  }

  // animate the particle
  if (animating) {
    //glBindVertexArray(sphere.VAO);
    //Matrix4 m = tb * mutil_Translate(particle.currentPos) * mutil_Translate(0.05 * particle.currentNml) * mutil_Scale(Vector3(0.02,0.02,0.02));
    //glDrawElements(GL_TRIANGLES,sphere.indx.size(),GL_UNSIGNED_INT,0);
    setParticlePose();
    if (!state["innerviewer"]) {
      glBindVertexArray(delta.VAO);
      Matrix4 m = tb * particle.pose * mutil_Scale(Vector3(0.05,0.05,0.05));
      glUniform3fv(mainProgram.colorUniform,1,OrngClr);
      glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
      glDrawElements(GL_TRIANGLES,delta.indx.size(),GL_UNSIGNED_INT,0);
    }
    // // nml used in particle pose
    // glBindVertexArray(pointer.VAO);
    // Vector3 axis = Vector3(0,0,1) + particle.currentNml;
    // m = tb * mutil_Translate(particle.currentPos) * mutil_Rotate(180,axis) * mutil_Scale(Vector3(0.03,0.03,0.03));
    // glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
    // glUniform3fv(mainProgram.colorUniform,1,YeloClr);
    // glDrawElements(GL_TRIANGLES,pointer.indx.size(),GL_UNSIGNED_INT,0);
    //
    // // dir used in particle pose
    // Vector3 dir = pathState.pathDir.cross(particle.currentNml);
    // dir = particle.currentNml.cross(dir);
    // dir.normalize();
    // axis = Vector3(0,0,1) + dir;
    // m = tb * mutil_Translate(particle.currentPos) * mutil_Rotate(180,axis) * mutil_Scale(Vector3(0.03,0.03,0.03));
    // glUniformMatrix4fv(mainProgram.modelUniform,1,GL_FALSE,m.get());
    // glUniform3fv(mainProgram.colorUniform,1,GrenClr);
    // glDrawElements(GL_TRIANGLES,pointer.indx.size(),GL_UNSIGNED_INT,0);
  }
}

void setProjViewFromLight() {
  // light space and viewer space are the same - view from light source
  // when rendering only to depth texture (LightDepth is true) only use light space
  // when render to window for visualizing (LightDepth is false) use both spaces
  glUniformMatrix4fv(mainProgram.projLSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(mainProgram.viewLSUniform,1,GL_FALSE,viewLSMatrix.get());
  glUniformMatrix4fv(mainProgram.projVSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(mainProgram.viewVSUniform,1,GL_FALSE,viewLSMatrix.get());
}

void setProjViewFromViewer(json state) {
  // supply shaders with all four matricies
  if (state["innerviewer"]) {
    Matrix4 pose = particle.pose;
    Matrix4 proj = mutil_PerspectiveMatrix(80,DrawAspect,0.001,20.0);
    Matrix4 view = mutil_Rotate(180,Vector3(0,1,0)) * pose.invert();
    glUniformMatrix4fv(mainProgram.projVSUniform,1,GL_FALSE,proj.get());
    glUniformMatrix4fv(mainProgram.viewVSUniform,1,GL_FALSE,view.get());
  }
  else {
    glUniformMatrix4fv(mainProgram.projVSUniform,1,GL_FALSE,projVSMatrix.get());
    glUniformMatrix4fv(mainProgram.viewVSUniform,1,GL_FALSE,viewVSMatrix.get());
  }
  glUniformMatrix4fv(mainProgram.projLSUniform,1,GL_FALSE,projLSMatrix.get());
  glUniformMatrix4fv(mainProgram.viewLSUniform,1,GL_FALSE,viewLSMatrix.get());
}

// serves as wrapper to provide two stage shadow work
void draw_Standard(json state) {
  // pass 1 - draw from point of view of light direction, only retain depth information
  // in FBO's depth texture
  glViewport(0,0,SHADOW_WIDTH,SHADOW_HEIGHT);
  glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
  glClear(GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_FRONT);
  setProjViewFromLight();
  glUniform1i(mainProgram.depthonlyUniform,true);
  drawSceneContents(state);
  glBindFramebuffer(GL_FRAMEBUFFER,0);

  // pass 2 - draw scene for viewer using captured depth information
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, shadowTexID);
  glUniform1i(mainProgram.shadowmapUniform,0);
  glViewport(0,0,DrawWidth,DrawHeight);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_BACK);
  setProjViewFromViewer(state);
  glUniform1i(mainProgram.depthonlyUniform,false);
  drawSceneContents(state);
}

void draw_Picking(json state) {
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, shadowTexID);
  glUniform1i(mainProgram.shadowmapUniform,0);
  glViewport(0,0,DrawWidth,DrawHeight);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_NONE);
  setProjViewFromViewer(state);
  glUniform1i(mainProgram.depthonlyUniform,false);
  state["picking"] = true;
  drawSceneContents(state);
  state["picking"] = false;
}

void draw_Scene(json state) {
  if (state["picking"]) {
    fprintf(stderr,"picking\n");
    float clearClr[4];
    glGetFloatv(GL_COLOR_CLEAR_VALUE,clearClr);
    glClearColor(0.0,0.0,0.0,1.0);
    glUniform1i(mainProgram.pickingUniform,true);
    draw_Picking(state);
    glClearColor(clearClr[0],clearClr[1],clearClr[2],clearClr[3]);
    unsigned char pixel[4];
    int x = state["pickingX"];
    int y = state["pickingY"];
    glReadPixels(x,DrawHeight-y,1,1,GL_RGB,GL_UNSIGNED_BYTE,&pixel);
    selectedFace = pixel[0] + (pixel[1] << 8) + (pixel[2] << 16);
    fprintf(stderr,"pick %d\n",selectedFace);
  }
  else {
    performCommands(state);
    animate(distancePerCycle);
    glUniform1i(mainProgram.pickingUniform,false);
    draw_Standard(state);
  }
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

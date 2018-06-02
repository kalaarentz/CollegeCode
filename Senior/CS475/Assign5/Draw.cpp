#include <iostream>
#include <SDL.h>
#include <GL/glew.h>
#include <SDL_opengl.h>
#include <gl/glu.h>
#include <stdio.h>
#include <string>
#include <math.h>
#include <cstdlib>
#include <vector>

#include <openvr.h>

#include "Vectors.h"
#include "Matrices.h"
#include "shaderUtil.h"
#include "model.h"
#include "stdModels.h"
#include "OBJModel.h"
#include "modelVAO.h"
#include "matrixUtil.h"

#include "Draw.h"

#define R2D(x)  ( ( (x) / M_PI) * 180.0)
#define SQR(x)  ( (x) * (x) )

/* define several colors */
GLfloat BlueClr[] = {0.2f, 0.2f, 0.8f, 1.0f};
GLfloat YeloClr[] = {0.8f, 0.8f, 0.0f, 1.0f};
GLfloat GrenClr[] = {0.0f, 0.8f, 0.0f, 1.0f};
GLfloat RedClr[]  = {0.8f, 0.0f, 0.0f, 1.0f};
GLfloat BrwnClr[] = {0.5f, 0.3f, 0.2f, 1.0f};
GLfloat LBrnClr[] = {0.8f, 0.6f, 0.5f, 1.0f};
GLfloat PrplClr[] = {0.7f, 0.2f, 0.7f, 1.0f};
GLfloat BlakClr[] = {0.0f, 0.0f, 0.0f, 1.0f};
GLfloat WhitClr[] = {1.0f, 1.0f, 1.0f, 1.0f};
GLfloat GrayClr[] = {0.3f, 0.3f, 0.3f, 1.0f};

GLfloat lightPos[] = {0.0f,1.0f,0.5f,0.0f};
GLfloat basicMaterial[4] = {0.2f, 0.7f, 1.0f, 50.0f};
GLfloat specMaterial[4] = {0.0f, 0.2f, 1.0f, 30.0f};
float backgroundColor[] = {0.15f, 0.15f, 0.15f, 1.0f};

Vector3 previousPosition;
Vector3 controllerPos;
Vector3 darkStarPos = Vector3(25, 0.5, -4);
int frameCnt = 0;
bool = triggerState = false;
bool shouldDrawSphere = false;

// this will be used to make my physics calculations
struct BallInfo {
	Vector3 velocity;
	Vector3 position;
	GLfloat color[4];
};

std::vector<struct BallInfo *> thrownObjects;

struct faceModelVAO axis;
struct faceModelVAO sphere;
struct faceModelVAO darkStar;
struct faceModelVAO pad;

void initModels() {
  axis = makeVAOForModel(faceCoordTetra());
  sphere = makeVAOForModel(readOBJArrays("OBJModels/sphere.obj"));
  darkStar = makeVAOForModel(readOBJArrays("OBJModels/sphere.obj"));
  pad = makeVAOForModel(readOBJArrays("OBJModels/pad.obj"));

  srand((unsigned int)time(NULL));

  fprintf(stderr,"models sphere %d \n",sphere.vCnt);
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

// shaders
struct {
  GLuint id;
  GLint projVSUniform;
  GLint viewVSUniform;
  GLint projLSUniform;
  GLint viewLSUniform;
  GLint modelUniform;
  GLint lightposUniform;
  GLint materialUniform;
  GLint colorUniform;
GLint shadowmapUniform;
} mainProgram;

struct {
	GLuint id;
	GLint depthmatUniform;
	GLint modelUniform;
} shadowProgram;

void initShaders() {
	mainProgram.id = shader_Compile("main");
	shader_Link(mainProgram.id);

	mainProgram.projVSUniform = glGetUniformLocation(mainProgram.id, "projVS");
	mainProgram.viewVSUniform = glGetUniformLocation(mainProgram.id, "viewVS");
	mainProgram.projLSUniform = glGetUniformLocation(mainProgram.id, "projLS");
	mainProgram.viewLSUniform = glGetUniformLocation(mainProgram.id, "viewLS");
	mainProgram.modelUniform = glGetUniformLocation(mainProgram.id, "model");
	mainProgram.lightposUniform = glGetUniformLocation(mainProgram.id, "lightpos");
	mainProgram.materialUniform = glGetUniformLocation(mainProgram.id, "material");
	mainProgram.colorUniform = glGetUniformLocation(mainProgram.id, "color");
	mainProgram.shadowmapUniform = glGetUniformLocation(mainProgram.id, "shadowMap");

	fprintf(stderr, "main shader locations %d %d %d %d %d %d %d %d %d\n",
		mainProgram.projVSUniform, mainProgram.viewVSUniform, mainProgram.projLSUniform, mainProgram.viewLSUniform, mainProgram.modelUniform, mainProgram.lightposUniform, mainProgram.materialUniform, mainProgram.colorUniform, mainProgram.shadowmapUniform);

	shadowProgram.id = shader_Compile("shadow");
	shader_Link(shadowProgram.id);

	shadowProgram.depthmatUniform = glGetUniformLocation(shadowProgram.id, "depthMat");
	shadowProgram.modelUniform = glGetUniformLocation(shadowProgram.id, "model");

	fprintf(stderr, "shadow shader locations %d %d\n", shadowProgram.depthmatUniform, shadowProgram.modelUniform);
}

Matrix4 projLS;
Matrix4 viewLS;

void initTransforms() {
	// light space matrices
	Vector3 lightdir = Vector3(lightPos[0], lightPos[1], lightPos[2]);
	lightdir.normalize();

	projLS = mutil_OrthographicMatrix(-15, 15, -15, 15, -15, 15.0);

	GLfloat xAngle = (GLfloat)R2D(atan2(lightdir.y, sqrt(SQR(lightdir.x) + SQR(lightdir.z))));
	GLfloat yAngle = (GLfloat)-R2D(atan2(lightdir.x, lightdir.z));

	viewLS = mutil_Rotate(xAngle, Vector3(1, 0, 0)) * mutil_Rotate(yAngle, Vector3(0, 1, 0));

	mutil_Print4x4(true, "projLS", projLS);
	mutil_Print4x4(true, "viewLS", viewLS);
}

void draw_Init() {
	initModels();
	initShadow();
	initShaders();
	initTransforms();
	fprintf(stderr, "draw_Init done\n");
}

bool isCurrentProgram(GLint id) {
	GLint arg;
	glGetIntegerv(GL_CURRENT_PROGRAM, &arg);
	return arg == id;
}

// Data for objects

void drawContents() {
	frameRate++;
	GLint modelUniform = (isCurrentProgram(mainProgram.id)) ? (mainProgram.modelUniform) : (shadowProgram.modelUniform);
	Vector3 controllerPos = Vector3(controller.x, controller.y, controller.z);

	glBindVertexArray(axis.VAO);
	glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(Vector3(-6, 0, -6)) * mutil_Translate(Vector3(0.5, 0, 0)) * mutil_Scale(Vector3(2, 0.5, 0.5))).get());
	if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, RedClr);
	glDrawArrays(GL_TRIANGLES, 0, axis.vCnt);
	glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(Vector3(-6, 0, -6)) * mutil_Translate(Vector3(0, 0.5, 0)) * mutil_Scale(Vector3(0.5, 2, 0.5))).get());
	if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, GrenClr);
	glDrawArrays(GL_TRIANGLES, 0, axis.vCnt);
	glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(Vector3(-6, 0, -6)) * mutil_Translate(Vector3(0, 0, 0.5)) * mutil_Scale(Vector3(0.5, 0.5, 2))).get());
	if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, BlueClr);
	glDrawArrays(GL_TRIANGLES, 0, axis.vCnt);

	// Drawing for objects

	// pad
	glBindVertexArray(pad.VAO);
	if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, PrplClr);
	glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(Vector3(0, 0, 0)) * mutil_Scale(Vector3(2, 1, 2))).get());
	glDrawArrays(GL_TRIANGLES, 0, pad.vCnt);

	//dark star
	glBindVertexArray(sphere.VAO);
	if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, BlakClr);
	glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(darkStarPos) *  mutil_Rotate(-90, Vector3(1, 0, 0)) * mutil_Scale(Vector3(.5, .5, .5))).get());
	glDrawArrays(GL_TRIANGLES, 0, sphere.vCnt);

	// drawing all the other objects
	for (int i = 0; i < thrownObjects.size(); i++) {
		struct BallInfo ball = &thrownObjects.at(i);
		if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, ball->color);
		glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(ball->position) *  mutil_Rotate(-90, Vector3(1, 0, 0)) * mutil_Scale(Vector3(.1,.1,.1))).get());
		glDrawArrays(GL_TRIANGLES, 0, sphere.vCnt);
	}

	if(shouldDrawSphere) {
		if (isCurrentProgram(mainProgram.id)) glUniform4fv(mainProgram.colorUniform, 1, WhitClr);
		glUniformMatrix4fv(modelUniform, 1, GL_FALSE, (mutil_Translate(controllerPos) *  mutil_Rotate(-90, Vector3(1, 0, 0)) * mutil_Scale(Vector3(.1,.1,.1))).get());
		glDrawArrays(GL_TRIANGLES, 0, sphere.vCnt);
	}

}

void draw_Scene(Matrix4 pMat,Matrix4 vMat) {
  // MainApplication::RenderStereoTargets provides framebuffer wrapping around this
  glUseProgram(mainProgram.id);

  glCullFace(GL_BACK);

  glUniform3fv(mainProgram.lightposUniform,1,lightPos);
  glUniform4fv(mainProgram.materialUniform,1,basicMaterial);

  glUniformMatrix4fv(mainProgram.projVSUniform,1,GL_FALSE,pMat.get());
  glUniformMatrix4fv(mainProgram.viewVSUniform,1,GL_FALSE,vMat.get());
  glUniformMatrix4fv(mainProgram.projLSUniform,1,GL_FALSE,projLS.get());
  glUniformMatrix4fv(mainProgram.viewLSUniform,1,GL_FALSE,viewLS.get());

  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, shadowTexID);
  glUniform1i(mainProgram.shadowmapUniform,0);

  drawContents();

  glUseProgram(0);
}

void draw_Shadow() {
  glUseProgram(shadowProgram.id);

  glViewport(0,0,SHADOW_WIDTH,SHADOW_HEIGHT);
  glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);

  glEnable(GL_DEPTH_TEST);
  glClearDepth(1.0);
  glClear(GL_DEPTH_BUFFER_BIT);
  glCullFace(GL_FRONT);

  glUniformMatrix4fv(shadowProgram.depthmatUniform,1,GL_FALSE,(projLS * viewLS).get());

  drawContents();

  glDisable(GL_DEPTH_TEST);

  glBindFramebuffer(GL_FRAMEBUFFER,0);

  glUseProgram(0);
}

void draw_Update() {
	// Update physics of objects for next frame
	struct BallInfo * ball;
	for (int i = 0; i < thrownObjects.size(); i++) {
		struct BallInfo *ball = thrownObjects.at(i);
		Vector3 dds = darkStarPos - ball->position;
		Vector3 acceleration = (1.0/ (SQR(dds.x) + SQR(dds.y) + SQR(dds.z))) * dds;
		float accelerationForFrameRate = 0.08;
		ball->velocity += accelerationForFrameRate * acceleration;
		ball->position += ball->velocity;
	}

}

void draw_UpdateDevicePose(unsigned int device, Matrix4 mat) {
	Vector4 pos = mat * Vector4(0,0,0,1);
	previousPosition = controllerPos;
	controllerPos = Vector3(pos.x,pos.y,pos.z);
}

void draw_UpdateDeviceState(unsigned int device, bool trigger) {
  // Capture controller trigger state
	if(triggerState != trigger) {
		triggerState = trigger;
		if(!trigger) {
			struct BallInfo * ball = (struct BallInfo *) malloc(sizeof(struct BallInfo));
			ball->position;
			ball->velocity: controllerPos - previousPosition;
			ball->color[0] = (rand() % 100) *.01;
			ball->color[1] = (rand() % 100) * .01;
			ball->color[2] = (rand() % 100) * .01;
			ball->color[3] = 1.0f;

			thrownObjects.push_back(ball);
			shouldDrawSphere = false;
		} else {
			shouldDrawSphere = true;
		}
	}
}

/*
 * Author: Kala Arentz
 * Created: 9/30/17
 * Resources: Suzy Ratcliff
 */
#include <stdio.h>
#include <math.h>
#include "platformGL.h"
#include <vector>

#include "Vectors.h"
#include "model.h"
#include "stdModels.h"
#include "draw.h"

#define FRAME_SIZE 10

struct faceModel axisModel;
struct faceModel coneModel;
struct faceModel tetraModel;
struct faceModel tetra1Model;
struct faceModel tetra2Model;

void draw_Init() {
    axisModel = faceCoordTetra();
    coneModel = faceCone(10);
    tetraModel = faceTetrahedron();
    tetra1Model = stellateFaceModel(tetraModel);
    tetra2Model = stellateFaceModel(tetra1Model);
}

void drawWithSpikes(struct faceModel model, float scale) {
    model.draw();
    Vector3 Z = Vector3(0, 0, 1);
    for (int i = 0; i < model.nmls.size(); i+=3) {
        glPushMatrix();

        // get the faces of the model
        Vector3 v1 = model.vrts[i];
        Vector3 v2 = model.vrts[i+1];
        Vector3 v3 = model.vrts[i+2];
        Vector3 rotate = model.nmls[i] + Z;

        GLfloat translate_1 = (v1.x + v2.x + v3.x)/ 3;
        GLfloat translate_2 = (v1.y + v2.y + v3.y)/ 3;
        GLfloat translate_3 = (v1.z + v2.z + v3.z)/ 3;

        glTranslatef(translate_1, translate_2, translate_3);
        glRotatef(180, rotate.x, rotate.y, rotate.z);

        // dont scale up to Vector Z
        glScalef(scale/ 10.0, scale/10.0, scale);
        coneModel.draw();
        glPopMatrix();
    }
}

void drawCoordFrame(float size) {
    glPushMatrix();
    glColor3f(1, 0.2, 0.2);
    glScalef(size, size / 10.0, size / 10.0);
    axisModel.draw();
    glPopMatrix();

    glPushMatrix();
    glColor3f(0.2, 1, 0.2);
    glScalef(size / 10.0, size, size / 10.0);
    axisModel.draw();
    glPopMatrix();

    glPushMatrix();
    glColor3f(0.2, 0.2, 1);
    glScalef(size / 10.0, size / 10.0, size);
    axisModel.draw();
    glPopMatrix();
}

void draw_Scene(json state) {
    glPushMatrix();
    glTranslatef(-5.0, -5.0, -5.0);
    drawCoordFrame(FRAME_SIZE);
    glPopMatrix();

    glPushMatrix();
    glColor3f(1, 1, 0.2);
    glScalef(3, 3, 3);
    drawWithSpikes(tetraModel, 1);
    glPopMatrix();

    glPushMatrix();
    glTranslatef(0, -6, 0);
    glColor3f(0.2, 1, 1);
    glScalef(3, 3, 3);
    tetra1Model.draw();
    glPopMatrix();

    glPushMatrix();
    glTranslatef(-6, 0, 0);
    glColor3f(1, 0.2, 1);
    glScalef(3, 3, 3);
    tetra2Model.draw();
    glPopMatrix();
}

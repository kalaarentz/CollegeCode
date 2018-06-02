#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <float.h>
#include <string.h>
#include <stdbool.h>

#include "platformGL.h"
#include "modelVAO.h"
#include "minHeap.h"
#include "meshTables.h"

#include "dijkstra.h"

#define SQR(x) ((x) * (x))

// vertex attributes consist of Vector3 for coordinates, Vector3 for normals and Vector2 for UVs
#define ATTR_SIZE (8)
#define ATTR_BYTES (ATTR_SIZE * sizeof(GLfloat))

int srcVertex, sinkVertex;
enum dijkstraModes dijkstraMode = IDLE;
int singleStep;
bool animating = true;
bool haveWall = false;

struct faceModelVAO * theModel;

void dijkstra_Init(struct faceModelVAO * model) {
  theModel = model;
}

void initUVs() {
  // set UVs of all vertices to 0.0 and FLT_MAX
  // associate model attrs with buffer data
  for (int i = 0; i < theModel->vCnt; i++) {
    GLfloat * uvs = uvsFor(theModel,i);
    uvs[0] = 0.0;
    uvs[1] = FLT_MAX;
  }
  glBindBuffer(GL_ARRAY_BUFFER,theModel->VBO[0]);
  glBufferData(GL_ARRAY_BUFFER,theModel->vCnt * ATTR_BYTES,theModel->attr.data(),GL_DYNAMIC_DRAW);
}

void dijkstra_PickEndPoints() {
  // init model uvs, init heap, randomly pick src and sink vertices, insert src into heap

  initUVs();
  heap_Init();

  srcVertex = (rand() % theModel->vCnt);
  sinkVertex =(rand() % theModel->vCnt);

  fprintf(stderr,"pick %d %d\n",srcVertex,sinkVertex);

  heap_Insert(srcVertex,0.0);
  dijkstraMode = EXPANDING;
  //HaveWall = false;
}

void dijkstra_ReverseEndPoints() {
  // reverse src and sink and restart

  initUVs();
  heap_Init();

  int v = srcVertex;
  srcVertex = sinkVertex;
  sinkVertex = v;

  heap_Insert(srcVertex,0.0);
  dijkstraMode = EXPANDING;
  //HaveWall = false;
}

void disjkatra_UpdateModelAttrs(int vertex) {
  // update model attr data for vertex
  glBindBuffer(GL_ARRAY_BUFFER,theModel->VBO[0]);
  glBufferSubData(GL_ARRAY_BUFFER,vertex * ATTR_BYTES,ATTR_BYTES,&(theModel->attr[vertex*ATTR_SIZE]));
}

float distance(int a, int b) {
  GLfloat * aPos = positionFor(theModel,a);
  GLfloat * bPos = positionFor(theModel,b);
  return sqrt(SQR(aPos[0] - bPos[0]) + SQR(aPos[1] - bPos[1]) + SQR(aPos[2] - bPos[2]));
}

void dijkstra_ExpandOneVertex() {
// run Dijkstra for one vertex -- remove vertex from heap, if sink return, if marked as already enumerated return, if not mark as enumerated  update model data for vertex, get list of neighbors and loop, calc new distance and compare to old, if new smaller update vertex data, if neighbor has been enumerated then return otherwise insert into heap, vertices may be inserted multiple times before being enumerated

//  if (!SingleStep) return;
//  SingleStep = 0;

  int vertex; float value;
  if (!heap_Remove(&vertex,&value)) {
    dijkstraMode = IDLE;
    printf("Dijkstra ended with empy heap before finding sink\n");
    return;
  }

  if (vertex == sinkVertex) {
    dijkstraMode = IDLE;
  //  dijkstraMode = (HaveWall) ? (IDLE) : (PATHING);
    return;
  }

  if (uvsFor(theModel,vertex)[0] == 1.0) return;

  uvsFor(theModel,vertex)[0] = 1.0;
  disjkatra_UpdateModelAttrs(vertex);

  std::vector<int> neighs = mesh_V2V(vertex);

  for (int i = 0; i < neighs.size(); i++) {
    int neigh = neighs[i];

    float newdistance = value + distance(vertex,neigh);
    float olddistance = uvsFor(theModel,neigh)[1];
    if (newdistance < olddistance) {
      uvsFor(theModel,neigh)[1] = newdistance;
      disjkatra_UpdateModelAttrs(neigh);
    }

    if (uvsFor(theModel,neigh)[0] == 0.0) heap_Insert(neigh,uvsFor(theModel,neigh)[1]);
  }
}

void dijkstra_ExpandBoundary() {
  // expand boundary one vertex at a time for the current size of the heap

  for (int cnt = heap_Size() ; cnt > 0; cnt--) {
    dijkstra_ExpandOneVertex();
  }
}

void appendToPath(struct faceModel & path, int vertex) {
  float *vc = positionFor(theModel,vertex);
  Vector3 p1 = Vector3(vc[0],vc[1],vc[2]);
  float *vn = normalFor(theModel,vertex);
  Vector3 n1 = Vector3(vn[0],vn[1],vn[2]);
  n1 = 0.05 * n1;
  Vector3 p2 = p1 + n1;



  Vector3 lastvert = path.vrts[path.vrts.size()-1];

}

struct faceModel dijkstra_MakePath() {
  struct faceModel path;

  appendToPath(path,sinkVertex);

  int vertex = sinkVertex;
  while (vertex != srcVertex) {
    std::vector<int> neighs = mesh_V2V(vertex);
    int minvertex = vertex;
    float mindist = uvsFor(theModel,vertex)[1];
    for (int i = 0; i < neighs.size(); i++) {
      int neigh = neighs[i];
      if (uvsFor(theModel,neigh)[1] < mindist) {
        minvertex = neigh;
        mindist = uvsFor(theModel,neigh)[1];
      }
    }
    if (vertex == minvertex) break;
    vertex = minvertex;
    appendToPath(path,vertex);
  }
  haveWall = true;
  dijkstraMode = IDLE;
  return path;
}

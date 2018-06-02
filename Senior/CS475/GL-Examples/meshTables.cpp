
#include <vector>

#include "model.h"

#include "meshTables.h"

struct faceModelVAO * aModel;

std::vector<std::vector<int>> V2VTable;
std::vector<std::vector<int>> V2FTable;

void insert(std::vector<int> & list, int v) {
  for (int i = 0; i < list.size(); i++) {
    if (list[i] == v) return;
  }
  list.push_back(v);
}

void mesh_Init(struct faceModelVAO * model) {
  printf("InitMeshTables\n");

  aModel = model;

  V2VTable.clear();
  V2FTable.clear();
  for(int i = 0; i < aModel->vCnt; i++) V2VTable.push_back(std::vector<int>());
  for(int i = 0; i < aModel->iCnt; i++) V2FTable.push_back(std::vector<int>());

  fprintf(stderr,"mesh_Init %ld %ld\n",V2VTable.size(),V2FTable.size());

  for (int f = 0; f < aModel->indx.size(); f += 3) {
    for (int i = 0; i < 3; i += 3) {
      int v0 = aModel->indx[f];
      int v1 = aModel->indx[f+1];
      int v2 = aModel->indx[f+2];
      insert(V2VTable[v0],v1);
      insert(V2VTable[v0],v2);
      insert(V2VTable[v1],v0);
      insert(V2VTable[v1],v2);
      insert(V2VTable[v2],v0);
      insert(V2VTable[v2],v1);
      insert(V2FTable[v0],f/3);
      insert(V2FTable[v1],f/3);
      insert(V2FTable[v2],f/3);
    }
  }
}

std::vector<int> mesh_V2V(int vertex) {
  return V2VTable[vertex];
}

std::vector<int> mesh_V2F(int vertex) {
  return V2FTable[vertex];
}

unsigned int * mesh_F2V(int Face) {
  return &(aModel->indx[3*Face]);
}

bool belongsTo(int indx, std::vector<int> & list) {
//  fprintf(stderr,"belongsTo %d ",indx);
  for (int i = 0; i < list.size(); i++) {
//    fprintf(stderr," %d",list[i]);
    if (indx == list[i]) return true;
  }
  return false;
}

int mesh_AdjF(int face, int a, int b) {
  std::vector<int> aFaces = mesh_V2F(a);
  std::vector<int> bFaces = mesh_V2F(b);

  // fprintf(stderr,"AdjF a %d ",a);
  // for (int i = 0; i < aFaces.size(); i++) fprintf(stderr,"  %d ",aFaces[i]);
  // fprintf(stderr,"\n");
  //
  // fprintf(stderr,"AdjF b %d ",b);
  // for (int i = 0; i < bFaces.size(); i++) fprintf(stderr,"  %d ",bFaces[i]);
  // fprintf(stderr,"\n");

  for (int i = 0; i < aFaces.size(); i++) {
    if (aFaces[i] != face && belongsTo(aFaces[i],bFaces) ) return aFaces[i];
  }
  printf("error in AdjF\n");
  return -1;
}

int mesh_F2rV(int face, int a, int b) {
  unsigned int * faces = faceFor(aModel,face);
  unsigned int v1 = faces[0];
  unsigned int v2 = faces[1];
  unsigned int v3 = faces[2];

  // fprintf(stderr,"F2rV a %d b %d   %d %d %d\n",a,b,v1,v2,v3);

  if (v1 != a && v1 != b) return v1;
  if (v2 != a && v2 != b) return v2;
  if (v3 != a && v3 != b) return v3;

  return -1;
}

void mesh_PrintV2V() {
  for (int i = 0; i < aModel->vCnt; i++) {
    fprintf(stderr,"v %d :: v ",i);
    for (int j = 0; j < V2VTable[i].size(); j++) fprintf(stderr,"%d ",V2VTable[i][j]);
    fprintf(stderr,"\n");
  }
}

void mesh_PrintV2F() {
  for (int i = 0; i < aModel->vCnt; i++) {
    fprintf(stderr,"v %d :: f ",i);
    for (int j = 0; j < V2FTable[i].size(); j++) fprintf(stderr,"%d ",V2FTable[i][j]);
    fprintf(stderr,"\n");
  }
}

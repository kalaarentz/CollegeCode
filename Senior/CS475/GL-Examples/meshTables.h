#ifndef MESHTABLES_H
#define MESHTABLES_H

#include "modelVAO.h"

// Provides a Lookup table to find neighbors of a vertex
// Computes table using order data contained in ObjModelElements structure

void mesh_Init(struct faceModelVAO * model);

std::vector<int> mesh_V2V(int Vertex);
std::vector<int> mesh_V2F(int Vertex);
unsigned int * mesh_F2V(int Face);
int mesh_AdjF(int Face, int A, int B);
int mesh_F2rV(int Face, int A, int B); // return 3rd vertex of F

void mesh_PrintV2V();
void mesh_PrintV2F();

#endif

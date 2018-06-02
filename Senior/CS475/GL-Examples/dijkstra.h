#ifndef DIJKSTRA_H
#define DIJKSTRA_H

// ===============================
// Dijkstra's Algorithm
// use Vertex UV[0] -- 1.0 if vertex neighs have been enumerated, 0.0 otherwise
// use Vertex UV[1] -- min distance to vertex from SrcVertex

extern int srcVertex, sinkVertex;
enum dijkstraModes {IDLE, EXPANDING, PATHING};
extern enum dijkstraModes dijkstraMode;
extern int singleStep;
extern bool animating;
extern bool haveWall;

void dijkstra_Init(struct faceModelVAO * model);
void dijkstra_PickEndPoints();
void dijkstra_ReverseEndPoints();
void disjkatra_UpdateModelAttrs(int vertex);
void dijkstra_ExpandOneVertex();
void dijkstra_ExpandBoundary();

struct faceModel dijkstra_MakePath();

#endif

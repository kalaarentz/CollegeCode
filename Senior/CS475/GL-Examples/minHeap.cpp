
#include <vector>

#include "minHeap.h"


struct heapElt {
  int vertex;
  float value;

  heapElt() : vertex(-1), value(0) {};
  heapElt(int vertex, float value) : vertex(vertex), value(value) {};

};

std::vector<struct heapElt> heap;

void heap_Init() {
  heap.clear();
  // fill slot 0 with bogus entry
  heap.push_back(heapElt(-1,0));
}

int heap_Size() {
  return heap.size()-1;
}

void heap_Insert(int vertex, float value) {
  heap.push_back(heapElt(vertex,value));
  int i = heap_Size();
  int p = i / 2;
  while (p >= 1 && heap[i].value < heap[p].value) {
    struct heapElt elt = heap[p];
    heap[p] = heap[i];
    heap[i] = elt;
    i = p;
    p = i / 2;
  }
}

int heap_Remove(int * vertex, float * value) {
  if (heap_Size() == 0) return 0;

  *vertex = heap[1].vertex;
  *value = heap[1].value;
  struct heapElt elt = heap.back();
  heap.pop_back();
  heap[1] = elt;

  int p = 1;
  while (1) {
    int c = 2*p;
    if (c+1 <= heap_Size() && heap[c+1].value < heap[c].value) {
      c = c+1;
    }
    if (c <= heap_Size() && heap[p].value > heap[c].value) {
      struct heapElt elt = heap[p];
      heap[p] = heap[c];
      heap[c] = elt;
      p = c;
      continue;
    }
    break;
  }
  return 1;
}

void heap_Print() {
  fprintf(stderr,"heap\n");
  for (int i = 1; i <= heap_Size(); i++) {
    fprintf(stderr,"%d -- %d %f\n",i,heap[i].vertex,heap[i].value);
  }
}

// int main(int argc, char *argv[]) {
//   heap_Init();
//   heap_Print();
//   heap_Insert(3,4);
//   heap_Insert(2,3);
//   heap_Insert(0,1);
//   heap_Insert(1,2);
//   heap_Insert(6,7);
//   heap_Insert(5,6);
//   heap_Insert(4,5);
//   heap_Insert(9,10);
//   heap_Insert(7,8);
//   heap_Insert(15,16);
//   heap_Insert(14,15);
//   heap_Insert(13,14);
//   heap_Insert(12,13);
//   heap_Insert(11,12);
//   heap_Insert(10,11);
//   heap_Insert(8,9);
//
//   heap_Print();
//
//   int vertex;
//   float value;
//   while (heap_Remove(&vertex,&value)) {
//     fprintf(stderr,"remove %d %f\n",vertex,value);
//   }
//
//   heap_Insert(20,21);
//   heap_Insert(25,26);
//   heap_Insert(24,25);
//   heap_Insert(23,24);
//   heap_Insert(21,22);
//   heap_Insert(22,23);
//
//   heap_Print();
//
//   while (heap_Remove(&vertex,&value)) {
//     fprintf(stderr,"remove %d %f\n",vertex,value);
//   }
// }

#ifndef MINHEAP_H
#define MINHEAP_H

// Manages a single min heap

void heap_Init();
int heap_Size();
void heap_Insert(int vertex, float value);
int heap_Remove(int * vertex, float * value);

#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef Windows
#include "strsep.c"
#endif

#include "OBJModel.h"

// extract indices from xxx/xxx/xxx string, adjust so index 0 is start
void
ParseFace(char *faceStr,int *V, int *VT, int *N)
{ char * val;

  if ((val = strsep(&faceStr,"/"))) *V = (strcmp(val,"") != 0) ? atoi(val)-1 : (-1);
  if ((val = strsep(&faceStr,"/"))) *VT = (strcmp(val,"") != 0) ? atoi(val)-1 : (-1);
  if ((val = strsep(&faceStr,"/"))) *N = (strcmp(val,"") != 0) ? atoi(val)-1 : (-1);
}

struct faceModel readOBJArrays(const char * filename) {
  struct faceModel model = faceModel();
  std::vector<Vector3> vrts;
  std::vector<Vector3> nmls;
  std::vector<Vector2> uvs;

  // read again processing the data
  FILE * fd = fopen(filename,"r");
  if (fd == NULL) return model;

  while (1) {
    // read first two characters which indicate line contents
    char tag[3];
    int ret = fscanf(fd,"%2s",tag);
    if (ret == EOF) break;

    if (strcmp(tag,"v") == 0) {
      // read a vertex
      float x, y, z;
      fscanf(fd,"%f %f %f\n",&x,&y,&z);
      vrts.push_back(Vector3(x,y,z));
    }
    else if (strcmp(tag,"vt") == 0) {
      // read texture vertex
      float s, t;
      fscanf(fd,"%f %f\n",&s,&t);
      uvs.push_back(Vector2(s,t));
    }
    else if (strcmp(tag,"vn") == 0) {
      // read vertex normal
      float x, y, z;
      fscanf(fd,"%f %f %f\n",&x,&y,&z);
      nmls.push_back(Vector3(x,y,z));
    }
    else if (strcmp(tag,"f") == 0) {
      // read face - assume triangular
      for (int i = 0; i < 3; i++) {
        int v, vt, n;
        char str[32];
        fscanf(fd,"%31s",str);
        ParseFace(str,&v,&vt,&n);
        model.vrts.push_back(vrts[v]);
        model.nmls.push_back(nmls[n]);
        if (vt >= 0) model.uvs.push_back(uvs[vt]);
      }
      fscanf(fd,"%*[^\n]\n");
    }
    else {
      // read the remainder of the line
      fscanf(fd,"%*[^\n]\n");
    }
  }

  fclose(fd);

  return model;
}

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include "ShaderUtil.h"

char * ReadShaderSrc(const char * filename) {
  FILE * fd = fopen(filename,"r");
  if (!fd) return NULL;

  // struct stat stats;
  // stat(filename,&stats);
  // int len = stats.st_size;
  // fprintf(stderr,"len %d\n",len);

  fseek(fd,0,SEEK_END);
  int len = ftell(fd);
  fprintf(stderr,"read shader %s %d\n",filename,len);
  char * buf = (char *) malloc(len+1);
  fseek(fd,0,SEEK_SET);
  fread(buf,len,1,fd);
  fclose(fd);
  buf[len] = 0;
  fprintf(stderr,"shdr\n %s\n",buf);

  return buf;
}

void CheckCompileErr(GLuint shader) {
  GLint compiled = 0;
  glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);

  if (compiled == GL_FALSE) {
    GLint len = 0;
    glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &len);
    char * log = (char *) malloc(len);
    glGetShaderInfoLog(shader,len,&len,log);
    fprintf(stderr,"Compilation Log\n%s\n",log);
    free(log);
    exit(1);
  }
}

void CheckLinkErr(GLuint program) {
  GLint linked = 0;
  glGetProgramiv(program, GL_LINK_STATUS, (int *)&linked);

  if (linked == GL_FALSE) {
    GLint len = 0;
    glGetProgramiv(program, GL_INFO_LOG_LENGTH, &len);
    char *log = (char *) malloc(len);
    glGetProgramInfoLog(program,len,&len,log);
    fprintf(stderr,"Link Log\n%s\n",log);
    free(log);
    exit(1);
  }
}

#define SHADER_DIR "Shaders/"
#define SHADER_MAX 5

struct shaderInfo {
  char * src;
  GLenum kind;
  GLuint id;
};

GLuint shader_Compile(const char * basename) {
  fprintf(stderr,"Compiling Shaders: %s\n",basename);
  struct shaderInfo shaders[SHADER_MAX];

  char * filename = (char *) malloc(strlen(SHADER_DIR) + strlen(basename) + 6 + 1);
  strcpy(filename,SHADER_DIR);
  strcat(filename,basename);
  strcat(filename,".shdr");

  char * src = ReadShaderSrc(filename);
  if (!src) {
    fprintf(stderr,"failed to read \"%s\"\n",filename);
    exit(1);
  }

  char * p = src;
  int cnt = 0;
  while ((p = strstr(p,"///")) && (cnt < SHADER_MAX)) {
    *p = '\0';
    p++;
    shaders[cnt].src = p;
    shaders[cnt].kind = 0;
    if (strncmp(shaders[cnt].src,"//VERT",6) == 0) shaders[cnt].kind = GL_VERTEX_SHADER;
    if (strncmp(shaders[cnt].src,"//FRAG",6) == 0) shaders[cnt].kind = GL_FRAGMENT_SHADER;
    if (strncmp(shaders[cnt].src,"//GEOM",6) == 0) shaders[cnt].kind = GL_GEOMETRY_SHADER;
    if (strncmp(shaders[cnt].src,"//END",5) == 0) { break; }
    if (shaders[cnt].kind == 0) {
      fprintf(stderr,"not a recognized shader type\n");
      exit(1);
    }
    p += 2;
    cnt++;
  }

  GLuint program = glCreateProgram();
  for (int i = 0; i < cnt; i++) {
    shaders[i].id = glCreateShader(shaders[i].kind);
    int err;
    glShaderSource(shaders[i].id,1,(const GLchar **)&(shaders[i].src),NULL);
    glCompileShader(shaders[i].id);
    CheckCompileErr(shaders[i].id);
    glAttachShader(program,shaders[i].id);
    if (shaders[i].kind == GL_VERTEX_SHADER) fprintf(stderr,"vert ");
    if (shaders[i].kind == GL_FRAGMENT_SHADER) fprintf(stderr,"frag ");
    if (shaders[i].kind == GL_GEOMETRY_SHADER) fprintf(stderr,"geom ");
  }
  fprintf(stderr,"\n");

  free(filename); free(src);

  return program;
}

void shader_Link(GLuint program) {
  glLinkProgram(program);
  CheckLinkErr(program);

  GLuint shaders[5];
  int cnt;
  glGetAttachedShaders(program,5,&cnt,shaders);

  for (int i = 0; i < cnt; i++) {
    glDetachShader(program, shaders[i]);
  }
}

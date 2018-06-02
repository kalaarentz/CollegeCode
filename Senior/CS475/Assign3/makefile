OS := $(shell uname -s)

ifeq ($(OS), Darwin)
	CPPFLAGS = -g -D $(OS) -I /usr/local/include -std=c++11
	LDFLAGS = -framework openGL -L /usr/local/lib
	LDLIBS = -lSDL2 -lm
endif

ifeq ($(OS), Linux)
	CPPFLAGS = -g -D $(OS) -std=c++11
	LDFLAGS =
	LDLIBS = -lGL -lSDL2 -lm
endif

# ===============

main.o:					main.cpp appCtl.h
appCtl.o:				appCtl.cpp appCtl.h
draw.o:					draw.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h OBJModel.h modelVAO.h
model.o:				model.cpp model.h
OBJModel.o:			OBJModel.cpp OBJModel.h model.h
stdModels.o:		stdModels.cpp stdModels.h model.h
trackball.o:		trackball.cpp trackball.h
shaderUtil.o:		shaderUtil.cpp shaderUtil.h
matrixUtil.o:		matrixUtil.cpp matrixUtil.h
modelVAO.o:			modelVAO.cpp modelVAO.h model.h stdModels.h


assign-3:				main.o appCtl.o trackball.o draw.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

all:						assign-3

# ================
EXECS := assign-3

.PHONEY: clean
clean:
		rm -f $(EXECS) *.o
		rm -rf *.dSYM

test:
		echo $(shell uname -s)

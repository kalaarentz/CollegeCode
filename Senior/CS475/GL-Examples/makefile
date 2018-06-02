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
appCtl-1.o:			appCtl-1.cpp appCtl.h trackball.h
appCtl-2.o:			appCtl-2.cpp appCtl.h trackball.h
appCtl-3.o:			appCtl-3.cpp appCtl.h trackball.h model.h stdModels.h
appCtl-4.o:			appCtl-4.cpp appCtl.h trackball.h model.h stdModels.h
appCtl-5.o:			appCtl-5.cpp appCtl.h trackball.h model.h stdModels.h
appCtl-6.o:			appCtl-6.cpp appCtl.h trackball.h model.h stdModels.h
appCtl-7.o:			appCtl-7.cpp appCtl.h trackball.h model.h stdModels.h
appCtl-8.o:			appCtl-8.cpp appCtl.h
appCtl-9.o:			appCtl-9.cpp appCtl.h
appCtl-10.o:		appCtl-10.cpp appCtl.h
appCtl-11.o:		appCtl-11.cpp appCtl.h
appCtl-12.o:		appCtl-12.cpp appCtl.h
appCtl-13.o:		appCtl-13.cpp appCtl.h
appCtl-14.o:		appCtl-14.cpp appCtl.h
appCtl-15.o:		appCtl-15.cpp appCtl.h
appCtl-16.o:		appCtl-16.cpp appCtl.h
appCtl-17.o:		appCtl-17.cpp appCtl.h
appCtl-18.o:		appCtl-18.cpp appCtl.h
appCtl-19.o:		appCtl-19.cpp appCtl.h
appCtl-20.o:		appCtl-20.cpp appCtl.h
appCtl-21.o:		appCtl-21.cpp appCtl.h
draw-1.o:				draw-1.cpp draw.h model.h
draw-2.o:				draw-2.cpp draw.h model.h
draw-3.o:				draw-3.cpp draw.h model.h stdModels.h
draw-4.o:				draw-4.cpp draw.h model.h stdModels.h
draw-5.o:				draw-5.cpp draw.h model.h stdModels.h OBJModel.h
draw-6.o:				draw-6.cpp draw.h model.h stdModels.h OBJModel.h
draw-7.o:				draw-7.cpp draw.h model.h stdModels.h OBJModel.h
draw-8.o:				draw-8.cpp draw.h trackball.h shaderUtil.h matrixUtil.h
draw-9.o:				draw-9.cpp draw.h trackball.h shaderUtil.h matrixUtil.h
draw-10.o:			draw-10.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h OBJModel.h
draw-11.o:			draw-11.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h OBJModel.h
draw-12.o:			draw-12.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h OBJModel.h modelVAO.h
draw-13.o:			draw-13.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h OBJModel.h modelVAO.h scene.h
draw-14.o:			draw-14.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h scene.h
draw-15.o:			draw-15.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h scene.h
draw-16.o:			draw-16.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h scene.h
draw-17.o:			draw-17.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h scene.h bezier.h
draw-18.o:			draw-18.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h dijkstra.h minHeap.h meshTables.h
draw-19.o:			draw-19.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h dijkstra.h minHeap.h meshTables.h
draw-20.o:			draw-20.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h dijkstra.h minHeap.h meshTables.h
draw-21.o:			draw-21.cpp draw.h trackball.h shaderUtil.h matrixUtil.h model.h stdModels.h modelVAO.h dijkstra.h minHeap.h meshTables.h
OBJModel.o:			OBJModel.cpp OBJModel.h model.h
stdModels.o:		stdModels.cpp stdModels.h model.h
trackball.o:		trackball.cpp trackball.h
shaderUtil.o:		shaderUtil.cpp shaderUtil.h
matrixUtil.o:		matrixUtil.cpp matrixUtil.h
modelVAO.o:			modelVAO.cpp modelVAO.h model.h stdModels.h
paths.o:					paths.cpp paths.h

#== fixed function opengl -- compatability profile
ex-1:						main.o appCtl-1.o trackball.o draw-1.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-2:						main.o appCtl-2.o trackball.o draw-2.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-3:						main.o appCtl-3.o trackball.o draw-3.o model.o stdModels.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-4:						main.o appCtl-4.o trackball.o draw-4.o model.o stdModels.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-5:						main.o appCtl-5.o trackball.o draw-5.o model.o stdModels.o OBJModel.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-6:						main.o appCtl-6.o trackball.o draw-6.o model.o stdModels.o OBJModel.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-7:						main.o appCtl-7.o trackball.o draw-7.o model.o stdModels.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

#== shader based opengl -- core profile
ex-8:						main.o appCtl-8.o trackball.o draw-8.o shaderUtil.o matrixUtil.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-9:						main.o appCtl-9.o trackball.o draw-9.o shaderUtil.o matrixUtil.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-10:					main.o appCtl-10.o trackball.o draw-10.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-11:					main.o appCtl-11.o trackball.o draw-11.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-12:					main.o appCtl-12.o trackball.o draw-12.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-13:					main.o appCtl-13.o trackball.o draw-13.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-14:					main.o appCtl-14.o trackball.o draw-14.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-15:					main.o appCtl-15.o trackball.o draw-15.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-16:					main.o appCtl-16.o trackball.o draw-16.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o Matrices.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-17:					main.o appCtl-17.o trackball.o draw-17.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o Matrices.o bezier.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-18:					main.o appCtl-18.o trackball.o draw-18.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o Matrices.o dijkstra.o minHeap.o meshTables.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-19:					main.o appCtl-19.o trackball.o draw-19.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o Matrices.o meshTables.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-20:					main.o appCtl-20.o trackball.o draw-20.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

ex-21:					main.o appCtl-21.o trackball.o draw-21.o shaderUtil.o matrixUtil.o model.o stdModels.o OBJModel.o modelVAO.o scene.o
		$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

all:						ex-1 ex-2 ex-3 ex-4 ex-5 ex-6 ex-7 ex-8 ex-9 ex-10 ex-11 ex-12 ex-13 ex-14 ex-15 ex-16 ex-17 ex-18 ex-19 ex-20 ex-21

# ================
EXECS := ex-1 ex-2 ex-3 ex-4 ex-5 ex-6 ex-7 ex-8 ex-9 ex-10 ex-11 ex-12 ex-13 ex-14 ex-15 ex-16 ex-17 ex-18 ex-19 ex-20 ex-21

.PHONEY: clean
clean:
		rm -f $(EXECS) *.o
		rm -rf *.dSYM

test:
		echo $(shell uname -s)

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
appCtl.o:				appCtl.cpp appCtl.h trackball.h model.h stdModels.h
trackball.o:		trackball.cpp trackball.h
draw.o:					draw.cpp draw.h model.h
model.o:				model.cpp model.h
stdModels.o:		stdModels.cpp stdModels.h model.h

assign-1:				main.o appCtl.o trackball.o draw.o model.o stdModels.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)


# ================
EXECS := assign-1

.PHONEY: clean
clean:
		rm -f $(EXECS) *.o
		rm -rf *.dSYM

test:
		echo $(shell uname -s)

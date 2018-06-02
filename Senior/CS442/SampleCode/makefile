CFLAGS= -std=gnu11 -g

SampleAux.o:	SampleAux.c SampleAux.h
Sample.o:     Sample.c SampleAux.h

Sample:		Sample.o SampleAux.o

test: Sample HighLight
	./Sample
	./HighLight all < HighLight_data

clean:
	rm -rf *.o *.dSYM Sample HighLight

CC=gcc
CFLAGS=-Wall -O0 -g

PROGS=test_how_many_bits test_string_to_binary test_solve_exp
#PROGS=test_how_many_bits test_string_to_binary

all: $(PROGS)


test_how_many_bits: test_how_many_bits.c solver.o
	$(CC) test_how_many_bits.c solver.o -o test_how_many_bits $(CFLAGS)

test_string_to_binary: test_string_to_binary.c solver.o
	$(CC) test_string_to_binary.c solver.o -o test_string_to_binary $(CFLAGS)

test_solve_exp: test_solve_exp.c solver.o
	$(CC) test_solve_exp.c solver.o -o test_solve_exp $(CFLAGS)

solver.o: solver.c solver.h
	$(CC) solver.c -c -o solver.o $(CFLAGS)

clean:
	$(RM) $(PROGS)
	$(RM) *.o 
	$(RM) -rf *.dSYM

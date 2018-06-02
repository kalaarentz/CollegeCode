#MIPS  Program 2 MARS lab 10/20/2016
# Kala Arentz

# begin data segment
	.data
sum:	.word 0					# reserve space for a number and innitialize it to 5
i:	.word 0					# counter variable
space:	.asciiz " " 				# space to insert between numbers
	.align 2				# align data segment after each string
	
# end of data segment

#=========================================================================================================

# begin next segment

	.text					# instructions start here 
MAIN: 	la $s0, sum				# load the address of sum
	lw $s0,  0($s0)				# load the value of sum into a register
	la $s1, i				# load the address of i
	lw $s1, 0($s1)				# load the value of i into a register
	la $s2, space				# load the address of space
	lw $s2, 0($s2)				# load the value of space into a register
WHILE: 	slti $t0, $s1, 10			# while (i < 10)
	beq $t0, $zero, EXIT			# if i is greater then 10, then exit the program
	add $s0, $s0, $s1			# $s0 = $s0 + $s1 (sum = sum + i)
	add $a0, $zero, $s0			# put sum in $a0 to print
	addi $v0, $zero, 1			# set up sum to print
	syscall					# print
	add $a0, $zero, $s2			# put space in $a0 to print
	addi $v0, $zero, 11			# sent up to print char
	syscall					# print 
	addi $s1, $s1, 1			# i++
	j WHILE					# loop
EXIT: 	addi $v0, $zero, 10			#system call for exit
	syscall					# clean termination of program
	

#MIPS Hello World
# Kala Arentz

# begin data segment
	.data
hi: 	.asciiz "Hello World!\n"		# create a null-terminated string ''Hello World''
	.align 2				# align data segment after each string
num:	.word 5					# reserve space for a number and innitialize it to 5
i:	.word 1					# counter variable
space:	.asciiz " " 				# space to insert between numbers
	.align 2				# align data segment after each string
	
# end of data segment

#=========================================================================================================

# begin next segment

	.text					# instructions start here 
MAIN:	la   $s0, hi				# load the address of string hi
	add  $a0, $zero, $s0			# put address of hi in $a0 to print
	addi $v0, $zero, 4			# put 4 in $vo for printing a string
	syscall					# print
	la   $s1, num				# load address of num for use
	lw   $s1, 0($s1)			# load value of num into register
	la   $s2, space				# load address of space for use
	lw   $s2, 0($s2)			# load value of space into a register
	la   $s3, i				# load address of i into register
	lw   $s3, 0($s3)			# load value of i into a register
	addi $s3, $zero, 0			# set i to 0
LOOP: 	slt  $t0, $s3, $s1			# i < num
	beq  $t0, $zero, END			# go to end when done--> This is if i > num
	add  $a0, $zero, $s3			# put i in $a0 to print
	addi $v0, $zero, 1			# set up for int print
	syscall					# print int (i)
	add  $a0, $zero, $s2			# put space in $a0
	addi $v0, $zero, 11			# set up for print char
	syscall					# print char
	addi $s3, $s3, 1			# i++
	j    LOOP				# loop
END: 	addi $v0, $zero, 10			#system call for exit
	syscall					# clean termination of program
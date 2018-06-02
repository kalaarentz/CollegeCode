#MIPS MIPS Program 3 (MARS Lab 10/20/2016)
# program 3 is a modified version of program 1 (mips1.asm)
# will be asking the user to input a variable and then
# the code will run to decerasing the values by 7 for 12 iterations
# Kala Arentz

# begin data segment
	.data
ask: 	.asciiz "Enter a value: "		# create a null terminated string "Enter a Value: "
	.align 2
count:	.word 12				# reserve space for a number and innitialize it to 7
i:	.word 1					# counter variable
num:	.space 4				# number that will be subtracted by for 7 times
space:	.asciiz " " 				# space to insert between numbers
	.align 2				# align data segment after each string
	
# end of data segment

#=========================================================================================================

# begin next segment

	.text					# instructions start here 
MAIN:	la   $s0, ask				# load address of ask for use 
	add  $a0, $zero, $s0			# put address of ask in $a0 to print
	addi $v0, $zero, 4			# put 4 in $v0 for printing a string
	syscall 				# print
	add  $v0, $zero, 5			# read integer
	syscall
	la   $s1, num				# load address of num for use
	lw   $s1, 0($s1)			# load value of num into register
	add  $s1, $zero, $v0
	la   $s2, space				# load address of space for use
	lw   $s2, 0($s2)			# load value of space into a register
	la   $s3, i				# load address of i into register
	lw   $s3, 0($s3)			# load value of i into a register
	la   $s4, count				# load the address of count into register
	lw   $s4, 0($s4)			# load the value of count into a register
	addi $s3, $zero, 0			# set i to 0
LOOP: 	slt  $t0, $s3, $s4			# i < count
	beq  $t0, $zero, END			# go to end when done--> This is if i > num
	add  $a0, $zero, $s1			# put num in $a0 to print
	addi $v0, $zero, 1			# set up for int print
	syscall					# print int (i)
	add  $a0, $zero, $s2			# put space in $a0
	addi $v0, $zero, 11			# set up for print char
	syscall					# print char
	addi $s3, $s3, 1			# i++
	addi $s1, $s1, -7			# subtract 7 from num ($s1 = $s1-7)
	j    LOOP				# loop
END: 	addi $v0, $zero, 10			#system call for exit
	syscall					# clean termination of program

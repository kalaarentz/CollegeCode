#MIPS Hello World
# Kala Arentz

# begin data segment
	.data
count:	.word 12				# reserve space for a number and innitialize it to 7
i:	.word 1					# counter variable
num:	.word 700				# number that will be subtracted by for 7 times
space:	.asciiz " " 				# space to insert between numbers
	.align 2				# align data segment after each string
	
# end of data segment

#=========================================================================================================

# begin next segment

	.text					# instructions start here 
MAIN:	la   $s1, num				# load address of num for use
	lw   $s1, 0($s1)			# load value of num into register
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

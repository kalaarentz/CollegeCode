## MARS Assignment 2 ---> Assignment 4 in total CS270
# Kala Arentz
# Due Nov. 10 2016 ---> written November 3
# updated and fixed bug November 4 

# This MIPS program uses an implementation of bubble sort to sort an array of numers.
# The values of the array will be given by the user

########### DATA SEGMENT #################

	.data
sort: 	.asciiz "The elements sorted in ascending order are: " 	# String sort
	.align 2							# align the word
comma: 	.asciiz ", "							# comma string with a space
	.align 2							# align the word
arr: 	.space 80							# space for a 20 length array (4bytes *20)
len:	.space 4							# space for an integer (4bytes)
i:	.word 0								# make i = 0;

#end of data segment 

########### TEXT SEGMENT ##################
	
	.text 
MAIN:	la $s0, len				# load the address of the len variable
	lw $s0, 0($s0)				# load the value of len variable 
	add $v0, $zero, 5			# read the variable into the $v0 register
	syscall					# read integer (call to OS)
	add $s0, $zero, $v0			# make $s0 into the value that is read in
	la $s1, arr				# load the address for the array
	la $s2, i				# load the address of the i variable
	lw $s2, 0($s2)				# store the value in $s2
	# will be the for loop that will get all the user numbers for the array
F1:	slt $t0, $s2, $s0			# i < len
	beq $t0, $zero, END1			# if it is false then go to the end of the for loop
	add $v0, $zero, 5			# read one of the values for the array
	syscall					# read integer (call to the OS)
	sll $t0, $s2, 2				# i * 4
	add $t0, $t0, $s1			# address of arr[ i ]
	sw  $v0, 0($t0)				# store the value that is in the $v0
	addi $s2, $s2, 1			# i++
	j F1					# loop!
	# will call the bubble method
END1: 	add $a0, $zero, $s1			# set up variables $a0 = $s1 (array)
	add $a1, $zero, $s0			# set up vairable $a1 = $s0 (len)
	jal BUB
	# print the sorted string, load the address for the comma, and then print the array
	la $s3, sort				# load the sort address
	add $a0, $zero, $s3			# load the sort to be printed into the $a0 
	addi $v0, $zero, 4			# load the $v0 so syscall can be used
	syscall					# print the integer (call to the OS)
	la $s3, comma				# load the comma address
	addi $s2, $zero, 0			# i = 0
	addi $t7, $s0, -1			# $t7 = len - 1 (will be used in for loop for the hanging comma)
	# for loop that will print the array
F2:	slt $t0, $s2, $s0			# i < len
	beq $t0, $zero, END2			# if is is false then go to the enf of the for loop END2
	sll $t0, $s2, 2				# i*4
	add $t0, $t0, $s1			# address of arr[ i ]
	lw  $t1, 0($t0)				# load the value of arr[ i ] in $t1
	add $a0, $zero, $t1			# load the $t1 into the $a0 to be printed
	addi $v0, $zero, 1			# print out the integer
	syscall					# printing the integer (call to the OS)
	# print out the comma
	beq $s2, $t7, IFEND			# if (i != len -1) then print out the print comma
	add $a0, $zero, $s3			# getting the comma ready to be printed
	add $v0, $zero, 4			# load the comma into the $v0 to be printed
	syscall					# print the integer (call to the OS)
IFEND:  addi $s2, $s2, 1			# i++;
	j F2					# loop!
END2: 	addi $v0, $zero, 10			#system call for exit
	syscall	
	
############## END OF THE MAIN TEXT ##################

BUB: 	addi $sp, $sp, -12			# make room on the stack for three variables
	sw   $s0, 0($sp)			# this is the position for len
	sw   $s1, 4($sp)			# this is the position for array
	sw   $s2, 8($sp)			# this is the poistion for the i
	addi $s0, $zero, 0			# $s0 represents c in bubble sort code c=0
	addi $t0, $a1, -1			# $t0 = n - 1
	# the outer for loop
FO3:  	slt  $t1, $s0, $t0			# c < (n-1)
	beq  $t1, $zero, RET			# if it exits out of outer loop then return to the main method
	addi $s1, $zero, 0			# d = 0;
	sub  $t1, $a1, $s0			# n - c
	addi $t1, $t1, -1			# n - c - 1
	# the inner for loop
FI4:	slt  $t2, $s1, $t1			# d < (n - c - 1)
	beq  $t2, $zero, OE			# if this is false then exit inner loop
	sll  $t2, $s1, 2			# d * 4
	add  $t2, $t2, $a0			# address of arr[ d ]
	lw   $t3, 0($t2)			# load value arr[ d ] into $t3
	addi $t4, $s1, 1			# d + 1
	sll  $t5, $t4, 2			# (d + 1) * 4
	add  $t5, $t5, $a0			# address of arr[ d + 1 ]
	lw   $t6, 0($t5)			# load the value of arr[ d + 1 ] into $t6
	# the swapping if statement 
	slt  $t7, $t6, $t3			# if (arr[d+1] < arr[d]
	beq  $t7, $zero, IE			# if you don't enter the if statemtn then go end to inner for loop
	add  $s2, $zero, $t3			# t = arr[ d ]
	sw   $t6, 0($t2)			# store arr[ d ] = arr[ d + 1 ]
	sw   $t3, 0($t5)			# store arr[d + 1 ] = t
IE:	addi $s1, $s1, 1			# d + 1
	j FI4					# loop (inner loop)
OE: 	addi $s0, $s0, 1			# c++
	j FO3					# loop (outer loop)		
RET:	lw   $s0, 0($sp)			# restore len
	lw   $s1, 4($sp)			# restore array
	lw   $s2, 8($sp)			# restore i
	addi $sp, $sp, 12			# pop the stack
	jr   $ra				# go back 
	

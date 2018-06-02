#MIPS Assignment 3 (First MIPS assignment 10/25/2016)
# program that reads in (from standard input) an array of values, prints (to standard
# output) the array backwards, and prints the smallest element in the array
# -------------------
# java code for this assignment is saved as javaCodeAssign.3(MIPS) it is saved in text editor
# Kala Arentz

# begin data segment
	.data
#----- Strings ----------
back:	.asciiz "The array in reverse is: "		# create null terminated string "The array in reverse is:"
	.align 2					# align data segment after each string
small:	.asciiz "The smallest element is: "		# create null terminated string "The smallest element is:"
	.align 2					# align data segment after each string 
thank:	.asciiz "Thank you and have a nice day!"	# create null terminated string "Thank you and have a nice day!"
	.align 2					# align data segment after each string 
space:	.asciiz " "					# create null terminated string for a space " "
	.align 2					# align data segment after each string 
enter:	.asciiz "\n"					# create null terminated string for enter (new line) "\n"
	.align 2					# align data segment after each string 
#----- Numbers -----------
len:	.space 4					# make space for 4 bytes (int) for the length of the array
i:	.space 4					# make space for 4 bytes (int) for the i value
array:	.space 400					# make space for 100 length array (100*4)
min: 	.space 4					# make space for 4 bytes (int) for the min value
# end of data segment

#=================================================================================================================

# begin text segment
	.text						# instructions start here 
	la   $s0, len					# load the address for the len variable
	# read the integer in for length
	lw   $s0, 0($s0)				# load the value of len into register
	add  $v0, $zero, 5				# read integer into $v0 register
	syscall						# read it (call to the OS)
	add  $s0, $zero, $v0				# give the variable len the value that is read in len = $v0 ($s0)
	la   $s1, i					# load the address for the i variable
	addi $s1, $zero, 0				# i = 0 ($s1)
	la   $s2, array					# laod the address for the array variable
W1:	slt  $t0, $s1, $s0				# i < len ($s1<$s0)
	beq  $t0, $zero, E1				# if !(i<len) then end my while loop
	# read the integer for array[i]
	add  $v0, $zero, 5				# read in integer for one of the array 
	syscall						# read it (call to the OS)
	sll  $t0, $s1, 2				# i * 4
	add  $t0, $s2, $t0				# address of array[i]
	sw   $v0, 0($t0)				# store the int read in by OS ($v0) into the array
	addi $s1, $s1, 1				# i++
	j W1						# loop!
	# print the "array in reverse:"
E1:	la   $s3, back					# laod address for back to use
	add  $a0, $zero, $s3				# put address in $a0 to print
	addi $v0, $zero, 4				# put 4 in $v0 for printing string
	syscall						# print
	la   $s3, space					# load the address for space
	addi $s1, $s1, -1				# i=i-1;
	addi $t0, $zero, 0				# $t0 = 0
	sll  $t0, $t0, 2				# 0 * 4 to get the address of the array[0]
	add  $t0, $s2, $t0				# load the address for array[0]
	lw   $t1, 0($t0)				# load the int at the address of array[0]
	la   $s4, min					# load address for min
	add  $s4, $zero, $t1				# load the value $t0 (array[0]) into min
	addi $t0, $zero, -1				# $t0 = 0
W2:	slt  $t2, $t0, $s1				# 0 <= i
	beq  $t2, $zero, E2				# if !(0<i) go to E2
	sll  $t2, $s1, 2				# i * 4
	add  $t2, $t2, $s2				# address of array[i]
	lw   $t3, 0($t2)				# load the value at array[i] into $t3
	# print out the array[i]
	add  $a0, $zero, $t3				# load the $t3 value into the $a0 to be printed
	addi $v0, $zero, 1				# print integer
	syscall						# print it
	# print out a space
	add  $a0, $zero, $s3				# add string of space to print
	addi $v0, $zero, 4				# print char
	syscall						# call to OS- print it
	# enter the if statement to find the smallest array
	slt $t4, $t3, $s4				# array[i] < min
	beq $t4, $zero, IE				# if that is not true then go to IE
	add $s4, $zero, 0				# min = 0;
	add $s4, $s4, $t3				# min = array[i]
IE:	addi $s1, $s1, -1				# decrease i by one
	j W2						# loop!
E2:    	la   $s5, enter					# load address for enter
	add  $a0, $zero, $s5				# add enter to $a0 to be printed
	addi $v0, $zero, 4				# print the enter
	syscall						# call to the OS
	# load the smallest element of the array string and print it out
	la   $s6, small					# laod address for small string
	add  $a0, $zero, $s6				# put small string in $a0 register to print
	addi $v0, $zero, 4				# print string
	syscall						# call to OS
	# print out the min value of the array
	add  $a0, $zero, $s4				# add min value to $a0 register to print
	addi $v0, $zero, 1				# print min value
	syscall						# call to OS
	# print out an enter (so it is on its own line)	
	add  $a0, $zero, $s5				# add enter to $a0 to be printed
	addi $v0, $zero, 4				# print the enter
	syscall						# call to the OS
	#print out thank you string
	la   $s6, thank					# load address for thank string
	add  $a0, $zero, $s6				# load thank string into $a0 register
	addi $v0, $zero, 4				# print thank string
	syscall						# call to OS	
END: 	addi $v0, $zero, 10				#system call for exit
	syscall						# clean termination of program
	
	

## MARS Assignment 5 in CS270
# Kala Arentz
# Due Nov. 29 2016 ---> written November 16
#  

# This MIPS program uses an implementation of merge sort to sort an array of numers.
# The values of the array will be given by the user
# merge sort code is included in Assignment5.pdf

########### DATA SEGMENT #################

	.data
sort: 	.asciiz "The elements sorted in ascending order are: " 	# String sort
	.align 2							# align the word
comma: 	.asciiz ", "							# comma string with a space
	.align 2							# align the word
arr: 	.space 80							# space for a 20 length array (4bytes *20)
aux:	.space 80							# tmp array for space of 20 length to be used in mergeSort (MGS)
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
	# will call the mergeSort method
END1: 	add $a0, $zero, $s1			# set up variables $a0 = $s1 (array)
	add $a1, $zero, $s0			# set up vairable $a1 = $s0 (len)
	jal MERGESORT
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
	
############## END OF THE MAIN TEXT ##################################

############################### mergeSort ###########################		

# precondition $a0 -> array

# precondition $a1 is arrayLength

# precondtion size of the array has to be less than 20	

MERGESORT:	addi	$sp, $sp, -16			# allocate room on stack
		sw	$a0, 0($sp)			# save $a0 -> int Y[] on stack
		sw	$a1, 4($sp)			# save $a1 -> int SizeY on stack
		sw	$s0, 8($sp)			# save $s0 -> int Aux[20]
		sw	$ra, 12($sp)			# save return
		#add	$a0, $a0, $zero			# put $a0 in $a0 to call mergeSort1; already done
		addi	$a3, $a1, -1			# put $a1, int Size Y, in $a3 and subtract 1, SizeY-1 to call mergeSort
		add	$a2, $zero, $zero		# $a2 = 0 to call mergeSort
		la	$a1, aux			# load address of arrayAux in $a1 to call mergeSore
		jal	MGS_1
		lw	$a0, 0($sp)			# restore $a0 -> int Y[] from the stack
		lw	$a1, 4($sp)			# restore $a1 -> int SizeY on stack
		lw	$s0, 8($sp)			# restore $s0 -> int Aux[20]
		lw	$ra, 12($sp)			# restore return
		addi	$sp, $sp, 16			# pop stack
		jr	$ra				# return

########################### mergeSort1 ##########################################

# precondition $a0 -> int Y[]

# precondition $a1 -> int A[]

# precondition $a2 -> int s

# precondition $a3 -> int c

MGS_1:		addi	$sp, $sp, -24			# allocate room on stack
		sw	$a0, 0($sp)			# save $a0 -> int Y[] on stack
		sw	$a1, 4($sp)			# save $a1 -> int A[] on stack
		sw	$a2, 8($sp)			# save $a2 -> int s
		sw	$a3, 12($sp)			# save $a3 -> int e
		sw	$s4, 16($sp)			# save $s4 onto stack
		sw	$ra, 20($sp)			# save return
		slt	$t0, $a2, $a3			# s < e
		beq	$t0, $zero, RETURN
		add	$a3, $a3, $a2			# $a3 = s + e
		srl	$a3, $a3, 1			# $a3 = (s + e) / 2
		jal 	MGS_1
		lw	$a0, 0($sp)			# restore $a0 -> int Y[] from stack
		lw	$a1, 4($sp)			# restore $a1 -> int A[] from stack
		lw	$a2, 8($sp)			# restore $a2 -> int s
		lw	$a3, 12($sp)			# restore $a3 -> int e
		lw	$s4, 16($sp)			# restore $s4
		lw	$ra, 20($sp)			# restore return
		add	$a2, $a2, $a3			# $a2 = s + e
		srl	$a2, $a2, 1			# $a2 = (s + e) / 2
		addi	$a2, $a2, 1			# $a2 = ((s + e) / 2) + 1
		jal	MGS_1		
		lw	$a0, 0($sp)			# restore $a0 -> int Y[] from stack
		lw	$a1, 4($sp)			# restore $a1 -> int A[] from stack
		lw	$a2, 8($sp)			# restore $a2 -> int s
		lw	$a3, 12($sp)			# restore $a3 -> int e
		lw	$s4, 16($sp)			# restore $s4
		lw	$ra, 20($sp)			# restore return
		add	$s4, $a3, $zero			# $s4 = e
		sw	$s4, -4($sp)			# store 5th argument in the stack
		add	$a3, $a3, $a2			# $a3 = s + e
		srl	$a3, $a3, 1			# $a3 = (s + e) / 2
		addi	$a3, $a3, 1			# $a3 = ((s + e) / 2) + 1
		jal	MERGE
RETURN:		lw	$a0, 0($sp)			# restore $a0 -> int Y[] from stack
		lw	$a1, 4($sp)			# restore $a1 -> int A[] from stack
		lw	$a2, 8($sp)			# restore $a2 -> int s
		lw	$a3, 12($sp)			# restore $a3 -> int e
		lw	$s4, 16($sp)			# restore $s4
		lw	$ra, 20($sp)			# restore return
		addi	$sp, $sp, 24			# pop stack
		jr	$ra		

############################## MERGE ##############################

# precondition $a0 -> int Y[]

# precondition $a1 -> int A[]

# precondition $a2 -> int s1

# precondition $a3 -> int s2

# precondtion $s4 -> int c2

MERGE:		addi	$sp, $sp, -20			# allocate room on stack
		sw	$s0, 0($sp)			# save $s0 on stack for int i
		sw	$s1, 4($sp)			# save $s1 on stack for int j
		sw	$s2, 8($sp)			# save $s2 on stack for int k
		sw	$s4, 12($sp)			# c2 (5th arg)
		lw	$s4, 16($sp)			# load $s4 from stack(e2)
		add	$s0, $zero, $a2			# i = s1
		add	$s1, $zero, $a3			# j = s2
		add	$s2, $zero, $a2			# k = s1
		# first while loop
MW_1:	slt	$t0, $s0, $a3			# i < s2
		beq	$t0, $zero, MW_2
		slt	$t0, $s4, $s1			# c2 < j
		bne	$t0, $zero, MW_2
		sll	$t0, $s0, 2			# i*4
		add	$t0, $t0, $a0			# load address of arr[i]
		lw	$t0, 0($t0)			# value of arr[i]
		sll	$t1, $s1, 2			# j*4
		add	$t1, $t1, $a0			# load address of arr[j]
		lw	$t1, 0($t1)			# value of arr[j]
		slt	$t3, $t0, $t1			# arr[i] < arr[j]
		beq	$t3, $zero, MELSE1
		sll	$t3, $s2, 2			# k*4
		add	$t3, $t3, $a1			# load address of aux[k]
		sw	$t0, 0($t3)			# aux[k] = arr[i]
		addi	$s0, $s0, 1			# i++
		addi	$s2, $s2, 1			# k++
		j	MW_1
MELSE1:		sll	$t3, $s2, 2			# k*4
		add	$t3, $t3, $a1			# load address of aux[k]
		sw	$t1, 0($t3)			# aux[k] = arr[j]
		addi	$s1, $s1, 1			# j++
		addi	$s2, $s2, 1			# k++
		j	MW_1
		# second while loop
MW_2:	slt	$t0, $s0, $a3			# i < s2
		beq	$t0, $zero, MW_3
		sll	$t0, $s0, 2			# i*4
		add	$t0, $t0, $a0			# load address of arr[i]
		lw	$t0, 0($t0)			# get value arr[i]
		sll	$t3, $s2, 2			# k*4
		add	$t3, $t3, $a1			# load address of aux[k]
		sw	$t0, 0($t3)			# aux[k] = arr[i]
		addi	$s0, $s0, 1			# i++
		addi	$s2, $s2, 1			# k++
		j	MW_2
		# third while loop
MW_3:	slt	$t0, $s4, $s1			# c2 < j
		bne	$t0, $zero, MW_4
		sll	$t0, $s1, 2			# j*4
		add	$t0, $t0, $a0			# load address of arr[j]
		lw	$t0, 0($t0)			# get value arr[j]
		sll	$t3, $s2, 2			# k*4
		add	$t3, $t3, $a1			# load address of aux[k]
		sw	$t0, 0($t3)			# aux[k] = arr[j]
		addi	$s1, $s1, 1			# j++
		addi	$s2, $s2, 1			# k++
		j 	MW_3
		# fourth while loop
MW_4:	add	$s0, $a2, $zero			# i = s1
MW_4B:	slt	$t0, $s4, $s0			# c2 < i
		bne	$t0, $zero, MERGERETURN
		sll	$t0, $s0, 2			# i*4
		add	$t0, $t0, $a1			# load address of aux[i]
		lw	$t0, 0($t0)			# get value aux[i]
		sll	$t1, $s0, 2			# i*4
		add	$t1, $t1, $a0			# load address of arr[i]
		sw	$t0, 0($t1)			# arr[i] = aux[i]
		addi	$s0, $s0, 1			# i ++
		j	MW_4B
		
MERGERETURN:	lw	$s0, 0($sp)			# restore $s0 on stack for int i
		lw	$s1, 4($sp)			# restore $s1 on stack for int j
		lw	$s2, 8($sp)			# restore $s2 on stack for int k
		lw	$s4, 12($sp)			# restor $s4 on stack
		addi	$sp, $sp, 20
		jr	$ra

#########################################################
# CS441/541 Project 4 Test Suite

This directory should contain your additional tests.\

These are test cases I created to test certain items.

###test_format.txt
This file is specifically allowing me to test formatting strings without
having to look at the level7.txt files. It is easier to test a smaller file,
with larger numbers automatically.

After utilizing this file with setting up my strings, I have passed the
level 7 testing.

###priority_cases.txt
This file is specifically allowing me to test priority
cases in which the priority numbers are the same, and how you determine
which ones are chosen. If they priorities are equal, you choose whatever one
was in the arrival order first.

The console will print this out:
```
Scheduler : 3 Priority
Quantum : 0
Sch. File : tests/priority_cases.txt
-------------------------------
Arrival Order: 4, 3, 2, 1, 5, 6
Process Information:
 4    9   1
 3    7   1
 2   12   2
 1    4   1
 5    3   1
 6    5   1
-------------------------------
Running...
##################################################
#  #  CPU Pri   W   T
#  4    9   1   0   9
#  3    7   1   9  16
#  2   12   2  28  40
#  1    4   1  16  20
#  5    3   1  20  23
#  6    5   1  23  28
##################################################
# Avg. Waiting Time   : 16.00
# Avg. Turnaround Time: 22.67
##################################################
```

### sjf_cases.txt
This file is specifically testing the order in which
to take the short job first if the CPU time is the same. You would choose
whatever one was in the arrival order first.
```
Scheduler : 2 SJF
Quantum : 0
Sch. File : tests/sjf_cases.txt
-------------------------------
Arrival Order: 3, 4, 6, 1, 2, 5
Process Information:
 3    2   3
 4    7   5
 6   10   1
 1    2   4
 2    9   7
 5    5   3
-------------------------------
Running...
##################################################
#  #  CPU Pri   W   T
#  3    2   3   0   2
#  4    7   5   9  16
#  6   10   1  25  35
#  1    2   4   2   4
#  2    9   7  16  25
#  5    5   3   4   9
##################################################
# Avg. Waiting Time   : 9.33
# Avg. Turnaround Time: 15.17
##################################################
```
### rr_cases.txt
This file is designed to help me test the Round Robin
algorithm. The file was used during the development of the algorithm
I used in the scheduler.c program.I tested with multiple different
quantum numbers.

#### Quantum 2
```
Scheduler : 4 RR
Quantum : 2
Sch. File : tests/rr_cases.txt
-------------------------------
Arrival Order: 3, 4, 6, 1, 2, 5, 7, 8, 12, 11, 10, 9
Process Information:
 3    2   3
 4    7   5
 6   10   1
 1    2   4
 2    9   7
 5    5   3
 7    2   3
 8    7   5
12   10   1
11    2   4
10    9   7
 9    5   3
-------------------------------
Running...
##################################################
#  #  CPU Pri   W   T
#  3    2   3   0   2
#  4    7   5  48  55
#  6   10   1  56  66
#  1    2   4   6   8
#  2    9   7  58  67
#  5    5   3  42  47
#  7    2   3  12  14
#  8    7   5  53  60
# 12   10   1  59  69
# 11    2   4  18  20
# 10    9   7  61  70
#  9    5   3  49  54
##################################################
# Avg. Waiting Time   : 38.50
# Avg. Turnaround Time: 44.33
##################################################
```

#### Quantum 8
```
Scheduler : 4 RR
Quantum : 8
Sch. File : tests/rr_cases.txt
-------------------------------
Arrival Order: 3, 4, 6, 1, 2, 5, 7, 8, 12, 11, 10, 9
Process Information:
 3    2   3
 4    7   5
 6   10   1
 1    2   4
 2    9   7
 5    5   3
 7    2   3
 8    7   5
12   10   1
11    2   4
10    9   7
 9    5   3
-------------------------------
Running...
##################################################
#  #  CPU Pri   W   T
#  3    2   3   0   2
#  4    7   5   2   9
#  6   10   1  56  66
#  1    2   4  17  19
#  2    9   7  58  67
#  5    5   3  27  32
#  7    2   3  32  34
#  8    7   5  34  41
# 12   10   1  59  69
# 11    2   4  49  51
# 10    9   7  61  70
#  9    5   3  59  64
##################################################
# Avg. Waiting Time   : 37.83
# Avg. Turnaround Time: 43.67
##################################################
```

#### Quantum 15
```
Scheduler : 4 RR
Quantum : 15
Sch. File : tests/rr_cases.txt
-------------------------------
Arrival Order: 3, 4, 6, 1, 2, 5, 7, 8, 12, 11, 10, 9
Process Information:
 3    2   3
 4    7   5
 6   10   1
 1    2   4
 2    9   7
 5    5   3
 7    2   3
 8    7   5
12   10   1
11    2   4
10    9   7
 9    5   3
-------------------------------
Running...
##################################################
#  #  CPU Pri   W   T
#  3    2   3   0   2
#  4    7   5   2   9
#  6   10   1   9  19
#  1    2   4  19  21
#  2    9   7  21  30
#  5    5   3  30  35
#  7    2   3  35  37
#  8    7   5  37  44
# 12   10   1  44  54
# 11    2   4  54  56
# 10    9   7  56  65
#  9    5   3  65  70
##################################################
# Avg. Waiting Time   : 31.00
# Avg. Turnaround Time: 36.83
##################################################
```

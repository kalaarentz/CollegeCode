#!/usr/bin/perl

#
# A little script to run the project and check it against the
# expected result trace.
#

use strict;

my $create;
my $display_errors ;
my $binary = "./stdinput";

###########################################
# Parse Command Line Arguments
# [LEVEL] [EXPECTED FILE PREFIX]
###########################################
if( scalar(@ARGV) < 2 ) {
    print "ERROR: Not enough arguments\n";
    exit -1;
}

my $func_level  = int($ARGV[0]);
my $input_filename  = $ARGV[1] . "-input.txt";
my $output_filename = $ARGV[1] . "-output.txt";

my $cmd;

if( !(-e $input_filename)  ) {
    print "Error: Expected file does not exist ($input_filename)\n";
    exit -2;
}
if( !(-e $output_filename) && !defined($create) ) {
    print "Error: Expected file does not exist ($output_filename)\n";
    exit -2;
}


###########################################
# Diagnostics
###########################################
print "-"x70 . "\n";
print "Running the command:\n";
$cmd = $binary . " < " . $input_filename;
print "\tshell\$ $cmd\n";

if( defined($create) ) {
    system( "$cmd 2>&1 >$output_filename" );
    exit 0;
}

###########################################
# Run the command and gather all output
###########################################
my @input_file;
my @output_exp;
my @output_actual;

@input_file = `cat $input_filename`;
@output_exp = `cat $output_filename`;
@output_actual = `$cmd 2>&1`;

my $total_errors = 0;

###########################################
# Test level 1 functionality
###########################################
# Is there a prompt for each line of input
my $num_prompts = 0;
my $exp_prompts = 0;

# Is the input displayed
my $found = 0;

my $tmp_exp;

if( $func_level >= 1 ) {

    foreach my $line_exp (@output_exp) {
        if( $line_exp =~ /^\$\$ / ) {
            $exp_prompts++;
        }
    }

    foreach my $line_actual (@output_actual) {
        if( $line_actual =~ /^\$\$ / ) {
            $num_prompts++;
        }
    }
    if( $exp_prompts != $num_prompts ) {
        print "Error: Incorrect number of prompts displayed\n";
        print "       Expected : ".$exp_prompts."\n";
        print "       Your code: ".$num_prompts."\n";
        $total_errors++;
    }

    # Input values displayed with quotes
    $num_prompts = 0;
    foreach my $line_exp (@input_file) {
        # 'quit' should not be displayed
        if( $line_exp =~ /^quit/) {
            last;
        }

        $found = 0;
        if( length($line_exp) <= 1 ) {
            $tmp_exp = "\"\"";
        }
        else {
            $tmp_exp = substr($line_exp, 0, -1);
            $tmp_exp = "\"".$tmp_exp."\"";
        }
        $num_prompts += 1;
        foreach my $line_actual (@output_actual) {
            if( $line_actual =~ /\Q$tmp_exp\E$/ ) {
                $found = 1;
                if( $line_actual =~ /\Q$num_prompts\E\s*\)/ ) {
                    $found = 1;
                }
                elsif( $line_actual =~ /\d+\s*\)/ ) {
                    my $tmp = $&;
                    $found = 2;
                    print "Error: Count incorrect for input (".substr($line_exp,0,-1).") expected \"$num_prompts)\" actual \"$tmp\".\n";
                    $total_errors++;
                }
                else {
                    $found = 3;
                    print "Error: Count not present before output (make sure you are not missing the parenthesis)\n";
                    $total_errors++;
                }
                last;
            }
            elsif( length($line_exp) > 1 && $line_actual =~ /\Q$line_exp\E$/ ) {
                print "Error: Input string appears without quotes!\n";
                print "       Expected to find: ".$tmp_exp."\n";
                $total_errors++;
                $found = 4;
                last;
            }
            
        }
        if( $found == 0) {
            print "Error: Did not find the input string in output\n";
            print "       Expected to find: ".$tmp_exp."\n";
            $total_errors++;
        }
    }
}


###########################################
# Test level 2 functionality
###########################################
my $line_exp;
my $token_exp;
my $values_exp_str;
my @values_exp;
my $line_act;
my $token_act;
my $values_act_str;
my @values_act;

if( $func_level >= 2 && $total_errors <= 0 ) {
    for(my $idx_exp = 0; $idx_exp < scalar(@output_exp); ++$idx_exp) {
        $line_exp = substr($output_exp[$idx_exp], 0, -1);

        # Skipped dashed lines
        if($line_exp =~ /#(-)*$/ ) {
            next;
        }

        # Match:    7 /    0 /    0 /    6 /    1 /    0
        if( $line_exp =~ /^\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*$/ ) {
            $values_exp_str = $&;
            @values_exp = split(/\s\//, $values_exp_str);
            # 'trim'
            foreach my $v (@values_exp) {
                $v =~ s/^\s+|\s+$//g;
            }
        }
        # Match $$
        elsif( $line_exp =~ /^\$\$\s+\d+\)\s*/ ) {
            $token_exp = $';              #' ;
        }


        if( defined($token_exp) && defined($values_exp_str) ) {
            # Find this token in the actual output, then check the values
            for( my $idx_act = 0; $idx_act < scalar(@output_actual); ++$idx_act ) {
                $line_act = substr($output_actual[$idx_act], 0, -1);

                # If we found the token
                if( $line_act =~ /\Q$token_exp\E$/ ) {

                    $idx_act += 1;
                    $values_act_str = substr($output_actual[$idx_act], 0, -1);
                    if( $values_act_str =~ /^\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*\/\s*\d+\s*$/ ) {
                        $values_act_str = $&;
                        @values_act = split(/\s\//, $values_act_str);
                        # 'trim'
                        foreach my $v (@values_act) {
                            $v =~ s/^\s+|\s+$//g;
                        }

                        # Make sure they are equal
                        if( scalar(@values_exp) != scalar(@values_act) ) {
                            print "Error: Did not find the correct number of character counts.\n";
                            print "       For token: $token_exp\n";
                            printf("       Expected  (%3d): %s\n", scalar(@values_exp), $values_exp_str);
                            printf("       Your code (%3d): %s\n", scalar(@values_act), $values_act_str);
                            $total_errors++;
                        }
                        else {
                            # check all of the values
                            my @pos = ();
                            for( my $idx = 0; $idx < scalar(@values_exp); ++$idx ) {
                                if( int($values_exp[$idx]) != int($values_act[$idx]) ) {
                                    push(@pos, $idx);
                                }
                            }
                            if( scalar(@pos) > 0 ) {
                                print "Error: Did not find the correct values for character counts.\n";
                                print "       For token: $token_exp\n";
                                printf("       Expected  (%3d): %s\n", scalar(@values_exp), $values_exp_str);
                                printf("       Your code (%3d): %s\n", scalar(@values_act), $values_act_str);
                                print "       Check positions: ". join(", ", @pos) . "\n";
                                $total_errors++;
                            }
                        }
                    }
                    else {
                        print "Error: Did not find a correctly formatted string containing character counts.\n";
                        print "       Expected : $values_exp_str\n";
                        print "       For token: $token_exp\n";
                        $total_errors++;
                    }

                }
                else {
                    #print "-A: $line_act\n";
                }
            }
            undef($token_exp);
            undef($values_exp_str);
        }
    }
}

###########################################
# Test level 3 functionality
###########################################
my @totals_exp_str;
my @totals_exp_values;
my @totals_act_str;
my @totals_act_values;

if( $func_level >= 3 && $total_errors <= 0 ) {
    # Extract the expected totals
    for(my $idx_exp = 0; $idx_exp < scalar(@output_exp); ++$idx_exp) {
        $line_exp = substr($output_exp[$idx_exp], 0, -1);
        if( $line_exp =~ /Number of / ) {
            push(@totals_exp_str, $line_exp);
            if( $line_exp =~ /\d+/ ) {
                push(@totals_exp_values, int($&) );
            }
        }
    }

    # Extract the actual totals
    for(my $idx_act = 0; $idx_act < scalar(@output_actual); ++$idx_act) {
        $line_act = substr($output_actual[$idx_act], 0, -1);
        if( $line_act =~ /Number of / || $line_act =~ /number of / ) {
            push(@totals_act_str, $line_act);
            if( $line_act =~ /\d+/ ) {
                push(@totals_act_values, int($&) );
            }
        }
    }

    # Check number of lines
    if( scalar(@totals_exp_values) != scalar(@totals_act_values) ) {
        print "Error: Did not find the correct number of total lines at the end of the program.\n";
        printf("       Expected : %3d\n", scalar(@totals_exp_values));
        printf("       Your code: %3d\n", scalar(@totals_act_values));
        $total_errors++;
    }

    # check all of the values
    my @pos = ();
    for( my $idx = 0; $idx < scalar(@totals_exp_values); ++$idx ) {
        if( int($totals_exp_values[$idx]) != int($totals_act_values[$idx]) ) {
            push(@pos, $idx);
        }
    }
    if( scalar(@pos) > 0 ) {
        print "Error: Did not find the correct values for total character counts.\n";
        print "       Expected : ".join(" ", @totals_exp_values)."\n";
        print "       Your code: ".join(" ", @totals_act_values)."\n";
        print "       Check positions: ". join(", ", @pos) . "\n";
        $total_errors += scalar(@pos);
    }

}


###########################################
# Determine pass/fail
###########################################
display_result($total_errors);
if( $total_errors != 0 ) {
    print "*"x35 . "\n";
    print "-"x10 . " Actual Output " . "-"x10 . "\n";
    foreach my $line (@output_actual) {
        print "> " .$line;
    }
    print "-"x10 . " Expected Output " . "-"x8 . "\n";
    foreach my $line (@output_exp) {
        print "< " .$line;
    }
    print "*"x35 . "\n";
}

print "\n";
exit 0;


###########################################
# Compare the expected to the actual (for necessary output)
###########################################
my $tmp_exp;
my $tmp_actual;

foreach my $line_exp (@output_exp) {
    $found = 0;

    # Ignore any dashed lines
    if($line_exp =~ /^#(-)*$/ ) {
        next;
    }

    $tmp_exp = $line_exp;
    $tmp_exp =~ s/\h+/ /g; # Many spaces to 1 space

    foreach my $line_actual (@output_actual) {
        # Ignore any dashed lines
        if($line_actual =~ /^#(-)*$/ ) {
            next;
        }

        $tmp_actual = $line_actual;
        $tmp_actual =~ s/\h+/ /g; # Many spaces to 1 space

        if($tmp_exp =~ /^Error/ && $tmp_actual =~ /^Error/ ) {
            if( defined($display_errors) ) {
                print "Compare Error Messages:\n";
                print "EXP: $line_exp";
                print "ACT: $line_actual";
            }
            $found = 1;
            last;
        }
        elsif( $tmp_exp eq $tmp_actual ) {
            $found = 1;
            last;
        }
    }
    if($found == 0 ) {
        $total_errors += 1;
    }
}

###########################################
# Compare the actual to the expected (for extra output)
###########################################
$found = 0;
foreach my $line_actual (@output_actual) {
    $found = 0;

    # Ignore any dashed lines
    if($line_actual =~ /^#(-)*$/ ) {
        next;
    }

    $tmp_actual = $line_actual;
    $tmp_actual =~ s/\h+/ /g; # Many spaces to 1 space

    foreach my $line_exp (@output_exp) {
        # Ignore any dashed lines
        if($line_exp =~ /^#(-)*$/ ) {
            next;
        }

        $tmp_exp = $line_exp;
        $tmp_exp =~ s/\h+/ /g; # Many spaces to 1 space

        if($tmp_exp =~ /^Error/ && $tmp_actual =~ /^Error/ ) {
            $found = 1;
            last;
        }
        elsif( $tmp_exp eq $tmp_actual ) {
            $found = 1;
            last;
        }
    }

    if($found == 0 ) {
        print "** Extra line in actual output:\n";
        print "-"x35 . "\n";
        print "> " .$line_actual;
        print "-"x35 . "\n";
        $total_errors += 1;
    }
}

###########################################
# Determine pass/fail
###########################################
display_result($total_errors);
if( $total_errors != 0 ) {
    print "*"x35 . "\n";
    print "-"x10 . " Actual Output " . "-"x10 . "\n";
    foreach my $line (@output_actual) {
        print "> " .$line;
    }
    print "-"x10 . " Expected Output " . "-"x8 . "\n";
    foreach my $line (@output_exp) {
        print "< " .$line;
    }
    print "*"x35 . "\n";
}

print "\n";
exit 0;

#
# Display the result of the test
#
sub display_result() {
    my $num_errors = shift(@_);

    if( 0 == $num_errors ) {
        print "Passed!\n";
    }
    else {
        print "*"x5 . " Failed test on ".$num_errors." lines! " . "*"x5 . "\n";
    }

    return $num_errors;
}

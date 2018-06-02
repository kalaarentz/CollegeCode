#!/usr/bin/perl

#
# A little script to run the project and check it against the
# expected result trace.
#

use strict;
use Class::Struct;

my $create;
my $display_errors ;
my $binary = "./fsplit";

###########################################
# Parse Command Line Arguments
# [LEVEL] [EXPECTED FILE PREFIX]
###########################################
if( scalar(@ARGV) < 2 ) {
    print "ERROR: Not enough arguments\n";
    exit -1;
}

my $func_level  = int($ARGV[0]);
my $input_filename  = $ARGV[1] . ".txt";
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
$cmd = $binary . " " . $input_filename;
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
my $found = 0;


###########################################
# Test level 1 functionality
###########################################
if( $func_level >= 1 ) {
    # make sure every line of the input file is displayed
    my @actual_processing = ();
    foreach my $line_exp (@input_file) {
        $line_exp = substr($line_exp, 0, -1);
        foreach my $line_act (@output_actual) {
            if( $line_act =~ /\Q$line_exp\E/ ) {
                push(@actual_processing, $line_act);
            }
        }
    }

    foreach my $line_exp (@input_file) {
        $line_exp = substr($line_exp, 0, -1);
        $found = 0;
        foreach my $line_act (@actual_processing) {
            if( $line_act =~ /Processing:\s*\Q$line_exp\E/ ) {
                $found = 1;
            }
            elsif( $line_act =~ /\Q$line_exp\E/ ) {
                print "Error: Found the following line, but no with \"Processing: \" preceeding it\n";
                print "       ".$line_exp."\n";
                $total_errors++;
                $found = 0;
                last;
            }
        }
        if( 0 == $found ) {
            print "Error: Did not find the following line displayed with \"Processing: \" preceeding it\n";
            print "       ".$line_exp."\n";
            $total_errors++;
        }
    }
}

###########################################
# Test level 2 functionality
###########################################
struct Result => [
    username => '$',
    encoding => '$',
    salt     => '$',
    hash     => '$'];

my @expected_processing = ();
my @expected_result = ();

my @expected_tuple_str = ();
my @expected_tuple_valid = ();
my @expected_tuple = ();
my $num_expected_tuples = 0;
my $num_expected_errors = 0;
my @actual_tuple_str = ();
my @actual_tuple = ();

my @expected_summary = ();
my @actual_summary = ();

my @expected_errors = ();
my @actual_errors = ();

my $tuple;

if( $func_level >= 2 ) {
    # Extract the tuple from the expected file
    foreach my $tmp_exp (@output_exp) {
        my $line_exp = substr($tmp_exp, 0, -1);
        if( $line_exp =~ /Processing:\s*/ ) {
            push(@expected_processing, $');          # ' );
        }
        elsif( $line_exp =~ /Number/ ) {
            $line_exp =~ /\s\d+/;
            push(@expected_summary, int($&));
        }
        elsif( $line_exp =~ /#-+/ ) {
            ;#print "SKIP: $line_exp\n";
        }
        else {
            push(@expected_result, $line_exp);
            if( $line_exp =~ /Error/ ) {
                push( @expected_errors, $line_exp);
                push( @expected_tuple, Result->new() );
                ++$num_expected_errors;
            }
            else {
                $line_exp = substr($line_exp, 2);
                push(@expected_tuple_str, $line_exp);
                my @tmp = split(/\s\/\s/, $line_exp);
                $tuple = Result->new();
                $tuple->username($tmp[0]);
                $tuple->encoding($tmp[1]);
                $tuple->salt($tmp[2]);
                $tuple->hash($tmp[3]);
                push(@expected_tuple, $tuple);
                push(@expected_tuple_valid, $tuple);
                ++$num_expected_tuples;
            }
        }
    }

    # Extract summary
    for( my $a = 0; $a < scalar(@output_actual); ++$a ) {
        my $line_act = $output_actual[$a];
        if( $line_act =~ /Number/ ) {
            $line_act =~ /\s\d+/;
            push(@actual_summary, int($&));
        }
        elsif( $line_act =~ /Error/ ) {
            push(@actual_errors, $line_act);
        }
    }

    # Find that tuple in the actual output for username/hash
    for( my $i = 0; $i < scalar(@expected_tuple); ++$i) {
        $found = 0;
        # find the Processing line in the actual output
        for( my $a = 0; $a < scalar(@output_actual); ++$a ) {
            my $line_act = $output_actual[$a];
            if( $line_act =~ /Number/ && $i == 0) {
                next;
            }
            elsif( $line_act =~ /\Q$expected_processing[$i]\E/ ) {
                ++$a;
                if( $a >= scalar(@output_actual) ) {
                    print "Error: Did not find the summary line:\n";
                    print "       Processing: ".$expected_processing[$i]."\n";
                    print "       ".$expected_result[$i]."\n";
                    $total_errors++;
                }

                # Make sure it is followed by a proper tuple
                $line_act = $output_actual[$a];

                if( $line_act =~ /Error/ ) { # Skip errors this round
                    $found = 1;
                    next;
                }

                # Extract the tuple
                my $line_tmp = $line_act;
                if( $line_tmp =~ /#\s*/ ) {
                    $line_tmp = substr($line_tmp, 1);
                    $line_tmp =~ s/^\s+|\s+$//g;
                }
                push(@actual_tuple_str, $line_tmp);
                my @tmp = split(/\s+\/\s+/, $line_tmp);
                foreach my $t (@tmp) {
                    $t =~ s/^\s+|\s+$//g;
                }
                if( scalar(@tmp) < 2 ) {
                    print "DEBUG 1 JJH\n";
                    print "Error: Did not find the summary line containing the username and encoding: (A)\n";
                    print "       Processing: ".$expected_processing[$i]."\n";
                    print "       Expected: ".$expected_result[$i]."\n";
                    print "       Actual  : ".$line_act;
                    $total_errors++;
                    $found = 2;
                    next;
                }
                $tuple = Result->new();
                $tuple->username($tmp[0]);
                $tuple->encoding($tmp[1]);
                if( scalar(@tmp) >= 3 ) {
                    $tuple->salt($tmp[2]);
                }
                else {
                    $tuple->salt("INVALID");
                }
                if( scalar(@tmp) >= 4 ) {
                    $tuple->hash($tmp[3]);
                }
                else {
                    $tuple->hash("INVALID");
                }
                push(@actual_tuple, $tuple);


                # Check username
                my $tmp_tuple = $expected_tuple[$i]->username();
                if( $line_act =~ /\s+\Q$tmp_tuple\E\s+/ ) {
                    $found = 1;
                }
                else {
                    print "Error: Did not find the summary line containing the username:\n";
                    print "       Processing: ".$expected_processing[$i]."\n";
                    print "       Expected: ".$expected_tuple_str[$i]."\n";
                    print "       Actual  : ".$line_act;
                    $total_errors++;
                    next;
                }


                # Check encoding
                my $tmp_tuple = $expected_tuple[$i]->encoding();
                if( $line_act =~ /\s+\Q$tmp_tuple\E\s+/ ) {
                    $found = 1;
                }
                else {
                    print "Error: Did not find the summary line containing the encoding:\n";
                    print "       Processing: ".$expected_processing[$i]."\n";
                    print "       Expected: ".$expected_tuple_str[$i]."\n";
                    print "       Actual  : ".$line_act;
                    $total_errors++;
                    next;
                }
            }
        }
        if( 0 == $found ) {
            print "Error: Did not find the summary line containing the username and encoding: (B)\n";
            print "       Processing: ".$expected_processing[$i]."\n";
            print "       ".$expected_tuple_str[$i]."\n";
            $total_errors++;
        }
    }

    # Check the summary
    if( scalar(@expected_summary) != scalar(@actual_summary) ) {
        print "Error: Did not find the correct number of final totals! Expected ".scalar(@expected_summary)." lines, instead found ".scalar(@actual_summary)."\n";
        $total_errors++;
    }
    else {
        my @pos = ();
        for(my $i = 0; $i < scalar(@expected_summary); ++$i) {
            if( int($expected_summary[$i]) != int($actual_summary[$i]) ) {
                push(@pos, $i);
            }
        }
        if( scalar(@pos) > 0 ) {
            print "Error: Did not find the correct values for the final totals.\n";
            printf("       Expected  : %s\n", join(", ",@expected_summary));
            printf("       Your code : %s\n", join(", ",@actual_summary));
            print "       Check positions: ". join(", ", @pos) . "\n";
            $total_errors++;
        }
    }
}

###########################################
# Test level 3 functionality
###########################################
if( $func_level >= 3 ) {

    # First check the tuple
    if( scalar(@expected_tuple_valid) != scalar(@actual_tuple) ) {
        print "Error: Expected to find ".scalar(@expected_tuple_valid)." individual results, instead found ".scalar(@actual_tuple)."\n";
        $total_errors++;
    }
    else {
        # check the full tuple line
        for(my $idx = 0; $idx < scalar(@expected_tuple_valid); ++$idx) {
            my $exp_tuple = $expected_tuple_valid[$idx];
            my $act_tuple = $actual_tuple[$idx];

            my $tmp_item = $act_tuple->username();
            if( !($exp_tuple->username() =~ /^\s*\Q$tmp_item\E\s*$/) ) {
                print "Error: Failed to extract the proper usename\n";
                print "       Expected Line: ".$expected_tuple_str[$idx]."\n";
                print "       Actual Line  : ".$actual_tuple_str[$idx]."\n";
                print "       Expected Username: ".$exp_tuple->username()."\n";
                print "       Actual   Username: ".$tmp_item."\n";
                $total_errors++;
                next;
            }
            $tmp_item = $act_tuple->encoding();
            if( !($exp_tuple->encoding() =~ /^\s*\Q$tmp_item\E\s*$/) ) {
                print "Error: Failed to extract the proper usename\n";
                print "       Expected Line: ".$expected_tuple_str[$idx]."\n";
                print "       Actual Line  : ".$actual_tuple_str[$idx]."\n";
                print "       Expected Encoding: ".$exp_tuple->encoding()."\n";
                print "       Actual   Encoding: ".$tmp_item."\n";
                $total_errors++;
                next;
            }
            $tmp_item = $act_tuple->salt();
            if( !($exp_tuple->salt() =~ /^\s*\Q$tmp_item\E\s*$/) ) {
                print "Error: Failed to extract the proper usename\n";
                print "       Expected Line: ".$expected_tuple_str[$idx]."\n";
                print "       Actual Line  : ".$actual_tuple_str[$idx]."\n";
                print "       Expected Salt: ".$exp_tuple->salt()."\n";
                print "       Actual   Salt: ".$tmp_item."\n";
                $total_errors++;
                next;
            }
            $tmp_item = $act_tuple->hash();
            if( !($exp_tuple->hash() =~ /^\s*\Q$tmp_item\E\s*$/) ) {
                print "Error: Failed to extract the proper usename\n";
                print "       Expected Line: ".$expected_tuple_str[$idx]."\n";
                print "       Actual Line  : ".$actual_tuple_str[$idx]."\n";
                print "       Expected Hashed Password: ".$exp_tuple->hash()."\n";
                print "       Actual   Hashed Password: ".$tmp_item."\n";
                $total_errors++;
                next;
            }
        }
    }

    # Now check for Error messages
    if( scalar(@expected_errors) != scalar(@actual_errors) ) {
        print "Error: Expected to find ".scalar(@expected_errors)." error messages, instead found ".scalar(@actual_errors)."\n";
        print "       Check the final output to see where you are missing or have too many lines prefixed with \"Error\"\n";
        $total_errors++;
    }
}

###########################################
# Test level 4 functionality
###########################################
if( $func_level >= 4 ) {
    # Will be naturally checked as part of level 3
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
















# Is there a prompt for each line of input
my $num_prompts = 0;
my $exp_prompts = 0;

# Is the input displayed


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

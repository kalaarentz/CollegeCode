import org.apache.commons.lang.StringUtils;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // types of instructions and what they can do
    private int instruc = 0;
    private int r_type = 0;
    private int i_type = 0;
    private int j_type = 0;
    private int fwd_taken = 0;
    private int bkw_taken = 0;
    private int not_taken = 0;
    private int loads = 0;
    private int stores = 0;

    // list of the regs
    private int[][] registers;
    private List<String[]> listOfInstructions = new ArrayList<>();

    public static void main(String[] args) {
	// write your code here
       new Main();
    }

    public Main () {

        // column 0 is the read number, column 1 is the write number
        registers = new int[32][2];
        for( int i = 0; i < registers.length; i++ ) {
            for( int j = 0; j < registers[i].length; j++ ) {
                registers[i][j] = 0;
            }
        }

        readFile();
        statistics();
        writeFile();
    }

    public void readFile() {

        File file = new File( "trace.txt" );

        try {
            Scanner scan = new Scanner(file);

            while( scan.hasNext() ) {
                String[] split = scan.nextLine().split(" ");
                Long x = Long.parseLong( split[1], 16 );
                split[1] = StringUtils.leftPad( Long.toBinaryString(x), 32, "0");
                listOfInstructions.add(split);
                instruc++;
            }
            scan.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        try  {
            File file = new File( "statistics.txt");
            FileWriter fw = new FileWriter(  file );
            BufferedWriter bw = new BufferedWriter( fw );

            bw.write( "insts: " + instruc );
            bw.newLine();
            bw.write( "r-type: " + r_type );
            bw.newLine();
            bw.write( "i-type: " + i_type );
            bw.newLine();
            bw.write( "j-type: " + j_type );
            bw.newLine();
            bw.write( "fwd-taken: " + fwd_taken );
            bw.newLine();
            bw.write( "bwd-taken: " + bkw_taken );
            bw.newLine();
            bw.write( "not-taken: " + not_taken );
            bw.newLine();
            bw.write( "loads: " + loads );
            bw.newLine();
            bw.write( "stores: " + stores );
            bw.newLine();

            for( int i = 0; i < registers.length; i++ ) {
                bw.write("reg-" + i + ": " + registers[i][0] + " " + registers[i][1]);
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void statistics() {

        int count = 1;
        String[] next = listOfInstructions.get(count);
        for( String[] list : listOfInstructions ) {
            //System.out.println( Arrays.deepToString(list));

            String instruction = list[1];
            String opcode = instruction.substring(0,6);
            String address = list[0];

            if( count < instruc ) {
                next = listOfInstructions.get(count);
                //System.out.println(Arrays.toString(next));
            } else if( count == instruc ) {
                next = list;
            }

            if( opcode.equals("000000") ) {
                // R-Type instructions
                rTypeInstructions( address, instruction);
            } else if ( opcode.equals("000010") || opcode.equals("000011") ) {
                // J-Type Instructions
                jTypeInstructions( address, instruction, next[0] );
            } else {
                // I-Type Instructions
                iTypeInstructions( address, instruction, next[0] );
            }

            count++;
        }
    }

    private void rTypeInstructions( String address ,String instruction ) {
        r_type++;
        String rs = instruction.substring(6,11);
        String rt = instruction.substring(11,16);
        String rd = instruction.substring(16,21);
        // need to find out the registers
        String funct = instruction.substring(26, instruction.length());
        if( funct.equals("000000") || funct.equals("000010") ) {
            //srl or sll
            updateRegisters( rs, rt, rd, "sll");
        } else if ( funct.equals( "001000") ) {
            updateRegisters( rs, rt, rd, "jr" );
        } else if ( funct.equals("000011") ) {
            updateRegisters( rs, rt, rd, "sra" );
        } else if( funct.equals("010000") || funct.equals( "010010") ) {
            updateRegisters( rs, rt, rd, "mfhi" );
        } else {
            updateRegisters( rs, rt, rd, "r-type" );
        }

    }

    private void iTypeInstructions( String address, String instruction, String target  ) {
        i_type++;
        // need to find out the registers
        String rs = instruction.substring(6,11);
        String rt = instruction.substring(11,16);
        // need to find out if it is a lw or sw --> if it is, needs to increase by one
        String opcode = instruction.substring(0, 6);
        if( opcode.equals("100100") || opcode.equals("100101") || opcode.equals("100011") ) {
            // load instructions
            updateRegisters(rs, rt, null, "i-type");
            loads++;
        } else if ( opcode.equals("101000") || opcode.equals( "1010001" ) || opcode.equals("101011") ) {
            // store instructions
            updateRegisters(rs, rt, null, "i-type");
            stores++;
        } else if ( opcode.equals( "000100") || opcode.equals("000101") ) {
            // bne and beq need to know if fwd or backward jump
            updateRegisters( rs, rt, null, "bne");
            determineBranchJump( address, target);
        } else if( opcode.equals( "001111") ) {
            // lui not considered a load
            updateRegisters( rs, rt, null, "lui");
        } else {
            updateRegisters( rs, rt, null, "i-type");
        }
    }

    private void jTypeInstructions(  String address, String instruction, String target) {
        j_type++;
        // j and jal need to know if fwd or backward jump
        determineBranchJump( address, target);
    }

    /**
     *  Will take substrings of the original and update the specific register from the array
     * @param rs --> source register
     * @param rt --> 2nd source register
     * @param rd --> destination register, may be null if it is for i-type instruction
     * @param type --> type of instruction (r-type or i-type or sll/srl)
     */
    private void updateRegisters( String rs, String rt, String rd, String type ) {

        int regRS = Integer.parseInt(rs, 2);
        int regRT = Integer.parseInt( rt, 2);
        int regRD = (rd != null) ? Integer.parseInt(rd, 2) : -1;

        //TODO: deal with the special cases of branches for i-type that do not
        // have rt as a destination register

        switch( type ) {
            case "mfhi" :
                registers[regRD][1] = registers[regRD][1] + 1;
                break;
            case "sra" :
                registers[regRT][0] = registers[regRT][1] + 1;
                registers[regRD][1] = registers[regRD][1] + 1;
                break;
            case "jr" :
                registers[regRS][0] = registers[regRS][0] + 1;
                break;
            case "lui" :
                registers[regRT][1] = registers[regRT][1] + 1;
                break;
            case "bne" :
                registers[regRS][0] = registers[regRS][0] + 1;
                registers[regRT][0] = registers[regRT][0] + 1;
                break;
            case "sll" :
                registers[regRT][0] = registers[regRT][0] + 1;
                registers[regRD][1] = registers[regRD][1] + 1;
                break;
            case "r-type" :
                // will do the rs, rt, and rd
                registers[regRS][0] = registers[regRS][0] + 1;
                registers[regRT][0] = registers[regRT][0] + 1;
                registers[regRD][1] = registers[regRD][1] + 1;
                //System.out.println("R-type");
                break;
            case "i-type" :
                // will only do the rs, rt registers
                registers[regRS][0] = registers[regRS][0] + 1;
                registers[regRT][1] = registers[regRT][1] + 1;
                //System.out.println("I-type");
                break;
            default:
                throw new IllegalArgumentException( "Invalid instruction: " + type );
        }
    }

    private void determineBranchJump( String address, String next ) {

        Long current = Long.parseLong(address, 16);
        Long target = Long.parseLong( next, 16 );

        // current - address = negative  --> forward jump
        // current - address = positive --> backward jump
        //String hexDest = StringUtils.leftPad( hex, current.length(), "0");
        Long compResult = target - ( current + Long.valueOf(4));

        if( compResult < 0 ) {
            bkw_taken++;
        } else if ( compResult > 0 ) {
            fwd_taken++;
        } else if ( compResult == 0 ) {
            not_taken++;
        }
    }
}
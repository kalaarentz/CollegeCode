package com.company;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    /*
    To calculaate offset, needs to be 31-offset+1;
    to calculate index,
     */

    private int hits, misses, writeBacks, writeThroughs, offset, indexSize, numOfInstrucs;
    private boolean isDirectMapped, isWriteAllocate, isWriteThrough;
    private LinkedList<String[]> instructions = new LinkedList<>();
    private Cache cache;
    private float hitRate;

    public static void main(String[] args) {
	// write your code here
        new Main();
    }

    public Main() {
        readParametersFiles();
        readAcessFiles();
        analyzeInstructions();
        writeFile();
    }

    public void readParametersFiles() {

        try {
            File file = new File("parameters.txt");
            String[] parameters = new String[5];

            Scanner scan = new Scanner(file);
            int count = 0;
            while( scan.hasNext() ) {
                parameters[count] = scan.next();
                count++;
            }

            isDirectMapped = parameters[0].equals("1");
            offset = Integer.parseInt(parameters[1]);
            indexSize = Integer.parseInt( parameters[2]);
            isWriteAllocate = parameters[3].equals("wa");
            isWriteThrough = parameters[4].equals("wt");

            scan.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    public void readAcessFiles() {
        try {
            File file = new File("accesses.txt");
            Scanner scan = new Scanner(file);

            while( scan.hasNext() ) {
                String[] list = new String[5]; // r/w, address, tag, index, offset
                String[] split = scan.nextLine().split(" ");
                Long x = Long.parseLong( split[1], 16 );
                split[1] = StringUtils.leftPad( Long.toBinaryString(x), 32, "0");
                list[0] = split[0];
                list[1] = split[1];
                //System.out.println( list[1].length());
                list[2] = split[1].substring(0, 31-offset-indexSize+1);
                //System.out.println( list[2].length());
                list[3] = split[1].substring(31-offset-indexSize+1, 31-offset+1);
                //System.out.println( list[3].length());
                list[4] = split[1].substring(31-offset+1);
                //System.out.println( list[4].length());
                instructions.add(list);
                numOfInstrucs++;
                //System.out.println(Arrays.toString(list));
            }
            scan.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        try  {
            File file = new File( "statistics.txt");
            FileWriter fw = new FileWriter(  file );
            BufferedWriter bw = new BufferedWriter( fw );
            NumberFormat formatter = new DecimalFormat("#0.000000");
            hitRate = ((float) hits)/ ((float) numOfInstrucs);

            bw.write("hits: " + hits);
            bw.newLine();
            bw.write( "misses: " + misses);
            bw.newLine();
            bw.write( "hrate: " + formatter.format(hitRate) );
            bw.newLine();
            bw.write( "wb: " + writeBacks);
            bw.newLine();
            bw.write("wt: " + writeThroughs);
            bw.newLine();

            bw.close();
            fw.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void analyzeInstructions() {

        if( isDirectMapped ) {
            cache = new DirectMapped(indexSize);
            int[] tmp = cache.analyzeInstructions(instructions, isWriteAllocate, isWriteThrough );
            hits = tmp[0];
            misses = tmp[1];
            writeBacks = tmp[2];
            writeThroughs = tmp[3];
        } else {
            cache = new TwoWay(indexSize);
            int[] tmp = cache.analyzeInstructions(instructions, isWriteAllocate, isWriteThrough );
            hits = tmp[0];
            misses = tmp[1];
            writeBacks = tmp[2];
            writeThroughs = tmp[3];
        }
    }
}

package com.company;

import java.util.LinkedList;

/**
 * Created by kalaarentz on 4/24/17.
 */
public class DirectMapped implements Cache {

    private int[][] cache;
    private int hits, misses, writeBacks, writeThroughs;
    // hits misses wb wt
    private int[] returnVals;

    public DirectMapped(int size) {
        cache = new int[(int) (int) Math.pow(2,size)][3];
        returnVals = new int[4];

        for( int i = 0; i < cache.length; i++ ) {
            cache[i][0] = 0;
            cache[i][1] = 0;
            cache[i][2] = 0;
        }
    }

    public int[] analyzeInstructions(LinkedList<String[]> instructions, boolean isWriteAllocate, boolean isWriteThrough ) {

        for( String[] list : instructions ) {

            String type = list[0];
            int index = Integer.parseInt(list[3], 2);
            int tag = Integer.parseInt(list[2], 2);

            //System.out.println("index: " + index + " tag: " + tag);

            if( isWriteAllocate && isWriteThrough ) {
                // write allocate and write through
                // every hit or miss of writeThrough cache will incremete the writeThrough variable
                if( cache[index][0] == 0 ) {
                    misses++;
                    writeThroughs++;
                    cache[index][0] = 1;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                } else if ( cache[index][0] == 1 && cache[index][2] != tag ) {
                    misses++;
                    writeThroughs++;
                    cache[index][0] = 1;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                } else {
                    hits++;
                    writeThroughs++;
                }

            } else if( isWriteAllocate && !isWriteThrough ) {
                //write allocate and write-back
                if( cache[index][0] == 0 ) {
                    // miss because valid bit is 0
                    misses++;
                    cache[index][0] = 1;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                } else if ( cache[index][0] == 1 && cache[index][2] != tag ) {
                    // miss because the tags are not the same
                    misses++;
                    writeBacks++;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                } else if ( cache[index][0] == 1 && cache[index][2] == tag ) {
                    hits++;
                }

            } else if( !isWriteAllocate && isWriteThrough ) {
                // write-no-allocate and write through
                if( cache[index][0] == 0 ) {
                    // miss
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][0] = 1;
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                    }
                } else if ( cache[index][0] == 1 && cache[index][2] != tag ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                    }
                } else {
                    hits++;
                    writeThroughs++;
                }

            } else {
                // write-no allocate and write back
                //writeBack cache && write-no-allocate increment write through will be incremented on misses
                if( cache[index][0] == 0 ) {
                    // miss
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][0] = 1;
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                    }
                } else if ( cache[index][0] == 1 && cache[index][2] != tag ) {
                    misses++;
                    if( type.equals("r") ) {
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                    } else if (type.equals("w") ) {
                        writeThroughs++;
                    }

                } else {
                    hits++;
                }
            }
        }

        returnVals[0] = hits;
        returnVals[1] = misses;
        returnVals[2] = writeBacks;
        returnVals[3] = writeThroughs;

        return returnVals;
    }

}

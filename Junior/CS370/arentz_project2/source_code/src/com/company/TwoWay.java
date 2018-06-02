package com.company;

import java.util.LinkedList;

/**
 * Created by kalaarentz on 4/24/17.
 */
public class TwoWay implements Cache {

    int [][] cache;
    private int hits = 0;
    private int misses = 0;
    private int writeBacks = 0;
    private int writeThroughs = 0;
    private int[] returnVals;
    private int[] fifo;

    public TwoWay( int size ) {
        //[valid 0][dirty 0][tag 0][valid 1][dirty 1][tag 1]
        cache = new int[(int) (int) Math.pow(2, size)][6];
        fifo = new int[(int) (int) Math.pow(2, size)];
        // hits misses wb wt
        returnVals = new int[4];

        for(int i = 0; i < cache.length; i++ ) {
            fifo[i] = 0;
            cache[i][0] = 0; //valid 1
            cache[i][1] = 0; //dirty 1
            cache[i][2] = 0; // tag 1
            cache[i][3] = 0; // valid 2
            cache[i][4] = 0; // dirty 2
            cache[i][5] = 0; // tag 3
        }
    }

    public int[] analyzeInstructions(LinkedList<String[]> instructions, boolean isWriteAllocate, boolean isWriteThrough ) {

        for( String[] list : instructions ) {

            String type = list[0];
            int index = Integer.parseInt(list[3], 2);
            int tag = Integer.parseInt(list[2], 2);

            //System.out.println("index: " + index + " tag: " + tag);

            if( isWriteAllocate && isWriteThrough ) {
                // every hit or miss of writeThrough cache will incremete the writeThrough variable
                if( cache[index][0] == 0 ) {
                    misses++;
                    writeThroughs++;
                    cache[index][0] = 1;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                    fifo[index] = 0;
                } else if ( cache[index][0] == 1 && cache[index][3] == 0 ) {
                    misses++;
                    writeThroughs++;
                    cache[index][3] = 1;
                    cache[index][4] ^= 1;
                    cache[index][5] = tag;
                    fifo[index] = 1;
                } else if ( cache[index][0] == 1 && cache[index][2] == tag ) {
                    //matches tag in the first row
                    hits++;
                    writeThroughs++;
                } else if ( cache[index][3] == 1 && cache[index][5] == tag ) {
                    hits++;
                    writeThroughs++;
                } else if( cache[index][3] != tag || cache[index][5] != tag ) {
                    misses++;
                    writeThroughs++;
                    switchFIFO( index, tag);
                }

            } else if( isWriteAllocate && !isWriteThrough ) {
                //write allocate write back
                if( cache[index][0] == 0 ) {
                    misses++;
                    cache[index][0] = 1;
                    cache[index][1] ^= 1;
                    cache[index][2] = tag;
                    fifo[index] = 0;
                } else if ( cache[index][0] == 1 && cache[index][3] == 0 ) {
                    misses++;
                    cache[index][3] = 1;
                    cache[index][4] ^= 1;
                    cache[index][5] = tag;
                    fifo[index] = 1;
                } else if ( cache[index][0] == 1 && cache[index][2] == tag ) {
                    //matches tag in the first row
                    hits++;
                } else if ( cache[index][3] == 1 && cache[index][5] == tag ) {
                    hits++;
                } else if( cache[index][3] != tag || cache[index][5] != tag ) {
                    misses++;
                    writeBacks++;
                    switchFIFO( index, tag);
                }

            } else if( !isWriteAllocate && isWriteThrough ) {
                // write no allocate write through
                if( cache[index][0] == 0 ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][0] = 1;
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                        fifo[index] = 0;
                    }
                } else if ( cache[index][0] == 1 && cache[index][3] == 0 ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][3] = 1;
                        cache[index][4] ^= 1;
                        cache[index][5] = tag;
                        fifo[index] = 1;
                    }
                } else if ( (cache[index][0] == 1 && cache[index][2] == tag)
                        || cache[index][3] == 1 && cache[index][5] == tag ) {
                    //matches tag in the first row
                    hits++;
                    writeThroughs++;
                }  else if( cache[index][3] != tag || cache[index][5] != tag ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        switchFIFO( index, tag);
                    }
                }

            } else {
                //writeBack cache && write-no-allocate increment writethrough will be incremented on misses
                if( cache[index][0] == 0 ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][0] = 1;
                        cache[index][1] ^= 1;
                        cache[index][2] = tag;
                        fifo[index] = 0;
                    }
                } else if ( cache[index][0] == 1 && cache[index][3] == 0 ) {
                    misses++;
                    writeThroughs++;
                    if( type.equals("r") ) {
                        cache[index][3] = 1;
                        cache[index][4] ^= 1;
                        cache[index][5] = tag;
                        fifo[index] = 1;
                    }
                } else if ( (cache[index][0] == 1 && cache[index][2] == tag)
                        || cache[index][3] == 1 && cache[index][5] == tag ) {
                    //matches tag in the first row
                    hits++;
                }  else if( cache[index][3] != tag || cache[index][5] != tag ) {
                    misses++;
                    if( type.equals("r") ) {
                        switchFIFO( index, tag);
                    } else {
                        writeThroughs++;
                    }
                }
            }

        }

        returnVals[0] = hits;
        returnVals[1] = misses;
        returnVals[2] = writeBacks;
        returnVals[3] = writeThroughs;

        return returnVals;
    }

    private void switchFIFO( int index, int tag ) {
        if( fifo[index] == 0 ) {
            // replace in the first row
            // fifo will be changed to the other row now
            cache[index][1] ^= 1;
            cache[index][2] = tag;
            fifo[index] ^= 1;
        } else {
            // replace in the second row
            // fifo will be changed to the other row now
            cache[index][4] ^= 1;
            cache[index][5] = tag;
            fifo[index] ^= 1;
        }
    }
}

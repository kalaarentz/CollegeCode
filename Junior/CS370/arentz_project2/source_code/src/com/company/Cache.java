package com.company;

import java.util.LinkedList;

/**
 * Created by kalaarentz on 4/24/17.
 */
public interface Cache {

    int[] analyzeInstructions(LinkedList<String[]> instructions, boolean isWriteAllocate, boolean isWriteThrough );
}

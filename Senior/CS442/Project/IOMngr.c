/*
 Author:      Kala Arentz
 Created:     9/22/17
 Resources:   https://www.cprogramming.com/tutorial/cfileio.html
              Ben Black
*/

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <memory.h>
#include "IOMngr.h"

FILE *sourceFile = NULL;
FILE *listingFile = NULL;
unsigned int curLineNumber = 0;

char originalCharBuffer[MAXLINE];
char postMessageCharBuffer[MAXLINE];

char *indicatorLine = NULL;

struct MessageInfo {
    int columnPosition;
    char postMsgChar;
    char *postMsg;
    int lenOfStr;
};

#define MAX_POST_MESSAGES 26
struct MessageInfo postMessageArray[MAX_POST_MESSAGES];
char currentPostMsgChar = 'A';

void PrintMessages();

bool OpenFiles(const char *aSourceName, const char *aListingName) {
    sourceFile = fopen(aSourceName, "r");
    if (!sourceFile) {
        fprintf(stderr, "Can't open source file: %s \n", aSourceName);
        return false;
    }
    if (!aListingName) {
        listingFile = stdout;
        return true;
    }
    listingFile = fopen(aListingName, "w");
    if (!listingFile) {
        fprintf(stderr, "Can't open listing file: %s \n", aListingName);
        return false;
    }
    return true;
}

void CloseFiles() {
    // clear buffer
    PrintMessages();
    fclose(sourceFile);
    if (listingFile != stdout) {
        fclose(listingFile);
    }
}

char GetSourceChar() {
    // check for a new line this is when you need to print prev line based off messages in PostMessage
    if (!indicatorLine || *indicatorLine == '\0') {
        // print prev line and any messages
        if (curLineNumber) {
            PrintMessages();
        }
        curLineNumber++;
        indicatorLine = fgets(originalCharBuffer, MAXLINE, sourceFile);
        if (!indicatorLine) {
            // clear buffer
            originalCharBuffer[0] = '\0';
            return EOF;
        }
    }
    if (*indicatorLine == '\t') {
        *indicatorLine = ' ';
    }
    return *indicatorLine++;
}

void PrintMessages() {
    if (listingFile != stdout || currentPostMsgChar != 'A') {
        fprintf(listingFile, "%5d: %s", curLineNumber, originalCharBuffer);

        int column = 0;
        struct MessageInfo *postMessageInfoPointer = postMessageArray;
        char postMessageChar = 'A';
        int currentLineLength = indicatorLine - originalCharBuffer;
        while (postMessageChar < currentPostMsgChar && column < currentLineLength) {
            // discard messages pointing to invalid columns
            while(postMessageChar < currentPostMsgChar && postMessageInfoPointer->columnPosition < 0 ) {
                postMessageInfoPointer++;
                postMessageChar++;
            }
            // space over to error position
            // if the originalCharBuffer does not have " " than have the '-' char
            while (postMessageChar < currentPostMsgChar && column < postMessageInfoPointer->columnPosition) {
                if(originalCharBuffer[column] == ' ') {
                    postMessageCharBuffer[column] = ' ';
                } else {
                    postMessageCharBuffer[column] = '-';
                }
                column++;
            }
            // post messages on current column (overwrite if necessary)
            while(postMessageChar < currentPostMsgChar && postMessageInfoPointer->columnPosition == column) {
                postMessageCharBuffer[column] = postMessageInfoPointer->postMsgChar;
                postMessageInfoPointer++;
                postMessageChar++;
            }
            column++;
        }
        postMessageCharBuffer[column] = '\0';
        if (currentPostMsgChar != 'A') {
            fprintf(listingFile, "       %s\n", postMessageCharBuffer);
        }

        postMessageInfoPointer = postMessageArray;
        for (char postMessageChar = 'A';
             postMessageChar < currentPostMsgChar; postMessageChar++, postMessageInfoPointer++) {
            fprintf(listingFile, "    -%c %s\n", postMessageChar, postMessageInfoPointer->postMsg);
            free(postMessageInfoPointer->postMsg);
        }
    }
    currentPostMsgChar = 'A';
}

void PostMessage(int aColumn,int aLength, const char *aMessage) {
    if (currentPostMsgChar <= 'Z' && aColumn < MAXLINE) {
        int postMessageArrayIndex = currentPostMsgChar - 'A';
        postMessageArray[postMessageArrayIndex].columnPosition = aColumn;
        postMessageArray[postMessageArrayIndex].postMsgChar = currentPostMsgChar;
        postMessageArray[postMessageArrayIndex].postMsg = strdup(aMessage);
        postMessageArray[postMessageArrayIndex].lenOfStr = aLength;
        currentPostMsgChar++;
    }
}

int GetCurrentLine() {
    return curLineNumber;
}

int GetCurrentColumn() {
    return (int) (indicatorLine - originalCharBuffer) - 1;
}


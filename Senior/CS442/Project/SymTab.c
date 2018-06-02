/*
 Author:      Kala Arentz
 Created:     9/8/17
 Resources:   https://www.tutorialspoint.com/c_standard_library/c_function_calloc.htm
              https://www.jetbrains.com/help/clion/quick-cmake-tutorial.html
              Suzy Ratcliff
*/

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <limits.h>

#include "SymTab.h"

struct SymTab *
CreateSymTab(int size, char *scopeName, struct SymTab *parentTable) {

    struct SymTab *table = calloc(1, sizeof(struct SymTab));
    table->size = size;
    table->scopeName = scopeName ? strdup(scopeName) : NULL;

    // parentTable is not null, then the created table will reference parentTable
    table->parent = parentTable ? parentTable : NULL;
    table->contents = calloc(size, sizeof(struct SymEntry *));
    return table;
}

void
RemoveEntry(struct SymEntry *anEntry, int cnt, void *ignore) {
    free(anEntry->name);
    free(anEntry);
}

struct SymTab *
DestroySymTab(struct SymTab *aTable) {
    // returns parentTable
    struct SymTab *parentTab = aTable->parent;
    // free all the entrys in the table
    DoForEntries(aTable, false, RemoveEntry, 0, NULL);

    free(aTable->contents);
    free(aTable);
    return parentTab;
}

int
HashName(int size, const char *name) {
    int sum = 0;
    for (int i = 0; i < strlen(name); i++) {
        sum += name[i];
    }
    return sum % size;
}

struct SymEntry *
FindHashedName(struct SymTab *aTable, int hashValue, const char *name) {
    struct SymEntry *e = aTable->contents[hashValue];
    while (e) {
        if (strcmp(e->name, name) == 0) {
            return e;
        }
        e = e->next;
    }
    return NULL;
}

struct SymEntry *
LookupName(struct SymTab *aTable, const char *name) {
    int hashVal = HashName(aTable->size, name);
    struct SymEntry *e = FindHashedName(aTable, hashVal, name);

    if (e) {
        return e;
    } else {
        if (aTable->parent) {
            return LookupName(aTable->parent, name);
        } else {
            return NULL;
        }
    }
}

struct SymEntry *
CreateEntry(struct SymTab *aTable, const char *name) {
    struct SymEntry *new = malloc(sizeof(struct SymEntry));
    new->name = strdup(name);
    new->table = aTable;
    new->attributes = NULL;
    new->next = NULL;
    return new;
}

struct SymEntry *
EnterName(struct SymTab *aTable, const char *name) {
    int hashVal = HashName(aTable->size, name);
    struct SymEntry *e = LookupName(aTable, name);

    if (e && strcmp(e->table->scopeName, aTable->scopeName) == 0) {
        // e exists and the table for the entry matches the table passed
        // throughs scopeName return e
        return e;
    } else if (e && strcmp(e->table->scopeName, aTable->scopeName) != 0) {
        // e exists but the two tables do not have the same scopeNames
        // create a new entry and add to the beginning of this contents
        struct SymEntry *newEntry = CreateEntry(aTable, name);
        newEntry->next = aTable->contents[hashVal];
        aTable->contents[hashVal] = newEntry;
        return newEntry;
    } else {
        struct SymEntry *newEntry = CreateEntry(aTable, name);
        newEntry->next = aTable->contents[hashVal];
        aTable->contents[hashVal] = newEntry;
        return newEntry;
    }
}

int
GetAttrKind(struct SymEntry *anEntry) {
    if(anEntry){
      return anEntry->attrKind;
    }
  return 0;
}

void
SetAttr(struct SymEntry *anEntry, int kind, void *attributes) {
    anEntry->attrKind = kind;
    anEntry->attributes = attributes;
}

void *
GetAttr(struct SymEntry *anEntry) {
    if(anEntry) {
        if(!anEntry->attributes) return NULL;
        return anEntry->attributes;
    } else {
        return NULL;
    }
}

const char *
GetName(struct SymEntry *anEntry) {
    if(anEntry) {
        if(!anEntry->name) return NULL;
        return anEntry->name;
    }
    return NULL;
}

struct SymTab *
GetTable(struct SymEntry *anEntry) {
    if(anEntry) {
        if(!anEntry->table) return NULL;
        return anEntry->table;
    }
    return NULL;
}

const char *
GetScopeName(struct SymTab *aTable) {
    if(aTable) {
        if (!aTable->scopeName) return NULL;
        return aTable->scopeName;
    }
    return NULL;
}

char *
CombineTwoStrings(char *str1, char *mid, char *str2) {
    // adding two so take care of NULL string and space between words
    int lengthOfStrings = strlen(str1) + strlen(str2) + 2;
    char *twoStringsCombined = malloc(sizeof(char) * lengthOfStrings);
    snprintf(twoStringsCombined, lengthOfStrings, "%s%s%s", str1,mid,str2);
    return twoStringsCombined;
}

char *
GetScope(struct SymTab *aTable) {

    struct SymTab *parentTab = aTable->parent;
    char *scope = strdup(aTable->scopeName);

    while (parentTab) {
        scope = parentTab->scopeName ? CombineTwoStrings(parentTab->scopeName, ">", scope) : CombineTwoStrings(parentTab->scopeName, "",scope);
        parentTab = parentTab->parent;
    }
    return scope;
}

struct SymTab *
GetParentTable(struct SymTab *aTable) {
    if(aTable) {
        if(!aTable->parent) return NULL;
        return aTable->parent;
    }
    return NULL;
}

void
DoForEntries(struct SymTab *aTable, bool includeParentTable,
             void (*entryFunc)(struct SymEntry *entry, int cnt, void *args),
             int startCnt, void *withArgs) {
    for (int i = 0; i < aTable->size; i++) {
        struct SymEntry *e = aTable->contents[i];
        while (e) {
            entryFunc(e, startCnt, withArgs);
            e = e->next;
            startCnt++;
        }
    }

    // if includeParentTable is true and the aTable has a parent
    // will need to search parentTable as well
    if (includeParentTable && aTable->parent) {
        // if the parentTable has a parent then pass true to keep
        // searching, if the parentTable does not have a parent
        // then pass false
        struct SymTab * parentTab = aTable->parent;
        if (parentTab->parent) {
            return DoForEntries(parentTab, true, entryFunc, startCnt, withArgs);
        } else {
            return DoForEntries(parentTab, false, entryFunc, startCnt, withArgs);
        }
    } else {
        return;
    }
}

struct Stats *
Statistics(struct SymTab *aTable) {
    struct Stats *statsObj = malloc(sizeof(struct Stats));
    int min = aTable->size;
    int max = 0;
    int sum = 0;
    int len = 0;

    for (int i = 0; i < aTable->size; i++) {
        struct SymEntry *entry = aTable->contents[i];
        len = 0;
        while (entry) {
            len++;
            entry = entry->next;
        }
        sum += len;
        if (len < min) min = len;
        if (len > max) max = len;
    }

    statsObj->minLen = min;
    statsObj->maxLen = max;
    statsObj->avgLen = sum / aTable->size;
    statsObj->entryCnt = sum;
    return statsObj;
}

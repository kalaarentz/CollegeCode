/*
 *  Author: Kala Arentz
 *  Created: 11/12/17
 *  Resources: Suzy Ratcliff
 *             Ben Black
 *             Cameron Trim
 *             Ben Klipfel
               Phuong Nguyen
 * Semantics.c
 Support and semantic action routines.

 */

#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "YSemantics.h"

// Shared Data

struct SymTab * IdentifierTable;
struct SymTab * StringLiteralTable;
enum AttrKinds { VOID_KIND = -1, INT_KIND, STRING_KIND, STRUCT_KIND };

char * BaseTypeNames[2] = { "int", "chr"};
char * TypeNames[2] = { "", "func"};
struct InstrSeq * globalCode;

// corresponds to enum Operators
char * Ops[] = { "add", "sub", "mul", "div"};
char * BoolOps[] = { "and", "or", "not" };

// corresponds to negation of enum Comparisons
// enum Comparisons { LessThan, LessEqual, GreaterThan, GreaterEqual, Equal, NotEqual };
char * Branches[] = { "bge", "bg", "ble", "bl", "bne", "beq"};
// comparison set instructions, in order with enum Comparisons
char * CmpSetReg[] = { "slt", "sle", "sgt", "sge", "seq", "sne" };


// Supporting Routines
void
PostMessageAndExit(int col, char * message) {
  PostMessage(col,1,message);
  CloseFiles();
  exit(0);
}

void
InitSemantics() {
  IdentifierTable = CreateSymTab(100,"global",NULL);
  globalCode = GenInstr(NULL,".data",NULL,NULL,NULL);
  StringLiteralTable = CreateSymTab(100,"global",NULL);
}

char *
StringForType(struct TypeDesc * desc) {
  switch (desc->declType) {
    case PrimType: {
      return strdup(BaseTypeNames[desc->primDesc]);
    } break;
    case FuncType: {
      return strdup(BaseTypeNames[desc->funcDesc->returnType]);
    } break;
  }
}

struct TypeDesc *
MakePrimDesc(enum BaseTypes type) {
  struct TypeDesc * desc = malloc(sizeof(struct TypeDesc));
  desc->declType = PrimType;
  desc->primDesc = type;
  return desc;
}

struct TypeDesc *
MakeFuncDesc(enum BaseTypes returnType) {
  struct TypeDesc * desc = malloc(sizeof(struct TypeDesc));
  desc->declType = FuncType;
  desc->funcDesc = malloc(sizeof(struct FuncDesc));
  desc->funcDesc->returnType = returnType;
  desc->funcDesc->funcCode = NULL;
  return desc;
}

void displayEntry(struct SymEntry * entry, int cnt, void * ignore) {
  char * scope = GetScope(GetTable(entry));
  printf("%3d %-20s %-15s",cnt,scope,GetName(entry));
  free(scope);
  int attrKind = GetAttrKind(entry);
  struct Attr * attr = GetAttr(entry);
  switch (attrKind) {
    case VOID_KIND: {
    } break;
    case INT_KIND: {
    } break;
    case STRING_KIND: {
    } break;
    case STRUCT_KIND: {
      if (attr) {
        printf("%-10s",attr->reference);
        printf("     ");
        struct TypeDesc * desc = attr->typeDesc;
        char * typeStr = StringForType(desc);
        printf("%-10s ",typeStr);
        free(typeStr);
      }
      else {
        printf("empty");
      }
    } break;
  }
  printf("\n");
}

void
ListIdentifierTable() {
  printf("\nIdentifier Symbol Table\n");
  printf("Num Scope                Name           Ref       Kind Type\n");
  DoForEntries(IdentifierTable,true,displayEntry,0,NULL);
}

void
FreeTypeDesc(struct TypeDesc * desc) {
  switch (desc->declType) {
    case PrimType: {
    } break;
    case FuncType: {
      if (desc->funcDesc->funcCode) FreeSeq(desc->funcDesc->funcCode);
      free(desc->funcDesc);
    } break;
  }
  free(desc);
}

// provided to the SymTab for destroying table contents
void
FreeEntryAttr(struct SymEntry * entry, int cnt, void * ignore) {
  int attrKind = GetAttrKind(entry);
  struct Attr * attr = GetAttr(entry);
  switch (attrKind) {
    case VOID_KIND: {
    } break;
    case INT_KIND: {
    } break;
    case STRING_KIND: {
    } break;
    case STRUCT_KIND: {
      if (attr) {
        if (attr->typeDesc) FreeTypeDesc(attr->typeDesc);
        if (attr->reference) free(attr->reference);
        free(attr);
      }
    } break;
  }
}

// Semantics Actions

void
Finish() {
  printf("Finish\n");
  ListIdentifierTable();

  struct InstrSeq * textCode = GenInstr(NULL,".text",NULL,NULL,NULL);
  struct InstrSeq * dataCode = GenInstr(NULL,".data",NULL,NULL,NULL);
  // form module preamble
  struct SymEntry * mainEntry = LookupName(IdentifierTable,"main");
  if (!mainEntry) {
    PostMessageAndExit(GetCurrentColumn(),"no main function for module");
  }
  // should make sure main takes no arguments
  struct Attr * mainAttr = GetAttr(mainEntry);

  // need to keep spim happy
  AppendSeq(textCode,GenInstr(NULL,".globl","__start",NULL,NULL));
  AppendSeq(textCode,GenInstr("__start",NULL,NULL,NULL,NULL));

  AppendSeq(textCode,GenInstr(NULL,"jal",mainAttr->reference,NULL,NULL));
  AppendSeq(textCode,GenInstr(NULL,"li","$v0","10",NULL));
  AppendSeq(textCode,GenInstr(NULL,"syscall",NULL,NULL,NULL));

  // put globals in data seg
  for(int i = 0; i < IdentifierTable->size; i++){
    struct SymEntry * idEntry = IdentifierTable->contents[i];
    while (idEntry){
      struct Attr * attr = GetAttr(idEntry);
      //printf("entry name: %s - %d > %d\n", idEntry->name, attr->typeDesc->declType, FuncType);
      if(attr->typeDesc->declType == PrimType){
        AppendSeq(dataCode, GenInstr(attr->reference,".word","0",NULL,NULL));
      }
      else{
        AppendSeq(textCode, attr->typeDesc->funcDesc->funcCode);
      }
      idEntry = idEntry->next;
    }
  }

  for(int i = 0; i < StringLiteralTable->size; i++){
    struct SymEntry * content = StringLiteralTable->contents[i];
    while (content){
      struct StringLiteral * attr = GetAttr(content);
      //printf("\n%s\n\n",attr->name);
      AppendSeq(dataCode, attr->instrSeq);
      free(attr->name);
      free(attr);
      content = content->next;
    }
  }
  // put functions in code seq

  // combine and write
  struct InstrSeq * moduleCode = AppendSeq(textCode,dataCode);
  WriteSeq(moduleCode);

  // free code
  FreeSeq(moduleCode);
  CloseCodeGen();
  fprintf(stderr,"Finish \n");
}

void
ProcDecls(struct IdList * idList, enum BaseTypes baseType) {
  struct IdList * temp = idList;
  // walk IdList items
  // names on IdList are only specified as PrimType or FuncType
  // set type desc
  // Try to make a struct here
  //printf("%s\n",temp->entry->name);
  while(temp){
    struct Attr * newAttr = GetAttr(temp->entry);
    if(GetAttrKind(temp->entry) == PrimType){
      newAttr->typeDesc = MakePrimDesc(baseType);
      char * name = calloc(strlen(temp->entry->name)+2, sizeof(char));
      sprintf(name,"_%s",temp->entry->name);
      AppendSeq(globalCode,GenInstr(name,".word","0",NULL,NULL));
    } else {
      newAttr->typeDesc = MakeFuncDesc(baseType);
    }
    newAttr->reference = calloc(strlen(temp->entry->name)+2, sizeof(char));
    sprintf(newAttr->reference, "_%s", temp->entry->name);

    SetAttr(temp->entry, STRUCT_KIND, newAttr);
    temp = temp->next;
  }
}

struct IdList *
AppendIdList(struct IdList * item, struct IdList * list) {
  item->next = list;
  return item;
}

struct IdList *
ProcName(char * id, enum DeclTypes  type) {
  // get entry for id, error if it exists
  // Print error
  // enter id
  // create IdList node for entry
    // create, init and set attr struct
    // create and init type descriptor
  if(LookupName(IdentifierTable,id)){
    printf("entry already exists: %s", id);
  }

  struct SymEntry * newEntry = EnterName(IdentifierTable, id);
  newEntry->attrKind = type;
  struct IdList * newIdList = calloc(1, sizeof(struct IdList));
  struct Attr * newAttr = calloc(1, sizeof(struct Attr));
  struct TypeDesc * newTypeDes = calloc(1, sizeof(struct TypeDesc));
  newAttr->typeDesc = newTypeDes;
  SetAttr(newEntry, type, newAttr);
  newIdList->entry = newEntry;

  free(id);

  return newIdList;
}

void
ProcFunc(char * id, struct InstrSeq * instrs) {
  // lookup name
  // return error
  // get attr
  // function entry
  // include function body code
  // function exit code
  struct SymEntry * entry = LookupName(IdentifierTable, id);
  if( !entry ){
    printf("error in ProcFunc()");
  }
  struct Attr * attr = GetAttr(entry);
  struct InstrSeq * bodyCode = GenInstr(attr->reference, NULL, NULL, NULL, NULL);
  AppendSeq(bodyCode, instrs);
  AppendSeq(bodyCode, GenInstr(NULL,"jr","$ra",NULL,NULL));
  attr->typeDesc->funcDesc->funcCode = bodyCode;
}

struct ExprResult *
AddOp(struct ExprResult *leftExpr,enum Operators operators, struct ExprResult *rightExpr ){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->instrSeq = calloc(1, sizeof(struct InstrSeq));

  result->registers = AvailTmpReg();
  struct InstrSeq * bodyCode =  leftExpr->instrSeq;
  AppendSeq(bodyCode, rightExpr->instrSeq);

  if ( operators == Add) {
    AppendSeq(bodyCode,
      GenInstr(NULL,"add",TmpRegName(result->registers),TmpRegName(leftExpr->registers),TmpRegName(rightExpr->registers)));
  } else {
    AppendSeq(bodyCode,
      GenInstr(NULL,"sub",TmpRegName(result->registers),TmpRegName(leftExpr->registers),TmpRegName(rightExpr->registers)));
  }
  ReleaseTmpReg(leftExpr->registers);
  ReleaseTmpReg(rightExpr->registers);
  result->instrSeq = bodyCode;
  return result;
}

struct ExprResult *
MultOp(struct ExprResult *leftExpr, enum Operators operators, struct ExprResult *rightExpr ){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->instrSeq = calloc(1, sizeof(struct InstrSeq));
  result->registers = AvailTmpReg();
  struct InstrSeq * bodyCode = leftExpr->instrSeq;

  AppendSeq(bodyCode, rightExpr->instrSeq);

  if ( operators == Mult ) {
    AppendSeq(bodyCode, GenInstr(NULL,"mul",TmpRegName(result->registers),TmpRegName(leftExpr->registers),TmpRegName(rightExpr->registers)));
  } else {
    AppendSeq(bodyCode, GenInstr(NULL,"div",TmpRegName(result->registers),TmpRegName(leftExpr->registers),TmpRegName(rightExpr->registers)));
  }
  ReleaseTmpReg(leftExpr->registers);
  ReleaseTmpReg(rightExpr->registers);
  result->instrSeq = bodyCode;
  return result;
}

struct ExprResult *
GetExpr(int type){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->instrSeq = calloc(1, sizeof(struct InstrSeq));
  result->registers = AvailTmpReg();

  if ( type == IntBaseType ){
    result->instrSeq = GenInstr(NULL,"li","$v0","5",NULL);
  } else if ( type == ChrBaseType ){
    result->instrSeq = GenInstr(NULL,"li","$v0","12",NULL);
  }
  AppendSeq(result->instrSeq, GenInstr(NULL,"syscall",NULL,NULL,NULL));
  AppendSeq(result->instrSeq, GenInstr(NULL,"move",TmpRegName(result->registers),"$v0",NULL));

  return result;
}

struct InstrSeq *
PutExpr(struct ExprResult * expr){
  struct InstrSeq * bodyCode = expr->instrSeq;
  if ( expr->type == IntBaseType ) {
    AppendSeq(bodyCode, GenInstr(NULL,"li","$v0","1",NULL));
  } else {
    AppendSeq(bodyCode, GenInstr(NULL,"li","$v0","11",NULL));
  }

  AppendSeq(bodyCode, GenInstr(NULL,"move","$a0",TmpRegName(expr->registers),NULL));
  AppendSeq(bodyCode, GenInstr(NULL,"syscall",NULL,NULL,NULL));

  ReleaseTmpReg(expr->registers);
  return bodyCode;
}


struct ExprResult *
NegateExpr(struct ExprResult * expr){
  int tempReg1 = AvailTmpReg();
  int tempReg2 = AvailTmpReg();

  AppendSeq(expr->instrSeq, GenInstr(NULL,"li",TmpRegName(tempReg1),"-1",NULL));
  AppendSeq(expr->instrSeq, GenInstr(NULL,"mult",TmpRegName(tempReg2),TmpRegName(tempReg1),TmpRegName(expr->registers)));

  expr->registers = tempReg2;
  ReleaseTmpReg(tempReg1);

  return expr;
}

struct ExprResult *
LoadInt(char * i){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->registers = AvailTmpReg();
  result->instrSeq = GenInstr(NULL,"li", TmpRegName(result->registers), i,NULL);
  result->type = IntBaseType;

  return result;
}

struct ExprResult *
LoadChar(char * c){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->instrSeq = calloc(1, sizeof(struct InstrSeq));
  result->registers = AvailTmpReg();
  result->type = ChrBaseType;
  int value = (int)c[1];

  char str[4];
  sprintf(str, "%d", value);

  result->instrSeq = GenInstr(NULL,"li", TmpRegName(result->registers), str, NULL);

  return result;
}

struct ExprResult *
LoadId(char * id){
  struct ExprResult * result = calloc(1, sizeof(struct ExprResult));
  result->instrSeq = calloc(1, sizeof(struct InstrSeq));
  result->registers = AvailTmpReg();

  struct SymEntry * entry = LookupName(IdentifierTable, id);
  if ( !entry ){
    // prinnt error
    printf("error! in LoadId()");
  }
  struct Attr * attr = GetAttr(entry);
  result->type = attr->typeDesc->primDesc;

  char * name = calloc(strlen(id) + 2, sizeof(char));
  sprintf(name, "_%s", id);

  result->instrSeq = GenInstr(NULL,"lw", TmpRegName(result->registers), name,NULL) ;

  free(id);
  // maybe free name and free id
  return result;
}

struct InstrSeq *
Assign(char * id, struct ExprResult * expr){
  char * name = calloc(strlen(id) + 2, sizeof(char));
  sprintf(name, "_%s", id);

  struct InstrSeq * bodyCode = expr->instrSeq;
  AppendSeq(bodyCode, GenInstr(NULL,"sw", TmpRegName(expr->registers), name,NULL));
  ReleaseTmpReg(expr->registers);

  free(id);
  return bodyCode;
}

char *
StrLiteral(char * c){
  struct StringLiteral * str;
  struct SymEntry * entry = LookupName(StringLiteralTable, c);

  if ( entry ) {
    str = GetAttr(entry);
  } else {
    struct SymEntry * newEntry = EnterName(StringLiteralTable, c);
    str = calloc(1,sizeof(struct StringLiteral));
    str->name = GenLabel();
    str->instrSeq = GenInstr(str->name, ".asciiz", c, NULL,NULL);
    SetAttr(newEntry, StrBaseType, str);
  }

   return str->name;
}

struct InstrSeq *
PutStr(char * s){
  struct InstrSeq * bodyCode = calloc(1, sizeof(struct InstrSeq));
  bodyCode = GenInstr(NULL,"li","$v0","4",NULL);
  AppendSeq(bodyCode, GenInstr(NULL,"la","$a0",s,NULL));
  AppendSeq(bodyCode, GenInstr(NULL,"syscall",NULL,NULL,NULL));

  return bodyCode;
}

struct ConditionalResult *
ConditionalOperation(struct ExprResult *leftExpr, enum Comparisons condition, struct ExprResult *rightExpr){
  struct ConditionalResult * result = calloc(1,sizeof(struct ConditionalResult));
  result->instrSeq = leftExpr->instrSeq;
  AppendSeq(result->instrSeq, rightExpr->instrSeq);

  result->label = GenLabel();
  AppendSeq(result->instrSeq, GenInstr(NULL, Branches[condition],
                TmpRegName(leftExpr->registers),   TmpRegName(rightExpr->registers), result->label));

  ReleaseTmpReg(leftExpr->registers);
  ReleaseTmpReg(rightExpr->registers);

  return result;
}

struct InstrSeq *
IfFunc(struct ConditionalResult * condResult, struct InstrSeq * instrSeq){
  struct InstrSeq * resultSeq = condResult->instrSeq;
  AppendSeq(resultSeq, instrSeq);
  AppendSeq(resultSeq, GenInstr(condResult->label, NULL,NULL,NULL,NULL));
  return resultSeq;
}

struct InstrSeq *
IfElseFunc(struct ConditionalResult * condResult, struct InstrSeq * ifInstrSeq, struct InstrSeq * elseInstrSeq){
  char * ifLabel = GenLabel();
  struct InstrSeq * resultSeq = condResult->instrSeq;
  AppendSeq(resultSeq, ifInstrSeq);
  AppendSeq(resultSeq, GenInstr(NULL,"b",ifLabel,NULL,NULL));
  AppendSeq(resultSeq, GenInstr(condResult->label,NULL,NULL,NULL,NULL));
  AppendSeq(resultSeq, elseInstrSeq);
  AppendSeq(resultSeq, GenInstr(ifLabel, NULL,NULL,NULL,NULL));

  free(condResult->label);
  free(condResult);
  free(ifLabel);

  return resultSeq;
}

struct InstrSeq *
WhileFunction(struct ConditionalResult * condResult, struct InstrSeq * instrSeq){
    char * newLabel = GenLabel();
    struct InstrSeq * resultSeq = GenInstr(newLabel,NULL,NULL,NULL,NULL);
    AppendSeq(resultSeq, condResult->instrSeq);
    AppendSeq(resultSeq, instrSeq);
    AppendSeq(resultSeq, GenInstr(NULL,"b",newLabel, NULL,NULL));
    AppendSeq(resultSeq, GenInstr(condResult->label ,NULL,NULL,NULL,NULL));
    free(newLabel);
    free(condResult->label);
    free(condResult);

    return resultSeq;
}

struct ExprResult *
IncDecFunc(struct ExprResult * expr, enum IncDecOps incDecOps){

  if ( incDecOps ==  Increment ){
    AppendSeq(expr->instrSeq, GenInstr(NULL, "addi", TmpRegName(expr->registers), TmpRegName(expr->registers), "1"));
  } else {
    AppendSeq(expr->instrSeq, GenInstr(NULL, "subi", TmpRegName(expr->registers), TmpRegName(expr->registers), "1"));
  }

  return expr;
}

struct InstrSeq *
ForLoop(struct InstrSeq * assignStmt, struct ConditionalResult * condResult, struct ExprResult * expr, struct InstrSeq * body){
  AppendSeq(body,expr->instrSeq);
  AppendSeq(assignStmt, WhileFunction(condResult, body));
  // Release Register maybe
  return body;
}

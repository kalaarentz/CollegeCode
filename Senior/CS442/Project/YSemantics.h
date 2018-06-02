/* Semantics.h
 The action and supporting routines for performing semantics processing.
 */

#include "SymTab.h"
#include "IOMngr.h"
#include "Scanner.h"
#include "YCodeGen.h"

// Declaration of semantic record data types used in grammar.y %union
struct IdList {
  struct SymEntry * entry;
  struct IdList * next;
};

struct StringLiteral {
  struct InstrSeq * instrSeq;
  char * name;
};

enum Operators { Add, Sub, Div, Mult};
enum Comparisons {LessThan, LessEqual, GreaterThan, GreaterEqual, Equal, NotEqual};
enum BaseTypes { IntBaseType, ChrBaseType, StrBaseType };
enum IncDecOps { Increment, Decrement};
enum DeclTypes { PrimType, FuncType };

struct FuncDesc {
  enum BaseTypes returnType;
  struct InstrSeq * funcCode;
};


struct TypeDesc {
  enum DeclTypes declType;
  union {
    enum BaseTypes primDesc;
    struct FuncDesc * funcDesc;
  };
};

// Symbol Table Structures
struct Attr {
  struct TypeDesc * typeDesc;
  char * reference;
};

// Expession result
struct ExprResult {
  struct InstrSeq * instrSeq;
  int registers;
  enum BaseTypes type;
};

// Condition result structure
struct ConditionalResult{
  struct InstrSeq * instrSeq;
  char * label;
};

// Supporting Routines

void InitSemantics();
void ListIdentifierTable();

struct TypeDesc * MakePrimDesc(enum BaseTypes type);
//struct TypeDesc * MakeFuncDesc(enum BaseTypes returnType, int argCnt, struct ArgTypeList * argTypes);
struct TypeDesc * MakeFuncDesc(enum BaseTypes returnType);
// Semantics Actions

//Create a struct ExprResult which holds both the code sequence that computes the expression and the
//register number where the result is located when the code executes. This will get added as an option
// for the "union" and associated with certain nonterminals. You may also want an enumerated type for
// the various operators or simply use the operator character.


void                    Finish();

void                    ProcDecls(struct IdList * idList, enum BaseTypes baseType);
struct IdList *         AppendIdList(struct IdList * item, struct IdList * list);
struct IdList *         ProcName(char * id, enum DeclTypes  type);
void                    ProcFunc(char * id, struct InstrSeq * instrs);

// printing method


struct ExprResult *     AddOp(struct ExprResult *leftExpr, enum Operators operators, struct ExprResult *rightExpr );
struct ExprResult *     MultOp(struct ExprResult *leftExpr, enum Operators operators, struct ExprResult *rightExpr );
struct ExprResult *     GetExpr(int type);
struct InstrSeq *       PutExpr(struct ExprResult * expr);
struct InstrSeq *       PutStr(char * s);
struct ExprResult *     NegateExpr(struct ExprResult * expr);

struct ExprResult *     LoadInt(char * i);
struct ExprResult *     LoadChar(char * c);
struct ExprResult *     LoadId(char * id);

char *                  StrLiteral(char * c);

struct ConditionalResult *     ConditionalOperation(struct ExprResult *leftExpr, enum Comparisons condition, struct ExprResult *rightExpr);

struct InstrSeq *       IfFunc(struct ConditionalResult * condResult, struct InstrSeq * instrSeq);
struct InstrSeq *       IfElseFunc(struct ConditionalResult * condResult, struct InstrSeq * ifInstrSeq, struct InstrSeq * elseInstrSeq);

struct InstrSeq *       WhileFunction(struct ConditionalResult * condResult, struct InstrSeq * instrSeq);

//struct InstrSeq *       CombineInstr(struct InstrSeq * leftSeq, struct InstrSeq * rightSeq);

struct InstrSeq *       Assign(char * id, struct ExprResult * expr);

struct ExprResult *     IncDecFunc(struct ExprResult * expr, enum IncDecOps incDecOps);
struct InstrSeq *       ForLoop(struct InstrSeq * assignStmt, struct ConditionalResult * condResult, struct ExprResult * expr, struct InstrSeq * body);

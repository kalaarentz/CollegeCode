%{
  #include "IOMngr.h"
  #include "Scanner.h"
  #include "YSemantics.h"
  #include <stdio.h>
  #include <string.h>

  void yyerror(char *s);
%}

/* Union structure of data that can be attached to non-terminals   */
%union     {
  int Integer;
  char * Text;
  struct IdList * IdList;
  enum BaseTypes BaseType;
  struct InstrSeq * InstrSeq;
  struct ExprResult * ExprResult;
  enum Operators Operators;
  enum Comparisons Comparisons;
  struct ConditionalResult * CondResult;
  enum IncDecOps IncDecOps;
}

/* Type declaration for data attched to non-terminals. Allows      */
/* $# notation to be correctly type casted for function arguments. */
/* %type <union-field-name> non-terminal                           */
%type <Text> Id
%type <Text> String

%type <IdList> DeclList
%type <IdList> DeclItem

%type <BaseType> Type


%type <InstrSeq> DeclImpls
%type <InstrSeq> FuncBody
%type <InstrSeq> FuncStmts
%type <InstrSeq> Stmt
%type <InstrSeq> AssignStmt
%type <InstrSeq> PutStmt
%type <InstrSeq> WhileStmt
%type <InstrSeq> IfElseStmt
%type <InstrSeq> IfStmt
%type <InstrSeq> ForStmt
//%type <InstrSeq> SwitchStmt
%type <InstrSeq> Case
%type <InstrSeq> CaseList

%type <CondResult> Cond

%type <ExprResult> Factor
%type <ExprResult> Expr
%type <ExprResult> Term

%type <Operators> MultOp
%type <Operators> AddOp

%type <IncDecOps> IncDecOp

%type <Comparisons> BoolOps


/* List of token grammar name and corresponding numbers */
/* y.tab.h will be generated from these for use by scanner*/
%token IDENT_TOK     1
%token DECL_TOK      2
%token IMPL_TOK      3
%token INT_TOK       4
%token CHR_TOK       5
%token PUT_TOK       6
%token GET_TOK       7
%token INT_LIT_TOK   8
%token CHR_LIT_TOK   9
%token STR_LIT_TOK  10

%token IF_TOK       11
%token ELSE_TOK     12
%token WHILE_TOK    13

%token LT_TOK       14
%token LTE_TOK      15
%token GT_TOK       16
%token GTE_TOK      17
%token EQUAL_TOK    18
%token NEQUAL_TOK   19

%token INCR_TOK     20
%token DECR_TOK     21

// Semantics 2
%token SWITCH_TOK   22
%token CASE_TOK     23

// can't go past 31 without conflicting with single char tokens
// could use larger token numbers

%%

Module        : DeclImpls                                       { Finish(); };

DeclImpls     : Decl DeclImpls                                  { };
DeclImpls     : Impl DeclImpls                                  { };
DeclImpls     :                                                 { };

Decl          : DECL_TOK DeclList ':' Type  ';'                 { ProcDecls($2,$4); };
DeclList      : DeclItem ',' DeclList                           { $$ = AppendIdList($1,$3); };
DeclList      : DeclItem                                        { $$ = $1; };

DeclItem      : Id                                              { $$ = ProcName($1,PrimType); };
DeclItem      : Id FuncArgTypes                                 { $$ = ProcName($1,FuncType); };

Id            : IDENT_TOK                                       { $$ = strdup(yytext); };
FuncArgTypes  : '('  ')'                                        {  };

Type          : INT_TOK                                         { $$ = IntBaseType; };
Type          : CHR_TOK                                         { $$ = ChrBaseType; };

Impl          : IMPL_TOK Id FuncArgNames FuncBody ';'           { ProcFunc($2,$4); };
FuncArgNames  : '('  ')'                                        {  };
FuncBody      : '{' FuncStmts '}'                               { $$ = $2; };

FuncStmts     : Stmt ';' FuncStmts                              { $$ = AppendSeq($1,$3); };
FuncStmts     :                                                 { $$ = NULL; };

Stmt          : AssignStmt                                      { $$ = $1; };
Stmt          : PutStmt                                         { $$ = $1; };
Stmt          : IfStmt                                          { $$ = $1; };
Stmt          : IfElseStmt                                      { $$ = $1; };
Stmt          : WhileStmt                                       { $$ = $1; };
Stmt          : ForStmt                                         { $$ = $1; };
//Stmt          : SwitchStmt                                      { $$ = $1; };

WhileStmt     : WHILE_TOK '(' Cond ')' FuncBody                 { $$ = WhileFunction($3,$5);};

//SwitchStmt    : SWITCH_TOK '(' Expr ')' '{' CaseList '}'        { $$ = SwitchFunc($3, $5); };

CaseList      : Case CaseList                                   { $$ = CaseListFunc($1, $2); };
CaseList      :                                                 {};
Case          : CASE_TOK Expr ':' FuncBody                      { $$ = CaseFunc($2, $4); };

IfStmt        : IF_TOK '(' Cond ')' FuncBody                    { $$ = IfFunc($3,$5); };
IfElseStmt    : IF_TOK '(' Cond ')' FuncBody ELSE_TOK FuncBody  { $$ = IfElseFunc($3,$5,$7); };

Cond          : Expr BoolOps Expr                               { $$ = ConditionalOperation($1,$2,$3); };

BoolOps       : LT_TOK                                          { $$ = LessThan; };
BoolOps       : LTE_TOK                                         { $$ = LessEqual; };
BoolOps       : GT_TOK                                          { $$ = GreaterThan; };
BoolOps       : GTE_TOK                                         { $$ = GreaterEqual; };
BoolOps       : EQUAL_TOK                                       { $$ = Equal; };
BoolOps       : NEQUAL_TOK                                      { $$ = NotEqual; };

AssignStmt    : Id '=' Expr                                     { $$ = Assign($1,$3); };

Expr          : Expr AddOp Term                                 { $$ = AddOp($1, $2, $3); };
Expr          : Term                                            { $$ = $1; };

Term          : Term MultOp Factor                              { $$ = MultOp($1, $2, $3); };
Term          : Factor                                          { $$ = $1; };
Term          : Factor IncDecOp                                 { $$ = IncDecFunc($1, $2); };

ForStmt       : '(' AssignStmt ';' Cond ';' Expr ')' FuncBody   { $$ = ForLoop($2, $4, $6, $8); };

IncDecOp      : INCR_TOK                                        { $$ = Increment; };
IncDecOp      : DECR_TOK                                        { $$ = Decrement; };

Factor        : '(' Expr ')'                                    { $$ = $2; };
Factor        : '-' '(' Expr ')'                                { $$ = NegateExpr($3); };
Factor        : INT_LIT_TOK                                     { $$ = LoadInt(yytext); };
Factor        : CHR_LIT_TOK                                     { $$ = LoadChar(yytext); };
Factor        : Id                                              { $$ = LoadId($1); };
Factor        : GET_TOK '(' Type ')'                            { $$ = GetExpr($3); };

AddOp         : '+'                                             { $$ = Add; };
AddOp         : '-'                                             { $$ = Sub; };

MultOp        : '*'                                             { $$ = Mult; };
MultOp        : '/'                                             { $$ = Div; };

PutStmt       : PUT_TOK '(' Expr ')'                            { $$ = PutExpr($3); };
PutStmt       : PUT_TOK '(' String ')'                          { $$ = PutStr($3); };

String        : STR_LIT_TOK                                     { $$ = StrLiteral(yytext); };
%%

void
yyerror( char *s)     {
  char msg[MAXLINE];
  sprintf(msg,"ERROR \"%s\" token: \"%s\"",s,yytext);
  PostMessage(GetCurrentColumn(),strlen(yytext),msg);
}

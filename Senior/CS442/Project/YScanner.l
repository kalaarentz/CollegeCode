Letter	[A-Za-z]
Digit   [0-9]

%{
#include "IOMngr.h"
#define YY_INPUT(buf,result,max_size) \
  { int c = GetSourceChar(); \
		result = (c == EOF) ? YY_NULL : (buf[0] = c, 1); \
  }

bool shouldLog = false;

#define RETURN(val) \
  { if (shouldLog) fprintf(stderr,"Token#=%d, Length=%zu, Text=\"%s\"\n",val,yyleng,yytext); \
    return(val); \
  }

int commentLevel = 0;
int lineComment = 0;
  #define IN_COMMENT (lineComment || commentLevel > 0)

#include "YSemantics.h"
#include "y.tab.h"
%}

%%

\/\*														{ if(!lineComment) commentLevel++; }
\*\/														{ if(!lineComment) commentLevel--; }
\/\/                            { if(!IN_COMMENT) lineComment++; }

decl                            { if(!IN_COMMENT) RETURN(DECL_TOK); }
int                							{ if(!IN_COMMENT) RETURN(INT_TOK); }
chr                							{ if(!IN_COMMENT) RETURN(CHR_TOK); }
impl                            { if(!IN_COMMENT) RETURN(IMPL_TOK); }
'([^'\\\n]|\\.)'                { if(!IN_COMMENT) RETURN(CHR_LIT_TOK); }
";"															{ if(!IN_COMMENT) RETURN(';'); }
":"															{ if(!IN_COMMENT) RETURN(':'); }
"("															{ if(!IN_COMMENT) RETURN('('); }
"="                             { if(!IN_COMMENT) RETURN('='); }
\)															{ if(!IN_COMMENT) RETURN(')'); }
\-															{ if(!IN_COMMENT) RETURN('-'); }
\+															{ if(!IN_COMMENT) RETURN('+'); }
\*															{ if(!IN_COMMENT) RETURN('*'); }
\/															{ if(!IN_COMMENT) RETURN('/'); }
\{															{ if(!IN_COMMENT) RETURN('{'); }
\}															{ if(!IN_COMMENT) RETURN('}'); }
\,															{ if(!IN_COMMENT) RETURN(','); }
-?{Digit}+         							{ if(!IN_COMMENT) RETURN(INT_LIT_TOK); }
"put"                           { if(!IN_COMMENT) RETURN(PUT_TOK); }
"get"                           { if(!IN_COMMENT) RETURN(GET_TOK); }

"if"                            { if(!IN_COMMENT) RETURN(IF_TOK); }
"else"                          { if(!IN_COMMENT) RETURN(ELSE_TOK); }
"while"                         { if(!IN_COMMENT) RETURN(WHILE_TOK); }
"<"                             { if(!IN_COMMENT) RETURN(LT_TOK); }
"<="                            { if(!IN_COMMENT) RETURN(LTE_TOK); }
">"                             { if(!IN_COMMENT) RETURN(GT_TOK); }
">="                            { if(!IN_COMMENT) RETURN(GTE_TOK); }
"=="                            { if(!IN_COMMENT) RETURN(EQUAL_TOK); }
"##"                            { if(!IN_COMMENT) RETURN(NEQUAL_TOK); }

"++"                            { if(!IN_COMMENT) RETURN(INCR_TOK); }
"--"                            { if(!IN_COMMENT) RETURN(DECR_TOK); }
"switch"                        { if(!IN_COMMENT) RETURN(SWITCH_TOK); }
"case"                          { if(!IN_COMMENT) RETURN(CASE_TOK); }

{Letter}({Letter}|{Digit})*			{ if(!IN_COMMENT) RETURN(IDENT_TOK); }
\"(\\.|[^"\\])*\"               { if(!IN_COMMENT) RETURN(STR_LIT_TOK);}

[\n]                            { lineComment = 0; }
[ \t]                           { }
.                               { }
%%

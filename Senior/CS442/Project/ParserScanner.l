Letter	        [A-Za-z]
Digit           [0-9]
Start_Comment	"/*"
End_Comment		"*/"

%{
#include "y.tab.h"
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
  int inComment = 0;

#define IN_COMMENT (lineComment || commentLevel > 0)

%}

%%

  /* Combine comment nesting from Scanner.l with RDScanner.l regular
     expressions except for real literal. Use RETURN(token-symbolic-name)
     where token-symbolic-name appears in yacc file.
   */

\/\/.*							                        { }
\/\*							                        { inComment++; }
\*\/							                        { inComment--; }

int                							            { if( inComment == 0 ) RETURN(INT_TOK); }
chr                							            { if( inComment == 0 ) RETURN(CHR_TOK); }
\:\=													{ if( inComment == 0 ) RETURN(ASSIGN_TOK); }
\;														{ if( inComment == 0 ) RETURN(SEMI_TOK); }
\(                                                      { if( inComment == 0 ) RETURN(LPAREN_TOK); }
\)                                                      { if( inComment == 0 ) RETURN(RPAREN_TOK); }
\-														{ if( inComment == 0 ) RETURN(MINUS_TOK); }
\+														{ if( inComment == 0 ) RETURN(PLUS_TOK); }
\*														{ if( inComment == 0 ) RETURN(TIMES_TOK); }
\/														{ if( inComment == 0 ) RETURN(DIV_TOK); }
\{														{ if( inComment == 0 ) RETURN(LBRACE_TOK); }
\}														{ if( inComment == 0 ) RETURN(RBRACE_TOK); }
\,														{ if( inComment == 0 ) RETURN(COMMA_TOK); }
-?{Digit}+         							            { if( inComment == 0 ) RETURN(INTLIT_TOK); }
{Letter}({Letter}|{Digit})*			                    { if( inComment == 0 ) RETURN(IDENT_TOK); }
[ \t]                                                   { }
[\n]                                                    { }
.                                                       { }

%%

Letter	[A-Za-z]
Digit   [0-9]

%{
#include "ScanTokens.h"
#include "IOMngr.h"
#define YY_INPUT(buf,result,max_size) \
	{ int c = GetSourceChar(); \
		result = (c == EOF) ? YY_NULL : (buf[0] = c, 1); \
	}

	int inComment = 0;

bool shouldLog = false;

#define RETURN(val) \
  { if (shouldLog) fprintf(stderr,"Token#=%d, Length=%zu, Text=\"%s\"\n",val,yyleng,yytext); \
    return(val); \
  }

%}

%%

\@INIT                          { if (!inComment) RETURN(INIT_TOK); }
\@DUMP                          { if (!inComment) RETURN(DUMP_TOK); }
{Letter}({Letter}|{Digit})*		{ if (!inComment) RETURN(IDENT_TOK); }
-?{Digit}+						{ if (!inComment) return(INT_TOK); }
-?(({Digit}+)\.{Digit}+)        { if (!inComment) return(FLOAT_TOK); }
\=								{ if (!inComment) return(EQUAL_TOK); }
\/\/.*							{ }
\/\*							{ inComment++; }
\*\/							{ inComment--; }
[ \t]                           { }
[\n]                            { }
.                               { }
%%

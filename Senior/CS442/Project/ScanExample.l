%{
#include <ctype.h>
int h2d(char * str) {
  int ret = 0;
  for (int i = 2; i < strlen(str); i++) {
    if (isdigit(str[i])) ret = (ret * 16) + (str[i] - '0');
    if (isalpha(str[i])) ret = (ret * 16) + (str[i] - 'A' + 10);
  }
  return ret;
}

%}


NZDigit [1-9]
Digit   [0-9]




%%

{Digit}+                      { printf( "Number\n" ); }
0[xX][0-9a-fA-F]+             { printf( "Hex value: %s, Decimal conversion: %d\n", yytext, h2d( yytext ) ); }
[ \t\n]                       { }
.                             { }
%%
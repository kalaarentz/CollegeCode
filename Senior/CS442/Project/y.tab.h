/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     IDENT_TOK = 1,
     DECL_TOK = 2,
     IMPL_TOK = 3,
     INT_TOK = 4,
     CHR_TOK = 5,
     PUT_TOK = 6,
     GET_TOK = 7,
     INT_LIT_TOK = 8,
     CHR_LIT_TOK = 9,
     STR_LIT_TOK = 10,
     IF_TOK = 11,
     ELSE_TOK = 12,
     WHILE_TOK = 13,
     LT_TOK = 14,
     LTE_TOK = 15,
     GT_TOK = 16,
     GTE_TOK = 17,
     EQUAL_TOK = 18,
     NEQUAL_TOK = 19,
     INCR_TOK = 20,
     DECR_TOK = 21,
     SWITCH_TOK = 22,
     CASE_TOK = 23
   };
#endif
/* Tokens.  */
#define IDENT_TOK 1
#define DECL_TOK 2
#define IMPL_TOK 3
#define INT_TOK 4
#define CHR_TOK 5
#define PUT_TOK 6
#define GET_TOK 7
#define INT_LIT_TOK 8
#define CHR_LIT_TOK 9
#define STR_LIT_TOK 10
#define IF_TOK 11
#define ELSE_TOK 12
#define WHILE_TOK 13
#define LT_TOK 14
#define LTE_TOK 15
#define GT_TOK 16
#define GTE_TOK 17
#define EQUAL_TOK 18
#define NEQUAL_TOK 19
#define INCR_TOK 20
#define DECR_TOK 21
#define SWITCH_TOK 22
#define CASE_TOK 23




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
#line 12 "YGrammar.y"
{
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
/* Line 1529 of yacc.c.  */
#line 112 "y.tab.h"
	YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;


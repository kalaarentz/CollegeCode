-- Kala Arentz
-- DUE May 6th 8:30 morning

{-  -Each line of input should be taken as a separate, complete expression. You do not need to deal with the case of multi-line expressions.
    -For each expression, your program should print a series of lines, with one expression per line, corresponding to the series of lambda
    terms to which the original term reduces. The series should continue until it arrives at a term with no redex demanded under a
    call-by-name evaluation context.
    -Allow variables to be spelled with several lower-case letters.
    -You do not need to parse the shorthand notation for multiple arguments under one lambda.
    -Use a capital L for the lambda symbol.
    -When printing expressions, use the minimum parentheses possible – but do not omit any parentheses which are necessary to correctly represent a term under the standard scoping rules which we discussed in class. You should also omit spaces except where necessary, namely between consecutive names.
-}
module Lmbeval where

import MonadicParser
import Control.Monad

data Term  = Application Term Term | Abstraction String Term | Variable String

 {- First three methods are for determining the form of the term.
    printHelper is the second example that was given on the website.
 -}
isApplication :: Term -> Bool
isApplication (Application _ _ ) = True
isApplication _ = False

isAbstraction :: Term -> Bool
isAbstraction (Abstraction _ _ ) = True
isAbstraction _ = False

isVariable :: Term -> Bool
isVariable (Variable _ ) = True
isVariable _ = False

printHelper :: Term -> String
printHelper (Variable v) = v
printHelper (Abstraction var body) =  "L" ++ var ++ "." ++ printHelper body
printHelper (Application expr1 expr2) =
  let str1 = printHelper expr1
      str2 = printHelper expr2
      final2 = if (isApplication expr2)
               then "(" ++ str2 ++ ")"
               else str2
    in str1 ++ final2

{- Have two functions that have the IO (). The first thing that is printed
   when you start the program is "Welcome" and then a prompt on how to exit.

   Then the main method will continue to go through the loop function,
   which is also a IO () type. It will continue to print out "Enter Lambda..."
   and print out the reduction if the expression, or getLine does not
   equal "/quit"
 -}
main :: IO ()
main = do
    putStrLn "Welcome! To exit: Type /quit"
    loop

loop :: IO ()
loop = do
    putStrLn "Enter Lambda Calculus Expression: "
    expression <- getLine
    if expression == "/quit"
      then putStrLn "Bye"
      else do
        putStrLn expression
        loop

-- PARSING ---
{-  Did not figure this out, but here is my thought process.

Overall, this would be need to be a loop; you would need to parse
each individual type of term, if it is one string excluding "L" than it is
a Variable.

If a char is a  "(" means it is a start of a Term
(Variable, Abstraction, Application).

If the paraentheses is followed by a L than it is going to be a Abstraction.
Than you would parse through until you hit a period. This would qualify for the
string part of the Abstraction, anything that follows until a ")" or end of String
would qualify as body.
    If the body of Abstraction also includes than you would need to parse this
    as well.

    parseStr :: String -> Parser -> Char -> [Char]
    parseStr expr = parse parseForParenthesis expr

    parseForParenthesis :: Parser Char -> [Char]
    parseForParenthesis = satisify '('

  -}

-- REDUCTION

-- method that prints the reduceTerm function
printReduction :: Term -> String
printReduction var = printHelper (reduceTerm var)

-- implements subsitition of labmdba terms
-- For example it is the representation of e[ex/x], the result of contracting the redex (λx.e) ex.
subst :: Term -> Term -> Term
subst expr1 expr2
  | (isVariable expr1 ) = (Application expr2 expr1)
  | otherwise = (Application expr1 expr2)

{- Helper method that is used the reduceTerm method.
  If Variable or Abstraction, it returns those terms,
  the real meat is in the herlper method that takes in the Application
    If the term1 or t1 is a Abstraction, than you will make a call the helper
    method on a the subsitution (subst) of t2, and than the Abstraction that
    came from helper t1

    If t1 is not a abstraction, than return the Application
 -}
helper :: Term -> Term
helper (Variable v) = Variable v
helper (Abstraction var body ) = Abstraction var body
helper (Application t1 t2 ) = case helper t1 of
                                      Abstraction var body -> helper( subst t2 (Abstraction var body ) )
                                      t1'                   -> Application t1' t2

{- Main method that is used to reduceTerm (this assumes that the strings are parsed
   into the correct type of Lambda Calculus Term).

   If Variable, return it.

   If Abstraction, than you will return the Abstration with the var,
   but you will be trying to reduce the body because it may be a
   term than needs to be reduced (Abstraction or Application).

   If Application, it will need to go throught the helper method
   for the first term, if is is an abstraction it will be later be
   redcuced on the subst of term 2 and the Abrstraction of term 1,
   else it is will reduce t1 in the Application.

-}
reduceTerm :: Term -> Term
reduceTerm (Variable x ) = Variable x
reduceTerm (Abstraction var body ) = Abstraction var (reduceTerm body)
reduceTerm (Application t1 t2 ) = case helper t1 of
                                   Abstraction var body -> reduceTerm ( subst t2 (Abstraction var body))
                                   t1                -> let t1'' = reduceTerm t1 in Application t1'' (reduceTerm t2)

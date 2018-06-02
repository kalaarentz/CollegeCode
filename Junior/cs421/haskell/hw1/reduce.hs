
data Term  = Application Term Term | Abstraction String Term | Variable String deriving (Eq )

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

-- REDUCTION ---
-- E::= []|EM
-- reduceTerm :: Term -> Term -> Term
-- reduceTerm (Variable str) _ = Variable str
-- reduceTerm (Abstraction var body) _ = (Abstraction var body)
-- reduceTerm (Application t1 t2) _ = if (isApplication t1) then Variable "it worked" else Variable "it did not work"

getName :: Term -> String
getName (Variable n ) = n
getName (Abstraction n _ ) = n

getTerm :: Term -> Term
getTerm (Variable str) = (Variable str)
getTerm (Application t1 t2) = t1
getTerm (Abstraction n t1 ) = t1

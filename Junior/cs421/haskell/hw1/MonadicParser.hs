
module MonadicParser where

-- Basic type of a parser
newtype Parser input output = Parser ([input] -> [(output,[input])])
parse (Parser p) = p

-- Top level operation to apply a parser to input, expecting to find
-- one single parse
getParse :: Parser i o -> [i] -> o
getParse parser input =
  case parse parser input of
    [(result,[])] -> result
    [] -> error "No parse"
    [(result,_)] -> error "Input not consumed"
    _ -> error "Parse is not unique"

bestParse :: Parser i o -> [i] -> o
bestParse parser input =
  case parse parser input of
    [] -> error "No parse"
    ((result,[]):_) -> result
    ((result,_):_) -> error "Input not consumed"

accept :: result -> Parser input result
accept res = Parser (\inp -> [(res, inp)])

reject :: Parser input result
reject = Parser (\inp -> [])

satisfy :: (a -> Bool) -> Parser a a
satisfy f = Parser (\inp -> case inp of
                       (x:xs) | f x -> [(x,xs)]
                       _ -> [])

literal :: Eq a => a -> Parser a a
literal s = satisfy (== s)

infixl 6 `using`
using :: Parser inp res -> (res -> res') -> Parser inp res'
p `using` f = Parser (\input -> [(f res, inp') | (res,inp') <- parse p input])

infixl 6 `pairing`
pairing :: Parser inp (res1, res2) -> (res1 -> res2 -> res') -> Parser inp res'
p `pairing` f = p `using` uncurry f

infixr 5 `thn`
thn :: Parser inp res1 -> (res1 -> Parser inp res2) -> Parser inp res2
p1 `thn` f = Parser (\input -> [ (res2, input'') |
                                 (res1,input') <- parse p1 input,
                                 (res2,input'') <- parse (f res1) input' ])

infixl 4 `alt`
alt :: Parser inp res -> Parser inp res -> Parser inp res
p1 `alt` p2 = Parser (\input -> parse p1 input ++ parse p2 input)

many :: Parser inp res -> Parser inp [res]
many p = (p `thn` \first ->
          many p `thn` \rest ->
          accept (first : rest))
         `alt` accept []

some :: Parser inp res -> Parser inp [res]
some p = p `thn` \first ->
         many p `thn` \rest ->
         accept (first : rest)

cut :: Parser inp res -> Parser inp res
cut (Parser f) = Parser (\inp -> case f inp of
                                 [] -> []
                                 (x:_) -> [x])

instance Functor (Parser i) where
  fmap f parser =
    Parser (\inp -> [ (f z, xs) | (z, xs) <- (parse parser) inp])

instance Applicative (Parser i) where
  pure = accept
  p1 <*> p2 = Parser (\inp -> [ (f a, inp'') |
                                (f,inp') <- (parse p1) inp,
                                (a,inp'') <- (parse p2) inp'])

instance Monad (Parser a) where
  return = accept
  p >>= f = p `thn` f

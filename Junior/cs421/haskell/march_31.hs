--Kala Arentz
-- exercises for class --> 47, 48, 49

import MonadicParser
import Data.Char

data MyComplex = MyComplex Double Double
instance Show MyComplex where
    show (MyComplex x iy) = case compare iy 0 of
                             GT -> show x ++ "+" ++ show iy ++ "i"
                             EQ -> show x
                             LT -> show x ++ "-" ++ show (-iy) ++ "i"
{-|
instance Num MyComplex where
    (MyComplex x iy) + (MyComplex x` iy`) = MyComplex (x + x`) (iy + iy`)
    (MyComplex x iy) - (MyComplex x` iy`) = MyComplex (x - x`) (iy - iy`)
    (MyComplex x iy) * (MyComplex x` iy`) = MyComplex (x*x` - iy*iy`)
                                                      (x*iy` + x*iy`)


abs (MyComplex x iy) = MyComplex (sqrt (x*x + iy*iy)) 0
signum (num@(MyComplex x iy)) = let (MyComplex a _) = abs num
                                in MyComplex (x/a) (iy/a)

fromInteger n = MyComplex (fromInteger n) 0.0

-}

--47
parsePeriod :: Parser Char Char
parsePeriod = literal '.'

--48
parseQmark :: Parser Char Char
parseQmark = literal '?'

parsePeriodQmark :: Parser Char Char
parsePeriodQmark = parsePeriod `alt` parseQmark

--49
parseDigits :: Parser Char Char
parseDigits = satisfy isDigit

parseIntegerChars :: Parser Char [Char]
parseIntegerChars = some parseDigits

--50
parseDigitsReturnInt res = parseIntegerChars `thn` \inp -> accept (read (res ++ inp) :: Int)

parseInteger :: Parser Char Int
parseInteger = parseIntegerChars `thn` parseDigitsReturnInt

--51
parseEven :: Parser Int Int
parseEven = satisfy even

threeEven :: Parser Int (Int, Int, Int)
threeEven =  parseEven `thn` \x1 -> parseEven `thn` \x2 -> parseEven `thn` \x3 -> accept (x1,x2,x3)

-- 58
-- Change the declaration of instance Show MyComplex so that the real coefficient is not shown
-- when it is zero and the complex coefficient is non-zero.

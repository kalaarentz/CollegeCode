-- Kala Arentz
-- 50,51,58
-- April 2

import MonadicParser
import Data.Char

--data MyComplex = MyComplex Double Double

-- 49
-- used in exercise 50
parseDigits :: Parser Char Char
parseDigits = satisfy isDigit

parseIntegerChars :: Parser Char [Char]
parseIntegerChars = some parseDigits

-- original 50, which does not include parsing two things
{-  answer if it is accept( read inp :: Int) [(0," z"),(0," z"),(0,"0 z")]
    answer if it is accept( read res :: Int) [(50," z"),(5," z"),(5,"0 z")]

wrongDigitsReturnInt res = parseIntegerChars `thn` \inp -> accept( read res :: Int)

wrongParseInteger :: Parser Char Int
wrongParseInteger = parseIntegerChars `thn` wrongDigitsReturnInt

** should not have used parseIntegerChars twice
-}

--50
parseDigitsReturnInt res = parseIntegerChars `thn` \inp -> accept (read (res ++ inp) :: Int)

parseInteger :: Parser Char Int
parseInteger = parseIntegerChars `thn` \inp -> accept ( read inp :: Int)

--51
parseEven :: Parser Int Int
parseEven = satisfy even

threeEven :: Parser Int (Int, Int, Int)
threeEven =  parseEven `thn` \x1 -> parseEven `thn` \x2 -> parseEven `thn` \x3 -> accept (x1,x2,x3)

-- 58
-- Change the declaration of instance Show MyComplex so that the real coefficient is not shown
-- when it is zero and the complex coefficient is non-zero.

{--instance Show MyComplex where
show (MyComplex x iy) | x == 0 && iy == 1 = "i"
                      | x == 0 = show iy ++ "i"
                      | iy == 0 = show x
                      | iy > 0 = show x ++ "+" ++ show iy ++ "i"
                      | iy < 0 = show x ++ "-" ++ show (-iy) ++ "i"
-}

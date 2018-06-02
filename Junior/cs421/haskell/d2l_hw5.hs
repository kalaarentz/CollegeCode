-- Kala Arentz
-- Due Monday March 27

-- 43. list comprehension notation to take a list of integers and make argument divisible by three
dividesByThree :: [Int] -> [Int]
dividesByThree xs = [ x | x <- xs, x `mod` 3 == 0]

-- 44. use list comprehension notation--> list of strings, and return which start with captital vowel
capVowelsFirst :: [String] -> [String]
capVowelsFirst xs = [ x | x <- xs, isCapVowel (head x) ]

isCapVowel :: Char -> Bool
isCapVowel x = x `elem` ['A', 'E', 'I', 'O', 'U']

-- 45. recurisve function, use byTens example to define a list of corresponding Fibonacci numbers
genFibs :: Int -> Int -> [Int]
genFibs n1 n2 = n1 : n2 : genFibs (n1 + n2) (n1 + n2 + n2)

--46. define the list of fibonacci numbers
fibs :: [Int]
fibs = genFibs 0 1

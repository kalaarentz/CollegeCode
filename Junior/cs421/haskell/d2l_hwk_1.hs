-- Kala Arentz
-- January 26, 2017

-- sort with conditionals
sort2 :: Ord a => a -> a -> (a,a)
sort2 x y = if x > y then (y,x) else (x,y)

-- sort with guards
sortGuard2 :: Ord a => a -> a -> (a,a)
sortGuard2 x y | x > y = (y,x)
               | otherwise = (x,y)

-- isLowerCase Char
isLower :: Char -> Bool
isLower ch = if 'a' <= ch then True else False

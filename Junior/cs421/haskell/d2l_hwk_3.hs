-- Kala Arentz
-- HW3 February 13 25, 27

-- 25 return true if there is an odd number of true values in list
xor :: [Bool] -> Bool
xor list = if odd (count list) then True else False

count :: [Bool] -> Int
count = foldl (\i v -> case v of { True -> i + 1 ; False -> i }) 0

--27 remove vowels from a string
removeVowels :: String -> String
removeVowels str = filter v str where v x = not (x == 'a' ||
                                                 x == 'e' ||
                                                 x == 'i' ||
                                                 x == 'o' ||
                                                 x == 'u' )

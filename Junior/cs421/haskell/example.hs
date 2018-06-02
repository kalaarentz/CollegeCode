--- Excercise 10 --> Presentation January 30, 2017

mangle3 :: String -> String
mangle3 str = if length str > 0 then tail str ++ [head str] else ""

mangle4 :: String -> String
mangle4 str | length str > 0 = tail str ++ [head str]
            | otherwise = ""

-- practice Feb 6th
applyTwice :: (a -> a) -> a -> a
applyTwice f x = addTwo (f x)

addTwo :: Integer -> Integer
addTwo n = n + 2

subtractFive :: Integer -> Integer
subtractFive n = n subtract 5

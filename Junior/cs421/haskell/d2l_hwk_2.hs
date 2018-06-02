-- Kala Arentz
-- Feburary 2
-- Exercise 14c, 16, 18, 20, 23

-- 14c
third :: [a] -> a
third (x1:x2:x3:xs) = x3

-- 16
lastButOne :: [a] -> a
lastButOne list = list !! (length list - 2)

-- 18
removeOdd :: [Int] -> [Int]
removeOdd [] = []
removeOdd (x:xs)
    | odd x = removeOdd xs
    | otherwise = x:removeOdd xs

-- 20
noNeighborDups :: [Int] -> [Int]
noNeighborDups [] = []
noNeighborDups [x] = [x]
noNeighborDups (x1:x2:xs)
    | x1==x2 = noNeighborDups (x2:xs)
    | otherwise = x1:noNeighborDups (x2:xs)

-- 23
splitListAt :: Int -> [a] -> ( [a],[a] )
splitListAt n list = splitAt n list

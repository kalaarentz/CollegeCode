worker :: [String] -> [String] -> String -> [a]
worker (x:xs) (n:ns) = x ++ (read n:String) : worker xs ns
worker _ _ = []

cross strings nums = worker (strings) ("0":nums)

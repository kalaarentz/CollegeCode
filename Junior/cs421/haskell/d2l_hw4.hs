-- Kala Arentz
-- 2/26/17

-- Examples -> helped me try to make a step by step of both
--foldr :: (a -> b -> b) -> b -> [a] -> b
--foldr  f z []     =  z
--foldr  f z (x:xs) =  f x (foldr f z xs)

--foldl  f z []     =  z
--foldl  f z (x:xs) =  foldl f (f z x) xs

--fold :: (a -> a -> a) -> (Int -> a) -> IntTree -> a
--fold f g (Branch l r) = f (fold f g l) (fold f g r )
--fold _ g (Leaf i) = g i

-- Original submission
--intTreeFoldl _ z (Leaf i) = z i
--intTreeFoldl f z (Branch l r) = intTreeFoldl f (f z l) r

--intTreeFoldr _ z (Leaf i) = z i
--intTreeFoldr f z (Branch l r) = f l (intTreeFoldr f z r)

-- New submission
-- 37
data IntTree = Branch (IntTree) (IntTree) | Leaf Int deriving (Show)

intTreeFoldl :: (t -> Int -> t) -> t -> IntTree -> t
intTreeFoldl f z (Leaf i) = f z i
intTreeFoldl f z (Branch l r) = (intTreeFoldl f (intTreeFoldl f z l) r)

intTreeFoldr :: (Int -> t -> t) -> t -> IntTree -> t
intTreeFoldr f z (Leaf i) = f i z
intTreeFoldr f z (Branch l r) = (intTreeFoldr f (intTreeFoldr f z l) r)

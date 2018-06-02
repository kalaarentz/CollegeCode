-- Haskell Assignment 4
-- Suzanna Schlottach-Ratcliff, Feb 2017
--
-- Haskell exercises 37

module HaskellExercises4
where
import Prelude hiding ()

data IntTree = Branch IntTree IntTree | Leaf Int deriving (Show, Eq)

intTreeFoldr :: (Int -> t -> t) -> t -> IntTree -> t
intTreeFoldr f x (Leaf a)     = f a x
intTreeFoldr f x (Branch l r) = (intTreeFoldr f (intTreeFoldr f x r ) l)

intTreeFoldl :: (t -> Int -> t) -> t -> IntTree -> t
intTreeFoldl f x (Leaf a)     = f x a
intTreeFoldl f x (Branch l r) =  (intTreeFoldl f (intTreeFoldl f x l ) r)

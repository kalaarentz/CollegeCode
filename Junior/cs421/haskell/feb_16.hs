--excercises due for class

-- 34

data Day = Sunday | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday deriving (Eq, Ord, Show, Read, Bounded, Enum)

nextDay :: Day -> Day
nextDay Sunday = Monday
nextDay Monday = Tuesday
nextDay Tuesday = Wednesday
nextDay Wednesday = Thursday
nextDay Thursday = Friday
nextDay Friday = Saturday
nextDay Saturday = Sunday

-- 35

data Month = January | February | March | April | May | June | July | August | September | October | November | December deriving (Eq, Ord, Show, Read, Bounded, Enum)

data Season = Spring | Fall | Summer | Winter deriving (Show)

monthSeason :: Month -> Season
monthSeason m | m >= December || m < March = Winter
              | m > March || m < June = Spring
              | m >= June || m <= August = Summer
              | m > December || m <= September = Fall


-- 36

data Shape = Circle Float | Rectangle Float Float | Triangle Float Float Float

-- a.)
area :: Shape -> Float
area (Circle r) = pi * r^2
area (Rectangle w h ) = w * h
area (Triangle s1 s2 s3) = sqrt val where val = h * (h - s1) * (h -s2) * (h -s3) where h = (s1 + s2 +s3)/2


perimeter :: Shape -> Float
perimeter (Circle r) = 2 * pi * r
perimeter (Rectangle w h) = (2 * w) + (2 * h)
perimeter (Triangle s1 s2 s3) = s1 + s2 + s3


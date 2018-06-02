#include "colors.inc"

background {color Black}

camera {
  location <45,0,10>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,0>
  angle 45
}

light_source { <15,20,15> color White }

plane { <0,0,1>, -4
  texture { 
    pigment {color rgb <.3,.3,.9> }
    finish { ambient .1 diffuse 0.9 phong 1.0 reflection .2}
  }
}

//difference {
  box { <-35,-18,-5> <-24,18,7>
    texture {
      pigment {color White}
      normal {bumps 0.01 scale 0.01}
      finish {ambient 0 diffuse 0 phong .5 reflection 1 }
    }
  }
//  cylinder { <-18,0,-5>, <-18,0,7>, 10
//    texture {
//      pigment{color Red}
//      finish {ambient 0 diffuse 0 phong .5 reflection 1 }
//    }
//  }
//}

sphere { <0,-9,0>, 2
  texture {
    pigment {color rgb <.2,0,0>}
    finish {ambient .2 }
  }
}

sphere { <0,-3,0>, 2
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 }
  }
}

sphere { <0,3,0>, 2
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 phong 1 }
  }
}

sphere { <0,9,0>, 2
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 phong 1 reflection 1 }
  }
}


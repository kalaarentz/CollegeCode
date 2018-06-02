#include "colors.inc"
#include "glass.inc"
#include "textures.inc"

background {color Black}

camera {
  location <90,0,45>
//  location <45,25,25>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,0>
  angle 45
  rotate <0,0,clock*180>
}

light_source { <15,20,15> color White }

plane { <0,0,1>, -4
  texture {
    pigment {color rgb <.3,.3,.9> }
    finish { ambient .1 diffuse 0.9 phong 1.0 reflection .2}
  }
}

/*
difference {
sphere { <0,0,0>, 4
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 phong 1}
  }
}
cylinder { <0,-5,0> <0,5,0> 1.5
  texture {
    pigment {color rgb <.5,.5,.2>}
    finish {ambient .2 diffuse .9 phong 1}
  }
}
cylinder { <0,0,-5> <0,0,5> 2
  texture {
    pigment {color rgb <.5,.5,.2>}
    finish {ambient .2 diffuse .9 phong 1}
  }
}
torus { 5 1.5
  texture {
    pigment {color rgb <0,.5,.5>}
    finish {ambient .2 diffuse .9 phong 1}
  }
    rotate <90,0,0>
}
}
*/

difference {
  sphere { <0,0,0>, 4
    texture {
      pigment {color rgb <.5,0,0>}
      finish {ambient .2 diffuse .9 phong 1 }
    }
  }
  union {
    sphere { <0,0,0> 2
      texture {
        pigment { color rgb <.1,.7,.2> }
        finish { ambient .1 diffuse .9 phong 1 }
      }
    }
    cylinder { <-4,0,0>, <4,0,0>, 1.5
      texture {
        pigment { color rgb <.5,.5,0> }
        finish { ambient .1 diffuse .9 phong 1 }
      }
    }
    cylinder { <0,0,-4>, <0,0,4>, 2.5
      texture {
        pigment { color rgb <.5,.5,0> }
        finish { ambient .1 diffuse .9 phong 1 }
      }
    }
  }
}

torus { 11, .5
  texture {
    pigment { color rgb <.4,0,.6> }
    finish { ambient .1 diffuse .9 phong 1}
  }
  rotate <0,0,90>
  rotate <0,-30,0>
  translate <19,0,10.5>
}

intersection {
  sphere { <10,0,5> 15
  }
  sphere { <28,0,16> 15
  }
//  texture { pigment {color Red} finish { ambient .1 diffuse .9 phong 1}}
  texture { Glass}
  interior { ior 1.15 caustics 1}
}

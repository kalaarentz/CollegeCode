#include "colors.inc"

background {color Black}

camera {
  location <25,8,7>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,3,0>
  focal_point <14*clock,0,0>
  aperture 2
  blur_samples 20
}

light_source { <15,20,15> color White}

plane { <0,0,1>, -4
  texture {
    pigment {color rgb <.1,.1,.3> }
    finish { ambient .1 diffuse 0.9 phong 1.0 reflection .5}
  }
}

sphere { <0,0,0>, 2
  texture {
    pigment {color rgb <.2,.2,.2>}
    finish {ambient .1 diffuse .9 phong 1.0}
  }
}

cylinder { <0,0,0>, <10,0,0>, .5
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

cone { <10,0,0>, 1, <14,0,0>, 0
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

cylinder { <0,0,0>, <0,10,0>, .5
  texture {
    pigment {color rgb <0,.5,0>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

cone { <0,10,0>, 1, <0,14,0>, 0
  texture {
    pigment {color rgb <0,.5,0>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

cylinder { <0,0,0>, <0,0,10>, .5
  texture {
    pigment {color rgb <0,0,.5>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

cone { <0,0,10>, 1 <0,0,14>, 0
  texture {
    pigment {color rgb <0,0,.5>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
}

torus { 4, 1
  texture {
    pigment {color rgb <.65,.65,0>}
    finish {ambient .2 diffuse .9 phong 1.0}
  }
  rotate <90,0,0>
  rotate <-20,20,0>
  translate <6,6,-2>
}

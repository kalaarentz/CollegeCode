#include "colors.inc"

background {color Black}

global_settings {
  photons {
    count 20000
    autostop 0
    jitter 0.4
  }
}

camera {
  location <35,clock*10,4>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,clock*5,0>
  angle 45
}

light_source { <15,20,15> color White }

plane { <0,0,1>, -4
  photons {
    target
    reflection on
    refraction on
  }
  texture { 
    pigment {color rgb <.3,.3,.9> }
    finish { ambient 0 diffuse 0.9 phong 1.0 reflection .2}
  }
}

box { <-35,-18,-3> <-24,18,7>
  photons {
    target
    reflection on
    refraction on
  }
  texture {
    pigment {color White}
    finish {ambient 0 diffuse 0 phong 0.5 reflection 1 }
  }
}

sphere { <0,-9,0>, 2
  photons {
    target
    reflection on
    refraction on
  }
  texture {
    pigment {color rgb <.2,0,0>}
    finish {ambient 0 }
  }
}

sphere { <0,-3,0>, 2
  photons {
    target
    reflection on
    refraction on
  }
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient 0 diffuse .9 }
  }
}

sphere { <0,3,0>, 2
  photons {
    target
    reflection on
    refraction on
  }
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient 0 diffuse .9 phong 1 }
  }
}

sphere { <0,9,0>, 2
  photons {
    target
    reflection on
    refraction on
  }
  texture {
    pigment {color rgb <.5,0,0>}
    finish {ambient 0 diffuse .9 phong 1 reflection 1 }
  }
}

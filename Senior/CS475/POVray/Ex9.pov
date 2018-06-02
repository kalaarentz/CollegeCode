#include "colors.inc"
//#include "TreeMesh.pov"
#include "rad_def.inc"

background {color White}

 global_settings {
    radiosity {
      pretrace_start 0.04
      pretrace_end   0.01
      count 400 // 4000

      nearest_count 10
      error_bound 0.5 // 0.1
      recursion_limit 3

      low_error_factor 0.5
      gray_threshold 0.0
      minimum_reuse 0.015
      brightness 0.3
      //always_sample off

      adc_bailout 0.01/2
    }
  }

//#declare posX = function(u) { 15 * cos(u*2*pi) }
//#declare posY = function(u) { 15 * sin(u*2*pi) }
//#declare posZ = function(u) { 15 * cos(u*pi) }

#declare pos = function {
  spline {
    natural_spline
    -0.1 <1,10,10>
    0.0 <0,10,10>
    0.2 <-10,10,7>
    0.4 <-10,0,4>
    0.9 <-4,-4,2>
    1.0 <-4,-2,2>
    1.1 <-4,-2,2>
  }
}

camera {
  location <-14,-20,20>
  //location <-4,-2,2>
  //location pos(clock)
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,-3,3>
  angle 90
}

#declare Finish = finish {ambient 0 diffuse 0.75 }


sphere_sweep {
  b_spline
  11
  #for (i,0,1,0.1)
    pos(i) 0.05
  #end
  texture {
    pigment {color rgb <0,0,0.7> }
    finish { ambient 0 diffuse 0.9 phong 0.5}
  }
}


light_source { <-100,150,150> color White }

plane { z 0
  texture {
    pigment {color rgb <.7,.7,.7> }
    finish { Finish }
  }
}

#declare Arch =
union {
  box { <-1.1,-0.2,0> <1.1,0.2,3> }
  cylinder { <0,-0.2,3> <0,0.2,3> 0.75}
}

difference {
  box { <-5,-0.1,0>, <5,0.1,4> }
  #for (i, -4, 4, 1)
    #for (j, 1, 3, 1)
      sphere { <i,0,j> 0.4}
    #end
  #end
  texture {
    pigment {color White}
    finish { Finish }
  }
}

difference {
  box { <-4, -0.1,0> <4,0.1,4> }
  #for (i,-2.5,2.5,2.5)
  object {
    Arch
    translate <i,0,0>
  }
  #end
  texture {
    pigment {color White}
    finish { Finish }
  }
  rotate <0,0,90>
  translate <-5,-4,0>
}

box { <-5,-0.1,0> <5,0.1,4>
  texture {
    pigment {color White}
    finish { Finish }
  }
  translate <0,-8,0>
}

box { <-4,-0.1,0> <4,0.1,4>
  texture {
    pigment {color White}
    finish { Finish }
  }
  rotate <0,0,90>
  translate <5,-4,0>
}

difference {
  box { <-5,-4,0> <5,4,0.2>
    translate <0,-4,4>
  }
  sphere { <0,-4,4> 3 }
  texture {
    pigment {color White}
    finish { Finish }
  }
}

difference {
  sphere { <0,0,0> 3.1 }
  sphere { <0,0,0> 3 }
  box { <-3.5,-3.5,-3.5> <3.5,3.5,0> }
  intersection {
    sphere { <0,0,3> 3 }
    union {
      #for (i,-6,6,1)
        #for (j -6,6,1)
          box { <-0.2,-0.2,-4> <0.2,0.2,4>
            translate <i/2.0,j/2.0,0>
          }
        #end
      #end
    }
  }
  texture {
    pigment {color White}
    finish { Finish }
  }
  translate <0,-4,4>
}

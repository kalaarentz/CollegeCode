/*
Author: Kala Arentz
Created: December 7, 2017
Resources: http://www.povray.org/documentation/view/3.6.2/425/
           http://www.imagico.de/pov/water/index.php (used the whole tutorial)
           http://www.f-lohmueller.de/pov_tut/tex/tex_755e.htm
           http://xahlee.info/3d/povray-glassy.html
*/

#include "colors.inc"
#include "rad_def.inc"
#include "functions.inc"
#include "textures.inc"
#include "glass.inc"

background {color White}

 global_settings {
    photons {
      count 20000
      spacing 0.005
    }
    radiosity {
      pretrace_start 0.04
      pretrace_end   0.01
      count 400

      nearest_count 10
      error_bound 0.5
      recursion_limit 3

      low_error_factor 0.5
      gray_threshold 0.0
      minimum_reuse 0.015
      brightness 0.3

      adc_bailout 0.01/2
    }
  }

#declare posX = function(u) { 15 * cos(u*2*pi) }
#declare posY = function(u) { 15 * sin(u*2*pi) }
#declare posZ = function(u) { 15 * cos(u*pi) }

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
  //location <-14,-20,20>
  //location <-1,-2,2>
  //location <-10,-15,10>
  location pos(clock)
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,-3,3>
  angle 90
}

#declare Finish = finish { ambient 0 diffuse 0.75 }

/*
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
*/


light_source { <150 * cos(clock*pi),0,150 * sin(clock*pi)> color White
  rotate <-45,0,0>
}

plane { z 0
  texture {
    pigment{ color SteelBlue}
    finish { Finish }
  }
}

plane { z .40
  texture {
    pigment {
      color rgbt <1, 1, 1, 1>
    }
    finish {
      ambient 0.0
      diffuse 0.0

      reflection {
        0.0, 1.0
        fresnel on
      }

      specular 0.8
      roughness 0.003
    }
    normal {
      function {
        f_ridged_mf(x, y, z, 0.1, 3.0, 7, 0.7, 0.7, 2)
      } 0.8
      scale 0.13
    }
  }
  interior {
    ior 1.3
  }
  photons{
    target
    reflection on
    refraction on
  }
}


#declare Arch =
union {
  box { <-1.1,-0.2,0> <1.1,0.2,3> }
  cylinder { <0,-0.2,3> <0,0.2,3> 0.75}
}

// marble floors
box { <5,0 ,-1> <-6,-8,.5>
  texture {
    pigment { agate scale 1  rotate <0,0,0>
         color_map{ [0.0 color rgb <1,1,1>]
                    [0.5 color rgb <0,0,0>]
                    [1.0 color rgb <1,1,1>]
                  }// end of color_map
          }
    finish {Finish}
  }
}

box { <5.75,.5,-1> <-7,-8.5,.2>
  texture {
    pigment { agate scale 1  rotate <0,0,0>
       color_map{ [0.0 color rgb <1,1,1>]
                  [0.5 color rgb <0,0,0>]
                  [1.0 color rgb <1,1,1>]
                }// end of color_map
        }
    finish {Finish}
  }
}

// wall to left
difference {
  box { <-5,-0.1,0>, <5,0.1,4> }

  #for (i, -3, 3, 3 )
    sphere { <i, 0, 3>, .75 }
  #end

  box { <2,-.2,1> <4,.2,3>}
  box { <-1, -.2, 1> <1, .2, 3> }
  box { <-4, -.2, 1> <-2, .2, 3> }

  texture {
    pigment {color White}
    finish { Finish }
  }
}


//front view
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

// right
difference {
  box { <-5,-0.1,0> <5,0.1,4>}

  #for (i, -3, 3, 3 )
    sphere { <i, 0, 3>, .75 }
  #end

  box {<2,-.2,1> <4 ,.2, 3>}
  box {<-1,-.2,1> <1 ,.2, 3>}
  box {<-4,-.2,1> <-2 ,.2, 3>}

  texture {
    pigment {color White}
    finish { Finish }
  }
  translate <0,-8,0>
}

// back wall
difference {
  box { <-4,-0.1,0> <4,0.1,4>}

  sphere { <0,0,3>, .75}
  box { <-1,-.2,1> <1,.2,3>}
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

// glass sphere
sphere {
  <0,-3.75,1.2> .8
  texture{
    pigment {color Red transmit .9 }
      finish{phong .8}
  }
  interior {ior 2.5}

}

// base of the prism
box {
  <1,-2.5,.5> <-1,-5,.75>
  texture {
    pigment{ marble scale 0.5 turbulence 1.15 }
    finish {Finish}
  }
}

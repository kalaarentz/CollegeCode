
 /* #include "colors.inc"
 #include "textures.inc"
  background{White}
  camera {
    angle 20
    location <2, 10, -30>
    look_at <0, 1, 0>
  }
  light_source { <20, 20, -20> color White }
  prism {
   conic_sweep
   linear_spline
   0, // height 1
   1, // height 2
   5, // the number of points making up the shape...
   <4,4>,<-4,4>,<-4,-4>,<4,-4>,<4,4>
   rotate <180, 0, 0>
   translate <0, 1, 0>
   scale <1, 4, 1>
   texture {
    pigment {color Yellow filter 1 }
    finish {phong .8}
  }
  interior {ior 1.0}
 }

 ---------------------------------------
 // http://xahlee.org/3d/index.html

// POV-Ray texture study

#include "colors.inc"

  light_source {
    100
    color White
  }

camera {
  location <5,6,-7>
  look_at  <0,0,0>
	translate -.8
}

//ior 1.0
//caustics 0.0
//dispersion 1.0
//dispersion_samples 7
//fade_distance 0.0
//fade_power 0.0
//fade_color <0,0,0>

sphere {<-4,1,4>, 1 texture{pigment{color Gray} finish {phong .7 }}}
sphere {<-2,1,4>, 1 texture{pigment{color Blue} finish {phong .7}}}
sphere {< 0,1,4>, 1 texture{pigment{color Green} finish {phong .7}}}
sphere {< 2,1,4>, 1 texture{pigment{color Red} finish {phong .7  }}}

//progressive ior
sphere {<-5,1,0>, 1 texture{pigment{color Yellow filter 1 }finish{phong .8}} interior {ior 1.0}}
sphere {<-3,1,0>, 1 texture{pigment{color Yellow filter 1 }finish{phong .8}} interior {ior 1.5}}
sphere {<-1,1,0>, 1 texture{pigment{color Yellow filter 1 }finish{phong .8}} interior {ior 2}}
sphere {< 1,1,0>, 1 texture{pigment{color Yellow filter 1 }finish{phong .8}} interior {ior 2.5}}

//progressive ior
sphere {<-5,1,-4>, 1 texture{pigment{color Red transmit .9 }finish{phong .8}} interior {ior 1.0}}
sphere {<-3,1,-4>, 1 texture{pigment{color Red transmit .9 }finish{phong .8}} interior {ior 1.5}}
sphere {<-1,1,-4>, 1 texture{pigment{color Red transmit .9 }finish{phong .8}} interior {ior 2}}
sphere {< 1,1,-4>, 1 texture{pigment{color Red transmit .9 }finish{phong .8}} interior {ior 2.5}}



plane { <0,1,0> // normal vector
      , 0 // distance from origin
  pigment {
    checker color White, color Gray
  }
}
*/
#include "textures.inc"
#include "colors.inc"
background{color White}
camera {
  angle 20
  location <2, 10, -30>
  look_at <0, 1, 0>
}

#declare MyGlass=
difference
{ cone { <0,0,0>,1,<0,5,0>,1.2 }
  cone { <0,.1,0>,.9,<0,5.1,0>,1.1 }
  texture { Glass }
}

#declare MyGlassWithWater=
union
{ object { MyGlass }
  intersection
  { cone { <0,.1,0>,.9,<0,5.1,0>,1.1 }
    plane { y,4 }
    scale .999
    texture { Water }
  }
}

MyGlassWithWater

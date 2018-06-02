#include "colors.inc"
#include "TreeMesh.pov"
#include "rad_def.inc"

background {color Black}

global_settings {
/*
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
    brightness 0.7

    adc_bailout 0.01/2
  }
*/
  subsurface {
    samples 500, 100
  }
}

light_source { <-500 * cos(clock*2*pi),500 * sin(clock*2*pi),500> color White }

camera {
  //location <0,6,6>
  location <0,8,7>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,1>
  angle 45
}

#declare Finish = finish {ambient 0 diffuse 0.75 }

#declare Torus3 =
union {
  torus { 0.5 0.05 scale <2,1,2> }
  torus { 0.5 0.05 scale <2,1,2> rotate <90,0,0> }
  torus { 0.5 0.05 scale <2,1,2> rotate <0,0,90> }
}

#declare Outer =
difference {
  sphere { <0,0,0> 1 }
  sphere { <0,0,0> 0.7 }
  object { Torus3 }
  cone { <0.1,0.1,0.1> 0.0 <1,1,1> 1.1}
  cone { <-0.1,0.1,0.1> 0.0 <-1,1,1> 1.1}
  cone { <0.1,-0.1,0.1> 0.0 <1,-1,1> 1.1}
  cone { <-0.1,-0.1,0.1> 0.0 <-1,-1,1> 1.1}
  cone { <0.1,0.1,-0.1> 0.0 <1,1,-1> 1.1}
  cone { <-0.1,0.1,-0.1> 0.0 <-1,1,-1> 1.1}
  cone { <0.1,-0.1,-0.1> 0.0 <1,-1,-1> 1.1}
  cone { <-0.1,-0.1,-0.1> 0.0 <-1,-1,-1> 1.1}
}

#declare Inner =
difference {
  sphere { <0,0,0> 0.7 }
  sphere { <0,0,0> 0.6 }
}

union {
  object { Outer
    pigment { color <1,1,0.1> }
    finish { ambient 0.1 diffuse 0.6 specular 0.5 }
  }
  object { Inner
    pigment { color <1,0.25,0.01> }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 }
  }
  object { torus { 0.5 0.05 scale <2,1,2> }
    pigment { color Aquamarine }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 }
  }
  rotate <0,0,12>
  translate <-2.5,0,1>
}


union {
  object { Outer
    pigment { color <1,1,0.1> }
    finish { ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency <1,1,0.1> } }
    interior { ior 1.3 }
  }
  object { Inner
    pigment { color <1,0.25,0.01> }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency <1,0.25,0.01> } }
  }
  object { torus { 0.5 0.05 scale <2,1,2> }
    pigment { color Aquamarine }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency 0.5 * Aquamarine } }
  }
  rotate <0,0,20>
  translate <0,0,1>
}


union {
  object { Outer
    pigment { color <1,1,0.1> }
    finish { ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency <0.9,0.9,0.9> } }
    interior { ior 1.3 }
  }
  object { Inner
    pigment { color <1,0.25,0.01> }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency <0.9,0.9,0.9> } }
  }
  object { torus { 0.5 0.05 scale <2,1,2> }
    pigment { color Aquamarine }
    finish {ambient 0.1 diffuse 0.6 specular 0.5 subsurface { translucency <0.5,0.5,0.5> } }
  }
  rotate <0,0,30>
  translate <2.5,0,1>
}


plane { z 0
  texture { 
    pigment {color rgb <.9,.9,.9> }
    finish { Finish }
  }
  translate <0,0,-0.1>
}


#include "colors.inc"
#include "glass.inc"
#include "textures.inc"

global_settings {
  assumed_gamma 1

  photons {
    count 500000
    media 100
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
    brightness 0.4

    adc_bailout 0.01/2
  }

}

camera {
  location <0,-5,1>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,1.5>
  angle 110
}

// External light source
light_source { <-150,0,40> 1 }

// Moving external light source
//light_source { <-150 * cos(clock*pi), 0, 150 * sin(clock*pi)> color White
//  rotate <75,0,0>
//  rotate <0,0,-80>
//}

background { color White }

// Used with radiosity
#declare Finish = finish { ambient 0 diffuse 0.75 }

// Used with Photons only
//#declare Finish = finish { ambient 0.1 diffuse 0.75 }
//light_source { <0,-5,4.5> media_interaction off photons { reflection off } }


plane { z 0
  texture {
    pigment { color rgb <.7,.7,.7> }
    finish { Finish }
  }
}

// room
union {
  box { <-0.25,-4,0> <0.25,4,3> translate <-4,0,0> }
  box { <-0.25, -4,0> <0.25,4,5> translate <4,0,0> }
  box { <-4,-0.25,0> <4,0.25,3> translate <0,4,0> }
  box { <-2.25,-0.25,3> <4,0.25,5> translate <0,4,0>}
  box { <-4,-4,2.75> <-2,4,3> }
  box { <-2.25,-4,4.75> <4,4,5> }

  texture {
    pigment {color White}
    finish { Finish }
  }
}

#declare Ring =
difference {
  cylinder { <0,0,0> <0,0,0.1> 1 }
  cylinder { <0,0,-0.1> <0,0,0.2> 0.9 }
}

#declare RingCenter = cylinder { <0,0,0> <0,0,0.05> 0.95 }

#declare Rose =
intersection {
  cylinder { <0,0,-0.1> <0,0,0.2> 1}
  union {
    object { Ring translate <-1,-1,0> }
    object { Ring translate <-1,1,0> }
    object { Ring translate <1,1,0> }
    object { Ring translate <1,-1,0> }
    box { <-0.4142,-0.05,0> <0.4142,0.05,0.1> rotate <0,0,45> }
    box { <-0.4142,-0.05,0> <0.4142,0.05,0.1> rotate <0,0,-45> }
  }
}

#declare RoseGlass =
intersection {
  cylinder { <0,0,-0.1> <0,0,0.2> 1 }
  union {
    object { RingCenter texture { pigment { rgbf <1.0,0.0,0.0,0.9> } } translate <-1,-1,0> }
    object { RingCenter texture { pigment { rgbf <1.0,1.0,0.0,0.9> } } translate <-1,1,0> }
    object { RingCenter texture { pigment { rgbf <0.0,1.0,0.0,0.9> } } translate <1,1,0> }
    object { RingCenter texture { pigment { rgbf <0.0,0.0,1.0,0.9> } } translate <1,-1,0> }
  }
}

// window wall
difference {
  box { <-2.1,-4,3> <-2,4,5> }
  #for (i, -3, 3, 2)
    sphere { <-2,i,3.75> 0.75}
  #end
  texture {
    pigment {color White}
    finish { Finish }
  }
}

// Rose frame in center
object {
  Rose
  pigment { color White}
  finish { reflection <1,1,1> }
  photons {target reflection on}
  rotate <45,0,0>
  translate <0,0,0.65>
}

// Rose frames in wall
#for (i, -3, 3, 2)
object { Rose
  texture {
    pigment {color White}
    finish { Finish }
  }
  scale <0.75,0.75,1>
  rotate <0,90,0>
  translate <-2.1,i,3.75>
}
#end

// Rose glass in wall
#for (i, -3, 3, 2)
object { RoseGlass
  photons { pass_through }
  scale <0.75,0.75,1>
  rotate <0,90,0>
  translate <-2.1,i,3.75>
}
#end

// Reflective strip on right wall
box { <3.7,-4,2.4> <3.75,4,2.7>
  pigment {color White}
  finish { reflection <1,1,1> }
  photons {target reflection on }
}

// Scattering media box
/*
box { <-4,-4,0> <4,4,5>
  pigment { rgbt 1 } hollow
  interior {
    media {
      scattering { 1, 0.07 extinction 0.01 }
      samples 30
    }
  }
  photons { pass_through }
}
*/

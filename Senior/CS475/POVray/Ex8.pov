#include "colors.inc"

background {color White}

global_settings {
    radiosity {
      pretrace_start 0.08
      pretrace_end   0.04
      count 35

      nearest_count 5
      error_bound 1.8
      recursion_limit 3

      low_error_factor 0.5
      gray_threshold 0.0
      minimum_reuse 0.015
      brightness 0.4

      adc_bailout 0.01/2
    }
  }

camera {
  location <-20,30,10>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,0>
  angle 45
}

light_source { <150,0,50> color White }

plane { <0,0,1>, -4
  texture { 
    pigment {color rgb <.8,.8,.8> }
    finish { ambient 0 diffuse 0.9 phong 0.5}
  }
}

box { <0.25,-18,-3> <-0.25,0,7>
  texture {
    pigment {color rgb <1,0,0>}
    finish {ambient 0 diffuse 1 phong 0.5}
  }
  rotate <0,-90 * clock,0>
  translate <-10,0,0>
}

sphere { <0,-8,0>, 2
  texture {
    pigment {color rgb <.9,.9,.9>}
    finish {ambient 0 diffuse 1 phong 0.5 }
  }
}

sphere { <0,8,0>, 2
  texture {
    pigment {color rgb <.9,.9,.9>}
    finish {ambient 0 diffuse 1 phong 0.5 }
  }
}


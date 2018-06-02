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
    brightness 0.3

    adc_bailout 0.01/2
  }
*/
}

light_source { <-100,150,150> color White }

camera {
  location <0,6,6>
  //location <0,12,12>
  sky <0,0,1>
  right -x * image_width/image_height
  look_at <0,0,1>
  angle 60
}

#declare Finish = finish {ambient 0 diffuse 0.75 }

#declare pos = function {
  spline {
    natural_spline
    #for (i, 0.0, 3 * 2 * pi, 0.5)
      i <0.8*cos(i),0.8*sin(i),i/10>
    #end
  }
}

#declare spiral_1 =
sphere_sweep {
  b_spline
  181
  #for (i,0,18,0.1)
  pos(i) 0.1
  #end
}

#declare spiral_2 =
sphere_sweep {
  b_spline
  175
  #for (i,0.3,17.7,0.1)
  pos(i) 0.2
  #end
}


  difference { object { spiral_2 } object { spiral_1 }
  pigment { color Green filter 0.8 }
  finish { ambient 0 diffuse 0 reflection .25 specular 1 roughness .001 }
  interior { ior 1.3 }
}

/*
cylinder { <0,0,0> <0,0,1> 1.1
  pigment { color Green filter 0.8 }
  finish { ambient 0 diffuse 0 reflection .25 specular 1 roughness .001 }
  interior { ior 1.3 }
}
*/
/*
object { spiral_1
  pigment { color Red filter 0.8}
  finish {ambient 0 diffuse 0 specular 1 reflection .25 }
}
*/
/*
#for (i, 0, 1, 0.2)
sphere { <0,0,1> 1
  pigment { color Blue filter i}
  finish { ambient 0 diffuse 0 reflection .25 specular 1 roughness .001 }
  interior { ior 1.33 fade_color Blue}
  translate <12*i-6,0,0>
}
#end

#declare shell =
difference {
  sphere { <0,0,0> 1 }
  sphere { <0,0,0> 0.9}
}

#for (i, 0, 1, 0.2)
object { shell
  pigment { color Blue filter i}
  finish { ambient 0 diffuse 0 reflection .25 specular 1 roughness .001 }
  interior { ior 1.33 fade_color Blue}
  translate <12*i-6,3,1>
}
#end
*/

plane { z 0
  texture { 
    pigment {color rgb <.9,.9,.9> }
    finish { Finish }
  }
  translate <0,0,-0.1>
}


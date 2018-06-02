// extended from a POVray example

global_settings {
  assumed_gamma 1
  photons {
    count 200000
    media 100
  }
}

camera { location <14.9, 1, -8> look_at -z angle 70 }
light_source { <10,100,150>, 1 }
background { rgb <0.3, 0.6, 0.9> }

// A dim light source inside the room which does not
// interact with media so that we can see the room:
light_source { <14, -5, 2>, 0.5 media_interaction off  photons { reflection off} }

// Room
union
{ difference
  { box { <-11, -7, -11>, <16, 7, 10.5> }
    box { <-10, -6, -10>, <15, 6, 10> }
    box { <-4, -2, 9.9>, <2, 3, 10.6> }
  }
  box { <-1.25, -2, 10>, <-0.75, 3, 10.5> }
  box { <-4, 0.25, 10>, <2, 0.75, 10.5> }
  pigment { rgb 1 }
}

// Reflective floor:
box
{ <-10, -5.99, -10>, <15, -6, 10>
  pigment { rgb 1 }
  finish { reflection <0.5, 0.4, 0.2> }
  photons { target reflection on }
}

// Scattering media box:

box
{ <-5, -6.5, -10.5>, <3, 6.5, 10.25>
  pigment { rgbt 1 } hollow
  interior
  { media
    { scattering { 1, 0.07 extinction 0.01 }
      samples 30
    }
  }
  photons { pass_through }
}

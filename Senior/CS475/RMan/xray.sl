surface 
xray (float Ka = 1, Kd = 0.5, Ks = 0.5, roughness = 0.1;
                 color specularcolor = 1;
                 color opacitycolor = color "rgb" (0,0,0)) {
        
  color o;

  normal Nf = faceforward (normalize(N),I);

  o = Os;

  illuminance ( P, N, PI / 2 ) {
    if ( Cl . Cl >= 3) o = opacitycolor;
  }

  Ci = Cs * (Ka*ambient() 
          + Kd*diffuse(Nf)) 
          + specularcolor * Ks*specular(Nf,-normalize(I),roughness);
  Oi = o;
  Ci *= Oi;
}

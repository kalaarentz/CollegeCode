surface 
fluoresce (float Ka = 1, Kd = 0.5, Ks = 0.5, roughness = 0.1;
                 color specularcolor = 1;
                 color fluoresce = color "rgb" (1,0,0)) {
        
  color s;

  normal Nf = faceforward (normalize(N),I);

  s = Cs;

  illuminance ( P, Nf, PI / 2 ) {
    if ( Cl . Cl >= 3) s = fluoresce;
  }

  Ci = s * (Ka*ambient() 
          + Kd*diffuse(Nf)) 
          + specularcolor * Ks*specular(Nf,-normalize(I),roughness);
  Oi = Os;
  Ci *= Oi;
}

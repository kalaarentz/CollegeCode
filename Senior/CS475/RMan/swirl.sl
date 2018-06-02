surface
swirl( float 	Ks 	= .5,
		Kd 	= .5,
		Ka	= .1,
		rough	= .1,
		density = .25,
		freq	= 20;
	 color  spec	= color (1,1,1) )
{ varying point Nf = faceforward(normalize(N),I);

  if (mod(s*freq+t*freq,1) < density)
    Oi = 1.0;
  else
    Oi = 0.0;
    
  Ci = Oi * ( Cs * (Ka*ambient() + Kd*diffuse(normalize(Nf))) 
              + spec * Ks * specular(Nf,-I,rough));
}
surface
screen2( float 	Ks 	= .5,
		Kd 	= .5,
		Ka	= .1,
		rough	= .1,
		density = .25,
		freq	= 20;
	 color  spec	= color (1,1,1) )
{ varying point Nf = faceforward(normalize(N),I);

  Oi = (cos(s*freq*3.14159) + 1 ) / 2.0;    
  Ci = Oi * ( Cs * (Ka*ambient() + Kd*diffuse(normalize(Nf))) 
              + spec * Ks * specular(Nf,-I,rough));
}
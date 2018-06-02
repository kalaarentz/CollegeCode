/*
	blue_marble(): a marble stone texture in shades of blue
*/

surface
marble(
	float	Kd				= .6,
				Ka				= .1,
				Ks        = .4,
				rough     = .1,
				txtscale  = 1;
	color spec      = 1)
				
{	point PP;			  /* scaled point in shader space */
	float csp;		  /* color spline parameter */
	vector Nf;			/* forward-facing normal */
	float pixelsize, twice, scale, weight, turbulence;
	
	/* Obtain a forward-facing normal for lighting calculations. */
	Nf = normalize(faceforward(N,I));
	
	/* Compute "turbulence a la [PERLIN85]. Turbulence is a sum of
		 "noise" components with a "fractal" 1/f power spectrum. It gives the
		  visual impression of turbulent fluid flow (for example, as in the
			formation of blue_marble from molten color splines!). Use the 
			surface element area in texture space to control the number of
			noise components so that the frequency content is appropriate
			to the scale. This prevents aliasing of the texture.
	*/

	PP = transform("shader",P) *txtscale;
	pixelsize = sqrt(area(PP));
	twice = 2*pixelsize;
	turbulence = 0;
	for (scale = 1; scale > twice; scale/=2)
		turbulence += scale * noise(PP/scale);

	/* gradual fade out of highest-frequency component near limit */

	if (scale >pixelsize) {
		weight = (scale / pixelsize) - 1;
		weight = clamp(weight,0,1);
		turbulence += weight*scale*noise(PP/scale);
	}

	/* Magnify the upper part of the turbulence range 0.75:1
	   to fill the range 0:1 and use it as the parameter of
		 a color spline through various shades of blue.
	*/

  csp = clamp(4*turbulence-3,0,1);
  float f = float spline(csp,1.0,1.0,0.6,0.6,0.6,0.8,0.8,0.4,0.4,0.2,0.2,0.6,0.2);
  
  Ci = Cs * f;
  
  /* Multiply this color by the diffusely reflected light */
	Ci *= Ka*ambient() + Kd*diffuse(Nf);
	
	/* Adjust for opacity */
	Oi = Os;
	Ci = Ci * Oi;
	
	/* Add in specular hightlight */
  if (f > 0.5) {
    Ci += f * spec * Ks * specular(Nf,normalize(-I),rough);
  }
}
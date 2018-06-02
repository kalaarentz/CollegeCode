/*
	blue_marble(): a marble stone texture in shades of blue
*/

surface
noise(
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

	PP = transform("shader",P) ;
  color clr = color noise(PP)*txtscale;
  
  Ci = clr;
  
  /* Multiply this color by the diffusely reflected light */
	Ci *= Ka*ambient() + Kd*diffuse(Nf);
	
	/* Adjust for opacity */
	Oi = Os;
	Ci = Ci * Oi;
	
	/* Add in specular hightlight */
  Ci += spec * Ks * specular(Nf,normalize(-I),rough);
}
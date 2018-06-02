light
gridlight( float ringangle = PI/12,
	ringwidth = 0.2,
	coneangle = PI/4;
	point 	from = (1,1,1),
		to	 = (0,0,0);
	color	lightcolor = color "rgb" (1,1,1);
	float intensity = 1 )

{ 	point axis, pv;
	float d, i, pa;
	
	axis = normalize(to-from);
	
	illuminate(from,axis,coneangle) {
		pv = normalize(Ps - from);
		pa = acos(pv . axis);
		d = abs(cos(pa * 100));
/*
		d = abs(ringangle - pa);
*//*
		printf("axis: %p pv: %d pa: %f d: %f\n",axis,pv,pa,d);
*/

		if (d < ringwidth) {
			Cl = intensity * lightcolor;
		}
		else {
			Cl = 0;
		}


	}

}
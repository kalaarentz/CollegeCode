displacement
waves(
	float Km = 0.01;
	float scale = 5)
{
	float mag;
	float r;

	r = sqrt( (s - 0.5) * (s - 0.5) + (t - 0.5) * (t - 0.5) );

	mag = sin((r)*scale*PI);
	
	P += -Km * mag * normalize(N);
	N = calculatenormal(P);
}
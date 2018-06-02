/* From the RenderMan Companion
*/

displacement
dented (
        float Km = 1.0)
{
  float size = 1.0,
  magnitude = 0.0,
  i;
  point P2;
		
  P2 = transform ("object", P);
  for (i = 0; i < 6.0; i += 1.0) {
    magnitude += abs (.5 - noise (P2 * size)) / size;
    size *= 2.0;
  }
  P = P - normalize (N) * (magnitude * magnitude * magnitude)* Km;
  N = calculatenormal(P);
}
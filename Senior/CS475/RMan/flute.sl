/* From the RenderMan Companion
*/

displacement
flute (
        float Km = .1,
              freq = 10,
              margin = 0.9,
              marginDelta = 0.05)
{
  float size = 1.0,
  magnitude = 0.0,
  i;
  point P2;
		
  P2 = transform ("object", P);
  float x = xcomp(P2);
  float y = ycomp(P2);
  float a = atan(y,x);
  float disp = cos(freq * a);
  disp = clamp(disp,0,1);
  disp = pow(disp,0.3);
  
  float contrib = smoothstep(margin-marginDelta,margin+marginDelta,abs(zcomp(P2)));
  contrib = contrib*contrib;
  contrib = contrib - 1;
  
  P = P + normalize (N) * contrib * disp * Km;
  N = calculatenormal(P);
}
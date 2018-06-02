/* emboss
*/

displacement
emboss(
	float Km = 0.1;
	string texturename = ""
	)
{
	if (texturename != "") {
		P -= Km * texture(texturename,s,t) * normalize(N);
		N = calculatenormal(P);
	}
}
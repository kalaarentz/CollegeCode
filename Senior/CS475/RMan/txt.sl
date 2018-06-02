surface
txt( float Ks=.5, Kd=.5, Ka=1, roughness=.1; color specularcolor=1;
         string texturename = ""; )
{
    normal Nf;
    vector V;
    color Ct;

    Nf = faceforward( normalize(N), I );
    V = -normalize(I);


    if (texturename != "") {
        Ct = color texture(texturename);
    } else {
        Ct = 1.0;
    }

/*
Ct = cos(s*10*3.14159) * cos(t*10*3.14159);
*/
    Oi = Os;
    Ci = Os * ( Cs * Ct * (Ka*ambient() + Kd*diffuse(Nf)) +
                specularcolor * Ks * specular(Nf,V,roughness) );
}

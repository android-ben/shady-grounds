package com.punkbytes.shadygrounds.shaders

import android.graphics.RuntimeShader

/**
 * Source: https://www.shadertoy.com/view/XlsyRB
 */
val FlyingHeartsShader = RuntimeShader(
    """
/** Flying Hearts
    Copyright (C) 2017  Alexander Kraus <nr4@z10.info>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

uniform float2 iResolution; // <-- parameter - all pixels get the same parameter
uniform float iTime;
        
const float PI = 3.14159;

vec4 gradient(vec4 old, vec2 x, vec2 start, vec2 end, vec4 color1, vec4 color2)
{
    vec2 dir = dot(x-start, end-start)*(end-start);
    float scale = length(dir)/length(end-start);
    return color1+scale*(color2-color1);
}

vec4 heart(vec4 background, vec2 x, vec2 x0, float size)
{
    vec2 xa = (x-x0)/size;

    if(abs(xa.x) > 1.) return background;
    
    vec2 wing = vec2(1.,2.);
    if(xa.y<abs(xa.x)*wing.y/wing.x+xa.x/abs(xa.x)/15.*sin(2.*PI*xa.x)) return background;
    
    vec2 c = vec2(-.5,1.), r2 = vec2(.5, 1.);
    float R = .5;
    if(xa.y > 1.+sqrt(R*R-(abs(xa.x)-r2.x)*(abs(xa.x)-r2.x))+r2.y)
        return background;
    
    return gradient(background, x, x*size+vec2(0.,1.), x*size+vec2(0.,0.),vec4(232./255., 23./255., 3./255., 1.),  vec4(231./255., 61./255., 156./255., 1.) );
}

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

half4 main( vec2 fragCoord)
{
    vec2 relative_coord = fragCoord.xy / iResolution.xy;
    float time = iTime-floor(iTime/6.)*6.;
    float phi = 1.e-2*time;
    
    vec4 structure = gradient(vec4(0.,0.,0.,1.), relative_coord, vec2(0.,1.), vec2(0.,.8), vec4(0.,0.,0.,1.), vec4(0.,0.,1.,1.));
    
    
    float j=0.;
    float x=-1.;
    for(int i=0; i<35; ++i)
    {
        x+=0.3*rand(vec2(x, j*x))+1.e-3;
        float y0 = .9*rand(vec2(x, 2.*j*x)), omega = 2.e-1*rand(vec2(x, 3.*j*x));
        float size = .04*rand(vec2(x, 5.*j*x));
        structure = heart(structure, relative_coord, vec2(x+.5*sin(omega*time),-1.+y0+1.e-1*time*time), size);
        j+=1.;
    }
    
    return structure;
}

    """
)
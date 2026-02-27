//
// via: https://github.com/drinkthestars/shady?ref=blog.sebastiano.dev
//
package com.punkbytes.shadygrounds.shaders

import android.graphics.RuntimeShader

/**
 * Shadertoy's default shader
 */
val GradientShader = RuntimeShader(
    """
        uniform float2 resolution;
        uniform float time;
        
        vec4 main(vec2 fragCoord) {
            // Normalized pixel coordinates (from 0 to 1)
            vec2 uv = fragCoord/resolution.xy;
    
            // Time varying pixel color
            vec3 col = 0.8 + 0.2 * cos(time*2.0+uv.xxx*2.0+vec3(1,2,4));
    
            // Output to screen
            return vec4(col,1.0);
        }
    """
)

/**
 * From: https://shaders.skia.org/?id=de2a4d7d893a7251eb33129ddf9d76ea517901cec960db116a1bbd7832757c1f
 */
val NoodleZoomShader = RuntimeShader(
    """
        uniform float2 resolution;
        uniform float time;

        // Source: @notargs https://twitter.com/notargs/status/1250468645030858753
        float f(vec3 p) {
            p.z -= time * 10.;
            float a = p.z * .1;
            p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
            return .1 - length(cos(p.xy) + sin(p.yz));
        }
        
        half4 main(vec2 fragcoord) { 
            vec3 d = .5 - fragcoord.xy1 / resolution.y;
            vec3 p=vec3(0);
            for (int i = 0; i < 32; i++) {
              p += f(p) * d;
            }
            return ((sin(p) + vec3(2, 5, 12)) / length(p)).xyz1;
        }
    """
)

val HeartsZoomingShader = RuntimeShader(
    """
        uniform float2 iResolution;
        uniform float iTime;
        
// returns: 1.0 = heart contour (bounded by -1,-1,+1,+1)
// <1.0 = inside, >1.0 = outside
float heart(vec2 uv) {
    uv /= vec2(4.5,4.0);
    float absX = abs(uv.x);
    uv.y = uv.y * 1.2 + absX * absX * 1.1 - absX * 0.66;    
    return 5.0*distance(uv, vec2(0.0));
}


// Function to create the colors (https://iquilezles.org/articles/palettes/)
vec3 palette( in float t )
{
    vec3 a = vec3(0.5, 0.5, 0.5);
    vec3 b = vec3(0.5, 0.5, 0.5);
    vec3 c = vec3(1.0, 1.0, 1.0);
    vec3 d = vec3(0.00, 0.10, 0.20);
    
    return a + b*cos( 6.28318*(c*t+d) );
}

half4 main( vec2 fragCoord )
{
    // Normalize uv with a fixed aspect ratio
    vec2 uv = (fragCoord * 2.0 - iResolution.xy) / iResolution.y;
    uv.y = -uv.y;
    
    vec3 color = 0.1 + 0.4*palette(sin(0.1*uv.x + 0.24*iTime) + sin(0.22*uv.y + 0.3*iTime) + sin(0.33*uv.x) +sin(0.13*uv.y) + sin(iTime*1.2));
    
    float d0 = 0.0;
    for (float j = 1.0; j < 2.0; j++) { // Makes 5 groups of stars
        for (float i = 0.0; i < 30.0; i+=3.0) { // Makes 20 stars in every group
            // Bezier curve (https://www.youtube.com/watch?v=aVwxzDHniEw)
            float t = (sin(1.0*iTime + i*8.0) + 1.0) / 2.5;
            float tt = 0.5*iTime + i*6.0;
            // 0.84, -0.67, 1.45 and so on are just random values to make the movement look random 
            vec2 p1 = 1.1*vec2(sin(tt* 2.83 * j), cos(tt* -1.67 * j / 2.0)); // Multiplying by j to create the kind of flower effect
            vec2 p2 = 1.2*vec2(cos(iTime * 1.45), cos(iTime * 0.78));
            vec2 p3 = 1.3*vec2(cos(iTime * -0.67), sin(iTime * 0.23));
            vec2 p4 = 1.2*vec2(sin(iTime * -0.19), sin(iTime * -0.35));
            vec2 p5 = p1;
            //vec2(0.0,0.0);
            vec2 q1 = p1 + t * (p2 - p1);
            vec2 q2 = p2 + t * (p3 - p2);
            vec2 q3 = p3 + t * (p4 - p3);
            vec2 q4 = p4 + t * (p5 - p4);

            vec2 r1 = q1 + t * (q2 - q1);
            vec2 r2 = q2 + t * (q3 - q2);
            vec2 r3 = q3 + t * (q4 - q3);

            vec2 s1 = r1 + t * (r2 - r1);
            vec2 s2 = r2 + t * (r3 - r2);

            vec2 u1 = s1 + t * (s2 - s1);
            u1.y *= 0.6;
            float d = heart(uv - u1);

            d = 0.5 / d;

            if (d > d0) d0 = d;
        }
    }
    
    return vec4(color*d0, 1.0);
} 
    """
)

/**
 * Source: https://shaders.skia.org/?id=23a360c975c3cb195c89ccdf65ec549e279ce8a959643b447e69cb70614a6eca
 *
 * Not currently working
 */
val CloudsZoomingShader = RuntimeShader(
    """
uniform float2 iResolution;
uniform float iTime;

// Source: @zozuar https://twitter.com/zozuar/status/1492217553103503363

vec3 hsv(float h, float s, float v){
    vec4 t = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(vec3(h) + t.xyz) * 6.0 - vec3(t.w));
    return v * mix(vec3(t.x), clamp(p - vec3(t.x), 0.0, 1.0), s);
}

//half4 main( vec2 fragCoord )
vec4 main(vec2 FC) {
  float e=0,R=0,t=iTime,s;
  vec2 r = iResolution.xy;
  vec3 q=vec3(0,0,-1), p, d=vec3((FC.xy-.5*r)/r.y,.7);
  vec4 o=vec4(0);
  for(float i=0;i<100;++i) {
    o.rgb+=hsv(.1,e*.4,e/1e2)+.005;
    p=q+=d*max(e,.02)*R*.3;
    float py = (p.x == 0 && p.y == 0) ? 1 : p.y;
    p=vec3(log(R=length(p))-t,e=asin(-p.z/R)-1.,atan(p.x,py)+t/3.);
    s=1;
    for(int z=1; z<=9; ++z) {
      e+=cos(dot(sin(p*s),cos(p.zxy*s)))/s;
      s+=s;
    }
    i>50.?d/=-d:d;
  }
  return o;
}
    """
)

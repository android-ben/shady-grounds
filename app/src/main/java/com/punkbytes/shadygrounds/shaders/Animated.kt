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

/*
val StarfallShader = RuntimeShader(
    """
// reference: https://www.shadertoy.com/view/tfByRK

uniform float2 iResolution;
uniform float iTime;

const int   MAX_RAY_STEPS   = 140;
const float TAIL_LENGTH     = 10.5; 
const float TAIL_DECAY      = 0.5;  
const float FALL_SPEED      = 12.0; 
const float GLOW_EXPOSURE   = 99.0; 
const float STAR_SIZE       = 1.5;  // Size of cross-flare-head
const float STAR_BRIGHTNESS = 2.0;  // Brightness of cross-flare-head

float sRGBencode(float linearColor) 
{ 
    return linearColor > 0.0031308 ? (1.055 * pow(linearColor, 1.0 / 2.4) - 0.055) : (12.92 * linearColor); 
}

vec3 sRGBencode(vec3 linearColor) 
{ 
    linearColor = clamp(linearColor, 0.0, 1.0); 
    return vec3(sRGBencode(linearColor.x), sRGBencode(linearColor.y), sRGBencode(linearColor.z)); 
}

float Star(vec2 uv, float flare)
{
    float d = length(uv);
    float m = 0.02 / (d + 0.001); // Add tiny number for dividing zero
    float rays = max(0.0, 1.0 - abs(uv.x * uv.y * 200.0)); 
    m += rays * flare;
    m *= smoothstep(0.8, 0.05, d); 
    return m;
}

// Replaces iChannel0 noise texture with a procedural hash per grid cell
vec4 getStarData(vec2 gridCell) {
    vec2 p = floor(gridCell);
    return vec4(
        fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453),
        fract(sin(dot(p, vec2(269.5, 183.3))) * 43758.5453),
        fract(sin(dot(p, vec2(419.2, 371.9))) * 43758.5453),
        fract(sin(dot(p, vec2(591.7, 449.3))) * 43758.5453)
    );
}

vec4 main(vec2 fragCoord)
{
    vec2 uv = (2.0 * fragCoord - iResolution.xy) / iResolution.y;
    uv.y = -uv.y;
    vec3 finalColor = vec3(0.0);
    float currentTime = iTime;

    // Camera Setup
    float focalLength = 2.0;
    vec3 rayOrigin = vec3(0.5, 3.8, 0.5 - currentTime); 
    vec3 rayDir = normalize(vec3(uv, -focalLength));    

    // Camera Rotation Angles
    float pitchAngle = -0.35;
    float cp = cos(pitchAngle), sp = sin(pitchAngle);
    float yawAngle = 0.25 * sin(currentTime * 0.3);
    float cy = cos(yawAngle), sy = sin(yawAngle);

    // Apply Rotation to Ray Direction
    rayDir.yz *= mat2(cp, sp, -sp, cp);
    rayDir.xz *= mat2(cy, sy, -sy, cy);

    // Calc Camera local axis(right, up) for 3D billboard
    vec3 camRight = vec3(1.0, 0.0, 0.0);
    camRight.yz *= mat2(cp, sp, -sp, cp);
    camRight.xz *= mat2(cy, sy, -sy, cy);

    vec3 camUp = vec3(0.0, 1.0, 0.0);
    camUp.yz *= mat2(cp, sp, -sp, cp);
    camUp.xz *= mat2(cy, sy, -sy, cy);

    // Raymarching Loop
    float rayDistance = 0.0;    
    for(int i = 0; i < MAX_RAY_STEPS; i++)
    {
        vec3 rayPos = rayDir * rayDistance + rayOrigin;
        vec2 gridCell = vec2(round(rayPos.x), round(rayPos.z));
        
        vec4 noiseData = getStarData(gridCell);

        float dropHeadY = mod(noiseData.a * 93.0 - currentTime * FALL_SPEED, 45.0);
        
        // Calc tail
        vec3 nearestPointOnLine = vec3(gridCell.x, clamp(rayPos.y, dropHeadY, dropHeadY + TAIL_LENGTH), gridCell.y);
        float distToLine = length(rayPos - nearestPointOnLine);
        
        float tailAttenuation = exp(-max(TAIL_DECAY * (rayPos.y - dropHeadY), 0.0)); 
        float tailGlow = tailAttenuation / (abs(distToLine * distToLine) + 0.001); 

        // Calc cross-flare-head position for billboard
        vec3 headPos = vec3(gridCell.x, dropHeadY, gridCell.y);
        vec3 deltaToHead = rayPos - headPos;

        // Project cross-shaped-head from 3d to 2d for billboard
        vec2 flareUV = vec2(dot(deltaToHead, camRight), dot(deltaToHead, camUp));
        
        // Rotate head through time
        float rotSpeed = currentTime * (3.0 + noiseData.b * 2.0);
        float cr = cos(rotSpeed), sr = sin(rotSpeed);
        flareUV *= mat2(cr, sr, -sr, cr);
        
        float headGlow = Star(flareUV * (1.0 / STAR_SIZE), STAR_BRIGHTNESS);
        float depthAttenuation = exp(-0.05 * rayDistance);        
        finalColor += depthAttenuation * (tailGlow * 0.5 + headGlow * 15.0) * noiseData.rgb;
        
        rayDistance += min(0.5, distToLine) + 0.01;
    }    

    finalColor = tanh(finalColor / GLOW_EXPOSURE);
    finalColor = sRGBencode(finalColor);
    return vec4(finalColor, 1.0);
}
    """
)
*/

val SunsetOceanShader = RuntimeShader(
    """
        
uniform float2 iResolution;
uniform float iTime;
float3 iMouse = float3(0, 0, 0);

const float PI = 3.1415926535;
const int STEPS  = 50;

mat2 rot( in float a ) {
    float c = cos(a);
    float s = sin(a);
	return mat2(c,s,-s,c);	
}

// noise function
float noise( in vec2 p ) {	
    p *= rot(1.941611);
    return sin(p.x) * .25 + sin(p.y) * .25 + .50;
}

// get the 2 closest point with the projected height as z
void grid( in vec2 p, inout vec3 projClosest, inout vec3 projSecondClosest ) {
    vec2 center = floor(p) + 0.5;
    vec2 secondBestCenter = center;
    float secondBestDist = 99999.9;
    vec2 bestCenter = center;
    float bestDist = 99999.9;
    
    for (int y = -1 ; y <= 1 ; y++)
    for (int x = -1 ; x <= 1 ; x++) {
		vec2 currentCenter = center + vec2(x, y);
        // vary each center a bit
       	currentCenter.x += noise( 
            iTime * vec2(0.5124, 0.5894) + 
            currentCenter * vec2(1.3124, 1.7894)) * 1.0 - 0.5;
        currentCenter.y += noise( 
            iTime * vec2(0.5565, 0.5561) - 
            currentCenter * vec2(1.5124, 1.6053)) * 1.0 - 0.5;
        
        vec2 delta = p - currentCenter;
        float currentDist = dot(delta, delta)*0.5;
        // use an analytical if to avoid the branch
        float if1 = step(currentDist, bestDist);
        float if1m = 1.0 - if1;
        secondBestCenter = if1*bestCenter + if1m*secondBestCenter;
        secondBestDist = if1*bestDist + if1m*secondBestDist;
        bestCenter = if1*currentCenter + if1m*bestCenter;
        bestDist = if1*currentDist + if1m*bestDist;
        // else if
        float if2 = step(currentDist, secondBestDist)*if1m;
        float if2m = 1.0 - if2;
        secondBestCenter = if2*currentCenter + if2m*secondBestCenter;
        secondBestDist = if2*currentDist + if2m*secondBestDist;

    }
    
    projClosest = vec3(bestCenter, bestDist);
    projSecondClosest = vec3(secondBestCenter, secondBestDist);

}

// normal function
vec3 normal( in vec3 p, in vec3 proj ) {
    vec2 dir = proj.xy - p.xy;
    vec3 tang = vec3(dir, proj.z*0.12);
    vec3 nine = vec3(dir, 0).yxz;
    nine.x = -nine.x;
    return normalize(cross(nine, tang));
}

// distance function
float de( in vec3 p, inout vec3 projClosest, inout vec3 projSecondClosest ) {
    // get the closest points
    grid(p.xy, projClosest, projSecondClosest);
    float below = 0.0;
    below -= sin(dot(p.xy, vec2(0.005, 0.051)) * 4.0 + iTime * 0.5) * 0.4 + 0.2;
    below -= 1.0 - projClosest.z;
	return max(0.0, p.z - below);
}

// return the sun color at this direction
vec4 getSunColor( in vec3 dir, inout float inside ) {
    float dotp = dot(dir, vec3(-0.99, 0.0, 0.1));
    float sunHeight = smoothstep(0.01, 0.29, dir.z);
    inside = smoothstep(0.977, 0.979, dotp);
    float ytemp = abs(dir.y)*dir.y;
    float sunWave = sin(dir.z*300.0+iTime*1.846+
                        sin(ytemp*190.0+iTime*0.45)*1.3)*0.5+0.5;
   	float sunHeight2 = smoothstep(-0.1, 0.2, dir.z);
    sunWave = sunWave * sunHeight2 + 1.0 - sunHeight2;
    sunWave = (1.0-smoothstep(sunHeight2, 1.0, sunWave)) * (1.0 - sunHeight2) + sunHeight2;
    float sun = inside * sunWave;
    return vec4(mix(vec3(0.998, 0.108, 0.47), vec3(0.988, 0.769, 0.176), sunHeight), sun);
}

// get the space color
vec3 getSpaceColor( in vec3 dir ) {
    float scanline = sin(dir.z * 700.0 - iTime * 5.1)*0.5+0.5;
    scanline *= scanline;
    vec3 color = mix(vec3(0.1, 0.16, 0.26), vec3(0.1), scanline);
    vec2 uv = vec2(atan(dir.y, dir.x) / (2.0 * PI) + 0.5, mod(dir.z, 1.0));
    uv.x = mod(uv.x+2.0*PI, 1.0);
    uv.x *= 100.0;
    uv.y *= 15.00;
    uv *= rot(1.941611+iTime*0.00155);
    vec2 center = floor(uv) + 0.5;
    center.x += noise(center*48.6613) * 0.8 - 0.4;
    center.y += noise(center*-31.1577) * 0.8 - 0.4;
    float radius = smoothstep(0.6, 1.0, noise(center*42.487+
                                              vec2(0.1514, 0.1355)*iTime)); 
    radius *= 0.01;
    vec2 delta = uv-center;
    float dist = dot(delta, delta);
    float frac = 1.0-smoothstep(0.0, radius, dist);
    float frac2 = frac;
    frac2 *= frac2; frac2 *= frac2; frac2 *= frac2;
    vec3 lightColor = mix(vec3(0.988, 0.769, 0.176), 
                          vec3(0.988, 0.434, 0.875), noise(center*74.487));
    return mix(color, lightColor, frac) + vec3(1)*frac2;
}

// get the background color (ala cubemap)
vec3 getBackgroundColor( in vec3 dir ) {
    float horizon = 1.0 - smoothstep(0.0, 0.02, dir.z);
    // this is the background with the scanline
    vec3 color = getSpaceColor(dir);
    // get the sun
    float inside = 0.0;
    vec4 sun = getSunColor(dir, inside);
    color = mix(color, vec3(0.1, 0.16, 0.26), inside);
    color = mix(color, sun.rgb, sun.a);
    // the horizon
    color = mix(color, vec3(0.43, 0.77, 0.85), horizon * (1.0 - sun.a * 0.19));
    return color;
}

// the color gets more blue/white near edges of the voronoi cells
vec3 getWaveColor( in vec3 p, in vec3 projClosest, in vec3 projSecondClosest,
                  in vec3 dir, float dist, vec2 frag ) {
    float distanceToEdge = abs(projClosest.z-projSecondClosest.z);
    float distanceFrac = smoothstep(-10.0, 100.0, dist);
    distanceFrac *= distanceFrac; distanceFrac *= distanceFrac;
    float frac = smoothstep(0.0, 0.1+distanceFrac*0.9, distanceToEdge);
    // get the reflection
    vec3 norm = normal(p, projClosest);
    vec3 color = getBackgroundColor(reflect(dir, norm));
    // add a screenspace scanline
    frac *= (sin(frag.y/iResolution.y*700.0)*0.5+0.5)*(1.0-distanceFrac);
    return mix(vec3(0.43, 0.77, 0.85), color, frac);
}

vec4 main( vec2 fragCoord ) {
    vec2 uv = fragCoord.xy / iResolution.xy * 2.0 - 1.0;
	uv.y *= iResolution.y / iResolution.x;
    uv.y = -uv.y;
	
	vec3 from = vec3(0, 0, 0.2);
	vec3 dir = normalize(vec3(uv.x*0.6, 1.0, uv.y*-0.6));
    
	dir.xy *= rot(PI*.5);
	vec2 mouse=(iMouse.xy / iResolution.xy - 0.5) * 0.5;
    mouse *= step(1.0, iMouse.z);
	dir.xz *= rot(3.16-(-mouse.y*1.5)+sin(iTime*0.785)*0.008);
	dir.xy *= rot(-mouse.x*4.0+sin(iTime*0.416)*0.01);
    dir.yz *= rot(sin(iTime*0.287)*0.009);
    
	vec3 color = vec3(0);
    
    if (dir.z > 0.0) {
        color = getBackgroundColor(dir);
    } else {
       // project the starting position to z = 0 so we ccan lower the raymarch count
        float totdist = from.z / -dir.z;
        for (int steps = 0 ; steps < STEPS ; steps++) {
            vec3 p = from + totdist * dir;
            vec3 projClosest;
            vec3 projSecondClosest;
            p.x -= iTime * 2.7;
            float dist = de(p, projClosest, projSecondClosest);
            totdist += dist;
            if ( dist < 0.01 || steps == STEPS-1 ) {
                color = getWaveColor(p, projClosest, projSecondClosest,
                                     dir, totdist, fragCoord);
                break;
            }
        }
	}
    
    return vec4(color, 1);
    
}
 
    """
)

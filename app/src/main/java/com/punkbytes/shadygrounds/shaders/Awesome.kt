//
// SHADERS ARE AWESOME!
// https://blog.sebastiano.dev/shaders-are-awesome/
//
package com.punkbytes.shadygrounds.shaders

import android.graphics.RuntimeShader

/*
 * half4 - half precision float
 * return value half4 RGBA (note alpha last component)
 *      return half4(0.0); <-- fully transparent
 *      return half4(1.0); <-- opaque white
 * half4(1.0) is syntactic sugar for half4(1.0, 1.0, 1.0, 1.0)
 */
val AwesomeTransparentShader = RuntimeShader(
    """
        half4 main(float2 fragCoord) { // <-- current pixel's coordinates
            //return half4(fragCoord.xy / resolution.xy, 1.0, 1.0);
            return half4(0.0);
        }
    """
)

val AwesomeGradientShader = RuntimeShader(
    """
        // resolution - size in pixels of the bounds that we're drawing in
        uniform float2 iResolution; // <-- parameter - all pixels get the same parameter
        
        half4 main(vec2 fragCoord) { 
            float2 uv = fragCoord.xy / iResolution.xy; // uv = normalised coordinates (to range 0 to 1)
            return half4(uv.x, uv.y, 1.0, 1.0);
        }
    """
)

val AwesomeAnimatedGradientShader = RuntimeShader(
    """
        // resolution - size in pixels of the bounds that we're drawing in
        uniform float2 iResolution; // <-- parameter - all pixels get the same parameter
        uniform float iTime; 
        
        half4 main(float2 fragCoord) {
            float2 uv = fragCoord.xy / iResolution.xy; // uv = normalised coordinates (to range 0 to 1)
            float lol = 0.5 + sin(iTime.x) * 0.5;
            return half4(uv.x, uv.y, lol, 1.0);
        }
    """
)

val AwesomeCircleShader = RuntimeShader(
    """
        uniform float2 iResolution; // <-- parameter - all pixels get the same parameter
        //uniform float iTime;
        
        half4 main(float2 fragCoord) {
            vec2 uv = fragCoord.xy / iResolution.xy;
            vec2 center = vec2(0.5);
            
            float distance = distance(uv, center) - .5;
            vec3 color = vec3(distance);
            return vec4(color, 1.);
        }
    """
)
package com.punkbytes.shadygrounds.shaders

import android.graphics.RuntimeShader

val HeartParticlesShader = RuntimeShader(
    """
uniform float2 iResolution;
uniform float  iTime;
uniform float2 iButtonPos;
uniform float  iPressTime;   // animation time when button was pressed; -1 if never pressed
uniform float  iReleaseTime; // animation time when button was released; -1 if still pressed

const float PI = 3.14159265;

float rand(float seed) {
    return fract(sin(seed * 127.1 + 311.7) * 43758.5453);
}

// Heart implicit equation: (x²+y²-1)³ - x²y³ < 0
bool inHeart(vec2 p, vec2 center, float size) {
    float qx = (p.x - center.x) / size;
    float qy = -(p.y - center.y) / size - 0.1;
    float base = qx*qx + qy*qy - 1.0;
    float v = base*base*base - qx*qx * (qy*qy*qy);
    return v < 0.0;
}

half4 main(vec2 fragCoord) {
    if (iPressTime < 0.0) return half4(0.0);

    vec4 color = vec4(0.0);

    for (int i = 0; i < 20; i++) {
        float fi       = float(i);
        float phase    = rand(fi * 3.7);
        float speed    = 0.25 + rand(fi * 7.1) * 0.35;
        float driftAmp = (rand(fi * 13.3) - 0.5) * 80.0;
        float driftFreq= 0.8 + rand(fi * 17.9) * 1.2;
        float size     = 12.0 + rand(fi * 23.1) * 16.0;
        float riseMax  = 350.0 + rand(fi * 31.7) * 200.0;

        // birthTime is when this particle's current cycle actually started.
        // Only render if it was born inside the press window.
        float genNow    = floor(iTime * speed + phase);
        float birthTime = (genNow - phase) / speed;

        float releaseOrNow = (iReleaseTime < 0.0) ? iTime : iReleaseTime;
        if (birthTime < iPressTime || birthTime > releaseOrNow) continue;

        float age = fract(iTime * speed + phase);

        float px = iButtonPos.x + driftAmp * sin(2.0 * PI * driftFreq * age + phase * 2.0 * PI);
        float py = iButtonPos.y - age * riseMax;

        float alpha = 1.0 - smoothstep(0.5, 1.0, age);

        if (inHeart(fragCoord, vec2(px, py), size)) {
            vec3 hc = mix(
                vec3(1.0, 0.08, 0.28),
                vec3(1.0, 0.55, 0.72),
                rand(fi * 41.3)
            );
            color.rgb += hc * alpha * (1.0 - color.a);
            color.a   += alpha * (1.0 - color.a);
        }
    }

    return half4(color);
}
    """
)

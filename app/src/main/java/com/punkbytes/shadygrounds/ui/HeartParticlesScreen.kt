package com.punkbytes.shadygrounds.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import com.punkbytes.shadygrounds.shaders.AwesomeAnimatedGradientShader
import com.punkbytes.shadygrounds.shaders.HeartParticlesShader
import com.punkbytes.shadygrounds.shaders.HeartsZoomingShader
import com.punkbytes.shadygrounds.shaders.SunsetOceanShader
import dev.sebastiano.shaders.AnimatedCanvas
import dev.sebastiano.shaders.produceAnimationTime

@Composable
fun HeartParticlesScreen() {
    val bgShader = remember { SunsetOceanShader }
    val bgBrush = remember(bgShader) { ShaderBrush(bgShader) }

    val particleShader = remember { HeartParticlesShader }
    val particleBrush = remember(particleShader) { ShaderBrush(particleShader) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var buttonCenter by remember { mutableStateOf(Offset.Zero) }

    // Snapshot animation time at the moment of press and release so in-flight
    // hearts keep rising after the button is released.
    var pressStartTime by remember { mutableStateOf(-1f) }
    var releaseTime by remember { mutableStateOf(-1f) }
    var prevPressStartTime by remember { mutableStateOf(-1f) }
    var prevReleaseTime by remember { mutableStateOf(-1f) }
    val particleTime by produceAnimationTime(speed = 1f)

    LaunchedEffect(isPressed) {
        if (isPressed) {
            // Shift the current window to previous so its in-flight hearts survive
            prevPressStartTime = pressStartTime
            prevReleaseTime = releaseTime
            // Start a fresh current window
            pressStartTime = particleTime
            releaseTime = -1f
        } else if (pressStartTime >= 0f) {
            releaseTime = particleTime
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background shader
        AnimatedCanvas(Modifier.fillMaxSize()) { time ->
            bgShader.setFloatUniform("iResolution", size.width, size.height)
            bgShader.setFloatUniform("iTime", time)
            onDrawBehind { drawRect(bgBrush) }
        }

        // Particle overlay — uses drawWithCache directly so particleTime, pressStartTime
        // and releaseTime are read as Compose state, triggering redraw each frame.
        Box(
            Modifier.fillMaxSize().drawWithCache {
                particleShader.setFloatUniform("iResolution", size.width, size.height)
                particleShader.setFloatUniform("iTime", particleTime)
                particleShader.setFloatUniform("iButtonPos", buttonCenter.x, buttonCenter.y)
                particleShader.setFloatUniform("iPressTime", pressStartTime)
                particleShader.setFloatUniform("iReleaseTime", releaseTime)
                particleShader.setFloatUniform("iPrevPressTime", prevPressStartTime)
                particleShader.setFloatUniform("iPrevReleaseTime", prevReleaseTime)
                onDrawBehind { drawRect(particleBrush) }
            }
        )

        Button(
            onClick = {},
            interactionSource = interactionSource,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .onGloballyPositioned { coords ->
                    val pos = coords.positionInParent()
                    buttonCenter = Offset(
                        pos.x + coords.size.width / 2f,
                        pos.y + coords.size.height / 2f
                    )
                }
        ) {
            Text("❤️ Hold Me")
        }
    }
}


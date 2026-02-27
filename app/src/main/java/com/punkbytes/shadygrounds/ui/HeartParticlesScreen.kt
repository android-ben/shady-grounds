package com.punkbytes.shadygrounds.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import com.punkbytes.shadygrounds.shaders.HeartParticlesShader
import dev.sebastiano.shaders.AnimatedCanvas

@Composable
fun HeartParticlesScreen() {
    val shader = remember { HeartParticlesShader }
    val brush = remember(shader) { ShaderBrush(shader) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var buttonCenter by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedCanvas(Modifier.fillMaxSize()) { time ->
            shader.setFloatUniform("iResolution", size.width, size.height)
            shader.setFloatUniform("iTime", time)
            shader.setFloatUniform("iButtonPos", buttonCenter.x, buttonCenter.y)
            shader.setFloatUniform("iActive", if (isPressed) 1f else 0f)
            onDrawBehind {
                drawRect(brush)
            }
        }

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

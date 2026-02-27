package com.punkbytes.shadygrounds.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import com.punkbytes.shadygrounds.shaders.CloudsZoomingShader
import com.punkbytes.shadygrounds.shaders.FlyingHeartsShader
import com.punkbytes.shadygrounds.shaders.GradientShader
import com.punkbytes.shadygrounds.shaders.HeartsZoomingShader
import com.punkbytes.shadygrounds.shaders.NoodleZoomShader
import dev.sebastiano.shaders.AnimatedCanvas

@Composable
fun GradientShaderScreen() {
    val compiledShader = remember {
        GradientShader
    }
    val brush = remember(compiledShader) {
        ShaderBrush(compiledShader)
    }

    AnimatedCanvas(
        Modifier.fillMaxSize()
    ) { time ->
        compiledShader.setFloatUniform(
            /* uniformName = */ "resolution",
            /* value1 = */ size.width,
            /* value2 = */ size.height
        )
        compiledShader.setFloatUniform("time", time)
        onDrawBehind {
            drawRect(brush)
        }
    }
}

@Composable
fun NoodleShaderScreen() {
    val compiledShader = remember {
        NoodleZoomShader
    }
    val brush = remember(compiledShader) {
        ShaderBrush(compiledShader)
    }

    AnimatedCanvas(
        Modifier.fillMaxSize()
    ) { time ->
        compiledShader.setFloatUniform(
            /* uniformName = */ "resolution",
            /* value1 = */ size.width,
            /* value2 = */ size.height
        )
        compiledShader.setFloatUniform("time", time)
        onDrawBehind {
            drawRect(brush)
        }
    }
}

@Composable
fun HeartsShaderScreen() {
    val compiledShader = remember {
        //HeartsZoomingShader
        FlyingHeartsShader
    }
    val brush = remember(compiledShader) {
        ShaderBrush(compiledShader)
    }

    AnimatedCanvas(
        Modifier.fillMaxSize()
    ) { time ->
        compiledShader.setFloatUniform(
            /* uniformName = */ "iResolution",
            /* value1 = */ size.width,
            /* value2 = */ size.height
        )
        compiledShader.setFloatUniform("iTime", time)
        onDrawBehind {
            drawRect(brush)
        }
    }
}




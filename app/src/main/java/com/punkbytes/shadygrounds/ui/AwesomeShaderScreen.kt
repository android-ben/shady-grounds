package com.punkbytes.shadygrounds.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import com.punkbytes.shadygrounds.shaders.AwesomeAnimatedGradientShader
import com.punkbytes.shadygrounds.shaders.AwesomeCircleShader
import com.punkbytes.shadygrounds.shaders.AwesomeGradientShader
import dev.sebastiano.shaders.AnimatedCanvas

@Composable
fun AwesomeShaderScreen() {
    val compiledShader = remember {
        AwesomeGradientShader
    }
    val brush = remember(compiledShader) {
        ShaderBrush(compiledShader)
    }

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                compiledShader.setFloatUniform(
                    /* uniformName = */ "iResolution",
                    /* value1 = */ size.width,
                    /* value2 = */ size.height
                )
                drawRect(brush)
            }
    )
}

@Composable
fun AwesomeAnimatedShaderScreen() {
    val compiledShader = remember {
        AwesomeAnimatedGradientShader
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

@Composable
fun AwesomeCircle() {
    val compiledShader = remember {
        AwesomeCircleShader
    }
    val brush = remember(compiledShader) {
        ShaderBrush(compiledShader)
    }

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                compiledShader.setFloatUniform(
                    /* uniformName = */ "iResolution",
                    /* value1 = */ size.width,
                    /* value2 = */ size.height
                )
                drawRect(brush)
            }
    )
}

//@Preview
//@Composable
//private fun AwesomeCirclePreview() {
//    AwesomeCircle()
//}

@Preview
@Composable
private fun AwesomeShaderScreenPreview() {
    AwesomeShaderScreen()
}
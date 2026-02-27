package com.punkbytes.shadygrounds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.punkbytes.shadygrounds.shaders.GradientShader
import com.punkbytes.shadygrounds.ui.AwesomeAnimatedShaderScreen
import com.punkbytes.shadygrounds.ui.AwesomeShaderScreen
import com.punkbytes.shadygrounds.ui.GradientShaderScreen
import com.punkbytes.shadygrounds.ui.HeartParticlesScreen
import com.punkbytes.shadygrounds.ui.HeartsShaderScreen
import com.punkbytes.shadygrounds.ui.NoodleShaderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //AwesomeAnimatedShaderScreen()
            //NoodleShaderScreen()
            //GradientShaderScreen()
            //HeartsShaderScreen()
            HeartParticlesScreen()
        }
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}
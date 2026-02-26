/*
 * Copyright 2025 Sebastiano Poggi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.sebastiano.shaders

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.drawWithCache
import kotlin.time.Duration.Companion.hours

private val maxTimeValue = 24.hours.inWholeMilliseconds

@Composable
fun produceAnimationTime(speed: Float): State<Float> =
    produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis { frameTime ->
                // Reset every 24h to avoid precision issues
                value = (frameTime % maxTimeValue) / 1000f * speed
            }
        }
    }

@Composable
fun AnimatedCanvas(
    modifier: Modifier = Modifier,
    speed: Float = 1f,
    onDrawWithCache: CacheDrawScope.(time: Float) -> DrawResult
) {
    val time by produceAnimationTime(speed)
    Box(
        modifier.drawWithCache { onDrawWithCache(time) }
    )
}
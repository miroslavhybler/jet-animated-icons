@file:Suppress("RedundantVisibilityModifier")

package com.jet.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mir.oslav.jet.annotations.JetExperimental


/**
 * Helps hold state of infinite animations and helps preventing them from render errors.
 * @param iconState State of icon which you want animate infinitely
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
public class InfiniteAnimationState internal constructor(
    val iconState: AnimatedIconState
) {

    /**
     * True when infinite animation is running, false otherwise. This is used just for holding the
     * state, no functionality depends on it.
     * @since 1.1.0
     */
    var isRunning: Boolean = false
        internal set

}


/**
 * Creates instance of [InfiniteAnimationState] and remembers it. Use with combination with [InfiniteAnimationEffect].
 * @param iconState State of icon which you want animate infinitely
 * @see InfiniteAnimationEffect
 * @since 1.0.0
 */
@Deprecated(
    message = "Will be internal soon, use rememberInfiniteAnimationState(id= )",
    replaceWith = ReplaceWith(expression = "rememberInfiniteAnimationState(id= )")
)
@Composable
public fun rememberInfiniteAnimationState(
    iconState: AnimatedIconState,
): InfiniteAnimationState {

    val animationState = remember(key1 = iconState) {
        InfiniteAnimationState(iconState = iconState)
    }

    return animationState
}


@Composable
@JetExperimental
public fun rememberInfiniteAnimationState(
    @DrawableRes id: Int,
    defaultTintColor: Color = Color.Unspecified,
): InfiniteAnimationState {
    return rememberInfiniteAnimationState(
        iconState = rememberAnimatedIconState(
            id = id,
            defaultTintColor = defaultTintColor
        )
    )
}


/**
 * [LaunchedEffect] to [InfiniteAnimationState] with lifecycle handling using [LocalLifecycleOwner].</br>
 * <b>OnResume</b></br> Animation is jumped at the initialState to be sure it will be animated properly
 * when screen is resuled. As of version 1.0.0 saving the actual state of animation is not supported yet.
 * <b>OnPause</b> Animation is stopped
 * @param state State of the infinite animation
 * @param block Your infinite animation
 * @since 1.0.0
 */
@Composable
public fun InfiniteAnimationEffect(
    state: InfiniteAnimationState,
    block: suspend CoroutineScope.() -> Unit,
) {
    //To prevent block from multiple runs
    var hasRan by rememberSaveable { mutableStateOf(value = false) }
    val coroutineScope = rememberCoroutineScope()

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        coroutineScope.launch {
            state.iconState.snapToInitial()
            state.isRunning = true
            if (!hasRan) {
                hasRan = true
                block()
            }

        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
        coroutineScope.launch {
            state.isRunning = false
            state.iconState.stop()
        }
    }
}
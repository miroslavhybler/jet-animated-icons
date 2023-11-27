package mir.oslav.jet.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Helps hold state of infinite animations and helps preventing them from render errors.
 * @param isRunning True when infinite animation is running, false otherwise
 * @param iconState State of icon which you want animate infinitely
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
public class InfiniteAnimationState internal constructor(
    var isRunning: Boolean,
    val iconState: AnimatedIconState
) {

}


/**
 * Creates instance of [InfiniteAnimationState] and remembers it.
 * @param iconState State of icon which you want animate infinitely
 * @param coroutineScope Coroutine Scope used to control infinite animation lifecycle
 * @since 1.0.0
 */
@Composable
fun rememberInifiniteAnimationState(
    iconState: AnimatedIconState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): InfiniteAnimationState {

    val lifecycle = LocalLifecycleOwner.current

    val animationState = remember {
        InfiniteAnimationState(
            isRunning = false,
            iconState = iconState
        )
    }

    LaunchedEffect(key1 = lifecycle.lifecycle, block = {
        lifecycle.lifecycle.addObserver(LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    animationState.isRunning = true
                }

                Lifecycle.Event.ON_PAUSE -> {
                    coroutineScope.launch {
                        animationState.iconState.snapToInitial()
                    }
                }

                else -> Unit
            }
        })
    })

    return animationState
}
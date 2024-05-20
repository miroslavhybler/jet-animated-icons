package jet.icons.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.jet.icon.InfiniteAnimationEffect
import com.jet.icon.JetIcon
import com.jet.icon.JetImage
import com.jet.icon.rememberAnimatedIconState
import com.jet.icon.rememberInfiniteAnimationState

/**
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetIconsExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Jet Icons example")
                                }
                            )
                        }
                    ) { paddingValues ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(count = 2),
                            content = {
                                onButtonClicked()
                                infiniteColored()
                                smilingFace()
                                checkAnimation()
                                //wind()
                                swipe()
                                rotationY()
                            },
                            contentPadding = paddingValues,
                            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


fun LazyGridScope.jetIconItem(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    item(span = { GridItemSpan(currentLineSpan = 1) }) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            content()

            Spacer(modifier = Modifier.height(height = 16.dp))

            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


fun LazyGridScope.infiniteColored() {
    jetIconItem(title = "Infinite, colored") {
        val animationState = rememberInfiniteAnimationState(id = R.drawable.ic_blocks)

        InfiniteAnimationEffect(state = animationState, block = {
            while (true) {
                animationState.iconState.animatePathsIndexed { index ->
                    launch {
                        delay(timeMillis = 300L * index)
                        rotationZ.animateTo(
                            targetValue = rotationZ.value + 360f,
                            animationSpec = tween(durationMillis = 1000)
                        )
                    }
                }
                delay(timeMillis = 2400)
            }
        })

        JetImage(
            state = animationState.iconState,
            modifier = Modifier.size(size = 72.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}


fun LazyGridScope.onButtonClicked() {
    jetIconItem(title = "On Button click") {
        var isClickEnabled by remember { mutableStateOf(value = true) }
        val coroutineScope = rememberCoroutineScope()
        val iconState = rememberAnimatedIconState(
            id = R.drawable.ic_timer,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )

        JetIcon(
            state = iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
        )

        Button(
            onClick = {
                isClickEnabled = false
                coroutineScope.run {
                    launch {
                        iconState.animatePath(name = "startButtonPath") {
                            translationY.animateTo(targetValue = 8f)
                            translationY.animateTo(targetValue = 0f)
                        }
                    }
                    launch {
                        iconState.animatePath(name = "handPath") {
                            rotationZ.animateTo(
                                targetValue = rotationZ.value + 360f,
                                tween(durationMillis = 1200, easing = FastOutSlowInEasing)
                            )
                        }
                        isClickEnabled = true
                    }
                }
            },
            enabled = isClickEnabled
        ) {
            Text(text = "Click me")
        }
    }
}


fun LazyGridScope.checkAnimation() {
    jetIconItem(title = "Trim path") {

        val iconState = rememberAnimatedIconState(
            id = R.drawable.ic_check,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )

        LaunchedEffect(key1 = Unit, block = {
            delay(timeMillis = 500)
            while (true) {
                delay(timeMillis = 3000)
                launch {
                    iconState.animatePath(name = "checkPath") {
                        trimPathEnd.snapTo(targetValue = 0f)
                        delay(timeMillis = 400)
                        trimPathEnd.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 600, easing = EaseInOutExpo),
                        )
                        delay(timeMillis = 500)

                        trimPathEnd.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 600, easing = EaseInOutExpo)
                        )

                        delay(timeMillis = 900)

                    }
                }

                launch {
                    iconState.animatePath(name = "circlePath") {
                        trimPathEnd.snapTo(targetValue = 0f)
                        trimPathEnd.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 1000)
                        )

                        delay(timeMillis = 500)

                        trimPathEnd.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 1000)
                        )
                        delay(timeMillis = 500)
                    }
                }
            }
        })

        JetIcon(
            state = iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
        )
    }
}


fun LazyGridScope.rotationY() {
    jetIconItem(title = "RotationY") {
        var isClickEnabled by remember { mutableStateOf(value = true) }
        val coroutineScope = rememberCoroutineScope()
        val iconState = rememberAnimatedIconState(
            id = R.drawable.ic_post,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )

        JetIcon(
            state = iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
        )

        Button(
            onClick = {
                isClickEnabled = false
                coroutineScope.run {
                    launch {
                        iconState.animatePath(name = "signBottomPath") {
                            rotationY.animateTo(
                                targetValue = rotationY.value + 180f,
                                tween(durationMillis = 800, easing = FastOutSlowInEasing)
                            )
                        }
                    }
                    launch {
                        delay(timeMillis = 400)
                        iconState.animatePath(name = "signTopPath") {
                            rotationY.animateTo(
                                targetValue = rotationY.value + 180f,
                                tween(durationMillis = 800, easing = FastOutSlowInEasing)
                            )
                        }
                        isClickEnabled = true
                    }
                }
            },
            enabled = isClickEnabled
        ) {
            Text(text = "Click me")
        }
    }
}

fun LazyGridScope.smilingFace() {
    jetIconItem(title = "Translation and scale") {
        val iconState = rememberAnimatedIconState(
            id = R.drawable.ic_smiling_face,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )

        LaunchedEffect(key1 = Unit, block = {
            delay(timeMillis = 1000)
            while (true) {
                delay(timeMillis = 1000)
                launch {
                    iconState.animatePath(name = "leftEye") {
                        for (i in 0 until 2) {
                            translationY.animateTo(
                                targetValue = 4f,
                                animationSpec = tween(durationMillis = 200, easing = EaseInOutExpo)
                            )
                            translationY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 200, easing = EaseInOutExpo)
                            )
                        }
                    }
                }

                launch {
                    iconState.animatePath(name = "rightEye") {
                        for (i in 0 until 2) {
                            translationY.animateTo(
                                targetValue = 4f,
                                animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                            )
                            translationY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                            )
                        }
                    }
                }

                launch {
                    iconState.animatePath(name = "mouth") {
                        scaleX.animateTo(targetValue = 1.2f)
                        scaleX.animateTo(targetValue = 1f)
                    }
                }
            }
        })

        JetIcon(
            state = iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
        )
    }
}


//TODO
fun LazyGridScope.wind() {
    jetIconItem(title = "Trim paths in group with rotation") {
        val iconState = rememberAnimatedIconState(
            id = R.drawable.ic_wind_power,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )

        LaunchedEffect(key1 = Unit, block = {
            delay(timeMillis = 1000)
            launch {
                while (true) {
                    delay(timeMillis = 1200)
                    iconState.animateGroup(name = "winds") {
                        animatePathsIndexed { index ->
                            launch {
                                trimPathEnd.snapTo(targetValue = 0f)
                                delay(timeMillis = 200L * index)
                                trimPathEnd.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                trimPathEnd.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                            }
                        }
                    }
                }
            }

            launch {
                while (true) {
                    iconState.animatePath(name = "rotorPath") {
                        rotationZ.animateTo(
                            targetValue = rotationZ.value + 360f,
                            tween(durationMillis = 1000, easing = LinearEasing)
                        )
                    }
                }
            }
        })

        JetIcon(
            state = iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
        )
    }
}


fun LazyGridScope.swipe() {
    jetIconItem(title = "Custom transform origin of hand") {

        rememberAnimatedIconState(
            id = R.drawable.ic_swipe,
            defaultTintColor = MaterialTheme.colorScheme.onBackground
        )
        val animationState = rememberInfiniteAnimationState(id = R.drawable.ic_swipe)

        InfiniteAnimationEffect(state = animationState, block = {
            delay(timeMillis = 1200L)
            while (true) {
                launch {
                    animationState.iconState.animatePath(name = "hand") {
                        transformOrigin = TransformOrigin(
                            pivotFractionX = 1f,
                            pivotFractionY = 0.8f
                        )
                        rotationZ.animateTo(
                            targetValue = -30f,
                            animationSpec = tween(durationMillis = 550, easing = EaseOutBack)
                        )
                        rotationZ.animateTo(
                            targetValue = 15f,
                            animationSpec = tween(durationMillis = 550, easing = EaseOutBack)
                        )
                    }
                    delay(timeMillis = 500)
                }

                launch {
                    animationState.iconState.animatePath(name = "arrow") {
                        fillAlpha.animateTo(
                            targetValue = 0f,
                            tween(durationMillis = 550, easing = FastOutSlowInEasing)
                        )
                        fillAlpha.animateTo(
                            targetValue = 1f,
                            tween(durationMillis = 550, easing = FastOutSlowInEasing)
                        )
                    }
                    delay(timeMillis = 500)
                }
                delay(timeMillis = 1600)
            }
        })

        JetIcon(
            state = animationState.iconState,
            modifier = Modifier.size(size = 72.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}
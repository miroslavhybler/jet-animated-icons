# Jet Animated Icons

Jet Animated Icons is an experimental library for vector drawable animations. Every path from drawable has it's own painter and Icon component. Path properties are animated using [Animatable](https://developer.android.com/jetpack/compose/animation/value-based#animatable).

## Add library to your project

**Project's settings.gradle.kts**
```kotlin
// Adds maven 
dependencyResolutionManagement {
    repositories {
        maven(url = "https://jitpack.io")
    }
}
```

**Application's module build.gradle.kts**
```kotlin
dependencies {
    implementation("com.github.miroslavhybler:animated-icons:1.0.0-alpha08")
}
```

## Usage
Use _rememberAnimatedIconState_ for simple animations and _rememberInfiniteAnimationState_ 
with InfiniteAnimationEffect for infinite animations.

### Simple one time animation
```kotlin
//Scope for animations
val coroutineScope = rememberCoroutineScope()

//Remembers icon and animation state
val iconState = rememberAnimatedIconState(
    id = R.drawable.ic_timer,
    defaultTintColor = MaterialTheme.colorScheme.onBackground
)

//Draws the icon
JetIcon(
    state = iconState,
    modifier = Modifier
        .size(size = 72.dp)
        .clickable(
            onClick = {
                coroutineScope.launch {
                    // Animates path "handpats" rotation around z axis when you click the icon
                    iconState.animatePath(name = "handPath") {
                        rotationZ.animateTo(
                            targetValue = rotationZ.value + 360f,
                            tween(
                                durationMillis = 1200,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            }
        )
)
```

### Infinite Animation
```kotlin
//Remembers icon and animation state
val iconState = rememberAnimatedIconState(
    id = R.drawable.ic_timer,
    defaultTintColor = MaterialTheme.colorScheme.onBackground
)

val infiniteAnimationState = rememberInfiniteAnimationState(
    iconState = iconState
)

//Infinitely animating rotationZ value
InfiniteAnimationEffect {
    while (true) {
        iconState.animatePath(name = "handPath") {
            rotationZ.animateTo(
                targetValue = rotationZ.value + 360f,
                tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }
}

//Draws the icon
JetIcon(
    state = iconState,
    modifier = Modifier
        .size(size = 72.dp)
)
```

### Path supported animations
**Native supported path properties**
- fillAlpha
- strokeAlpha
- strokeLineWidth
- strokeLineMiter
- trimPathStart
- trimPathEnd
- trimPathOffset

**Other animations**

- tintColor
- rotationX
- rotationZ
- scaleX
- scaleY
- translationX
- translationY
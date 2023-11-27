# Jet Animated Icons

Jet Animated Icons is an experimental library for vector drawable animations. Every path from drawable has it's own painter and Icon component. Path properties are animated using [Animatable](https://developer.android.com/jetpack/compose/animation/value-based#animatable).

## Add library to your project

**Project's settings.gradle.kts**
```kotlin
// Adds maven 
dependencyResolutionManagement {
    repositories {
        maven(url = "https://maven.pkg.github.com/miroslavhybler/Maven")
    }
}
```

**Application's module build.gradle.kts**
```kotlin
dependencies {
    implementation("mir.oslav.jet:animated-icons:1.0.0-alpha01")
}
```

## Usage

```kotlin
//Scope for animations
val coroutineScope = rememberCoroutineScope()

//Remembers icon
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
                    // Animates path "handpats" rotation around z axis
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
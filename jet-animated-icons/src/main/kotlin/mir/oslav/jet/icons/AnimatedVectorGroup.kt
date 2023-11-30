package mir.oslav.jet.icons

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.ui.graphics.vector.VectorGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Represents vector group from vector drawable.
 * Animations work little different than in animated-vector drawables, all animations are relative
 * to [AnimatedVectorPath] including group animations like rotation, scale and translation. To see
 * all possible properties for animation look at [AnimatedVectorPath].
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
public class AnimatedVectorGroup internal constructor(
    internal val vectorGroup: VectorGroup,
    internal val groups: Map<String, AnimatedVectorGroup>,
    internal val paths: Map<String, AnimatedVectorPath>,
) {

    val name: String get() = vectorGroup.name


    /**
     * Animates path by given [name] calling the [block] on it. Tries to find path within this group
     * by given [name]. [animatePath] is suspend function meaning it's suspending the caller
     * [CoroutineScope] until all suspending functions within [block] are done.
     * To run multiple animations at the same time, use [CoroutineScope.launch] for every animation
     * with delaying the [CoroutineScope] to stay away from screen freeze from endless cycle.
     * @param name Name of the path you want to animate
     * @param block Block that will be called on found path to animate the found path. Suspends caller
     * [CoroutineScope] during the animation.
     * @since 1.0.0
     */
    public suspend fun animatePath(
        name: String,
        block: suspend AnimatedVectorPath.() -> Unit
    ) {
        val path = paths[name]

        if (path != null) {
            block(path)
        } else {
            Log.w(
                "JetIconState",
                "Path with name \"$name\" was not found in group ${name}, animation will not do anything"
            )
        }
    }


    /**
     * Animates all paths from this group by calling [block] on them. [animatePaths] is suspend
     * function meaning it's suspending the caller [CoroutineScope] until all suspending functions
     * within [block] are done. To run multiple animations at the same time, use [CoroutineScope.launch]
     * for every animation with delaying the [CoroutineScope] to stay away from screen freeze from
     * endless cycle.
     * @param block Block that will be called on found path to animate the found path. Suspends caller
     * [CoroutineScope] during the animation.
     * @since 1.0.0
     */
    public suspend fun animatePaths(
        block: suspend AnimatedVectorPath.() -> Unit
    ) {
        paths.values.forEach { path ->
            block(path)
        }
    }


    /**
     * Animates all paths from this group by calling [block] on them. [animatePathsIndexed] is suspend
     * function meaning it's suspending the caller [CoroutineScope] until all suspending functions
     * within [block] are done. To run multiple animations at the same time, use [CoroutineScope.launch]
     * for every animation with delaying the [CoroutineScope] to stay away from screen freeze from
     * endless cycle.
     * @param block Block that will be called on found path to animate the found path. Suspends caller
     * [CoroutineScope] during the animation.
     * @since 1.0.0
     */
    public suspend fun animatePathsIndexed(
        block: suspend AnimatedVectorPath.(index: Int) -> Unit
    ) {
        paths.values.forEachIndexed { index, path ->
            block(path, index)
        }
    }

    /**
     * Tries to find path within this group by given [name]
     * @param name Name of the path
     * @throws NullPointerException When path was not found within this group
     * @return Found path by [name]
     * @since 1.0.0
     */
    public fun getPath(name: String): AnimatedVectorPath {
        val path = paths[name]

        require(value = path != null, lazyMessage = {
            "Path \"$name\" was not found in group \"${name}\""
        })

        return path
    }


    /**
     * Tries to find group within this group by given [name]
     * @param name Name of the group
     * @throws NullPointerException When group was not found within this group
     * @return Found path by [name]
     * @since 1.0.0
     */
    public fun getGroup(name: String): AnimatedVectorGroup {
        val group = groups[name]

        require(value = group != null, lazyMessage = {
            "Group \"$name\" was not found in group \"${name}\""
        })

        return group
    }
}
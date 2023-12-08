package mir.oslav.jet.icons

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mir.oslav.jet.annotations.JetExperimental


/**
 * State of [AnimatedIcon], can be created with [rememberAnimatedIconState] using vector drawable resource id.
 * Animations work little different than in animated-vector drawables, all animations are relative
 * to [AnimatedVectorPath] including group animations like rotation, scale and translation. To see
 * all possible properties for animation look at [AnimatedVectorPath].
 * @param animatedIcon Icon which state is hold
 * @param original Original image vector
 * @see rememberAnimatedIconState
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
//TODO geters for paths and groups
public class AnimatedIconState internal constructor(
    internal val animatedIcon: AnimatedIcon,
    internal val original: ImageVector
) {

    /**
     * Animates path by given [name] calling the [block] on it. Tries to find path within the [animatedIcon]
     * by given [name] including also all paths in all groups. [animatePath] is suspend function meaning
     * it's suspending the caller [CoroutineScope] until all suspending functions within [block] are done.
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
        val path = animatedIcon.allPaths[name]

        if (path != null) {
            block(path)
        } else {
            Log.w(
                "JetIconState",
                "Path with name \"$name\" was not found in icon ${original.name}, animation will not do anything"
            )
        }
    }


    /**
     * Animates all paths from [animatedIcon] by calling [block] on them. [animatePaths] is suspend
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
        animatedIcon.allPaths.values.forEach { path ->
            block(path)
        }
    }

    /**
     * Animates all paths from [animatedIcon] including all paths from all groups by calling [block] on them.
     * [animatePathsIndexed] is suspend function meaning it's suspending the caller [CoroutineScope]
     * until all suspending functions within [block] are done. To run multiple animations at the same
     * time, use [CoroutineScope.launch] for every animation with delaying the [CoroutineScope] to
     * stay away from screen freeze from endless cycle.
     * @param block Block that will be called on found path to animate the found path. Suspends caller
     * [CoroutineScope] during the animation.
     * @since 1.0.0
     */
    public suspend fun animatePathsIndexed(
        block: suspend AnimatedVectorPath.(index: Int) -> Unit
    ) {
        animatedIcon.allPaths.values.forEachIndexed { index, path ->
            block(path, index)
        }
    }


    /**
     * Tries to find path within this icon by given [name]
     * @param name Name of the path
     * @throws NullPointerException When path was not found within this icon
     * @return Found path by [name]
     * @since 1.0.0
     */
    public fun getPath(name: String): AnimatedVectorPath {
        val path = animatedIcon.allPaths[name]

        require(value = path != null, lazyMessage = {
            "Path \"$name\" was not found in icon \"${original.name}\""
        })

        return path
    }


    /**
     * Tries to find an [AnimatedVectorGroup] by [name] and calls [block] on it.
     * Animations work little different than in animated-vector drawables, all animations are relative
     * to [AnimatedVectorPath] including group animations like rotation, scale and translation. To see
     * all possible properties for animation look at [AnimatedVectorPath].
     * @since 1.0.0
     */
    public suspend fun animateGroup(
        name: String,
        block: suspend AnimatedVectorGroup.() -> Unit
    ) {
        val group = animatedIcon.allGroups[name]

        if (group != null) {
            block(group)
        } else {
            Log.w(
                "JetIconState",
                "Group with name \"$name\" was not found in icon ${original.name}, animation will not do anything"
            )
        }
    }


    /**
     * Tries to find group within this icon by given [name]
     * @param name Name of the group
     * @throws NullPointerException When group was not found within this icon
     * @return Found path by [name]
     * @since 1.0.0
     */
    public fun getGroup(name: String): AnimatedVectorGroup {
        val group = animatedIcon.allGroups[name]

        require(value = group != null, lazyMessage = {
            "Group \"$name\" was not found in icon \"${original.name}\""
        })

        return group
    }


    /**
     * Snaps all animations of all paths in [animatedIcon] to the target state.
     * @since 1.0.0
     */
    public suspend fun snapToTarget() {
        animatedIcon.snapToTargets()
    }


    /**
     * Snaps all animations (excepts [AnimatedVectorPath.tintColor]) of all paths within this icon
     * to the initial state.
     * @since 1.0.0
     */
    public suspend fun snapToInitial() {
        animatedIcon.snapToInitial()
    }


    /**
     * Stops all the running animations on this path
     * @since 1.0.0
     */
    public suspend fun stop() {
        animatedIcon.stop()
    }
}


/**
 * Creates instance of [AnimatedIconState] using vector drawable [id] and remembers it. Animations work
 * a little different than in animated-vector drawables, all animations are relative to [AnimatedVectorPath]
 * including group animations like rotation, scale and translation. To see all possible properties
 * for animation look at [AnimatedVectorPath].
 * This is experimental and need to be opt-in.
 * @since 1.0.0
 */
@Composable
@JetExperimental
public fun rememberAnimatedIconState(
    @DrawableRes id: Int,
    defaultTintColor: Color = Color.Unspecified
): AnimatedIconState {
    val imageVector = ImageVector.vectorResource(id = id)

    return remember(key1 = id) {
        val jetIcon = getJetIconData(
            rootVectorGroup = imageVector.root,
            defaultTintColor = defaultTintColor
        )
        AnimatedIconState(
            animatedIcon = jetIcon,
            original = imageVector
        )
    }
}


/**
 * Composable component to draw your [AnimatedIcon] using [AnimatedIconState]. Creates an [Icon] component
 * for every path from [AnimatedIcon] and draws it with [VectorPainter] with applied [AnimatedVectorPath.tintColor].
 * @param modifier
 * @param state
 * @since 1.0.0
 */
//TODO enable default tint
@Composable
@JetExperimental
public fun JetIcon(
    modifier: Modifier = Modifier,
    state: AnimatedIconState,
) {
    Box(modifier = modifier) {
        state.animatedIcon.allPaths.forEach { (_, path) ->
            val imageVector = state.original
            val painter = rememberVectorPainter(
                defaultWidth = imageVector.defaultWidth,
                defaultHeight = imageVector.defaultHeight,
                viewportWidth = imageVector.viewportWidth,
                viewportHeight = imageVector.viewportHeight,
                autoMirror = false,
                content = { w, h ->
                    RenderAnimatedVectorPath(animatedPath = path)
                }
            )
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .jetIconModifier(path = path),
                tint = path.tintColor.value
            )
        }
    }
}


/**
 * Composable component to draw your [AnimatedIcon] using [AnimatedIconState]. Creates an [Image] component
 * for every path from [AnimatedIcon] and draws it with [VectorPainter].
 * @param modifier
 * @param state
 * @since 1.0.0
 */
@Composable
@JetExperimental
public fun JetImage(
    modifier: Modifier = Modifier,
    state: AnimatedIconState,
    colorFilter: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.None
) {
    Box(modifier = modifier) {
        state.animatedIcon.allPaths.forEach { (name, path) ->
            val imageVector = state.original
            //Each path has its own painter
            val painter = rememberVectorPainter(
                defaultWidth = imageVector.defaultWidth,
                defaultHeight = imageVector.defaultHeight,
                viewportWidth = imageVector.viewportWidth,
                viewportHeight = imageVector.viewportHeight,
                autoMirror = false,
                content = { w, h ->
                    RenderAnimatedVectorPath(animatedPath = path)
                }
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .jetIconModifier(path = path),
                colorFilter = colorFilter,
                contentScale = contentScale
            )
        }
    }
}


/**
 * Queries all paths and groups from [rootVectorGroup] and creates [AnimatedIcon]
 * @since 1.0.0
 * @return Animated icon instance
 */
private fun getJetIconData(
    rootVectorGroup: VectorGroup,
    defaultTintColor: Color
): AnimatedIcon {
    val rootGroups = ArrayList<AnimatedVectorGroup>()
    val rootPaths = ArrayList<AnimatedVectorPath>()

    val allGroups = ArrayList<AnimatedVectorGroup>()
    val allPaths = ArrayList<AnimatedVectorPath>()

    rootVectorGroup.forEach { vectorNode ->
        when (vectorNode) {
            is VectorPath -> {
                val path = AnimatedVectorPath(
                    vectorPath = vectorNode,
                    defaultTintColor = defaultTintColor
                )
                rootPaths.add(element = path)
                allPaths.add(element = path)
            }

            is VectorGroup -> {
                val paths = getPaths(
                    vectorGroup = vectorNode,
                    defaultTintColor = defaultTintColor,
                    allPathsList = allPaths
                )
                val group = AnimatedVectorGroup(
                    vectorGroup = vectorNode,
                    groups = getGroups(
                        vectorGroup = vectorNode,
                        allGroupsList = allGroups,
                        allPathsList = allPaths,
                        defaultTintColor = defaultTintColor
                    ).associateBy(AnimatedVectorGroup::name),
                    paths = paths.associateBy(AnimatedVectorPath::name)
                )

                allGroups.add(element = group)
                rootGroups.add(element = group)
            }
        }
    }

    return AnimatedIcon(
        root = AnimatedVectorGroup(
            vectorGroup = rootVectorGroup,
            paths = rootPaths.associateBy(AnimatedVectorPath::name),
            groups = rootGroups.associateBy(AnimatedVectorGroup::name),
        ),
        allPaths = allPaths.associateBy(AnimatedVectorPath::name),
        allGroups = allGroups.associateBy(AnimatedVectorGroup::name),
    )
}


private fun getPaths(
    vectorGroup: VectorGroup,
    defaultTintColor: Color,
    allPathsList: MutableList<AnimatedVectorPath>,
): List<AnimatedVectorPath> {
    val outList = ArrayList<AnimatedVectorPath>()

    vectorGroup.forEach { vectorNode ->
        if (vectorNode is VectorPath) {
            outList.add(
                element = AnimatedVectorPath(
                    defaultTintColor = defaultTintColor,
                    vectorPath = vectorNode
                )
            )
        }
    }
    allPathsList.addAll(elements = outList)
    return outList
}


private fun getGroups(
    vectorGroup: VectorGroup,
    defaultTintColor: Color,
    allGroupsList: MutableList<AnimatedVectorGroup>,
    allPathsList: MutableList<AnimatedVectorPath>
): List<AnimatedVectorGroup> {
    val outList = ArrayList<AnimatedVectorGroup>()

    vectorGroup.forEach { vectorNode ->
        if (vectorNode is VectorGroup) {
            val paths = getPaths(
                vectorGroup = vectorGroup,
                defaultTintColor = defaultTintColor,
                allPathsList = allPathsList
            )

            val groups = getGroups(
                vectorGroup = vectorNode,
                allGroupsList = allGroupsList,
                allPathsList = allPathsList,
                defaultTintColor = defaultTintColor
            )

            val group = AnimatedVectorGroup(
                groups = groups.associateBy(AnimatedVectorGroup::name),
                paths = paths.associateBy(AnimatedVectorPath::name),
                vectorGroup = vectorNode
            )
            outList.add(element = group)
            allGroupsList.add(element = group)
        }
    }

    return outList
}


/**
 * Applies graphicsLayer effects on the receiving Modifier
 * @return Modifier
 * @since 1.0.0
 */
private fun Modifier.jetIconModifier(
    path: AnimatedVectorPath
): Modifier = this.graphicsLayer(
    rotationX = path.rotationX.value,
    rotationY = path.rotationY.value,
    rotationZ = path.rotationZ.value,
    scaleX = path.scaleX.value,
    scaleY = path.scaleY.value,
    translationX = path.translationX.value,
    translationY = path.translationY.value,
    shadowElevation = 0f, //  path.shadowElevation.value,
    transformOrigin = path.transformOrigin,
    compositingStrategy = CompositingStrategy.ModulateAlpha
)
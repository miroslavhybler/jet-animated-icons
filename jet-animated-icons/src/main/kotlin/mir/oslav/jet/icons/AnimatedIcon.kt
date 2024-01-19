package mir.oslav.jet.icons


/**
 * Represents a vector drawable.
 * Animations work little different than in animated-vector drawables, all animations are relative
 * to [AnimatedVectorPath] including group animations like rotation, scale and translation. To see
 * all possible properties for animation look at [AnimatedVectorPath]. Always remember that all paths
 * and groups must have name.
 * @param root Root group of vector drawable
 * @param allPaths All paths extracted from [root] and from [allGroups] in vector drawable, associated by
 * path name.
 * @param allGroups All groups from vector drawable (excepts [root]) associated by group name.
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 26.11.2023
 */
internal class AnimatedIcon internal constructor(
    internal val root: AnimatedVectorGroup,
    internal val allPaths: Map<String, AnimatedVectorPath>,
    internal val allGroups: Map<String, AnimatedVectorGroup>,
) {


    /**
     * Snaps all animations of all paths within this icon to the target state.
     * @since 1.0.0
     */
    internal suspend fun snapToTargets() {
        allPaths.forEach { (_, path) -> path.snapToTarget() }
    }


    /**
     * Snaps all animations (excepts [AnimatedVectorPath.tintColor]) of all paths within this icon
     * to the initial state.
     * @since 1.0.0
     */
    internal suspend fun snapToInitial() {
        allPaths.forEach { (_, path) -> path.snapToInitialValues() }
    }


    /**
     * Stops all the running animations on this icon
     * @since 1.0.0
     */
    internal suspend fun stop() {
        allPaths.forEach { (_, path) -> path.stop() }
    }
}
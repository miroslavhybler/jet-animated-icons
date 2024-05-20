package com.jet.icon

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.toPath


/**
 * Represents a vector path with attributes as [Animatable]'s.
 * Animations work little different than in animated-vector drawables, all animations are relative
 * to [AnimatedVectorPath] including group animations like rotation, scale and translation.
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 23.11.2023
 */
public class AnimatedVectorPath internal constructor(
    defaultTintColor: Color,
    internal val vectorPath: VectorPath,
) {


    companion object {
        private val tempPath: Path = Path()
    }

    /**
     * Animates fill alpha natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val fillAlpha = Animatable(initialValue = vectorPath.fillAlpha)


    /**
     * Animates stroke alpha natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val strokeAlpha = Animatable(initialValue = vectorPath.strokeAlpha)

    /**
     * Animates stroke line width natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val strokeLineWidth = Animatable(initialValue = vectorPath.strokeLineWidth)

    /**
     * Animates stroke line miter natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val strokeLineMiter = Animatable(initialValue = vectorPath.strokeLineMiter)

    /**
     * Animates trim path start natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val trimPathStart = Animatable(initialValue = vectorPath.trimPathStart)

    /**
     * Animates trim path end natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val trimPathEnd = Animatable(initialValue = vectorPath.trimPathEnd)

    /**
     * Animates trim path offset natively by calling [RenderAnimatedVectorPath] and [Path].
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0,
     */
    @FloatRange(from = 0.0, to = 1.0)
    val trimPathOffset = Animatable(initialValue = vectorPath.trimPathOffset)


    /**
     * Animates tint color of the path.
     * If you want to change it immediately, use [Animatable.snapTo].
     * @since 1.0.0
     */
    val tintColor = androidx.compose.animation.Animatable(initialValue = defaultTintColor)


    /**
     * Transform origin for other animations that are not supported natively for [VectorPath].
     * @since 1.0.0
     */
    var transformOrigin: TransformOrigin = TransformOrigin.Center


    /**
     * Rotation around x axis from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [rotationX] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val rotationX = Animatable(initialValue = 0f)


    /**
     * Rotation around y axis from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [rotationY] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val rotationY = Animatable(initialValue = 0f)


    /**
     * Rotation around z axis from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [rotationZ] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val rotationZ = Animatable(initialValue = 0f)


    /**
     * Scale x from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [scaleX] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val scaleX = Animatable(initialValue = 1f)


    /**
     * Scale y from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [scaleY] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val scaleY = Animatable(initialValue = 1f)


    /**
     * Translation x from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [translationX] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val translationX = Animatable(initialValue = 0f)


    /**
     * Translation y from [transformOrigin] point.
     * Animation is done by calling [Modifier.jetIconModifier] and [Modifier.graphicsLayer] with
     * passing [translationX] value.
     * @see Modifier.jetIconModifier
     * @since 1.0.0
     */
    val translationY = Animatable(initialValue = 0f)

    //TODO shadowElevation
    // val shadowElevation = Animatable(initialValue = 0f)


    val name: String get() = vectorPath.name


    /**
     * All property animations will jump to target state immediately
     * @since 1.0.0
     */
    suspend fun snapToTarget() {
        if (fillAlpha.value != fillAlpha.targetValue) {
            fillAlpha.snapTo(targetValue = fillAlpha.targetValue)
        }
        if (strokeAlpha.value != strokeAlpha.targetValue) {
            strokeAlpha.snapTo(targetValue = strokeAlpha.targetValue)
        }
        if (strokeLineWidth.value != strokeLineWidth.targetValue) {
            strokeLineWidth.snapTo(targetValue = strokeLineWidth.targetValue)
        }
        if (strokeLineMiter.value != strokeLineMiter.targetValue) {
            strokeLineMiter.snapTo(targetValue = strokeLineMiter.targetValue)
        }
        if (trimPathStart.value != trimPathStart.targetValue) {
            trimPathStart.snapTo(targetValue = trimPathStart.targetValue)
        }
        if (trimPathEnd.value != trimPathEnd.targetValue) {
            trimPathEnd.snapTo(targetValue = trimPathEnd.targetValue)
        }
        if (trimPathOffset.value != trimPathOffset.targetValue) {
            trimPathOffset.snapTo(targetValue = trimPathOffset.targetValue)
        }
        if (tintColor.value != tintColor.targetValue) {
            tintColor.snapTo(targetValue = tintColor.targetValue)
        }
        if (rotationX.value != rotationX.targetValue) {
            rotationX.snapTo(targetValue = rotationX.targetValue)
        }
        if (rotationY.value != rotationY.targetValue) {
            rotationY.snapTo(targetValue = rotationY.targetValue)
        }
        if (rotationZ.value != rotationZ.targetValue) {
            rotationZ.snapTo(targetValue = rotationZ.targetValue)
        }
        if (scaleX.value != scaleX.targetValue) {
            scaleX.snapTo(targetValue = scaleX.targetValue)
        }
        if (scaleY.value != scaleY.targetValue) {
            scaleY.snapTo(targetValue = scaleY.targetValue)
        }
        if (translationX.value != translationX.targetValue) {
            translationX.snapTo(targetValue = translationX.targetValue)
        }
        if (translationY.value != translationY.targetValue) {
            translationY.snapTo(targetValue = translationY.targetValue)
        }
        /*
        if (shadowElevation.value != shadowElevation.targetValue) {
            shadowElevation.snapTo(targetValue = shadowElevation.targetValue)
        }
         */
    }


    /**
     * All property animations (excepts [AnimatedVectorPath.tintColor]) will jump to initial state immediately
     * @since 1.0.0
     */
    suspend fun snapToInitialValues() {
        if (fillAlpha.value != vectorPath.fillAlpha) {
            fillAlpha.snapTo(targetValue = vectorPath.fillAlpha)
        }
        if (strokeAlpha.value != vectorPath.strokeAlpha) {
            strokeAlpha.snapTo(targetValue = vectorPath.strokeAlpha)
        }
        if (strokeLineWidth.value != vectorPath.strokeLineWidth) {
            strokeLineWidth.snapTo(targetValue = vectorPath.strokeLineWidth)
        }
        if (strokeLineMiter.value != vectorPath.strokeLineMiter) {
            strokeLineMiter.snapTo(targetValue = vectorPath.strokeLineMiter)
        }
        if (trimPathStart.value != vectorPath.trimPathStart) {
            trimPathStart.snapTo(targetValue = vectorPath.trimPathStart)
        }
        if (trimPathEnd.value != vectorPath.trimPathEnd) {
            trimPathEnd.snapTo(targetValue = vectorPath.trimPathEnd)
        }
        if (trimPathOffset.value != vectorPath.trimPathOffset) {
            trimPathOffset.snapTo(targetValue = vectorPath.trimPathOffset)
        }
        if (tintColor.value != tintColor.targetValue) {
            tintColor.snapTo(targetValue = tintColor.targetValue)
        }
        if (rotationX.value != 0f) {
            rotationX.snapTo(targetValue = 0f)
        }
        if (rotationY.value != 0f) {
            rotationY.snapTo(targetValue = 0f)
        }
        if (rotationZ.value != 0f) {
            rotationZ.snapTo(targetValue = 0f)
        }
        if (scaleX.value != 1f) {
            scaleX.snapTo(targetValue = 1f)
        }
        if (scaleY.value != 1f) {
            scaleY.snapTo(targetValue = 1f)
        }
        if (translationX.value != 0f) {
            translationX.snapTo(targetValue = 0f)
        }
        if (translationY.value != 0f) {
            translationY.snapTo(targetValue = 0f)
        }
        /*
        if (shadowElevation.value != shadowElevation.targetValue) {
            shadowElevation.snapTo(targetValue = shadowElevation.targetValue)
        }
         */
    }


    /**
     * Stops all the running animations on this path
     * @since 1.0.0
     */
    suspend fun stop() {
        fillAlpha.stop()
        strokeAlpha.stop()
        strokeLineWidth.stop()
        strokeLineMiter.stop()
        trimPathStart.stop()
        trimPathEnd.stop()
        trimPathOffset.stop()

        tintColor.stop()
        rotationX.stop()
        rotationZ.stop()
        scaleX.stop()
        scaleY.stop()
        translationX.stop()
        translationY.stop()
    }


    /**
     * @return Center point of this vector path
     * @since 1.0.0
     */
    fun getCenter(): Offset {
        tempPath.reset()
        vectorPath.pathData.toPath(target = tempPath)
        return tempPath.getBounds().center
    }
}
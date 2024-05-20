package com.jet.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath


/**
 * @since 1.0.0
 */
@Composable
@VectorComposable
internal fun RenderAnimatedVectorNode(
    group: VectorGroup,
    jetIcon: AnimatedIcon
) {
    for (vectorNode in group) {
        when (vectorNode) {
            is VectorPath -> {
                val animatedPath = jetIcon.allPaths[vectorNode.name]
                if (animatedPath != null) {
                    RenderAnimatedVectorPath(animatedPath = animatedPath)
                } else {
                    //TODO log or throw
                }
            }

            is VectorGroup -> {
                val animatedGroup = jetIcon.allGroups[vectorNode.name]
                if (animatedGroup != null) {
                    RenderAnimatedVectorGroup(
                        vectorGroup = animatedGroup,
                        jetIcon = jetIcon
                    )
                } else {
                    //TODO log or throw
                }
            }
        }
    }
}


/**
 * Draws [VectorPath] from [animatedPath] using [animatedPath]'s properties.
 * @since 1.0.0
 */
@Composable
@VectorComposable
internal fun RenderAnimatedVectorPath(
    animatedPath: AnimatedVectorPath
) {
    val vectorPath = animatedPath.vectorPath
    Path(
        pathData = vectorPath.pathData,
        pathFillType = vectorPath.pathFillType,
        name = vectorPath.name,
        fill = vectorPath.fill,
        fillAlpha = animatedPath.fillAlpha.value,
        stroke = vectorPath.stroke,
        strokeLineCap = vectorPath.strokeLineCap,
        strokeLineJoin = vectorPath.strokeLineJoin,
        strokeAlpha = animatedPath.strokeAlpha.value,
        strokeLineWidth = animatedPath.strokeLineWidth.value,
        strokeLineMiter = animatedPath.strokeLineMiter.value,
        trimPathStart = animatedPath.trimPathStart.value,
        trimPathEnd = animatedPath.trimPathEnd.value,
        trimPathOffset = animatedPath.trimPathOffset.value,
    )
}


/**
 * @since 1.0.0
 */
@Composable
@VectorComposable
internal fun RenderAnimatedVectorGroup(
    vectorGroup: AnimatedVectorGroup,
    jetIcon: AnimatedIcon,
) {
    val originalGroup = vectorGroup.vectorGroup
    Group(
        name = originalGroup.name,
        rotation = originalGroup.rotation,
        scaleX = originalGroup.scaleX,
        scaleY = originalGroup.scaleY,
        translationX = originalGroup.translationX,
        translationY = originalGroup.translationY,
        pivotX = originalGroup.pivotX,
        pivotY = originalGroup.pivotY,
        clipPathData = originalGroup.clipPathData
    ) {
        RenderAnimatedVectorNode(
            group = originalGroup,
            jetIcon = jetIcon
        )
    }
}
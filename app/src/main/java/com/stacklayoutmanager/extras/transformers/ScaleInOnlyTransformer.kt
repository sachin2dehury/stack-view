package com.stacklayoutmanager.extras.transformers

import android.graphics.Path
import android.view.View
import com.stacklayoutmanager.StackLayoutManager
import com.stacklayoutmanager.extras.layoutinterpolators.FreePathInterpolator

/**
 * This transformer scales the view only during entry
 *
 * Preview: https://raw.githubusercontent.com/DarkionAvey/StackLayoutManager/master/Showcase/gifs/scale_in_transform.webp
 */
object ScaleInOnlyTransformer {
    private val scalePath =
        FreePathInterpolator(
            Path().apply {
                //0.7f is the minimum scale
                moveTo(0f, 0.7f)
                lineTo(1f, 1f)
            })

    fun transform(x: Float, v: View, stackLayoutManager: StackLayoutManager) {
        StackLayoutManager.ElevationTransformer.transform(x, v, stackLayoutManager)
        val scale = if (x <= 0f) 1f else scalePath.getInterpolation(1f - kotlin.math.abs(x))
        v.scaleX = scale
        v.scaleY = scale
    }
}
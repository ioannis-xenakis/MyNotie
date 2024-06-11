package com.code_that_up.john_xenakis.my_notie.utils

import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath

/**
 * Corner treatment(different corner shape) for each note.
 */
class NoteCornerTreatment: CornerTreatment() {

    override fun getCornerPath(
        shapePath: ShapePath,
        angle: Float,
        interpolation: Float,
        radius: Float
    ) {
        shapePath.reset(0f, radius * interpolation, 180f, 180 - angle)
        shapePath.addArc(
            0f,
            0f,
            130f * interpolation,
            130f * interpolation,
            180f,
            -270f
        )
    }

}
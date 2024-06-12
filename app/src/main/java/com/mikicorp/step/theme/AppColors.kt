package com.mikicorp.step.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    primary: Color,
) {
    var primary by mutableStateOf(primary)
        private set

    fun copy(
        primary: Color = this.primary,
    ): AppColors = AppColors(
        primary
    )
    
    // will be explained later
    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
    }
}

internal val LocalColors = staticCompositionLocalOf { AppColors(Color.Blue) }
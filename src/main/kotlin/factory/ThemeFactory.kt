package factory

import androidx.compose.ui.graphics.Color

interface ThemeFactory {

    fun backgroundColor(): Color

    fun cardColor(): Color

    fun buttonColor(): Color

    fun textFieldColor(): Color

    fun priorityHighColor(): Color

    fun priorityMediumColor(): Color

    fun priorityLowColor(): Color
}
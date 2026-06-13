package factory

import androidx.compose.ui.graphics.Color

class DarkThemeFactory : ThemeFactory {

    override fun backgroundColor() =
        Color(0xFF101418)

    override fun cardColor() =
        Color(0xFF1B222A)

    override fun buttonColor() =
        Color(0xFF60A5FA)

    override fun textFieldColor() =
        Color(0xFF202A33)

    override fun priorityHighColor() =
        Color(0xFFEF5350)

    override fun priorityMediumColor() =
        Color(0xFFFFB74D)

    override fun priorityLowColor() =
        Color(0xFF81C784)
}

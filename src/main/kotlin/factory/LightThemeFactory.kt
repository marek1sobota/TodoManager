package factory

import androidx.compose.ui.graphics.Color

class LightThemeFactory : ThemeFactory {

    override fun backgroundColor() =
        Color(0xFFF4F6F8)

    override fun cardColor() =
        Color(0xFFFFFFFF)

    override fun buttonColor() =
        Color(0xFF2563EB)

    override fun textFieldColor() =
        Color(0xFFFFFFFF)

    override fun priorityHighColor() =
        Color.Red

    override fun priorityMediumColor() =
        Color(0xFFFF9800)

    override fun priorityLowColor() =
        Color(0xFF4CAF50)
}

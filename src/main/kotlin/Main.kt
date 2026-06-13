import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import factory.ThemeManager
import ui.MainScreen

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Todo Manager"
    ) {

        var darkMode by remember {
            mutableStateOf(
                ThemeManager.isDarkMode()
            )
        }

        MaterialTheme(

            colors = if (darkMode)
                darkColors()
            else
                lightColors()

        ) {

            MainScreen(

                darkMode = darkMode,

                themeFactory =
                    ThemeManager.currentFactory(),

                onThemeChange = {

                    ThemeManager.toggleTheme()

                    darkMode =
                        ThemeManager.isDarkMode()
                }
            )
        }
    }
}
package factory

object ThemeManager {

    private var darkMode = false

    fun toggleTheme() {
        darkMode = !darkMode
    }

    fun currentFactory(): ThemeFactory {

        return if (darkMode)
            DarkThemeFactory()
        else
            LightThemeFactory()
    }

    fun isDarkMode(): Boolean =
        darkMode
}
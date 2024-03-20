import io.github.alexzhirkevich.cupertino.adaptive.Theme

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun determineTheme(): Theme

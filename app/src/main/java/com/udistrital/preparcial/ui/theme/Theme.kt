package com.udistrital.preparcial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LumiLightColorScheme = lightColorScheme(
    primary = VerdeLumi,
    onPrimary = Color.White,
    primaryContainer = VerdeSuave,
    onPrimaryContainer = VerdeOscuro,
    background = GrisFondo,
    surface = BlancoTarjeta,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro,
    outline = BordeSuave
)

@Composable
fun PreparcialTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LumiLightColorScheme,
        typography = Typography,
        content = content
    )
}
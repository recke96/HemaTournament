package info.marozzo.tournament.desktop.components.util

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState

enum class WidthClass {

    Unspecified, Compact, Medium, Expanded;

    companion object {
        fun get(width: Dp) = when (width) {
            in Compact -> Compact
            in Medium -> Medium
            in Expanded -> Expanded
            else -> Unspecified
        }
    }

    operator fun contains(width: Dp): Boolean = when (this) {
        Compact -> width < 600.dp
        Medium -> 600.dp < width && width < 840.dp
        Expanded -> width > 840.dp
        Unspecified -> false
    }
}

@Composable
fun rememberWidthClass(width: Int?) = with(LocalDensity.current) {
    remember(this, width) {
        derivedStateOf { WidthClass.get(width?.toDp() ?: Dp.Unspecified) }
    }
}

val LocalWidthClass = compositionLocalOf { WidthClass.Unspecified }

@Composable
fun Responsive(
    compact: (@Composable () -> Unit)? = null,
    medium: (@Composable () -> Unit)? = null,
    expanded: (@Composable () -> Unit)? = null
): Unit {

}

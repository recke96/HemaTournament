package info.marozzo.tournament.desktop.components.util

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

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
        Medium -> 600.dp <= width && width < 840.dp
        Expanded -> 840.dp <= width
        Unspecified -> false
    }
}

@Composable
fun FrameWindowScope.WithWidthClass(content: @Composable () -> Unit) = with(LocalDensity.current) {
    val (width, setWidth) = remember { mutableStateOf<Int?>(null) }

    DisposableEffect(Unit) {
        val listener = object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                setWidth(e.component.size.width)
            }
        }
        window.addComponentListener(listener)

        onDispose { window.removeComponentListener(listener) }
    }

    val widthClass by remember(this, width) {
        derivedStateOf { WidthClass.get(width?.toDp() ?: Dp.Unspecified) }
    }

    CompositionLocalProvider(LocalWidthClass provides widthClass) {
        content()
    }
}

val LocalWidthClass = compositionLocalOf { WidthClass.Unspecified }

@Composable
fun Responsive(
    compact: (@Composable () -> Unit)? = null,
    medium: (@Composable () -> Unit)? = null,
    expanded: (@Composable () -> Unit)? = null
) = when (LocalWidthClass.current) {
    WidthClass.Compact -> compact?.invoke()
    WidthClass.Medium -> medium?.invoke() ?: compact?.invoke()
    WidthClass.Expanded -> expanded?.invoke() ?: medium?.invoke() ?: compact?.invoke()
    WidthClass.Unspecified -> Unit
}

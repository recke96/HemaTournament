package info.marozzo.tournament.desktop.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <E> Select(
    value: E,
    onValueChanged: (E) -> Unit,
    options: ImmutableList<E>,
    renderValue: @Composable (E) -> Unit = { Text(it.toString()) },
    modifier: Modifier = Modifier,
) {
    val (isOpen, setIsOpen) = remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isOpen) 180f else 0f)

    Button(onClick = { setIsOpen(!isOpen) }, modifier = modifier) {
        renderValue(value)
        Icon(
            Icons.Default.ArrowDropDown,
            contentDescription = if (isOpen) "Close drop-down" else "Open drop-down",
            modifier = Modifier.rotate(rotation)
        )
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { setIsOpen(false) },
        ) {
            Column {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            if (option != value) {
                                onValueChanged(option)
                                setIsOpen(false)
                            }
                        },
                    ) {
                        renderValue(option)
                    }
                }
            }
        }
    }
}

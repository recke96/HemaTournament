package info.marozzo.tournament.desktop.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import info.marozzo.tournament.core.domain.Name

@Composable
fun NameComponent(name: Name?, default: String = "") = Text(text = name?.value ?: default)

package info.marozzo.tournament.desktop.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import info.marozzo.tournament.desktop.i18n.AppStrings

internal enum class Screen(
    val icon: ImageVector,
    val iconSelected: ImageVector = icon,
    val label: AppStrings.() -> String
) {
    HOME(Icons.Outlined.Home, Icons.Filled.Home, { navigation.home }),
    SETTINGS(Icons.Outlined.Settings, Icons.Filled.Settings, { navigation.settings })
}

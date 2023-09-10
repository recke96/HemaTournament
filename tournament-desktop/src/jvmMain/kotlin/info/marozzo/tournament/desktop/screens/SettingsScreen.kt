package info.marozzo.tournament.desktop.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import info.marozzo.tournament.desktop.i18n.LocalStrings
import info.marozzo.tournament.desktop.application.stores.application.ApplicationIntent
import info.marozzo.tournament.desktop.application.stores.application.ApplicationState
import info.marozzo.tournament.desktop.i18n.AppStrings

@Composable
internal fun SettingsScreen(state: ApplicationState, accept: (ApplicationIntent) -> Unit) {
    val strings: AppStrings = LocalStrings.current
    val titleMediumDp = with(LocalDensity.current) { MaterialTheme.typography.titleMedium.fontSize.toDp() }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Card(modifier = Modifier.fillMaxHeight().widthIn(max = 900.dp).padding(16.dp).verticalScroll(rememberScrollState())) {

            Text(strings.settings.title, style = MaterialTheme.typography.titleLarge)
            Divider()

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.Language,
                    modifier = Modifier.size(titleMediumDp).align(Alignment.CenterVertically),
                    contentDescription = strings.settings.languageLabel
                )
                Spacer(Modifier.widthIn(10.dp, 25.dp))
                Text(strings.settings.languageLabel, style = MaterialTheme.typography.titleMedium)
            }
            Divider()
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.Palette,
                    modifier = Modifier.size(titleMediumDp).align(Alignment.CenterVertically),
                    contentDescription = strings.settings.themeLabel
                )
                Spacer(Modifier.widthIn(10.dp, 25.dp))
                Text(strings.settings.themeLabel, style = MaterialTheme.typography.titleMedium)
                Text(strings.settings.themeText(state.theme))
            }
        }
    }
}

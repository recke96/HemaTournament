package info.marozzo.tournament.desktop.i18n

internal object Locales {
    const val EN = "en"
    const val DE = "de"
}

internal interface AppStrings {
    val appTitle: String
    val common: Common
    val closeDialog: CloseDialog
}

internal interface Common {
    val ok: String
    val cancel: String
}

internal interface CloseDialog {
    val title: String
    val text: String
}

package info.marozzo.tournament.desktop.i18n

enum class LanguageTag(private val tag: String?) {
    SYSTEM(null), DE("de"), EN("en");

    override fun toString() = tag ?: "<SYSTEM>"
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

package info.marozzo.tournament.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = EnStrings.TAG, default = true)
internal val EnAppStrings: AppStrings = EnStrings

private object EnStrings : AppStrings {
    const val TAG = "en"

    override val appTitle = "Tournament Planner"

    override val common = object : AppStrings.Common {
        override val ok = "Ok"
        override val cancel = "Cancel"
    }

    override val navigation = object : AppStrings.Navigation {
        override val home: String = "Home"
        override val settings: String = "Settings"
    }

    override val closeDialog = object : AppStrings.CloseDialog {
        override val title = "Close $appTitle?"
        override val text = "Do you really want to close the application?"
    }
}

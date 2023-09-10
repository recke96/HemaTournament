package info.marozzo.tournament.desktop.application.dirs

import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Path

typealias FilePath = Path

internal interface AppDirsContext {
    val dataDir: FilePath
}

internal suspend fun <T> withAppDirs(block: suspend AppDirsContext.() -> T): T {
    val dirs = AppDirsFactory.getInstance()

    val context = object : AppDirsContext {
        override val dataDir: FilePath =
            dirs.getUserDataDir(
                "tournament-planner",
                "1.0.0",
                "recke96",
                true
            ).let(FilePath::of)
    }

    println(context.dataDir)

    return context.block()
}


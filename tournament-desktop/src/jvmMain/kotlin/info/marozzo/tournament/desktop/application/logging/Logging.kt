package info.marozzo.tournament.desktop.application.logging

import arrow.fx.coroutines.ResourceScope
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.JsonEncoder
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.spi.ContextAwareBase
import info.marozzo.tournament.desktop.application.dirs.AppDirsContext
import info.marozzo.tournament.desktop.application.dirs.FilePath
import org.slf4j.Logger
import org.slf4j.LoggerFactory

context (ResourceScope, AppDirsContext)
suspend fun <T> withLogging(block: suspend () -> T): T {
    val ctx = install(
        { LoggerFactory.getILoggerFactory() as LoggerContext },
        { lc, _ -> lc.stop() }
    )

    ctx.reset()
    ApplicationLoggingConfigurator(dataDir).configure(ctx)

    return block()
}


private class ApplicationLoggingConfigurator(private val logDir: FilePath) : ContextAwareBase(), Configurator {
    override fun configure(context: LoggerContext): Configurator.ExecutionStatus {
        addInfo("Configuring")

        val infoFilter = ThresholdFilter().apply {
            this.context = context
            setLevel("INFO")
            start()
        }

        val patternEncoder = PatternLayoutEncoder().apply {
            this.context = context
            pattern = "%date{HH:mm:ss.SSS} %.-3level %logger{15} %message %ex{short}%n"
            start()
        }

        val jsonEncoder = JsonEncoder().apply {
            this.context = context
            start()
        }

        val console = ConsoleAppender<ILoggingEvent>().apply {
            name = "Console"
            this.context = context
            encoder = patternEncoder
            start()
        }

        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
            this.context = context
            fileNamePattern = "${logDir.toAbsolutePath()}/%d{yyyy-MM-dd}_logs.ndjson"
            maxHistory = 7
            isCleanHistoryOnStart = true
        }

        val file = RollingFileAppender<ILoggingEvent>().apply {
            name = "File"
            this.context = context
            addFilter(infoFilter)
            encoder = jsonEncoder
            this.rollingPolicy = rollingPolicy.also {
                it.setParent(this)
                it.start()
            }
            start()
        }

        context.getLogger(Logger.ROOT_LOGGER_NAME).apply {
            level = Level.DEBUG
            addAppender(console)
            addAppender(file)
        }

        return Configurator.ExecutionStatus.NEUTRAL
    }

}

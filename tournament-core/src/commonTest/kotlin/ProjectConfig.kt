import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter
import kotlinx.collections.immutable.persistentListOf

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {

    override fun extensions() = persistentListOf(
        JunitXmlReporter(
            includeContainers = false,
            useTestPathAsName = true
        ),
        HtmlReporter()
    )
}

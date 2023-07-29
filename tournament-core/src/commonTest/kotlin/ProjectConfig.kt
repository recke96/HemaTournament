import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {

    override fun extensions(): List<Extension> = listOf(
        MarkdownReporter()
    )

}

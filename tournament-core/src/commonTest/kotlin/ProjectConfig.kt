import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import kotlinx.collections.immutable.persistentListOf

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {

    override fun extensions() = persistentListOf<Extension>()
}

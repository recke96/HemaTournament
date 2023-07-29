import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.Path
import kotlin.io.path.writeText
import kotlin.reflect.KClass

class MarkdownReporter : FinalizeSpecListener, AfterProjectListener {

    private val successes = AtomicInteger(0)
    private val ignored = AtomicInteger(0)
    private val errors = AtomicInteger(0)
    private val failures = AtomicInteger(0)

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) {
        successes.addAndGet(results.values.filter { it.isSuccess }.size)
        ignored.addAndGet(results.values.filter { it.isIgnored }.size)
        errors.addAndGet(results.values.filter { it.isError }.size)
        failures.addAndGet(results.values.filter { it.isFailure }.size)
    }

    override suspend fun afterProject() {
        val path = Path("./build/reports/summary.md")
        path.parent.toFile().mkdirs()

        path.writeText(
            """
                # Test Summary
                
                ```mermaid
                %%{init: {'theme': 'base', 'themeVariables': { 'pie1': '#32CD32', 'pie2': '#FFEA00', 'pie3': '#E60000', 'pie4': '#FF1A1A'}}}%%
                pie showData
                    title Test Results
                    "Success" : ${successes.get()}
                    "Ignored" : ${ignored.get()}
                    "Errors" : ${errors.get()}
                    "Failures": ${failures.get()}
                ```
            """.trimIndent()
        )
    }


}

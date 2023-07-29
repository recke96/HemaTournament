import io.kotest.core.config.ProjectConfiguration
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.FinalizeSpecListener
import io.kotest.core.project.ProjectContext
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.engine.test.names.formatTestPath
import io.kotest.engine.test.names.getDisplayNameFormatter
import java.nio.file.Path
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.writeBytes
import kotlin.reflect.KClass

enum class Summary {
    TABLE, MERMAID_PIE_CHART
}

enum class Details {
    SUCCESS, IGNORED, ERROR, FAILURE
}

class MarkdownSummaryReporter(
    private val useTestPathAsName: Boolean = true,
    private val combineErrorAndFailure: Boolean = false,
    private val summaries: Set<Summary> = setOf(Summary.MERMAID_PIE_CHART),
    private val details: Set<Details> = setOf(Details.IGNORED, Details.ERROR, Details.FAILURE),
    private val outputFile: String = "reports/summary.md",
    private val outputFileSizeLimit: Int = DEFAULT_SIZE_LIMIT,
) : ProjectExtension, FinalizeSpecListener, AfterProjectListener {

    companion object {
        const val DEFAULT_BUILD_DIR = "./build"
        const val BUILD_DIR_KEY = "gradle.build.dir"
        const val DEFAULT_SIZE_LIMIT = 1 shl 20
    }

    private val successes = ConcurrentLinkedQueue<Pair<TestCase, TestResult.Success>>()
    private val ignored = ConcurrentLinkedQueue<Pair<TestCase, TestResult.Ignored>>()
    private val errors = ConcurrentLinkedQueue<Pair<TestCase, TestResult.Error>>()
    private val failures = ConcurrentLinkedQueue<Pair<TestCase, TestResult.Failure>>()

    private lateinit var config: ProjectConfiguration

    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {
        this.config = context.configuration
        callback(context)
    }

    override suspend fun finalizeSpec(kclass: KClass<out Spec>, results: Map<TestCase, TestResult>) =
        results.filterKeys { it.type != TestType.Container }.forEach { (case, result) ->
            when (result) {
                is TestResult.Success -> successes.offer(case to result)
                is TestResult.Ignored -> ignored.offer(case to result)
                is TestResult.Error -> errors.offer(case to result)
                is TestResult.Failure -> failures.offer(case to result)
            }
        }

    override suspend fun afterProject() {
        val path = outputFile()
        path.parent.toFile().mkdirs()

        val summary = buildString {
            appendLine("# Test Summary")
            appendLine()

            if (summaries.contains(Summary.MERMAID_PIE_CHART)) {
                append(mermaidPieChart())
                appendLine()
            }
            if (summaries.contains(Summary.TABLE)) {
                append(tableSummary())
                appendLine()
            }

            if (combineErrorAndFailure && (details.contains(Details.ERROR) || details.contains(Details.FAILURE))) {
                appendLine(details("Errors & Failures", errors + failures))
            }

            if (!combineErrorAndFailure && details.contains(Details.ERROR)) {
                appendLine(details("Errors", errors))
            }

            if (!combineErrorAndFailure && details.contains(Details.FAILURE)) {
                appendLine(details("Failures", failures))
            }

            if (details.contains(Details.IGNORED)) {
                appendLine(details("Ignored", ignored))
            }

            if (details.contains(Details.SUCCESS)) {
                appendLine(details("Successes", successes))
            }
        }

        val bytes = summary.encodeToByteArray().let {
            if (it.size < outputFileSizeLimit) it else it.copyOf(outputFileSizeLimit)
        }

        path.writeBytes(bytes)
    }

    private fun outputFile(): Path {
        val buildDir = System.getProperty(BUILD_DIR_KEY) ?: DEFAULT_BUILD_DIR
        return Path(buildDir, outputFile).normalize().absolute()
    }

    private fun mermaidPieChart() = when (combineErrorAndFailure) {
        true -> """
            ```mermaid
            %%{init: {'theme': 'base', 'themeVariables': { 'pie1': '#32CD32', 'pie2': '#FFEA00', 'pie3': '#E60000', 'pie4': '#FF1A1A'}}}%%
            pie showData
                title Test Results
                "Success" : ${successes.size}
                "Ignored" : ${ignored.size}
                "Errors & Failures" : ${errors.size + failures.size}
            ```
        """.trimIndent()

        false -> """
            ```mermaid
            %%{init: {'theme': 'base', 'themeVariables': { 'pie1': '#32CD32', 'pie2': '#FFEA00', 'pie3': '#E60000', 'pie4': '#FF1A1A'}}}%%
            pie showData
                title Test Results
                "Success" : ${successes.size}
                "Ignored" : ${ignored.size}
                "Errors" : ${errors.size}
                "Failures": ${failures.size}
            ```
        """.trimIndent()
    }

    private fun tableSummary() = when (combineErrorAndFailure) {
        true -> """
            | Status | Count |
            | ------ | ----- |
            | Success | ${successes.size} |
            | Ignored | ${ignored.size} |
            | Errors & Failures | ${errors.size + failures.size} |
        """.trimIndent()

        false -> """
            | Status | Count |
            | ------ | ----- |
            | Success | ${successes.size} |
            | Ignored | ${ignored.size} |
            | Errors | ${errors.size} |
            | Failures | ${failures.size} |
        """.trimIndent()
    }

    private fun details(heading: String, results: Iterable<Pair<TestCase, TestResult>>) = buildString {
        if (results.none()) {
            return@buildString
        }

        appendLine("<details>")
        appendLine("<summary><strong>$heading</strong></summary>")
        appendLine()

        appendLine("| Test | Duration | Message |")
        appendLine("| :--: | :------: | :------ |")

        val formatter = getDisplayNameFormatter(config.registry, config)
        for ((case, result) in results) {
            val test = if (useTestPathAsName) formatter.formatTestPath(case, "--") else formatter.format(case)

            appendLine("| $test | ${result.duration} | ${result.message()} |")
        }

        appendLine("</details>")
    }

    private fun TestResult.message() = when (this) {
        is TestResult.Success -> "Ok"
        is TestResult.Ignored -> reason
        is TestResult.Error -> cause.message ?: ""
        is TestResult.Failure -> cause.message ?: ""
    }
}

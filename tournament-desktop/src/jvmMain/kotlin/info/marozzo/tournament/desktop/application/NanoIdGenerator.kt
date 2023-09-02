package info.marozzo.tournament.desktop.application

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import java.security.SecureRandom
import kotlin.math.ceil
import kotlin.math.log2

typealias RandomByteGenerator = (Int) -> ByteArray

val defaultAlphabet = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

private val defaultRandom = SecureRandom()

val defaultRandomByteGenerator: RandomByteGenerator =
    { size -> ByteArray(size).apply(defaultRandom::nextBytes) }

@JvmInline
value class NanoId(val id: String)

sealed class NanoIdError(val message: String)
data class InvalidAlphabet(val size: Int) :
    NanoIdError("Alphabet must contain between 1 and 255 symbols, but contained $size.")

data class InvalidSize(val size: Int) :
    NanoIdError("Size must be greater than 0, but was $size")


fun generateNanoId(
    size: Int = 10,
    random: RandomByteGenerator = defaultRandomByteGenerator,
    alphabet: CharArray = defaultAlphabet
): Either<NonEmptyList<NanoIdError>, NanoId> = either {
    zipOrAccumulate(
        { ensure(alphabet.size in 1..255) { InvalidAlphabet(alphabet.size) } },
        { ensure(size > 0) { InvalidSize(size) } }
    ) { _, _ ->
        NanoId(buildString {
            val mask = ((2 shl log2(alphabet.size - 1f).toInt()) - 1)
            val step = ceil(1.6f * mask * size / alphabet.size).toInt()

            while (length < size) {
                val bytes = random(step)

                for (i in 0 until step) {
                    val idx = bytes[i].toInt() and mask

                    if (idx < alphabet.size) {
                        append(alphabet[idx])
                    }
                }
            }
        })
    }
}

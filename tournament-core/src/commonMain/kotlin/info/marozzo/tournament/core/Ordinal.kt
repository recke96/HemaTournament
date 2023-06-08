package info.marozzo.tournament.core

/**
 * [Ordinal] represent a ranking.
 *
 * They can be ordered ([compared][compareTo]), but not used for calculations!
 */
@JvmInline
value class Ordinal private constructor(private val number: Int) : Comparable<Ordinal> {

    companion object {
        /**
         * First valid [Ordinal].
         */
        val first = Ordinal(1)

        /**
         * Infinite [Sequence] of [Ordinals][Ordinal].
         */
        val sequence = generateSequence(first) { it.next() }
    }

    init {
        require(number > 0) { "Ordinal must be greater then 0, but was $number" }
    }

    /**
     * Get the next [Ordinal].
     */
    fun next() = Ordinal(number + 1)

    /**
     * Compares this [Ordinal] to the [other] [Ordinal].
     *
     * @return Returns zero if they are of the same rank,
     * a negative number if this [Ordinal] is of a lesser rank,
     * or a positive number if it's of a higher rank.
     */
    override fun compareTo(other: Ordinal) = number.compareTo(other.number)

    override fun toString() = number.toString()
}

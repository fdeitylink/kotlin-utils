package io.fdeitylink.util

import java.util.EnumSet

fun <E : Enum<E>> encode(set: EnumSet<E>): Long {
    var flags = 0L
    set.forEach { flags = flags or (1L shl it.ordinal) }
    return flags
}

inline fun <reified E : Enum<E>> decode(encoded: Long): EnumSet<E> {
    /*
     * Assumes at most 64 values in the enum class (64 bits in a long)
     * http://stackoverflow.com/a/2199486
     */

    //TODO: Throw exception if there are more than 64 values?

    val set = EnumSet.noneOf(E::class.java)
    var ordinal = 0

    val constants: Array<out E> = E::class.java.enumConstants

    /*
     * Bitshift through every constant in the enum
     * and check the flag in the encoded Long. If it
     * is set, then add the enum constant to the set
     */

    var i = 1L
    while (i != 0L && ordinal < constants.size) {
        if (0L != (i and encoded)) {
            set.add(constants[ordinal])
        }
        i = i shl 1
        ordinal++
    }

    return set
}
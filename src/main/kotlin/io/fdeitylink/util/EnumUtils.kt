package io.fdeitylink.util

import java.util.EnumSet

import kotlin.reflect.KClass

//TODO: Make the following two methods regular, non-extension methods?
fun <E> EnumSet<E>.encoded(): Long where E: Enum<E>, E: SafeEnum<E> {
    var flags = 0L
    for (e in this) {
        flags = flags or (1L shl e.ordinal)
    }

    return flags
}

fun <E> Long.decoded(enumClass: KClass<E>): EnumSet<E> where E: Enum<E>, E: SafeEnum<E> {
    /*
     * Assumes at most 64 values in the enum class (64 bits in a long)
     * http://stackoverflow.com/a/2199486
     */

    //TODO: Throw exception if there are more than 64 values?

    val set = EnumSet.noneOf(enumClass.java)
    var ordinal = 0

    val constants: Array<out E> = enumClass.java.enumConstants

    /*
     * Bitshift through every constant in the enum
     * and check the flag in the encoded Long. If it
     * is set, then add the enum constant to the set
     */

    //TODO: Possible to do this as a for loop?
    var i = 1L
    while (i != 0L && ordinal < constants.size) {
        if (0L != (i and this)) {
            set.add(constants[ordinal])
        }
        i = i shl 1
        ordinal++
    }

    return set
}
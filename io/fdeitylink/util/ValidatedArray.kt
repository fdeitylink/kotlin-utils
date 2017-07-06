package io.fdeitylink.util

import java.util.Arrays

/*
 * TODO:
 * Allow specifying lazyMessage for when validator fails?
 * Add forEach/iteration methods
 * Allow _backing to be a primitive Array (e.g. IntArray)
 *  - Create separate classes? Sealed classes?
 */
class ValidatedArray<T>(private val _backing: Array<T>, private val validator: (Int, T) -> Boolean) {
    val size get() = _backing.size

    val indices = _backing.indices

    //TODO: Rename to something else that indicates it is a separate Array, not a ValidatedArray
    val copy: Array<T>
        get() = Arrays.copyOf(_backing, _backing.size)

    companion object {
        inline operator fun <reified T> invoke(size: Int,
                                               noinline init: (Int) -> T,
                                               noinline validator: (Int, T) -> Boolean) =
                ValidatedArray(Array(size, init), validator)
    }

    operator fun get(index: Int) = _backing[index]

    operator fun set(index: Int, value: T) {
        require(validator(index, value)) { "$value is not a valid value for this array" }
        _backing[index] = value
    }

    //TODO: Make both of the following inline
    fun forEach(action: (T) -> Unit) = _backing.forEach(action)

    fun forEachIndexed(action: (Int, T) -> Unit) = _backing.forEachIndexed(action)
}
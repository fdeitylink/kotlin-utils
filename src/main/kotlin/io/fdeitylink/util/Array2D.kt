package io.fdeitylink.util

import java.util.Arrays

/**
 * A class that simplifies implementing a rectangular two-dimensional array so as
 * to avoid confusing code that uses `Array<Array<T>>`.
 *
 * An `Array2D` should be considered as a series of _rows of elements_ rather than a
 * series of columns of elements, which matters for iterating over it, methods like
 * `get(y: Int)` and the primary constructor's parameters. Given the following 2D array
 *
 * ```
 * //Java
 * int[][] arr = {{0,  1,  2},
 *                {3,  4,  5},
 *                {6,  7,  8},
 *                {9, 10, 11}};
 * ```
 *
 * the height would be considered `4` and width `3`, and any iterating would pass through
 * 0, 1, and 2 before 3, 4, and 5, and so on.
 *
 * @param T the type of the element to be stored by this `Array2D`
 *
 * @constructor
 * Constructs a new `Array2D`, given its [width], [height], and a [backing] 2D array.
 *
 * This constructor is useful for turning a regular Java 2D array (or `Array<Array<T>>`)
 * into an `Array2D`. For example, the following would create an `Array2D` with `regArray`
 * as its backing list.
 * ```
 * //Java
 * String[][] regArray = {{"Hello", "World"}, {"Alice", "Bob"}};
 * Array2D<String> array2D = new Array2D<String>(regArray[0].length, regArray.length, regArray);
 * ```
 *
 * Most of the time, however, the [emptyArray2D], [array2DOfNulls], and [invoke] methods should
 * be used for constructing a new `Array2D`.
 *
 * @throws IllegalArgumentException if [backing] is not a purely rectangular array (the widths
 * of its rows are inconsistent).
 *
 * @property width The width of this `Array2D`.
 *
 * @property height The height of this `Array2D`.
 *
 * @property backing The backing `Array<Array<T>>` of this `Array2D`. It can be used for
 * methods that expect a standard Java 2D array (or Kotlin methods not using this class).
 * For example, the following would give `methodTakingStandard2DArray` direct access to
 * the contents of this `Array2D` (thereby also allowing it to modify elements).
 * ```
 * //Kotlin
 * methodTakingStandard2DArray(anArray2D.backing)
 * ```
 */
//TODO: Allow backing to be an Array of primitive arrays (e.g. Array<IntArray>)
class Array2D<T>(val backing: Array<Array<T>>) {
    val height = backing.size
    val width = if (backing.isEmpty()) 0 else backing[0].size

    init {
        if (backing.isNotEmpty()) {
            var prevWidth = backing[0].size

            for (row in backing) {
                require(row.size == prevWidth)
                { "width of all rows must be equivalent (current width: ${row.size}, previous width: $prevWidth)" }
                prevWidth = row.size
            }
        }
    }

    /*
     * TODO:
     * Provide iterator() method and properties that standard Array has
     */

    companion object {
        /**
         * Constructs an `Array2D<T>` with the given [width] and [height], whose elements
         * are filled via the [init] function. [init] takes the x and y coordinates of the
         * element to be constructed and returns that element. Has the same purpose as the
         * [Array] constructor.
         *
         * This method allows one to write code such as the following.
         * ```
         * Array2D(width, height) {...}
         * ```
         * Although appearing equivalent to a constructor call, it is not entirely
         * equivalent. The reason for using the `invoke` operator rather than a constructor
         * is that a constructor could not be used since [T] is a `reified` parameter.
         * Because [T] is `reified`, this method cannot be called from Java code. In such
         * cases where an `Array2D` must be constructed in Java, the primary constructor
         * for this class should be used.
         */
        //TODO: make init noinline?
        inline operator fun <reified T> invoke(width: Int, height: Int, init: (x: Int, y: Int) -> (T)) =
                Array2D(Array(height, { y -> Array(width, { x -> init(x, y) }) }))

        /**
         * Returns a [String] representation of [array2D] equivalent to [Arrays.deepToString].
         */
        fun toString(array2D: Array2D<*>): String = Arrays.deepToString(array2D.backing)
    }

    /**
     * Returns the row stored by this `Array2D` with index [y].
     *
     * As an operator, this method can also be called with the index operator:
     * ```
     * val row = array2D[y]
     * ```
     *
     * @throws IndexOutOfBoundsException if [y] is outside the bounds of this `Array2D`.
     */
    operator fun get(y: Int) = backing[y]

    /**
     * Returns the element stored by this `Array2D` with coordinates ([x], [y]).
     *
     * As an operator, this method can also be called with the index operator:
     * ```
     * val value = array2D[x, y]
     * ```
     *
     * @throws IndexOutOfBoundsException if [x] or [y] are outside the bounds of this `Array2D`.
     */
    operator fun get(x: Int, y: Int) = backing[y][x]

    /**
     * Sets the element in this `Array2D` with coordinates ([x], [y]) to [value]. As an operator,
     * this method can also be called with the index operator:
     * ```
     * array2D[x, y] = value
     * ```
     *
     * @throws IndexOutOfBoundsException if [x] or [y] are outside the bounds of this `Array2D`.
     */
    operator fun set(x: Int, y: Int, value: T) {
        backing[y][x] = value
    }

    /**
     * Calls [action] on each element. Note that it will iterate through each element of each _row_,
     * not of each column.
     */
    inline fun forEach(action: (T) -> Unit) = backing.forEach { it.forEach { action.invoke(it) } }

    /**
     * Calls [action] on each element, giving it the element as well as its x and y coordinates. Note
     * that it will iterate through each element of each _row_, not of each column. In other words,
     * x will increase before y does.
     */
    inline fun forEachIndexed(action: (x: Int, y: Int, T) -> Unit) =
            backing.forEachIndexed { y, row -> row.forEachIndexed { x, value -> action.invoke(x, y, value) } }

    /**
     * Returns a new `Array2D` with the same elements as this one.
     */
    inline fun <reified T> clone() = Array2D(this.width, this.height) { x, y -> this[x, y] as T }
}

/**
 * Constructs an `Array2D<T>` with a `width` and `height` of `0`. Has the same purpose
 * as [emptyArray].
 */
inline fun <reified T> emptyArray2D() = Array2D(Array(0, { emptyArray<T>() }))

/**
 * Constructs an `Array2D<T>` with the given [width] and [height], with a backing
 * array whose elements are all `null`. Has the same purpose as [arrayOfNulls].
 */
inline fun <reified T> array2DOfNulls(width: Int, height: Int) = Array2D(Array(height, { arrayOfNulls<T>(width) }))

/**
 * Constructs an `Array2D<T>` with the given [width] and [elements]. Has the same
 * purpose as [arrayOf].
 *
 * If the width were `3`, for example, then the first three elements given would
 * go into the first row of the `Array2D`, the next three the second row, and so on.
 *
 * @throws IllegalArgumentException if [elements.size][Array.size] is not a multiple
 * of [width].
 */
inline fun <reified T> array2DOf(width: Int, vararg elements: T): Array2D<T> {
    require(0 == elements.size % width)
    { "elements.size must be divisible by width (width: $width, size: ${elements.size})" }

    return Array2D(width, elements.size / width) { x, y -> elements[(width * y) + x] }
}
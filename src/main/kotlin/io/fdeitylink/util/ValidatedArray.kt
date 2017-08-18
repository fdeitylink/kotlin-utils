/*
 * Copyright 2017 Brian Christian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    val size = _backing.size

    val indices get() = _backing.indices

    val backingClone: Array<T>
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
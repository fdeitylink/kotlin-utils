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

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.function.UnaryOperator

open class ValidatedObservableList<E>
private constructor(private val delegate: ObservableList<E>,
                    private val lazyMessage: (E) -> Any,
                    private val validator: (E) -> Boolean
                   ): ObservableList<E> by delegate {
    constructor(lazyMessage: (E) -> Any = { "Invalid element attempted to be added to list (value: $it)" },
                validator: (E) -> Boolean
               ): this(FXCollections.observableArrayList(), lazyMessage, validator)

    override fun add(element: E): Boolean {
        require(validator(element), { lazyMessage(element) })
        return delegate.add(element)
    }

    override fun add(index: Int, element: E) {
        require(validator(element), { lazyMessage(element) })
        return delegate.add(index, element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        for (e in elements) {
            require(validator(e), { lazyMessage(e) })
        }
        return delegate.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        for (e in elements) {
            require(validator(e), { lazyMessage(e) })
        }
        return delegate.addAll(elements)
    }

    override fun replaceAll(operator: UnaryOperator<E>) {
        for (e in this) {
            require(validator(operator.apply(e)), { lazyMessage(e) })
        }
        return delegate.replaceAll(operator)
    }

    override fun addAll(vararg elements: E): Boolean {
        for (e in elements) {
            require(validator(e), { lazyMessage(e) })
        }
        return delegate.addAll(elements.toList())
    }

    override fun set(index: Int, element: E): E {
        require(validator(element), { lazyMessage(element) })
        return delegate.set(index, element)
    }

    override fun setAll(col: MutableCollection<out E>): Boolean {
        for (e in col) {
            require(validator(e), { lazyMessage(e) })
        }
        return delegate.setAll(col)
    }

    override fun setAll(vararg elements: E): Boolean {
        for (e in elements) {
            require(validator(e), { lazyMessage(e) })
        }
        return delegate.setAll(elements.toList())
    }
}
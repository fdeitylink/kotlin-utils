package io.fdeitylink.util

import java.nio.file.Path
import java.nio.file.Files

import java.util.function.UnaryOperator

import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * Returns the filename of this [Path] sans the ending [extension]
 *
 * @receiver a [Path] that represents a file
 *
 * @throws IllegalArgumentException if the receiving [Path] does not
 * represent a file
 */
fun Path.baseFilename(extension: String = "."): String {
    require(!Files.isDirectory(this)) { "Receiver Path $this is not a file" }

    val fname = this.fileName.toString()
    val extIndex = fname.lastIndexOf(extension)

    return if (-1 == extIndex) fname else fname.substring(0, extIndex)
}

/**
 * Executes the given [block] function on the receiving resource and then
 * closes it down correctly whether an exception is thrown or not. Ripped
 * from the [Closeable][java.io.Closeable] extension method defined in [kotlin.io].
 *
 * @param block a function to process this [AutoCloseable] resource.
 * @return the result of [block] function invoked on this resource.
 */
inline fun <T: AutoCloseable, R> T.use(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    }
    catch (except: Exception) {
        closed = true
        try {
            this.close()
        }
        catch (closeExcept: Exception) {
            except.addSuppressed(closeExcept)
        }
        throw except
    }
    finally {
        if (!closed) {
            this.close()
        }
    }
}

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
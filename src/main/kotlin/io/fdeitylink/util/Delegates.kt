package io.fdeitylink.util

import kotlin.properties.ReadWriteProperty
import kotlin.properties.ObservableProperty

import kotlin.reflect.KProperty

/**
 * A property delegate that emulates
 * [Delegates.notNull][kotlin.properties.Delegates.notNull] while also
 * allowing delegated properties to ensure that their values are only set
 * to proper values so that invariants can be maintained. As such, any
 * attempts to get the value of the delegated property before it is
 * initialized will throw an [IllegalStateException]. Additionally, any
 * attempts to set the value of the delegated property to an invalid value
 * (checked via a function passed to the constructor for this class) will
 * result in an [IllegalArgumentException]. This essentially emulates
 * having a non-default setter for the delegated property while providing
 * the features of the delegate returned by
 * [Delegates.notNull][kotlin.properties.Delegates.notNull].
 *
 * @param T the type of the delegated property
 *
 * @constructor
 *
 * @param validator a function that will be called to ensure that values
 * given to set() are valid for the delegated property. Its single parameter
 * is the value passed to set() that needs to be validated. If calling [validator]
 * with the value returns true, the given value is valid and will be set, otherwise
 * the value is invalid and an [IllegalArgumentException] will be thrown.
 *
 * @param lazyMessage a supplier function that returns an [Any] whose [toString]
 * method will be called to provide a message for any [IllegalArgumentException]s
 * thrown when a value passed to set() is invalid. Appended to that string will be
 * the string " (value: $value)" so the caller knows what was wrong with their input.
 * Defaults to a function returning the String "Invalid value passed to set()".
 */
/*
 * TODO:
 * Turn validator: (T) -> Boolean into onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean
 * Either create NotNullVetoable class or add throwExcept: Boolean  = true to the constructor
 * Turn into CustomNotNullVar class that takes a setter validator and a custom getter?
 * Delegate to Delegates.notNull?
 */
class ValidatedNotNullVar<T : Any>(private val validator: (value: T) -> Boolean,
                                   private val lazyMessage: () -> Any = { "Invalid value passed to set()" }
) : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        //TODO: Add name of class/object to exception string
        return value ?: throw IllegalStateException("Property ${property.name} should be initialized before get.")
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        require(validator(value)) { lazyMessage().toString() + " (value: $value)" }
        this.value = value
    }
}

class ReInitializableVar<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Property ${property.name} should be initialized before get.")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class ObservableValue<T>(initialValue: T) : ObservableProperty<T>(initialValue) {
    private val listeners: MutableList<ChangeListener<T>> = mutableListOf()

    fun addListener(listener: ChangeListener<T>) {
        listeners.add(listener)
    }

    fun removeListener(listener: ChangeListener<T>) {
        listeners.remove(listener)
    }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            listeners.forEach { it(property, oldValue, newValue) }
}

typealias ChangeListener<T> = (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
package io.fdeitylink.util

/**
 * Marker interface for [Enum]s whose ordinals can be relied on to
 * remain unchanged. Useful for [Enum]s that are used for array or
 * list indexes.
 *
 * @param E the [Enum] class being implemented by this interface
 */
@Suppress("unused")
interface SafeEnum<E : Enum<E>>
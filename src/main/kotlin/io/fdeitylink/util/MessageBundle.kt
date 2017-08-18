package io.fdeitylink.util

import java.util.ResourceBundle

class MessageBundle(val bundleName: String) {
    private val resourceBundle = ResourceBundle.getBundle(bundleName)

    init {
        allBundles[bundleName] = this
    }

    /**
     * Returns a [String] that corresponds to [key] in the resource bundle specified
     * by [bundleName], or [key] if there is no corresponding [String]
     */
    operator fun get(key: String): String = if (resourceBundle.containsKey(key)) resourceBundle.getString(key) else key

    companion object {
        /**
         * A [HashMap] mapping each instantiated [MessageBundle's][MessageBundle] [bundleName] to itself
         */
        val allBundles = hashMapOf<String, MessageBundle>()

        /**
         * Returns a [String] that corresponds to [key] in the resource bundle specified by [bundleName],
         * or [key] if no [MessageBundle] exists with the given [bundleName] or if no [String] corresponds
         * to [key] in an existing [MessageBundle]
         */
        operator fun get(bundleName: String, key: String) = allBundles[bundleName]?.get(key) ?: key
    }
}
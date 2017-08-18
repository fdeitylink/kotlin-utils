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

import java.util.ResourceBundle
import java.util.MissingResourceException

/**
 * Wraps a [ResourceBundle] and provides a convenient [get] operator method.
 *
 * The class also suppresses [ClassCastExceptions][ClassCastException] and
 * [MissingResourceExceptions][MissingResourceException] should they occur when attempting to retrieve a bundle
 * or one of its values.
 *
 * @constructor
 * Constructs a new [MessageBundle] that wraps a [ResourceBundle] with the given [bundleName]
 *
 * If no bundle exists for the given [bundleName], then the get [instance method][get] and
 * [companion object method][MessageBundle.Companion.get] will always return the key they are given
 */
class MessageBundle(val bundleName: String)  {
    @Suppress("JoinDeclarationAndAssignment")
    private val resourceBundle: ResourceBundle?

    init {
        resourceBundle = try {
            ResourceBundle.getBundle(bundleName)
        }
        catch (except: MissingResourceException) {
            null
        }

        _bundles[bundleName] = this
    }

    /**
     * Returns a [String] that corresponds to [key] in the resource bundle specified by [bundleName],
     * or [key] if no [ResourceBundle] with the name [bundleName] exists or there is no corresponding
     * [String] in the [ResourceBundle] that does exist
     */
    operator fun get(key: String): String {
        return try {
            if (true == resourceBundle?.containsKey(key)) resourceBundle.getString(key) else key
        }
        catch (except: ClassCastException) {
            key
        }
    }

    companion object {
        @JvmStatic
        private val _bundles = hashMapOf<String, MessageBundle>()

        /**
         * A [Map] mapping each instantiated [MessageBundle's][MessageBundle] [bundleName] to itself
         */
        @JvmStatic
        val bundles: Map<String, MessageBundle>
            get() = _bundles

        /**
         * Returns a [String] that corresponds to [key] in the resource bundle specified by [bundleName],
         * or [key] if no [ResourceBundle] with the name [bundleName] exists or there is no corresponding
         * [String] in the [ResourceBundle] that does exist
         */
        @JvmStatic
        operator fun get(bundleName: String, key: String) = bundles[bundleName]?.get(key) ?: key
    }
}
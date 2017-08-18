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
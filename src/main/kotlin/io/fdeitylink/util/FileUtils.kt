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

@file:JvmName("UtilsKt")
@file:JvmMultifileClass

package io.fdeitylink.util

import java.nio.charset.Charset

import java.nio.file.Path
import java.nio.file.Files

import java.io.IOException

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
 * Returns the contents of the file pointed to by the receiving [Path] as a [String],
 * or an empty [String] if the file [is not a regular file][Files.isRegularFile].
 *
 * @throws IOException if any IO error occurs while opening or reading the file
 */
@Throws(IOException::class)
fun Path.getContents(charset: Charset = Charsets.UTF_8): String {
    val result = StringBuilder()

    if (Files.isRegularFile(this)) {
        Files.newBufferedReader(this, charset).use {
            val buf = CharArray(DEFAULT_BUFFER_SIZE) { 0.toChar() }
            var read = it.read(buf)

            while (read >= 0) {
                result.append(String(buf, 0, read))
                read = it.read(buf)
            }
        }
    }

    return result.toString()
}

/**
 * Executes the given [block] function on the receiving resource and then
 * closes it down correctly whether an exception is thrown or not. Ripped
 * from the [Closeable][java.io.Closeable] extension method defined in [kotlin.io].
 */
inline fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {
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
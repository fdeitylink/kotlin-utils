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

/*
 * TODO:
 * Deprecate this in favor of standard java.util.logging system
 */

package io.fdeitylink.util

import java.util.logging.LogRecord
import java.util.logging.FileHandler
import java.util.logging.Level

import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter

object Logger {
    fun logMessage(message: String, logFile: String = "error.log") {
        try {
            val handle = FileHandler(logFile)
            handle.publish(LogRecord(Level.ALL, message))
            handle.close()
        }
        catch (except: IOException) {
            System.err.println(message)
        }
    }

    @JvmOverloads
    fun logThrowable(message: String = "", t: Throwable, logFile: String = "error.log") {
        val writer = StringWriter()
        writer.append(message).append('\n')
        writer.append("${t.javaClass.name}: ${t.message}")

        t.printStackTrace(PrintWriter(writer))

        val finalMessage = writer.toString()

        try {
            val handle = FileHandler(logFile)
            handle.publish(LogRecord(Level.ALL, finalMessage))
            handle.close()
        }
        catch (except: IOException) {
            System.err.println(finalMessage)
        }
    }
}
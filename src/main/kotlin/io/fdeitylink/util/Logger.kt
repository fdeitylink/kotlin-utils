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
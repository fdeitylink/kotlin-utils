@file:JvmName("UtilsKt")
@file:JvmMultifileClass

package io.fdeitylink.util

//TODO: Verify lower < upper for each of these
fun Double.bound(lower: Double, upper: Double) = Math.max(lower, Math.min(this, upper))

fun Float.bound(lower: Float, upper: Float) = Math.max(lower, Math.min(this, upper))

fun Long.bound(lower: Long, upper: Long) = Math.max(lower, Math.min(this, upper))

fun Int.bound(lower: Int, upper: Int) = Math.max(lower, Math.min(this, upper))

fun Short.bound(lower: Short, upper: Short) = maxOf(lower, minOf(this, upper))

fun Byte.bound(lower: Byte, upper: Byte) = maxOf(lower, minOf(this, upper))
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

//TODO: Verify lower < upper for each of these
fun Double.bound(lower: Double, upper: Double) = Math.max(lower, Math.min(this, upper))

fun Float.bound(lower: Float, upper: Float) = Math.max(lower, Math.min(this, upper))

fun Long.bound(lower: Long, upper: Long) = Math.max(lower, Math.min(this, upper))

fun Int.bound(lower: Int, upper: Int) = Math.max(lower, Math.min(this, upper))

fun Short.bound(lower: Short, upper: Short) = maxOf(lower, minOf(this, upper))

fun Byte.bound(lower: Byte, upper: Byte) = maxOf(lower, minOf(this, upper))
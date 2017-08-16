package io.fdeitylink.util

class NullArgumentException(funcName: String, argName: String) :
        NullPointerException("Null value passed for argument $argName to function " +
                             "${funcName + if ("" == funcName) "" else "()"} when a null value is not permitted") {

    constructor() : this("", "")
    constructor(funcName: String) : this(funcName, "")
}

fun <T> requireNonNull(obj: T?) = requireNonNull(obj, "", "")

fun <T> requireNonNull(obj: T, funcName: String) = requireNonNull(obj, funcName, "")

fun <T> requireNonNull(obj: T, funcName: String, argName: String) =
        obj ?: throw NullArgumentException(funcName, argName)
package io.fdeitylink.util

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE) //TODO: AnnotationRetention.BINARY?
@MustBeDocumented
annotation class NotNull

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE,
        AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE) //TODO: AnnotationRetention.BINARY?
@MustBeDocumented
annotation class Nullable
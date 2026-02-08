package com.evandhardspace.core.domain.validation.rule

interface ValidationRule <in T> {
    fun validate(value: T): Boolean
}

infix fun <T> ValidationRule<T>.and(other: ValidationRule<T>): ValidationRule<T> =
    object : ValidationRule<T> {
        override fun validate(value: T): Boolean =
            this@and.validate(value) && other.validate(value)
    }

infix fun <T> ValidationRule<T>.or(other: ValidationRule<T>): ValidationRule<T> =
    object : ValidationRule<T> {
        override fun validate(value: T): Boolean =
            this@or.validate(value) || other.validate(value)
    }

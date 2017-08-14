package com.colavo.android.entity.query

import kotlin.reflect.KClass

/**
 * Created by RUS on 12.07.2016.
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Handle(val value: KClass<out BaseQuery>)
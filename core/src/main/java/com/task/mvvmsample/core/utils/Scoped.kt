package com.task.mvvmsample.core.utils

import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.ext.getFullName

interface Scoped {

    val scope: Scope
        get() = koin.getOrCreateScope(
            this::class.getFullName(),
            TypeQualifier(this::class)
        )

    private val koin: Koin
        get() = GlobalContext.get().koin

    fun closeScope() {
        scope.close()
    }
}
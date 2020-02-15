package com.task.mvvmsample.viewmodel.base.implementation

abstract class OnceExecutableCommand : BaseCommand() {
    abstract fun executeOnce()

    final override fun executeCore() {
        executeOnce()
        setCanExecute(false)
    }
}
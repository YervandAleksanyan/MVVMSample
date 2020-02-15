package com.task.mvvmsample.viewmodel.base.implementation

import androidx.lifecycle.ViewModel
import com.task.mvvmsample.core.IDisposable
import com.task.mvvmsample.core.utils.Scoped
import com.task.mvvmsample.core.utils.weak
import com.task.mvvmsample.viewmodel.base.ICommand
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass


internal abstract class BaseCommandFactory(
    viewModel: ViewModel
) : IDisposable, Scoped {

    private val viewModel by weak(viewModel)
    private val disposableCommands = HashMap<KClass<out Any>, IDisposable>()

    protected inline fun <reified T : BaseCommand> getCommand(
        noinline parameters: ParametersDefinition? = null
    ) = scope.get<T>(parameters = initCommandParameters(parameters))
        .also { command ->
            addDisposable(command)
        }

    private fun initCommandParameters(parameters: ParametersDefinition?): ParametersDefinition {
        val parametersValues: Array<out Any?> = parameters?.invoke()?.values ?: emptyArray()
        return {
            parametersOf(*parametersValues, viewModel!!)
        }
    }

    private fun addDisposable(command: ICommand) {
        if (command is IDisposable) {
            val key = command::class

            with(disposableCommands) {
                get(key)?.dispose()
                put(key, command)
            }
        }
    }

    override fun dispose() {
        disposeCommands()
        closeScope()
    }

    private fun disposeCommands() {
        with(disposableCommands) {
            values.forEach { it.dispose() }
            clear()
        }
    }
}
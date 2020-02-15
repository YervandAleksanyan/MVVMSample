package com.task.mvvmsample.viewmodel.base.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.mvvmsample.viewmodel.base.IDisposableAsyncCommand
import com.task.mvvmsample.viewmodel.base.ImmutableLiveData
import kotlinx.coroutines.*


abstract class BaseAsyncCommand : BaseCommand(), IDisposableAsyncCommand {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val jobs: MutableList<Job> = ArrayList()

    private val isBusyMutable = MutableLiveData(false)
    final override val isBusy: LiveData<Boolean> = ImmutableLiveData(isBusyMutable)

    private val isSuccessfulMutable = MutableLiveData(false)
    final override val isSuccessful: LiveData<Boolean> = ImmutableLiveData(isSuccessfulMutable)

    private var failureMessageMutable = MutableLiveData<String?>()
    final override val failureMessage: LiveData<String?> = ImmutableLiveData(failureMessageMutable)

    protected abstract suspend fun executeCoreAsync(): Boolean

    internal var exception: Throwable? = null

    final override suspend fun executeAsync() {
        if (isExecutable.value == true) {
            resetState()
            try {
                setState(State.BUSY)
                val result: Boolean = executeCoreAsync()
                setState(State.FINISHED, successful = result)
            } catch (ex: Exception) {
                setState(State.FAILED)
                handleError(ex)
            }
        }
    }

    final override fun executeCore() {
        jobs += scope.launch {
            executeAsync()

            coroutineContext[Job]?.let { currentJob ->
                jobs -= currentJob
            }
        }
    }

    private fun resetState() {
        isBusyMutable.value = false
        isSuccessfulMutable.value = false
        exception = null
        failureMessageMutable.value = null
    }

    private fun setState(
        state: State,
        successful: Boolean = false
    ) {
        when (state) {
            State.BUSY -> isBusyMutable.value = true
            State.FINISHED -> {
                isBusyMutable.value = false
                isSuccessfulMutable.value = successful
            }
            State.FAILED -> {
                isBusyMutable.value = false
            }
            State.IDLE -> {
            }
        }
    }

    protected open suspend fun handleError(ex: Throwable) {
        exception = ex
        failureMessageMutable.value = ex.message
    }

    protected fun setFailureMessage(failureMessage: String) {
        failureMessageMutable.value = failureMessage
    }

    override fun dispose() {
        cancelJobs()
    }

    override fun cancelJobs() {
        for (index in 0 until jobs.size) {
            jobs[index].cancel()
        }
    }

    private enum class State {
        IDLE,
        BUSY,
        FINISHED,
        FAILED
    }
}



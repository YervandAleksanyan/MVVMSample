package com.task.mvvmsample.bindings

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.task.mvvmsample.utils.lifecycleOwner
import com.task.mvvmsample.viewmodel.base.ICommand

@BindingAdapter(value = ["command"])
fun bindViewCommand(view: View, command: ICommand?) {
    command?.isExecutable?.observe(view.lifecycleOwner, Observer {
        view.isEnabled = it
    })
    view.setOnClickListener {
        command?.execute()
    }
}
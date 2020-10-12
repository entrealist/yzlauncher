package com.yz.books.base.vmpath

import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation


class VMRouter(private var viewModel: ViewModel) {

    companion object {

        val mCallMap = ArrayMap<String, KCallable<*>>()

        fun init(viewModel: ViewModel): VMRouter {
            return VMRouter(viewModel)
        }
    }

    fun toTarget(path: String, vararg args: Any?) {
        if (mCallMap["${viewModel.javaClass.name}/${path}"] == null) {
            viewModel.viewModelScope.launch(Dispatchers.Main) {
                try {
                    kotlin.run {
                        viewModel::class.members.forEach { call ->
                            val annotation = call.findAnnotation<VMPath>()
                            annotation?.apply {
                                if (this.path == path) {
                                    if (args.isNotEmpty()) {
                                        call.call(viewModel, *args)
                                    } else {
                                        call.call(viewModel)
                                    }
                                    mCallMap["${viewModel.javaClass.name}/${path}"] = call
                                    return@run
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Exception  : ${e.message}")
                }
            }
        } else {
            try {
                mCallMap["${viewModel.javaClass.name}/${path}"]?.apply {
                    if (args.isNotEmpty()) {
                        call(viewModel, *args)
                    } else {
                        call(viewModel)
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception  call : ${e.message}")
            }
        }
    }
}
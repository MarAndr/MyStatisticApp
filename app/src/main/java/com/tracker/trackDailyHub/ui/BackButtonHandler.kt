package com.tracker.trackDailyHub.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun BackButtonHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val callback = rememberUpdatedState(onBackPressed)

    DisposableEffect(dispatcher, enabled) {
        val backCallback = object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                callback.value()
            }
        }

        dispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}
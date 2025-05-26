package com.org.basshead.utils.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.org.basshead.utils.core.PrimaryOrange


@Composable
fun LoadingScreen() {
    Box(
       modifier = Modifier.fillMaxSize().background(Color.DarkGray.copy(0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = PrimaryOrange)
    }
}
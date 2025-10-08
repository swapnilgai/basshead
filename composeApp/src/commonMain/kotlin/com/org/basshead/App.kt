package com.org.basshead

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.org.basshead.design.theme.ProvideBassheadTheme
import com.org.basshead.navigation.NavigationGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ProvideBassheadTheme(
        isDarkTheme = false, // Force light theme as default
    ) {
        val navController = rememberNavController()
        NavigationGraph(navController = navController)
    }
}

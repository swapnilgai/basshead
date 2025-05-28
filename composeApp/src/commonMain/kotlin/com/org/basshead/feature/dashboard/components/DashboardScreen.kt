package com.org.basshead.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardList(
    dashBoardViewModel: DashBoardViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text("Dashboard")
    }
}

package com.org.basshead.feature.main.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.org.basshead.feature.dashboard.components.DashboardScreenRoot
import com.org.basshead.feature.profile.components.ProfileScreenRoot
import com.org.basshead.feature.search.components.SearchScreenRoot

@Composable
fun MainScreen(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    },
                    label = { Text("Search") },
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                        )
                    },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2) },
                )
            }
        },
    ) { paddingValues ->
        when (selectedTab) {
            0 -> DashboardScreenRoot(
                navigate = navigate,
                modifier = Modifier.padding(paddingValues),
            )
            1 -> SearchScreenRoot(
                navigate = navigate,
                modifier = Modifier.padding(paddingValues),
            )
            2 -> ProfileScreenRoot(
                navigate = navigate,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

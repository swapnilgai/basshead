package com.org.basshead.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.org.basshead.feature.auth.components.LoginScreenRoot
import com.org.basshead.feature.dashboard.components.DashboardScreenRoot
import com.org.basshead.feature.splash.components.SplashScreenRoot

@Composable
fun NavigationGraph(navController: NavHostController) {
    val routes: Map<String?, String> = remember {
        mapOf(
            Route.Splash::class.simpleName to "splash",
            Route.Auth::class.simpleName to "auth",
            Route.Dashboard::class.simpleName to "dashboard",
            Route.Profile::class.simpleName to "profile",
            Route.FestivalDetails::class.simpleName to "festival_detail",
            Route.FestivalLeaderBoard::class.simpleName to "festival_leaderboard",
        )
    }

    NavHost(navController = navController, startDestination = routes[Route.Splash::class.simpleName]!!) {
        composable(routes[Route.Splash::class.simpleName]!!) {
            SplashScreenRoot { destination, popUpTp, inclusive ->
                navigate(navController, routes, destination, popUpTp, inclusive)
            }
        }
        composable(routes[Route.Auth::class.simpleName]!!) {
            LoginScreenRoot { destination, popUpTp, inclusive ->
                navigate(navController, routes, destination, popUpTp, inclusive)
            }
        }
        composable(routes[Route.Dashboard::class.simpleName]!!) {
            DashboardScreenRoot { destination, popUpTp, inclusive ->
                navigate(navController, routes, destination, popUpTp, inclusive)
            }
        }
    }
}

private fun navigate(
    navController: NavHostController,
    routes: Map<String?, String>,
    destination: String,
    popUpTp: String?,
    inclusive: Boolean?,
) {
    navController.navigate(routes[destination]!!) {
        popUpTp?.let {
            this.popUpTo(routes[popUpTp]!!) {
                this.inclusive = inclusive ?: false
            }
        }
    }
}

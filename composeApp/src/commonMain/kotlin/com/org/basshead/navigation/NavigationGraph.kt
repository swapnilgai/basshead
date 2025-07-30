package com.org.basshead.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.org.basshead.feature.auth.components.LoginScreenRoot
import com.org.basshead.feature.festivaldetail.components.FestivalDetailScreenRoot
import com.org.basshead.feature.main.components.MainScreen
import com.org.basshead.feature.search.components.SearchScreenRoot
import com.org.basshead.feature.splash.components.SplashScreenRoot

@Composable
fun NavigationGraph(navController: NavHostController) {
    val routes: Map<String?, String> = remember {
        mapOf(
            Route.Splash::class.simpleName to "splash",
            Route.Auth::class.simpleName to "auth",
            Route.Dashboard::class.simpleName to "main",
            Route.Profile::class.simpleName to "profile",
            Route.Search::class.simpleName to "search",
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
            MainScreen(
                navigate = { destination, popUpTp, inclusive ->
                    navigate(navController, routes, destination, popUpTp, inclusive)
                },
            )
        }
        composable(routes[Route.Search::class.simpleName]!!) {
            SearchScreenRoot(
                navigate = { destination, popUpTp, inclusive ->
                    navigate(navController, routes, destination, popUpTp, inclusive)
                },
            )
        }
        composable<Route.FestivalDetails> { backStackEntry ->
            val festivalDetails = backStackEntry.toRoute<Route.FestivalDetails>()
            FestivalDetailScreenRoot(
                festivalId = festivalDetails.festivalID,
                navigate = { destination, popUpTp, inclusive ->
                    if (destination == "Dashboard") {
                        navController.navigate(routes[Route.Dashboard::class.simpleName]!!) {
                            popUpTp?.let {
                                this.popUpTo(routes[popUpTp]!!) {
                                    this.inclusive = inclusive ?: false
                                }
                            }
                        }
                    } else {
                        navigate(navController, routes, destination, popUpTp, inclusive)
                    }
                },
            )
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
    when (destination) {
        "FestivalDetails" -> {
            // This is a special case - will be handled differently
            // For now, navigate to a placeholder
            navController.navigate("festival_detail") {
                popUpTp?.let {
                    this.popUpTo(routes[popUpTp]!!) {
                        this.inclusive = inclusive ?: false
                    }
                }
            }
        }
        else -> {
            // Check if destination contains festival ID parameter
            if (destination.startsWith("FestivalDetails/")) {
                val festivalId = destination.substringAfter("FestivalDetails/")
                navController.navigate(Route.FestivalDetails(festivalId)) {
                    popUpTp?.let {
                        this.popUpTo(routes[popUpTp]!!) {
                            this.inclusive = inclusive ?: false
                        }
                    }
                }
            } else {
                navController.navigate(routes[destination]!!) {
                    popUpTp?.let {
                        this.popUpTo(routes[popUpTp]!!) {
                            this.inclusive = inclusive ?: false
                        }
                    }
                }
            }
        }
    }
}

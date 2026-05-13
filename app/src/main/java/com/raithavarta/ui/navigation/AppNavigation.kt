package com.raithavarta.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raithavarta.ui.screens.CameraScreen
import com.raithavarta.ui.screens.FarmerDashboardScreen
import com.raithavarta.ui.screens.SasyaLibraryScreen
import com.raithavarta.ui.screens.HomeScreen
import androidx.compose.material.icons.filled.List
import com.raithavarta.ui.screens.SpoorthiScreen
import com.raithavarta.ui.screens.SplashScreen
import com.raithavarta.ui.screens.RoleSelectionScreen
import com.raithavarta.ui.screens.EmailAuthScreen
import com.raithavarta.model.UserRole
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.runtime.remember
import com.raithavarta.viewmodel.AuthViewModel
import com.raithavarta.viewmodel.CameraViewModel
import com.raithavarta.viewmodel.FarmerDashboardViewModel
import com.raithavarta.viewmodel.SpoorthiViewModel
import com.raithavarta.viewmodel.ProfileViewModel
import com.raithavarta.ui.screens.ProfileScreen
import androidx.compose.material.icons.filled.Person

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Splash : Screen("splash", "Splash", Icons.Filled.Home)
    object RoleSelection : Screen("role_selection", "Role", Icons.Filled.Home)
    object EmailAuth : Screen("email_auth/{role}", "Auth", Icons.Filled.Home) {
        fun createRoute(role: String) = "email_auth/$role"
    }
    object Home : Screen("home", "ಮುಖಪುಟ", Icons.Filled.Home)
    object Tips : Screen("tips", "ಸಲಹೆಗಳು", Icons.Filled.List)
    object Sasya : Screen("sasya", "ಸಸ್ಯ", Icons.Filled.LocalLibrary)
    object Spoorthi : Screen("spoorthi", "ಸ್ಪೂರ್ತಿ", Icons.Filled.Star)
    object EGidha : Screen("egidha", "ಇ-ಗಿಡ", Icons.Filled.CameraAlt)
    object Profile : Screen("profile", "ಖಾತೆ", Icons.Filled.Person)
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    farmerDashboardViewModel: FarmerDashboardViewModel,
    spoorthiViewModel: SpoorthiViewModel,
    cameraViewModel: CameraViewModel,
    profileViewModel: ProfileViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Splash.route && currentRoute != Screen.RoleSelection.route && currentRoute?.startsWith("email_auth") != true) {
                NavigationBar {
                    val items = listOf(Screen.Home, Screen.Tips, Screen.Sasya, Screen.Spoorthi, Screen.EGidha)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToAuth = {
                        navController.navigate(Screen.RoleSelection.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.RoleSelection.route) {
                RoleSelectionScreen(onRoleSelected = { role ->
                    navController.navigate(Screen.EmailAuth.createRoute(role.name))
                })
            }
            composable(
                route = Screen.EmailAuth.route,
                arguments = listOf(navArgument("role") { type = NavType.StringType })
            ) { backStackEntry ->
                val roleStr = backStackEntry.arguments?.getString("role") ?: UserRole.CUSTOMER.name
                val role = UserRole.valueOf(roleStr)
                val activity = LocalContext.current as Activity
                val authViewModel = remember { AuthViewModel(activity) }
                
                EmailAuthScreen(
                    role = role,
                    viewModel = authViewModel,
                    onAuthSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.RoleSelection.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Tips.route) {
                FarmerDashboardScreen(
                    viewModel = farmerDashboardViewModel,
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
            composable(Screen.Sasya.route) {
                SasyaLibraryScreen(
                    onCategorySelected = { cropId ->
                        farmerDashboardViewModel.setCropFilter(cropId)
                        navController.navigate(Screen.Tips.route) {
                            launchSingleTop = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
            composable(Screen.Spoorthi.route) {
                SpoorthiScreen(viewModel = spoorthiViewModel)
            }
            composable(Screen.EGidha.route) {
                CameraScreen(viewModel = cameraViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = {
                        navController.navigate(Screen.RoleSelection.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

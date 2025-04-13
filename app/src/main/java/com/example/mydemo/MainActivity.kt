package com.example.mydemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mydemo.business.services.MusicPlayerService
import com.example.mydemo.ui.bands.BandsView
import com.example.mydemo.ui.bands.CurrentBand
import com.example.mydemo.ui.device.ElectronicsView
import com.example.mydemo.ui.navigation.BottomNavigation
import com.example.mydemo.ui.navigation.BottomNavigationItem
import com.example.mydemo.ui.navigation.DemoApplicationScreens
import com.example.mydemo.ui.navigation.TopBar
import com.example.mydemo.ui.theme.MyDemoTheme
import com.example.mydemo.ui.user.UserView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDemoTheme {
                val navController = rememberNavController()
                val navigationItems = remember {
                    mutableStateListOf(
                        BottomNavigationItem(
                            route = DemoApplicationScreens.Home.name,
                            title = DemoApplicationScreens.Home.name,
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            hasNews = false
                        ),
                        BottomNavigationItem(
                            route = DemoApplicationScreens.Users.name,
                            title = DemoApplicationScreens.Users.name,
                            selectedIcon = Icons.Filled.Person,
                            unselectedIcon = Icons.Outlined.Person,
                            hasNews = false
                        ),
                        BottomNavigationItem(
                            route = DemoApplicationScreens.Electronics.name,
                            title = DemoApplicationScreens.Electronics.name,
                            selectedIcon = Icons.Filled.Star,
                            unselectedIcon = Icons.Outlined.Star,
                            hasNews = true,
                        )
                    )
                }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(
                            navController = navController,
                            items = navigationItems
                        )
                    },
                    topBar = {
                        TopBar(
                            navController = navController,
                        )
                    }
                ) { innerPadding ->
                    DemoAppNavHost(
                        navHostController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onUpdateBandsCount = { count ->
                            navigationItems[0] = navigationItems[0].copy(
                                badgeCount = count
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    onUpdateBandsCount: (Int) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to the home Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        BandsView(
            navHostController = navHostController,
            onUpdateBandsCount = onUpdateBandsCount
        )
    }
}

@Composable
fun DemoAppNavHost(
    navHostController: NavHostController,
    modifier: Modifier,
    onUpdateBandsCount: (Int) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = DemoApplicationScreens.Home.name,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = DemoApplicationScreens.Home.name) {
            HomeScreen(
                navHostController = navHostController,
                onUpdateBandsCount = onUpdateBandsCount
            )
        }
        composable(
            route = "${DemoApplicationScreens.BandInfo.name}/{bandCode}",
            arguments = listOf(
                navArgument("bandCode") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val bandCode = navBackStackEntry.arguments?.getString("bandCode") ?: ""
            CurrentBand(
                bandCode = bandCode
            )
        }
        composable(
            route = DemoApplicationScreens.Electronics.name,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }

        ) {
            ElectronicsView()
        }
        composable(
            route = DemoApplicationScreens.Users.name
        ) {
            UserView()
        }
    }
}

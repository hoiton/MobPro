package com.example.mydemo.ui.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mydemo.business.services.MusicPlayerService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TopBar(navController: NavHostController) {
    var toggle by remember { mutableStateOf(false) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route?.split("/")?.firstOrNull()
    val context = LocalContext.current
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = android.Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }
    TopAppBar(
        title = {
            currentDestination?.let { destination ->
                val title = when (destination) {
                    DemoApplicationScreens.Home.name -> "Home"
                    DemoApplicationScreens.BandInfo.name -> "Band Info"
                    DemoApplicationScreens.Electronics.name -> "Electronics"
                    DemoApplicationScreens.Users.name -> "User"
                    else -> ""
                }
                Text(text = title)
            }
        },
        navigationIcon = {
            if (currentDestination == DemoApplicationScreens.BandInfo.name) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }

        },
        actions = {

            if (currentDestination == DemoApplicationScreens.Electronics.name) {
                IconButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                            && notificationPermissionState?.status?.isGranted == false
                        ) {
                            notificationPermissionState.launchPermissionRequest()
                        } else {
                            if (toggle) {
                                val intent = Intent(context, MusicPlayerService::class.java)
                                context.stopService(intent)
                            }
                            else {
                                val intent = Intent(context, MusicPlayerService::class.java)
                                context.startForegroundService(intent)
                            }
                            toggle = !toggle;
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "check"
                    )
                }
            }
        }
    )
}
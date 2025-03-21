package com.example.mydemo

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mydemo.ui.bands.BandsView
import com.example.mydemo.ui.bands.CurrentBand
import com.example.mydemo.ui.device.DeviceViewModel
import com.example.mydemo.ui.device.ElectronicsView
import com.example.mydemo.ui.theme.MyDemoTheme
import com.example.mydemo.ui.user.UserView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDemoTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoAppNavHost(
                        navHostController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navHostController: NavHostController, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val text by remember { mutableStateOf("Some text") }
        val number by remember { mutableIntStateOf(0) }
        Text(
            text = "Welcome to the home Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        BandsView(
            navHostController = navHostController
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick =  {
                navHostController.navigate(DemoApplicationScreens.Users.name)
            }
        ) {
            Text(
                text = "Users",
            )
        }
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick =  {
                navHostController.navigate(DemoApplicationScreens.Electronics.name)
            }
        ) {
            Text(
                text = "Go to Electronics Screen",
            )
        }
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick =  {
                navHostController.navigate("${DemoApplicationScreens.Detail.name}/HomeScreen")
            }
        ) {
            Text(
                text = "Go to detail Screen",
            )
        }
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick =  {
                navHostController.navigate("${DemoApplicationScreens.Overview.name}/$number?text=$text")
            }
        ) {
            Text(
                text = "Go to overview Screen",
            )
        }
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "With the button above, you can navigate to a new screen",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


@Composable
fun DemoAppNavHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = DemoApplicationScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = DemoApplicationScreens.Home.name) {
            HomeScreen(
                navHostController = navHostController,
                modifier = modifier
            )
        }
        composable(
            route = "${DemoApplicationScreens.Detail.name}/{senderText}",
            arguments = listOf(
                navArgument("senderText") {
                    type = NavType.StringType
                }
            ),
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
        ) { navBackStackEntry ->
            val senderText = navBackStackEntry.arguments?.getString("senderText") ?: "error"
            DetailScreen(
                senderText = senderText,
                navHostController = navHostController,
                modifier = modifier)
        }
        composable(
            route = "${DemoApplicationScreens.Overview.name}/{number}?text={text}",
            arguments = listOf(
                navArgument("number") {
                    type = NavType.IntType
                },
                navArgument("text") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val number = navBackStackEntry.arguments?.getInt("number") ?: -1
            val text = navBackStackEntry.arguments?.getString("text") ?: ""
            OverviewScreen(
                number = number,
                text = text,
                navHostController = navHostController)
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
            route = DemoApplicationScreens.Electronics.name
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

@Preview(showBackground = true, device = Devices.PIXEL, locale = "en-US")
@Composable
fun DetailScreenPreview() {
    MyDemoTheme {
        DetailScreen("helooo", rememberNavController(), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun DetailScreen(senderText: String,
                 navHostController: NavHostController,
                 modifier: Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Oben Anfang")
                Text(text = "Oben Ende")
            }
            Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "Oben Mitte")
            Text(modifier = Modifier.align(Alignment.End), text = "Mitte Ende")
            Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "Unten Mitte")
            Text(text = "Unten Anfang")
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = "Welcome to DetailScreen from $senderText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Button(
                onClick = { navHostController.popBackStack() }
            ) {
                Text("Go Back")
            }
        }
    }
}

@Composable
fun OverviewScreen(number: Number, text: String, navHostController: NavHostController) {
    Column {
        Text("Number: $number")
        Text("Number: $text")
        Button(onClick =  {
            navHostController.navigate("${DemoApplicationScreens.Detail.name}/OverviewScreen")
        }) {
            Text("To to DetailScreen")
        }
    }
}

enum class DemoApplicationScreens {
    Home,
    Detail,
    Overview,
    BandInfo,
    Electronics,
    Users
}
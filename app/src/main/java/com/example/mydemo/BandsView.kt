package com.example.mydemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage

@Composable
fun BandsView(navHostController: NavHostController, viewModel: BandsViewModel = viewModel()) {
    val bands by viewModel.bandsFlow.collectAsState()
    Column (
        verticalArrangement = Arrangement.Center
    ){
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.requestBandCodesFromServer()
            }
        ) {
            Text("Bands laden")
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            bands.forEach { band ->
                Text(
                    modifier = Modifier.clickable {
                        navHostController.navigate(
                            route = "${DemoApplicationScreens.BandInfo.name}/${band.code}"
                        )
                    },
                    text = band.name
                )
            }
        }
    }
}

@Composable
fun CurrentBand(
    bandCode: String,
    viewModel: BandsViewModel = viewModel()
) {
    viewModel.requestBandInfoFromServer(bandCode)
    val currentBand by viewModel.currentBand.collectAsState(null)
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center

    ){
        Text(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            text = currentBand?.name ?: "",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            text = "${currentBand?.homeCountry}, ${currentBand?.foundingYear}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        AsyncImage(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            model = currentBand?.bestOfCdCoverImageUrl,
            contentDescription = null
        )
    }

}
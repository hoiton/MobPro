package com.example.mydemo.ui.device

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ElectronicsView(viewModel: DeviceViewModel = viewModel()) {
    viewModel.requestElectronicsFromServer()

    val electronics by viewModel.electronicsFlow.collectAsState()
    Column (
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        electronics.forEach { electronic ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = electronic.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    electronic.data?.let { data ->
                        data.year?.let { year ->
                            Text(text = "Year: $year", style = MaterialTheme.typography.bodyMedium)
                        }
                        data.price?.let { price ->
                            Text(text = "Price: $price", style = MaterialTheme.typography.bodyMedium)
                        }
                        data.cpuModel?.let { cpu ->
                            Text(text = "CPU Model: $cpu", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
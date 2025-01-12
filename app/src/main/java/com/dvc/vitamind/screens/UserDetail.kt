package com.dvc.vitamind.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvc.vitamind.model.FoodNutrient
import com.dvc.vitamind.viewmodel.FoodNutrientViewModel
import com.dvc.vitamind.viewmodel.UserViewModel

@Composable
fun UserDetailScreen() {
    val foodNutrientViewModel: FoodNutrientViewModel = viewModel()


    // Fetch food nutrients
    val nutrients = foodNutrientViewModel.allNutrients.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Nutrients Section Header
        Text(
            text = "Food Nutrients",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // List of Nutrients
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(nutrients.value) { nutrient ->
                FoodNutrientItem(nutrient)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FoodDetailScreen() {
    val foodNutrientViewModel: FoodNutrientViewModel = viewModel()
    // Fetch food nutrients
    val nutrients = foodNutrientViewModel.allNutrients.observeAsState(emptyList())

    if (nutrients.value.isEmpty()) {
        // Show empty state message
        Text(
            text = "No food nutrients found in the database.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        // List of food nutrients
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(nutrients.value) { nutrient ->
                FoodNutrientItem(nutrient)
            }
        }
    }
}

@Composable
fun FoodNutrientItem(nutrient: FoodNutrient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle click if needed */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nutrient: ${nutrient.nutrientName}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Value: ${nutrient.value} ${nutrient.unit}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

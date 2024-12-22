package com.dvc.vitamind.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvc.vitamind.model.FoodNutrient
import com.dvc.vitamind.viewmodel.FoodNutrientViewModel
import com.dvc.vitamind.viewmodel.UserViewModel


@Composable
fun UserDetailScreen() {
    val userViewModel: UserViewModel = viewModel()
    val foodNutrientViewModel: FoodNutrientViewModel = viewModel()

    // Kullanıcı verilerini al
    LaunchedEffect(Unit) {
        userViewModel.getLastUser()
    }

    val selectedUser = userViewModel.selectedUser.value

    // Besin verilerini al
    val nutrients = foodNutrientViewModel.allNutrients.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        selectedUser?.let { user ->
            // Header Section - User Details
            Text(
                text = "User Details",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // User Details Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Name
                    DetailRow(label = "Name", value = user.name.toString())

                    // Age
                    DetailRow(label = "Age", value = user.age.toString())

                    // Gender
                    DetailRow(label = "Gender", value = user.gender.toString())

                    // Weight
                    DetailRow(label = "Weight", value = "${user.weight} kg")

                    // Height
                    DetailRow(label = "Height", value = "${user.height} cm")

                    // Activity Level
                    DetailRow(label = "Activity Level", value = user.activityLevel.toString())

                    // Health Conditions
                    DetailRow(
                        label = "Health Conditions",
                        value = user.healthConditions.toString()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Besin öğelerini gösteren LazyColumn
        Text(
            text = "Food Nutrients",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}




@Composable
fun FoodDeatilScreen() {
    val foodNutrientViewModel: FoodNutrientViewModel = viewModel()
    // Besin öğelerini al
    val nutrients = foodNutrientViewModel.allNutrients.observeAsState(emptyList())

    // Veritabanından çekilen veriyi göster
    if (nutrients.value.isEmpty()) {
        // Veritabanında hiç veri yoksa bunu göster
        Text(text = "No food nutrients found in the database.")
    } else {
        // Veritabanında veriler varsa listeyi göster
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
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nutrient: ${nutrient.nutrientName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Value: ${nutrient.value} ${nutrient.unit}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}


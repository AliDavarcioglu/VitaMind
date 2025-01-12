package com.dvc.vitamind.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvc.vitamind.viewmodel.FoodNutrientViewModel
import com.dvc.vitamind.viewmodel.UserViewModel

@Composable
fun AnalysisScreen() {

    val context = LocalContext.current


    val userViewModel: UserViewModel = viewModel()
    val foodNutrientViewModel: FoodNutrientViewModel = viewModel()



    // Yapay zeka mesajını göstermek için bir state
    val aiResponse = remember { mutableStateOf<String?>(null) }


    // Kullanıcı verileri
    val prompt = "Bu besinin kalori değerlerini analiz et."
    val besinAdi = "pizza"
    val gramaj = 150

    // Fetch user details
    LaunchedEffect(Unit) {
        userViewModel.getLastUser()
    }

    val selectedUser = userViewModel.selectedUser.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Information Header
        Text(
            text = "User Details",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        selectedUser?.let { user ->
            // User Information Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailRow(label = "Name", value = user.name.toString())
                    DetailRow(label = "Age", value = user.age.toString())
                    DetailRow(label = "Gender", value = user.gender.toString())
                    DetailRow(label = "Weight", value = "${user.weight} kg")
                    DetailRow(label = "Height", value = "${user.height} cm")
                    DetailRow(label = "Activity Level", value = user.activityLevel.toString())
                    DetailRow(label = "Health Conditions", value = user.healthConditions.toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Analiz yap butonu
        Button(
            onClick = {
                analyzeFood(prompt, besinAdi, gramaj) { result ->
                    // API yanıtını ekranda gösterebilmek için State'e atıyoruz
                    aiResponse.value = result
                }
            }
        ) {
            Text("Analiz Yap")
        }

        // Yapay zeka yanıtını gösterme
        aiResponse.value?.let { response ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = response,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
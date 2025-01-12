package com.dvc.vitamind.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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

    val nutrients = foodNutrientViewModel.allNutrients.observeAsState(emptyList())

    val aiResponse = remember { mutableStateOf<String?>(null) }

    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userViewModel.getLastUser()
    }

    val selectedUser = userViewModel.selectedUser.value

    val prompt = "Verilen besinler, gramajlar ve kullanıcının özelliklerini dikkate alarak günlük besin ihtiyacının ne kadarını karşıladığını, nasıl eksiklerini kapatacağını en fazla 3 cümle ile anlat"
    val besinAdi = nutrients.toString()
    val user = "${selectedUser?.age},${selectedUser?.gender},${selectedUser?.weight},${selectedUser?.height},${selectedUser?.activityLevel},${selectedUser?.healthConditions}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "User Details",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            selectedUser?.let { user ->

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


            aiResponse.value?.let { response ->
                Text(
                    text = response,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }


        if (isLoading.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Analiz Ediliyor...")
            }
        } else {
            Button(
                onClick = {
                    isLoading.value = true
                    analyzeFood(prompt, besinAdi, user) { result ->
                        aiResponse.value = result
                        isLoading.value = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Analiz Yap")
            }
        }
    }
}

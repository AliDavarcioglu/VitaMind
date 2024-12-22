package com.dvc.vitamind.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dvc.vitamind.model.User
import com.dvc.vitamind.viewmodel.UserViewModel


@Composable
fun UserInputScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var healthConditions by remember { mutableStateOf("") }

    val viewModel: UserViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), // Elemanlar arasına eşit boşluk koyar
        horizontalAlignment = Alignment.CenterHorizontally // Elemanları ortalar
    ) {
        Text(
            text = "Kullanıcı Bilgileri",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            singleLine = true,
            onValueChange = { name = it },
            label = { Text("Ad Soyad") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            singleLine = true,
            onValueChange = { age = it },
            label = { Text("Yaş") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gender,
            singleLine = true,
            onValueChange = { gender = it },
            label = { Text("Cinsiyet") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            singleLine = true,
            onValueChange = { weight = it },
            label = { Text("Kilo (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            singleLine = true,
            onValueChange = { height = it },
            label = { Text("Boy (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = activityLevel,
            singleLine = true,
            onValueChange = { activityLevel = it },
            label = { Text("Aktivite Seviyesi") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = healthConditions,
            singleLine = true,
            onValueChange = { healthConditions = it },
            label = { Text("Sağlık Durumları") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && age.isNotBlank() && gender.isNotBlank() &&
                    weight.isNotBlank() && height.isNotBlank() && activityLevel.isNotBlank()
                ) {
                    val user = User(
                        name = name,
                        age = age,
                        gender = gender,
                        weight = weight.toIntOrNull() ?:0,
                        height = height.toIntOrNull() ?:0,
                        activityLevel = activityLevel,
                        healthConditions = healthConditions
                    )
                    viewModel.saveUser(user)
                }
                navController.navigate("user_detail")

            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save User")
        }




    }
}



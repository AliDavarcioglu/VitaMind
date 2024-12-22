import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.dvc.vitamind.Food
import com.dvc.vitamind.model.FoodNutrient
import com.dvc.vitamind.roomdb.FoodDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(food: Food, onBack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedNutrient by remember { mutableStateOf<FoodNutrient?>(null) }

    val db = remember { Room.databaseBuilder(context, FoodDatabase::class.java, "food_database").build() }
    val foodNutrientDao = db.foodNutrientDao()
    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true } // Butona tıklandığında dialog açılacak
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Nutrient") // Artı simgesi
            }
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Başlık ve Genel Bilgi
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = food.description,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), thickness = 1.dp)
                }
            }


            val grams = food.foodNutrients.filter { it.unitName.equals("g", ignoreCase = true) }
                .sortedByDescending { it.value }

            val milligrams = food.foodNutrients.filter { it.unitName.equals("mg", ignoreCase = true) }
                .sortedByDescending { it.value }
            val calori = food.foodNutrients.filter { it.unitName.equals("kcal", ignoreCase = true) }
                .sortedByDescending { it.value }
            val others = food.foodNutrients.filter {
                !it.unitName.equals("g", ignoreCase = true) &&
                        !it.unitName.equals("mg", ignoreCase = true)
            }.sortedByDescending { it.value}

            val sortedNutrients = calori + grams + milligrams + others

            items(sortedNutrients) { nutrient ->
                NutrientCard(
                    nutrientName = nutrient.nutrientName,
                    value = nutrient.value,
                    unit = nutrient.unitName
                )
            }
        }

        if (showDialog) {
            GramDialog(
                onDismiss = { showDialog = false },
                onAdd = { gramValue ->
                    // Handle the nutrient data and insert into the database
                    val newNutrient = FoodNutrient(
                        nutrientName = food.description, // You may need to customize the nutrient name as needed
                        value = gramValue.toDouble(),  // Or any default value
                        unit = "g",// Assuming foodId is the primary key
                        gram = gramValue
                    )

                    // Insert the new nutrient into the database
                    CoroutineScope(Dispatchers.IO).launch {
                        foodNutrientDao.insertNutrient(newNutrient)
                    }

                    // Close the dialog after adding
                    showDialog = false
                }
            )
        }





    }
}

@Composable
fun NutrientCard(nutrientName: String, value: Double, unit: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = nutrientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${value ?: "Bilinmiyor"} ${unit ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GramDialog(
    onDismiss: () -> Unit,
    onAdd: (Double) -> Unit,
) {
    var gramaj by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gramaj Giriniz") },
        text = {
            Column {
                TextField(
                    value = gramaj,
                    onValueChange = { gramaj = it },
                    label = { Text("Gramaj (g)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val gramajValue = gramaj.toDoubleOrNull() ?: 0.0
                    onAdd(gramajValue)
                    onDismiss()
                }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}


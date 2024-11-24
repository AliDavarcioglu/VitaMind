import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dvc.vitamind.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(food: Food, onBack: () -> Unit) {
    Scaffold(

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

/*
            val sortedNutrients = food.foodNutrients.filter { true }
                .sortedByDescending { it.value }

 */

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

            // Besin Değerlerini Listeleme
            items(sortedNutrients) { nutrient ->
                NutrientCard(
                    nutrientName = nutrient.nutrientName,
                    value = nutrient.value,
                    unit = nutrient.unitName
                )
            }
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

package com.dvc.vitamind

import FoodDetailScreen
import FoodSearchApp
import FoodSearchViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dvc.vitamind.screens.AnalysisScreen
import com.dvc.vitamind.screens.UserDetailScreen
import com.dvc.vitamind.screens.UserInputScreen
import com.dvc.vitamind.ui.theme.VitaMindTheme
import com.dvc.vitamind.viewmodel.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
     val apiKey = "jAhGvWsJX5jxgAWiwhgeyJNHqAflkuQ1n3ji9lp2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VitaMindTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenWithBottomBar(apiKey)


                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(
        modifier = Modifier.background(Color.White),
        elevation = 0.dp
    ) {
        BottomNavigationItem(
            selected = currentDestination == "user_input",
            onClick = { navController.navigate("user_input") },
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Kullanıcı Girişi")
            },
            label = { Text("Giriş") }
        )
        BottomNavigationItem(
            selected = currentDestination == "user_detail",
            onClick = { navController.navigate("user_detail") },
            icon = {
                Icon(Icons.Default.Info, contentDescription = "Kullanıcı Detayları")
            },
            label = { Text("Detaylar") }
        )
        BottomNavigationItem(
            selected = currentDestination == "search",
            onClick = { navController.navigate("search") },
            icon = {
                Icon(Icons.Default.Search, contentDescription = "Gıda Arama")
            },
            label = { Text("Ara") }
        )
        BottomNavigationItem(
            selected = currentDestination == "analysis",
            onClick = { navController.navigate("analysis") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Analiz")
            },
            label = { Text("Analiz") }
        )
        /*
        BottomNavigationItem(
            selected = currentDestination == "details",
            onClick = { navController.navigate("details") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Detaylar")
            },
            label = { Text("Besin Detayı") }
        )

         */
    }
}




fun fetchFoodData(apiKey: String, foodName: String, onResult: (List<Food>) -> Unit) {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(logging).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.nal.usda.gov/fdc/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val service = retrofit.create(FoodDataService::class.java)
    val call = service.searchFood(apiKey, foodName)


    call.enqueue(object : Callback<FoodResponse> {
        override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
            if (response.isSuccessful) {

                val foods = response.body()?.foods ?: emptyList()

                onResult(foods)
            } else {
                onResult(emptyList())
            }
        }

        override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
            onResult(emptyList())
        }
    })
}

@Composable
fun MainScreenWithBottomBar(apiKey: String) {
    val navController = rememberNavController()
    val viewModel: FoodSearchViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding -> // innerPadding burada alınır
        NavHost(
            navController = navController,
            startDestination = "user_input",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("user_input") {
                UserInputScreen(navController)
            }
            composable("user_detail") {
                UserDetailScreen()
            }
            composable("search") {
                FoodSearchApp(apiKey) { selectedFood ->
                    viewModel.selectFood(selectedFood)
                    navController.navigate("details")
                }
            }
            composable("analysis") {
                AnalysisScreen()
            }
            composable("details") {
                val selectedFood = viewModel.selectedFood.value
                if (selectedFood != null) {
                    FoodDetailScreen(selectedFood) {
                        navController.popBackStack()
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Besin detayı bulunamadı.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VitaMindTheme {

    }
}
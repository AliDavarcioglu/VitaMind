package com.dvc.vitamind.screens

import com.dvc.vitamind.Keys
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


fun analyzeFood(
    prompt: String,
    foodName: String,
    weight: Int,
    onResult: (String?) -> Unit
) {

      val apiKey = Keys.API_KEY
      val baseUrl = Keys.BASE_URL

    // Gönderilecek metni oluştur
    val textToAnalyze = "$prompt\nBesin adı: $foodName\nGramaj: $weight"


    val jsonObject = JSONObject().apply {
        val contentsArray = JSONArray().apply {
            val contentObject = JSONObject().apply {
                val partsArray = JSONArray().apply {
                    val partObject = JSONObject().apply {
                        put("text", textToAnalyze)
                    }
                    put(partObject)
                }
                put("parts", partsArray)
            }
            put(contentObject)
        }
        put("contents", contentsArray)
    }

    // JSON gövdeyi RequestBody’e çevir
    val requestBody = jsonObject
        .toString()
        .toRequestBody("application/json".toMediaType())

    val client = OkHttpClient()

    // HTTP isteğini oluştur
    val request = Request.Builder()
        .url(baseUrl)
        .post(requestBody)
        .addHeader("Content-Type", "application/json")
        .build()

    // Asenkron olarak API çağrısı yap
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResult("Hata oluştu: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!it.isSuccessful) {
                    onResult("Başarısız yanıt: ${response.code}")
                    return
                }
                val responseData = response.body?.string()
                if (responseData != null) {

                    val jsonResponse = JSONObject(responseData)
                    val candidatesArray = jsonResponse.optJSONArray("candidates")
                    if (candidatesArray != null && candidatesArray.length() > 0) {
                        val firstCandidate = candidatesArray.getJSONObject(0)
                        val contentObject = firstCandidate.optJSONObject("content")
                        val partsArray = contentObject?.optJSONArray("parts")
                        if (partsArray != null && partsArray.length() > 0) {
                            val firstPart = partsArray.getJSONObject(0)
                            val text = firstPart.optString("text", "")
                            onResult(text)
                        } else {
                            onResult("Yanıtta 'parts' bulunamadı.")
                        }
                    } else {
                        onResult("Yanıtta 'candidates' bulunamadı.")
                    }

                    //onResult(responseData)
                } else {
                    onResult("Gelen yanıt boş")
                }
            }
        }
    })
}

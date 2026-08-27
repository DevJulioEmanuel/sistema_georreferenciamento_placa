package com.example.sistema_georreferenciamento_placa.data.repository

import com.example.sistema_georreferenciamento_placa.data.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PlateRepository {
    private val api = RetrofitClient.api

    suspend fun uploadPlate(description: String, latitude: Double, longitude: Double, photoFile: File) {
        // Prepara os campos de texto
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val latPart = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lngPart = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // Prepara o arquivo da imagem
        val requestImageFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", photoFile.name, requestImageFile)

        // Chama a API
        val response = api.createPlate(descPart, latPart, lngPart, imagePart)

        if (!response.isSuccessful) {
            throw Exception("Erro no servidor: ${response.code()}")
        }
    }
}
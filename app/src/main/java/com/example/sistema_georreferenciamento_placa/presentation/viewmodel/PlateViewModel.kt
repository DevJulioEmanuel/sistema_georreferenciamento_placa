package com.seuprojeto.placas.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistema_georreferenciamento_placa.data.repository.PlateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class PlateViewModel : ViewModel() {
    private val repository = PlateRepository()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    var currentPhotoFile: File? = null

    private val _uploadState = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadState: StateFlow<UploadStatus> = _uploadState.asStateFlow()

    fun onDescriptionChange(newText: String) {
        _description.value = newText
    }

    fun onPhotoTaken(uri: Uri?, file: File?) {
        _photoUri.value = uri
        currentPhotoFile = file
    }

    fun submitPlateData(latitude: Double, longitude: Double) {
        val desc = _description.value
        val file = currentPhotoFile

        if (file == null || desc.isBlank()) {
            _uploadState.value = UploadStatus.Error("Tire a foto e digite uma descrição.")
            return
        }

        _uploadState.value = UploadStatus.Loading

        viewModelScope.launch {
            try {
                repository.uploadPlate(desc, latitude, longitude, file)
                _uploadState.value = UploadStatus.Success
            } catch (e: Exception) {
                _uploadState.value = UploadStatus.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun resetState() {
        _uploadState.value = UploadStatus.Idle
        _photoUri.value = null
        currentPhotoFile = null
        _description.value = ""
    }
}

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    object Success : UploadStatus()
    data class Error(val message: String) : UploadStatus()
}
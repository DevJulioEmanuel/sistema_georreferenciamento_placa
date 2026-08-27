package com.seuprojeto.placas.presentation.ui

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sistema_georreferenciamento_placa.utils.AppUtils
import com.seuprojeto.placas.presentation.viewmodel.PlateViewModel
import com.seuprojeto.placas.presentation.viewmodel.UploadStatus
import java.io.File

@Composable
fun PlateScreen(viewModel: PlateViewModel = viewModel()) {
    val context = LocalContext.current
    val description by viewModel.description.collectAsState()
    val photoUri by viewModel.photoUri.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()

    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var tempFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                viewModel.onPhotoTaken(tempUri, tempFile)
            }
        }
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                AppUtils.getCurrentLocation(context) { lat, lng ->
                    viewModel.submitPlateData(lat, lng)
                }
            } else {
                Toast.makeText(context, "Permissão de GPS necessária", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadStatus.Success -> {
                Toast.makeText(context, "Placa enviada com sucesso!", Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            is UploadStatus.Error -> {
                Toast.makeText(context, (uploadState as UploadStatus.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (photoUri == null) {
            Button(
                onClick = {
                    val (uri, file) = AppUtils.createTempPictureUri(context)
                    tempUri = uri
                    tempFile = file
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Tirar Foto da Placa")
            }
        } else {
            AsyncImage(
                model = photoUri,
                contentDescription = "Foto capturada",
                modifier = Modifier.fillMaxWidth().height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Descrição (Obrigatório)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uploadState !is UploadStatus.Loading
            ) {
                if (uploadState is UploadStatus.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Enviar com GPS")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { viewModel.resetState() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Descartar e Tirar Nova Foto")
            }
        }
    }
}
package com.example.sistema_georreferenciamento_placa.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun createTempPictureUri(context: Context): Pair<Uri, File> {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.cacheDir
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return Pair(uri, file)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onLocationResult: (Double, Double) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationResult(location.latitude, location.longitude)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                        if (lastLoc != null) {
                            onLocationResult(lastLoc.latitude, lastLoc.longitude)
                        }
                    }
                }
            }
    }
}
package com.example.sistema_georreferenciamento_placa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.seuprojeto.placas.presentation.ui.PlateScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlateScreen()
        }
    }
}
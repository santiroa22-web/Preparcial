package com.udistrital.preparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udistrital.preparcial.View.AhorroScreen
import com.udistrital.preparcial.View.CrearPlanScreen
import com.udistrital.preparcial.View.HomeScreen
import com.udistrital.preparcial.View.RegistroScreen
import com.udistrital.preparcial.Viewmodel.AhorroViewModel
import com.udistrital.preparcial.Viewmodel.CrearPlanViewModel
import com.udistrital.preparcial.Viewmodel.HomeViewmodel
import com.udistrital.preparcial.Viewmodel.RegistroViewModel
import com.udistrital.preparcial.ui.theme.PreparcialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreparcialTheme {
                val homeVM: HomeViewmodel = viewModel()
                val crearPlanVM: CrearPlanViewModel = viewModel()
                val ahorroVM: AhorroViewModel = viewModel()
                val registroVM: RegistroViewModel = viewModel()

                var pantallaActual by remember { mutableStateOf("home") }
                var planSeleccionadoId by remember { mutableStateOf<String?>(null) }

                // Manejo del botón Atrás del dispositivo
                BackHandler(enabled = pantallaActual != "home") {
                    pantallaActual = when (pantallaActual) {
                        "registro" -> "detalle"
                        else -> "home"
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    when (pantallaActual) {
                        "home" -> HomeScreen(
                            planes = homeVM.planes,
                            onCrearPlanClick = { pantallaActual = "crear" },
                            onPlanClick = { plan ->
                                planSeleccionadoId = plan.id
                                pantallaActual = "detalle"
                            }
                        )

                        "crear" -> CrearPlanScreen(
                            viewModel = crearPlanVM,
                            onBack = { pantallaActual = "home" },
                            onGuardarPlan = { nuevoPlan ->
                                homeVM.agregarPlan(nuevoPlan)
                                pantallaActual = "home"
                            }
                        )

                        "detalle" -> {
                            val plan = homeVM.planes.find { it.id == planSeleccionadoId }
                            if (plan != null) {
                                AhorroScreen(
                                    plan = plan,
                                    viewModel = ahorroVM,
                                    onBack = { pantallaActual = "home" },
                                    onRegistrarPagoClick = { pantallaActual = "registro" }
                                )
                            } else {
                                pantallaActual = "home"
                            }
                        }

                        "registro" -> RegistroScreen(
                            viewModel = registroVM,
                            onBack = { pantallaActual = "detalle" },
                            onGuardarAporte = { nuevoAporte ->
                                planSeleccionadoId?.let { id ->
                                    homeVM.agregarAporte(id, nuevoAporte)
                                }
                                pantallaActual = "detalle"
                            }
                        )
                    }
                }
            }
        }
    }
}
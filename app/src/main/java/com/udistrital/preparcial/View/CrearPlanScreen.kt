package com.udistrital.preparcial.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.udistrital.preparcial.Model.FechaUtils
import com.udistrital.preparcial.Model.PlanAhorro
import com.udistrital.preparcial.Viewmodel.CrearPlanViewModel
import com.udistrital.preparcial.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPlanScreen(
    viewModel: CrearPlanViewModel,
    onBack: () -> Unit,
    onGuardarPlan: (PlanAhorro) -> Unit
) {
    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.fechaLimite = FechaUtils.millisToFecha(millis)
                        viewModel.mensajeError = null
                    }
                    mostrarCalendario = false
                }) {
                    Text("Aceptar", color = VerdeLumi, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) {
                    Text("Cancelar", color = TextoGris)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            TopAppBar(
                title = { Text("Crear plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.mensajeError = null
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrisFondo)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.nombre,
                        onValueChange = {
                            viewModel.nombre = it
                            viewModel.mensajeError = null
                        },
                        label = { Text("Nombre del plan") },
                        placeholder = { Text("Ej. Ahorro para viaje familiar") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.meta,
                        onValueChange = {
                            viewModel.meta = it
                            viewModel.mensajeError = null
                        },
                        label = { Text("Meta de ahorro ($)") },
                        placeholder = { Text("5000000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Campo de solo lectura que abre el calendario al tocarlo
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = viewModel.fechaLimite,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fecha límite") },
                            placeholder = { Text("Selecciona una fecha") },
                            trailingIcon = {
                                Icon(Icons.Default.DateRange, contentDescription = "Calendario", tint = VerdeLumi)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { mostrarCalendario = true }
                        )
                    }

                    viewModel.mensajeError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.construirPlan()?.let { onGuardarPlan(it) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeLumi)
            ) {
                Text("Crear", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
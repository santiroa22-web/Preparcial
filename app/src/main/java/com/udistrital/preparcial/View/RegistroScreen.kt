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
import com.udistrital.preparcial.Model.Aporte
import com.udistrital.preparcial.Model.FechaUtils
import com.udistrital.preparcial.Model.PlanAhorro
import com.udistrital.preparcial.Viewmodel.RegistroViewModel
import com.udistrital.preparcial.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    plan: PlanAhorro,
    viewModel: RegistroViewModel,
    onBack: () -> Unit,
    onGuardarAporte: (Aporte) -> Unit
) {
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }
    val millisLimite = remember(plan.fechaLimite) { FechaUtils.fechaToMillis(plan.fechaLimite) }

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return millisLimite == null || utcTimeMillis <= millisLimite
            }
        }
    )

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.fecha = FechaUtils.millisToFecha(millis)
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
                title = { Text("Registrar pago", fontWeight = FontWeight.Bold) },
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
                    // Indicador informativo del saldo disponible y fecha tope
                    Surface(
                        color = VerdeSuave,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Falta por ahorrar: ${formatoMoneda.format(plan.montoRestante)}\nFecha límite: ${plan.fechaLimite}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VerdeOscuro,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = viewModel.integrante,
                        onValueChange = {
                            viewModel.integrante = it
                            viewModel.mensajeError = null
                        },
                        label = { Text("Integrante") },
                        placeholder = { Text("Nombre de quien aporta") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.monto,
                        onValueChange = {
                            viewModel.monto = it
                            viewModel.mensajeError = null
                        },
                        label = { Text("Monto ($)") },
                        placeholder = { Text("Máx. ${formatoMoneda.format(plan.montoRestante)}") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = viewModel.fecha,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fecha") },
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
                    viewModel.construirAporte(plan)?.let { onGuardarAporte(it) }
                },
                enabled = plan.montoRestante > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeLumi)
            ) {
                Text(
                    text = if (plan.montoRestante > 0) "Registrar" else "Meta completada",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
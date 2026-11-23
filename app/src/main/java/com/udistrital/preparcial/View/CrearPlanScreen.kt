package com.udistrital.preparcial.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            TopAppBar(
                title = { Text("Crear plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                        onValueChange = { viewModel.nombre = it },
                        label = { Text("Nombre del plan") },
                        placeholder = { Text("Ej. Ahorro para viaje familiar") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.meta,
                        onValueChange = { viewModel.meta = it },
                        label = { Text("Meta de ahorro ($)") },
                        placeholder = { Text("5000000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.fechaLimite,
                        onValueChange = { viewModel.fechaLimite = it },
                        label = { Text("Fecha límite") },
                        placeholder = { Text("DD/MM/AAAA") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
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
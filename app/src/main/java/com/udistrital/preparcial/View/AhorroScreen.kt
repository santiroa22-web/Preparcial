package com.udistrital.preparcial.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.preparcial.Model.PlanAhorro
import com.udistrital.preparcial.Viewmodel.AhorroViewModel
import com.udistrital.preparcial.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AhorroScreen(
    plan: PlanAhorro,
    viewModel: AhorroViewModel,
    onBack: () -> Unit,
    onRegistrarPagoClick: () -> Unit
) {
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }
    val progreso = viewModel.calcularProgreso(plan)

    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            TopAppBar(
                title = { Text(plan.nombre, fontWeight = FontWeight.Bold) },
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
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoTarjeta),
                border = BorderStroke(1.dp, BordeSuave)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total ahorrado", style = MaterialTheme.typography.labelLarge, color = TextoGris)
                    Text(
                        text = formatoMoneda.format(plan.totalAhorrado),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                    Text(
                        text = "de ${formatoMoneda.format(plan.meta)} • Límite: ${plan.fechaLimite}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoGris
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50)),
                        color = VerdeLumi,
                        trackColor = VerdeSuave
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Aportes realizados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextoOscuro
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(plan.aportes) { aporte ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoTarjeta)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(aporte.integrante, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                                Text(aporte.fecha, style = MaterialTheme.typography.bodySmall, color = TextoGris)
                            }
                            Text(
                                text = "+ ${formatoMoneda.format(aporte.monto)}",
                                fontWeight = FontWeight.Bold,
                                color = VerdeLumi
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRegistrarPagoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeLumi)
            ) {
                Text("Registrar pago", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
package com.udistrital.preparcial.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.preparcial.Model.PlanAhorro
import com.udistrital.preparcial.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    planes: List<PlanAhorro>,
    onCrearPlanClick: () -> Unit,
    onPlanClick: (PlanAhorro) -> Unit
) {
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GrisFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Plan Familiar\nde Ahorro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCrearPlanClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeLumi)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear plan", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Planes de ahorro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextoOscuro
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(planes) { plan ->
                    val progreso = (plan.totalAhorrado / plan.meta).toFloat().coerceIn(0f, 1f)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlanClick(plan) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoTarjeta),
                        border = BorderStroke(1.dp, BordeSuave)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = plan.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoOscuro
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Meta: ${formatoMoneda.format(plan.meta)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoGris
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { progreso },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = VerdeLumi,
                                trackColor = VerdeSuave
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ahorrado: ${formatoMoneda.format(plan.totalAhorrado)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = VerdeOscuro
                            )
                        }
                    }
                }
            }
        }
    }
}
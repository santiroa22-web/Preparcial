package com.udistrital.preparcial.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.Aporte
import com.udistrital.preparcial.Model.FechaUtils
import com.udistrital.preparcial.Model.PlanAhorro
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

class RegistroViewModel : ViewModel() {
    var integrante by mutableStateOf("")
    var monto by mutableStateOf("")
    var fecha by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)

    fun construirAporte(plan: PlanAhorro): Aporte? {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }

        if (integrante.isBlank()) {
            mensajeError = "Ingresa el nombre del integrante"
            return null
        }

        val montoValor = monto.toDoubleOrNull()
        if (montoValor == null || montoValor <= 0) {
            mensajeError = "Ingresa un monto válido mayor a $0"
            return null
        }

        // Validación 1: El monto no puede superar lo que falta para cumplir la meta
        if (montoValor > plan.montoRestante) {
            mensajeError = "El monto supera lo que falta (${formatoMoneda.format(plan.montoRestante)})"
            return null
        }

        if (fecha.isBlank()) {
            mensajeError = "Selecciona la fecha del pago"
            return null
        }

        // Validación 2: La fecha del pago debe ser igual o anterior a la fecha límite del objetivo
        val millisPago = FechaUtils.fechaToMillis(fecha)
        val millisLimite = FechaUtils.fechaToMillis(plan.fechaLimite)
        if (millisPago != null && millisLimite != null && millisPago > millisLimite) {
            mensajeError = "La fecha no puede ser posterior al límite (${plan.fechaLimite})"
            return null
        }

        val nuevo = Aporte(
            id = UUID.randomUUID().toString(),
            integrante = integrante.trim(),
            monto = montoValor,
            fecha = fecha
        )
        integrante = ""
        monto = ""
        fecha = ""
        mensajeError = null
        return nuevo
    }
}
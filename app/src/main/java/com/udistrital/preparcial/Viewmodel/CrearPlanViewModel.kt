package com.udistrital.preparcial.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.PlanAhorro
import java.util.UUID

class CrearPlanViewModel : ViewModel() {
    var nombre by mutableStateOf("")
    var meta by mutableStateOf("")
    var fechaLimite by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)

    fun construirPlan(): PlanAhorro? {
        val metaValor = meta.toDoubleOrNull()
        if (nombre.isBlank()) {
            mensajeError = "Ingresa un nombre para el plan"
            return null
        }
        if (metaValor == null || metaValor <= 0) {
            mensajeError = "Ingresa una meta válida mayor a $0"
            return null
        }
        if (fechaLimite.isBlank()) {
            mensajeError = "Selecciona una fecha límite"
            return null
        }

        val nuevo = PlanAhorro(
            id = UUID.randomUUID().toString(),
            nombre = nombre.trim(),
            meta = metaValor,
            fechaLimite = fechaLimite
        )
        nombre = ""
        meta = ""
        fechaLimite = ""
        mensajeError = null
        return nuevo
    }
}
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

    fun construirPlan(): PlanAhorro? {
        val metaValor = meta.toDoubleOrNull() ?: return null
        if (nombre.isBlank() || fechaLimite.isBlank()) return null

        val nuevo = PlanAhorro(
            id = UUID.randomUUID().toString(),
            nombre = nombre.trim(),
            meta = metaValor,
            fechaLimite = fechaLimite.trim()
        )
        // Limpiar campos
        nombre = ""
        meta = ""
        fechaLimite = ""
        return nuevo
    }
}
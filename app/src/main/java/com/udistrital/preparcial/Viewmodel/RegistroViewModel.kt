package com.udistrital.preparcial.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.Aporte
import java.util.UUID

class RegistroViewModel : ViewModel() {
    var integrante by mutableStateOf("")
    var monto by mutableStateOf("")
    var fecha by mutableStateOf("")

    fun construirAporte(): Aporte? {
        val montoValor = monto.toDoubleOrNull() ?: return null
        if (integrante.isBlank() || fecha.isBlank()) return null

        val nuevo = Aporte(
            id = UUID.randomUUID().toString(),
            integrante = integrante.trim(),
            monto = montoValor,
            fecha = fecha.trim()
        )
        integrante = ""
        monto = ""
        fecha = ""
        return nuevo
    }
}
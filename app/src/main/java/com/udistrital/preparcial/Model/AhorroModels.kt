package com.udistrital.preparcial.Model

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Aporte(
    val id: String,
    val integrante: String,
    val monto: Double,
    val fecha: String
)

data class PlanAhorro(
    val id: String,
    val nombre: String,
    val meta: Double,
    val fechaLimite: String,
    val aportes: MutableList<Aporte> = mutableStateListOf()
) {
    val totalAhorrado: Double
        get() = aportes.sumOf { it.monto }

    val montoRestante: Double
        get() = (meta - totalAhorrado).coerceAtLeast(0.0)
}

object FechaUtils {
    private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun millisToFecha(millis: Long): String = formatter.format(Date(millis))

    fun fechaToMillis(fecha: String): Long? = try {
        formatter.parse(fecha)?.time
    } catch (e: Exception) {
        null
    }
}
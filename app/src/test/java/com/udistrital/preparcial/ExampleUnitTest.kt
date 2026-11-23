package com.udistrital.preparcial

import com.udistrital.preparcial.Model.Aporte
import com.udistrital.preparcial.Model.FechaUtils
import com.udistrital.preparcial.Model.PlanAhorro
import com.udistrital.preparcial.Viewmodel.AhorroViewModel
import com.udistrital.preparcial.Viewmodel.CrearPlanViewModel
import com.udistrital.preparcial.Viewmodel.HomeViewmodel
import com.udistrital.preparcial.Viewmodel.RegistroViewModel
import org.junit.Assert.*
import org.junit.Test

class PlanAhorroUnitTest {

    // 1. Pruebas del Modelo y Cálculos
    @Test
    fun `calcular totalAhorrado y montoRestante correctamente`() {
        val plan = PlanAhorro(
            id = "1",
            nombre = "Viaje familiar",
            meta = 1000000.0,
            fechaLimite = "31/03/2027"
        )
        plan.aportes.add(Aporte("a1", "Thiago", 400000.0, "10/01/2027"))
        plan.aportes.add(Aporte("a2", "Majo", 250000.0, "15/02/2027"))

        assertEquals(650000.0, plan.totalAhorrado, 0.01)
        assertEquals(350000.0, plan.montoRestante, 0.01)
    }

    @Test
    fun `calcularProgreso en AhorroViewModel devuelve porcentaje entre 0 y 1`() {
        val ahorroVM = AhorroViewModel()
        val plan = PlanAhorro(
            id = "1",
            nombre = "Fondo emergencia",
            meta = 500000.0,
            fechaLimite = "30/06/2027"
        )
        plan.aportes.add(Aporte("a1", "Thiago", 250000.0, "01/02/2027"))

        val progreso = ahorroVM.calcularProgreso(plan)
        assertEquals(0.5f, progreso, 0.001f)
    }

    // 2. Pruebas de CrearPlanViewModel
    @Test
    fun `construirPlan retorna null y muestra error si faltan datos o meta es invalida`() {
        val vm = CrearPlanViewModel()
        vm.nombre = ""
        vm.meta = "500000"
        vm.fechaLimite = "15/05/2027"

        assertNull(vm.construirPlan())
        assertNotNull(vm.mensajeError)

        vm.nombre = "Nuevo Carro"
        vm.meta = "-100"
        assertNull(vm.construirPlan())
        assertNotNull(vm.mensajeError)
    }

    @Test
    fun `construirPlan exitoso crea el objeto y limpia los campos`() {
        val vm = CrearPlanViewModel()
        vm.nombre = "Viaje a Santa Marta"
        vm.meta = "3000000"
        vm.fechaLimite = "20/03/2027"

        val plan = vm.construirPlan()

        assertNotNull(plan)
        assertEquals("Viaje a Santa Marta", plan?.nombre)
        assertEquals(3000000.0, plan!!.meta, 0.01)
        assertEquals("", vm.nombre)
        assertEquals("", vm.meta)
        assertNull(vm.mensajeError)
    }


    @Test
    fun `construirAporte rechaza monto mayor al saldo restante`() {
        val plan = PlanAhorro(
            id = "1",
            nombre = "Meta corta",
            meta = 500000.0,
            fechaLimite = "30/03/2027"
        )
        plan.aportes.add(Aporte("a1", "Thiago", 300000.0, "10/01/2027"))

        val registroVM = RegistroViewModel()
        registroVM.integrante = "Majo"
        registroVM.monto = "250000"
        registroVM.fecha = "15/02/2027"

        val aporte = registroVM.construirAporte(plan)

        assertNull(aporte)
        assertTrue(registroVM.mensajeError!!.contains("supera lo que falta"))
    }

    @Test
    fun `construirAporte rechaza fecha posterior a la fecha limite del objetivo`() {
        val plan = PlanAhorro(
            id = "1",
            nombre = "Objetivo Marzo",
            meta = 1000000.0,
            fechaLimite = "15/03/2027"
        )

        val registroVM = RegistroViewModel()
        registroVM.integrante = "Thiago"
        registroVM.monto = "200000"
        registroVM.fecha = "10/12/2027" // Diciembre es después de marzo

        val aporte = registroVM.construirAporte(plan)

        assertNull(aporte)
        assertTrue(registroVM.mensajeError!!.contains("no puede ser posterior al límite"))
    }

    @Test
    fun `construirAporte acepta monto exacto restante y fecha igual a la limite`() {
        val plan = PlanAhorro(
            id = "1",
            nombre = "Objetivo Marzo",
            meta = 500000.0,
            fechaLimite = "15/03/2027"
        )
        plan.aportes.add(Aporte("a1", "Thiago", 300000.0, "01/02/2027"))

        val registroVM = RegistroViewModel()
        registroVM.integrante = "Majo"
        registroVM.monto = "200000"
        registroVM.fecha = "15/03/2027"

        val aporte = registroVM.construirAporte(plan)

        assertNotNull(aporte)
        assertEquals(200000.0, aporte!!.monto, 0.01)
        assertNull(registroVM.mensajeError)
    }

    // 4. Pruebas de HomeViewmodel y FechaUtils
    @Test
    fun `HomeViewmodel agrega planes y registra aportes en el plan correcto`() {
        val homeVM = HomeViewmodel()
        assertTrue(homeVM.planes.isEmpty())

        val plan = PlanAhorro("p1", "Ahorro PC", 4000000.0, "01/08/2027")
        homeVM.agregarPlan(plan)
        assertEquals(1, homeVM.planes.size)

        val aporte = Aporte("ap1", "Thiago", 1000000.0, "01/04/2027")
        homeVM.agregarAporte("p1", aporte)

        assertEquals(1000000.0, homeVM.planes.first().totalAhorrado, 0.01)
        assertEquals(3000000.0, homeVM.planes.first().montoRestante, 0.01)
    }

    @Test
    fun `FechaUtils convierte ida y vuelta sin desfase`() {
        val fechaOriginal = "23/11/2026"
        val millis = FechaUtils.fechaToMillis(fechaOriginal)
        assertNotNull(millis)
        assertEquals(fechaOriginal, FechaUtils.millisToFecha(millis!!))
    }
}
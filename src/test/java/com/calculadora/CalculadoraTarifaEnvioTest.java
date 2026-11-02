package com.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraTarifaEnvioTest {

    private CalculadoraTarifaEnvio calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraTarifaEnvio();
    }

    // Descuentos por tipo de cliente

    @Test
    void clienteEstandarNoRecibeDescuento() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 10);

        assertEquals(105, total, 0.001);
    }

    @Test
    void clienteVipRecibeQuincePorCientoDeDescuento() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.VIP, false, 10);

        assertEquals(90, total, 0.001);
    }

    @Test
    void clienteCorporativoConCompraMenorA1000RecibeVeintePorCiento() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.CORPORATIVO, false, 10);

        assertEquals(85, total, 0.001);
    }

    @Test
    void clienteCorporativoConCompraDe1000RecibeTreintaPorCiento() {
        double total = calculadora.calcularTotal(
                1000, TipoCliente.CORPORATIVO, false, 10);

        assertEquals(700, total, 0.001);
    }

    // Cupon

    @Test
    void cuponValidoAplicaCincoPorCientoAdicional() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, true, 10);

        assertEquals(100, total, 0.001);
    }

    @Test
    void cuponInvalidoNoAplicaDescuento() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 10);

        assertEquals(105, total, 0.001);
    }

    // Tarifas y limites de distancia

    @Test
    void distanciaDe50KmTieneEnvioDe5() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 50);

        assertEquals(105, total, 0.001);
    }

    @Test
    void distanciaDe51KmTieneEnvioDe10() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 51);

        assertEquals(110, total, 0.001);
    }

    @Test
    void distanciaDe200KmTieneEnvioDe10() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 200);

        assertEquals(110, total, 0.001);
    }

    @Test
    void distanciaDe201KmTieneEnvioDe20() {
        double total = calculadora.calcularTotal(
                100, TipoCliente.ESTANDAR, false, 201);

        assertEquals(120, total, 0.001);
    }

    // Envio gratis

    @Test
    void compraConMontoDescontadoDe500TieneEnvioGratis() {
        double total = calculadora.calcularTotal(
                500, TipoCliente.ESTANDAR, false, 300);

        assertEquals(500, total, 0.001);
    }

    @Test
    void compraConMontoDescontadoMenorA500PagaEnvio() {
        double total = calculadora.calcularTotal(
                499, TipoCliente.ESTANDAR, false, 300);

        assertEquals(519, total, 0.001);
    }

    // Validaciones

    @Test
    void montoCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        0, TipoCliente.ESTANDAR, false, 10));
    }

    @Test
    void montoNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        -100, TipoCliente.ESTANDAR, false, 10));
    }

    @Test
    void montoNaNLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        Double.NaN, TipoCliente.ESTANDAR, false, 10));
    }

    @Test
    void montoInfinitoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        Double.POSITIVE_INFINITY, TipoCliente.ESTANDAR, false, 10));
    }

    @Test
    void tipoClienteNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        100, null, false, 10));
    }

    @Test
    void distanciaCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        100, TipoCliente.ESTANDAR, false, 0));
    }

    @Test
    void distanciaMayorA500LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcularTotal(
                        100, TipoCliente.ESTANDAR, false, 501));
    }
}

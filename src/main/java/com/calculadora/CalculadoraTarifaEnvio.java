package com.calculadora;

public class CalculadoraTarifaEnvio {

    public double calcularTotal(
            double montoCompra,
            TipoCliente tipoCliente,
            boolean esCuponValido,
            int distanciaKm) {

        validarDatos(montoCompra, tipoCliente, distanciaKm);

        double montoConDescuento = calcularMontoConDescuento(
                montoCompra, tipoCliente, esCuponValido);

        double costoEnvio = calcularCostoEnvio(
                montoConDescuento, montoCompra, tipoCliente, distanciaKm);

        return montoConDescuento + costoEnvio;
    }

    private void validarDatos(
            double montoCompra,
            TipoCliente tipoCliente,
            int distanciaKm) {

        if (!Double.isFinite(montoCompra) || montoCompra <= 0) {
            throw new IllegalArgumentException(
                    "El monto de compra debe ser un número finito mayor que cero");
        }

        if (tipoCliente == null) {
            throw new IllegalArgumentException(
                    "El tipo de cliente no puede ser nulo");
        }

        if (distanciaKm < 1 || distanciaKm > 500) {
            throw new IllegalArgumentException(
                    "La distancia debe estar entre 1 y 500 kilómetros");
        }
    }

    private double calcularMontoConDescuento(
            double montoCompra,
            TipoCliente tipoCliente,
            boolean esCuponValido) {

        double montoConDescuento = switch (tipoCliente) {
            case ESTANDAR -> montoCompra;
            case VIP -> montoCompra * 0.85;
            case CORPORATIVO -> montoCompra < 1000
                    ? montoCompra * 0.80
                    : montoCompra * 0.70;
        };

        if (esCuponValido) {
            montoConDescuento *= 0.95;
        }

        return montoConDescuento;
    }

    private double calcularCostoEnvio(
            double montoConDescuento,
            double montoCompraOriginal,
            TipoCliente tipoCliente,
            int distanciaKm) {

        boolean envioGratisPorMonto = montoConDescuento >= 500;
        boolean envioGratisCorporativo = tipoCliente == TipoCliente.CORPORATIVO
                && montoCompraOriginal >= 1000;

        if (envioGratisCorporativo || envioGratisPorMonto) {
            return 0;
        }

        if (distanciaKm <= 50) {
            return 5;
        }

        if (distanciaKm <= 200) {
            return 10;
        }

        return 20;
    }
}

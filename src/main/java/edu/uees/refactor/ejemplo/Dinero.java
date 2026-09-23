package edu.uees.refactor.ejemplo;

/**
 * Clase de ejemplo AISLADA para practicar {@code assertThrows} sin tocar
 * todavia el contrato heredado de {@code ServicioReservas} ni de
 * {@code Reserva}. No participa del flujo de negocio actual: existe
 * unicamente como ejercicio del Laboratorio 2 (seccion 14 de la guia).
 */
public record Dinero(double valor) {

    public Dinero {
        if (valor < 0) {
            throw new IllegalArgumentException("El dinero no puede ser negativo");
        }
    }
}

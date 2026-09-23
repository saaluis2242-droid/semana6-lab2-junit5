package edu.uees.refactor.ejemplo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Ejercicio de la seccion 14 de la guia del Laboratorio 2: practicar
 * assertThrows sobre una clase aislada, sin modificar el comportamiento
 * heredado de ServicioReservas.
 */
class DineroTest {

    @Test
    void dineroNegativoLanzaExcepcion() {
        // Arrange / Act / Assert
        // (la construccion invalida ES la accion que se esta probando)
        assertThrows(
                IllegalArgumentException.class,
                () -> new Dinero(-1)
        );
    }
}

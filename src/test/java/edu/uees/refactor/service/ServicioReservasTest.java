package edu.uees.refactor.service;

import edu.uees.refactor.domain.EstadoReserva;
import edu.uees.refactor.domain.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Red de seguridad (pruebas de caracterizacion) sobre el codigo heredado
 * de {@link ServicioReservas}, construida en el Laboratorio 2 a partir
 * de la linea base manual del Laboratorio 1 (docs/01_LINEA_BASE.md).
 *
 * <p>Estas pruebas NO afirman que las reglas actuales sean las
 * "correctas" o las "mejores" — afirman cual es el comportamiento
 * observable de hoy, para que una refactorizacion futura (Extract
 * Class, Value Objects, simplificacion de condicionales, etc., a
 * desarrollarse en Ae5) pueda ejecutarse con evidencia de que ese
 * comportamiento no cambio por accidente.</p>
 */
class ServicioReservasTest {

    private ServicioReservas servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioReservas();
    }

    /**
     * Protege: el precio base actual es 40, una reserva NORMAL valida
     * se confirma, y la regla de descuento VIP no afecta a NORMAL.
     */
    @Test
    void normalActualmenteRetornaCuarenta() {
        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertAll(
                () -> assertEquals(40, total, 0.001),
                () -> assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado())
        );
    }

    /**
     * Protege: una reserva VIP valida se confirma y paga el precio base
     * con el 15% de descuento actual (40 * 0.85 = 34). No afirma que
     * 15% sea la politica ideal, solo que es la vigente hoy.
     */
    @Test
    void vipActualmenteRetornaTreintaYCuatro() {
        // Arrange
        Reserva reserva = reservaVipValida();

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertAll(
                () -> assertEquals(34, total, 0.001),
                () -> assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado())
        );
    }

    /**
     * Protege: un correo sin "@" es rechazado (retorno 0) y la reserva
     * NO se confirma. Riesgo a futuro: si se introduce un Value Object
     * Correo que valida en el constructor, esta prueba podria empezar
     * a fallar (o a lanzar excepcion antes de llegar aqui), lo cual
     * indicaria un cambio de contrato observable, no una refactorizacion
     * pura.
     */
    @Test
    void correoInvalidoNoProcesaReserva() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        Reserva reserva = new Reserva(
                "R-EMAIL", "correo-invalido", inicio, inicio.plusHours(1), "NORMAL");

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertAll(
                () -> assertEquals(0, total, 0.001),
                () -> assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado())
        );
    }

    /**
     * Protege: un periodo donde `fin` no es posterior a `inicio` es
     * rechazado (retorno 0) y la reserva no se confirma.
     */
    @Test
    void periodoConFinAnteriorNoProcesa() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        Reserva reserva = new Reserva(
                "R-PERIODO", "ana@uees.edu.ec", inicio, inicio.minusHours(1), "NORMAL");

        // Act
        double total = servicio.procesar(reserva, 5);

        // Assert
        assertAll(
                () -> assertEquals(0, total, 0.001),
                () -> assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado())
        );
    }

    /**
     * Protege el limite exacto: 2 horas de anticipacion SI permiten
     * procesar (la condicion actual es `horasAnticipacion < 2`, es
     * decir, 2 es el limite inclusivo valido).
     */
    @Test
    void dosHorasExactasPermitenProcesar() {
        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 2);

        // Assert
        assertAll(
                () -> assertEquals(40, total, 0.001),
                () -> assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado())
        );
    }

    /**
     * Protege el limite exacto complementario: 1 hora de anticipacion
     * NO permite procesar. Junto con el caso de 2h, fija la frontera
     * exacta `< 2` para detectar errores de < vs <= si se simplifican
     * las validaciones en el futuro.
     */
    @Test
    void unaHoraNoPermiteProcesar() {
        // Arrange
        Reserva reserva = reservaNormalValida();

        // Act
        double total = servicio.procesar(reserva, 1);

        // Assert
        assertAll(
                () -> assertEquals(0, total, 0.001),
                () -> assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado())
        );
    }

    /**
     * Caso especial adicional: hoy, pasar una Reserva nula retorna 0 de
     * forma silenciosa en vez de lanzar una excepcion. Se caracteriza
     * este comportamiento (aunque sea discutible) para que quede
     * registrado como una decision explicita a revisar en Ae5, no como
     * un olvido.
     */
    @Test
    void reservaNulaRetornaCero() {
        // Arrange
        Reserva reservaNula = null;

        // Act
        double total = servicio.procesar(reservaNula, 5);

        // Assert
        assertEquals(0, total, 0.001);
    }

    private Reserva reservaNormalValida() {
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        return new Reserva("R-NORMAL", "ana@uees.edu.ec", inicio, inicio.plusHours(1), "NORMAL");
    }

    private Reserva reservaVipValida() {
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 20, 10, 0);
        return new Reserva("R-VIP", "vip@uees.edu.ec", inicio, inicio.plusHours(1), "VIP");
    }
}

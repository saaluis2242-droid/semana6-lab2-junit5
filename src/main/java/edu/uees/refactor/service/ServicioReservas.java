package edu.uees.refactor.service;

import edu.uees.refactor.domain.Reserva;

/**
 * Código heredado intencional para el Laboratorio 1 y 2.
 *
 * IMPORTANTE:
 * No refactorizar antes de completar la línea base,
 * el diagnóstico y el plan de refactorización.
 *
 * Laboratorio 2 (protegido por ServicioReservasTest): se extrajo el
 * cálculo de la tarifa a {@link #calcularTotal(Reserva)}. Es una
 * refactorización de Extract Method pura: no cambia ninguna regla,
 * solo le da un nombre con intención a un fragmento que antes vivía
 * mezclado con la validación, la persistencia simulada y la
 * notificación simulada dentro de {@link #procesar(Reserva, int)}.
 */
public class ServicioReservas {

    public double procesar(
            Reserva r,
            int horasAnticipacion) {

        if (r == null) {
            return 0;
        }

        if (r.getCorreo() == null
                || !r.getCorreo().contains("@")) {
            return 0;
        }

        if (r.getInicio() == null
                || r.getFin() == null
                || !r.getFin().isAfter(r.getInicio())) {
            return 0;
        }

        if (horasAnticipacion < 2) {
            return 0;
        }

        double total = calcularTotal(r);

        System.out.println(
                "Guardando reserva " + r.getId()
        );

        System.out.println(
                "Correo enviado a " + r.getCorreo()
        );

        r.confirmar();

        return total;
    }

    private double calcularTotal(Reserva r) {
        double total = 40;

        if ("VIP".equals(r.getTipo())) {
            return total * 0.85;
        }

        return total;
    }
}

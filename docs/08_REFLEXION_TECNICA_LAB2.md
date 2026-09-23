# Reflexión técnica final | Laboratorio 2

Extensión: 250–350 palabras.

## 1. ¿Qué prueba te dio mayor confianza y por qué?

`vipActualmenteRetornaTreintaYCuatro()`, porque es la que efectivamente detectó una regresión real durante el experimento de la sección 19: al cambiar `0.85` por `0.80`, fue la única prueba que falló, con un mensaje claro (`esperado: <34.0> pero fue: <32.0>`). Que una prueba reaccione exactamente ante el cambio que se supone debe vigilar es mejor evidencia de su valor que cualquier razonamiento a priori.

## 2. ¿Qué cambio de diseño podría alterar el contrato de correo inválido?

Introducir un Value Object `Correo` que valide el formato en su propio constructor. Hoy `correoInvalidoNoProcesaReserva()` espera `retorno 0` y `estado PENDIENTE`; si `Correo` lanza `IllegalArgumentException` al construirse, esa excepción ocurriría *antes* de llegar a `ServicioReservas.procesar()`, y la prueba empezaría a fallar (o habría que reescribirla para esperar la excepción). Eso sería un cambio de contrato observable, no una refactorización pura, y debe decidirse explícitamente en Ae5.

## 3. ¿Por qué los casos de 1h y 2h son más útiles que probar solo 5h?

Porque 5h está lejos de la condición `horasAnticipacion < 2` y no ejercita la frontera. Los casos de 1h y 2h prueban exactamente el límite de la comparación: si alguien simplifica la validación y cambia `<` por `<=` por error, una prueba con 5h seguiría pasando sin detectar nada, mientras que el par 1h/2h falla de inmediato.

## 4. ¿Qué diferencia observaste entre prueba de caracterización y nueva funcionalidad?

Una prueba de caracterización documenta lo que el código *ya hace*, sin opinar si es correcto (por ejemplo, `reservaNulaRetornaCero()` no defiende que "0 silencioso" sea una buena decisión de diseño, solo la registra). Una prueba de funcionalidad nueva definiría un comportamiento *deseado* que todavía no existe. Confundirlas es peligroso: se podría "arreglar" con una refactorización algo que en realidad requería una decisión de producto.

## 5. ¿Qué evidencia concreta mostrarías para demostrar que Extract Method preservó comportamiento?

Las tres capturas de `docs/evidencias/`: la suite en 8/8 antes del refactor (`lab2-suite-antes-refactor.txt`), la suite en 8/8 después de extraer `calcularTotal()` (`lab2-suite-despues-refactor.txt`), y la suite en 7/8 con la regresión intencional (`lab2-suite-regresion-intencional.txt`), que confirma que la red de seguridad sí reacciona cuando algo cambia de verdad.

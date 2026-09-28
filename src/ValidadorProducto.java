// ============================================================
// CLASE VALIDADOR PRODUCTO
// ============================================================

// ValidadorProducto administra las validaciones
// de los datos escritos en los formularios.
public class ValidadorProducto {

    // ResultadoValidacion guarda el resultado
    // de validar los datos de un producto.
    public static class ResultadoValidacion {

        // Guardamos si los datos son correctos.
        private boolean valido;

        // Guardamos el mensaje de error si existe.
        private String mensaje;

        // Guardamos el campo que debe recibir el cursor.
        private String campoAFocalizar;

        // Guardamos el precio convertido a double.
        private double precio;

        // Guardamos el stock convertido a int.
        private int stock;

        // Constructor privado para crear un resultado de validación.
        private ResultadoValidacion(
                boolean valido,
                String mensaje,
                String campoAFocalizar,
                double precio,
                int stock
        ) {
            this.valido = valido;
            this.mensaje = mensaje;
            this.campoAFocalizar = campoAFocalizar;
            this.precio = precio;
            this.stock = stock;
        }

        // Devuelve si la validación fue correcta.
        public boolean esValido() {
            return valido;
        }

        // Devuelve el mensaje que se debe mostrar al usuario.
        public String getMensaje() {
            return mensaje;
        }

        // Devuelve el campo que debe recibir el cursor.
        public String getCampoAFocalizar() {
            return campoAFocalizar;
        }

        // Devuelve el precio ya convertido.
        public double getPrecio() {
            return precio;
        }

        // Devuelve el stock ya convertido.
        public int getStock() {
            return stock;
        }
    }

    // validar recibe los textos del formulario
    // y devuelve si representan un producto válido.
    public static ResultadoValidacion validar(
            String nombre,
            String precioTexto,
            String stockTexto
    ) {

        // Validamos que el nombre no esté vacío.
        if (nombre.isEmpty()) {
            return new ResultadoValidacion(
                    false,
                    "Debe ingresar el nombre del producto.",
                    "nombre",
                    0,
                    0
            );
        }

        // Declaramos las variables para los valores convertidos.
        double precio;
        int stock;

        // Convertimos el texto del precio a double.
        try {
            precio = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
            return new ResultadoValidacion(
                    false,
                    "El precio debe ser un número válido.",
                    "precio",
                    0,
                    0
            );
        }

        // Convertimos el texto del stock a int.
        try {
            stock = Integer.parseInt(stockTexto);
        } catch (NumberFormatException e) {
            return new ResultadoValidacion(
                    false,
                    "El stock debe ser un número entero.",
                    "stock",
                    0,
                    0
            );
        }

        // Validamos que el precio sea mayor que cero.
        if (precio <= 0) {
            return new ResultadoValidacion(
                    false,
                    "El precio debe ser mayor que cero.",
                    null,
                    0,
                    0
            );
        }

        // Validamos que el stock no sea negativo.
        if (stock < 0) {
            return new ResultadoValidacion(
                    false,
                    "El stock no puede ser negativo.",
                    null,
                    0,
                    0
            );
        }

        // Devolvemos los valores convertidos cuando todo es válido.
        return new ResultadoValidacion(
                true,
                null,
                null,
                precio,
                stock
        );
    }

    // validarParaEdicion conserva las reglas y los mensajes
    // que utiliza el diálogo para editar un producto.
    public static ResultadoValidacion validarParaEdicion(
            String nombre,
            String precioTexto,
            String stockTexto
    ) {

        // Validamos que todos los campos estén completos.
        if (nombre.isEmpty()
                || precioTexto.isEmpty()
                || stockTexto.isEmpty()) {
            return new ResultadoValidacion(
                    false,
                    "Debe completar todos los campos.",
                    null,
                    0,
                    0
            );
        }

        // Reutilizamos la validación general para convertir los datos.
        ResultadoValidacion resultado = validar(
                nombre,
                precioTexto,
                stockTexto
        );

        // Si el precio o el stock no cumplen su rango, usamos el mensaje de edición.
        if (!resultado.esValido()
                && resultado.getCampoAFocalizar() == null) {
            return new ResultadoValidacion(
                    false,
                    "El precio debe ser mayor que cero y el stock no puede ser negativo.",
                    "precio",
                    0,
                    0
            );
        }

        // Devolvemos el resultado cuando no hay error de rango.
        return resultado;
    }
}

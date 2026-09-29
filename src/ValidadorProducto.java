// CLASE VALIDADOR PRODUCTO
// administra las validaciones de los datos escritos en los formularios.
public class ValidadorProducto {
    //guarda el resultado de validar los datos de un producto.
    public static class ResultadoValidacion {
        private boolean valido;
        private String mensaje;
        private String campoAFocalizar;
        private double precio;
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
// Devuelve si los datos son válidos.
        public boolean esValido() {
            return valido;
        }
        public String getMensaje() {
            return mensaje;
        }
        public String getCampoAFocalizar() {
            return campoAFocalizar;
        }
        public double getPrecio() {
            return precio;
        }
        public int getStock() {
            return stock;
        }
    }

    // validar recibe los textos del formulario y devuelve un resultado
    public static ResultadoValidacion validar(
            String nombre,
            String precioTexto,
            String stockTexto
    ) {

        if (nombre.isEmpty()) {
            return new ResultadoValidacion(
                    false,
                    "Debe ingresar el nombre del producto.",
                    "nombre",
                    0,
                    0
            );
        }

        double precio;
        int stock;
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

        if (precio <= 0) {
            return new ResultadoValidacion(
                    false,
                    "El precio debe ser mayor que cero.",
                    null,
                    0,
                    0
            );
        }
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

    // validarParaEdicion conserva las reglas y los mensajes que utiliza el diálogo
    public static ResultadoValidacion validarParaEdicion(
            String nombre,
            String precioTexto,
            String stockTexto
    ) {
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
        return resultado;
    }
}

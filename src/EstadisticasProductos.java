// CLASE ESTADISTICAS PRODUCTOS
// se encarga de calcular información estadística a partir de los productos de la tabla.
public class EstadisticasProductos {
    private ModeloTablaProductos modelo;

    // Constructor.
    public EstadisticasProductos(ModeloTablaProductos modelo) {
        this.modelo = modelo;
    }
    // Devuelve la cantidad de productos registrados.
    public int cantidadProductos() {
        return modelo.getRowCount();
    }

    // Devuelve la cantidad total de unidades.
    public int cantidadUnidades() {
        int totalUnidades = 0;
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {

            Number stock = (Number) modelo.getValueAt(
                    fila,
                    ModeloTablaProductos.COLUMNA_STOCK
            );
            totalUnidades += stock.intValue();
        }
        return totalUnidades;
    }

    // Devuelve el valor total de todos los productos.
    public double valorTotalInventario() {
        return modelo.calcularTotal();
    }
    //valor por categoria
    public String valoresPorCategoria() {
    StringBuilder resultado = new StringBuilder();
    for (int fila = 0; fila < modelo.getRowCount(); fila++) {
        String categoria =
                modelo.getValueAt(
                        fila,
                        ModeloTablaProductos.COLUMNA_CATEGORIA
                ).toString();
        boolean yaMostrada = resultado
                .toString()
                .contains(categoria + ":");
        if (!yaMostrada) {
            double total = 0;
            for (int otraFila = 0;
                    otraFila < modelo.getRowCount();
                    otraFila++) {
                String otraCategoria =
                        modelo.getValueAt(
                                otraFila,
                                ModeloTablaProductos.COLUMNA_CATEGORIA
                        ).toString();
                if (otraCategoria.equals(categoria)) {
                    Number valor =
                            (Number) modelo.getValueAt(
                                    otraFila,
                                    ModeloTablaProductos.COLUMNA_VALOR_STOCK
                            );
                    total += valor.doubleValue();
                }
            }
            resultado.append(
                    categoria
                    + ": $"
                    + String.format("%.2f", total)
                    + "\n"
            );
        }
    }
    return resultado.toString();
}
}
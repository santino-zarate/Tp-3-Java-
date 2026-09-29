// CLASE ESTADISTICAS PRODUCTOS
// EstadisticasProductos se encarga de calcular
// información estadística a partir de los productos de la tabla.
public class EstadisticasProductos {

    // Modelo que contiene los productos.
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
}
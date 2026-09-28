// Importamos DefaultTableModel para administrar las filas y columnas de la tabla.
import javax.swing.table.DefaultTableModel;

// ============================================================
// CLASE MODELO TABLA PRODUCTOS
// ============================================================

// ModeloTablaProductos administra los datos
// que se muestran en la tabla de productos.
public class ModeloTablaProductos extends DefaultTableModel {

    // Definimos los nombres de las columnas de la tabla.
    private static final String[] COLUMNAS = {
            "Nombre",
            "Precio",
            "Stock",
            "Categoría",
            "Valor Stock"
    };

    // Creamos el modelo sin filas iniciales.
    public ModeloTablaProductos() {
        super(COLUMNAS, 0);
    }

    // Impedimos que las celdas se editen directamente en la tabla.
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }

    // agregarProducto transforma un Producto en una fila de la tabla.
    public void agregarProducto(Producto producto) {
        addRow(new Object[] {
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getValorStock()
        });
    }

    // actualizarProducto reemplaza los datos de una fila existente.
    public void actualizarProducto(int fila, Producto producto) {
        setValueAt(producto.getNombre(), fila, 0);
        setValueAt(producto.getPrecio(), fila, 1);
        setValueAt(producto.getStock(), fila, 2);
        setValueAt(producto.getCategoria(), fila, 3);
        setValueAt(producto.getValorStock(), fila, 4);
    }

    // eliminarProducto elimina una fila según su índice dentro del modelo.
    public void eliminarProducto(int fila) {
        removeRow(fila);
    }

    // calcularTotal suma el valor de stock de todos los productos.
    public double calcularTotal() {

        // Comenzamos el total en cero.
        double total = 0;

        // Recorremos todas las filas del modelo.
        for (int fila = 0; fila < getRowCount(); fila++) {

            // Obtenemos el valor de stock de la quinta columna.
            Number valor = (Number) getValueAt(fila, 4);

            // Sumamos el valor al total general.
            total += valor.doubleValue();
        }

        // Devolvemos el total calculado.
        return total;
    }
}

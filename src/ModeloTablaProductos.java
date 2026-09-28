// Importamos DefaultTableModel para administrar las filas y columnas de la tabla.
import javax.swing.table.DefaultTableModel;

// Importamos Arrays para ordenar las filas antes de eliminarlas.
import java.util.Arrays;

// ============================================================
// CLASE MODELO TABLA PRODUCTOS
// ============================================================

// ModeloTablaProductos administra los datos
// que se muestran en la tabla de productos.
public class ModeloTablaProductos extends DefaultTableModel {

    // Definimos los índices de las columnas para evitar números mágicos.
    public static final int COLUMNA_SELECCION = 0;
    public static final int COLUMNA_NOMBRE = 1;
    public static final int COLUMNA_PRECIO = 2;
    public static final int COLUMNA_STOCK = 3;
    public static final int COLUMNA_CATEGORIA = 4;
    public static final int COLUMNA_VALOR_STOCK = 5;

    // Definimos los nombres de las columnas de la tabla.
    private static final String[] COLUMNAS = {
            "Sel.",
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

    // Devolvemos Boolean para que la tabla dibuje un checkbox en Selección.
    @Override
    public Class<?> getColumnClass(int columna) {
        if (columna == COLUMNA_SELECCION) {
            return Boolean.class;
        }

        return super.getColumnClass(columna);
    }

    // Permitimos editar solamente los checkboxes de Selección.
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return columna == COLUMNA_SELECCION;
    }

    // agregarProducto transforma un Producto en una fila de la tabla.
    public void agregarProducto(Producto producto) {
        addRow(new Object[] {
                false,
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getValorStock()
        });
    }

    // actualizarProducto reemplaza los datos de una fila existente.
    public void actualizarProducto(int fila, Producto producto) {
        setValueAt(producto.getNombre(), fila, COLUMNA_NOMBRE);
        setValueAt(producto.getPrecio(), fila, COLUMNA_PRECIO);
        setValueAt(producto.getStock(), fila, COLUMNA_STOCK);
        setValueAt(producto.getCategoria(), fila, COLUMNA_CATEGORIA);
        setValueAt(producto.getValorStock(), fila, COLUMNA_VALOR_STOCK);
    }

    // obtenerProducto crea un Producto usando los datos de una fila del modelo.
    public Producto obtenerProducto(int fila) {
        return new Producto(
                getValueAt(fila, COLUMNA_NOMBRE).toString(),
                ((Number) getValueAt(fila, COLUMNA_PRECIO)).doubleValue(),
                ((Number) getValueAt(fila, COLUMNA_STOCK)).intValue(),
                getValueAt(fila, COLUMNA_CATEGORIA).toString()
        );
    }

    // estaSeleccionada indica si el checkbox de una fila está tildado.
    public boolean estaSeleccionada(int fila) {
        return Boolean.TRUE.equals(getValueAt(fila, COLUMNA_SELECCION));
    }

    // eliminarFilasSeleccionadas borra las filas indicadas desde el final.
    public void eliminarFilasSeleccionadas(int[] filasModelo) {

        // Copiamos y ordenamos los índices para borrar de mayor a menor.
        int[] filasOrdenadas = Arrays.copyOf(
                filasModelo,
                filasModelo.length
        );
        Arrays.sort(filasOrdenadas);

        // Recorremos al revés para que los índices no se corran.
        for (int indice = filasOrdenadas.length - 1; indice >= 0; indice--) {
            int fila = filasOrdenadas[indice];

            // Borramos solo si el checkbox continúa seleccionado.
            if (estaSeleccionada(fila)) {
                removeRow(fila);
            }
        }
    }

    // getColumnasBuscables devuelve las columnas de datos sin Selección.
    public static int[] getColumnasBuscables() {
        return new int[] {
                COLUMNA_NOMBRE,
                COLUMNA_PRECIO,
                COLUMNA_STOCK,
                COLUMNA_CATEGORIA,
                COLUMNA_VALOR_STOCK
        };
    }

    // calcularTotal suma el valor de stock de todos los productos.
    public double calcularTotal() {

        // Comenzamos el total en cero.
        double total = 0;

        // Recorremos todas las filas del modelo.
        for (int fila = 0; fila < getRowCount(); fila++) {

            // Obtenemos el valor de stock de la columna correspondiente.
            Number valor = (Number) getValueAt(
                    fila,
                    COLUMNA_VALOR_STOCK
            );

            // Sumamos el valor al total general.
            total += valor.doubleValue();
        }

        // Devolvemos el total calculado.
        return total;
    }
}

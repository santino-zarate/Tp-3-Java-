// Importamos JTable para consultar las filas visibles al usuario.
import javax.swing.JTable;

// Importamos ArrayList para guardar los índices seleccionados.
import java.util.ArrayList;
import java.util.List;

// ============================================================
// CLASE SELECTOR FILAS PRODUCTOS
// ============================================================

// SelectorFilasProductos administra la selección visible
// cuando la tabla puede estar filtrada por el buscador.
public class SelectorFilasProductos {

    // JTable permite convertir índices visibles a índices del modelo.
    private JTable tabla;

    // ModeloTablaProductos permite consultar y eliminar los checks.
    private ModeloTablaProductos modelo;

    // Creamos el selector usando la tabla y su modelo de productos.
    public SelectorFilasProductos(
            JTable tabla,
            ModeloTablaProductos modelo
    ) {
        this.tabla = tabla;
        this.modelo = modelo;
    }

    // haySeleccionadosVisibles indica si existe algún checkbox visible tildado.
    public boolean haySeleccionadosVisibles() {
        return cantidadSeleccionadosVisibles() > 0;
    }

    // cantidadSeleccionadosVisibles devuelve los checks tildados que se ven.
    public int cantidadSeleccionadosVisibles() {
        return obtenerFilasSeleccionadasVisibles().length;
    }

    // eliminarSeleccionadosVisibles borra solamente los checks visibles tildados.
    public void eliminarSeleccionadosVisibles() {
        modelo.eliminarFilasSeleccionadas(
                obtenerFilasSeleccionadasVisibles()
        );
    }

    // obtenerFilasSeleccionadasVisibles convierte filas de vista a modelo.
    private int[] obtenerFilasSeleccionadasVisibles() {

        // Guardamos los índices reales de las filas tildadas.
        List<Integer> filasSeleccionadas = new ArrayList<>();

        // Recorremos únicamente las filas que están visibles en la tabla.
        for (int filaVista = 0; filaVista < tabla.getRowCount(); filaVista++) {
            int filaModelo = tabla.convertRowIndexToModel(filaVista);

            // Agregamos la fila real cuando su checkbox está tildado.
            if (modelo.estaSeleccionada(filaModelo)) {
                filasSeleccionadas.add(filaModelo);
            }
        }

        // Convertimos la lista en un arreglo para el modelo.
        int[] filasModelo = new int[filasSeleccionadas.size()];
        for (int indice = 0; indice < filasSeleccionadas.size(); indice++) {
            filasModelo[indice] = filasSeleccionadas.get(indice);
        }

        // Devolvemos los índices reales de las filas seleccionadas visibles.
        return filasModelo;
    }
}

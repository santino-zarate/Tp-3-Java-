import javax.swing.JTable;
import java.util.ArrayList;
import java.util.List;
// CLASE SELECTOR FILAS PRODUCTOS
// administra la selección visible cuando la tabla puede estar filtrada por el buscador.
public class SelectorFilasProductos {
    // convertir índices visibles a índices del modelo.
    private JTable tabla;
    // consultar y eliminar los checks.
    private ModeloTablaProductos modelo;
    // Creamos el selector usando la tabla y su modelo de productos.
    public SelectorFilasProductos(
            JTable tabla,
            ModeloTablaProductos modelo
    ) {
        this.tabla = tabla;
        this.modelo = modelo;
    }
    public boolean haySeleccionadosVisibles() {
        return cantidadSeleccionadosVisibles() > 0;
    }
    public int cantidadSeleccionadosVisibles() {
        return obtenerFilasSeleccionadasVisibles().length;
    }
// elimina las filas tildadas 
    public void eliminarSeleccionadosVisibles() {
        modelo.eliminarFilasSeleccionadas(
                obtenerFilasSeleccionadasVisibles()
        );
    }
    //convierte filas de vista a modelo.
    private int[] obtenerFilasSeleccionadasVisibles() {
        // Guardamos los índices reales de las filas tildadas.
        List<Integer> filasSeleccionadas = new ArrayList<>();
        // Recorremos únicamente las filas que están visibles en la tabla.
        for (int filaVista = 0; filaVista < tabla.getRowCount(); filaVista++) {
            int filaModelo = tabla.convertRowIndexToModel(filaVista);

            if (modelo.estaSeleccionada(filaModelo)) {
                filasSeleccionadas.add(filaModelo);
            }
        }
        int[] filasModelo = new int[filasSeleccionadas.size()];
        for (int indice = 0; indice < filasSeleccionadas.size(); indice++) {
            filasModelo[indice] = filasSeleccionadas.get(indice);
        }
        return filasModelo;
    }
}

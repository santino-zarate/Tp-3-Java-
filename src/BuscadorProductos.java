// Importamos los componentes necesarios para crear el buscador.
import javax.swing.*;

// Importamos clases para detectar cambios en el campo de búsqueda.
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Importamos TableRowSorter para filtrar las filas de la tabla.
import javax.swing.table.TableRowSorter;

// Importamos clases para organizar el panel del buscador.
import java.awt.*;

// Importamos Pattern para tratar los caracteres especiales como texto.
import java.util.regex.Pattern;

// ============================================================
// CLASE BUSCADOR PRODUCTOS
// ============================================================

// BuscadorProductos administra el campo de búsqueda
// y el filtro aplicado sobre la tabla de productos.
public class BuscadorProductos {

    // Campo donde el usuario escribe el texto a buscar.
    private JTextField txtBuscar;

    // TableRowSorter administra el filtrado de las filas.
    private TableRowSorter<ModeloTablaProductos> sorter;

    // Panel que contiene la etiqueta y el campo de búsqueda.
    private JPanel panelBusqueda;

    // Creamos el buscador y lo asociamos a la tabla recibida.
    public BuscadorProductos(JTable tabla, ModeloTablaProductos modelo) {

        // Creamos el sorter utilizando el modelo de productos.
        sorter = new TableRowSorter<>(modelo);

        // Asociamos el sorter a la tabla para aplicar los filtros.
        tabla.setRowSorter(sorter);

        // Construimos los componentes visuales del buscador.
        crearInterfaz();
    }

    // crearInterfaz construye el campo y escucha sus cambios.
    private void crearInterfaz() {

        // Creamos el campo donde se escribe la búsqueda.
        txtBuscar = new JTextField();

        // Aplicamos nuevamente el filtro cada vez que cambia el texto.
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarProductos();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarProductos();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarProductos();
            }
        });

        // Creamos el panel que agrupa la etiqueta y el campo.
        panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBorder(
                BorderFactory.createEmptyBorder(0, 15, 10, 15)
        );
        panelBusqueda.add(new JLabel("Buscar producto:"), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
    }

    // filtrarProductos aplica el texto escrito sobre todas las columnas.
    private void filtrarProductos() {

        // Obtenemos el texto y quitamos espacios innecesarios.
        String textoBusqueda = txtBuscar.getText().trim();

        // Si el campo está vacío, mostramos nuevamente todos los productos.
        if (textoBusqueda.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        // quote evita que caracteres especiales se interpreten como regex.
        // (?i) permite buscar sin diferenciar mayúsculas de minúsculas.
        sorter.setRowFilter(RowFilter.regexFilter(
                "(?i)" + Pattern.quote(textoBusqueda)
        ));
    }

    // getPanelBusqueda devuelve el panel visual del buscador.
    public JPanel getPanelBusqueda() {
        return panelBusqueda;
    }
}

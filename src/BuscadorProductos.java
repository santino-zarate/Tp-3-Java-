import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;

// CLASE BUSCADOR PRODUCTOS
// administra el campo de búsqueda y el filtro aplicado sobre la tabla de productos.
public class BuscadorProductos {
    private JTextField txtBuscar;
    private TableRowSorter<ModeloTablaProductos> sorter;
    private JPanel panelBusqueda;

    // Creamos el buscador y lo asociamos a la tabla recibida.
    public BuscadorProductos(JTable tabla, ModeloTablaProductos modelo) {
        // Creamos el sorter utilizando el modelo de productos.
        sorter = new TableRowSorter<>(modelo);
        // Asociamos el sorter a la tabla para aplicar los filtros.
        tabla.setRowSorter(sorter);
        crearInterfaz();
    }

    // crearInterfaz construye el campo y escucha sus cambios.
    private void crearInterfaz() {
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
                "(?i)" + Pattern.quote(textoBusqueda),
                ModeloTablaProductos.getColumnasBuscables()
        ));
    }
    public JPanel getPanelBusqueda() {
        return panelBusqueda;
    }
}

// Importamos los componentes necesarios para crear el formulario.
import javax.swing.*;

// ============================================================
// CLASE FORMULARIO PRODUCTO
// ============================================================

// FormularioProducto administra los campos visuales
// utilizados para cargar o editar un producto.
public class FormularioProducto {

    // Campo donde el usuario escribe el nombre.
    private JTextField txtNombre;

    // Campo donde el usuario escribe el precio.
    private JTextField txtPrecio;

    // Campo donde el usuario escribe el stock.
    private JTextField txtStock;

    // Lista desplegable para seleccionar la categoría.
    private JComboBox<String> cmbCategoria;

    // Creamos un formulario vacío para cargar un producto nuevo.
    public FormularioProducto() {
        crearCampos();
    }

    // Creamos un formulario con los datos de un producto existente.
    public FormularioProducto(Producto producto) {
        crearCampos();
        cargarProducto(producto);
    }

    // crearCampos crea los componentes que forman el formulario.
    private void crearCampos() {

        // Creamos el campo Nombre.
        txtNombre = new JTextField();

        // Creamos el campo Precio.
        txtPrecio = new JTextField();

        // Creamos el campo Stock.
        txtStock = new JTextField();

        // Creamos la lista de categorías.
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("Almacén");
        cmbCategoria.addItem("Bebidas");
        cmbCategoria.addItem("Limpieza");
        cmbCategoria.addItem("Verduleria");
        cmbCategoria.addItem("Tecnologia");
        cmbCategoria.addItem("Otros");
    }

    // agregarCampos incorpora las etiquetas y campos al panel recibido.
    public void agregarCampos(JPanel panelDestino) {

        // Agregamos la etiqueta y el campo Nombre.
        panelDestino.add(new JLabel("Nombre:"));
        panelDestino.add(txtNombre);

        // Agregamos la etiqueta y el campo Precio.
        panelDestino.add(new JLabel("Precio:"));
        panelDestino.add(txtPrecio);

        // Agregamos la etiqueta y el campo Stock.
        panelDestino.add(new JLabel("Stock:"));
        panelDestino.add(txtStock);

        // Agregamos la etiqueta y la lista de Categoría.
        panelDestino.add(new JLabel("Categoría:"));
        panelDestino.add(cmbCategoria);
    }

    // cargarProducto completa el formulario con los datos recibidos.
    public void cargarProducto(Producto producto) {
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getStock()));
        cmbCategoria.setSelectedItem(producto.getCategoria());
    }

    // getNombre devuelve el nombre sin espacios innecesarios.
    public String getNombre() {
        return txtNombre.getText().trim();
    }

    // getPrecioTexto devuelve el precio sin espacios innecesarios.
    public String getPrecioTexto() {
        return txtPrecio.getText().trim();
    }

    // getStockTexto devuelve el stock sin espacios innecesarios.
    public String getStockTexto() {
        return txtStock.getText().trim();
    }

    // getCategoria devuelve la categoría seleccionada.
    public String getCategoria() {
        return cmbCategoria.getSelectedItem().toString();
    }

    // limpiar borra los campos y selecciona la primera categoría.
    public void limpiar() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        cmbCategoria.setSelectedIndex(0);
        enfocarNombre();
    }

    // enfocarNombre coloca el cursor en el campo Nombre.
    public void enfocarNombre() {
        txtNombre.requestFocus();
    }

    // enfocarPrecio coloca el cursor en el campo Precio.
    public void enfocarPrecio() {
        txtPrecio.requestFocus();
    }

    // enfocarStock coloca el cursor en el campo Stock.
    public void enfocarStock() {
        txtStock.requestFocus();
    }
}

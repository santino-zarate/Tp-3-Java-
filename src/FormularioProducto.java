import javax.swing.*;
// CLASE FORMULARIO PRODUCTO
// administra los campos visuales utilizados para cargar o editar un producto.
public class FormularioProducto {
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;
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

    private void crearCampos() {
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("Almacén");
        cmbCategoria.addItem("Bebidas");
        cmbCategoria.addItem("Limpieza");
        cmbCategoria.addItem("Verduleria");
        cmbCategoria.addItem("Tecnologia");
        cmbCategoria.addItem("Otros");
    }
    // incorpora las etiquetas y campos al panel recibido.
    public void agregarCampos(JPanel panelDestino) {
        panelDestino.add(new JLabel("Nombre:"));
        panelDestino.add(txtNombre);
        panelDestino.add(new JLabel("Precio:"));
        panelDestino.add(txtPrecio);
        panelDestino.add(new JLabel("Stock:"));
        panelDestino.add(txtStock);
        panelDestino.add(new JLabel("Categoría:"));
        panelDestino.add(cmbCategoria);
    }
    // formulario con los datos recibidos.
    public void cargarProducto(Producto producto) {
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getStock()));
        cmbCategoria.setSelectedItem(producto.getCategoria());
    }
    public String getNombre() {
        return txtNombre.getText().trim();
    }
    public String getPrecioTexto() {
        return txtPrecio.getText().trim();
    }
    public String getStockTexto() {
        return txtStock.getText().trim();
    }
    public String getCategoria() {
        return cmbCategoria.getSelectedItem().toString();
    }

    // limpiar campos
    public void limpiar() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        cmbCategoria.setSelectedIndex(0);
        enfocarNombre();
    }

    // enfocar campos
    public void enfocarNombre() {
        txtNombre.requestFocus();
    }
    public void enfocarPrecio() {
        txtPrecio.requestFocus();
    }
    public void enfocarStock() {
        txtStock.requestFocus();
    }
}

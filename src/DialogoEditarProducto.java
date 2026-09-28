// Importamos los componentes necesarios para crear el diálogo de edición.
import javax.swing.*;

// Importamos clases para organizar los componentes gráficos.
import java.awt.*;

// ============================================================
// CLASE DIÁLOGO EDITAR PRODUCTO
// ============================================================

// DialogoEditarProducto administra la ventana modal
// que permite editar los datos de un producto.
public class DialogoEditarProducto extends JDialog {

    // Campo donde se edita el nombre del producto.
    private JTextField txtNombre;

    // Campo donde se edita el precio del producto.
    private JTextField txtPrecio;

    // Campo donde se edita el stock del producto.
    private JTextField txtStock;

    // Lista desplegable para editar la categoría.
    private JComboBox<String> cmbCategoria;

    // Guardamos el producto editado cuando el usuario confirma los cambios.
    private Producto productoEditado;

    // Creamos el diálogo usando los datos del producto seleccionado.
    public DialogoEditarProducto(JFrame ventanaPadre, Producto producto) {
        super(ventanaPadre, "Editar producto", true);

        // Definimos el tamaño del diálogo.
        setSize(400, 300);

        // Centramos el diálogo con respecto a la ventana principal.
        setLocationRelativeTo(ventanaPadre);

        // Construimos la interfaz del diálogo.
        crearInterfaz(producto);
    }

    // crearInterfaz construye los campos y el botón para guardar cambios.
    private void crearInterfaz(Producto producto) {

        // Creamos el panel que organiza los campos en filas y columnas.
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Agregamos un margen interno al panel.
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Creamos el campo Nombre con el valor actual.
        txtNombre = new JTextField(producto.getNombre());

        // Creamos el campo Precio con el valor actual.
        txtPrecio = new JTextField(String.valueOf(producto.getPrecio()));

        // Creamos el campo Stock con el valor actual.
        txtStock = new JTextField(String.valueOf(producto.getStock()));

        // Creamos la lista de categorías.
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("Almacén");
        cmbCategoria.addItem("Bebidas");
        cmbCategoria.addItem("Limpieza");
        cmbCategoria.addItem("Verduleria");
        cmbCategoria.addItem("Tecnologia");
        cmbCategoria.addItem("Otros");
        cmbCategoria.setSelectedItem(producto.getCategoria());

        // Agregamos la etiqueta y el campo Nombre.
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        // Agregamos la etiqueta y el campo Precio.
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);

        // Agregamos la etiqueta y el campo Stock.
        panel.add(new JLabel("Stock:"));
        panel.add(txtStock);

        // Agregamos la etiqueta y la lista de Categoría.
        panel.add(new JLabel("Categoría:"));
        panel.add(cmbCategoria);

        // Creamos el botón que guarda los cambios realizados.
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        // Ubicamos el formulario en el centro del diálogo.
        add(panel, BorderLayout.CENTER);

        // Ubicamos el botón para guardar en la parte inferior.
        add(btnGuardar, BorderLayout.SOUTH);
    }

    // guardarCambios valida los campos y crea el producto editado.
    private void guardarCambios() {

        // Obtenemos los textos escritos por el usuario.
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();
        String categoria = cmbCategoria.getSelectedItem().toString();

        // Validamos y convertimos los datos escritos en el diálogo.
        ValidadorProducto.ResultadoValidacion resultado =
                ValidadorProducto.validarParaEdicion(
                        nombre,
                        precioTexto,
                        stockTexto
                );

        // Si hay un error, informamos al usuario.
        if (!resultado.esValido()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // Enfocamos Precio cuando ese campo es inválido.
            if ("precio".equals(resultado.getCampoAFocalizar())) {
                txtPrecio.requestFocus();
            }

            // Enfocamos Stock cuando ese campo es inválido.
            if ("stock".equals(resultado.getCampoAFocalizar())) {
                txtStock.requestFocus();
            }

            // Interrumpimos el guardado si los datos son inválidos.
            return;
        }

        // Creamos el producto con los datos editados.
        productoEditado = new Producto(
                nombre,
                resultado.getPrecio(),
                resultado.getStock(),
                categoria
        );

        // Cerramos el diálogo después de guardar los cambios.
        dispose();
    }

    // getProductoEditado devuelve el producto guardado o null si se canceló.
    public Producto getProductoEditado() {
        return productoEditado;
    }
}

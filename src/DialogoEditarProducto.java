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

    // FormularioProducto administra los campos del diálogo.
    private FormularioProducto formulario;

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

        // Creamos el formulario con los datos del producto seleccionado.
        formulario = new FormularioProducto(producto);

        // Creamos el panel que organiza los campos del formulario.
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Agregamos los campos del formulario al panel.
        formulario.agregarCampos(panelFormulario);

        // Creamos el botón que guarda los cambios realizados.
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        // Ubicamos el formulario en el centro del diálogo.
        add(panelFormulario, BorderLayout.CENTER);

        // Ubicamos el botón para guardar en la parte inferior.
        add(btnGuardar, BorderLayout.SOUTH);
    }

    // guardarCambios valida los campos y crea el producto editado.
    private void guardarCambios() {

        // Obtenemos los textos escritos por el usuario.
        String nombre = formulario.getNombre();
        String precioTexto = formulario.getPrecioTexto();
        String stockTexto = formulario.getStockTexto();
        String categoria = formulario.getCategoria();

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
                formulario.enfocarPrecio();
            }

            // Enfocamos Stock cuando ese campo es inválido.
            if ("stock".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarStock();
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

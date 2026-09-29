import javax.swing.*;
import java.awt.*;

// CLASE DIÁLOGO EDITAR PRODUCTO
// administra la ventana modal que permite editar los datos de un producto.
public class DialogoEditarProducto extends JDialog {
    // FormularioProducto administra los campos del diálogo.
    private FormularioProducto formulario;
    // Guardamos el producto editado cuando el usuario confirma los cambios.
    private Producto productoEditado;

    // Creamos el diálogo usando los datos del producto seleccionado.
    public DialogoEditarProducto(JFrame ventanaPadre, Producto producto) {
        super(ventanaPadre, "Editar producto", true);
        setSize(400, 300);

        setLocationRelativeTo(ventanaPadre);
        crearInterfaz(producto);
    }
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
        //boton guardar cambios
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        // Ubicamos el formulario en el centro del diálogo.
        add(panelFormulario, BorderLayout.CENTER);
        // Ubicamos el botón para guardar en la parte inferior.
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
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
        if (!resultado.esValido()) {
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        //enfocamos el campo que tiene el error para que el usuario lo corrija.
            if ("precio".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarPrecio();
            }
            if ("stock".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarStock();
            }
            return;
        }

        // Creamos el producto con los datos editados.
        productoEditado = new Producto(
                nombre,
                resultado.getPrecio(),
                resultado.getStock(),
                categoria
        );
        dispose();
    }
    public Producto getProductoEditado() {
        return productoEditado;
    }
}

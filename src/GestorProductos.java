import javax.swing.*;
import java.awt.*;

// CLASE PRINCIPAL
// GestorProductos hereda de JFrame. Por lo tanto, esta clase representa una ventana.
public class GestorProductos extends JFrame {
    // FormularioProducto administra los campos para agregar productos.
    private FormularioProducto formulario;
    // BuscadorProductos administra la búsqueda sobre la tabla.
    private BuscadorProductos buscador;
    // SelectorFilasProductos administra los checks visibles de la tabla.
    private SelectorFilasProductos selectorFilas;

    // COMPONENTES DE LA TABLA
    // JTable muestra los productos al usuario.
    private JTable tabla;
    // ModeloTablaProductos administra las filas y columnas.
    private ModeloTablaProductos modelo;
    // JButton elimina los productos que tienen su checkbox tildado.
    private JButton btnEliminar;
    private JButton btnEstadisticas;

    // CONSTRUCTOR
    public GestorProductos() {
        // Título que aparecerá en la ventana.
        setTitle("Gestor de Productos");
        setSize(800, 500);
        // Al presionar X se cierra el programa.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Centra la ventana en la pantalla.
        setLocationRelativeTo(null);
        // Llamamos al método que construye la interfaz.
        crearInterfaz();
    }

    // CREAR INTERFAZ
    private void crearInterfaz() {
        // Creamos un panel con filas 6 y 2 columnas.
        JPanel panelFormulario =
                new JPanel(new GridLayout(6, 2, 10, 10));
        // Agregamos un margen interno al formulario.
        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // Creamos el formulario reutilizable para agregar productos.
        formulario = new FormularioProducto();
        // Agregamos los campos en la grilla del formulario principal.
        formulario.agregarCampos(panelFormulario);

        // BOTONES
        JButton btnAgregar = new JButton("Agregar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEditar = new JButton("Editar");
        panelFormulario.add(btnLimpiar);
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnEditar);
       
        // CREAR TABLA
        modelo = new ModeloTablaProductos();
        tabla = new JTable(modelo);
        buscador = new BuscadorProductos(tabla, modelo);
        selectorFilas = new SelectorFilasProductos(tabla, modelo);
        JPanel panelBusqueda = buscador.getPanelBusqueda();

        // JScrollPane permite desplazarnos si hay muchas filas.
        JScrollPane scrollTabla =
                new JScrollPane(tabla);

        // Ubicamos el buscador arriba de la tabla.
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // BOTÓN ELIMINAR
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEstadisticas = new JButton("Ver estadísticas");

        // EVENTO AGREGAR
        btnAgregar.addActionListener(e -> {
            agregarProducto();
        });
        // Ejecutamos la edición del producto seleccionado.
        btnEditar.addActionListener(e -> {
            editarProducto();
        });

        // EVENTO LIMPIAR
        btnLimpiar.addActionListener(e -> {
            limpiarFormulario();
        });

        // EVENTO ELIMINAR
        btnEliminar.addActionListener(e -> {
            eliminarProductosSeleccionados();
        });
        // EVENTO ESTADÍSTICAS
        btnEstadisticas.addActionListener(e -> {
        mostrarEstadisticas();
        });     
        // Programamos la actualización después de sincronizar modelo y sorter.
        modelo.addTableModelListener(
                e -> programarActualizacionBotonEliminar()
        );
        // Programamos la actualización cuando el filtro modifica las filas visibles.
        tabla.getRowSorter().addRowSorterListener(
                e -> programarActualizacionBotonEliminar()
        );

        // PANEL INFERIOR
        JPanel panelInferior =
                new JPanel(new BorderLayout());
        // Botón a la izquierda.
        panelInferior.add(
                btnEliminar,
                BorderLayout.WEST
        );
            panelInferior.add(
            btnEstadisticas,
            BorderLayout.EAST
            );

        // CONFIGURAR VENTANA
        setLayout(new BorderLayout());
        // Formulario arriba.
        add(
                panelFormulario,
                BorderLayout.NORTH
        );
        add(
                panelTabla,
                BorderLayout.CENTER
        );
        add(
                panelInferior,
                BorderLayout.SOUTH
        );
    }

    // AGREGAR PRODUCTO
    private void agregarProducto() {
        String nombre = formulario.getNombre();
        String precioTexto = formulario.getPrecioTexto();
        String stockTexto = formulario.getStockTexto();
        String categoria = formulario.getCategoria();

        // VALIDAR DATOS
        ValidadorProducto.ResultadoValidacion resultado =
                ValidadorProducto.validar(
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
            if ("nombre".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarNombre();
            }
            if ("precio".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarPrecio();
            }
            if ("stock".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarStock();
            }
            return;
        }

        // Obtenemos los valores ya convertidos por el validador.
        double precio = resultado.getPrecio();
        int stock = resultado.getStock();

        // CREAR OBJETO
        Producto producto =
                new Producto(
                        nombre,
                        precio,
                        stock,
                        categoria
                );

        // AGREGAR A LA TABLA
        modelo.agregarProducto(producto);
        // Limpiamos el formulario.
        limpiarFormulario();
        JOptionPane.showMessageDialog(
                this,
                "Producto agregado correctamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // MÉTODO EDITAR
    private void editarProducto() {
        // Obtenemos la fila visual seleccionada por el usuario.
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un producto.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Convertimos la fila visible a la fila real del modelo.
        int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
        // Obtenemos el producto correspondiente a la fila seleccionada.
        Producto productoSeleccionado = modelo.obtenerProducto(filaModelo);
        // Mostramos el diálogo para editar el producto seleccionado.
        DialogoEditarProducto dialogo =
                new DialogoEditarProducto(this, productoSeleccionado);
        dialogo.setVisible(true);
        // Obtenemos el producto editado al cerrar el diálogo.
        Producto productoEditado = dialogo.getProductoEditado();
        // Actualizamos la fila solo si el usuario guardó los cambios.
        if (productoEditado != null) {
            modelo.actualizarProducto(filaModelo, productoEditado);
            JOptionPane.showMessageDialog(
                    this,
                    "Producto modificado correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // LIMPIAR FORMULARIO
    private void limpiarFormulario() {
        formulario.limpiar();
    }

    // ELIMINAR PRODUCTOS
    private void eliminarProductosSeleccionados() {
        // Finalizamos la edición del checkbox para guardar su estado en el modelo.
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }
        int cantidadSeleccionados =
                selectorFilas.cantidadSeleccionadosVisibles();
        // Interrumpimos si no hay checks visibles tildados.
        if (cantidadSeleccionados == 0) {
            actualizarEstadoBotonEliminar();
            return;
        }
        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Eliminar " + cantidadSeleccionados + " productos?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
        // Eliminamos los productos solo si el usuario confirma.
        if (respuesta == JOptionPane.YES_OPTION) {
            selectorFilas.eliminarSeleccionadosVisibles();
            // Programamos la actualización del botón después de eliminar.
            programarActualizacionBotonEliminar();
        }
    }
    private void programarActualizacionBotonEliminar() {
        SwingUtilities.invokeLater(this::actualizarEstadoBotonEliminar);
    }
    private void actualizarEstadoBotonEliminar() {
        btnEliminar.setEnabled(selectorFilas.haySeleccionadosVisibles());
    }

    // MOSTRAR ESTADÍSTICAS
        private void mostrarEstadisticas() {
        DialogoEstadisticas dialogo =
                new DialogoEstadisticas(this, modelo);
        dialogo.setVisible(true);
        }

    // MÉTODO MAIN
    public static void main(String[] args) {
        // invokeLater ejecuta la creación de la GUI
        // en el hilo de eventos de Swing.
        SwingUtilities.invokeLater(() -> {
            // Creamos la ventana.
            GestorProductos ventana =
                    new GestorProductos();
            // Mostramos la ventana.
            ventana.setVisible(true);
        });
    }
}

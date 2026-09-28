// Importamos los componentes principales de Swing.
import javax.swing.*;

// Importamos clases para organizar los componentes gráficos.
import java.awt.*;


// ============================================================
// CLASE PRINCIPAL
// ============================================================

// GestorProductos hereda de JFrame.
// Por lo tanto, esta clase representa una ventana.
public class GestorProductos extends JFrame {

    // ========================================================
    // COMPONENTES DEL FORMULARIO
    // ========================================================

    // FormularioProducto administra los campos para agregar productos.
    private FormularioProducto formulario;

    // BuscadorProductos administra la búsqueda sobre la tabla.
    private BuscadorProductos buscador;

    // SelectorFilasProductos administra los checks visibles de la tabla.
    private SelectorFilasProductos selectorFilas;


    // ========================================================
    // COMPONENTES DE LA TABLA
    // ========================================================

    // JTable muestra los productos al usuario.
    private JTable tabla;

    // ModeloTablaProductos administra las filas y columnas.
    private ModeloTablaProductos modelo;

    // JButton elimina los productos que tienen su checkbox tildado.
    private JButton btnEliminar;

    // ========================================================
    // COMPONENTE PARA MOSTRAR EL TOTAL
    // ========================================================

    // JLabel utilizado para mostrar el valor total del stock.
    private JLabel lblTotal;


    // ========================================================
    // CONSTRUCTOR
    // ========================================================

    public GestorProductos() {

        // Título que aparecerá en la ventana.
        setTitle("Gestor de Productos");

        // Tamaño de la ventana: ancho x alto.
        setSize(800, 500);

        // Al presionar X se cierra el programa.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centra la ventana en la pantalla.
        setLocationRelativeTo(null);

        // Llamamos al método que construye la interfaz.
        crearInterfaz();
    }


    // ========================================================
    // CREAR INTERFAZ
    // ========================================================

    private void crearInterfaz() {

        // Creamos un panel con 5 filas y 2 columnas.
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


        // ----------------------------------------------------
        // BOTONES
        // ----------------------------------------------------

        JButton btnAgregar = new JButton("Agregar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEditar = new JButton("Editar");

        panelFormulario.add(btnLimpiar);
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnEditar);
       
        // ====================================================
        // CREAR TABLA
        // ====================================================

        // Creamos el modelo que administra los datos de la tabla.
        modelo = new ModeloTablaProductos();

        // Creamos la tabla utilizando nuestro modelo.
        tabla = new JTable(modelo);

        // Creamos el buscador asociado a la tabla de productos.
        buscador = new BuscadorProductos(tabla, modelo);

        // Creamos el selector para administrar las filas visibles tildadas.
        selectorFilas = new SelectorFilasProductos(tabla, modelo);

        // Obtenemos el panel visual del buscador.
        JPanel panelBusqueda = buscador.getPanelBusqueda();

        // JScrollPane permite desplazarnos si hay muchas filas.
        JScrollPane scrollTabla =
                new JScrollPane(tabla);

        // Ubicamos el buscador arriba de la tabla.
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);


        // ====================================================
        // BOTÓN ELIMINAR
        // ====================================================

        btnEliminar = new JButton("Eliminar");

        // El botón comienza deshabilitado porque no hay checks tildados.
        btnEliminar.setEnabled(false);


        // ====================================================
        // ETIQUETA TOTAL
        // ====================================================

        lblTotal =
                new JLabel("Valor total del stock: $0.00");


        // ====================================================
        // EVENTO AGREGAR
        // ====================================================

        // addActionListener detecta el clic del botón.
        btnAgregar.addActionListener(e -> {

            // Ejecutamos nuestro método.
            agregarProducto();
        });
        // Ejecutamos la edición del producto seleccionado.
        btnEditar.addActionListener(e -> {
            editarProducto();
        });

        // ====================================================
        // EVENTO LIMPIAR
        // ====================================================

        btnLimpiar.addActionListener(e -> {

            // Limpiamos los campos.
            limpiarFormulario();
        });


        // ====================================================
        // EVENTO ELIMINAR
        // ====================================================

        btnEliminar.addActionListener(e -> {

            // Eliminamos los productos que tengan su checkbox tildado.
            eliminarProductosSeleccionados();
        });

        // Programamos la actualización después de sincronizar modelo y sorter.
        modelo.addTableModelListener(
                e -> programarActualizacionBotonEliminar()
        );

        // Programamos la actualización cuando el filtro modifica las filas visibles.
        tabla.getRowSorter().addRowSorterListener(
                e -> programarActualizacionBotonEliminar()
        );


        // ====================================================
        // PANEL INFERIOR
        // ====================================================

        JPanel panelInferior =
                new JPanel(new BorderLayout());

        // Botón a la izquierda.
        panelInferior.add(
                btnEliminar,
                BorderLayout.WEST
        );

        // Total a la derecha.
        panelInferior.add(
                lblTotal,
                BorderLayout.EAST
        );


        // ====================================================
        // CONFIGURAR VENTANA
        // ====================================================

        // Utilizamos BorderLayout para la ventana.
        setLayout(new BorderLayout());

        // Formulario arriba.
        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        // Tabla en el centro.
        add(
                panelTabla,
                BorderLayout.CENTER
        );

        // Panel inferior abajo.
        add(
                panelInferior,
                BorderLayout.SOUTH
        );
    }


    // ========================================================
    // AGREGAR PRODUCTO
    // ========================================================

    private void agregarProducto() {

        // Obtenemos los datos escritos en el formulario.
        String nombre = formulario.getNombre();
        String precioTexto = formulario.getPrecioTexto();
        String stockTexto = formulario.getStockTexto();
        String categoria = formulario.getCategoria();


        // ====================================================
        // VALIDAR DATOS
        // ====================================================

        // Validamos y convertimos los datos escritos en el formulario.
        ValidadorProducto.ResultadoValidacion resultado =
                ValidadorProducto.validar(
                        nombre,
                        precioTexto,
                        stockTexto
                );

        // Si hay un error, informamos al usuario.
        if (!resultado.esValido()) {

            // Mostramos el mismo mensaje de error que antes.
            JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // Enfocamos Nombre cuando ese campo es inválido.
            if ("nombre".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarNombre();
            }

            // Enfocamos Precio cuando no puede convertirse a número.
            if ("precio".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarPrecio();
            }

            // Enfocamos Stock cuando no puede convertirse a entero.
            if ("stock".equals(resultado.getCampoAFocalizar())) {
                formulario.enfocarStock();
            }

            // Interrumpimos el agregado si los datos son inválidos.
            return;
        }

        // Obtenemos los valores ya convertidos por el validador.
        double precio = resultado.getPrecio();
        int stock = resultado.getStock();


        // ====================================================
        // CREAR OBJETO
        // ====================================================

        // Creamos un objeto Producto utilizando
        // los datos ingresados por el usuario.
        Producto producto =
                new Producto(
                        nombre,
                        precio,
                        stock,
                        categoria
                );


        // ====================================================
        // AGREGAR A LA TABLA
        // ====================================================

        // Agregamos el producto mediante el modelo de la tabla.
        modelo.agregarProducto(producto);


        // Actualizamos el total.
        actualizarTotal();

        // Limpiamos el formulario.
        limpiarFormulario();


        // Informamos que la operación fue exitosa.
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

        // Informamos si no hay ningún producto seleccionado.
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
            actualizarTotal();

            // Informamos que la modificación fue exitosa.
            JOptionPane.showMessageDialog(
                    this,
                    "Producto modificado correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ========================================================
    // LIMPIAR FORMULARIO
    // ========================================================

    private void limpiarFormulario() {

        // Limpiamos los campos mediante el formulario reutilizable.
        formulario.limpiar();
    }


    // ========================================================
    // ELIMINAR PRODUCTOS
    // ========================================================

    // eliminarProductosSeleccionados confirma y elimina los checks visibles.
    private void eliminarProductosSeleccionados() {

        // Finalizamos la edición del checkbox para guardar su estado en el modelo.
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        // Obtenemos la cantidad de productos visibles tildados.
        int cantidadSeleccionados =
                selectorFilas.cantidadSeleccionadosVisibles();

        // Interrumpimos si no hay checks visibles tildados.
        if (cantidadSeleccionados == 0) {
            actualizarEstadoBotonEliminar();
            return;
        }

        // Pedimos confirmación indicando la cantidad de productos a eliminar.
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

            // Eliminamos las filas visibles tildadas mediante el selector.
            selectorFilas.eliminarSeleccionadosVisibles();

            // Recalculamos el total.
            actualizarTotal();

            // Programamos la actualización del botón después de eliminar.
            programarActualizacionBotonEliminar();
        }
    }

    // programarActualizacionBotonEliminar espera que la tabla actualice su sorter.
    private void programarActualizacionBotonEliminar() {
        SwingUtilities.invokeLater(this::actualizarEstadoBotonEliminar);
    }

    // actualizarEstadoBotonEliminar habilita el botón según los checks visibles.
    private void actualizarEstadoBotonEliminar() {
        btnEliminar.setEnabled(selectorFilas.haySeleccionadosVisibles());
    }


    // ========================================================
    // ACTUALIZAR TOTAL
    // ========================================================

    private void actualizarTotal() {

        // Calculamos el total mediante el modelo de la tabla.
        double total = modelo.calcularTotal();


        // Actualizamos el JLabel.
        lblTotal.setText(
                String.format(
                        "Valor total del stock: $%.2f",
                        total
                )
        );
    }


    // ========================================================
    // MÉTODO MAIN
    // ========================================================

    // Punto de entrada de nuestro programa.
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

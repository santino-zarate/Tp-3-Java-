// Importamos los componentes principales de Swing.
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Importamos DefaultTableModel para administrar los datos de la JTable.
import javax.swing.table.DefaultTableModel;

// Importamos clases para organizar los componentes gráficos.
import java.awt.*;

// TableRowSorter para poder filtrar
// las filas de la tabla según la búsqueda.
import javax.swing.table.TableRowSorter;

// Pattern para que el buscador no falle
// si se escriben caracteres especiales.
import java.util.regex.Pattern;


// ============================================================
// CLASE PRINCIPAL
// ============================================================

// GestorProductos hereda de JFrame.
// Por lo tanto, esta clase representa una ventana.
public class GestorProductos extends JFrame {

    // ========================================================
    // COMPONENTES DEL FORMULARIO
    // ========================================================

    // Campo donde el usuario escribe el nombre.
    private JTextField txtNombre;

    // Campo donde el usuario escribe el precio.
    private JTextField txtPrecio;

    // Campo donde el usuario escribe el stock.
    private JTextField txtStock;

    // Campo donde el usuario escribe el texto a buscar.
    private JTextField txtBuscar;

    // Lista desplegable para seleccionar la categoría.
    private JComboBox<String> cmbCategoria;


    // ========================================================
    // COMPONENTES DE LA TABLA
    // ========================================================

    // JTable muestra los productos al usuario.
    private JTable tabla;

    // DefaultTableModel administra las filas y columnas.
    private DefaultTableModel modelo;

    // TableRowSorter administra el filtrado de las filas.
    private TableRowSorter<DefaultTableModel> sorter;
    private int filaEditando = -1;

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


        // ----------------------------------------------------
        // CAMPO NOMBRE
        // ----------------------------------------------------

        // Agregamos la etiqueta.
        panelFormulario.add(new JLabel("Nombre:"));

        // Creamos el campo de texto.
        txtNombre = new JTextField();

        // Agregamos el campo al formulario.
        panelFormulario.add(txtNombre);


        // ----------------------------------------------------
        // CAMPO PRECIO
        // ----------------------------------------------------

        panelFormulario.add(new JLabel("Precio:"));

        txtPrecio = new JTextField();

        panelFormulario.add(txtPrecio);


        // ----------------------------------------------------
        // CAMPO STOCK
        // ----------------------------------------------------

        panelFormulario.add(new JLabel("Stock:"));

        txtStock = new JTextField();

        panelFormulario.add(txtStock);


        // ----------------------------------------------------
        // CATEGORÍA
        // ----------------------------------------------------

        panelFormulario.add(new JLabel("Categoría:"));

        // Creamos el ComboBox.
        cmbCategoria = new JComboBox<>();

        // Agregamos las opciones.
        cmbCategoria.addItem("Almacén");
        cmbCategoria.addItem("Bebidas");
        cmbCategoria.addItem("Limpieza");
        cmbCategoria.addItem("Verduleria");
        cmbCategoria.addItem("Tecnologia");
        cmbCategoria.addItem("Otros");

        // Agregamos el ComboBox.
        panelFormulario.add(cmbCategoria);


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

        // Nombres de las columnas.
        String[] columnas = {
                "Nombre",
                "Precio",
                "Stock",
                "Categoría",
                "Valor Stock"
        };

        // Creamos el modelo sin filas inicialmente.
        modelo = new DefaultTableModel(columnas, 0);

        // Creamos la tabla utilizando nuestro modelo.
        tabla = new JTable(modelo);

        // Asociamos el sorter a la tabla para poder mostrar solo las filas buscadas.
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Campo que permite buscar productos por cualquiera de sus columnas.
        txtBuscar = new JTextField();

        // Cada cambio en el texto aplica nuevamente el filtro.
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

        // Panel que agrupa la etiqueta y el campo del buscador.
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panelBusqueda.add(new JLabel("Buscar producto:"), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

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

        JButton btnEliminar = new JButton("Eliminar");


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
        //editar
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

            // Eliminamos el producto seleccionado.
            eliminarProducto();
        });


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

        // Obtenemos el nombre escrito.
        // trim() elimina espacios al principio y al final.
        String nombre =
                txtNombre.getText().trim();

        // Obtenemos el precio como texto.
        String precioTexto =
                txtPrecio.getText().trim();

        // Obtenemos el stock como texto.
        String stockTexto =
                txtStock.getText().trim();

        // Obtenemos la categoría seleccionada.
        String categoria =
                cmbCategoria.getSelectedItem().toString();


        // ====================================================
        // VALIDAR NOMBRE
        // ====================================================

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del producto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // Volvemos el cursor al campo Nombre.
            txtNombre.requestFocus();

            // Interrumpimos el método.
            return;
        }


        // Variables donde almacenaremos
        // los valores convertidos.
        double precio;
        int stock;


        // ====================================================
        // CONVERTIR PRECIO
        // ====================================================

        try {

            // Convertimos String a double.
            precio =
                    Double.parseDouble(precioTexto);

        } catch (NumberFormatException e) {

            // Si la conversión falla, mostramos un error.
            JOptionPane.showMessageDialog(
                    this,
                    "El precio debe ser un número válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            txtPrecio.requestFocus();

            return;
        }


        // ====================================================
        // CONVERTIR STOCK
        // ====================================================

        try {

            // Convertimos String a int.
            stock =
                    Integer.parseInt(stockTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            txtStock.requestFocus();

            return;
        }


        // ====================================================
        // VALIDAR PRECIO
        // ====================================================

        if (precio <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El precio debe ser mayor que cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ====================================================
        // VALIDAR STOCK
        // ====================================================

        if (stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "El stock no puede ser negativo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


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

        // addRow agrega una nueva fila.
        modelo.addRow(new Object[] {

                // Columna 1.
                producto.getNombre(),

                // Columna 2.
                producto.getPrecio(),

                // Columna 3.
                producto.getStock(),

                // Columna 4.
                producto.getCategoria(),

                // Columna 5.
                producto.getValorStock()
        });


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

    //MÉTODO EDITAR
    private void editarProducto() {
     int filaSeleccionada =
            tabla.getSelectedRow();

     if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Debe seleccionar un producto.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
        return;
     }

     // Convertimos el índice visual al índice real del modelo si hay un filtro activo.
     filaEditando = tabla.convertRowIndexToModel(filaSeleccionada);

     String nombre =
        modelo.getValueAt(filaEditando, 0).toString();

     String precio =
        modelo.getValueAt(filaEditando, 1).toString();

     String stock =
        modelo.getValueAt(filaEditando, 2).toString();

     String categoria =
        modelo.getValueAt(filaEditando, 3).toString();

      JDialog dialogo =
        new JDialog(this, "Editar producto", true);

        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);  

        JPanel panel =
        new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(
        BorderFactory.createEmptyBorder(  15, 15, 15, 15));
        JTextField txtEditarNombre =
        new JTextField(nombre);

       JTextField txtEditarPrecio =
        new JTextField(precio);

      JTextField txtEditarStock =
        new JTextField(stock);

      JComboBox<String> cmbEditarCategoria =
        new JComboBox<>();
        cmbEditarCategoria.addItem("Almacén");
        cmbEditarCategoria.addItem("Bebidas");
        cmbEditarCategoria.addItem("Limpieza");
        cmbEditarCategoria.addItem("Verduleria");
        cmbEditarCategoria.addItem("Tecnologia");
        cmbEditarCategoria.addItem("Otros");
        cmbEditarCategoria.setSelectedItem(categoria);
        panel.add(new JLabel("Nombre:"));

        panel.add(txtEditarNombre);

        panel.add(new JLabel("Precio:"));
        panel.add(txtEditarPrecio);

        panel.add(new JLabel("Stock:"));
        panel.add(txtEditarStock);

        panel.add(new JLabel("Categoría:"));
        panel.add(cmbEditarCategoria);

        JButton btnGuardar =
        new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> {
         guardarCambios(
            dialogo,
            txtEditarNombre,
            txtEditarPrecio,
            txtEditarStock,
            cmbEditarCategoria
           );
        });

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(btnGuardar, BorderLayout.SOUTH);
        dialogo.setVisible(true);
 }

        //METODO GUARDAR CAMBIOS
        private void guardarCambios(
        JDialog dialogo,
        JTextField txtEditarNombre,
        JTextField txtEditarPrecio,
        JTextField txtEditarStock,
        JComboBox<String> cmbEditarCategoria
) {
    String nombre =
            txtEditarNombre.getText().trim();

    String precioTexto =
            txtEditarPrecio.getText().trim();

    String stockTexto =
            txtEditarStock.getText().trim();

    String categoria =
            cmbEditarCategoria.getSelectedItem().toString();

     if (nombre.isEmpty() ||
                precioTexto.isEmpty() ||
                stockTexto.isEmpty()) {

        JOptionPane.showMessageDialog(
                dialogo,
                "Debe completar todos los campos.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        return;
        }
        double precio;
        int stock;
        try {
        precio = Double.parseDouble(precioTexto);
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
                dialogo,
                "El precio debe ser un número válido.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        txtEditarPrecio.requestFocus();
        return;
        }
        try {
        stock =
                Integer.parseInt(stockTexto);
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
                dialogo,
                "El stock debe ser un número entero.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        txtEditarStock.requestFocus();
        return;
        }       

        if (precio <= 0 || stock < 0) {
        JOptionPane.showMessageDialog(
                dialogo,
                "El precio debe ser mayor que cero y el stock no puede ser negativo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        txtEditarPrecio.requestFocus();
        return;
        }
        double valorStock =
        precio * stock;
        modelo.setValueAt(
                nombre,
                filaEditando,
                0
        );
        modelo.setValueAt(
                precio,
                filaEditando,
                1
        );
        modelo.setValueAt(
                stock,
                filaEditando,
                2
        );
        modelo.setValueAt(
                categoria,
                filaEditando,
                3
        );
        modelo.setValueAt(
                valorStock,
                filaEditando,
                4
        );
        actualizarTotal();
        dialogo.dispose();
        JOptionPane.showMessageDialog(
        this,
        "Producto modificado correctamente.",
        "Información",
        JOptionPane.INFORMATION_MESSAGE
);
    }

    // ========================================================
    // LIMPIAR FORMULARIO
    // ========================================================

    private void limpiarFormulario() {

        // Borramos el nombre.
        txtNombre.setText("");

        // Borramos el precio.
        txtPrecio.setText("");

        // Borramos el stock.
        txtStock.setText("");

        // Seleccionamos la primera categoría.
        cmbCategoria.setSelectedIndex(0);

        // Volvemos a colocar el cursor en Nombre.
        txtNombre.requestFocus();
    }


    // ========================================================
    // ELIMINAR PRODUCTO
    // ========================================================

    private void eliminarProducto() {

        // getSelectedRow() devuelve el índice
        // de la fila seleccionada.
        int filaSeleccionada =
                tabla.getSelectedRow();


        // Si devuelve -1 no hay selección.
        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un producto.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convertimos la fila visible al índice real del modelo.
        int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);

        // Obtenemos el nombre para identificar el producto en el mensaje.
        String nombreProducto = modelo.getValueAt(filaModelo, 0).toString();

        // Pedimos confirmación al usuario indicando qué producto se eliminará.
        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar el producto: "
                                + nombreProducto + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        // Comprobamos si respondió "Sí".
        if (respuesta == JOptionPane.YES_OPTION) {

            // Eliminamos la fila real del modelo.
            modelo.removeRow(filaModelo);

            // Recalculamos el total.
            actualizarTotal();
        }
    }


    // ========================================================
    // FILTRAR PRODUCTOS
    // ========================================================

    private void filtrarProductos() {

        // Obtenemos el texto y quitamos espacios innecesarios.
        String textoBusqueda = txtBuscar.getText().trim();

        // Si el campo está vacío, mostramos nuevamente todos los productos.
        if (textoBusqueda.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        // quote() evita que caracteres como +, * o ? se interpreten como regex.
        // (?i) permite buscar sin diferenciar mayúsculas de minúsculas.
        sorter.setRowFilter(RowFilter.regexFilter(
                "(?i)" + Pattern.quote(textoBusqueda)
        ));
    }


    // ========================================================
    // ACTUALIZAR TOTAL
    // ========================================================

    private void actualizarTotal() {

        // Comenzamos en cero.
        double total = 0;


        // Recorremos todas las filas.
        for (
                int i = 0;
                i < modelo.getRowCount();
                i++
        ) {

            // Obtenemos la columna 4,
            // que corresponde a Valor Stock.
            double valor =
                    Double.parseDouble(
                            modelo
                                    .getValueAt(i, 4)
                                    .toString()
                    );

            // Sumamos el valor.
            total += valor;
        }


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

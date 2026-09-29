import javax.swing.*;
import java.awt.*;

public class DialogoEstadisticas extends JDialog {

    private EstadisticasProductos estadisticas;
    private ModeloTablaProductos modelo;

    public DialogoEstadisticas(
            JFrame ventanaPadre,
            ModeloTablaProductos modelo
    ) {
        super(ventanaPadre, "Estadísticas", true);

        this.modelo = modelo;
        this.estadisticas = new EstadisticasProductos(modelo);

        crearInterfaz();
    }

    private void crearInterfaz() {

        setSize(500, 400);
        setLocationRelativeTo(getParent());

        JPanel panelPrincipal = new JPanel(
                new BorderLayout(10, 10)
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Título
        JLabel titulo = new JLabel(
                "Estadísticas de productos",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Arial", Font.BOLD, 22)
        );
        titulo.setBorder(
        BorderFactory.createEmptyBorder(
                0, 0, 10, 0
        )
        );
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Información general
        JPanel panelResumen = new JPanel(
                new GridLayout(3, 1, 5, 10)
        );
    
        JLabel lblCantidadProductos = new JLabel(
                "Cantidad de productos: "
                + estadisticas.cantidadProductos()
        );

        JLabel lblCantidadUnidades = new JLabel(
                "Cantidad de unidades: "
                + estadisticas.cantidadUnidades()
        );

        JLabel lblValorTotal = new JLabel(
                String.format(
                        "Valor total del inventario: $%.2f",
                        estadisticas.valorTotalInventario()
                )
        );

        panelResumen.add(lblCantidadProductos);
        panelResumen.add(lblCantidadUnidades);
        panelResumen.add(lblValorTotal);

        JPanel panelContenido =
                new JPanel(new BorderLayout(10, 10));

        panelContenido.add(
                panelResumen,
                BorderLayout.NORTH
        );

        // Valores por producto
        JTextArea areaProductos = new JTextArea();

        areaProductos.setEditable(false);

        areaProductos.append("Valor por producto:\n");
        areaProductos.append("------------------------------\n");

        for (int fila = 0; fila < modelo.getRowCount(); fila++) {

            Producto producto = modelo.obtenerProducto(fila);

            areaProductos.append(
                    producto.getNombre()
                    + " - $"
                    + String.format("%.2f", producto.getValorStock())
                    + "\n"
            );
        }

        JScrollPane scrollProductos =
                new JScrollPane(areaProductos);

        JTextArea areaCategorias =
        new JTextArea();
        areaCategorias.setEditable(false);
        areaCategorias.append(
                "Valor por categoría:\n"
        );
        areaCategorias.append(
                "------------------------------\n"
        );
        areaCategorias.append(
                estadisticas.valoresPorCategoria()
        );

        JScrollPane scrollCategorias =
                new JScrollPane(areaCategorias);

        JPanel panelListas =
        new JPanel(new GridLayout(2, 1, 5, 5));
        panelListas.add(scrollProductos);
        panelListas.add(scrollCategorias);
        panelContenido.add(
                panelListas,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
        panelContenido,
        BorderLayout.CENTER
        );

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");

        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        panelPrincipal.add(
        panelBoton,
        BorderLayout.SOUTH
        );
        add(panelPrincipal);
    }
}

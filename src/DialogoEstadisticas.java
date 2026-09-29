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
                new Font("Arial", Font.BOLD, 20)
        );

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Información general
        JPanel panelResumen = new JPanel(
                new GridLayout(3, 1, 5, 5)
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

        panelPrincipal.add(
                panelResumen,
                BorderLayout.CENTER
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

        panelPrincipal.add(
                scrollProductos,
                BorderLayout.SOUTH
        );

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");

        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);

        panelPrincipal.add(
                panelBoton,
                BorderLayout.PAGE_END
        );

        add(panelPrincipal);
    }
}

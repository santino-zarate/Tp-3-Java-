// CLASE PRODUCTO
// Esta clase representa un producto del comercio.
public class Producto {
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;
    //constructor
    public Producto(String nombre, double precio, int stock, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    public String getNombre() {
        return nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public int getStock() {
        return stock;
    }
    public String getCategoria() {
        return categoria;
    }
    public double getValorStock() {
        return precio * stock;
    }
}

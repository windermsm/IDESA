package inventory.objetos;

public class ObjetosFalla {
    
    private int id_falla;
    private String nombre_falla;
    private String categoria_producto;
    private int id_categoria;
    private String estado_categoria;

    public String getCategoria_producto() {
        return categoria_producto;
    }

    public void setCategoria_producto(String categoria_producto) {
        this.categoria_producto = categoria_producto;
    }

    public String getEstado_categoria() {
        return estado_categoria;
    }

    public void setEstado_categoria(String estado_categoria) {
        this.estado_categoria = estado_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_falla() {
        return id_falla;
    }

    public void setId_falla(int id_falla) {
        this.id_falla = id_falla;
    }

    public String getNombre_falla() {
        return nombre_falla;
    }

    public void setNombre_falla(String nombre_falla) {
        this.nombre_falla = nombre_falla;
    }
    
}

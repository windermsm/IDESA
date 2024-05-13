package inventory.objetos;

public class ObjetosMarca {
    
    private int id_marca_producto;
    private String marca_producto;
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_marca_producto() {
        return id_marca_producto;
    }

    public void setId_marca_producto(int id_marca_producto) {
        this.id_marca_producto = id_marca_producto;
    }

    public String getMarca_producto() {
        return marca_producto;
    }

    public void setMarca_producto(String marca_producto) {
        this.marca_producto = marca_producto;
    }
    
}

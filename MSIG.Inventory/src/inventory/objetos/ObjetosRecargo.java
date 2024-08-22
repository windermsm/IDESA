package inventory.objetos;

public class ObjetosRecargo {
    
    private int id_recargo;
    private String descripcion;
    private double valor;
    private String estado;

    public int getId_recargo() {
        return id_recargo;
    }

    public void setId_recargo(int id_recargo) {
        this.id_recargo = id_recargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}

package inventory.objetos;

public class ObjetosProforma {
    
    private int id_proforma;
    private int id_cliente;
    private String fecha_proforma;
    private String dir_envio_proforma;
    private float total_proforma;
    private int id_empleado;
    private String nombre_empleado;
    private String nombre_persona;
    private String fecha_emision_proforma;
    private String nombre_proforma;

    public String getDir_envio_proforma() {
        return dir_envio_proforma;
    }

    public void setDir_envio_proforma(String dir_envio_proforma) {
        this.dir_envio_proforma = dir_envio_proforma;
    }

    public String getFecha_emision_proforma() {
        return fecha_emision_proforma;
    }

    public void setFecha_emision_proforma(String fecha_emision_proforma) {
        this.fecha_emision_proforma = fecha_emision_proforma;
    }

    public String getFecha_proforma() {
        return fecha_proforma;
    }

    public void setFecha_proforma(String fecha_proforma) {
        this.fecha_proforma = fecha_proforma;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_proforma() {
        return id_proforma;
    }

    public void setId_proforma(int id_proforma) {
        this.id_proforma = id_proforma;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getNombre_persona() {
        return nombre_persona;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public float getTotal_proforma() {
        return total_proforma;
    }

    public void setTotal_proforma(float total_proforma) {
        this.total_proforma = total_proforma;
    }

    public String getNombre_proforma() {
        return nombre_proforma;
    }

    public void setNombre_proforma(String nombre_proforma) {
        this.nombre_proforma = nombre_proforma;
    }
    
}

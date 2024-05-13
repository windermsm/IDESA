package inventory.objetos;

public class ObjetosPlanilla {
    
    private int id_planilla;
    private int id_sucursal;
    private int anio_planilla;
    private String mes_planilla;
    private int periodo_planilla;
    private String fecha_inicial_planilla;
    private String fecha_final_planilla;
    private String fecha_elaboro_planilla;
    private String usuario_elaboro_planilla;
    private String tipo_planilla;
    private String estado_planilla;
    private String comentario_planilla;

    public int getAnio_planilla() {
        return anio_planilla;
    }

    public void setAnio_planilla(int anio_planilla) {
        this.anio_planilla = anio_planilla;
    }

    public String getComentario_planilla() {
        return comentario_planilla;
    }

    public void setComentario_planilla(String comentario_planilla) {
        this.comentario_planilla = comentario_planilla;
    }

    public String getEstado_planilla() {
        return estado_planilla;
    }

    public void setEstado_planilla(String estado_planilla) {
        this.estado_planilla = estado_planilla;
    }

    public String getFecha_elaboro_planilla() {
        return fecha_elaboro_planilla;
    }

    public void setFecha_elaboro_planilla(String fecha_elaboro_planilla) {
        this.fecha_elaboro_planilla = fecha_elaboro_planilla;
    }

    public String getFecha_final_planilla() {
        return fecha_final_planilla;
    }

    public void setFecha_final_planilla(String fecha_final_planilla) {
        this.fecha_final_planilla = fecha_final_planilla;
    }

    public String getFecha_inicial_planilla() {
        return fecha_inicial_planilla;
    }

    public void setFecha_inicial_planilla(String fecha_inicial_planilla) {
        this.fecha_inicial_planilla = fecha_inicial_planilla;
    }

    public int getId_planilla() {
        return id_planilla;
    }

    public void setId_planilla(int id_planilla) {
        this.id_planilla = id_planilla;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getMes_planilla() {
        return mes_planilla;
    }

    public void setMes_planilla(String mes_planilla) {
        this.mes_planilla = mes_planilla;
    }

    public int getPeriodo_planilla() {
        return periodo_planilla;
    }

    public void setPeriodo_planilla(int periodo_planilla) {
        this.periodo_planilla = periodo_planilla;
    }

    public String getTipo_planilla() {
        return tipo_planilla;
    }

    public void setTipo_planilla(String tipo_planilla) {
        this.tipo_planilla = tipo_planilla;
    }

    public String getUsuario_elaboro_planilla() {
        return usuario_elaboro_planilla;
    }

    public void setUsuario_elaboro_planilla(String usuario_elaboro_planilla) {
        this.usuario_elaboro_planilla = usuario_elaboro_planilla;
    }
    
}
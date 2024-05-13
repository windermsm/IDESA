package objetos;

import java.sql.Date;

/**
 *
 * @author FERNANDO
 */
public class ObjetosCaja {

    private String fechaInicio;
    private String fechaFin;
    private double saldoInicioDia;
    private double saldoFinalDia;
    private int cantVentas;
    

    //modulo de aperturar caja
    private String usuario;
    private String fechaApertura;
    private String hora;
    private double cantidadApertura;
    private String id;

    public ObjetosCaja() {
    }


    public double getSaldoInicioDia() {
        return saldoInicioDia;
    }

    public void setSaldoInicioDia(double saldoInicioDia) {
        this.saldoInicioDia = saldoInicioDia;
    }

    public double getSaldoFinalDia() {
        return saldoFinalDia;
    }

    public void setSaldoFinalDia(double saldoFinalDia) {
        this.saldoFinalDia = saldoFinalDia;
    }

    public int getCantVentas() {
        return cantVentas;
    }

    public void setCantVentas(int cantVentas) {
        this.cantVentas = cantVentas;
    }

    public String getUsuario() {
        return usuario;
    }


    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getCantidadApertura() {
        return cantidadApertura;
    }

    public void setCantidadApertura(double cantidadApertura) {
        this.cantidadApertura = cantidadApertura;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * paso de datos al modulo correspondiente
     *
     * @param usuario
     * @param fechaApertura
     * @param hora
     * @param cantidad
     */
    public void setDataApertura(String usuario, String fechaApertura, String hora,
            double cantidad) {
        this.usuario = usuario;
        this.fechaApertura = fechaApertura;
        this.hora = hora;
        this.cantidadApertura = cantidad;
    }

}

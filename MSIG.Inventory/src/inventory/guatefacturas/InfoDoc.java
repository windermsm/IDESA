package inventory.guatefacturas;

public class InfoDoc {
    
    private String TipoVenta;       /* @param TipoVenta Indica si el tipo de venta corresponde a B=Bienes o S=Servicios */
    private String DestinoVenta;    /* @param DestinoVenta Indica el pais de destino de la venta 1=Guatemala */
    private String Fecha;           /* @param Fecha Fecha del documento en formato DD/MM/AAAA */
    private String Moneda;          /* @param Moneda Codigo de la moneda (siendo 1=Quetzal, 2=Dolar) */
    private String Tasa;            /* @param Tasa Tasa de cambio segun moneda (1 cuando es quetzal) */
    private String Referencia;      /* @param Referencia  Indicador unico en el sistema original (EFACE) el dato es obligatorio para todos los EFACE que quieren realizar versiones */
    private String NumeroAcceso;
    private String Reversion;       /* @param Revision Opcional, indica si debe anular el documento anteriormente generado "S" para revertir la operacion "N" o vacio para no hacer revision, tambien puede no incluirse el TAG */
    private String SerieAdmin;      /* @param Serie que se utiliza de referencia dentro del establecimientos (agencia, sucursal) */
    private String NumeroAdmin;     /* @param Numero que se utiliza de referencia dentro del establecimientos (agencia, sucursal) */

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String TipoVenta) {
        this.TipoVenta = TipoVenta;
    }

    public String getDestinoVenta() {
        return DestinoVenta;
    }

    public void setDestinoVenta(String DestinoVenta) {
        this.DestinoVenta = DestinoVenta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        this.Moneda = Moneda;
    }

    public String getTasa() {
        return Tasa;
    }

    public void setTasa(String Tasa) {
        this.Tasa = Tasa;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public String getNumeroAcceso() {
        return NumeroAcceso;
    }

    public void setNumeroAcceso(String NumeroAcceso) {
        this.NumeroAcceso = NumeroAcceso;
    }

    public String getReversion() {
        return Reversion;
    }

    public void setReversion(String Reversion) {
        this.Reversion = Reversion;
    }

    public String getSerieAdmin() {
        return SerieAdmin;
    }

    public void setSerieAdmin(String SerieAdmin) {
        this.SerieAdmin = SerieAdmin;
    }

    public String getNumeroAdmin() {
        return NumeroAdmin;
    }

    public void setNumeroAdmin(String NumeroAdmin) {
        this.NumeroAdmin = NumeroAdmin;
    }
    
}

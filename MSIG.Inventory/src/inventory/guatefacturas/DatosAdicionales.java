package inventory.guatefacturas;

class DatosAdicionales {
    
    private String Email;   /*@param Correos para el envio de la factura electronica */
    private String Enviar;  /*@param Especifica si guatefacturas envia el correo S=si N=no */
    private String Observaciones; /*@param Observaciones en la factura para datos adicionales en la factura enviada por guatefacturas */

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEnviar() {
        return Enviar;
    }

    public void setEnviar(String Enviar) {
        this.Enviar = Enviar;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }
    
}

package inventory.guatefacturas;

public class Encabezado {
    
    private Receptor Receptor = new Receptor();
    private InfoDoc InfoDoc = new InfoDoc();
    private Totales Totales = new Totales();
    private DatosAdicionales DatosAdicionales = new DatosAdicionales();

    public Receptor getReceptor() {
        return Receptor;
    }

    public void setReceptor(Receptor Receptor) {
        this.Receptor = Receptor;
    }

    public InfoDoc getInfoDoc() {
        return InfoDoc;
    }

    public void setInfoDoc(InfoDoc InfoDoc) {
        this.InfoDoc = InfoDoc;
    }

    public Totales getTotales() {
        return Totales;
    }

    public void setTotales(Totales Totales) {
        this.Totales = Totales;
    }

    public DatosAdicionales getDatosAdicionales() {
        return DatosAdicionales;
    }

    public void setDatosAdicionales(DatosAdicionales DatosAdicionales) {
        this.DatosAdicionales = DatosAdicionales;
    }
    
}

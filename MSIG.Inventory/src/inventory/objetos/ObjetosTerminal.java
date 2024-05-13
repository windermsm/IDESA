package inventory.objetos;

public class ObjetosTerminal {
    
    private int id_terminal;
    private int numero_terminal;
    private String nombre_terminal;
    private String mac_adress_terminal;

    public int getId_terminal() {
        return id_terminal;
    }

    public void setId_terminal(int id_terminal) {
        this.id_terminal = id_terminal;
    }

    public String getMac_adress_terminal() {
        return mac_adress_terminal;
    }

    public void setMac_adress_terminal(String mac_adress_terminal) {
        this.mac_adress_terminal = mac_adress_terminal;
    }

    public String getNombre_terminal() {
        return nombre_terminal;
    }

    public void setNombre_terminal(String nombre_terminal) {
        this.nombre_terminal = nombre_terminal;
    }

    public int getNumero_terminal() {
        return numero_terminal;
    }

    public void setNumero_terminal(int numero_terminal) {
        this.numero_terminal = numero_terminal;
    }
    
}

package inventory.acceso;

import inventory.objetos.ObjetosTerminal;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoTerminal {

    public ArrayList<ObjetosTerminal> validarLicencia(String pMacAdressTerminal, String pUsuario, String pTerminal) {

        ArrayList<ObjetosTerminal> lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        boolean valor = false;

        String sql = "SELECT * "
                + "FROM m_terminal "
                + "WHERE mac_adress_terminal = '" + pMacAdressTerminal + "'";
        System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Ingreso Usuario", "Validar Licencia", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosTerminal objeto = new ObjetosTerminal();
                objeto.setId_terminal(tabla.getInt("id_terminal"));
                objeto.setNumero_terminal(tabla.getInt("numero_terminal"));
                objeto.setNombre_terminal(tabla.getString("nombre_terminal"));
                objeto.setMac_adress_terminal(tabla.getString("mac_adress_terminal"));
                lista.add(objeto);
            }
        } catch (Exception error) {
            lista = null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
}
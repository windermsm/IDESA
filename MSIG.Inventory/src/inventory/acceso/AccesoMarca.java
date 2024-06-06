/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosMarca;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoMarca {
    
    AccesoInventario Acceso = new AccesoInventario();
    
    public ArrayList<ObjetosMarca> listarMarcas(String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosMarca> listado = new ArrayList<>();
        String sql = "SELECT * FROM m_marca ORDER BY marca_producto";
        //System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Productos", "Listar Marcas", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosMarca obj = new ObjetosMarca();
                obj.setId_marca_producto(tabla.getInt("id_marca_producto"));
                obj.setMarca_producto(tabla.getString("marca_producto"));
                obj.setEstado(tabla.getString("estado"));
                listado.add(obj);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return listado;
        
    }
}

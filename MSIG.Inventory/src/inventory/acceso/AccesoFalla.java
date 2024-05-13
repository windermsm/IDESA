/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosFalla;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author DSANTACRUZ
 */
public class AccesoFalla {
    
    AccesoInventario Acceso = new AccesoInventario();
    
    public ArrayList<ObjetosFalla> listarFallas(String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosFalla> listado = new ArrayList<>();
        String sql = "SELECT * FROM m_falla ORDER BY nombre_falla";
        //System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Fallas", "Listar Fallas", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosFalla obj = new ObjetosFalla();
                obj.setId_categoria(tabla.getInt("id_falla"));
                obj.setNombre_falla(tabla.getString("nombre_falla"));
                obj.setCategoria_producto(tabla.getString("categoria_producto"));
                obj.setId_categoria(tabla.getInt("id_categoria"));
                obj.setEstado_categoria(tabla.getString("estado_falla"));
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

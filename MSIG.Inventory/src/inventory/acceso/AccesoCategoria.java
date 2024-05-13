/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosCategoria;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author DSANTACRUZ
 */
public class AccesoCategoria {
    
    AccesoInventario Acceso = new AccesoInventario();
    
    public ArrayList<ObjetosCategoria> listarCategorias(String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosCategoria> listado = new ArrayList<>();
        String sql = "SELECT * FROM m_categoria ORDER BY nombre_categoria";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Productos", "Listar Categorias", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosCategoria obj = new ObjetosCategoria();
                obj.setId_categoria(tabla.getInt("id_categoria"));
                obj.setNombre_categoria(tabla.getString("nombre_categoria"));
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

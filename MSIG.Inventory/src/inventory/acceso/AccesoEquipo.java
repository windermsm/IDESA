package inventory.acceso;

import inventory.objetos.ObjetosEquipo;
import inventory.objetos.ObjetosSerie;
import java.sql.ResultSet;

public class AccesoEquipo {

    public int insertarEquipo(ObjetosEquipo pEquipo, String pUsuario, String pTerminal) {

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        sql = "INSERT INTO m_equipo VALUES (NULL, '" + pEquipo.getDescripcion_equipo() 
                + "', " + pEquipo.getDepreciacion_equipo() 
                + ", '" + pEquipo.getNumero_serie() 
                + "', " + pEquipo.getCosto_equipo() 
                + ", '" + pEquipo.getEstado_equipo() + "')";
        try {
            return Acceso.ejecutarConsultaKey(sql, "Movimiliario y Equipo", "Insertar Equipo", pUsuario, pTerminal);
        } catch (Error error) {
            return 0;
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarSerieEquipo(String pNumeroSerie, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_serie "
                + "SET estado = 'Equipo Uso Sucursal' "
                + "WHERE numero_serie = '" + pNumeroSerie + "' AND estado = 'En Inventario'";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Mobiliario y Equipo", "Actualizar Serie Equipo", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarCantidadProducto(String pIdProducto, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_producto SET exi_producto = exi_producto - 1 WHERE id_producto = '" + pIdProducto + "'";
        try {
            return Acceso.ejecutarConsulta(sql, "Mobiliario y Equipo", "Actualizar Cantidad Producto", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public ObjetosSerie buscarNumeroSerie(String serie, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_serie where numero_serie = '" + serie + "'";
        ObjetosSerie obj = new ObjetosSerie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) { 
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie")); 
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
            }
        } catch (Exception Error) {
            return null;         
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
}

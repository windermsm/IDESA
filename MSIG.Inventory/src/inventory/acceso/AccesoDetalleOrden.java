package inventory.acceso;

import inventory.objetos.ObjetosDetalleOrden;
import inventory.objetos.ObjetosDetalleOrdenEstado;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoDetalleOrden {
    
    AccesoInventario Acceso = new AccesoInventario();
    
    public int insertarDetalleOrden(ObjetosDetalleOrden pProducto, String pUsuario, String pTerminal) {
        int Llave = 0;
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "INSERT INTO d_orden_producto VALUES (NULL, " + pProducto.getId_orden() + ", '" + pProducto.getId_producto() + "', "
                + "'" + pProducto.getDescripcion_d_orden_producto() + "', " + pProducto.getCantidad_d_orden_produco() + ", " 
                + pProducto.getPrecio_d_orden_producto() + ", " + pProducto.getSub_total_d_orden_producto() + ", '" + pProducto.getEstado_d_orden_producto() + "')";
        try {
            Llave = Acceso.ejecutarConsultaKey(sql, "Orden Servicio", "Insertar Producto", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
            Llave = 0;
        } finally {
            Acceso.desconectar();
        }
        return Llave;
    }
    
    public void EliminarDetalleOrden(int pIdDetalleOrden, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "DELETE FROM d_orden_producto WHERE id_d_orden_producto = " + pIdDetalleOrden;
        try {
            Acceso.ejecutarConsulta(sql, "Orden Servicio", "Eliminar Producto", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public void EliminarDetalleOrdenEstado(int pIdDetalleOrden, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "DELETE FROM d_orden_estado WHERE id_d_orden_estado = " + pIdDetalleOrden;
        try {
            Acceso.ejecutarConsulta(sql, "Orden Servicio", "Eliminar Detalle Estado Orden", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public ArrayList<ObjetosDetalleOrden> listarProductos(int pIdOrden, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosDetalleOrden> listado = new ArrayList<>();
        String sql = "SELECT * FROM d_orden_producto WHERE id_orden = " + pIdOrden;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Orden Servicio", "Listar Productos", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosDetalleOrden producto = new ObjetosDetalleOrden();
                producto.setId_d_orden_producto(tabla.getInt("id_d_orden_producto"));
                producto.setId_orden(tabla.getInt("id_orden"));
                producto.setId_producto(tabla.getString("id_producto"));
                producto.setDescripcion_d_orden_producto(tabla.getString("descripcion_d_orden_producto"));
                producto.setCantidad_d_orden_produco(tabla.getDouble("cantidad_d_orden_produco"));
                producto.setPrecio_d_orden_producto(tabla.getDouble("precio_d_orden_producto"));
                producto.setSub_total_d_orden_producto(tabla.getDouble("sub_total_d_orden_producto"));
                producto.setEstado_d_orden_producto(tabla.getString("estado_d_orden_producto"));
                listado.add(producto);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return listado;
        
    }
    
    public ObjetosDetalleOrdenEstado buscarUltimoEstadoOrden(int pIdOrden, String pUsuario, String pTerminal) {
        
        ObjetosDetalleOrdenEstado orden = new ObjetosDetalleOrdenEstado();
        String sql = "select  * "
                + "from    d_orden_estado  "
                + "where   id_d_orden_estado in ( "
                + "        select  max(id_d_orden_estado) as id_d_orden_estado "
                + "        from    d_orden_estado "
                + "        where   id_orden = " + pIdOrden
                + "        ) ";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Orden Servicio", "Buscar Ultimo Estado Orden", pUsuario, pTerminal);
            while (tabla.next()) { 
                orden.setId_d_orden_estado(tabla.getInt("id_d_orden_estado"));
                orden.setId_orden(tabla.getInt("id_orden"));
                orden.setDescripcion_d_orden_estado(tabla.getString("descripcion_d_orden_estado"));
                orden.setFecha_d_orden_estado(tabla.getString("fecha_d_orden_estado"));
                orden.setUsuario_d_orden_estado(tabla.getString("usuario_d_orden_estado"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return orden;
        
    }
    
    public ArrayList<ObjetosDetalleOrden> listarProductosOrdenFinalizada(int pIdOrden, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosDetalleOrden> listado = new ArrayList<>();
        String sql = "SELECT * FROM d_orden_producto WHERE id_orden = " + pIdOrden;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Venta de Productos", "Buscar Orden Servicio", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosDetalleOrden producto = new ObjetosDetalleOrden();
                producto.setId_d_orden_producto(tabla.getInt("id_d_orden_producto"));
                producto.setId_orden(tabla.getInt("id_orden"));
                producto.setId_producto(tabla.getString("id_producto"));
                producto.setDescripcion_d_orden_producto(tabla.getString("descripcion_d_orden_producto"));
                producto.setCantidad_d_orden_produco(tabla.getDouble("cantidad_d_orden_produco"));
                producto.setPrecio_d_orden_producto(tabla.getDouble("precio_d_orden_producto"));
                producto.setSub_total_d_orden_producto(tabla.getDouble("sub_total_d_orden_producto"));
                producto.setEstado_d_orden_producto(tabla.getString("estado_d_orden_producto"));
                listado.add(producto);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return listado;
        
    }
    
}

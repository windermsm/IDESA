package inventory.acceso;

import inventory.objetos.ObjetosDetalleFactura;
import inventory.objetos.ObjetosListaProducto;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoDetalleFactura {

    public String eliminarDetalleFactura(ObjetosDetalleFactura pDetalleFactura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete d_factura where id_d_factura = " + pDetalleFactura.getId_d_factura()
                + "id_factura = " + pDetalleFactura.getId_factura();
        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Eliminar Detalle Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarDetalleFactura(ObjetosDetalleFactura pDetalleFactura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into d_factura values (0," + pDetalleFactura.getId_factura() + ",'"
                + pDetalleFactura.getId_producto() + "'," + pDetalleFactura.getCantidad_d_factura() + ", " 
                + pDetalleFactura.getPrecio_compra_d_factura() + ", " + pDetalleFactura.getPrecio_estandar_d_factura() + ", "
                + pDetalleFactura.getPrecio_venta_d_factura() + "," + pDetalleFactura.getSub_total_d_factura()
                + ", '" + pDetalleFactura.getComentario_d_factura() + "', " + pDetalleFactura.getArancel_d_factura() + ")";
        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Insertar Detalle Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarDetalleFactura(ObjetosDetalleFactura pDetalleFactura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update d_factura set id_producto = '" + pDetalleFactura.getId_producto()
                + "', cant_d_factura = " + pDetalleFactura.getCantidad_d_factura() + ", precio_venta_factura = "
                + pDetalleFactura.getPrecio_venta_d_factura() + ", precio_compra_d_factura = " + pDetalleFactura.getPrecio_compra_d_factura() 
                + ", precio_estandar_d_factura = " + pDetalleFactura.getPrecio_estandar_d_factura() + ", "
                + "sub_total_d_factura = " + pDetalleFactura.getSub_total_d_factura()
                + ", comentario_factura = " + pDetalleFactura.getComentario_d_factura() + ", arancel_factura = " + pDetalleFactura.getArancel_d_factura()
                + " where id_d_factura = " + pDetalleFactura.getId_d_factura() + " and id_factura = " + pDetalleFactura.getId_factura();
        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Actualizar Detalle Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosDetalleFactura> seleccionarDetalleFactura(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_cliente";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Factura", "Seleccionar Detalle Factura", pUsuario, pTerminal);
            ObjetosDetalleFactura Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleFactura();
                Registros.setId_d_factura(tabla.getInt("id_d_factura"));
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCantidad_d_factura(tabla.getFloat("cantidad_d_producto"));
                Registros.setPrecio_venta_d_factura(tabla.getFloat("precio_venta_d_factura"));
                Registros.setPrecio_estandar_d_factura(tabla.getFloat("precio_estandar_d_factura"));
                Registros.setSub_total_d_factura(tabla.getFloat("sub_total_d_factura"));
                Registros.setComentario_d_factura(tabla.getString("comentario_d_factura"));
                Registros.setArancel_d_factura(tabla.getFloat("arancel_d_factura"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosDetalleFactura> buscarDetalleFactura(int id_factura, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from d_factura where id_factura = " + id_factura;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Factura", "Buscar Detalle Factura", pUsuario, pTerminal);
            ObjetosDetalleFactura Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleFactura();
                Registros.setId_d_factura(tabla.getInt("id_d_factura"));
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCantidad_d_factura(tabla.getFloat("cantidad_d_factura"));
                Registros.setPrecio_compra_d_factura(tabla.getFloat("precio_compra_d_factura"));
                Registros.setPrecio_estandar_d_factura(tabla.getFloat("precio_estandar_d_factura"));
                Registros.setPrecio_venta_d_factura(tabla.getFloat("precio_venta_d_factura"));
                Registros.setSub_total_d_factura(tabla.getFloat("sub_total_d_factura"));
                Registros.setComentario_d_factura(tabla.getString("comentario_d_factura"));
                Registros.setArancel_d_factura(tabla.getFloat("arancel_d_factura"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosListaProducto> seleccionarDetallePorFactura(String pIdFactura, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  c.id_producto,\n"
                + "        c.desc_producto,\n"
                + "        c.serie_producto,\n"
                + "        c.componente_producto,\n"
                + "        c.garantia_producto,\n"
                + "        a.cantidad_d_factura\n"
                + "FROM    d_factura AS a, \n"
                + "        m_factura AS b, \n"
                + "        m_producto AS c\n"
                + "WHERE a.id_factura = b.id_factura\n"
                + "  AND a.id_producto = c.id_producto\n"
                + "  AND c.serie_producto = 'SI'\n"
                + "  AND b.id_factura = " + pIdFactura;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Compra", "Selecionar Detalle Por Compra", pUsuario, pTerminal);
            ObjetosListaProducto Registros;
            while (tabla.next()) {
                Registros = new ObjetosListaProducto(); 
                Registros.setIdproducto(tabla.getString("id_producto")); 
                Registros.setCantidad(tabla.getInt("cantidad_d_factura")); 
                Registros.setDescripcionproducto(tabla.getString("desc_producto"));
                Registros.setSerie(tabla.getString("serie_producto")); 
                Registros.setComponentes(tabla.getString("componente_producto")); 
                Registros.setGarantia(tabla.getString("garantia_producto")); 
                lista.add(Registros); 
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
 
        return lista;
        
    }
    
}

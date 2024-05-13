package inventory.acceso;

import inventory.objetos.ObjetosDetalleCompra;
import inventory.objetos.ObjetosListaProducto;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoDetalleCompra {

    public String eliminarDetalleCompra(ObjetosDetalleCompra pDetalleCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete d_compra where id_d_compra = " + pDetalleCompra.getId_d_compra()
                + "id_compra = " + pDetalleCompra.getId_compra();
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Eliminar Detalle Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarDetalleCompra(ObjetosDetalleCompra pDetalleCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into d_compra values (0," + pDetalleCompra.getId_compra() + ",'"
                + pDetalleCompra.getId_producto() + "'," + pDetalleCompra.getCant_d_compra()
                + "," + pDetalleCompra.getPrecio_d_compra() + "," + pDetalleCompra.getSub_total_d_compra()
                + ", '" + pDetalleCompra.getComentario_compra() + "'," + pDetalleCompra.getArancel_compra() + ")";
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Insertar Detalle Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarDetalleCompra(ObjetosDetalleCompra pDetalleCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update d_compra set id_producto = '" + pDetalleCompra.getId_producto()
                + "', cant_d_compra = " + pDetalleCompra.getCant_d_compra() + ", precio_d_compra = "
                + pDetalleCompra.getPrecio_d_compra() + ", sub_total_d_compra = " + pDetalleCompra.getSub_total_d_compra()
                + ", comentario_compra = '" + pDetalleCompra.getComentario_compra() + "', arancel_compra = " + pDetalleCompra.getArancel_compra()
                + " where id_d_compra = " + pDetalleCompra.getId_d_compra() + " and id_compra = " + pDetalleCompra.getId_compra();
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Actualizar Detalle Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosDetalleCompra> seleccionarDetalleCompra(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from d_compra";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Compra", "Selecionar Detalle Compra", pUsuario, pTerminal);
            ObjetosDetalleCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleCompra();
                Registros.setId_d_compra(tabla.getInt("id_d_compra"));
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCant_d_compra(tabla.getFloat("cant_d_compra"));
                Registros.setPrecio_d_compra(tabla.getFloat("precio_d_compra"));
                Registros.setSub_total_d_compra(tabla.getFloat("sub_total_d_compra"));
                Registros.setComentario_compra(tabla.getString("comentario_compra"));
                Registros.setArancel_compra(tabla.getFloat("arancel_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosListaProducto> seleccionarDetallePorCompra(String pIdCompra, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  c.id_producto,\n"
                + "        c.desc_producto,\n"
                + "        c.serie_producto,\n"
                + "        c.componente_producto,\n"
                + "        c.garantia_producto,\n"
                + "        a.cant_d_compra\n"
                + "FROM    d_compra AS a, \n"
                + "        m_compra AS b, \n"
                + "        m_producto AS c\n"
                + "WHERE a.id_compra = b.id_compra\n"
                + "  AND a.id_producto = c.id_producto\n"
                + "  AND c.serie_producto = 'SI'\n"
                + "  AND b.id_compra = " + pIdCompra;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Compra", "Selecionar Detalle Por Compra", pUsuario, pTerminal);
            ObjetosListaProducto Registros;
            while (tabla.next()) {
                Registros = new ObjetosListaProducto(); 
                Registros.setIdproducto(tabla.getString("id_producto")); 
                Registros.setCantidad(tabla.getInt("cant_d_compra")); 
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

    public ArrayList<ObjetosDetalleCompra> buscarDetalleCompra(int pIdDCompra, int pIdCompra, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from d_compra where id_d_compra = " + pIdDCompra
                + " and id_compra = " + pIdCompra;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Compra", "Buscar Detalle Compra", pUsuario, pTerminal);
            ObjetosDetalleCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleCompra();
                Registros.setId_d_compra(tabla.getInt("id_d_compra"));
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCant_d_compra(tabla.getFloat("cant_d_compra"));
                Registros.setPrecio_d_compra(tabla.getFloat("precio_d_compra"));
                Registros.setSub_total_d_compra(tabla.getFloat("sub_total_d_compra"));
                Registros.setComentario_compra(tabla.getString("comentraio_compra"));
                Registros.setArancel_compra(tabla.getFloat("arancel_compra"));
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

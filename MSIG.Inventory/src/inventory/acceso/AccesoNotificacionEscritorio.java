/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import java.sql.ResultSet;

/**
 *
 * @author DSANTACRUZ
 */
public class AccesoNotificacionEscritorio {
    
   public String facturasProveedorPendientes(String pUsuario, String pTerminal) {

       String total = "0";

       AccesoInventario Acceso = new AccesoInventario();
       String sql = "SELECT  COUNT(b.id_compra) AS total \n"
               + "FROM    m_formas_pago AS a, \n"
               + "        m_compra AS b \n"
               + "WHERE   a.id_compra = b.id_compra \n"
               + "  and a.credito > 0 \n"
               + "  AND DATE_ADD(b.fecha_factura_compra, INTERVAL a.dias_credito DAY) < NOW()";
       
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Facturas Proveedor Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
            
    public String facturasClientePendientes(String pUsuario, String pTerminal) {

        String total = "0";

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  COUNT(a.id_factura) AS total \n"
                + "FROM    m_abono AS a, \n"
                + "        m_factura AS b \n"
                + "WHERE   a.id_factura = b.id_factura \n"
                + "  AND saldo_pendiente > 0 \n"
                + "  AND b.estado_factura NOT IN ('Anulada', 'Anulado', 'Contado', 'Cancelado', 'Pendiente')"
                + "  AND DATE_ADD(b.fecha_emision_factura, INTERVAL b.dias_de_credito DAY) < NOW()";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Facturas Proveedor Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
    
    public String reparacionesPendientes(String pUsuario, String pTerminal) {

        String total = "0";

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  COUNT(id_orden) AS total \n"
                + "FROM    m_orden \n"
                + "WHERE   estado_orden NOT IN ('Entregado') \n"
                + "    AND aplica_garantia_orden = 'NO'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Facturas Proveedor Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
    
    public String garantiaClientePendientes(String pUsuario, String pTerminal) {

        String total = "0";

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  COUNT(id_orden) AS total \n"
                + "FROM    m_orden \n"
                + "WHERE   estado_orden NOT IN ('Entregado') \n"
                + "    AND aplica_garantia_orden = 'SI'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Facturas Proveedor Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
    
    public String garantiaProveedorPendientes(String pUsuario, String pTerminal) {

        String total = "0";

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  COUNT(id_garantia_proveedor) AS total \n"
                + "FROM    m_garantia_proveedor \n"
                + "WHERE   DATE_ADD(fecha_solicitud, INTERVAL 15 DAY) < NOW() \n"
                + "    AND solucion = 'Solo Guardar'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Facturas Proveedor Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
    
    public String anticiposPendientes(String pUsuario, String pTerminal) {

        String total = "0";

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  count(id_anticipo) as total "
                + "from    m_anticipo "
                + "where   estado_anticipo not in('Despachado', 'Anulado', 'Devuelto') and DATE_ADD(fecha_entrega_persona_aticipo, INTERVAL 15 DAY) < NOW()";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Escritorio", "Obtener Anticipos Pendientes de Despachar", pUsuario, pTerminal);
            while (tabla.next()) {
                total = tabla.getString("total");
            }
        } catch (Exception error) {
            return "0";
        }

        return total;
    }
    
}

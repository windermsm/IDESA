package inventory.acceso;

import inventory.objetos.ObjetosCertificado;
import java.sql.ResultSet;

public class AccesoCertificado {
    
    AccesoInventario Acceso = new AccesoInventario();
    
    public ObjetosCertificado buscarCertificado(String pSerie, String pId_producto, String pUsuario, String pTerminal) {

        ObjetosCertificado obj = new ObjetosCertificado();
        
        String sql = "SELECT  DISTINCT c.id_cliente AS id_cliente, "
                + "        a.id_serie AS id_serie, "
                + "        b.id_factura AS id_factura, "
                + "        IFNULL(date_format(a.fecha_limite_garantia_cliente, '%d-%m-%Y'), a.fecha_limite_garantia_cliente)  AS vence, "
                + "        b.nombre_factura AS nombre, "
                + "        c.tel_cliente AS telefono, "
                + "        date_format(now(), '%d-%m-%Y') AS fecha, "
                + "        a.numero_serie AS serie, "
                + "        e.marca_producto as marca, "
                + "        e.desc_producto AS producto "
                + "FROM    m_serie AS a, "
                + "        m_factura AS b, "
                + "        m_cliente AS c, "
                + "        m_persona AS d, "
                + "        m_producto AS e "
                + "WHERE   a.id_factura = b.id_factura "
                + "    AND b.id_cliente = c.id_cliente "
                + "    AND c.id_persona = d.id_persona "
                + "    AND a.id_producto = e.id_producto "
                + "    AND a.id_producto = '" + pId_producto + "' "
                + "    AND a.numero_serie = '" + pSerie + "' ";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Movimiento de Sieres", "Listar Series", pUsuario, pTerminal);
            while (tabla.next()) {
                obj.setId_cliente(tabla.getString("id_cliente"));
                obj.setId_serie(tabla.getString("id_serie"));
                obj.setId_factura(tabla.getString("id_factura"));
                obj.setVence(tabla.getString("vence"));
                obj.setNombre(tabla.getString("nombre"));
                obj.setTelefono(tabla.getString("telefono"));
                obj.setFecha(tabla.getString("fecha"));
                obj.setSerie(tabla.getString("serie"));
                obj.setMarca(tabla.getString("marca"));
                obj.setProducto(tabla.getString("producto"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
}

package inventory.acceso;

import inventory.objetos.ObjetosFacturaFel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccesoFacturaFEL {
    
    public String insertarFacturaFEL(ObjetosFacturaFel pFel, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "INSERT INTO m_factura_fel VALUES (NULL, " + pFel.getId_factura() + ", '" + pFel.getSerie_factura_fel() + "', '" 
                + pFel.getNumero_factura_fel() + "', '" + pFel.getAutorizacion_factura_fel() + "', NOW(), '" + pUsuario 
                + "', '" + pFel.getTipo_documento_factura_fel() + "')";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Insertar Factura", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }
    
    public ObjetosFacturaFel buscarFacturaFEL(String idFactura, String pPantalla, String pUsuario, String pTerminal) {
       
        AccesoInventario acceso = new AccesoInventario();
        ObjetosFacturaFel factura = new ObjetosFacturaFel();
        
        factura.setId_factura_fel(0);
        factura.setId_factura_fel(Integer.parseInt(idFactura));
        factura.setSerie_factura_fel("0");
        factura.setNumero_factura_fel("0");
        factura.setAutorizacion_factura_fel("0");
        
        String sql = "SELECT * FROM m_factura_fel WHERE id_factura = " + idFactura;
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, pPantalla, "Generar FEL", pUsuario, pTerminal);
            if (tabla.first()) {
                factura.setId_factura_fel(tabla.getInt("id_factura_fel"));
                factura.setId_factura_fel(tabla.getInt("id_factura"));
                factura.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                factura.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                factura.setAutorizacion_factura_fel(tabla.getString("autorizacion_factura_fel"));
                factura.setFecha_factura_fel(tabla.getString("fecha_factura_fel"));
                factura.setUsuario_genero_factura_fel(tabla.getString("usuario_genero_factura_fel"));
                factura.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
            }
        } catch (SQLException error) {
            System.out.println("Error al buscar datos de FEL");
            System.out.println(error.toString());
        } finally {
            acceso.desconectar();
        }
        
        return factura;
        
    }
    
    public String actualizarDatosFEL(ObjetosFacturaFel det, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_factura_fel "
                + "SET serie_factura_fel = '" + det.getSerie_factura_fel()  + "', "
                + "numero_factura_fel = '" + det.getNumero_factura_fel() + "', " 
                + "autorizacion_factura_fel = '" + det.getAutorizacion_factura_fel() + "', "
                + "fecha_factura_fel = NOW(), "
                + "usuario_genero_factura_fel = '" + pUsuario + "', "
                + "tipo_documento_factura_fel = '" + det.getTipo_documento_factura_fel() + "' "
                + "WHERE id_factura = " + det.getId_factura();
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, pPantalla, pOpcion, pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
}

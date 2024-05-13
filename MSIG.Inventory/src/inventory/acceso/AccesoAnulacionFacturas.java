package inventory.acceso;

import inventory.objetos.ObjetosAnulacionFacturas;
import inventory.objetos.ObjetosFactura;
import inventory.objetos.ObjetosProducto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccesoAnulacionFacturas {

    public ArrayList<ObjetosAnulacionFacturas> BuscarFactura(int pEnvio, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  a.id_factura, b.id_d_factura, a.num_factura, a.serie_factura, a.folio_factura, a.fecha_emision_factura, a.total_factura, c.id_producto, c.desc_producto, c.marca_producto, b.cantidad_d_factura, b.precio_venta_d_factura, (cantidad_d_factura * precio_venta_d_factura) as subtotal "
                + " from    m_factura a, d_factura b, m_producto c "
                + " where   a.id_factura = b.id_factura and b.id_producto = c.id_producto and a.estado_factura in ('Cancelado', 'Credito') and a.id_factura = " + pEnvio;
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Anulacion Facturas", "Buscar Factura", pUsuario, pTerminal);
            ObjetosAnulacionFacturas Registros;
            
            while (tabla.next()) {
                Registros = new ObjetosAnulacionFacturas();
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setId_d_factura(tabla.getInt("id_d_factura"));
                Registros.setDescripcion(tabla.getString("desc_producto"));
                Registros.setNum_factura(tabla.getInt("num_factura"));
                Registros.setSerie_factura(tabla.getString("serie_factura"));
                Registros.setFolio_factura(tabla.getString("folio_factura"));
                Registros.setFecha_emision_factura(tabla.getString("fecha_emision_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setMarca_producto(tabla.getString("marca_producto"));
                Registros.setCant_d_factura(tabla.getFloat("cantidad_d_factura"));
                Registros.setPrecio_venta_d_factura(tabla.getFloat("precio_venta_d_factura"));
                Registros.setSubTotal(tabla.getFloat("subtotal"));
                lista.add(Registros);
            }
            
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosFactura> BuscarFacturaSinDetalle(int pEnvio, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosFactura> lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + " from    m_factura a"
                + " where   a.estado_factura in ('Cancelado', 'Credito') and a.id_factura = " + pEnvio;
        System.out.println(sql);
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Anulacion Facturas", "Buscar Factura Sin Detalle", pUsuario, pTerminal);
            ObjetosFactura Registros;
            
            while (tabla.next()) {
                Registros = new ObjetosFactura();
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setNumero_factura(tabla.getInt("num_factura"));
                Registros.setSerie_factura(tabla.getString("serie_factura"));
                Registros.setFolio_factura(tabla.getString("folio_factura"));
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setFecha_emision_factura(tabla.getString("fecha_emision_factura")); 
                Registros.setDir_envio_factura(tabla.getString("dir_envio_factura"));
                Registros.setEstado_factura(tabla.getString("estado_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta")); 
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura")); 
                /*Registros.setComision_factura(tabla.getFloat("comision_factura")); System.out.println(15);
                Registros.setNombre_empleado(tabla.getString("nombre_empleado")); System.out.println(16);
                Registros.setCajero_factura(tabla.getString("cajero_factura")); System.out.println(17);
                Registros.setNombre_factura(tabla.getString("nombre_factura")); System.out.println(18);
                Registros.setPos_factura(tabla.getString("pos_factura")); System.out.println(19);
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura")); System.out.println(20);
                Registros.setId_terminal(tabla.getInt("id_terminal")); System.out.println(21);*/
                lista.add(Registros);
            }
            
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    // Le cambio estado a la factura
    public String CambiarEstadoFactura(int pId_Factura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "update m_factura set estado_factura = 'Anulado' where id_factura = " + pId_Factura;

        try {
            return Acceso.ejecutarConsulta(sql, "Anular Venta", "Cambiar Estado Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    
    public String DevuelveAlInventario(String pId_producto, float pCantidad_producto_devuelto, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select exi_producto from m_producto where id_producto = '" + pId_producto + "'";
        float vProducto_actual_inventario = 0;

        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Anulacion Facturas", "Devuelve Inventario", pUsuario, pTerminal);
            
            while (tabla.next()) {
                vProducto_actual_inventario = tabla.getFloat("exi_producto");
            }

            float vNueva_existenacia_producto = vProducto_actual_inventario + pCantidad_producto_devuelto;
            String sql2 = "update m_producto set exi_producto = " + vNueva_existenacia_producto + " where id_producto = '" + pId_producto + "'";

            return Acceso.ejecutarConsulta(sql2, "Anular Venta", "Devuelve al Inventario", pUsuario, pTerminal);

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
    }
    
    
    public String insertarCardex(String id_movimiento, String id_producto, double cantidad, String operacion, 
            String tipo_movimiento, String usuario, String terminal) {

        String resultado = "";
        
        AccesoInventario Acceso = new AccesoInventario();
        
        // Buscar existencia actual del Producto
        ObjetosProducto producto = new ObjetosProducto();
        producto = buscarExistenciaProducto(id_producto, usuario, terminal);
        
        System.out.println("Producto: " + id_producto + " Existencia: " + producto.getExi_producto());
         
        String sql = "INSERT INTO d_cardex VALUES ( NULL, " + 
                id_movimiento + ", " + id_producto + ", " + cantidad +
                ", (" + producto.getExi_producto() + " - " + cantidad + 
                "), " + producto.getExi_producto() + ", '" + 
                operacion + "', '" + 
                tipo_movimiento + "', NOW() )";
        
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Anular Venta", "Anular Venta", usuario, terminal);
        } catch(Exception error) {
            resultado = error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
        
    }
    
    public ObjetosProducto buscarInformacionProducto(String pId_producto, String pUsuario, String pTerminal) {

        ObjetosProducto Registro = new ObjetosProducto();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_producto where visible_producto = 'SI' and id_producto = '" + pId_producto + "'";

        try {

            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Informacion Producto", pUsuario, pTerminal);

            if (tabla != null) {
                while (tabla.next()) {
                    Registro.setId_producto(tabla.getString("id_producto"));
                    Registro.setId_proveedor(tabla.getInt("id_proveedor"));
                    Registro.setMarca_producto(tabla.getString("marca_producto"));
                    Registro.setDesc_producto(tabla.getString("desc_producto"));
                    Registro.setLinea_producto(tabla.getString("linea_producto"));
                    Registro.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                    Registro.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                    Registro.setExi_producto(tabla.getFloat("exi_producto"));
                    Registro.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                    Registro.setMaximo_producto(tabla.getFloat("maximo_producto"));
                    Registro.setMinimo_producto(tabla.getFloat("minimo_producto"));
                    Registro.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                    Registro.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                    Registro.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                    Registro.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                    Registro.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                    Registro.setFactura_producto(tabla.getString("factura_producto"));
                    Registro.setDescuento_producto(tabla.getString("descuento_producto"));
                    Registro.setVisible_producto(tabla.getString("visible_producto"));
                    Registro.setSerie_producto(tabla.getString("serie_producto"));
                    Registro.setComponente_producto(tabla.getString("componente_producto"));
                    Registro.setGarantia_producto(tabla.getString("garantia_producto"));
                    Registro.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                    Registro.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                }
            }

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Registro;
        
    }
    
    public ObjetosProducto buscarExistenciaProducto(String pId_producto, String pUsuario, String pTerminal) {

        ObjetosProducto Registro = new ObjetosProducto();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_producto where id_producto = '" + pId_producto + "'";

        try {

            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Informacion Producto", pUsuario, pTerminal);

            if (tabla != null) {
                while (tabla.next()) {
                    Registro.setId_producto(tabla.getString("id_producto"));
                    Registro.setId_proveedor(tabla.getInt("id_proveedor"));
                    Registro.setMarca_producto(tabla.getString("marca_producto"));
                    Registro.setDesc_producto(tabla.getString("desc_producto"));
                    Registro.setLinea_producto(tabla.getString("linea_producto"));
                    Registro.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                    Registro.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                    Registro.setExi_producto(tabla.getFloat("exi_producto"));
                    Registro.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                    Registro.setMaximo_producto(tabla.getFloat("maximo_producto"));
                    Registro.setMinimo_producto(tabla.getFloat("minimo_producto"));
                    Registro.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                    Registro.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                    Registro.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                    Registro.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                    Registro.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                    Registro.setFactura_producto(tabla.getString("factura_producto"));
                    Registro.setDescuento_producto(tabla.getString("descuento_producto"));
                    Registro.setVisible_producto(tabla.getString("visible_producto"));
                    Registro.setSerie_producto(tabla.getString("serie_producto"));
                    Registro.setComponente_producto(tabla.getString("componente_producto"));
                    Registro.setGarantia_producto(tabla.getString("garantia_producto"));
                    Registro.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                    Registro.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                }
            }

        } catch (SQLException error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Registro;
        
    }
    
}

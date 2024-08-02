package inventory.acceso;

import inventory.objetos.ObjetosEmpleado;
import inventory.objetos.ObjetosFactura;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccesoFactura {

    public String eliminarFactura(ObjetosFactura pFactura, String pUsuario, String pTerminal) {

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete from m_factura where id_factura = " + pFactura.getId_factura();

        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Eliminar Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String cobrarFactura(String no_factura, String cajero, String tipo, String pUsuario, String pTerminal) {

        String actualizacion = "";
        String cancelacion = "";

        if (tipo.equals("Credito") || tipo.equals("Credito / Contado")) {
            actualizacion = "Credito";
        }
        
        if (tipo.equals("Tarjeta") || tipo.equals("Contado")) {
            cancelacion = ", fecha_cancelacion_factura = NOW() ";
            actualizacion = "Cancelado";
        }
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_factura SET estado_factura = '" + actualizacion + "', cajero_factura = '" + cajero + "' " + cancelacion + " where id_factura = " + no_factura;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Ventas Activas", "Cobrar Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }

    public String insertarFactura(ObjetosFactura pFactura, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "insert into m_factura values (NULL, "
                + pFactura.getNumero_factura() + ", '"
                + pFactura.getSerie_factura() + "', '"
                + pFactura.getFolio_factura() + "', "
                + pFactura.getId_cliente() + ", now(), '"
                + pFactura.getDir_envio_factura() + "', '"
                + pFactura.getEstado_factura() + "', "
                + pFactura.getId_empleado() + ", "
                + pFactura.getTotal_bruto_factura() + ", "
                + pFactura.getTotal_factura() + ", '"
                + pFactura.getTipo_venta() + "', '"
                + pFactura.getDias_de_credito() + "', "
                + pFactura.getMonto_credito() + ", "
                + pFactura.getMonto_contado() + ", "
                + pFactura.getIva_factura() + ", "
                + pFactura.getComision_factura() + ", '"
                + pFactura.getCajero_factura() + "', '"
                + pFactura.getNombre_factura() + "', '"
                + pFactura.getPos_factura() + "', NULL, " + pFactura.getId_terminal() + ")";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Guardar Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }

    public String actualizarFactura(ObjetosFactura pFactura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_factura set num_factura = " + pFactura.getNumero_factura()
                + ", serie_factura = '" + pFactura.getSerie_factura()
                + "', folio_factura = '" + pFactura.getFolio_factura()
                + "', id_cliente = " + pFactura.getId_cliente()
                + ", fecha_emision_factura = '" + pFactura.getFecha_emision_factura()
                + "', dir_envio_factura = '" + pFactura.getDir_envio_factura()
                + "', estado_factura = '" + pFactura.getEstado_factura()
                + "', id_empleado = " + pFactura.getId_empleado()
                + "', total_factura = " + pFactura.getTotal_factura()
                + ", tipo_venta = '" + pFactura.getTipo_venta()
                + "', dias_de_credito = '" + pFactura.getDias_de_credito()
                + "', monto_credito = " + pFactura.getMonto_credito()
                + ", monto_contado = " + pFactura.getMonto_contado()
                + ", iva_factura = " + pFactura.getIva_factura()
                + ", comision_factura = " + pFactura.getComision_factura()
                + ", cajero_factura = '" + pFactura.getCajero_factura() + "'"
                + ", nombre_factura = '" + pFactura.getNombre_factura() + "'"
                + ", pos_factura = '" + pFactura.getPos_factura() + "'"
                + ", id_terminal = " + pFactura.getId_terminal()
                + " where id_factura = " + pFactura.getId_factura();
        try {
            return Acceso.ejecutarConsulta(sql, "Venta de Productos", "Actualizar Factura", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosFactura> seleccionarFactura(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_factura, m_empleado "
                + "where estado_factura = 'Pendiente' "
                + "and m_factura.id_empleado = m_empleado.id_empleado "
                + "order by id_factura";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura", pUsuario, pTerminal);
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
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                lista.add(Registros);
            }
        } catch (SQLException error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public int seleccionarIdFactura(ObjetosFactura pFactura, String pUsuario, String pTerminal) {
        
        int valor = 0;
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  max(id_factura) AS id_factura\n"
                + "from    m_factura\n"
                + "where   num_factura = " + pFactura.getNumero_factura() + "\n"
                + "    and serie_factura = '" + pFactura.getSerie_factura() + "'\n"
                + "    and id_cliente = " + pFactura.getId_cliente() + "\n"
                + "    and total_Factura = " + pFactura.getTotal_factura() + "\n"
                + "    and estado_factura = '" + pFactura.getEstado_factura() + "'\n"
                + "    and id_terminal = " + pFactura.getId_terminal();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar ID Factura", pUsuario, pTerminal);
            while (tabla.next()) {
                valor = tabla.getInt("id_factura");
                
            }
        } catch (Exception error) {
            valor = 0;
        } finally {
            Acceso.desconectar();
        }
        return valor;
    }

    public ArrayList<ObjetosFactura> seleccionarFacturaFecha(String pFecha_inicial, String pFecha_final, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  * "
                + "from    m_factura as a "
                + "        left join m_factura_fel as b on a.id_factura = b.id_factura "
                + "where   str_to_date(date_format(a.fecha_emision_factura, '%Y-%m-%d'), '%Y-%m-%d') between str_to_date('" + pFecha_inicial + "', '%Y-%m-%d') and str_to_date('" + pFecha_final + "', '%Y-%m-%d') "
                + "order by a.fecha_emision_factura DESC";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Fecha", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                //Validar si viene null al no tener factura FEL
                String valor = "";
                if(tabla.getString("tipo_documento_factura_fel") == null || tabla.getString("tipo_documento_factura_fel").toString().equals("null")) {
                    valor = "S/F";
                } else {
                    valor = tabla.getString("tipo_documento_factura_fel");
                }
                Registros.setTipo_documento_factura_fel(valor);
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosFactura> seleccionarFacturaFechaTipo(String tipo, String pFecha_inicial, String pFecha_final, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  * "
                + "from    m_factura as a "
                + "        left join m_factura_fel as b on a.id_factura = b.id_factura "
                + "where   a.tipo_venta = '" + tipo + "' AND str_to_date(date_format(a.fecha_emision_factura, '%Y-%m-%d'), '%Y-%m-%d') between str_to_date('" + pFecha_inicial + "', '%Y-%m-%d') and str_to_date('" + pFecha_final + "', '%Y-%m-%d') "
                + "order by a.fecha_emision_factura DESC";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Fecha", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosFactura> seleccionarFacturaNombre(String nombre, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_factura as a left join m_factura_fel as b on a.id_factura = b.id_factura "
                + "where upper(a.nombre_factura) like upper('%" + nombre + "%')";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Nombre", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosFactura> seleccionarFacturaPorId(String id, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_factura as a left join m_factura_fel as b on a.id_factura = b.id_factura "
                + "where a.id_factura = " + id;

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Nombre", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosFactura> seleccionarNumeroFacturaFEL(String numero, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * "
                + "FROM    m_factura_fel AS a, "
                + "        m_factura AS b "
                + "WHERE   a.id_factura = b.id_factura "
                + "  AND a.numero_factura_fel = " + numero;

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Nombre", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    
    public ArrayList<ObjetosFactura> seleccionarSerieFacturaFEL(String serie, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * "
                + "FROM    m_factura_fel AS a, "
                + "        m_factura AS b "
                + "WHERE   a.id_factura = b.id_factura "
                + "  AND a.serie_factura_fel = '" + serie + "'";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por Nombre", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    
    public ArrayList<ObjetosFactura> seleccionarFacturaNIT(String nit, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * "
                + "FROM    m_factura AS a left join m_factura_fel as d on a.id_factura = d.id_factura, "
                + "        m_cliente as b, "
                + "        m_persona as c "
                + "WHERE   a.id_cliente = b.id_cliente "
                + "    AND b.id_persona = c.id_persona "
                + "    AND UPPER(c.nit_persona) = UPPER('" + nit + "')";
        System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar Factura por NIT", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                Registros.setSerie_factura_fel(tabla.getString("serie_factura_fel"));
                Registros.setNumero_factura_fel(tabla.getString("numero_factura_fel"));
                Registros.setAtorizacion_fel(tabla.getString("autorizacion_factura_fel"));
                Registros.setTipo_documento_factura_fel(tabla.getString("tipo_documento_factura_fel"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public int retornaIDEmpleado(String pNombre, String pUsuario, String pTerminal) {
        int registro = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select id_empleado from m_empleado where nombre_empleado = '" + pNombre + "'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna ID Empleado", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getInt("id_empleado");
            }
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
    
    public ObjetosEmpleado retornaEmpleado(String pNombre, String pUsuario, String pTerminal) {
        
        ObjetosEmpleado empleado = new ObjetosEmpleado();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "select * from m_empleado where nombre_empleado = '" + pNombre + "' AND estado_empleado = 'A'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna ID Empleado", pUsuario, pTerminal);
            while (tabla.next()) {
                empleado.setId_empleado(tabla.getInt("id_empleado"));
                empleado.setNombre_empleado(tabla.getString("nombre_empleado"));
                empleado.setTipo_empleado(tabla.getString("tipo_empleado"));
                empleado.setSalario_empleado(tabla.getDouble("salario_empleado"));
                empleado.setBono_empleado(tabla.getDouble("bono_empleado"));
            }
        } catch (Exception error) {
            return empleado;
        } finally {
            acceso.desconectar();
        }
        
        return empleado;
        
    }

    public ArrayList<ObjetosFactura> retornaNombreEmpleados(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_empleado where estado_empleado = 'A'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna Nombre Empleados", pUsuario, pTerminal);
            ObjetosFactura registros;

            while (tabla.next()) {
                registros = new ObjetosFactura();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public float retornaComisionEmpleado(int pIdEmpleado, String pUsuario, String pTerminal) {
        float registro = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select porcentaje_comision from m_empleado where id_empleado = " + pIdEmpleado;


        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retona Comision Empleado", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getFloat("porcentaje_comision");
            }
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public float retornaCreditoDisponible(String pNit, String pUsuario, String pTerminal) {
        
        float limite = 0;
        float saldo = 0;
        float registro = 0;
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select * from vw_saldo_disponible_cliente where nit = '" + pNit + "'";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna Credito Disponible", pUsuario, pTerminal);
            while (tabla.next()) {
                limite = tabla.getFloat("limite");
                saldo = tabla.getFloat("credito");
            }
            registro = limite - saldo;
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        
        return registro;
    }

    public String retornaCategoriaCliente(String pNit, String pUsuario, String pTerminal) {
        String registro = "";
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select c.tipo_cliente from m_persona a, m_cliente b, m_cliente c where a.id_persona = b.id_persona and b.id_cliente = c.id_cliente and a.nit_persona = '" + pNit + "'";
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna Categoria Cliente", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getString("tipo_cliente");
            }
        } catch (Exception error) {
            return "";
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public String retornatRolUsuario(String pUsuario, String pTerminal) {
        
        String registro = "";

        AccesoInventario acceso = new AccesoInventario();
        String sql = "select tipo_usuario from m_usuario where nombre_usuario = '" + pUsuario + "'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna Rol Usuario", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getString("tipo_usuario");
            }
        } catch (Exception error) {
            return "";
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public ArrayList<ObjetosFactura> buscarFacturaPorId(String id_factura, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_factura where id_factura = " + id_factura;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Buscar Factura por ID", pUsuario, pTerminal);
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
                Registros.setTotal_bruto_factura(tabla.getFloat("total_bruto_factura"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setDias_de_credito(tabla.getString("dias_de_credito"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setIva_factura(tabla.getFloat("iva_factura"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setCajero_factura(tabla.getString("cajero_factura"));
                Registros.setNombre_factura(tabla.getString("nombre_factura"));
                Registros.setPos_factura(tabla.getString("pos_factura"));
                Registros.setFecha_cancelacion_factura(tabla.getString("fecha_cancelacion_factura"));
                Registros.setId_terminal(tabla.getInt("id_terminal"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public String retornatNumeroFactura(String serie, String folio, String pUsuario, String pTerminal) {
        
        String registro = "";

        AccesoInventario acceso = new AccesoInventario();
        String sql = "select ifnull(max(num_factura),0) + 1 as numero from m_factura where serie_factura = '" + serie + "' and folio_factura = '" + folio + "'";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Retorna Numero Factura", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getString("numero");
            }
        } catch (Exception error) {
            registro = "0";
        } finally {
            acceso.desconectar();
        }
        
        return registro;
    }
    
    public String buscarComprobantePOS(String pos, String pUsuario, String pTerminal) {
        String registro = "";
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT COUNT(id_factura) AS cantidad FROM m_factura WHERE pos_factura = " + pos;
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Buscar Comprobante POS", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getString("cantidad");
            }
        } catch (Exception error) {
            registro = "";
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
    
    public String buscarNombreFactura(String pIdFactura, String pUsuario, String pTerminal) {
        String registro = "";
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT * FROM m_factura WHERE id_factura = " + pIdFactura;
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Factura", "Buscar Comprobante POS", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getString("nombre_factura");
            }
        } catch (Exception error) {
            registro = "";
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
    
    public String buscarEstadoFactua(String idFactura, String pUsuario, String pTerminal) {
        
        String valor = "Ninguno";
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT estado_factura AS estado FROM m_factura WHERE id_factura = " + idFactura;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Factura", "Seleccionar ID Factura", pUsuario, pTerminal);
            while (tabla.next()) {
                valor = tabla.getString("estado"); 
            }
        } catch (SQLException error) {
            valor = "Ninguno";
        } finally {
            Acceso.desconectar();
        }
        return valor;
    }
    
}

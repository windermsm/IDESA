package inventory.acceso;

import inventory.objetos.ObjetosOrden;
import inventory.objetos.ObjetosOrdenLlamada;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoOrden {
    
    public String eliminarOrden(String pIdOrden, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new  AccesoInventario();
        String sql = "DELETE FROM m_orde WHERE id_orden = " + pIdOrden;
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Eliminar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String crearOrden(ObjetosOrden pOrden, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_orden VALUES ( "
                + " NULL "
                + "," + pOrden.getId_cliente() + " "
                + ",'" + pOrden.getNombre_cliente_orden() + "' "
                + "," + pOrden.getTelefono_cliente_orden() + " "
                + ", NOW() "
                + ",'" + pOrden.getUsuario_creacion_orden() + "' "
                + "," + pOrden.getId_serie() + " "
                + ",'" + pOrden.getSerie_producto_orden() + "' "
                + ",'" + pOrden.getMarca_producto_orden() + "' "
                + ",'" + pOrden.getModelo_producto_orden() + "' "
                + ",'" + pOrden.getCaracteristicas_producto_orden() + "' "
                + ",'" + pOrden.getDefecto_reportado_orden() + "' "
                + ",'" + pOrden.getObservaciones_cliente_orden() + "' "
                + ",'" + pOrden.getDiagnostico_orden() + "' "
                + ",'" + pOrden.getAplica_garantia_orden() + "' "
                + ",NULL "
                + ",'" + pOrden.getCliente_acepta_presupuesto_orden() + "' "
                + ",NULL "
                + "," +  pOrden.getValor_presupuesto_orden() + " "
                + "," + pOrden.getAbono_presupuesto_orden() + " "
                + ",'" + pOrden.getTecnico_encargado_reparacion_orden() + "' "
                + ",NULL "
                + ",NULL "
                + ",NULL "
                + "," + pOrden.getId_factura_emitida() + ", 'Creada'" 
                + ", '" + pOrden.getCase_orden() 
                + "', '" + pOrden.getCargador_orden() 
                + "', '" + pOrden.getBateria_orden() 
                + "', '" + pOrden.getRam1_orden() 
                + "', '" + pOrden.getRam2_orden() 
                + "', '" + pOrden.getRam3_orden() 
                + "', '" + pOrden.getRam4_orden() 
                + "', '" + pOrden.getFuente_orden() 
                + "', '" + pOrden.getOtros_orden() 
                + "', " + pOrden.getTotal_orden() 
                + ", '" + pOrden.getTarjeta_orden() 
                + "', '" + pOrden.getDisco_orden() 
                + "', '" + pOrden.getTipo_producto_orden() 
                + "', '" + pOrden.getTipo_trabajo_orden() + "') "; 
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String insertarOrden(ObjetosOrden pOrden, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "INSERT INTO m_orden VALUES ( "
                + " NULL "
                + "," + pOrden.getId_cliente() + " "
                + ",'" + pOrden.getNombre_cliente_orden() + "' "
                + "," + pOrden.getTelefono_cliente_orden() + " "
                + ",'" + pOrden.getFecha_creacion_orden() + "' "
                + ",'" + pOrden.getUsuario_creacion_orden() + "' "
                + "," + pOrden.getId_serie() + " "
                + ",'" + pOrden.getSerie_producto_orden() + "' "
                + ",'" + pOrden.getMarca_producto_orden() + "' "
                + ",'" + pOrden.getModelo_producto_orden() + "' "
                + ",'" + pOrden.getCaracteristicas_producto_orden() + "' "
                + ",'" + pOrden.getDefecto_reportado_orden() + "' "
                + ",'" + pOrden.getObservaciones_cliente_orden() + "' "
                + ",'" + pOrden.getDiagnostico_orden() + "' "
                + ",'" + pOrden.getAplica_garantia_orden() + "' "
                + ",'" +  pOrden.getSerie_entregada_garantia_orden() + "' "
                + ",'" + pOrden.getCliente_acepta_presupuesto_orden() + "' "
                + ",'" + pOrden.getFecha_acepta_presupuesto_orden() + "' "
                + "," +  pOrden.getValor_presupuesto_orden() + " "
                + "," + pOrden.getAbono_presupuesto_orden() + " "
                + ",'" + pOrden.getTecnico_encargado_reparacion_orden() + "' "
                + ",'"+ pOrden.getFecha_programada_entrega_orden() + "' "
                + ",'" + pOrden.getFecha_notifica_cliente_orden() + "' "
                + ",'" + pOrden.getFecha_entrega_orden() + "' "
                + "," + pOrden.getId_factura_emitida() + ", '" 
                + pOrden.getEstado_orden() + "'"
                + ", '" + pOrden.getCase_orden() 
                + "', '" + pOrden.getCargador_orden() 
                + "', '" + pOrden.getBateria_orden() 
                + "', '" + pOrden.getRam1_orden() 
                + "', '" + pOrden.getRam2_orden() 
                + "', '" + pOrden.getRam3_orden() 
                + "', '" + pOrden.getRam4_orden() 
                + "', '" + pOrden.getFuente_orden() 
                + "', '" + pOrden.getOtros_orden() 
                + "', " + pOrden.getTotal_orden() 
                + ", '" + pOrden.getTarjeta_orden() 
                + "', '" + pOrden.getDisco_orden() 
                + "', '" + pOrden.getTipo_producto_orden() 
                + "', '" + pOrden.getTipo_trabajo_orden() + "') ";
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarUltimoEstadoOrden(String pEstado, int pIdOrden, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = " update m_orden set estado_orden = '" + pEstado + "' where id_orden = " + pIdOrden;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Actualizar Ultimo Estado", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarOrden(ObjetosOrden pOrden, String pRol, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        
        String administrador = "";
        
        //Si es el administrador se pude actualizar los numero de serie
        if (pRol.equals("Administrador")) {
            administrador = ", case_orden = '" + pOrden.getCase_orden() + "', "
                    + "cargador_orden = '" + pOrden.getCargador_orden() + "', "
                    + "bateria_orden = '" + pOrden.getBateria_orden() + "', "
                    + "ram1_orden = '" + pOrden.getRam1_orden() + "', "
                    + "ram2_orden = '" + pOrden.getRam2_orden() + "', "
                    + "ram3_orden = '" + pOrden.getRam3_orden() + "', "
                    + "ram4_orden = '" + pOrden.getRam4_orden() + "', "
                    + "fuente_orden = '" + pOrden.getFuente_orden() + "', "
                    + "otros_orden = '" + pOrden.getOtros_orden() + "', "
                    + "total_orden = '" + pOrden.getTotal_orden() + "', "
                    + "tarjeta_orden = '" + pOrden.getTarjeta_orden() + "', "
                    + "disco_orden = '" + pOrden.getDisco_orden() + "' ";
        }
        
        String sql = "update m_orden SET "
                + " id_cliente =  " + pOrden.getId_cliente()
                + ", nombre_cliente_orden = '" + pOrden.getNombre_cliente_orden() + "' "
                + ", telefono_cliente_orden = " + pOrden.getTelefono_cliente_orden()
                + ", fecha_creacion_orden = '" + pOrden.getFecha_creacion_orden() + "' "
                + ", usuario_creacion_orden = '" + pOrden.getUsuario_creacion_orden() + "' "
                + ", id_serie = " + pOrden.getId_serie()
                + ", serie_producto_orden = '" + pOrden.getSerie_producto_orden() + "' "
                + ", marca_producto_orden = '" + pOrden.getMarca_producto_orden() + "' "
                + ", modelo_producto_orden = '" + pOrden.getModelo_producto_orden() + "' "
                + ", caracteristicas_producto_orden = '" + pOrden.getCaracteristicas_producto_orden() + "' "
                + ", defecto_reportado_orden = '" + pOrden.getDefecto_reportado_orden() + "' "
                + ", observaciones_cliente_orden = '" + pOrden.getObservaciones_cliente_orden() + "' "
                + ", diagnostico_orden = '" + pOrden.getDiagnostico_orden() + "' "
                + ", aplica_garantia_orden = '" + pOrden.getAplica_garantia_orden() + "' "
                + ", serie_entregada_garantia_orden = '" + ( pOrden.getSerie_entregada_garantia_orden() == "NULL" ? "" : pOrden.getSerie_entregada_garantia_orden() ) + "' "
                + ", cliente_acepta_presupuesto_orden = '" + pOrden.getCliente_acepta_presupuesto_orden() + "' "
                + ", fecha_acepta_presupuesto_orden = " + ( pOrden.getFecha_acepta_presupuesto_orden() == null ? "NULL" : "'" + pOrden.getFecha_acepta_presupuesto_orden() + "'" ) + " "
                + ", valor_presupuesto_orden = " + pOrden.getValor_presupuesto_orden()
                + ", abono_presupuesto_orden = " + pOrden.getAbono_presupuesto_orden()
                + ", tecnico_encargado_reparacion_orden = '" + pOrden.getTecnico_encargado_reparacion_orden() + "' "
                + ", fecha_programada_entrega_orden = " + ( pOrden.getFecha_programada_entrega_orden() == null ? "NULL" : "'" + pOrden.getFecha_programada_entrega_orden() + "'" ) + " "
                + ", fecha_notifica_cliente_orden = " + ( pOrden.getFecha_notifica_cliente_orden() == null ? "NULL" : "'" + pOrden.getFecha_notifica_cliente_orden() + "'" ) + " "
                + ", fecha_entrega_orden = " + ( pOrden.getFecha_entrega_orden() == null ? "NULL" : "'" + pOrden.getFecha_entrega_orden() + "'" ) + " "
                + ", id_factura_emitida = " + pOrden.getId_factura_emitida()
                + ", tipo_producto_orden = '" + pOrden.getTipo_producto_orden() + "'"
                + ", tipo_trabajo_orden = '" + pOrden.getTipo_trabajo_orden() + "'"
                + ", estado_orden = '" + pOrden.getEstado_orden() + "' "
                + administrador + " WHERE id_orden = " + pOrden.getId_orden();
        
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public ArrayList<ObjetosOrden> listarOrdenes(String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ArrayList<ObjetosOrden> listarOrdenesPorFecha(String pFechaInicial, String pFechaFinal, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden " 
                + "WHERE STR_TO_DATE(DATE_FORMAT(fecha_creacion_orden, '%Y-%m-%d'), '%Y-%m-%d') between STR_TO_DATE('"
                + pFechaInicial
                + "', '%Y-%m-%d') and STR_TO_DATE('"
                + pFechaFinal
                + "', '%Y-%m-%d') ORDER BY fecha_creacion_orden DESC";;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden")); 
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ArrayList<ObjetosOrden> listarOrdenesPorEstado(String pEstado, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE estado_orden = '" + pEstado + "'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ArrayList<ObjetosOrden> listarOrdenesAplicaGarantia(String pAplica, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE aplica_garantia_orden = '" + pAplica + "'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar Aplica Garantia", pUsuario, pTerminal);
            
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ArrayList<ObjetosOrden> listarOrdenesPorNombre(String pNombre, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE UPPER(nombre_cliente_orden) LIKE UPPER('%" + pNombre + "%')";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ArrayList<ObjetosOrden> listarOrdenesPorSerie(String pSerie, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosOrden> Lista = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE serie_producto_orden LIKE UPPER('" + pSerie + "')";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosOrden Registro = new ObjetosOrden();
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
                Lista.add(Registro);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Lista;
        
    }
    
    public ObjetosOrden buscarOrdenPorId(int pId_orden, String pUsuario, String pTerminal) {
        
        ObjetosOrden Registro = new ObjetosOrden();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE id_orden = " + pId_orden;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
            } 
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }

        return Registro;

    }
    
    public String equiposPendientes(String pTecnico, String pUsuario, String pTerminal) {
     
        String cantidad = "0";
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "SELECT COUNT(*) AS cantidad "
                + "FROM m_orden "
                + "WHERE estado_orden NOT IN ('No Acepta Reparacion', 'Remplazo de Equipo', 'Rembolso de Efectivo', 'Finaliza Reparacion', 'Notifica Finalizacion', 'Entregado') "
                + "AND tecnico_encargado_reparacion_orden = '" + pTecnico + "' ";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Ordenes de Servicio", "Buscar Equipos Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                cantidad = tabla.getString("cantidad");
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return cantidad;
        
    }
    
    public String equiposEnEspera(String pTecnico, String pUsuario, String pTerminal) {
     
        String cantidad = "0";
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "SELECT COUNT(*) AS cantidad "
                + "FROM m_orden "
                + "WHERE estado_orden IN ('En Espera') AND tecnico_encargado_reparacion_orden = '" + pTecnico + "' ";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Ordenes de Servicio", "Buscar Equipos Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                cantidad = tabla.getString("cantidad");
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return cantidad;
        
    }
    
    public String equiposGarantia(String pTecnico, String pUsuario, String pTerminal) {
     
        String cantidad = "0";
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "SELECT COUNT(*) AS cantidad "
                + "FROM m_orden "
                + "WHERE aplica_garantia_orden = 'SI' "
                + " AND estado_orden NOT IN ('No Acepta Reparacion', 'Remplazo de Equipo', 'Rembolso de Efectivo', 'Finaliza Reparacion', 'Notifica Finalizacion', 'Entregado') "
                + " AND tecnico_encargado_reparacion_orden = '" + pTecnico + "' ";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Ordenes de Servicio", "Buscar Equipos Pendientes", pUsuario, pTerminal);
            while (tabla.next()) {
                cantidad = tabla.getString("cantidad");
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return cantidad;
        
    }

    public ObjetosOrden buscarOrdenPorSerie(String pSerie, String pIdProducto, String pUsuario, String pTerminal) {

        ObjetosOrden Registro = new ObjetosOrden();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT DISTINCT 0 AS id_orden, \n"
                + "        b.id_cliente AS id_cliente, \n"
                + "        d.nom_persona AS nombre_cliente_orden, \n"
                + "        c.tel_cliente AS telefono_cliente_orden, \n"
                + "        NOW() AS fecha_creacion_orden, \n"
                + "        '" + pUsuario + "' AS usuario_creacion_orden, \n"
                + "        a.id_serie AS id_serie, \n"
                + "        a.numero_serie AS serie_producto_orden, \n"
                + "        e.marca_producto AS marca_producto_orden, \n"
                + "        '0' AS modelo_producto_orden, \n"
                + "        e.desc_producto AS caracteristicas_producto_orden, \n"
                + "        '' AS defecto_reportado_orden, \n"
                + "        '' AS observaciones_cliente_orden, \n"
                + "        '' AS diagnostico_orden, \n"
                + "        IF(DATEDIFF(a.fecha_limite_garantia_cliente, NOW()) > 0, 'SI', 'NO') AS aplica_garantia_orden, \n"
                + "        '' AS serie_entregada_garantia_orden, \n"
                + "        'NO' AS cliente_acepta_presupuesto_orden, \n"
                + "        NULL AS fecha_acepta_presupuesto_orden, \n"
                + "        0.0 AS valor_presupuesto_orden, \n"
                + "        0.0 AS abono_presupuesto_orden, \n"
                + "        '" + pUsuario + "' AS tecnico_encargado_reparacion_orden, \n"
                + "        NULL AS fecha_programada_entrega_orden, \n"
                + "        NULL AS fecha_notifica_cliente_orden, \n"
                + "        NULL AS fecha_entrega_orden, \n"
                + "        b.id_factura AS id_factura_emitida, \n"
                + "        'Pendiente' AS estado_orden \n"
                + "FROM    m_serie AS a, \n"
                + "        m_factura AS b, \n"
                + "        m_cliente AS c, \n"
                + "        m_persona AS d, \n"
                + "        m_producto AS e \n"
                + "WHERE   a.id_factura = b.id_factura \n"
                + "    AND b.id_cliente = c.id_cliente \n"
                + "    AND c.id_persona = d.id_persona \n"
                + "    AND a.id_producto = e.id_producto \n"
                + "    AND a.numero_serie = '" + pSerie + "'"; 
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_orden(tabla.getInt("id_orden")); 
                Registro.setId_cliente(tabla.getInt("id_cliente")); 
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden")); 
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden")); 
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));  
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden")); 
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden")); 
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden")); 
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden")); 
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden")); 
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden")); 
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden")); 
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden")); 
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden")); 
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden")); 
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden")); 
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden")); 
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida")); 
                Registro.setEstado_orden(tabla.getString("estado_orden")); 
                Registro.setCase_orden(""); 
                Registro.setCargador_orden("");
                Registro.setBateria_orden(""); 
                Registro.setRam1_orden(""); 
                Registro.setRam2_orden(""); 
                Registro.setRam3_orden("");
                Registro.setRam4_orden(""); 
                Registro.setFuente_orden(""); 
                Registro.setOtros_orden(""); 
                Registro.setTotal_orden(0.0);
                Registro.setTarjeta_orden("");
                Registro.setDisco_orden("");
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Registro;
        
    }
    
    public ObjetosOrden buscarOrdenPorSerieExistente(String pSerie, String pUsuario, String pTerminal) {
        
        ObjetosOrden Registro = new ObjetosOrden();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden WHERE serie_producto_orden = '" + pSerie + "'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial Ordenes de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_orden(tabla.getInt("id_orden"));
                Registro.setId_cliente(tabla.getInt("id_cliente"));
                Registro.setNombre_cliente_orden(tabla.getString("nombre_cliente_orden"));
                Registro.setTelefono_cliente_orden(tabla.getInt("telefono_cliente_orden"));
                Registro.setFecha_creacion_orden(tabla.getString("fecha_creacion_orden"));
                Registro.setUsuario_creacion_orden(tabla.getString("usuario_creacion_orden"));
                Registro.setId_serie(tabla.getInt("id_serie"));
                Registro.setSerie_producto_orden(tabla.getString("serie_producto_orden"));
                Registro.setMarca_producto_orden(tabla.getString("marca_producto_orden"));
                Registro.setModelo_producto_orden(tabla.getString("modelo_producto_orden"));
                Registro.setCaracteristicas_producto_orden(tabla.getString("caracteristicas_producto_orden"));
                Registro.setDefecto_reportado_orden(tabla.getString("defecto_reportado_orden"));
                Registro.setObservaciones_cliente_orden(tabla.getString("observaciones_cliente_orden"));
                Registro.setDiagnostico_orden(tabla.getString("diagnostico_orden"));
                Registro.setAplica_garantia_orden(tabla.getString("aplica_garantia_orden"));
                Registro.setSerie_entregada_garantia_orden(tabla.getString("serie_entregada_garantia_orden"));
                Registro.setCliente_acepta_presupuesto_orden(tabla.getString("cliente_acepta_presupuesto_orden"));
                Registro.setFecha_acepta_presupuesto_orden(tabla.getString("fecha_acepta_presupuesto_orden"));
                Registro.setValor_presupuesto_orden(tabla.getDouble("valor_presupuesto_orden"));
                Registro.setAbono_presupuesto_orden(tabla.getDouble("abono_presupuesto_orden"));
                Registro.setTecnico_encargado_reparacion_orden(tabla.getString("tecnico_encargado_reparacion_orden"));
                Registro.setFecha_programada_entrega_orden(tabla.getString("fecha_programada_entrega_orden"));
                Registro.setFecha_notifica_cliente_orden(tabla.getString("fecha_notifica_cliente_orden"));
                Registro.setFecha_entrega_orden(tabla.getString("fecha_entrega_orden"));
                Registro.setId_factura_emitida(tabla.getInt("id_factura_emitida"));
                Registro.setEstado_orden(tabla.getString("estado_orden"));
                Registro.setCase_orden(tabla.getString("case_orden"));
                Registro.setCargador_orden(tabla.getString("cargador_orden"));
                Registro.setBateria_orden(tabla.getString("bateria_orden"));
                Registro.setRam1_orden(tabla.getString("ram1_orden"));
                Registro.setRam2_orden(tabla.getString("ram2_orden"));
                Registro.setRam3_orden(tabla.getString("ram3_orden"));
                Registro.setRam4_orden(tabla.getString("ram4_orden"));
                Registro.setFuente_orden(tabla.getString("fuente_orden"));
                Registro.setOtros_orden(tabla.getString("otros_orden"));
                Registro.setTotal_orden(tabla.getDouble("total_orden"));
                Registro.setTarjeta_orden(tabla.getString("tarjeta_orden"));
                Registro.setDisco_orden(tabla.getString("disco_orden"));
                Registro.setTipo_producto_orden(tabla.getString("tipo_producto_orden"));
                Registro.setTipo_trabajo_orden(tabla.getString("tipo_trabajo_orden"));
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return Registro;
        
    }
    
    public int buscarIdOrdenCreada(String pSerie, String pMarca, String pModelo, String pUsuario, String pTerminal) {
        
        int valor = 0;
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM m_orden "
                + "WHERE serie_producto_orden = TRIM(UPPER('" + pSerie + "')) "
                + "AND UPPER(marca_producto_orden) = UPPER('" + pMarca + "') "
                + "AND UPPER(modelo_producto_orden) = UPPER('" + pModelo + "') "
                + "AND estado_orden = 'Creada' "
                + "ORDER BY id_orden ASC";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Orden de Servicio", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                valor = tabla.getInt("id_orden");
            }
        } catch (Exception Error) {
            return valor;
        } finally {
            Acceso.desconectar();
        }
        
        return valor;
        
    }
    
    public String modificarEstado(String pIdOrden, String pEstado, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario(); 
        
        String sql = "UPDATE m_orden SET estado_orden = '" + pEstado + "' WHERE id_orden = " + pIdOrden;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String insertarRegistroLlamada(ObjetosOrdenLlamada pLlamada, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into d_orden_llamada VALUES ( "
                + "  NULL "
                + " ," + pLlamada.getId_orden() + " "
                + " ,'" + pLlamada.getMotivo_d_orden_llamada() + "' "
                + " ,'" + pLlamada.getRespuesta_d_orden_llamada() + "' "
                + " ,'" + pLlamada.getUsuario_d_orden_llamada() + "' "
                + " ,NOW() "
                + ")";
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
}

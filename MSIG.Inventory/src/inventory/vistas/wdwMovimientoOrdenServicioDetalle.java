/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import com.toedter.calendar.JDateChooser;
import inventory.acceso.*;
import inventory.librerias.WindowController;
import inventory.objetos.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import objetos.ObjetosCaja;

/**
 *
 * @author DSANTACRUZ
 */
public final class wdwMovimientoOrdenServicioDetalle extends javax.swing.JInternalFrame {
    
    private static AccesoOrden acceso = new AccesoOrden();
    private static AccesoEstadoOrden acceso_estado = new AccesoEstadoOrden();
    public static ObjetosOrden orden = new ObjetosOrden();
    private static AccesoExcepciones mensaje = new AccesoExcepciones();
    private static AccesoDetalleOrden acceso_detalle = new AccesoDetalleOrden();
    private static ObjetosDetalleOrden detalle = new ObjetosDetalleOrden();
    private static AccesoSerie acceso_serie = new AccesoSerie();
    private static DefaultTableModel modelo = new DefaultTableModel();
    private static WindowController ventana = new WindowController();
    private static boolean recibe_equipo;
    private static String Log = "";

    /**
     * Creates new form wdwMovimientoOrdenServicioDetalle
     */
    public wdwMovimientoOrdenServicioDetalle() {
        initComponents();
        
        obtenerMarcas();
        obtenerFallas();
        cargaEmpleados();
        limpiarCampos();
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        
        txtUsuarioCreacion.setText(Inventory.lblUsuario.getText().replaceAll("'", "´"));
        txtSerieProducto.requestFocus();
        
        restablecerPanelEstados();
    }
    
    private String fechaActual() {
        Calendar c = Calendar.getInstance();
        String dia = Integer.toString(c.get(Calendar.DATE));
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        String anio = Integer.toString(c.get(Calendar.YEAR));
        String hora = Integer.toString(c.get(Calendar.HOUR));
        String minutos = Integer.toString(c.get(Calendar.MINUTE));
        String segundos = Integer.toString(c.get(Calendar.SECOND));
        String tipo = Integer.toString(c.get(Calendar.AM_PM));
        String h = hora.equals("0") ? "12" : hora;
        String fecha = anio + "-" + mes + "-" + dia + " " + h + ":" + minutos + ":" + segundos;
        return fecha;
    }
    
    public static void agregarLog(String texto) {
        wdwMovimientoOrdenServicioDetalle.Log = wdwMovimientoOrdenServicioDetalle.Log + "ORDEN SERVICIO" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }
    
    public void modificar() {
        
        if (txtIdOrden.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("informacion", "Debe ingresar el Id de la Orden que desea modificar.", "Boton Modificar");
        } else {
            crearObjeto();
            mensaje.manipulacionExcepciones("informacion", acceso.actualizarOrden(orden, Inventory.lblRol.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()), "Modificiar Orden");
            limpiarCampos();
        }
        
        limpiarTablaDetalle();
        buscarPendientes();
        buscarEnEspera();
        restablecerPanelEstados();
        
    }
 
    public void restablecerPanelEstados() {
        btnIniciarDiagnostico.setEnabled(false);
        txtNotificarCliente.setEnabled(false);
        btnClienteAceptaPresupuesto.setEnabled(false);
        btnClienteNoAceptaPresupuesto.setEnabled(false);
        btnClienteAceptaCambioDeEquipo.setEnabled(false);
        btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
        btnOrdenServicioEnProceso.setEnabled(false);
        btnOrdenFinalizada.setEnabled(false);
        btnNotificaFinalizacion.setEnabled(false);
        btnEntregaClienteProducto.setEnabled(false);
        btnEquipoEnEspera.setEnabled(false);
        
        btnBuscarRepuesto.setEnabled(true);
        btnGuardarDetalleProducto.setEnabled(true);
        btnEliminarDetalleProducto.setEnabled(true);
        agregarLog("Restablecer los estado de la Orden.");
    }
    
    public void habilitarEstados() {
        
        if(txtEstadoOrden.getText().equals("Creada")) {
            btnIniciarDiagnostico.setEnabled(true);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Creada.");
        }
        
        if(txtEstadoOrden.getText().equals("Diagnostico")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(true);
            btnClienteAceptaPresupuesto.setEnabled(true);
            btnClienteNoAceptaPresupuesto.setEnabled(true);
            btnClienteAceptaCambioDeEquipo.setEnabled(true);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(true);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(true);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Diagnostico.");
        }
        
        if(txtEstadoOrden.getText().equals("No Contesta")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(true);
            btnClienteAceptaPresupuesto.setEnabled(true);
            btnClienteNoAceptaPresupuesto.setEnabled(true);
            btnClienteAceptaCambioDeEquipo.setEnabled(true);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(true);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(true);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            
            agregarLog("Habilitando estados para orden No Contesta.");
        }
        
        if(txtEstadoOrden.getText().equals("Acepta Reparacion")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(true);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(true);
            btnGuardarDetalleProducto.setEnabled(true);
            btnEliminarDetalleProducto.setEnabled(true);
            
            agregarLog("Habilitando estados para orden Acepta Reparacion.");
        }
        
        if(txtEstadoOrden.getText().equals("No Acepta Reparacion")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(true);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            txtTotalRepuestos.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Entregado.");
        }
        
        if(txtEstadoOrden.getText().equals("Remplazo de Equipo")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(true);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Remplazo de Equipo.");
        }
        
        if(txtEstadoOrden.getText().equals("Rembolso de Efectivo")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(true);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Rembolso de Efectivo.");
        }
        
        if(txtEstadoOrden.getText().equals("En Proceso")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(true);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(true);
            btnGuardarDetalleProducto.setEnabled(true);
            btnEliminarDetalleProducto.setEnabled(true);
            
            agregarLog("Habilitando estados para orden En Proceso.");
        }
        
        if(txtEstadoOrden.getText().equals("Finaliza Reparacion")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(true);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(true);
            btnGuardarDetalleProducto.setEnabled(true);
            btnEliminarDetalleProducto.setEnabled(true);
            
            agregarLog("Habilitando estados para orden Finaliza Reparacion.");
        }
        
        if(txtEstadoOrden.getText().equals("Notifica Finalizacion")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(true);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(true);
            btnGuardarDetalleProducto.setEnabled(true);
            btnEliminarDetalleProducto.setEnabled(true);
            
            agregarLog("Habilitando estados para orden Notifica Finalizacion.");
        }
        
        if(txtEstadoOrden.getText().equals("Entregado")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(false);
            btnClienteNoAceptaPresupuesto.setEnabled(false);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            txtTotalRepuestos.setEnabled(false);
            
            agregarLog("Habilitando estados para orden Entregado.");
        }
        
        if(txtEstadoOrden.getText().equals("En Espera")) {
            btnIniciarDiagnostico.setEnabled(false);
            txtNotificarCliente.setEnabled(false);
            btnClienteAceptaPresupuesto.setEnabled(true);
            btnClienteNoAceptaPresupuesto.setEnabled(true);
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            btnOrdenServicioEnProceso.setEnabled(false);
            btnOrdenFinalizada.setEnabled(false);
            btnNotificaFinalizacion.setEnabled(false);
            btnEntregaClienteProducto.setEnabled(false);
            btnEquipoEnEspera.setEnabled(false);
            
            btnBuscarRepuesto.setEnabled(false);
            btnGuardarDetalleProducto.setEnabled(false);
            btnEliminarDetalleProducto.setEnabled(false);
            txtTotalRepuestos.setEnabled(false);
            
            agregarLog("Habilitando estados para orden En Espera.");
        }
        
    }
    
    
    public boolean validarIngresoDeSeries() {
        
        boolean resultado = false;
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("Descktop")) {
            //Si es una PC de escritorio deber tener ingresada estas series
            if(txtCase.getText().isEmpty() || txtRamUno.getText().isEmpty() || txtDisco.getText().isEmpty()) {
                resultado = false;
                mensaje.manipulacionExcepciones("critico", "Para este equipo usted debe ingresar: \n\n1) Case \n2) RAM \n3) Disco", "Validacion Tipo de Equipo");
            } else {
                resultado = true;
            }
        }
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("Laptop")) {
            if(txtCase.getText().isEmpty() || txtBateria.getText().isEmpty()) {
                resultado = false;
                mensaje.manipulacionExcepciones("critico", "Para este equipo usted debe ingresar: \n\n1) Case \n2) Bateria", "Validacion Tipo de Equipo");
            } else {
                resultado = true;
            }
        }
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("Impresora")) {
            resultado = true;
        }
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("UPS")) {
            resultado = true;
        }
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("Bateria")) {
            resultado = true;
        }
        
        if(cbxTipoEquipo.getSelectedItem().toString().equals("Otros")) {
            resultado = true;
        }
        
        return resultado;
    }
    
    public static void guardarDatos() {
        
        if (recibe_equipo == true) {

            try {
                
                //Guarada el objeto en la base de datos con el estado "Creada"
                crearObjeto();
                acceso.crearOrden(orden, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

                //Consultar el ultimo registro guardado segun los datos de la orden
                int valor_optenido_id = acceso.buscarIdOrdenCreada(orden.getSerie_producto_orden().trim().toUpperCase().replaceAll("'", "´"),
                        orden.getMarca_producto_orden(), orden.getModelo_producto_orden(),
                        Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                txtIdOrden.setText(String.valueOf(valor_optenido_id));
                agregarLog("ID Obtenido: " + valor_optenido_id);
                
                //Actualizar la serie para indicar que ahora esta en un servicio, pero antes buscar si la serie
                //esta registrada en el sistema. De lo contrario no actualizará el nuevo estado de la misma.
                ObjetosSerie buscar_serie = acceso_serie.buscarSerie(txtSerieProducto.getText().replaceAll("'", "´"), txtIdProducto.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                if (buscar_serie.getId_serie() > 0) {
                    acceso_serie.actualizarOrdenServicio(orden.getSerie_producto_orden(), txtIdOrden.getText(), "Servicio", "Orden de Servicio", "Guardar", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                }

                //Guardar el nuevo estado del detalle de la orden
                if (Integer.parseInt(txtIdOrden.getText()) > 0 || !txtIdOrden.getText().isEmpty()) {
                    guardarNuevoEstado("Ingreso de la Orden de Servicio a Taller", "Creada");
                }

                //El boton se deshabilita solo si guarda correctamente
                if(Integer.parseInt(txtIdOrden.getText())==0) {
                    btnGuardar.setEnabled(true);
                } else {
                    btnGuardar.setEnabled(false);
                }
                
            } catch (Exception Error) {
                mensaje.manipulacionExcepciones("critico", Error.getMessage(), "Guardar Orden");
            }

        } else {
            mensaje.manipulacionExcepciones("critico", "Ya no puede guardar este equipo.", "Guardar Equipo");
        }
        
    }
    
    public void buscarPendientes() {
        int cantidad = Integer.parseInt(acceso.equiposPendientes(Inventory.lblNombre.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()));
        lblPendientes.setText(String.valueOf(cantidad));
        if (cantidad >= 10) {
            mensaje.manipulacionExcepciones("critico", "Llego al limite de Equipos por día.", "Equipos por día");
            recibe_equipo = false;
        } else {
            recibe_equipo = true;
        }
        agregarLog("Recibe el Equipo: " + recibe_equipo + ".");
    }
    
    public void buscarEnEspera() {
        int cantidad = Integer.parseInt(acceso.equiposEnEspera(Inventory.lblNombre.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()));
        lblEnEspera.setText(String.valueOf(cantidad));
    }
    
    public void buscarGarantia() {
        int cantidad = Integer.parseInt(acceso.equiposGarantia(Inventory.lblNombre.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()));
        lblGarantia.setText(String.valueOf(cantidad));
    }
    
    public Date CovertirDate(String valor) {
        Date fecha = null;
        try {
            if (valor == null || valor.equals("")) {
                fecha = null;
            } else {
                fecha = new SimpleDateFormat("yyyy-MM-dd").parse(valor);
            }
        } catch (Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Error al convertir fecha. \n" + Error.getMessage(), "Convertir Fecha");
        }
        return fecha;
    }
    
    private void obtenerMarcas() {
        AccesoMarca acceso_marca = new AccesoMarca();
        Iterator iterador_marca = acceso_marca.listarMarcas(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()).iterator();
        while(iterador_marca.hasNext()) {
            ObjetosMarca marca = (ObjetosMarca) iterador_marca.next();
            cbxMarca.addItem(marca.getMarca_producto());
        }
    }
    
    private void obtenerFallas() {
        AccesoFalla acceso_falla = new AccesoFalla();
        Iterator iterador_fallas = acceso_falla.listarFallas(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()).iterator();
        while(iterador_fallas.hasNext()) {
            ObjetosFalla falla = (ObjetosFalla) iterador_fallas.next();
            cbxDefectoReportado.addItem(falla.getNombre_falla());
        }
    }
    
    private void limpiarTablaDetalle() {
        try {
            modelo = (DefaultTableModel) tblDetalleProductos.getModel();
            for (int x=0; x < modelo.getRowCount(); x++) {
                modelo.removeRow(x);
            }
        } catch (Exception Error) {
            System.out.println("Error al limpira tabla de Componentes. \n" + Error.toString());
        }
        modelo.setRowCount(20);
        tblDetalleProductos.setModel(modelo);
    }
    
    public void limpiarCampos() {
        
        txtIdOrden.setText("");
        txtIdCliente.setText("0");
        txtIdSerie.setText("0");
        txtNombre.setText("");
        txtTelefono.setText(""); 
        txtFechaCreacion.setText("");
        txtUsuarioCreacion.setText("");
        txtSerieProducto.setText("");
        //cbxMarca.setSelectedIndex(0);
        txtModelo.setText("");
        txtCaracteristicas.setText("");
        //cbxDefectoReportado.setSelectedIndex(0);
        txtObservacionesCliente.setText("");
        txtDiagonosticoTecnico.setText("");
        cbxAplicaGarantia.setSelectedItem("NO"); 
        txtSerieEntregaGarantia.setText("");
        cbxAceptaPresupuesto.setSelectedItem("NO");
        txtFechaAceptaPresupuesto.setText("");
        txtValorPresupuesto.setText("0.0");
        txtAbono.setText("0.0");
        txtTotalRepuestos.setText("0.0");
        cbxTecnicoEncargado.setSelectedItem(Inventory.lblNombre.getText());        
        txtFechaNotificaDiagnostico.setText("");
        txtFechaEntrega.setText("");
        txtIdFactura.setText("0");
        txtFechaEstimadaEntrega.setDate(null);
        txtEstadoOrden.setText("");
        
        txtCase.setText("");
        txtRamUno.setText("");
        txtRamDos.setText("");
        txtCargador.setText("");
        txtRamTres.setText("");
        txtRamCuatro.setText("");
        txtBateria.setText("");
        txtFuente.setText("");
        txtOtros.setText("");
        
        txtIdProducto.setText("");
        txtDescripcionProductoBuscar.setText("");
        
        txtTotalRepuestos.setText("0.00");
        txtTarjeta.setText("");
        txtDisco.setText("");
        
        //datos nuevos para llamadas
        txtMotivoLlamada.setText("");
        txtRespuestaLlamada.setText("");
        
    }
    
    public void llenarDatosOrden(ObjetosOrden Orden) {
        
        //Validadr el numero de orden, si viene un cero es orden nueva
        String sId_Orden = Orden.getId_orden() == 0 ? "" : String.valueOf(Orden.getId_orden());
        
        txtIdOrden.setText(sId_Orden);
        txtIdCliente.setText(String.valueOf(Orden.getId_cliente()));
        txtNombre.setText(Orden.getNombre_cliente_orden());
        txtTelefono.setText(String.valueOf(Orden.getTelefono_cliente_orden())); 
        txtFechaCreacion.setText(Orden.getFecha_creacion_orden());
        txtUsuarioCreacion.setText(Orden.getUsuario_creacion_orden());
        txtIdSerie.setText(String.valueOf(Orden.getId_serie()));
        txtSerieProducto.setText(Orden.getSerie_producto_orden());
        cbxMarca.setSelectedItem(Orden.getMarca_producto_orden());
        txtModelo.setText(Orden.getModelo_producto_orden());
        txtCaracteristicas.setText(Orden.getCaracteristicas_producto_orden());
      
        try {
            if (Orden.getDefecto_reportado_orden().equals("")) {
                cbxDefectoReportado.setSelectedIndex(0);
            } else {
                cbxDefectoReportado.setSelectedItem(Orden.getDefecto_reportado_orden());
            }
        } catch (Exception Error) {
           cbxDefectoReportado.setSelectedIndex(0);
        }

        txtObservacionesCliente.setText(Orden.getObservaciones_cliente_orden());
        txtDiagonosticoTecnico.setText(Orden.getDiagnostico_orden());
        cbxAplicaGarantia.setSelectedItem(Orden.getAplica_garantia_orden()); 
        txtSerieEntregaGarantia.setText(Orden.getSerie_entregada_garantia_orden());
        cbxAceptaPresupuesto.setSelectedItem(Orden.getCliente_acepta_presupuesto_orden());
        txtFechaAceptaPresupuesto.setText(Orden.getFecha_acepta_presupuesto_orden());
        txtValorPresupuesto.setText(String.valueOf(Orden.getValor_presupuesto_orden()));
        txtAbono.setText(String.valueOf(Orden.getAbono_presupuesto_orden()));
        cbxTecnicoEncargado.setSelectedItem(Orden.getTecnico_encargado_reparacion_orden());        
        txtFechaNotificaDiagnostico.setText(Orden.getFecha_notifica_cliente_orden());
        txtFechaEntrega.setText(Orden.getFecha_entrega_orden());
        txtEstadoOrden.setText(orden.getEstado_orden());
        txtIdFactura.setText(String.valueOf(Orden.getId_factura_emitida()));
        
        if (orden.getFecha_programada_entrega_orden() == null) {
            txtFechaEstimadaEntrega.setDate(null);
        } else {
            if (orden.getFecha_programada_entrega_orden().length() > 4) {
                txtFechaEstimadaEntrega.setDate(CovertirDate(orden.getFecha_programada_entrega_orden()));
            } else {
                txtFechaEstimadaEntrega.setDate(null);
            }
        }
        
        //Llenar el codigo y descripción del producto
        ObjetosProducto producto_buscado = new ObjetosProducto();
        AccesoProducto  acceso_producto = new AccesoProducto();
        
        try {
            agregarLog("Consultando la descripción del Producto.");
            String valor_a_buscar = txtSerieProducto.getText().trim().replaceAll("'", "´").toUpperCase();
            agregarLog("Buscar : " + valor_a_buscar);
            producto_buscado = acceso_producto.buscarProductoPorSerie(valor_a_buscar, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            agregarLog("ID Producto " + producto_buscado.getId_producto());
            txtIdProducto.setText(producto_buscado.getId_producto());
            txtDescripcionProductoBuscar.setText(producto_buscado.getDesc_producto());
        } catch(Exception error) {
            mensaje.manipulacionExcepciones("informacion", "No se encontro la descripción del producto.", "Busqueda Descripcion Producto");
        }
                
        //Validar si el producto tiene garantia
        agregarLog("Aplica Garantía: " + Orden.getAplica_garantia_orden());
        try {
            if (Orden.getAplica_garantia_orden().equals("NO")) {
                mensaje.manipulacionExcepciones("informacion", "Este producto ya no tiene garantia", "Busqueda de Producto por Serie");
                btnClienteAceptaCambioDeEquipo.setEnabled(false);
                btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
                txtSerieEntregaGarantia.setEnabled(false);
            }
        } catch (Exception Error) {
            mensaje.manipulacionExcepciones("informacion", "Este producto ya no tiene garantia", "Busqueda de Producto por Serie");
            btnClienteAceptaCambioDeEquipo.setEnabled(false);
            btnClienteSolicitaDevolucionDeEfectivo.setEnabled(false);
            txtSerieEntregaGarantia.setEnabled(false);
        }

        //Datos nuevos agregados para las series internas
        txtCase.setText("null".equals(orden.getCase_orden()) ? "" : orden.getCase_orden());
        txtCargador.setText("null".equals(orden.getCargador_orden()) ? "" : orden.getCargador_orden());
        txtBateria.setText("null".equals(orden.getBateria_orden()) ? "" : orden.getBateria_orden());
        txtRamUno.setText("null".equals(orden.getRam1_orden()) ? "" : orden.getRam1_orden());
        txtRamDos.setText("null".equals(orden.getRam2_orden()) ? "" : orden.getRam2_orden());
        txtRamTres.setText("null".equals(orden.getRam3_orden()) ? "" : orden.getRam3_orden());
        txtRamCuatro.setText("null".equals(orden.getRam4_orden()) ? "" : orden.getRam4_orden());
        txtFuente.setText("null".equals(orden.getFuente_orden()) ? "" : orden.getFuente_orden());
        txtOtros.setText("null".equals(orden.getOtros_orden()) ? "" : orden.getOtros_orden());
        
        txtTotalRepuestos.setText(String.valueOf(orden.getTotal_orden()));
        
        txtTarjeta.setText("null".equals(orden.getTarjeta_orden()) ? "" : orden.getTarjeta_orden());
        txtDisco.setText("null".equals(orden.getDisco_orden()) ? "" : orden.getDisco_orden());

        try {
            if (orden.getTipo_producto_orden().isEmpty()) {
                cbxTipoEquipo.setSelectedItem(orden.getTipo_producto_orden());
            } else {
                cbxTipoEquipo.setSelectedIndex(0);
            }
        } catch (Exception Error) {
            cbxTipoEquipo.setSelectedIndex(0);
        }
        
        try {
            if (Orden.getTipo_trabajo_orden().isEmpty()) {
                cbxTipoTrabajo.setSelectedItem(Orden.getTipo_trabajo_orden());
            } else {
                cbxTipoTrabajo.setSelectedIndex(0);
            }
        } catch (Exception Error) {
            cbxTipoTrabajo.setSelectedIndex(0);
        }
        
    }
    
    public String obtenerFechaActual() {
        
        String Hora = "";
        String Fecha = "";
        Date Date = new Date();
        
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Hora = hourFormat.format(Date);
        Fecha = dateFormat.format(Date);
        
        return Fecha + " " + Hora;
        
    }
    
    private void cargaEmpleados(){
        
        ArrayList<ObjetosEmpleado> lista_empleados = new ArrayList();
        AccesoEmpleado acceso_empleados = new AccesoEmpleado();
            
        try{
            lista_empleados = acceso_empleados.listarEmpleadosActivos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Nombre Empleado");
        }
        
        //Lleno el combobox con los registros de empleados que existen
        for(ObjetosEmpleado empleados : lista_empleados){
            cbxTecnicoEncargado.addItem(empleados.getNombre_empleado());
        }
        
    }
    
    public static void crearObjeto() {
        
        if(txtIdOrden.getText().isEmpty()) {
            orden.setId_orden(0); 
        } else {
           orden.setId_orden(Integer.parseInt(txtIdOrden.getText())); 

        }
        
        orden.setId_cliente(Integer.parseInt(txtIdCliente.getText()));
        orden.setNombre_cliente_orden(txtNombre.getText().toUpperCase().replaceAll("'", "´"));
        orden.setTelefono_cliente_orden(Integer.parseInt(txtTelefono.getText())); 
        orden.setFecha_creacion_orden(txtFechaCreacion.getText());
        orden.setUsuario_creacion_orden(txtUsuarioCreacion.getText());
        orden.setId_serie(Integer.parseInt(txtIdSerie.getText()));
        orden.setSerie_producto_orden(txtSerieProducto.getText().toUpperCase().replaceAll("'", "´"));
        orden.setMarca_producto_orden(cbxMarca.getSelectedItem().toString().toUpperCase().replaceAll("'", "´"));
        
        if(txtModelo.getText().isEmpty()) {
            orden.setModelo_producto_orden("0");
        } else {
            orden.setModelo_producto_orden(txtModelo.getText().toUpperCase().replaceAll("'", "´"));
        }
        
        orden.setCaracteristicas_producto_orden(txtCaracteristicas.getText().toUpperCase().replaceAll("'", "´"));
        orden.setDefecto_reportado_orden(cbxDefectoReportado.getSelectedItem().toString().toUpperCase().replaceAll("'", "´"));
        orden.setObservaciones_cliente_orden(txtObservacionesCliente.getText().toUpperCase().replaceAll("'", "´"));
        orden.setDiagnostico_orden(txtDiagonosticoTecnico.getText().toUpperCase().replaceAll("'", "´"));
        orden.setAplica_garantia_orden(cbxAplicaGarantia.getSelectedItem().toString()); 
        
        if(txtSerieEntregaGarantia.getText().isEmpty()) {
            orden.setSerie_entregada_garantia_orden("0");
        } else {
            orden.setSerie_entregada_garantia_orden(txtSerieEntregaGarantia.getText().toString().toUpperCase());  
        }
        
        orden.setCliente_acepta_presupuesto_orden(cbxAceptaPresupuesto.getSelectedItem().toString());
        
        if(txtFechaAceptaPresupuesto.getText().isEmpty()) {
            orden.setFecha_acepta_presupuesto_orden(null);
        } else {
            orden.setFecha_acepta_presupuesto_orden(txtFechaAceptaPresupuesto.getText());
        }
        
        orden.setValor_presupuesto_orden(Double.parseDouble(txtValorPresupuesto.getText()));
        orden.setAbono_presupuesto_orden(Double.parseDouble(txtAbono.getText()));
        orden.setTecnico_encargado_reparacion_orden(cbxTecnicoEncargado.getSelectedItem().toString());
        
        if(txtFechaEstimadaEntrega.getDate() == null) {
           orden.setFecha_programada_entrega_orden(null);
        } else {
            orden.setFecha_programada_entrega_orden(ConvertirFecha(txtFechaEstimadaEntrega)); 
        }
        
        if(txtFechaNotificaDiagnostico.getText().isEmpty()) {
            orden.setFecha_notifica_cliente_orden(null);
        } else {
            orden.setFecha_notifica_cliente_orden(txtFechaNotificaDiagnostico.getText());
        }
        
        if(txtFechaEntrega.getText().isEmpty()) {
            orden.setFecha_entrega_orden(null);
        } else {
            orden.setFecha_entrega_orden(txtFechaEntrega.getText());
        }
        
        orden.setId_factura_emitida(Integer.parseInt(txtIdFactura.getText()));
        
        //Datos nuevos para las series
        orden.setCase_orden(txtCase.getText().toUpperCase().replaceAll("'", "´"));
        orden.setCargador_orden(txtCargador.getText().toUpperCase().replaceAll("'", "´"));
        orden.setBateria_orden(txtBateria.getText().toUpperCase().replaceAll("'", "´"));
        orden.setRam1_orden(txtRamUno.getText().toUpperCase().replaceAll("'", "´"));
        orden.setRam2_orden(txtRamDos.getText().toUpperCase().replaceAll("'", "´"));
        orden.setRam3_orden(txtRamTres.getText().toUpperCase().replaceAll("'", "´"));
        orden.setRam4_orden(txtRamCuatro.getText().toUpperCase().replaceAll("'", "´"));     
        orden.setFuente_orden(txtFuente.getText().toUpperCase().replaceAll("'", "´"));
        orden.setOtros_orden(txtOtros.getText().toUpperCase().replaceAll("'", "´"));
        
        orden.setEstado_orden(txtEstadoOrden.getText());
        
        orden.setTotal_orden(Double.parseDouble(txtTotalRepuestos.getText()));
        
        orden.setTarjeta_orden(txtTarjeta.getText().toUpperCase().replaceAll("'", "´"));
        orden.setDisco_orden(txtDisco.getText().toUpperCase().replaceAll("'", "´"));
        
        orden.setTipo_producto_orden(cbxTipoEquipo.getSelectedItem().toString());
        orden.setTipo_trabajo_orden(cbxTipoTrabajo.getSelectedItem().toString());
        
    }
    
    public void buscarDetalleGuardado() {
        
        ArrayList<ObjetosDetalleOrden> detalle = acceso_detalle.listarProductos(Integer.parseInt(txtIdOrden.getText()), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        Iterator iterardor = detalle.iterator();
        int pos = 0;
        double total = 0.0;
        
        while(iterardor.hasNext()) {
            
            ObjetosDetalleOrden orden = (ObjetosDetalleOrden) iterardor.next();
            tblDetalleProductos.setValueAt(orden.getId_d_orden_producto(), pos, 0);
            tblDetalleProductos.setValueAt(orden.getId_orden(), pos, 1);
            tblDetalleProductos.setValueAt(orden.getId_producto(), pos, 2);
            tblDetalleProductos.setValueAt(orden.getDescripcion_d_orden_producto(), pos, 3);
            tblDetalleProductos.setValueAt(orden.getCantidad_d_orden_produco(), pos, 4);
            tblDetalleProductos.setValueAt(orden.getPrecio_d_orden_producto(), pos,  5);
            tblDetalleProductos.setValueAt(orden.getSub_total_d_orden_producto(), pos, 6);
            tblDetalleProductos.setValueAt(orden.getEstado_d_orden_producto(), pos, 7);
            pos++;

            total = total + orden.getSub_total_d_orden_producto();
            
        }
        
        txtTotalRepuestos.setText(String.valueOf(total));
        
    }
    
    public static String ConvertirFecha(JDateChooser Calendario) {
        return ( Calendario.getDate().getYear() + 1900 ) + "-" + ( Calendario.getDate().getMonth() + 1 ) + "-" + Calendario.getDate().getDate();
    }
    
    public static void guardarNuevoEstado(String pEstado, String pEstadoGeneral) {
        if (txtIdOrden.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("citico", "Debe de buscar una Orden de Servicio para ejecutar esta accion.", "Iniciar Diagnostico");
        } else {
            ObjetosEstadoOrden Estado = new ObjetosEstadoOrden();
            //Guardar el nuevo estado
            Estado.setId_d_orden_estado(0);
            Estado.setId_orden(Integer.parseInt(txtIdOrden.getText()));
            Estado.setDescripcion_d_orden_estado(pEstado);
            Estado.setUsuario_d_orden_estado(Inventory.lblUsuario.getText());
            agregarLog("Cambiado el estado de la Orden a " + pEstadoGeneral + ".");
            acceso_estado.InsertarEstado(Estado, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //Modificar el estado general de la orden
            acceso.modificarEstado(txtIdOrden.getText(), pEstadoGeneral, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }
    }
    
    public void guardarRegistroLlamada() {
        try {
            ObjetosOrdenLlamada llamada = new ObjetosOrdenLlamada();
            llamada.setId_orden(Integer.parseInt(txtIdOrden.getText()));
            llamada.setMotivo_d_orden_llamada(txtMotivoLlamada.getText());
            llamada.setRespuesta_d_orden_llamada(txtRespuestaLlamada.getText());
            llamada.setUsuario_d_orden_llamada(Inventory.lblUsuario.getText());
            String respuesta = acceso.insertarRegistroLlamada(llamada, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            mensaje.manipulacionExcepciones("informacion", respuesta, "Guardar Registro Llamada");
        } catch(Exception Error) {
            agregarLog(Error.getMessage());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDetalleProductos = new javax.swing.JTable();
        btnGuardarDetalleProducto = new javax.swing.JButton();
        btnEliminarDetalleProducto = new javax.swing.JButton();
        btnBuscarRepuesto = new javax.swing.JButton();
        pnlEstadistica = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        lblGarantia = new javax.swing.JLabel();
        lblPendientes = new javax.swing.JLabel();
        lblEnEspera = new javax.swing.JLabel();
        pnlRegistroLlamadas = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtMotivoLlamada = new javax.swing.JTextField();
        txtRespuestaLlamada = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btnGuardarLLamada = new javax.swing.JButton();
        btnContrasenia1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cbxTipoEquipo = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCaracteristicas = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        cbxDefectoReportado = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtObservacionesCliente = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        cbxTipoTrabajo = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDiagonosticoTecnico = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        btnIniciarDiagnostico = new javax.swing.JButton();
        txtNotificarCliente = new javax.swing.JToggleButton();
        btnClienteAceptaPresupuesto = new javax.swing.JButton();
        btnClienteNoAceptaPresupuesto = new javax.swing.JButton();
        btnClienteAceptaCambioDeEquipo = new javax.swing.JButton();
        btnClienteSolicitaDevolucionDeEfectivo = new javax.swing.JButton();
        btnOrdenServicioEnProceso = new javax.swing.JButton();
        btnOrdenFinalizada = new javax.swing.JButton();
        btnNotificaFinalizacion = new javax.swing.JButton();
        btnEntregaClienteProducto = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        cbxAceptaPresupuesto = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        cbxTecnicoEncargado = new javax.swing.JComboBox();
        txtFechaEstimadaEntrega = new com.toedter.calendar.JDateChooser();
        txtFechaAceptaPresupuesto = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtValorPresupuesto = new javax.swing.JTextField();
        txtAbono = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtTotalRepuestos = new javax.swing.JTextField();
        pnlBotones1 = new javax.swing.JPanel();
        btnContrasenia = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnImprimirOrdenServicio = new javax.swing.JButton();
        btnEquipoEnEspera = new javax.swing.JButton();
        cbxTipoImpresion = new javax.swing.JComboBox<>();
        pnlDatos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdOrden = new javax.swing.JTextField();
        txtIdCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdSerie = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtIdFactura = new javax.swing.JTextField();
        btnBuscarSerie1 = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        txtFechaCreacion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUsuarioCreacion = new javax.swing.JTextField();
        pnlProducto = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtSerieProducto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        cbxMarca = new javax.swing.JComboBox();
        btnBuscarSerie = new javax.swing.JButton();
        txtBuscarProducto = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtDescripcionProductoBuscar = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        cbxAplicaGarantia = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        txtSerieEntregaGarantia = new javax.swing.JTextField();
        txtFechaEntrega = new javax.swing.JTextField();
        txtEstadoOrden = new javax.swing.JTextField();
        txtFechaNotificaDiagnostico = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        pnlSeriesInternas = new javax.swing.JPanel();
        lblRam = new javax.swing.JLabel();
        txtRamUno = new javax.swing.JTextField();
        txtRamDos = new javax.swing.JTextField();
        txtRam1 = new javax.swing.JLabel();
        txtRam2 = new javax.swing.JLabel();
        txtRam3 = new javax.swing.JLabel();
        txtRamTres = new javax.swing.JTextField();
        txtRamCuatro = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txtTarjeta = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtCargador = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtBateria = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtFuente = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        txtOtros = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtCase = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtDisco = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Orden de Servicio");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovmientoOrdenDeServicio.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1220, 780));
        setMinimumSize(new java.awt.Dimension(1220, 780));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1220, 780));

        pnlDetalle.setBackground(new java.awt.Color(255, 204, 153));
        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblDetalleProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Detalle", "ID Orden", "ID Producto", "Descripcion", "Cantidad", "Precio", "Total", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblDetalleProductos);

        btnGuardarDetalleProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardarDetalleProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarDetalleProductoActionPerformed(evt);
            }
        });

        btnEliminarDetalleProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminarDetalleProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarDetalleProductoActionPerformed(evt);
            }
        });

        btnBuscarRepuesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarRepuesto.setActionCommand("buscarProducto");
        btnBuscarRepuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarRepuestoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardarDetalleProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarDetalleProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscarRepuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetalleLayout.createSequentialGroup()
                        .addComponent(btnBuscarRepuesto)
                        .addGap(4, 4, 4)
                        .addComponent(btnGuardarDetalleProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarDetalleProducto)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlDetalleLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pnlEstadistica.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setBackground(new java.awt.Color(255, 0, 0));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("PENDIENTES");

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("EN ESPERA");

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("EN GARANTIA");

        lblGarantia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGarantia.setForeground(new java.awt.Color(0, 153, 51));
        lblGarantia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGarantia.setText("00");

        lblPendientes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPendientes.setForeground(new java.awt.Color(255, 0, 0));
        lblPendientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPendientes.setText("00");

        lblEnEspera.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEnEspera.setForeground(new java.awt.Color(255, 153, 0));
        lblEnEspera.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEnEspera.setText("00");

        javax.swing.GroupLayout pnlEstadisticaLayout = new javax.swing.GroupLayout(pnlEstadistica);
        pnlEstadistica.setLayout(pnlEstadisticaLayout);
        pnlEstadisticaLayout.setHorizontalGroup(
            pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadisticaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEstadisticaLayout.createSequentialGroup()
                        .addComponent(lblEnEspera, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlEstadisticaLayout.createSequentialGroup()
                        .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlEstadisticaLayout.setVerticalGroup(
            pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadisticaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEnEspera))
                .addGap(3, 3, 3)
                .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lblPendientes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEstadisticaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lblGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlRegistroLlamadas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setText("Motivo");

        txtMotivoLlamada.setBackground(new java.awt.Color(255, 255, 204));

        txtRespuestaLlamada.setBackground(new java.awt.Color(255, 255, 204));

        jLabel39.setText("Respuesta");

        btnGuardarLLamada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/btnBotonRegistrarLlamada.png"))); // NOI18N
        btnGuardarLLamada.setText("Registrar");
        btnGuardarLLamada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarLLamadaActionPerformed(evt);
            }
        });

        btnContrasenia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnContrasenia1.setToolTipText("Imprimir Contraseña de Entrega");
        btnContrasenia1.setActionCommand("imprimirEnvio");
        btnContrasenia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContrasenia1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRegistroLlamadasLayout = new javax.swing.GroupLayout(pnlRegistroLlamadas);
        pnlRegistroLlamadas.setLayout(pnlRegistroLlamadasLayout);
        pnlRegistroLlamadasLayout.setHorizontalGroup(
            pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegistroLlamadasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegistroLlamadasLayout.createSequentialGroup()
                        .addComponent(txtRespuestaLlamada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardarLLamada))
                    .addGroup(pnlRegistroLlamadasLayout.createSequentialGroup()
                        .addComponent(txtMotivoLlamada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnContrasenia1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlRegistroLlamadasLayout.setVerticalGroup(
            pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroLlamadasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtMotivoLlamada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnContrasenia1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRegistroLlamadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txtRespuestaLlamada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardarLLamada))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbxTipoEquipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Descktop", "Laptop", "Impresora", "UPS", "Bateria", "Otros" }));

        jLabel12.setText("Carateristicas del Producto");

        txtCaracteristicas.setColumns(20);
        txtCaracteristicas.setRows(5);
        jScrollPane1.setViewportView(txtCaracteristicas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxTipoEquipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbxTipoEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setText("Observaciones Cliente");

        txtObservacionesCliente.setColumns(20);
        txtObservacionesCliente.setRows(5);
        jScrollPane5.setViewportView(txtObservacionesCliente);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxDefectoReportado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbxDefectoReportado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbxTipoTrabajo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hardware", "Software", "Equipo Nuevo" }));

        jLabel15.setText("Diagnostico Tecnico");

        txtDiagonosticoTecnico.setColumns(20);
        txtDiagonosticoTecnico.setRows(5);
        jScrollPane3.setViewportView(txtDiagonosticoTecnico);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxTipoTrabajo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbxTipoTrabajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnIniciarDiagnostico.setText("Iniciar Diagnostico");
        btnIniciarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarDiagnosticoActionPerformed(evt);
            }
        });

        txtNotificarCliente.setText("Cliente No Contesta");
        txtNotificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotificarClienteActionPerformed(evt);
            }
        });

        btnClienteAceptaPresupuesto.setText("Cliente Acepta Presupuesto Reparacion");
        btnClienteAceptaPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteAceptaPresupuestoActionPerformed(evt);
            }
        });

        btnClienteNoAceptaPresupuesto.setText("Cliente No Acepta Presupuesto Reparacion");
        btnClienteNoAceptaPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteNoAceptaPresupuestoActionPerformed(evt);
            }
        });

        btnClienteAceptaCambioDeEquipo.setText("Cliente Acepta Cambio de Equipo");
        btnClienteAceptaCambioDeEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteAceptaCambioDeEquipoActionPerformed(evt);
            }
        });

        btnClienteSolicitaDevolucionDeEfectivo.setText("Cliente Solicita Devolución de Efectivo");
        btnClienteSolicitaDevolucionDeEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteSolicitaDevolucionDeEfectivoActionPerformed(evt);
            }
        });

        btnOrdenServicioEnProceso.setText("Orden de Servicio en Proceso");
        btnOrdenServicioEnProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdenServicioEnProcesoActionPerformed(evt);
            }
        });

        btnOrdenFinalizada.setText("Orden Finalizada");
        btnOrdenFinalizada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdenFinalizadaActionPerformed(evt);
            }
        });

        btnNotificaFinalizacion.setText("Notifica a Cliente de la Finalizacion");
        btnNotificaFinalizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificaFinalizacionActionPerformed(evt);
            }
        });

        btnEntregaClienteProducto.setText("Equipo Entregado a Cliente");
        btnEntregaClienteProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregaClienteProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIniciarDiagnostico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNotificarCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClienteAceptaPresupuesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClienteNoAceptaPresupuesto, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(btnClienteAceptaCambioDeEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClienteSolicitaDevolucionDeEfectivo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOrdenServicioEnProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOrdenFinalizada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNotificaFinalizacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEntregaClienteProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIniciarDiagnostico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNotificarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClienteAceptaPresupuesto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClienteNoAceptaPresupuesto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClienteAceptaCambioDeEquipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClienteSolicitaDevolucionDeEfectivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOrdenServicioEnProceso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOrdenFinalizada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNotificaFinalizacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntregaClienteProducto)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbxAceptaPresupuesto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SI", "NO" }));

        jLabel18.setText("Cliente Acepta Presupuesto");

        txtFechaAceptaPresupuesto.setEditable(false);
        txtFechaAceptaPresupuesto.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaAceptaPresupuesto.setForeground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Empleado Encargado");

        jLabel21.setText("Fecha Estimada Entrega");

        jLabel22.setText("Fecha Aceptación");

        txtValorPresupuesto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtAbono.setBackground(new java.awt.Color(255, 255, 204));
        txtAbono.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel28.setText("Valor Presupuesto");

        txtTotalRepuestos.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalRepuestos.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalRepuestos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxTecnicoEncargado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaEstimadaEntrega, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaAceptaPresupuesto)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtValorPresupuesto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbxAceptaPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalRepuestos)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAceptaPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtTotalRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTecnicoEncargado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtFechaEstimadaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaAceptaPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAbono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBotones1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnContrasenia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnContrasenia.setToolTipText("Imprimir Contraseña de Entrega");
        btnContrasenia.setActionCommand("imprimirEnvio");
        btnContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContraseniaActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonNuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnImprimirOrdenServicio.setText("Imprimir Orden de Servicio");
        btnImprimirOrdenServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirOrdenServicioActionPerformed(evt);
            }
        });

        btnEquipoEnEspera.setText("Equipo En Espera");
        btnEquipoEnEspera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEquipoEnEsperaActionPerformed(evt);
            }
        });

        cbxTipoImpresion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Media Carta", "Carta" }));

        javax.swing.GroupLayout pnlBotones1Layout = new javax.swing.GroupLayout(pnlBotones1);
        pnlBotones1.setLayout(pnlBotones1Layout);
        pnlBotones1Layout.setHorizontalGroup(
            pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotones1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimirOrdenServicio, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .addComponent(btnEquipoEnEspera, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlBotones1Layout.createSequentialGroup()
                        .addComponent(btnContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbxTipoImpresion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlBotones1Layout.setVerticalGroup(
            pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotones1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnContrasenia)
                    .addGroup(pnlBotones1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnModificar)
                            .addComponent(btnEquipoEnEspera))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(cbxTipoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimirOrdenServicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDatos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("ID Orden");

        txtIdOrden.setBackground(new java.awt.Color(255, 255, 204));
        txtIdOrden.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        txtIdCliente.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("ID Cliente");

        jLabel3.setText("ID Serie");

        txtIdSerie.setBackground(new java.awt.Color(255, 255, 204));

        jLabel4.setText("ID Factura");

        txtIdFactura.setBackground(new java.awt.Color(255, 255, 204));

        btnBuscarSerie1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarSerie1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarSerie1ActionPerformed(evt);
            }
        });

        txtNombre.setBackground(new java.awt.Color(255, 255, 204));

        jLabel5.setText("Nombre");

        txtTelefono.setBackground(new java.awt.Color(255, 255, 204));

        txtFechaCreacion.setEditable(false);
        txtFechaCreacion.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaCreacion.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Telefono");

        jLabel7.setText("Fecha");

        jLabel8.setText("Usuario");

        txtUsuarioCreacion.setEditable(false);
        txtUsuarioCreacion.setBackground(new java.awt.Color(102, 102, 102));
        txtUsuarioCreacion.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlDatosLayout.createSequentialGroup()
                                .addComponent(txtIdOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarSerie1))
                            .addComponent(txtTelefono))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdSerie, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(txtIdCliente))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)))
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(25, 25, 25)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIdFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(txtUsuarioCreacion)
                    .addComponent(txtFechaCreacion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarSerie1)
                    .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtIdOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(txtIdSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtUsuarioCreacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(txtFechaCreacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlProducto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlProducto.setMaximumSize(new java.awt.Dimension(317, 32767));
        pnlProducto.setMinimumSize(new java.awt.Dimension(317, 180));

        jLabel9.setText("Numero Serie");

        txtSerieProducto.setBackground(new java.awt.Color(255, 255, 204));

        jLabel10.setText("Marca");

        txtModelo.setBackground(new java.awt.Color(255, 255, 204));

        btnBuscarSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarSerieActionPerformed(evt);
            }
        });

        txtBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });

        txtIdProducto.setEditable(false);
        txtIdProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtIdProducto.setForeground(new java.awt.Color(255, 255, 255));

        jLabel26.setText("ID Producto");

        txtDescripcionProductoBuscar.setEditable(false);
        txtDescripcionProductoBuscar.setBackground(new java.awt.Color(102, 102, 102));
        txtDescripcionProductoBuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtDescripcionProductoBuscar.setMaximumSize(new java.awt.Dimension(6, 20));

        jLabel27.setText("Descripcion");

        jLabel11.setText("Modelo");

        javax.swing.GroupLayout pnlProductoLayout = new javax.swing.GroupLayout(pnlProducto);
        pnlProducto.setLayout(pnlProductoLayout);
        pnlProductoLayout.setHorizontalGroup(
            pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProductoLayout.createSequentialGroup()
                            .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26)
                                .addComponent(jLabel9))
                            .addGap(18, 18, 18))
                        .addGroup(pnlProductoLayout.createSequentialGroup()
                            .addComponent(jLabel27)
                            .addGap(28, 28, 28)))
                    .addGroup(pnlProductoLayout.createSequentialGroup()
                        .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlProductoLayout.createSequentialGroup()
                        .addComponent(txtIdProducto)
                        .addGap(10, 10, 10)
                        .addComponent(txtBuscarProducto))
                    .addGroup(pnlProductoLayout.createSequentialGroup()
                        .addComponent(txtSerieProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarSerie))
                    .addComponent(txtDescripcionProductoBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtModelo))
                .addContainerGap())
        );
        pnlProductoLayout.setVerticalGroup(
            pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarSerie)
                    .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtSerieProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26))
                    .addComponent(txtBuscarProducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(txtDescripcionProductoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbxAplicaGarantia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SI", "NO" }));

        jLabel17.setText("Aplica Garantia");

        txtSerieEntregaGarantia.setBackground(new java.awt.Color(255, 255, 204));

        txtFechaEntrega.setBackground(new java.awt.Color(255, 255, 204));

        txtEstadoOrden.setEditable(false);
        txtEstadoOrden.setBackground(new java.awt.Color(102, 102, 102));
        txtEstadoOrden.setForeground(new java.awt.Color(255, 255, 255));

        txtFechaNotificaDiagnostico.setEditable(false);
        txtFechaNotificaDiagnostico.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaNotificaDiagnostico.setForeground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("Serie Entregada");

        jLabel19.setText("Fecha Entrega");

        jLabel23.setText("Estado Orden");

        jLabel25.setText("Fecha Notifica");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFechaEntrega)
                            .addComponent(txtEstadoOrden)
                            .addComponent(txtFechaNotificaDiagnostico)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(cbxAplicaGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 94, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(29, 29, 29)
                        .addComponent(txtSerieEntregaGarantia)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAplicaGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSerieEntregaGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstadoOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaNotificaDiagnostico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSeriesInternas.setBackground(new java.awt.Color(204, 255, 204));
        pnlSeriesInternas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblRam.setText("RAM 1");

        txtRam1.setText("RAM 2");

        txtRam2.setText("RAM 3");

        txtRam3.setText("RAM 4");

        javax.swing.GroupLayout pnlSeriesInternasLayout = new javax.swing.GroupLayout(pnlSeriesInternas);
        pnlSeriesInternas.setLayout(pnlSeriesInternasLayout);
        pnlSeriesInternasLayout.setHorizontalGroup(
            pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSeriesInternasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSeriesInternasLayout.createSequentialGroup()
                        .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRam)
                            .addComponent(txtRam2)
                            .addComponent(txtRam3))
                        .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSeriesInternasLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(txtRamUno, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSeriesInternasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRamTres, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRamCuatro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSeriesInternasLayout.createSequentialGroup()
                        .addComponent(txtRam1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRamDos, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlSeriesInternasLayout.setVerticalGroup(
            pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSeriesInternasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRamUno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRamDos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRam1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRamTres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRam2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSeriesInternasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRamCuatro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRam3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 255, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel37.setText("Tarjeta");

        jLabel30.setText("Cargador");

        jLabel31.setText("Bateria");

        jLabel32.setText("Fuente");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCargador, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(txtBateria, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(txtFuente, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addGap(27, 27, 27)
                        .addComponent(txtTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCargador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFuente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel33.setText("Otros");

        jLabel29.setText("Case");

        jLabel38.setText("Disco");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel29)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDisco, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOtros, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCase, javax.swing.GroupLayout.DEFAULT_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOtros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDisco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pnlProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlBotones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSeriesInternas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlRegistroLlamadas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlEstadistica, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSeriesInternas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlRegistroLlamadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlEstadistica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlBotones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContraseniaActionPerformed
        
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptContraseniaOrdenServicio.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //Parametros del reporte para los datos de la empresa.
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_ORDEN", Integer.parseInt(txtIdOrden.getText()));
            parametro.put("P_ID_CLIENTE", txtIdCliente.getText());
            parametro.put("P_ID_SERIE", txtIdSerie.getText());
            parametro.put("P_ID_FACTURA", txtIdFactura.getText());
            parametro.put("P_DEFECTO", cbxDefectoReportado.getSelectedItem().toString().toUpperCase());
            
            parametro.put("P_NOMBRE", txtNombre.getText().toUpperCase());
            parametro.put("P_TELEFONO", txtTelefono.getText());
            parametro.put("P_FECHA_CREACION", txtFechaCreacion.getText());
            parametro.put("P_USUARIO_RECIBE", txtUsuarioCreacion.getText());
            
            parametro.put("P_NUMERO_SERIE", txtSerieProducto.getText().replaceAll("'", "´"));
            parametro.put("P_MARCA", cbxMarca.getSelectedItem().toString());
            parametro.put("P_MODELO", txtModelo.getText());
            
            
            parametro.put("P_SALDO_TOTAL", txtValorPresupuesto.getText());
            parametro.put("P_SALDO_INICIAL", txtAbono.getText());
            
            Double valor = Double.parseDouble(txtValorPresupuesto.getText());
            Double abono = Double.parseDouble(txtAbono.getText());
            Double saldo = valor - abono;
            
            parametro.put("P_SALDO_VENTAS", String.valueOf(saldo));
            
            //Datos nuevos para las series internas
            parametro.put("P_CASE", txtCase.getText().replaceAll("'", "´"));
            parametro.put("P_CARGADOR", txtCargador.getText().replaceAll("'", "´"));
            parametro.put("P_BATERIA", txtBateria.getText().replaceAll("'", "´"));
            parametro.put("P_FUENTE", txtFuente.getText().replaceAll("'", "´"));
            parametro.put("P_RAM1", txtRamUno.getText().replaceAll("'", "´"));
            parametro.put("P_RAM2", txtRamDos.getText().replaceAll("'", "´"));
            parametro.put("P_RAM3", txtRamTres.getText().replaceAll("'", "´"));
            parametro.put("P_RAM4", txtRamCuatro.getText().replaceAll("'", "´"));
            parametro.put("P_DISCO", txtDisco.getText().replaceAll("'", "´"));
            parametro.put("P_TARJETA", txtTarjeta.getText().replaceAll("'", "´"));
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, acceso_inventario.conectar());
            JInternalFrame ventana = new JInternalFrame("Inventory 3.0");
            ventana.getContentPane().add(new JRViewer(pantalla));
            ventana.setPreferredSize(new Dimension(900, 500));
            ventana.setClosable(true);
            ventana.setMaximizable(true);
            ventana.setResizable(true);
            ventana.setVisible(true);
            ventana.pack();
            
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x  = (ancho  - ventana.getWidth() ) / 2;
            int y  = ( alto - ventana.getHeight() - 100 ) / 2;
            
            Inventory.pnlPrincipal.add(ventana);
            
            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();
            
            this.toBack();
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Contraseña Orden Servicio");
        }
    }//GEN-LAST:event_btnContraseniaActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if(txtEstadoOrden.getText().isEmpty()) {
            txtEstadoOrden.setText("Creada");
        }
        
        if (cbxTipoTrabajo.getSelectedItem().toString().equals("Equipo Nuevo")) {
            wdwMovimientoAutorizacionOrden autorizacion = new wdwMovimientoAutorizacionOrden();
            int ancho = Inventory.pnlPrincipal.getWidth();
            int alto = Inventory.pnlPrincipal.getHeight();
            int x = (ancho / 2) - (autorizacion.getWidth() / 2);
            int y = ((alto / 2) - (autorizacion.getHeight() / 2));
            autorizacion.setVisible(true);
            Inventory.pnlPrincipal.add(autorizacion);
            autorizacion.toFront();
            autorizacion.setLocation(x, y);
        } else {
            if (validarIngresoDeSeries()) {
                guardarDatos();
            }
        }

        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        
        txtFechaCreacion.setText(fechaActual());
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.dispose();
        wdwMovimientoOrdenServicioDetalle VentaDeProductos = new wdwMovimientoOrdenServicioDetalle();
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x = (ancho / 2) - (VentaDeProductos.getWidth() / 2);
        int y = (alto / 2) - (VentaDeProductos.getHeight() / 2);
        VentaDeProductos.setVisible(true);
        Inventory.pnlPrincipal.add(VentaDeProductos);
        VentaDeProductos.toFront();
        VentaDeProductos.setLocation(x, y);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificar();
        
        //Desbloquear el campo de busqueda
        txtIdOrden.setEditable(true);
        txtIdOrden.setBackground(new Color(255, 255, 204));
        txtIdOrden.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnIniciarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarDiagnosticoActionPerformed
        
        txtEstadoOrden.setText("Diagnostico");
        guardarNuevoEstado("Inicio de Diagnostico", "Diagnostico");

        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        
        modificar();
    }//GEN-LAST:event_btnIniciarDiagnosticoActionPerformed

    private void btnImprimirOrdenServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirOrdenServicioActionPerformed
        
        String costo = "0.00";
        String costo_extra = "0.00";
        String mensaje_extra = "";
        
        //Obtener valores constantes
        try {
            AccesoArchivo archivo = new AccesoArchivo();
            costo = archivo.leer("[Costo]");
            costo_extra = archivo.leer("[CostoExtra]");
            mensaje_extra = archivo.leer("[MensajeExtra]");
        } catch(IOException Error) {
            costo = "Q.75.00";
            costo_extra = "Q.100.00";
            mensaje_extra = "";
            mensaje.manipulacionExcepciones("critico", "No se encontro el valor del Costo fijo del Diagnostico.", "Costo Diagnostico");
        }
                
        //LLamar al reporte
        String strNombreReporte = "rptOrdenServicio";
        if(cbxTipoImpresion.getSelectedItem().toString().equals("Carta")) {
            strNombreReporte = "rptOrdenServicioCarta";
        }
        
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/" + strNombreReporte + ".jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //Parametros del reporte para los datos de la empresa.
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO_EMPRESA", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", "Impreso por: " + Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_ORDEN", Integer.parseInt(txtIdOrden.getText()));
            parametro.put("P_ID_CLIENTE", txtIdCliente.getText());
            parametro.put("P_ID_SERIE", txtIdSerie.getText());
            parametro.put("P_ID_FACTURA", txtIdFactura.getText());
            parametro.put("P_CARACTERISTICAS", txtCaracteristicas.getText().replaceAll("\n", ""));
            parametro.put("P_DEFECTO", txtObservacionesCliente.getText());
            parametro.put("P_DIAGNOSTICO", txtDiagonosticoTecnico.getText().replaceAll("\n", ""));
            
            parametro.put("P_NOMBRE", txtNombre.getText().toUpperCase());
            parametro.put("P_TELEFONO", txtTelefono.getText());
            parametro.put("P_FECHA_CREACION", txtFechaCreacion.getText());
            parametro.put("P_USUARIO_RECIBE", cbxTecnicoEncargado.getSelectedItem().toString());
            
            parametro.put("P_NUMERO_SERIE", txtSerieProducto.getText().replaceAll("'", "´"));
            parametro.put("P_MARCA", cbxMarca.getSelectedItem().toString());
            parametro.put("P_MODELO", txtModelo.getText());
            
            parametro.put("P_SALDO_TOTAL", txtValorPresupuesto.getText());
            parametro.put("P_SALDO_INICIAL", txtAbono.getText());
            
            Double valor = Double.parseDouble(txtValorPresupuesto.getText());
            Double abono = Double.parseDouble(txtAbono.getText());
            Double saldo = valor - abono;
            
            parametro.put("P_SALDO_VENTAS", String.valueOf(saldo));
            
            parametro.put("P_ESTADO", txtEstadoOrden.getText());
            
            parametro.put("P_COSTO", costo);
            parametro.put("P_COSTO_EXTRA", costo_extra);
            
            
            //Datos nuevos para las series internas
            parametro.put("P_CASE", txtCase.getText().replaceAll("'", "´"));
            parametro.put("P_CARGADOR", txtCargador.getText().replaceAll("'", "´"));
            parametro.put("P_BATERIA", txtBateria.getText().replaceAll("'", "´"));
            parametro.put("P_FUENTE", txtFuente.getText().replaceAll("'", "´"));
            parametro.put("P_RAM1", txtRamUno.getText().replaceAll("'", "´"));
            parametro.put("P_RAM2", txtRamDos.getText().replaceAll("'", "´"));
            parametro.put("P_RAM3", txtRamTres.getText().replaceAll("'", "´"));
            parametro.put("P_RAM4", txtRamCuatro.getText().replaceAll("'", "´"));
            parametro.put("P_DISCO", txtDisco.getText().replaceAll("'", "´"));
            parametro.put("P_TARJETA", txtTarjeta.getText().replaceAll("'", "´"));
            
            parametro.put("P_MSN_EXTRA", mensaje_extra);
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, acceso_inventario.conectar());
            JInternalFrame ventana = new JInternalFrame("Inventory 3.0");
            ventana.getContentPane().add(new JRViewer(pantalla));
            ventana.setPreferredSize(new Dimension(900, 500));
            ventana.setClosable(true);
            ventana.setMaximizable(true);
            ventana.setResizable(true);
            ventana.setVisible(true);
            ventana.pack();
            
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x  = (ancho  - ventana.getWidth() ) / 2;
            int y  = ( alto - ventana.getHeight() - 100 ) / 2;
            
            Inventory.pnlPrincipal.add(ventana);
            
            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();
            
            this.toBack();
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Orden Servicio");
        }
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        
    }//GEN-LAST:event_btnImprimirOrdenServicioActionPerformed

    private void txtNotificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotificarClienteActionPerformed
        txtFechaNotificaDiagnostico.setText(obtenerFechaActual());
        guardarNuevoEstado("Cliente No Contesta", "No Contesta");
        txtEstadoOrden.setText("No Contesta");
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
    }//GEN-LAST:event_txtNotificarClienteActionPerformed

    private void btnClienteAceptaPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteAceptaPresupuestoActionPerformed
       
        guardarNuevoEstado("Cliente Acepta Presupuesto Reparacion", "Acepta Reparacion");
        
        txtEstadoOrden.setText("Acepta Reparacion");
        cbxAceptaPresupuesto.setSelectedItem("SI");
        mensaje.manipulacionExcepciones("informacion", "Por favor ingrese Fecha Estimada de Entrega y el valor del Presupuesto", "Acepta Reparacion");
        
        txtFechaAceptaPresupuesto.setText(obtenerFechaActual());
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnClienteAceptaPresupuestoActionPerformed

    private void btnClienteAceptaCambioDeEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteAceptaCambioDeEquipoActionPerformed
        guardarNuevoEstado("Cleinte solicita remplazo de Equipo", "Remplazo de Equipo");
        txtEstadoOrden.setText("Remplazo de Equipo");
        mensaje.manipulacionExcepciones("informacion", "Por favor ingrese la Serie que se le entregará al Cliente", "Cliente acepta cambio de Equipo");
        txtFechaEntrega.setText(obtenerFechaActual());
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
    }//GEN-LAST:event_btnClienteAceptaCambioDeEquipoActionPerformed

    private void btnClienteSolicitaDevolucionDeEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteSolicitaDevolucionDeEfectivoActionPerformed
        
        guardarNuevoEstado("Cliente solicita rembolso de dinero", "Rembolso de Efectivo");
        
        txtEstadoOrden.setText("Rembolso de Efectivo");
        mensaje.manipulacionExcepciones("informacion", "La factura debe de ser anulada.", "Rembolso de Efectivo");
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnClienteSolicitaDevolucionDeEfectivoActionPerformed

    private void btnOrdenServicioEnProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenServicioEnProcesoActionPerformed
        
        if(Double.parseDouble(txtValorPresupuesto.getText()) > 0) {
            guardarNuevoEstado("Inicia el proceso de reparacion", "En Proceso");
            if(txtFechaEstimadaEntrega.getDate().toString().isEmpty()) {
                mensaje.manipulacionExcepciones("informacion", "Ingrese la fecha Estimada de Entrega y Precione el boton Modificar.", "Validar Fecha de Entrega");
            } else {
                guardarNuevoEstado("Inicia el proceso de reparacion", "En Proceso");
            }
        } else {
            mensaje.manipulacionExcepciones("informacion", "Ingrese el valor del Presupuesto.", "En Proceso");
        }
        
        txtEstadoOrden.setText("En Proceso");
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnOrdenServicioEnProcesoActionPerformed

    private void btnNotificaFinalizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificaFinalizacionActionPerformed
        
        txtFechaNotificaDiagnostico.setText(obtenerFechaActual());
        txtEstadoOrden.setText("Notifica Finalizacion");
        
        guardarNuevoEstado("Notifica a Cliente finalizacion", "Notifica Finalizacion");

        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnNotificaFinalizacionActionPerformed

    private void btnEntregaClienteProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregaClienteProductoActionPerformed
        
        txtFechaEntrega.setText(obtenerFechaActual());
        txtEstadoOrden.setText("Entregado");
        
        guardarNuevoEstado("Equipo entregado al Cliente", "Entregado");
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnEntregaClienteProductoActionPerformed

    private void btnBuscarRepuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarRepuestoActionPerformed
        wdwConsultaProductosOrdenes BuscarProducto = new wdwConsultaProductosOrdenes();
        ventana.abrirPantalla(BuscarProducto);
    }//GEN-LAST:event_btnBuscarRepuestoActionPerformed

    private void btnGuardarDetalleProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarDetalleProductoActionPerformed
        
        int datos_guardados = 0;
        
        for(int c=0; c<20; c++) {
            try {
                if (tblDetalleProductos.getValueAt(c, 7).toString().equals("Pendiente")) {
                    detalle.setId_d_orden_producto(0);
                    detalle.setId_orden(Integer.parseInt(tblDetalleProductos.getValueAt(c, 1).toString()));
                    detalle.setId_producto(tblDetalleProductos.getValueAt(c, 2).toString());
                    detalle.setDescripcion_d_orden_producto(tblDetalleProductos.getValueAt(c, 3).toString());
                    detalle.setCantidad_d_orden_produco(Double.parseDouble(tblDetalleProductos.getValueAt(c, 4).toString()));
                    detalle.setPrecio_d_orden_producto(Double.parseDouble(tblDetalleProductos.getValueAt(c, 5).toString()));
                    detalle.setSub_total_d_orden_producto(Double.parseDouble(tblDetalleProductos.getValueAt(c, 6).toString()));
                    detalle.setEstado_d_orden_producto("Guardado");
                    acceso_detalle.insertarDetalleOrden(detalle, Inventory.lblUsuario.getText(), Inventory.lblSucursal.getText());
                    tblDetalleProductos.setValueAt("Guardado", c, 7);
                    datos_guardados++;
                }
            } catch (Exception Error) {
                c=20;
            }
        }
        
        if(datos_guardados>0) {
            mensaje.manipulacionExcepciones("informacion", "Se almacenaron " + datos_guardados + " Productos.", "Guardar Productos");
        }
        
        limpiarTablaDetalle();
        buscarDetalleGuardado();
        
    }//GEN-LAST:event_btnGuardarDetalleProductoActionPerformed

    private void btnEliminarDetalleProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarDetalleProductoActionPerformed
        try {
            acceso_detalle.EliminarDetalleOrden(Integer.parseInt(tblDetalleProductos.getValueAt(tblDetalleProductos.getSelectedRow(), 0).toString()), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            limpiarTablaDetalle();
            buscarDetalleGuardado();
        } catch(Exception error) {
            mensaje.manipulacionExcepciones("critico", "Ocurrio un error al eliminar registro.", "Eliminar Registro");
        }
    }//GEN-LAST:event_btnEliminarDetalleProductoActionPerformed

    private void btnOrdenFinalizadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenFinalizadaActionPerformed
        
        guardarNuevoEstado("Finaliza proceso de reparacion", "Finaliza Reparacion");
        txtEstadoOrden.setText("Finaliza Reparacion");
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnOrdenFinalizadaActionPerformed

    private void btnEquipoEnEsperaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEquipoEnEsperaActionPerformed
        
        guardarNuevoEstado("En Espera", "En Espera");
        txtEstadoOrden.setText("En Espera");
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnEquipoEnEsperaActionPerformed

    private void btnGuardarLLamadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarLLamadaActionPerformed
        if(txtMotivoLlamada.getText().isEmpty() || txtRespuestaLlamada.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Por favor ingrese el motivo y la respuesta de la llamada.", "Guardar Registro Llamada");
        } else {
            if(txtIdOrden.getText().isEmpty()) {
                mensaje.manipulacionExcepciones("critico", "Por favor seleccione una Orden de Servicio.", "Seleccionar Orden de Servicio");
            } else {
                guardarRegistroLlamada();
            }
        }
    }//GEN-LAST:event_btnGuardarLLamadaActionPerformed

    private void btnContrasenia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContrasenia1ActionPerformed
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptRegistroLlamadas.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //Parametros del reporte para los datos de la empresa.
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_ORDEN", Integer.parseInt(txtIdOrden.getText()));
            parametro.put("P_ID_CLIENTE", txtIdCliente.getText());
            parametro.put("P_ID_SERIE", txtIdSerie.getText());
            parametro.put("P_ID_FACTURA", txtIdFactura.getText());
            parametro.put("P_DEFECTO", cbxDefectoReportado.getSelectedItem().toString().toUpperCase());
            
            parametro.put("P_NOMBRE", txtNombre.getText().toUpperCase());
            parametro.put("P_TELEFONO", txtTelefono.getText());
            parametro.put("P_FECHA_CREACION", txtFechaCreacion.getText());
            parametro.put("P_USUARIO_RECIBE", txtUsuarioCreacion.getText());
            
            parametro.put("P_NUMERO_SERIE", txtSerieProducto.getText().replaceAll("'", "´"));
            parametro.put("P_MARCA", cbxMarca.getSelectedItem().toString());
            parametro.put("P_MODELO", txtModelo.getText());
            
            
            parametro.put("P_SALDO_TOTAL", txtValorPresupuesto.getText());
            parametro.put("P_SALDO_INICIAL", txtAbono.getText());
            
            Double valor = Double.parseDouble(txtValorPresupuesto.getText());
            Double abono = Double.parseDouble(txtAbono.getText());
            Double saldo = valor - abono;
            
            parametro.put("P_SALDO_VENTAS", String.valueOf(saldo));
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, acceso_inventario.conectar());
            JInternalFrame ventana = new JInternalFrame("Inventory 3.0");
            ventana.getContentPane().add(new JRViewer(pantalla));
            ventana.setPreferredSize(new Dimension(900, 500));
            ventana.setClosable(true);
            ventana.setMaximizable(true);
            ventana.setResizable(true);
            ventana.setVisible(true);
            ventana.pack();
            
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x  = (ancho  - ventana.getWidth() ) / 2;
            int y  = ( alto - ventana.getHeight() - 100 ) / 2;
            
            Inventory.pnlPrincipal.add(ventana);
            
            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();
            
            this.toBack();
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Registro de Llamadas");
        }
        
    }//GEN-LAST:event_btnContrasenia1ActionPerformed

    private void btnClienteNoAceptaPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteNoAceptaPresupuestoActionPerformed
        
        guardarNuevoEstado("Cliente No Acepta Presupuesto Reparacion", "No Acepta Reparacion");
        
        txtEstadoOrden.setText("No Acepta Reparacion");
        cbxAceptaPresupuesto.setSelectedItem("SI");
        
        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        modificar();
        
    }//GEN-LAST:event_btnClienteNoAceptaPresupuestoActionPerformed

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed
        wdwConsultaProductos ConsultaProductos = new wdwConsultaProductos("Orden de Servicio Detalle");
        ventana.abrirPantalla(ConsultaProductos);
    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void btnBuscarSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSerieActionPerformed

        if (txtSerieProducto.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Por favor ingrese el número de Serie del Producto", "Buscar Serie");
        } else {
            ObjetosOrden Orden = acceso.buscarOrdenPorSerie(txtSerieProducto.getText().replaceAll("'", "´"), txtIdProducto.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            txtIdOrden.setText("");
            llenarDatosOrden(Orden);
        }

        buscarPendientes();
        buscarEnEspera();
        buscarGarantia();
        limpiarTablaDetalle();
        habilitarEstados();
    }//GEN-LAST:event_btnBuscarSerieActionPerformed

    private void btnBuscarSerie1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSerie1ActionPerformed

        //Bloquear el campo de busqueda
        txtIdOrden.setEditable(false);
        txtIdOrden.setBackground(new Color(102, 102, 102));
        txtIdOrden.setForeground(Color.WHITE);
        
        txtMotivoLlamada.setText("");
        txtRespuestaLlamada.setText("");

        orden = acceso.buscarOrdenPorId(Integer.parseInt(txtIdOrden.getText()), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

        if (orden.getId_orden() == 0) {
            mensaje.manipulacionExcepciones("informacion", "No existen registros almacenados para este numero de Orden.", "Buscar No. de Orden");
        } else {
            llenarDatosOrden(orden);
        }

        if (!txtIdOrden.getText().isEmpty()) {
            btnGuardar.setEnabled(false);
        }

        limpiarTablaDetalle();
        buscarDetalleGuardado();
        buscarPendientes();

        habilitarEstados();
    }//GEN-LAST:event_btnBuscarSerie1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarRepuesto;
    private javax.swing.JButton btnBuscarSerie;
    private javax.swing.JButton btnBuscarSerie1;
    private javax.swing.JButton btnClienteAceptaCambioDeEquipo;
    private javax.swing.JButton btnClienteAceptaPresupuesto;
    private javax.swing.JButton btnClienteNoAceptaPresupuesto;
    private javax.swing.JButton btnClienteSolicitaDevolucionDeEfectivo;
    private javax.swing.JButton btnContrasenia;
    private javax.swing.JButton btnContrasenia1;
    private javax.swing.JButton btnEliminarDetalleProducto;
    private javax.swing.JButton btnEntregaClienteProducto;
    private javax.swing.JButton btnEquipoEnEspera;
    public static javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarDetalleProducto;
    private javax.swing.JButton btnGuardarLLamada;
    private javax.swing.JButton btnImprimirOrdenServicio;
    private javax.swing.JButton btnIniciarDiagnostico;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNotificaFinalizacion;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnOrdenFinalizada;
    private javax.swing.JButton btnOrdenServicioEnProceso;
    public static javax.swing.JComboBox cbxAceptaPresupuesto;
    public static javax.swing.JComboBox cbxAplicaGarantia;
    public static javax.swing.JComboBox cbxDefectoReportado;
    public static javax.swing.JComboBox cbxMarca;
    public static javax.swing.JComboBox cbxTecnicoEncargado;
    public static javax.swing.JComboBox cbxTipoEquipo;
    private javax.swing.JComboBox<String> cbxTipoImpresion;
    public static javax.swing.JComboBox cbxTipoTrabajo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblEnEspera;
    private javax.swing.JLabel lblGarantia;
    private javax.swing.JLabel lblPendientes;
    private javax.swing.JLabel lblRam;
    private javax.swing.JPanel pnlBotones1;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlEstadistica;
    private javax.swing.JPanel pnlProducto;
    private javax.swing.JPanel pnlRegistroLlamadas;
    private javax.swing.JPanel pnlSeriesInternas;
    public static javax.swing.JTable tblDetalleProductos;
    public static javax.swing.JTextField txtAbono;
    public static javax.swing.JTextField txtBateria;
    private javax.swing.JButton txtBuscarProducto;
    public static javax.swing.JTextArea txtCaracteristicas;
    public static javax.swing.JTextField txtCargador;
    public static javax.swing.JTextField txtCase;
    public static javax.swing.JTextField txtDescripcionProductoBuscar;
    public static javax.swing.JTextArea txtDiagonosticoTecnico;
    public static javax.swing.JTextField txtDisco;
    public static javax.swing.JTextField txtEstadoOrden;
    public static javax.swing.JTextField txtFechaAceptaPresupuesto;
    public static javax.swing.JTextField txtFechaCreacion;
    public static javax.swing.JTextField txtFechaEntrega;
    public static com.toedter.calendar.JDateChooser txtFechaEstimadaEntrega;
    public static javax.swing.JTextField txtFechaNotificaDiagnostico;
    public static javax.swing.JTextField txtFuente;
    public static javax.swing.JTextField txtIdCliente;
    public static javax.swing.JTextField txtIdFactura;
    public static javax.swing.JTextField txtIdOrden;
    public static javax.swing.JTextField txtIdProducto;
    public static javax.swing.JTextField txtIdSerie;
    public static javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtMotivoLlamada;
    public static javax.swing.JTextField txtNombre;
    private javax.swing.JToggleButton txtNotificarCliente;
    public static javax.swing.JTextArea txtObservacionesCliente;
    public static javax.swing.JTextField txtOtros;
    private javax.swing.JLabel txtRam1;
    private javax.swing.JLabel txtRam2;
    private javax.swing.JLabel txtRam3;
    public static javax.swing.JTextField txtRamCuatro;
    public static javax.swing.JTextField txtRamDos;
    public static javax.swing.JTextField txtRamTres;
    public static javax.swing.JTextField txtRamUno;
    private javax.swing.JTextField txtRespuestaLlamada;
    public static javax.swing.JTextField txtSerieEntregaGarantia;
    public static javax.swing.JTextField txtSerieProducto;
    public static javax.swing.JTextField txtTarjeta;
    public static javax.swing.JTextField txtTelefono;
    public static javax.swing.JTextField txtTotalRepuestos;
    public static javax.swing.JTextField txtUsuarioCreacion;
    public static javax.swing.JTextField txtValorPresupuesto;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import com.toedter.calendar.JDateChooser;
import inventory.acceso.*;
import inventory.objetos.ObjetosDetallePlanilla;
import inventory.objetos.ObjetosEmpleado;
import inventory.objetos.ObjetosPlanilla;
import inventory.objetos.ObjetosSucursal;
import inventory.servicios.Matematicas;
import java.awt.Dimension;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author DSANTACRUZ
 */
public class wdwContabilidadSalarios extends javax.swing.JInternalFrame {

    private AccesoExcepciones mensaje = new AccesoExcepciones();
    private Matematicas matematicas = new Matematicas();
    private ObjetosPlanilla objeto_planilla = new ObjetosPlanilla();
    private ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
    private ObjetosEmpleado objeto_empleado = new ObjetosEmpleado();
    private ObjetosDetallePlanilla objeto_detalle = new ObjetosDetallePlanilla();
    private AccesoPlanilla acceso_planilla = new AccesoPlanilla();
    private AccesoFactura acceso_factura = new AccesoFactura();
    private AccesoEmpleado acceso_empleados = new AccesoEmpleado();
    private AccesoSucursal acceso_sucusal = new AccesoSucursal();
    private AccesoDetallePlanilla acceso_detalle = new AccesoDetallePlanilla();
    private String resultado = "";
    
    private static DefaultTableModel modelo = new DefaultTableModel();
    
    /**
     * Creates new form wdwContabilidadSalarios
     */
    public wdwContabilidadSalarios() {
        initComponents();
        cargaEmpleados();
        cargaSucursales();
        txtUsuarioCreacion.setText(Inventory.lblUsuario.getText());
    }
    
    private void limpiarTablaDetalle() {
        try {
            modelo = (DefaultTableModel) tblDetalle.getModel();
            for (int x=0; x < modelo.getRowCount(); x++) {
                modelo.removeRow(x);
            }
        } catch (Exception Error) {
            System.out.println("Error al limpira tabla de Componentes. \n" + Error.toString());
        }
        modelo.setRowCount(20);
        tblDetalle.setModel(modelo);
    }
    
    private void calcularTotal() {
        int id_detalle = txtIdDetallePlanilla.getText().isEmpty() ? 0 : Integer.parseInt(txtIdDetallePlanilla.getText());
        double salario = txtSalario.getText().isEmpty() ? 0.0 : Double.parseDouble(txtSalario.getText());
        double bonificacion = txtBonificacion.getText().isEmpty() ? 0.0 : Double.parseDouble(txtBonificacion.getText());
        double igss = txtIgss.getText().isEmpty() ? 0.0 : Double.parseDouble(txtIgss.getText());
        double comision = txtComision.getText().isEmpty() ? 0.0 : Double.parseDouble(txtComision.getText());
        double horas = txtHorasExtras.getText().isEmpty() ? 0.0 : Double.parseDouble(txtHorasExtras.getText());
        double descuentos = txtDescuentos.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDescuentos.getText());
        double creditos = txtCreditos.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCreditos.getText());
        double vales = txtVales.getText().isEmpty() ? 0.0 : Double.parseDouble(txtVales.getText());
        double total = salario + bonificacion - igss + comision + horas - descuentos - creditos - vales;
        double pago_total = matematicas.aproxima(total, 2);
        //quitar los decimales extra del valor total
        float valor = acceso_detalle.quitarDecimalesExtra(total, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        txtTotal.setText(String.valueOf(total));
    }
    
    private void limpiarDatosDetalle() {
        txtSalario.setText("0.0");
        txtBonificacion.setText("0.0");
        txtIgss.setText("0.0");
        txtComision.setText("0.0");
        txtHorasExtras.setText("0.0");
        txtDescuentos.setText("0.0");
        txtCreditos.setText("0.0");
        txtVales.setText("0.0");
        txtTotal.setText("0.0");
    }
    
    private void cargaSucursales(){
        ArrayList<ObjetosEmpleado> lSucursales = new ArrayList();
        
        try{
            lSucursales = acceso_empleados.retornaNombreSucursal(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Nombre Surcursal");
        }
        
        // Lleno el combobox con los registros de sucursales que existen
        for(ObjetosEmpleado xSucursal : lSucursales){
            cbxSucursal.addItem(xSucursal.getNombre_sucursal());
        }
    }
    
     private void cargaEmpleados(){
        
        ArrayList<ObjetosEmpleado> lista_empleados = new ArrayList();
        
        try{
            lista_empleados = acceso_empleados.listarEmpleadosActivos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Nombre Empleado");
        }
        
        //Lleno el combobox con los registros de empleados que existen
        for(ObjetosEmpleado empleados : lista_empleados){
            cbxEmpleado.addItem(empleados.getNombre_empleado());
        }

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

    public String ConvertirFecha(JDateChooser Calendario) {
        if ( Calendario.getDate() == null ) {
            return null;
        } else {
        return ( Calendario.getDate().getYear() + 1900 ) + "-" + ( Calendario.getDate().getMonth() + 1 ) + "-" + Calendario.getDate().getDate();
        }
    }
    
    public void crearObjetoPlanilla () {
        int id_planilla = txtIdPlanilla.getText().isEmpty() ? 0 : Integer.parseInt(txtIdPlanilla.getText());
        objeto_planilla.setId_planilla(id_planilla);
        objeto_planilla.setId_sucursal(Integer.parseInt(txtIdSucursal.getText()));
        objeto_planilla.setAnio_planilla(Integer.parseInt(txtAnio.getText()));
        objeto_planilla.setMes_planilla(cbxMes.getSelectedItem().toString());
        objeto_planilla.setPeriodo_planilla(Integer.parseInt(txtPeriodo.getText()));
        objeto_planilla.setFecha_inicial_planilla(ConvertirFecha(dcFechaInicial));
        objeto_planilla.setFecha_final_planilla(ConvertirFecha(dcFechaFinal));
        objeto_planilla.setFecha_elaboro_planilla(txtFechaCreacion.getText());
        objeto_planilla.setUsuario_elaboro_planilla(txtUsuarioCreacion.getText());
        objeto_planilla.setTipo_planilla(cbxTipo.getSelectedItem().toString());
        objeto_planilla.setEstado_planilla(cbxEstado.getSelectedItem().toString());
        objeto_planilla.setComentario_planilla(txtComentario.getText());
    }
    
    public void leerObjetoPlanilla (ObjetosPlanilla planilla) {
        txtIdPlanilla.setText(String.valueOf(planilla.getId_planilla()));
        txtIdSucursal.setText(String.valueOf(planilla.getId_sucursal()));
        txtAnio.setText(String.valueOf(planilla.getAnio_planilla()));
        cbxMes.setSelectedItem(planilla.getMes_planilla());
        txtPeriodo.setText(String.valueOf(planilla.getPeriodo_planilla()));
        dcFechaInicial.setDate(CovertirDate(planilla.getFecha_inicial_planilla()));
        dcFechaFinal.setDate(CovertirDate(planilla.getFecha_final_planilla()));
        txtFechaCreacion.setText(planilla.getFecha_elaboro_planilla());
        txtUsuarioCreacion.setText(planilla.getUsuario_elaboro_planilla());
        cbxTipo.setSelectedItem(planilla.getTipo_planilla());
        cbxEstado.setSelectedItem(planilla.getEstado_planilla());
        txtComentario.setText(planilla.getComentario_planilla());
    }
    
    public void buscarDetalleGuardado() {
        
        ArrayList<ObjetosDetallePlanilla> lista = acceso_detalle.listarPlanilla(Integer.parseInt(txtIdPlanilla.getText()), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        Iterator iterardor = lista.iterator();
        int pos = 0;

        while(iterardor.hasNext()) {
            ObjetosDetallePlanilla d = (ObjetosDetallePlanilla) iterardor.next();
            tblDetalle.setValueAt(d.getId_d_planilla(), pos, 0);
            tblDetalle.setValueAt(d.getId_planilla(), pos, 1);
            tblDetalle.setValueAt(d.getId_empleado(), pos, 2);
            tblDetalle.setValueAt(d.getNombre_d_planilla(), pos, 3);
            tblDetalle.setValueAt(d.getPuesto_d_planilla(), pos, 4);
            tblDetalle.setValueAt(d.getSalario_d_planilla(), pos, 5);
            tblDetalle.setValueAt(d.getBonificacion_d_planilla(), pos, 6);
            tblDetalle.setValueAt(d.getIgsss_d_planilla(), pos, 7);
            tblDetalle.setValueAt(d.getComision_d_planilla(), pos, 8);
            tblDetalle.setValueAt(d.getHoras_extras_d_planilla(), pos, 9);
            tblDetalle.setValueAt(d.getDescuentos_d_planilla(), pos, 10);
            tblDetalle.setValueAt(d.getCreditos_d_planilla(), pos, 11);
            tblDetalle.setValueAt(d.getVales_d_planilla(), pos, 12);
            tblDetalle.setValueAt(d.getComentarios_d_planilla(), pos, 13);
            pos++;
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

        pnlPlanilla = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdPlanilla = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtIdSucursal = new javax.swing.JTextField();
        cbxSucursal = new javax.swing.JComboBox();
        pnlPeriodo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JFormattedTextField();
        cbxMes = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPeriodo = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        dcFechaInicial = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        dcFechaFinal = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        txtUsuarioCreacion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtFechaCreacion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxTipo = new javax.swing.JComboBox();
        cbxEstado = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        txtComentario = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscarIdPlanilla = new javax.swing.JButton();
        pnlEmpleado = new javax.swing.JPanel();
        cbxEmpleado = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        txtIdDetallePlanilla = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        pnlDinero = new javax.swing.JPanel();
        txtSalario = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtBonificacion = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtIgss = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtComision = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtHorasExtras = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescuentos = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCreditos = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtVales = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtComentarioDetalle = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        txtPuesto = new javax.swing.JTextField();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        pnlBotones = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Planilla");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgContabilidadSalarios.png"))); // NOI18N

        pnlPlanilla.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("ID Planilla");

        txtIdPlanilla.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("ID Sucursal");

        txtIdSucursal.setEditable(false);
        txtIdSucursal.setBackground(new java.awt.Color(102, 102, 102));
        txtIdSucursal.setForeground(new java.awt.Color(255, 255, 255));

        cbxSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSucursalActionPerformed(evt);
            }
        });

        pnlPeriodo.setBackground(new java.awt.Color(204, 204, 204));
        pnlPeriodo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PERIODO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        jLabel3.setText("Año");

        txtAnio.setBackground(new java.awt.Color(255, 255, 204));
        txtAnio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####"))));

        cbxMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel4.setText("Mes");

        jLabel5.setText("Periodo");

        txtPeriodo.setBackground(new java.awt.Color(255, 255, 204));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscar.setActionCommand("buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPeriodoLayout = new javax.swing.GroupLayout(pnlPeriodo);
        pnlPeriodo.setLayout(pnlPeriodoLayout);
        pnlPeriodoLayout.setHorizontalGroup(
            pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPeriodoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPeriodoLayout.createSequentialGroup()
                        .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPeriodoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)))
                .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAnio)
                    .addComponent(cbxMes, 0, 89, Short.MAX_VALUE)
                    .addGroup(pnlPeriodoLayout.createSequentialGroup()
                        .addComponent(txtPeriodo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlPeriodoLayout.setVerticalGroup(
            pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPeriodoLayout.createSequentialGroup()
                .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPeriodoLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPeriodoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(btnBuscar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("Fecha Inicial");

        jLabel7.setText("Fecha Final");

        jLabel8.setText("Usuario");

        txtUsuarioCreacion.setEditable(false);
        txtUsuarioCreacion.setBackground(new java.awt.Color(102, 102, 102));
        txtUsuarioCreacion.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("Fecha");

        txtFechaCreacion.setEditable(false);
        txtFechaCreacion.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaCreacion.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("Tipo");

        cbxTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Salario Ordinario", "Vacaciones", "Bono 14", "Aguinaldo ", "Pago Especial" }));

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Creado", "Pagado", "Descartado" }));

        jLabel11.setText("Estado");

        txtComentario.setBackground(new java.awt.Color(255, 255, 204));

        jLabel12.setText("Comentario");

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscarIdPlanilla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarIdPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarIdPlanillaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPlanillaLayout = new javax.swing.GroupLayout(pnlPlanilla);
        pnlPlanilla.setLayout(pnlPlanillaLayout);
        pnlPlanillaLayout.setHorizontalGroup(
            pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlanillaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlPlanillaLayout.createSequentialGroup()
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPlanillaLayout.createSequentialGroup()
                                .addComponent(txtIdPlanilla)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarIdPlanilla))
                            .addComponent(cbxEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlPlanillaLayout.createSequentialGroup()
                        .addComponent(btnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar)))
                .addGap(18, 18, 18)
                .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPlanillaLayout.createSequentialGroup()
                        .addComponent(txtIdSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxSucursal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlPlanillaLayout.createSequentialGroup()
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .addComponent(txtUsuarioCreacion))
                        .addGap(18, 18, 18)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(txtFechaCreacion)))
                    .addComponent(txtComentario))
                .addGap(18, 18, 18)
                .addComponent(pnlPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlPlanillaLayout.setVerticalGroup(
            pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlanillaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlPlanillaLayout.createSequentialGroup()
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(txtIdSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarIdPlanilla))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dcFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(cbxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10))
                            .addComponent(jLabel7)
                            .addComponent(dcFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUsuarioCreacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(txtFechaCreacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPlanillaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtComentario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(btnEditar)
                            .addComponent(btnGuardar))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlEmpleado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbxEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPersonaActionPerromed(evt);
            }
        });

        jLabel13.setText("ID Empleado");

        txtIdEmpleado.setBackground(new java.awt.Color(102, 102, 102));
        txtIdEmpleado.setForeground(new java.awt.Color(255, 255, 255));

        txtIdDetallePlanilla.setEditable(false);
        txtIdDetallePlanilla.setBackground(new java.awt.Color(102, 102, 102));
        txtIdDetallePlanilla.setForeground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("ID Detalle Planilla");

        pnlDinero.setBackground(new java.awt.Color(204, 204, 204));
        pnlDinero.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "RUBROS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));
        pnlDinero.setForeground(new java.awt.Color(204, 204, 204));

        txtSalario.setBackground(new java.awt.Color(255, 255, 204));
        txtSalario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSalario.setText("0.0");
        txtSalario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                salarioLostFocus(evt);
            }
        });

        jLabel15.setText("+ Salario");

        txtBonificacion.setBackground(new java.awt.Color(255, 255, 204));
        txtBonificacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBonificacion.setText("0.0");
        txtBonificacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bonificacionLostFocus(evt);
            }
        });

        jLabel16.setText("+ Bonificacion");

        txtIgss.setBackground(new java.awt.Color(204, 255, 204));
        txtIgss.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIgss.setText("0.0");
        txtIgss.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                igssLostFocus(evt);
            }
        });

        jLabel17.setText("- IGSS");

        txtComision.setBackground(new java.awt.Color(255, 255, 204));
        txtComision.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComision.setText("0.0");
        txtComision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comisionLostFocus(evt);
            }
        });

        jLabel18.setText("+ Comisión");

        txtHorasExtras.setBackground(new java.awt.Color(255, 255, 204));
        txtHorasExtras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtHorasExtras.setText("0.0");
        txtHorasExtras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                horasLostFocus(evt);
            }
        });

        jLabel19.setText("+ Horas Extras");

        txtDescuentos.setBackground(new java.awt.Color(204, 255, 204));
        txtDescuentos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDescuentos.setText("0.0");
        txtDescuentos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descuentosLostFocus(evt);
            }
        });

        jLabel20.setText("- Descuentos");

        txtCreditos.setBackground(new java.awt.Color(204, 255, 204));
        txtCreditos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCreditos.setText("0.0");
        txtCreditos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                creditosLostFocus(evt);
            }
        });

        jLabel21.setText("- Créditos");

        txtVales.setBackground(new java.awt.Color(204, 255, 204));
        txtVales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtVales.setText("0.0");
        txtVales.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valesLostFocus(evt);
            }
        });

        jLabel22.setText("- Vales");

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 102, 102));
        txtTotal.setForeground(new java.awt.Color(255, 255, 255));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setText("0.0");

        jLabel23.setText("Total");

        jLabel24.setText("Comentario");

        txtComentarioDetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtComentarioLostFocus(evt);
            }
        });

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonAgregar.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDineroLayout = new javax.swing.GroupLayout(pnlDinero);
        pnlDinero.setLayout(pnlDineroLayout);
        pnlDineroLayout.setHorizontalGroup(
            pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDineroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDineroLayout.createSequentialGroup()
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtComentarioDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlDineroLayout.createSequentialGroup()
                        .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addGroup(pnlDineroLayout.createSequentialGroup()
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBonificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIgss, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtComision, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHorasExtras, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addGap(18, 18, 18)
                                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(txtVales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlDineroLayout.setVerticalGroup(
            pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDineroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBonificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIgss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHorasExtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDineroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComentarioDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtPuesto.setEditable(false);
        txtPuesto.setBackground(new java.awt.Color(102, 102, 102));
        txtPuesto.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlEmpleadoLayout = new javax.swing.GroupLayout(pnlEmpleado);
        pnlEmpleado.setLayout(pnlEmpleadoLayout);
        pnlEmpleadoLayout.setHorizontalGroup(
            pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdDetallePlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlDinero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlEmpleadoLayout.setVerticalGroup(
            pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdDetallePlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDinero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Detalle", "ID Planilla", "ID Empleado", "Nombre", "Puesto", "Salario", "Bonificacion", "IGSS", "Comisión", "Horas Extras", "Descuentos", "Creditos", "Vales", "Comentarios"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblDetalle);

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonNuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnReporte.setActionCommand("btnReporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnReporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReporte)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnNuevo)
                        .addComponent(btnEliminar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        if (txtAnio.getText().isEmpty() || txtPeriodo.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Debe ingresar el Año y el Periodo.", "Buscar Planilla");
        } else {
            btnGuardar.setEnabled(false);
            objeto_planilla = acceso_planilla.seleccionarPlanillaPorPeriodo(Integer.parseInt(txtAnio.getText()), cbxMes.getSelectedItem().toString(), Integer.parseInt(txtPeriodo.getText()),
                    Inventory.lblUsuario.getText(), Inventory.lblSucursal.getText());
            leerObjetoPlanilla(objeto_planilla);
            if (!txtIdPlanilla.getText().isEmpty()) {
                limpiarTablaDetalle();
                buscarDetalleGuardado();
                limpiarDatosDetalle();
            }
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.dispose();
        wdwContabilidadSalarios Salarios = new wdwContabilidadSalarios();
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x = (ancho / 2) - (Salarios.getWidth() / 2);
        int y = (alto / 2) - (Salarios.getHeight() / 2);
        Salarios.setVisible(true);
        Inventory.pnlPrincipal.add(Salarios);
        Salarios.toFront();
        Salarios.setLocation(x, y);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptBoletaDePagoEmpleado.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //parametros del reporte
            int id = Integer.parseInt(tblDetalle.getValueAt(tblDetalle.getSelectedRow(), 0).toString());
            parametro.put("P_ID_DETALLE", id);
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_USUARIO", Inventory.lblNombre.getText());    
            parametro.put("P_TIPO_PLANILLA", cbxTipo.getSelectedItem().toString());
            
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
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte BOLETA DE PAGO");
        }
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int id = Integer.parseInt(tblDetalle.getValueAt(tblDetalle.getSelectedRow(), 0).toString());
        acceso_detalle.eliminarDetalle(id, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        limpiarTablaDetalle();
        buscarDetalleGuardado();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        crearObjetoPlanilla();
        txtUsuarioCreacion.setText(Inventory.lblUsuario.getText());
        resultado = acceso_planilla.actualizarPlanilla(objeto_planilla, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        mensaje.manipulacionExcepciones("informacion", resultado, "Editar");
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        crearObjetoPlanilla();
        resultado = acceso_planilla.insertaPlanilla(objeto_planilla, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        mensaje.manipulacionExcepciones("informacion", resultado, "Guardar");
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cbxSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSucursalActionPerformed
        String seleccion = "";
        seleccion = cbxSucursal.getSelectedItem().toString();
        
        int idSucursal = 0;
            
        try{
            objeto_sucursal = acceso_sucusal.buscarSucursalPorNombre(seleccion, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            txtIdSucursal.setText(String.valueOf(objeto_sucursal.getId_sucursal()));   
        }catch(Exception error){
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar ID Sucursal");
        }
    }//GEN-LAST:event_cbxSucursalActionPerformed

    private void cbxPersonaActionPerromed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPersonaActionPerromed
        String seleccion = "";
        seleccion = cbxEmpleado.getSelectedItem().toString();
        
        int idEmpleado = 0;
            
        try{
            objeto_empleado = acceso_factura.retornaEmpleado(seleccion, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            txtIdEmpleado.setText(String.valueOf(objeto_empleado.getId_empleado()));
            txtPuesto.setText(objeto_empleado.getTipo_empleado());
            txtSalario.setText(String.valueOf(objeto_empleado.getSalario_empleado()));
        }catch(Exception error){
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar ID Empleado");
        }
    }//GEN-LAST:event_cbxPersonaActionPerromed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed

        calcularTotal();
        
        int id_detalle = txtIdDetallePlanilla.getText().isEmpty() ? 0 : Integer.parseInt(txtIdDetallePlanilla.getText());
        double salario = txtSalario.getText().isEmpty() ? 0.0 : Double.parseDouble(txtSalario.getText());
        double bonificacion = txtBonificacion.getText().isEmpty() ? 0.0 : Double.parseDouble(txtBonificacion.getText());
        double igss = txtIgss.getText().isEmpty() ? 0.0 : Double.parseDouble(txtIgss.getText());
        double comision = txtComision.getText().isEmpty() ? 0.0 : Double.parseDouble(txtComision.getText());
        double horas = txtHorasExtras.getText().isEmpty() ? 0.0 : Double.parseDouble(txtHorasExtras.getText());
        double descuentos = txtDescuentos.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDescuentos.getText());
        double creditos = txtCreditos.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCreditos.getText());
        double vales = txtVales.getText().isEmpty() ? 0.0 : Double.parseDouble(txtVales.getText());
        
        double total = salario + bonificacion - igss + comision + horas - descuentos - creditos - vales;
        
        objeto_detalle.setId_planilla(Integer.parseInt(txtIdPlanilla.getText()));
        objeto_detalle.setId_d_planilla(id_detalle);
        objeto_detalle.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
        objeto_detalle.setNombre_d_planilla(cbxEmpleado.getSelectedItem().toString());
        objeto_detalle.setPuesto_d_planilla(txtPuesto.getText());
        objeto_detalle.setSalario_d_planilla(salario);
        objeto_detalle.setBonificacion_d_planilla(bonificacion);
        objeto_detalle.setIgsss_d_planilla(igss);
        objeto_detalle.setComision_d_planilla(comision);
        objeto_detalle.setHoras_extras_d_planilla(horas);
        objeto_detalle.setDescuentos_d_planilla(descuentos);
        objeto_detalle.setCreditos_d_planilla(creditos);
        objeto_detalle.setVales_d_planilla(vales);
        objeto_detalle.setComentarios_d_planilla(txtComentarioDetalle.getText());
        
        txtTotal.setText(String.valueOf(total));
        
        if(total > 0) {
            resultado = acceso_detalle.insertaPlanilla(objeto_detalle, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            mensaje.manipulacionExcepciones("informacion", resultado, "Guardar Detalle");
        } else {
            mensaje.manipulacionExcepciones("critico", "Ingrese los datos para calcular el pago.", "Calculo de Pago");
        }
       
        limpiarTablaDetalle();
        limpiarDatosDetalle();
        buscarDetalleGuardado();
        
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void salarioLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salarioLostFocus
        calcularTotal();
    }//GEN-LAST:event_salarioLostFocus

    private void bonificacionLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bonificacionLostFocus
        calcularTotal();
    }//GEN-LAST:event_bonificacionLostFocus

    private void igssLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_igssLostFocus
        calcularTotal();
    }//GEN-LAST:event_igssLostFocus

    private void comisionLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comisionLostFocus
        calcularTotal();
    }//GEN-LAST:event_comisionLostFocus

    private void horasLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horasLostFocus
        calcularTotal();
    }//GEN-LAST:event_horasLostFocus

    private void descuentosLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentosLostFocus
        calcularTotal();
    }//GEN-LAST:event_descuentosLostFocus

    private void creditosLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_creditosLostFocus
        calcularTotal();
    }//GEN-LAST:event_creditosLostFocus

    private void valesLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valesLostFocus
        calcularTotal();
    }//GEN-LAST:event_valesLostFocus

    private void txtComentarioLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtComentarioLostFocus
        if(txtComentario.getText().isEmpty()) {
            txtComentario.setText("S/C");
        }
    }//GEN-LAST:event_txtComentarioLostFocus

    private void btnBuscarIdPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarIdPlanillaActionPerformed
        if (txtIdPlanilla.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Debe ingresar el ID de Planilla.", "Buscar Planilla ID");
        } else {
            btnGuardar.setEnabled(false);
            int id = Integer.parseInt(txtIdPlanilla.getText());
            objeto_planilla = acceso_planilla.seleccionarPlanillaPorId(id, Inventory.lblUsuario.getText(), Inventory.lblSucursal.getText());
            leerObjetoPlanilla(objeto_planilla);
            if (!txtIdPlanilla.getText().isEmpty()) {
                limpiarTablaDetalle();
                buscarDetalleGuardado();
                limpiarDatosDetalle();
            }
        }
    }//GEN-LAST:event_btnBuscarIdPlanillaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarIdPlanilla;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnReporte;
    private javax.swing.JComboBox cbxEmpleado;
    private javax.swing.JComboBox cbxEstado;
    private javax.swing.JComboBox cbxMes;
    private javax.swing.JComboBox cbxSucursal;
    private javax.swing.JComboBox cbxTipo;
    private com.toedter.calendar.JDateChooser dcFechaFinal;
    private com.toedter.calendar.JDateChooser dcFechaInicial;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlDinero;
    private javax.swing.JPanel pnlEmpleado;
    private javax.swing.JPanel pnlPeriodo;
    private javax.swing.JPanel pnlPlanilla;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JFormattedTextField txtAnio;
    private javax.swing.JTextField txtBonificacion;
    private javax.swing.JTextField txtComentario;
    private javax.swing.JTextField txtComentarioDetalle;
    private javax.swing.JTextField txtComision;
    private javax.swing.JTextField txtCreditos;
    private javax.swing.JTextField txtDescuentos;
    private javax.swing.JTextField txtFechaCreacion;
    private javax.swing.JTextField txtHorasExtras;
    private javax.swing.JTextField txtIdDetallePlanilla;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdPlanilla;
    private javax.swing.JTextField txtIdSucursal;
    private javax.swing.JTextField txtIgss;
    private javax.swing.JTextField txtPeriodo;
    private javax.swing.JTextField txtPuesto;
    private javax.swing.JTextField txtSalario;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuarioCreacion;
    private javax.swing.JTextField txtVales;
    // End of variables declaration//GEN-END:variables
}

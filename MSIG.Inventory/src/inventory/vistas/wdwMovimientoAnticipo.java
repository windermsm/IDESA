/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import com.toedter.calendar.JDateChooser;
import inventory.acceso.*;
import inventory.librerias.WindowController;
import inventory.objetos.ObjetosAnticipo;
import inventory.objetos.ObjetosCliente;
import inventory.objetos.ObjetosPersona;
import inventory.objetos.ObjetosSucursal;
import inventory.servicios.Matematicas;
import java.awt.Dimension;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JInternalFrame;
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
public class wdwMovimientoAnticipo extends javax.swing.JInternalFrame {

    private WindowController ventana = new WindowController();
    private AccesoExcepciones mensaje = new AccesoExcepciones();
    private ObjetosAnticipo anticipo = new ObjetosAnticipo();
    private AccesoAnticipo acceso = new AccesoAnticipo();
    private Matematicas matematicas = new Matematicas();
    
    String Log = "";
    
    /**
     * Creates new form wdwMovimientoAnticipo
     */
    public wdwMovimientoAnticipo() {
        initComponents();
    }
    
    private void agregarLog(String texto) {
        this.Log = this.Log + "ANTICIPO" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
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
    
    public static String ConvertirFecha(JDateChooser Calendario) {
        return ( Calendario.getDate().getYear() + 1900 ) + "-" + ( Calendario.getDate().getMonth() + 1 ) + "-" + Calendario.getDate().getDate();
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
    
    public void imprimirReporte(String tamanio) {
        //ejecución del repote seleccionado
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            
            //Validar el tamaño
            String nombre_reporte = "rptAnticipo";
            if(tamanio.equals("Carta")) {
                nombre_reporte = "rptAnticipoCarta";
            }
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/" + nombre_reporte + ".jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //parametros del reporte
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_NOMBRE_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_SUCURSAL", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            
            //parametro de busqueda
            parametro.put("P_ID_ANTICIPO", Integer.parseInt(txtIdAnticipo.getText().trim()));
            
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
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Anticipo Cliente");
        }
    }
    
    public void calcularTotal() {
        try {
            double v_anticipo = Double.parseDouble(txtTotal.getText().isEmpty() ? "0.0" : txtTotal.getText());
            double v_saldo = Double.parseDouble(txtSaldo.getText().isEmpty() ? "0.0" : txtSaldo.getText());
            double v_calculo_total = matematicas.aproxima((v_saldo + v_anticipo), 2);
            txtCalculoTotal.setText(String.valueOf(v_calculo_total));
        } catch (Exception Error) {
            txtCalculoTotal.setText("0.0");
            agregarLog("Error al calcular Total. \n" + Error.toString());
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

        pnlEncabezado = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdAnticipo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTelefonos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        calFechaEntrega = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        txtComentario = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        txtIdFactura = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCalculoTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNIT = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtFechaEntregaAnticipo = new javax.swing.JTextField();
        pnlBotones = new javax.swing.JPanel();
        btnImpresion = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        cbxTamanio = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Anticipo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAnticipos.png"))); // NOI18N

        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlEncabezado.setToolTipText("");

        jLabel1.setText("ID Anticipo");

        txtNombre.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("Nombre");

        txtTelefonos.setBackground(new java.awt.Color(255, 255, 204));

        jLabel3.setText("Telefonos");

        jLabel4.setText("Fecha Entrega Anticipo");

        jLabel5.setText("Descripción");

        txtDetalle.setColumns(20);
        txtDetalle.setRows(5);
        jScrollPane1.setViewportView(txtDetalle);

        jLabel6.setText("Comentario");

        txtTotal.setBackground(new java.awt.Color(255, 255, 204));

        jLabel7.setText("Anticipo");

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Creado", "Anulado", "Despachado", "Devuelto" }));

        jLabel8.setText("Estado");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel9.setText("ID Factura");

        txtSaldo.setBackground(new java.awt.Color(255, 255, 204));

        jLabel10.setText("Saldo");

        txtCalculoTotal.setEditable(false);
        txtCalculoTotal.setBackground(new java.awt.Color(102, 102, 102));
        txtCalculoTotal.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("Total");

        jLabel12.setText("NIT");

        txtNIT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNitLostFocus(evt);
            }
        });

        jLabel14.setText("Fecha Entrega Producto");

        txtFechaEntregaAnticipo.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaEntregaAnticipo.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(17, 17, 17)
                        .addComponent(txtComentario))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 543, Short.MAX_VALUE)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12))
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                                .addComponent(txtIdAnticipo, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnBuscar))
                                            .addComponent(txtNIT)
                                            .addComponent(txtTelefonos))
                                        .addGap(39, 39, 39)
                                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtNombre))
                                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(calFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtFechaEntregaAnticipo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCalculoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel4)
                        .addComponent(txtFechaEntregaAnticipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtIdAnticipo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel12)
                        .addComponent(txtNIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel14))
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(calFechaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(7, 7, 7)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCalculoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtComentario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnImpresion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnImpresion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpresionActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonNuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        jLabel13.setText("Tamaño");

        cbxTamanio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1/2 Carta", "Carta" }));

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxTamanio, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImpresion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNuevo)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxTamanio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevo)
                            .addComponent(btnGuardar)
                            .addComponent(btnModificar))
                        .addComponent(btnImpresion)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.dispose();
        wdwMovimientoAnticipo anticipo = new wdwMovimientoAnticipo();
        ventana.abrirPantalla(anticipo);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        calcularTotal();
        
        if(!txtIdAnticipo.getText().isEmpty()) {
            
            mensaje.manipulacionExcepciones("critico", "Este registro ya fue guardado.", "Guardar Anticipo");
        
        } else {
            
            anticipo.setId_anticipo(0);
            anticipo.setNombre_persona_anticipo(txtNombre.getText().toUpperCase());
            anticipo.setTelefono_anticipo(txtTelefonos.getText());
            anticipo.setFecha_entrega_producto_anticipo(ConvertirFecha(calFechaEntrega));
            anticipo.setDetalle_anticipo(txtDetalle.getText());
            anticipo.setTotal_anticipo(Double.parseDouble(txtTotal.getText()));
            anticipo.setComentarios_anticipo(txtComentario.getText());
            anticipo.setEstado_anticipo(cbxEstado.getSelectedItem().toString());
            anticipo.setUsuario_registro_anticipo(Inventory.lblUsuario.getText());
            anticipo.setId_factura(Integer.parseInt(txtIdFactura.getText().isEmpty() ? "0" : txtIdFactura.getText()));
            anticipo.setSaldo_anticipo(Double.parseDouble(txtSaldo.getText().isEmpty() ? "0.0" : txtSaldo.getText()));
            anticipo.setNit_anticipo(txtNIT.getText());
            anticipo.setUsuario_registro_anticipo(Inventory.lblUsuario.getText());
            
            int id = 0;
            
            try {
                id = acceso.insertarAnticipo(anticipo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            } catch (Exception Error) {
                agregarLog(Error.toString());
            }
            
            if(id > 0) {
                txtIdAnticipo.setText(String.valueOf(id));
                txtFechaEntregaAnticipo.setText(fechaActual());
                mensaje.manipulacionExcepciones("informacion", "Registro almacenado con exito.", "Guardar Anticipo");
                btnGuardar.setEnabled(false);
            } else {
                mensaje.manipulacionExcepciones("critico", "Ocurrio un error al alamacer el registro.", "Guardar Anticipo"); 
            }
            
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        calcularTotal();
        
        try {
            anticipo = acceso.buscarAnticipoID(txtIdAnticipo.getText().trim(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            txtNombre.setText(anticipo.getNombre_persona_anticipo());
            cbxEstado.setSelectedItem(anticipo.getEstado_anticipo());
            txtTelefonos.setText(anticipo.getTelefono_anticipo());
            calFechaEntrega.setDate(CovertirDate(anticipo.getFecha_entrega_producto_anticipo()));
            txtTotal.setText(String.valueOf(anticipo.getTotal_anticipo()));
            txtDetalle.setText(anticipo.getDetalle_anticipo());
            txtComentario.setText(anticipo.getComentarios_anticipo());
            txtIdFactura.setText(String.valueOf(anticipo.getId_factura()));
            txtSaldo.setText(String.valueOf(anticipo.getSaldo_anticipo()));
            txtNIT.setText(anticipo.getNit_anticipo());
            double ant = Double.parseDouble(txtTotal.getText());
            double sal = Double.parseDouble(txtSaldo.getText());
            double tot = ant + sal;
            txtCalculoTotal.setText(String.valueOf(tot));
            txtFechaEntregaAnticipo.setText(anticipo.getFecha_entrega_persona_aticipo());
        } catch (Exception Error) {
            agregarLog(Error.toString());
        }
        
        btnGuardar.setEnabled(false);
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
     
        
        calcularTotal();
        
        anticipo.setNombre_persona_anticipo(txtNombre.getText().toUpperCase());
        anticipo.setTelefono_anticipo(txtTelefonos.getText());
        anticipo.setFecha_entrega_producto_anticipo(ConvertirFecha(calFechaEntrega));
        anticipo.setDetalle_anticipo(txtDetalle.getText());
        anticipo.setTotal_anticipo(Double.parseDouble(txtTotal.getText()));
        anticipo.setComentarios_anticipo(txtComentario.getText());
        anticipo.setEstado_anticipo(cbxEstado.getSelectedItem().toString());
        anticipo.setUsuario_registro_anticipo(Inventory.lblUsuario.getText());
        anticipo.setId_factura(Integer.parseInt(txtIdFactura.getText().isEmpty() ? "0" : txtIdFactura.getText()));
        anticipo.setSaldo_anticipo(Double.parseDouble(txtSaldo.getText().isEmpty() ? "0.0" : txtSaldo.getText()));
        anticipo.setId_anticipo(Integer.valueOf(txtIdAnticipo.getText()));
        anticipo.setNit_anticipo(txtNIT.getText());
        anticipo.setUsuario_modifico_anticipo(Inventory.lblUsuario.getText());

        try {
            String valor = acceso.actualizarAnticipo(anticipo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            mensaje.manipulacionExcepciones("informacion", valor, "Modificar Anticipo");
            if(cbxEstado.getSelectedItem().toString().equals("Despachado") || cbxEstado.getSelectedItem().toString().equals("Devuelto")) {
                if(valor.equals("Operacion realizada con exito.")) {
                    String valor_dato = acceso.actualizarAnticipoDespachadoDevuelto(anticipo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                }
            }
        } catch (Exception Error) {
            agregarLog(Error.toString());
        }
        
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnImpresionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpresionActionPerformed
        if(txtIdAnticipo.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "No se puede mostrar el reporte porque no esta guardado el registro.", "Reporte Anticipo");
        } else {
            imprimirReporte(cbxTamanio.getSelectedItem().toString());
        }
    }//GEN-LAST:event_btnImpresionActionPerformed

    private void txtNitLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitLostFocus
        if (txtNIT.getText().equals("c/f") || txtNIT.getText().equals("C/F") || txtNIT.getText().equals("")) {
            txtNIT.setText("C/F");
            txtNombre.setText("Consumidor Final");
        } else {
            obtenerDatosCliente(txtNIT.getText());
        }
    }//GEN-LAST:event_txtNitLostFocus

    private void obtenerDatosCliente(String pNit_persona){
        
        ArrayList<ObjetosPersona> BuscarPersona = new ArrayList();
        ArrayList<ObjetosCliente> BuscarCliente = new ArrayList();
        AccesoPersona NuevaPersona = new AccesoPersona();
        
        boolean persona_encontrada = false;

        //Consultar NIT de la persona
        try {
            BuscarPersona = NuevaPersona.buscarPersona(pNit_persona, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch (Exception error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Persona");
        }

        //Validar el resultado de la busqueda
        if (BuscarPersona.isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "No existen datos de esta Persona en la Base de Datos.", "Obtener Datos Cliente");
            persona_encontrada = false;
            txtNIT.setText("C/F");
            txtNombre.setText("Consumidor Final");
        } else {
            if (BuscarPersona.size() > 1) {
                mensaje.manipulacionExcepciones("critico", "Existe mas de una persona con este numero de NIT.", "Obtener Datos Cliente");
                persona_encontrada = false;
                txtNIT.setText("C/F");
                txtNombre.setText("Consumidor Final");
            } else {
                txtNombre.setText(BuscarPersona.get(0).getNom_persona());
                persona_encontrada = true;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImpresion;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private com.toedter.calendar.JDateChooser calFechaEntrega;
    private javax.swing.JComboBox cbxEstado;
    private javax.swing.JComboBox<String> cbxTamanio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JTextField txtCalculoTotal;
    private javax.swing.JTextField txtComentario;
    private javax.swing.JTextArea txtDetalle;
    private javax.swing.JTextField txtFechaEntregaAnticipo;
    private javax.swing.JTextField txtIdAnticipo;
    private javax.swing.JTextField txtIdFactura;
    private javax.swing.JTextField txtNIT;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtTelefonos;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}

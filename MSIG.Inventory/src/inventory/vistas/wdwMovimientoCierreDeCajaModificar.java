/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import inventory.acceso.AccesoCaja;
import inventory.acceso.AccesoExcepciones;
import inventory.acceso.AccesoInventario;
import inventory.acceso.AccesoSucursal;
import inventory.objetos.ObjetosSucursal;
import java.awt.Dimension;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JInternalFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import objetos.ObjetosCaja;

/**
 *
 * @author FERNANDO
 */
public class wdwMovimientoCierreDeCajaModificar extends javax.swing.JInternalFrame {

    AccesoExcepciones Mensaje = new AccesoExcepciones();
    private AccesoCaja ctrlModificar;
    private String id;
    
    public wdwMovimientoCierreDeCajaModificar(String id) {
        initComponents();
        this.setResizable(false);
        //this.setBounds(150, 150, 430, 550);
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x  = ((ancho / 2) - (this.getWidth() / 2));
        int y  = ((alto / 2) - (this.getHeight() / 2));
        this.setLocation(x, y);
        this.id = id;
        ctrlModificar = new AccesoCaja(this);
        ctrlModificar.cargarData(id);
        ctrlModificar.consultarSaldoFinal(this.txtFechaM.getText(),this.txtIdM.getText()); 
        ctrlModificar.guardarCierre(this);
        ctrlModificar.validarCierre(this, Inventory.lblRol.getText());
    }
    
    private boolean verificarFacturasPendientes() {
        boolean respuesta = false;
        int pendientes = ctrlModificar.consultarFacturasPendientes(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        if( pendientes > 0) {
            agregarLog("No se puede cerrar hay " + pendientes + " Facturas pendientes de cobrar.");
            respuesta = false;
        } else {
            respuesta = true;
        }
        return respuesta;
    }
    
    private String Log = "";
    
    private void agregarLog(String texto) {
        this.Log = this.Log + "CIERRE CAJA" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtHoraM = new javax.swing.JTextField();
        txtFechaM = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtUsuarioM = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCantidadM = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtIdM = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtEstado = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSaldoAperturado = new javax.swing.JTextField();
        txtTotalVentas = new javax.swing.JTextField();
        txtSaldoCierre = new javax.swing.JTextField();
        btnCerrarCaja = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTotalCredito = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalAbonos = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtTotalGastos = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTotalDevoluciones = new javax.swing.JTextField();
        txtTotalAnticipos = new javax.swing.JTextField();
        txtTotalAnticiposPositivos = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Editar Cierre de Caja");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCierreDeCaja.png"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "APERTURA DE CAJA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        txtHoraM.setBackground(new java.awt.Color(102, 102, 102));
        txtHoraM.setEditable(false);
        txtHoraM.setForeground(new java.awt.Color(255, 255, 255));

        txtFechaM.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaM.setEditable(false);
        txtFechaM.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Usuario");

        txtUsuarioM.setBackground(new java.awt.Color(102, 102, 102));
        txtUsuarioM.setEditable(false);
        txtUsuarioM.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Cantidad");

        txtCantidadM.setBackground(new java.awt.Color(255, 255, 204));

        jLabel5.setText("ID Cierre");

        txtIdM.setBackground(new java.awt.Color(102, 102, 102));
        txtIdM.setEditable(false);
        txtIdM.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Fecha");

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnModificar.setText("Guardar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        jLabel2.setText("Hora");

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminar.setText("eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        txtEstado.setBackground(new java.awt.Color(102, 102, 102));
        txtEstado.setEditable(false);
        txtEstado.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("Estado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdM, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addComponent(txtFechaM)
                            .addComponent(txtUsuarioM, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCantidadM, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(txtHoraM)
                            .addComponent(txtEstado))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(txtFechaM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHoraM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUsuarioM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCantidadM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(btnEliminar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(txtIdM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(btnModificar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "GASTOS E INGRESOS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jLabel8.setText("Apertura");

        jLabel9.setText("Efectivo");

        jLabel10.setText("Total");

        txtSaldoAperturado.setBackground(new java.awt.Color(102, 102, 102));
        txtSaldoAperturado.setEditable(false);
        txtSaldoAperturado.setForeground(new java.awt.Color(255, 255, 255));
        txtSaldoAperturado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtTotalVentas.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalVentas.setEditable(false);
        txtTotalVentas.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalVentas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtSaldoCierre.setBackground(new java.awt.Color(102, 102, 102));
        txtSaldoCierre.setEditable(false);
        txtSaldoCierre.setForeground(new java.awt.Color(255, 255, 255));
        txtSaldoCierre.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btnCerrarCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCerrarCaja.png"))); // NOI18N
        btnCerrarCaja.setText("Cerrar");
        btnCerrarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarCajaActionPerformed(evt);
            }
        });

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        jLabel6.setText("Creditos");

        txtTotalCredito.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalCredito.setEditable(false);
        txtTotalCredito.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalCredito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setText("Abonos");

        txtTotalAbonos.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalAbonos.setEditable(false);
        txtTotalAbonos.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalAbonos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel13.setText("Gastos");

        txtTotalGastos.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalGastos.setEditable(false);
        txtTotalGastos.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalGastos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setText("Anticipos (-)");

        jLabel15.setText("Devoluciones");

        txtTotalDevoluciones.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalDevoluciones.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalDevoluciones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtTotalAnticipos.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalAnticipos.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalAnticipos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtTotalAnticiposPositivos.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalAnticiposPositivos.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalAnticiposPositivos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel16.setText("Anticipo (+)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSaldoAperturado, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSaldoCierre, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTotalAbonos)
                                    .addComponent(txtTotalGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnReporte)
                                .addGap(18, 18, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCerrarCaja, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(txtTotalDevoluciones)
                            .addComponent(txtTotalAnticipos)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalAnticiposPositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalAnticiposPositivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSaldoAperturado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addComponent(txtTotalCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtTotalAnticipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(txtTotalAbonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtTotalDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSaldoCierre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel13)
                        .addComponent(txtTotalGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnReporte)
                    .addComponent(btnCerrarCaja))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        ctrlModificar.habilitarCampos();
        ctrlModificar.habilitarEdicion();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        ctrlModificar.actualizarApertura(this);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
       ctrlModificar.eliminar(this.txtIdM.getText());
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarCajaActionPerformed
        if (verificarFacturasPendientes()) {
            ctrlModificar.cerrar_caja(this, Inventory.lblRol.getText());
            ctrlModificar.guardarFechaHoraCierre(this, Inventory.lblRol.getText());
        } else {
            Mensaje.manipulacionExcepciones("informacion", "Hay facturas pendientes de cobrar, por favor verifique.", "Facturas Pendientes");
        }
    }//GEN-LAST:event_btnCerrarCajaActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
       
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptCierreDeCaja.jasper");
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
            parametro.put("P_USUARIO", "Impreso Por: " + Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_CIERRE", txtIdM.getText());
            parametro.put("P_FECHA",txtFechaM.getText());
            parametro.put("P_HORA", txtHoraM.getText());
            parametro.put("P_SALDO_INICIAL", txtSaldoAperturado.getText());
            parametro.put("P_SALDO_VENTAS", txtTotalVentas.getText());
            parametro.put("P_SALDO_TOTAL", txtSaldoCierre.getText());
            
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
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Compra");
        }

    }//GEN-LAST:event_btnReporteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCerrarCaja;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnModificar;
    private javax.swing.JButton btnReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JTextField txtCantidadM;
    public javax.swing.JTextField txtEstado;
    public javax.swing.JTextField txtFechaM;
    public javax.swing.JTextField txtHoraM;
    public javax.swing.JTextField txtIdM;
    public javax.swing.JTextField txtSaldoAperturado;
    public javax.swing.JTextField txtSaldoCierre;
    public javax.swing.JTextField txtTotalAbonos;
    public javax.swing.JTextField txtTotalAnticipos;
    public javax.swing.JTextField txtTotalAnticiposPositivos;
    public javax.swing.JTextField txtTotalCredito;
    public javax.swing.JTextField txtTotalDevoluciones;
    public javax.swing.JTextField txtTotalGastos;
    public javax.swing.JTextField txtTotalVentas;
    public javax.swing.JTextField txtUsuarioM;
    // End of variables declaration//GEN-END:variables
}

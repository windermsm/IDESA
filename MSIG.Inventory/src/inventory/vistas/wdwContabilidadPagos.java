/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import com.toedter.calendar.JDateChooser;
import inventory.acceso.AccesoExcepciones;
import inventory.acceso.AccesoPago;
import inventory.librerias.WindowController;
import inventory.objetos.ObjetosPago;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class wdwContabilidadPagos extends javax.swing.JInternalFrame {

    private WindowController ventana = new WindowController();
    
    AccesoExcepciones Mensaje = new AccesoExcepciones();
    AccesoPago acceso = new AccesoPago();
    ObjetosPago objeto = new ObjetosPago();

    public wdwContabilidadPagos() {
        initComponents();
        mostrarRegistros();
    }

    private void guardarRegistro() {
        objeto.setNumero_factura_pago(txtNoFactura.getText());
        objeto.setSerie_factura_pago(txtSerie.getText());
        objeto.setNumero_doc_pago(txtNoDocumento.getText());
        objeto.setTipo_doc_pago(cbxTipoDocumento.getSelectedItem().toString());
        objeto.setFecha_doc_pago(ConvetirFecha(txtFecha));
        objeto.setMonto_doc_pago(Float.parseFloat(txtMonto.getText()));
        objeto.setCuenta_pago(cbxCuenta.getSelectedItem().toString());
        objeto.setMotivo_pago(txtMotivo.getText());
        objeto.setEstado_pago(cbxEstado.getSelectedItem().toString());
        objeto.setTipo_pago(cbxTipoPago.getSelectedItem().toString());
        acceso.insertarPago(objeto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
    }

    private void buscarRegistros() {

        int fila = 0;
        DefaultTableModel tabla = new DefaultTableModel();
        ArrayList<ObjetosPago> lista = new ArrayList();

        lista = acceso.buscarPago(txtBuscar.getText().toUpperCase(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

        tabla.setRowCount(lista.size());
        tabla.addColumn("ID Pago");
        tabla.addColumn("No. Factura");
        tabla.addColumn("Serie");
        tabla.addColumn("No. Documento");
        tabla.addColumn("Tipo Pago");
        tabla.addColumn("Fecha Documento");
        tabla.addColumn("Monto");
        tabla.addColumn("Cuenta");
        tabla.addColumn("Motivo");
        tabla.addColumn("Fecha Grabacion");
        tabla.addColumn("Estado");


        for (ObjetosPago x : lista) {
            tabla.setValueAt(x.getId_pago(), fila, 0);
            tabla.setValueAt(x.getNumero_factura_pago(), fila, 1);
            tabla.setValueAt(x.getSerie_factura_pago(), fila, 2);
            tabla.setValueAt(x.getNumero_doc_pago(), fila, 3);
            tabla.setValueAt(x.getTipo_doc_pago(), fila, 4);
            tabla.setValueAt(x.getFecha_doc_pago(), fila, 5);
            tabla.setValueAt(x.getMonto_doc_pago(), fila, 6);
            tabla.setValueAt(x.getCuenta_pago(), fila, 7);
            tabla.setValueAt(x.getMotivo_pago(), fila, 8);
            tabla.setValueAt(x.getFecha_pago(), fila, 9);
            tabla.setValueAt(x.getEstado_pago(), fila, 10);
            fila++;
        }

        tblPago.setModel(tabla);

    }

    private void limpiarCampos() {
        btnGuardar.setEnabled(true);
        txtIdPago.setText("");
        txtNoFactura.setText("");
        txtSerie.setText("");
        txtNoDocumento.setText("0");
        txtFecha.setDate(null);
        txtMonto.setText("");
        txtMotivo.setText("");
    }
    
    public Date CovertirDate(String valor) {
        Date fecha = null;
        try {
            if (valor == null || valor.equals("null")) {
                fecha = null;
            } else {
                fecha = new SimpleDateFormat("yyyy-MM-dd").parse(valor);
            }
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al convertir fecha. \n" + Error.getMessage(), "Convertir Fecha");
        }
        return fecha;
    }
    
    private String ConvetirFecha(JDateChooser calendario) {
        String fecha = "";
        try {
            fecha = ( calendario.getDate().getYear() + 1900 ) + "-" + ( calendario.getDate().getMonth() + 1 ) + "-" + calendario.getDate().getDate();
        } catch(Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "No se pudo convertir la fecha seleccionada.", "Convertir Fecha");
        }
        return fecha;
    }

    private void mostrarRegistros() {

        int fila = 0;
        DefaultTableModel tabla = new DefaultTableModel();
        ArrayList<ObjetosPago> lista = new ArrayList();

        lista = acceso.listarPago(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

        tabla.setRowCount(lista.size());
        tabla.addColumn("ID Pago");
        tabla.addColumn("Factura");
        tabla.addColumn("Serie");
        tabla.addColumn("No. Documento");
        tabla.addColumn("Tipo Pago");
        tabla.addColumn("Fecha Documento");
        tabla.addColumn("Monto");
        tabla.addColumn("Cuenta");
        tabla.addColumn("Motivo");
        tabla.addColumn("Fecha Grabacion");
        tabla.addColumn("Estado");


        for (ObjetosPago x : lista) {
            tabla.setValueAt(x.getId_pago(), fila, 0);
            tabla.setValueAt(x.getNumero_factura_pago(), fila, 1);
            tabla.setValueAt(x.getSerie_factura_pago(), fila, 2);
            tabla.setValueAt(x.getNumero_doc_pago(), fila, 3);
            tabla.setValueAt(x.getTipo_doc_pago(), fila, 4);
            tabla.setValueAt(x.getFecha_doc_pago(), fila, 5);
            tabla.setValueAt(x.getMonto_doc_pago(), fila, 6);
            tabla.setValueAt(x.getCuenta_pago(), fila, 7);
            tabla.setValueAt(x.getMotivo_pago(), fila, 8);
            tabla.setValueAt(x.getFecha_pago(), fila, 9);
            tabla.setValueAt(x.getEstado_pago(), fila, 10);
            fila++;
        }

        tblPago.setModel(tabla);
        
    }

    private void actualizarRegistro() {
        objeto.setId_pago(Integer.parseInt(txtIdPago.getText()));
        objeto.setNumero_factura_pago(txtNoFactura.getText());
        objeto.setSerie_factura_pago(txtSerie.getText());
        objeto.setNumero_doc_pago(txtNoDocumento.getText());
        objeto.setTipo_doc_pago(cbxTipoDocumento.getSelectedItem().toString());
        objeto.setFecha_doc_pago(ConvetirFecha(txtFecha));
        objeto.setMonto_doc_pago(Float.parseFloat(txtMonto.getText()));
        objeto.setCuenta_pago(cbxCuenta.getSelectedItem().toString());
        objeto.setMotivo_pago(txtMotivo.getText());
        objeto.setEstado_pago(cbxEstado.getSelectedItem().toString());
        objeto.setTipo_pago(cbxTipoPago.getSelectedItem().toString());
        acceso.actualizarPago(objeto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
    }
    
    private boolean verificarRegistros() {
        boolean grabar = true;
        if(txtNoFactura.getText().isEmpty()){ grabar = false; }
        if(txtSerie.getText().isEmpty()){ grabar = false; }
        if(txtNoDocumento.getText().isEmpty()){ grabar = false; }
        if(txtMonto.getText().isEmpty()){ grabar = false; }
        if(txtFecha.getDate()==null){ grabar = false; }
        if(txtMotivo.getText().isEmpty()){ grabar = false; }
        return grabar;
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
        lblNombreUsuario = new javax.swing.JLabel();
        txtIdPago = new javax.swing.JTextField();
        lblContraseniaUsuario = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JComboBox();
        txtNoFactura = new javax.swing.JFormattedTextField();
        lblContraseniaUsuario1 = new javax.swing.JLabel();
        txtNoDocumento = new javax.swing.JFormattedTextField();
        lblContraseniaUsuario2 = new javax.swing.JLabel();
        cbxTipoDocumento = new javax.swing.JComboBox();
        lblNombreUsuario1 = new javax.swing.JLabel();
        lblContraseniaUsuario3 = new javax.swing.JLabel();
        lblContraseniaUsuario4 = new javax.swing.JLabel();
        cbxCuenta = new javax.swing.JComboBox();
        lblNombreUsuario2 = new javax.swing.JLabel();
        txtMotivo = new javax.swing.JTextField();
        txtMonto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbxTipoPago = new javax.swing.JComboBox();
        txtFecha = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPago = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gastos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgContabilidadPagos.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(900, 500));
        setMinimumSize(new java.awt.Dimension(900, 500));
        setPreferredSize(new java.awt.Dimension(900, 500));

        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNombreUsuario.setText("ID Gasto");
        lblNombreUsuario.setToolTipText("");

        txtIdPago.setBackground(new java.awt.Color(102, 102, 102));
        txtIdPago.setForeground(new java.awt.Color(255, 255, 255));
        txtIdPago.setToolTipText("Ingrese la descripcion del producto");

        lblContraseniaUsuario.setText("Serie");

        txtSerie.setBackground(new java.awt.Color(255, 255, 204));
        txtSerie.setToolTipText("Ingrese el precio de compra, no puede ingresar letras");

        jLabel1.setText("Estado");

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Inactivo" }));

        txtNoFactura.setBackground(new java.awt.Color(255, 255, 204));
        txtNoFactura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        lblContraseniaUsuario1.setText("Tipo");

        txtNoDocumento.setBackground(new java.awt.Color(255, 255, 204));
        txtNoDocumento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNoDocumento.setText("0");
        txtNoDocumento.setToolTipText("");

        lblContraseniaUsuario2.setText("No.");

        cbxTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cheque", "Deposito", "Efectivo", "Recibo", "Nota Credito", "Transferencia" }));
        cbxTipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoDocumentoActionPerformed(evt);
            }
        });

        lblNombreUsuario1.setText("Fecha");
        lblNombreUsuario1.setToolTipText("");

        lblContraseniaUsuario3.setText("Monto");

        lblContraseniaUsuario4.setText("Cuenta");

        cbxCuenta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alquiler", "Aranceles", "Electricidad", "Equipo de Computo", "Gasolina", "Internet", "Impuestos", "Otros", "Proveedores", "Papeleria y Utiles", "Rembolso", "Repuestos", "Servicio Tecnico", "Servicio Profecional", "Suministros", "Taller", "Telefonia", "Transporte", "Viaticos" }));

        lblNombreUsuario2.setText("Motivo");
        lblNombreUsuario2.setToolTipText("");

        txtMotivo.setBackground(new java.awt.Color(255, 255, 204));
        txtMotivo.setToolTipText("Ingrese el precio de compra, no puede ingresar letras");

        txtMonto.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("Tipo");

        cbxTipoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrativo", "Operativo" }));

        jLabel3.setText("Numero");

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreUsuario)
                    .addComponent(lblNombreUsuario1)
                    .addComponent(lblNombreUsuario2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdPago)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContraseniaUsuario)
                            .addComponent(lblContraseniaUsuario3))
                        .addGap(18, 18, 18)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSerie, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(txtMonto))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNoFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                    .addComponent(txtMotivo))
                .addGap(18, 18, 18)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lblContraseniaUsuario4)
                    .addComponent(lblContraseniaUsuario1))
                .addGap(29, 29, 29)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(cbxEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbxCuenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(cbxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblContraseniaUsuario2)
                        .addGap(18, 18, 18)
                        .addComponent(txtNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreUsuario)
                    .addComponent(txtIdPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContraseniaUsuario)
                    .addComponent(txtNoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContraseniaUsuario2)
                    .addComponent(cbxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContraseniaUsuario1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNombreUsuario1)
                        .addComponent(lblContraseniaUsuario3)
                        .addComponent(lblContraseniaUsuario4)
                        .addComponent(cbxCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreUsuario2)
                    .addComponent(jLabel1)
                    .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cbxTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtSerie.getAccessibleContext().setAccessibleName("txtPrecioCompra");

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblBuscar.setText("Buscar");

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonNuevo.png"))); // NOI18N
        btnLimpiar.setText("Nuevo");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscar.setActionCommand("buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        txtBuscar.setToolTipText("Ingrese el nombre del producto que desea buscar");

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBuscar)
                        .addComponent(btnLimpiar)
                        .addComponent(btnModificar)
                        .addComponent(btnGuardar)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblPago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPago.getTableHeader().setReorderingAllowed(false);
        tblPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPagoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPago);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if(verificarRegistros()==true){
            guardarRegistro();
            mostrarRegistros();
            limpiarCampos();
        } else {
            Mensaje.manipulacionExcepciones("critico", "Porfavor llene todos los campos.", "Boton Guardar");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarRegistros();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tblPagoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPagoMouseClicked
        btnGuardar.setEnabled(false);
        txtIdPago.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 0)));
        txtNoFactura.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 1)));
        txtSerie.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 2)));
        txtNoDocumento.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 3)));
        cbxTipoDocumento.setSelectedItem(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 4)));
        txtFecha.setDate(CovertirDate(tblPago.getValueAt(tblPago.getSelectedRow(), 5).toString()));
        txtMonto.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 6)));
        cbxCuenta.setSelectedItem(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 7)));
        txtMotivo.setText(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 8)));
        cbxEstado.setSelectedItem(String.valueOf(tblPago.getValueAt(tblPago.getSelectedRow(), 10)));
    }//GEN-LAST:event_tblPagoMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        actualizarRegistro();
        mostrarRegistros();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void cbxTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoDocumentoActionPerformed
        if(cbxTipoDocumento.getSelectedItem().toString().equals("Efectivo")) {
            txtNoDocumento.setBackground(new Color(102,102,102));
            txtNoDocumento.setForeground(Color.WHITE);
            txtNoDocumento.setText("0");
        } else {
            txtNoDocumento.setBackground(new Color(255, 255, 204));
            txtNoDocumento.setForeground(Color.BLACK);
            txtNoDocumento.setText("");
        }
    }//GEN-LAST:event_cbxTipoDocumentoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        this.dispose();
        wdwContabilidadPagos Pagos = new wdwContabilidadPagos();
        ventana.abrirPantalla(Pagos);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox cbxCuenta;
    private javax.swing.JComboBox cbxEstado;
    private javax.swing.JComboBox cbxTipoDocumento;
    private javax.swing.JComboBox cbxTipoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblContraseniaUsuario;
    private javax.swing.JLabel lblContraseniaUsuario1;
    private javax.swing.JLabel lblContraseniaUsuario2;
    private javax.swing.JLabel lblContraseniaUsuario3;
    private javax.swing.JLabel lblContraseniaUsuario4;
    private javax.swing.JLabel lblNombreUsuario;
    private javax.swing.JLabel lblNombreUsuario1;
    private javax.swing.JLabel lblNombreUsuario2;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JTable tblPago;
    private javax.swing.JTextField txtBuscar;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIdPago;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtMotivo;
    private javax.swing.JFormattedTextField txtNoDocumento;
    private javax.swing.JFormattedTextField txtNoFactura;
    private javax.swing.JTextField txtSerie;
    // End of variables declaration//GEN-END:variables
}

package inventory.vistas;

import inventory.acceso.*;
import inventory.librerias.NormalCell;
import inventory.objetos.ObjetosPersona;
import inventory.objetos.ObjetosProducto;
import inventory.objetos.ObjetosProveedor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class wdwConsultaProductos extends javax.swing.JInternalFrame {

    //VARIABLES GLOBALES
    AccesoExcepciones Mensaje = new AccesoExcepciones();
    ObjetosProducto Producto = new ObjetosProducto();
    AccesoProducto NuevoProducto = new AccesoProducto();
    
    //VARIABLES PUBLICAR PARA ENVIAR EL ID DEL PRODUCTO
    //SELECCIONADO A LA PATALLA DE VENTAS
    public int fila_seleccionada = 0;
    public int columan_seleccionada = 0;
    private String id_producto = null;
    private String pantalla = ""; 
   
    
    /**
     * Creates new form wdwCatalogoProductos
     */
    public wdwConsultaProductos(String opcion) {
        initComponents();
        mostrarRegistrosGuardados();
        tblProducto.setDefaultRenderer(Object.class, new NormalCell());
        this.pantalla = opcion;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        pnlBotones = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnAgregarEquipo = new javax.swing.JButton();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecioNormal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPrecioMinimo = new javax.swing.JTextField();
        btnMostrar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtPrecioEspecial = new javax.swing.JTextField();

        jCheckBox1.setText("jCheckBox1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Consulta");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoConsulta.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(750, 350));
        setMinimumSize(new java.awt.Dimension(750, 350));
        setPreferredSize(new java.awt.Dimension(750, 350));

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblBuscar.setText("Buscar");

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonLimpiar.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
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
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                keyPressedEnter(evt);
            }
        });

        btnAgregarEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonAgregar.png"))); // NOI18N
        btnAgregarEquipo.setEnabled(false);
        btnAgregarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEquipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarEquipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiar)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAgregarEquipo)
                        .addComponent(btnLimpiar))
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnBuscar)
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBuscar)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProducto.getTableHeader().setReorderingAllowed(false);
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("ID Producto");

        txtIdProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtIdProducto.setEditable(false);
        txtIdProducto.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Descripcion");

        txtDescripcionProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtDescripcionProducto.setEditable(false);
        txtDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Cantidad");

        txtCantidadProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtCantidadProducto.setEditable(false);
        txtCantidadProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtCantidadProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                CantidadLostFocus(evt);
            }
        });

        jLabel4.setText("P. Normal");

        txtPrecioNormal.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioNormal.setEditable(false);
        txtPrecioNormal.setForeground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("P. Minimo");

        txtPrecioMinimo.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioMinimo.setEditable(false);
        txtPrecioMinimo.setForeground(new java.awt.Color(255, 255, 255));

        btnMostrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMostrarImagenes.png"))); // NOI18N
        btnMostrar.setText("Imagenes");
        btnMostrar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        jLabel6.setText("P. Especial");

        txtPrecioEspecial.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioEspecial.setEditable(false);
        txtPrecioEspecial.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCantidadProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(txtIdProducto))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtPrecioNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(txtPrecioMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(txtDescripcionProducto))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrecioEspecial))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtPrecioNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtPrecioMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtPrecioEspecial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarEnRegistrosGuardados();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        if (String.valueOf(tblProducto.getSelectedColumn()).equals("0")) {
            txtIdProducto.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 0).toString());
            txtDescripcionProducto.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 2).toString());
            txtCantidadProducto.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 5).toString());
            txtPrecioNormal.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 7).toString());
            txtPrecioMinimo.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 8).toString());
            txtPrecioEspecial.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 9).toString());
        }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void CantidadLostFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CantidadLostFocus
        if(Float.parseFloat(txtCantidadProducto.getText()) > Float.parseFloat(tblProducto.getValueAt(tblProducto.getSelectedRow(), 5).toString())){
            Mensaje.manipulacionExcepciones("critico", "La cantidad ingresada excede la existencia.", "Cantidad");
        }
    }//GEN-LAST:event_CantidadLostFocus

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        wdwMostrarImagenes pantalla = new wdwMostrarImagenes();
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int x  = (ancho / 2) - (pantalla.getWidth() / 2);
        int y  = (alto / 2) -  (pantalla.getHeight() / 2) / 2;
        pantalla.setLocation(x, y);
        pantalla .setVisible(true);
        Inventory.pnlPrincipal.add(pantalla);
        pantalla .toFront();
        
        //cargar los id's de las imagenes asociadas al producto seleccionado
        ArrayList<String> lista = new ArrayList();
        AccesoImagen acceso = new AccesoImagen();
        String id = txtIdProducto.getText();
        String pro = txtDescripcionProducto.getText();
        wdwMostrarImagenes.txtIdProducto.setText(id);
        wdwMostrarImagenes.txtDescripcionProducto.setText(pro);
        lista = acceso.listarImagenes(id, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        for(String c : lista){
            wdwMostrarImagenes.cbxIdProducto.addItem(c);
        }
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void keyPressedEnter(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyPressedEnter
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            buscarEnRegistrosGuardados();
        }
    }//GEN-LAST:event_keyPressedEnter

    private void btnAgregarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEquipoActionPerformed
        if("Catalogo Series".equals(this.pantalla)) {
            wdwCatalogoSerie.txtIdProducto.setText(txtIdProducto.getText());
            wdwCatalogoSerie.txtDescripcionProducto.setText(txtDescripcionProducto.getText());
        }
        if("Orden de Servicio".equals(this.pantalla)) {
            wdwCatalogoSerieConsulta.txtIdProducto.setText(txtIdProducto.getText());
            wdwCatalogoSerieConsulta.txtDescripcionProducto.setText(txtDescripcionProducto.getText());
        }
        if("Orden de Servicio Detalle".equals(this.pantalla)) {
            wdwMovimientoOrdenServicioDetalle.txtIdProducto.setText(txtIdProducto.getText());
            wdwMovimientoOrdenServicioDetalle.txtDescripcionProductoBuscar.setText(txtDescripcionProducto.getText());
        }
        if("Historial Ordenes".equals(this.pantalla)) {
            wdwBaseDeDAtosHistorialOrdenesDeServicio.txtIdProducto.setText(txtIdProducto.getText());
            wdwBaseDeDAtosHistorialOrdenesDeServicio.txtDescripcionProducto.setText(txtDescripcionProducto.getText());
        }
        if("Catalogo Bonificaciones".equals(this.pantalla)) {
            wdwCatalogoBonificacion.txtIdProducto.setText(txtIdProducto.getText());
            wdwCatalogoBonificacion.txtDescripcionProducto.setText(txtDescripcionProducto.getText());
        }
        this.dispose();
    }//GEN-LAST:event_btnAgregarEquipoActionPerformed

    private void mostrarRegistrosGuardados(){
        

        DefaultTableModel tabla = new DefaultTableModel();
        AccesoProducto Producto = new AccesoProducto();
        AccesoProveedor Proveedor = new AccesoProveedor();
        AccesoPersona Persona = new AccesoPersona();
        
        ArrayList<ObjetosProducto> listaProductos = new ArrayList();
        ArrayList<ObjetosProveedor> listaProveedores = new ArrayList();
        ArrayList<ObjetosPersona> listaPersonas = new ArrayList();
        
        try{
            listaProductos = Producto.seleccionarProductoVisible(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            listaProveedores = Proveedor.seleccionarProveedor(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            listaPersonas = Persona.seleccionarPersona(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Error error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Seleccionar Producto - Seleccionar Proveedor - Seleccionar Persona");
        }
        
        //MOSTRAR LA INFORMACION DE LOS PRODUCTOS EN LA TABLA DE LA VISTA
        if(listaProductos.isEmpty()){
            Mensaje.manipulacionExcepciones("critico", "No existen datos almacenados de productos.", "Mostrar Registros Guardados");
        } else {
            
            tabla.addColumn("ID Producto");
            tabla.addColumn("Marca");
            tabla.addColumn("Descripcion");
            tabla.addColumn("Linea/Familia");
            tabla.addColumn("Proveedor");
            tabla.addColumn("Existencias");
            tabla.addColumn("Unidad de Medida");
            tabla.addColumn("Precio Estandar");
            tabla.addColumn("Precio Minimo");
            tabla.addColumn("Precio Especial");
            tabla.addColumn("Codigo Fabricante");
            tabla.addColumn("Descuento");
            
            tabla.setRowCount(listaProductos.size());
            int cProducto = 0;
            
            // RECORRO LA LISTA DE PRODUCTOS
            for(ObjetosProducto xProducto:listaProductos ){
                
                // POR CADA PRODUCTO RECORRO LA LISTA DE PROVEEDORES
                for(ObjetosProveedor xProveedor:listaProveedores){
                    
                    // VERIFICO SI EL ID DEL PROVEEDOR EN PRODUCTO ES EL MISMO QUE EN PROVEEDORES
                    if(xProducto.getId_proveedor() == xProveedor.getId_proveedor()){
                        
                        // SOLO SI COINCIDE EL ID PROVEEDOR ARRIBA RECORRO  LA LISTA DE PERSONAS
                        for(ObjetosPersona xPersona:listaPersonas){
                            
                            // SI EL ID PERSONA ES EL MISMO EN PERSONA QUE EN PROVEEDOR PINTO LOS DATOS
                            if(xProveedor.getId_persona() == xPersona.getId_persona()){
                                tabla.setValueAt(xProducto.getId_producto(), cProducto, 0);
                                tabla.setValueAt(xProducto.getMarca_producto(), cProducto, 1);
                                tabla.setValueAt(xProducto.getDesc_producto(), cProducto, 2);
                                tabla.setValueAt(xProducto.getLinea_producto(), cProducto, 3);
                                tabla.setValueAt(xPersona.getNom_persona(), cProducto, 4);
                                tabla.setValueAt(xProducto.getExi_producto(), cProducto, 5);   
                                tabla.setValueAt(xProducto.getUnidad_medida_producto(), cProducto, 6);
                                tabla.setValueAt(xProducto.getPrecio_est_producto(), cProducto, 7);
                                tabla.setValueAt(xProducto.getPrecio_min_producto(), cProducto, 8);
                                tabla.setValueAt(xProducto.getPrecio_especial_producto(), cProducto, 9);
                                tabla.setValueAt(xProducto.getCodigo_fabricante(), cProducto, 10);
                                tabla.setValueAt(xProducto.getDescuento_producto(), cProducto, 11);
                                cProducto++;
                            }
                        }
                    }
                }
            }
            
            tblProducto.setModel(tabla);
            tblProducto.getColumnModel().getColumn(2).setPreferredWidth(200);
        }
    }
    
    private void limpiarCampos(){
        txtBuscar.setText("");
        txtCantidadProducto.setText("");
        txtDescripcionProducto.setText("");
        txtIdProducto.setText("");
        txtPrecioMinimo.setText("");
        txtPrecioNormal.setText("");
    }
    
    private void totalProforma(){
        float total_proforma = 0;
        for(int cFilas = 0; cFilas <= 199; cFilas++) {
            try{
                total_proforma = total_proforma + (float) wdwMovimientoProforma.tblDetalleProforma.getValueAt(cFilas, 3) * (float) wdwMovimientoProforma.tblDetalleProforma.getValueAt(cFilas, 4);
            } catch(Exception error) {
                total_proforma = total_proforma + 0;
            }
        }
        wdwMovimientoProforma.txtTotalProforma.setText(String.valueOf(Math.rint(total_proforma * 100)/100));
    }
    
    private void buscarEnRegistrosGuardados(){
        
        // CAPTURO EL DATO A BUSCAR
        String busqueda = txtBuscar.getText().toUpperCase();
        
        if(busqueda != null){
            DefaultTableModel tabla = new DefaultTableModel();
            
            AccesoProducto Producto = new AccesoProducto();
            AccesoProveedor Proveedor = new AccesoProveedor();
            AccesoPersona Persona = new AccesoPersona();

            ArrayList<ObjetosProducto> listaProductos = new ArrayList();
            ArrayList<ObjetosProveedor> listaProveedores = new ArrayList();
            ArrayList<ObjetosPersona> listaPersonas = new ArrayList();

            //VERIFICAR ALGUN ERROR AL CARGAR LA INFORMACION DE LOS PRODUCTOS
            try{
                
                listaProductos = Producto.seleccionarProducto(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), Inventory.lblRol.getText());
                listaProveedores = Proveedor.seleccionarProveedor(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                listaPersonas = Persona.seleccionarPersona(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            }catch(Error error){
                Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Seleccionar Producto - Seleccionar Proveedor - Seleccionar Persona");
            }

            //MOSTRAR LA INFORMACION DE LOS PRODUCTOS EN LA TABLA DE LA VISTA
            if(listaProductos.isEmpty()){
                Mensaje.manipulacionExcepciones("critico", "No existen datos almacenados de productos.", "Buscar Registros Guardados");
            } else {
                
                tabla.addColumn("ID Producto");
                tabla.addColumn("Marca");
                tabla.addColumn("Descripcion");
                tabla.addColumn("Linea/Familia");
                tabla.addColumn("Proveedor");
                tabla.addColumn("Existencias");
                tabla.addColumn("Unidad de Medida");
                tabla.addColumn("Precio Estandar");
                tabla.addColumn("Precio Minimo");
                tabla.addColumn("Precio Especial");
                tabla.addColumn("Codigo Fabricante");

                int cProducto = 0;
                int contadorFilas = 1;

                // RECORRO LA LISTA DE PRODUCTOS
                for (ObjetosProducto xProducto : listaProductos) {

                    // POR CADA PRODUCTO RECORRO LA LISTA DE PROVEEDORES
                    for (ObjetosProveedor xProveedor : listaProveedores) {

                        // VERIFICO SI EL ID DEL PROVEEDOR EN PRODUCTO ES EL MISMO QUE EN PROVEEDORES
                        if (xProducto.getId_proveedor() == xProveedor.getId_proveedor()) {

                            // SOLO SI COINCIDE LE ID PROVEEDOR ARRIBA RECORRO LA LISTA DE PERSONAS
                            for (ObjetosPersona xPersona : listaPersonas) {

                                // SI EL ID PERSONA ES EL MISMO EN PERSONA QUE EN PROVEEDOR COMPARO CON EL STRING BUSCADO
                                if (xProveedor.getId_persona() == xPersona.getId_persona()) {
                                   
                                    // SI EXISTE ALGUNA COINCIDENCIA CON EL STRING BUSCADO PINTO LA TUPLA EN EL DETALLE
                                    // SEPARO LA CADENA PARA BUSCAR CADA PALABRA INGRESADA Y BUSCO POR CADA TOKEN
                                    String[] tokens = busqueda.split("%");
                                    int cuenta_coincidencias = tokens.length;
                                    cuenta_coincidencias = 0;
                                    
                                    for(int x = 0; x < tokens.length; x++) {

                                        if (xProducto.getId_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getMarca_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getDesc_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xPersona.getNom_persona().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getLinea_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getCodigo_fabricante().toUpperCase().indexOf(tokens[x]) >= 0) {

                                            cuenta_coincidencias++;

                                        } //fin de la validacion de la palabra

                                    } //fin del while que recorre los tokens
                                    
                                    if (cuenta_coincidencias == tokens.length) {
                                        // ESTABLECE EL TAMAÑO DE LA TABLA
                                        tabla.setRowCount(contadorFilas);
                                        tabla.setValueAt(xProducto.getId_producto(), cProducto, 0);
                                        tabla.setValueAt(xProducto.getMarca_producto(), cProducto, 1);
                                        tabla.setValueAt(xProducto.getDesc_producto(), cProducto, 2);
                                        tabla.setValueAt(xProducto.getLinea_producto(), cProducto, 3);
                                        tabla.setValueAt(xPersona.getNom_persona(), cProducto, 4);
                                        tabla.setValueAt(xProducto.getExi_producto(), cProducto, 5);
                                        tabla.setValueAt(xProducto.getUnidad_medida_producto(), cProducto, 6);
                                        tabla.setValueAt(xProducto.getPrecio_est_producto(), cProducto, 7);
                                        tabla.setValueAt(xProducto.getPrecio_min_producto(), cProducto, 8);
                                        tabla.setValueAt(xProducto.getPrecio_especial_producto(), cProducto, 9);
                                        tabla.setValueAt(xProducto.getCodigo_fabricante(), cProducto, 10);
                                        cProducto++;
                                        contadorFilas++;
                                    } else {
                                        System.out.println("No se tomo en cuenta " + cuenta_coincidencias + " de " + tokens.length);
                                    }
                                }
                            }
                        }
                    }
                }

                tblProducto.setModel(tabla);
                //CAMBIAR DE TAMAÑO LA TERCERA COLUMNA 'DESCRIPCION'
                tblProducto.getColumnModel().getColumn(2).setPreferredWidth(200);
            }
        }else{
            // SI NO HAY CARACTER DE BUSQUEDA SE DESPLIEGAN TODOS LOS DATOS
            mostrarRegistrosGuardados();
        } 
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAgregarEquipo;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtPrecioEspecial;
    private javax.swing.JTextField txtPrecioMinimo;
    private javax.swing.JTextField txtPrecioNormal;
    // End of variables declaration//GEN-END:variables
}

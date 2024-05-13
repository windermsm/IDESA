package inventory.vistas;

import com.toedter.calendar.JDateChooser;
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

public class wdwReportesMovimientoDeProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form wdwReportesVentasPorFecha
     */
    public wdwReportesMovimientoDeProductos() {
        initComponents();
    }
    
    public String ConvertirFecha(JDateChooser Calendario) {
        if ( Calendario.getDate() == null ) {
            return null;
        } else {
        return ( Calendario.getDate().getYear() + 1900 ) + "-" + ( Calendario.getDate().getMonth() + 1 ) + "-" + Calendario.getDate().getDate();
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

        pnlTitulo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlFormato = new javax.swing.JPanel();
        lblFormato = new javax.swing.JLabel();
        pnlParametros = new javax.swing.JPanel();
        lblFechaInicial = new javax.swing.JLabel();
        lblFechaFinal = new javax.swing.JLabel();
        btnGenerarReporte = new javax.swing.JButton();
        txtFechaInicial = new com.toedter.calendar.JDateChooser();
        txtFechaFinal = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setForeground(java.awt.Color.white);
        setIconifiable(true);
        setResizable(true);
        setTitle("Reporte");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 450));
        setPreferredSize(new java.awt.Dimension(300, 450));

        pnlTitulo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTitulo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Movimiento Producto");

        javax.swing.GroupLayout pnlTituloLayout = new javax.swing.GroupLayout(pnlTitulo);
        pnlTitulo.setLayout(pnlTituloLayout);
        pnlTituloLayout.setHorizontalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTituloLayout.setVerticalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFormato.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblFormato.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormato.setText("Hoja Oficio 8.50 x 13.00 plgs");

        javax.swing.GroupLayout pnlFormatoLayout = new javax.swing.GroupLayout(pnlFormato);
        pnlFormato.setLayout(pnlFormatoLayout);
        pnlFormatoLayout.setHorizontalGroup(
            pnlFormatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormatoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFormato, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFormatoLayout.setVerticalGroup(
            pnlFormatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormatoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFormato)
                .addContainerGap())
        );

        pnlParametros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblFechaInicial.setText("Fecha Inicial");

        lblFechaFinal.setText("Fecha Final");
        lblFechaFinal.setToolTipText("Ingrese la Fecha Final");

        btnGenerarReporte.setText("Generar Reporte");
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlParametrosLayout = new javax.swing.GroupLayout(pnlParametros);
        pnlParametros.setLayout(pnlParametrosLayout);
        pnlParametrosLayout.setHorizontalGroup(
            pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParametrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(pnlParametrosLayout.createSequentialGroup()
                        .addGroup(pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaInicial)
                            .addComponent(lblFechaFinal))
                        .addGap(18, 18, 18)
                        .addGroup(pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlParametrosLayout.setVerticalGroup(
            pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParametrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFechaInicial)
                    .addComponent(txtFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFinal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(btnGenerarReporte)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlFormato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlParametros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlParametros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFormato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        
       AccesoExcepciones Mensaje = new AccesoExcepciones();

        try {
            
            AccesoInventario Acceso = new AccesoInventario();
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptMovimientoInventario.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            
            //ENVIAR EL PARAMETRO AL REPORTES
            HashMap parametro = new HashMap();
            parametro.put("P_FECHA_INICIAL", ConvertirFecha(txtFechaInicial));
            parametro.put("P_FECHA_FINAL", ConvertirFecha(txtFechaFinal));
            
            //parametros generales del encabezado
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();

            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //parametros del reporte
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_SUCURSAL", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", "Impreso por " + Inventory.lblUsuario.getText().toUpperCase());
            parametro.put("P_INFORMACION", "Del " + ConvertirFecha(txtFechaInicial) + " al " + ConvertirFecha(txtFechaFinal));
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, Acceso.conectar());
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
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte MOVIMIENTO INVENTARIO");
        }
        
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JLabel lblFechaFinal;
    private javax.swing.JLabel lblFechaInicial;
    private javax.swing.JLabel lblFormato;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFormato;
    private javax.swing.JPanel pnlParametros;
    private javax.swing.JPanel pnlTitulo;
    private com.toedter.calendar.JDateChooser txtFechaFinal;
    private com.toedter.calendar.JDateChooser txtFechaInicial;
    // End of variables declaration//GEN-END:variables
}

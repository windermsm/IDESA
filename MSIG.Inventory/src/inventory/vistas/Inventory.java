package inventory.vistas;

import inventory.acceso.*;
import inventory.librerias.ImagenFondo;
import inventory.librerias.WindowController;
import inventory.objetos.ObjetosSucursal;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

public class Inventory extends javax.swing.JFrame {

    private static String archivo_fondo = "";
    private WindowController ventana = new WindowController();
    private AccesoExcepciones mensaje = new AccesoExcepciones();
    
    public Inventory() {
        
        try {
            
            initComponents();
            
            pnlLog.setVisible(false);

            try {    
                pnlPrincipal.setBorder(new ImagenFondo("/inventory/imagenes/imgFondoPantalla.png"));
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                //Ocultar todos los accesos a las pantallas
                menu.setVisible(false);
                btnAccesoProforma.setVisible(false);
                btnAccesoOrdenDeServicio.setVisible(false);
                btnAccesoConsultaProductos.setVisible(false);
                btnAccesoVentaDeProductos.setVisible(false);
                btnFacturaCliente.setVisible(false);
                btnFacturasProveedor.setVisible(false);
                btnGarantiaCliente.setVisible(false);
                btnGarantiaProveedor.setVisible(false);
                btnReparaciones.setVisible(false);
                btnAnticipos.setVisible(false);
                btnActualizar.setVisible(false);
            } catch (IOException ex) {
                System.out.println("Ocurrio un error al inciar sistema");
            }

            //Mostrar la patanlla para el ingreso del usuario
            wdwIngresoDeUsuario Ingreso = new wdwIngresoDeUsuario();
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x = ((ancho - 30) - Ingreso.getWidth()) / 2;
            int y = ((alto - 90) - Ingreso.getHeight()) / 2;
            pnlPrincipal.add(Ingreso);
            Ingreso.setLocation(x, y);
            Ingreso.setVisible(true);
            Ingreso.toFront();
            Ingreso.setSelected(true);
            
        } catch (PropertyVetoException ex) {
            System.out.println("Ocurrio un error al solicitar usuario");
        }
        
        //Consultar el valor de las notificaciones
        consultaNotificaciones();
        
    }
    
    public void consultaNotificaciones() {
        AccesoNotificacionEscritorio notificacion = new AccesoNotificacionEscritorio();
        try {
            btnAnticipos.setText(notificacion.anticiposPendientes("Inventory", "0"));
            btnFacturaCliente.setText(notificacion.facturasClientePendientes("Inventory", "0"));
            btnFacturasProveedor.setText(notificacion.facturasProveedorPendientes("Inventory", "0"));
            btnReparaciones.setText(notificacion.reparacionesPendientes("Inventory", "0"));
            btnGarantiaCliente.setText(notificacion.garantiaClientePendientes("Inventory", "0"));
            btnGarantiaProveedor.setText(notificacion.garantiaProveedorPendientes("Inventory", "0"));
        } catch(Exception Error) {
            agregarLog("Error al consultar las notificaciones. \nError: " + Error.toString());
        }
    }
    
    public static void definirFondoUsuario() {
        try {
            AccesoUsuario acceso_usuario = new AccesoUsuario();
            archivo_fondo = acceso_usuario.retornaFondoUsuario(lblUsuario.getText(), "Inventory");
            pnlPrincipal.setBorder(new ImagenFondo(archivo_fondo));
            pnlPrincipal.updateUI();
        } catch (IOException ex) {
           System.out.println("Error al definir fondo de usuario");
        }
    }
    
    public void abrirReporte(String nombreReporte) {
        //ejecución del repote seleccionado
        try {

            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();

            URL url_reporte = this.getClass().getResource("/inventory/reportes/" + nombreReporte + ".jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();

            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            //parametros del reporte
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_SUCURSAL", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO","Impreso por " + Inventory.lblUsuario.getText().toUpperCase());
            parametro.put("P_INFORMACION", " ");

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
            int x = (ancho - ventana.getWidth()) / 2;
            int y = (alto - ventana.getHeight() - 100) / 2;

            Inventory.pnlPrincipal.add(ventana);

            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();

        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte de Factura Proveedor Vencidas");
        }
    }
    
    private String Log = "";
    
    private void agregarLog(String texto) {
        this.Log = this.Log + "INVENTORY" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }

    @Override
    public Image getIconImage() {
        Image icono = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("inventory/imagenes/imgLogoSistema.png"));
        return icono;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        pnlPrincipal = new javax.swing.JDesktopPane();
        btnAccesoProforma = new javax.swing.JButton();
        btnAccesoOrdenDeServicio = new javax.swing.JButton();
        btnAccesoConsultaProductos = new javax.swing.JButton();
        btnAccesoVentaDeProductos = new javax.swing.JButton();
        pnlLog = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        btnActualizar = new javax.swing.JButton();
        btnFacturaCliente = new javax.swing.JButton();
        btnFacturasProveedor = new javax.swing.JButton();
        btnReparaciones = new javax.swing.JButton();
        btnGarantiaCliente = new javax.swing.JButton();
        btnGarantiaProveedor = new javax.swing.JButton();
        btnAnticipos = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblSucursal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTerminal = new javax.swing.JLabel();
        menu = new javax.swing.JMenuBar();
        menuUsuario = new javax.swing.JMenu();
        menuFondoDeEscritorio = new javax.swing.JMenuItem();
        menuCambioDeClave = new javax.swing.JMenuItem();
        menuMovimiento = new javax.swing.JMenu();
        menuMovimientoCompras = new javax.swing.JMenu();
        menuAbonoProveedores = new javax.swing.JMenuItem();
        menuCompraDeProductos = new javax.swing.JMenuItem();
        menuRetencionProveedor = new javax.swing.JMenuItem();
        menuMovimientoInventario = new javax.swing.JMenu();
        menuAjusteInventario = new javax.swing.JMenuItem();
        menuIntercambiarSerie = new javax.swing.JMenuItem();
        menuTraslado = new javax.swing.JMenuItem();
        menuMovimientoGarantia = new javax.swing.JMenu();
        menuMovimientoGarantiaCliente = new javax.swing.JMenuItem();
        menuMovimientoGarantiaProveedor = new javax.swing.JMenuItem();
        menuMovimientoVentas = new javax.swing.JMenu();
        menuAbonoDeClientes = new javax.swing.JMenuItem();
        menuAnulacionVentas = new javax.swing.JMenuItem();
        menuAnticipo = new javax.swing.JMenuItem();
        menuCierreDeCaja = new javax.swing.JMenuItem();
        menuProforma = new javax.swing.JMenuItem();
        menuVentasActivas = new javax.swing.JMenuItem();
        menuVentaDeProductos = new javax.swing.JMenuItem();
        menuMovimientoTaller = new javax.swing.JMenu();
        menuOrdenDeServicio = new javax.swing.JMenuItem();
        menuCatalogo = new javax.swing.JMenu();
        menuCategorias = new javax.swing.JMenuItem();
        menuMarcas = new javax.swing.JMenuItem();
        menuProductos = new javax.swing.JMenuItem();
        menuFallas = new javax.swing.JMenuItem();
        menuSeries = new javax.swing.JMenuItem();
        menuConsultaProductos = new javax.swing.JMenuItem();
        sepMenuCatalogoUno = new javax.swing.JPopupMenu.Separator();
        menuSucursales = new javax.swing.JMenuItem();
        menuEmpleados = new javax.swing.JMenuItem();
        menuUsuarios = new javax.swing.JMenuItem();
        menuBonificaciones = new javax.swing.JMenuItem();
        sepMenuCatalogoDos = new javax.swing.JPopupMenu.Separator();
        menuClientes = new javax.swing.JMenuItem();
        menuProveedores = new javax.swing.JMenuItem();
        menuContabilidad = new javax.swing.JMenu();
        menuSalario = new javax.swing.JMenuItem();
        menuContabilidadEgresoPago = new javax.swing.JMenuItem();
        menuContabilidadMobiliarioEquipo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuContabilidadReportes = new javax.swing.JMenu();
        menuContabilidadReportePagosFecha = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        menuComisiones = new javax.swing.JMenu();
        menuReportesComisionesSucursal = new javax.swing.JMenuItem();
        menuReportesComisionesSucursal1 = new javax.swing.JMenuItem();
        menuReportesComisionesSucursal3 = new javax.swing.JMenuItem();
        menuReportesBonificacionesPorEmpleado = new javax.swing.JMenuItem();
        menuCompras = new javax.swing.JMenu();
        menuReportesComprasPorFecha = new javax.swing.JMenuItem();
        menuReportesDetalleVenta1 = new javax.swing.JMenuItem();
        menuReportesDetalleAbonosProveedor = new javax.swing.JMenuItem();
        menuVentas = new javax.swing.JMenu();
        menuReportesAbonosDetalle = new javax.swing.JMenuItem();
        menuReportesVentasPorFechaFEL = new javax.swing.JMenuItem();
        menuReportesVentasPorFechaSinFEL = new javax.swing.JMenuItem();
        menuReportesVentasPorFecha = new javax.swing.JMenuItem();
        menuReportesDetalleVenta = new javax.swing.JMenuItem();
        menuReportesDetalleGanancia = new javax.swing.JMenuItem();
        menuReportesComisionesSucursal2 = new javax.swing.JMenuItem();
        menuInventario = new javax.swing.JMenu();
        menuInventarioImpreso = new javax.swing.JMenuItem();
        menuReportesMovimientoProducto = new javax.swing.JMenuItem();
        menuExistenciaPorLinea = new javax.swing.JMenuItem();
        menuAjusteInventarioReporte = new javax.swing.JMenuItem();
        menuTopProductos = new javax.swing.JMenuItem();
        menuTopGanancia = new javax.swing.JMenuItem();
        menuProductosAlMinimo = new javax.swing.JMenuItem();
        menuProductosEnNegativo = new javax.swing.JMenuItem();
        menuTrazabilidadSerie = new javax.swing.JMenuItem();
        menuBaseDeDatos = new javax.swing.JMenu();
        menuExportarCatalogoDeProductos = new javax.swing.JMenuItem();
        menuImportarCatalogoDeProductos = new javax.swing.JMenuItem();
        sepExportar = new javax.swing.JPopupMenu.Separator();
        menuBaseDeDatosHistorialAnticipos = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialDeCompras = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialDeVentas = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialDeProforma = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialTraslado = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialOrdenes = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialGarantiaCliente = new javax.swing.JMenuItem();
        menuBaseDeDatosHistorialGarantiaProveedor = new javax.swing.JMenuItem();
        sepMenuBaseDeDatos = new javax.swing.JPopupMenu.Separator();
        menuBaseDeDatosMantenimiento = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        menuManualDeUsuario = new javax.swing.JMenuItem();
        menuAcercaDe = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory 3.0");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(getIconImage());
        setName("LaEconomicaInventario"); // NOI18N

        btnAccesoProforma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAccesoMovimientoProforma.png"))); // NOI18N
        btnAccesoProforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoProformaActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnAccesoProforma);
        btnAccesoProforma.setBounds(10, 370, 80, 80);

        btnAccesoOrdenDeServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAccesoMovimientoOrdenDeServicio.png"))); // NOI18N
        btnAccesoOrdenDeServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoOrdenDeServicioActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnAccesoOrdenDeServicio);
        btnAccesoOrdenDeServicio.setBounds(10, 550, 80, 80);

        btnAccesoConsultaProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAccesoConsultaProductos.png"))); // NOI18N
        btnAccesoConsultaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoConsultaProductosActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnAccesoConsultaProductos);
        btnAccesoConsultaProductos.setBounds(10, 280, 80, 80);

        btnAccesoVentaDeProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAccesoMovimientoVentaDeProductos.png"))); // NOI18N
        btnAccesoVentaDeProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoVentaDeProductosActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnAccesoVentaDeProductos);
        btnAccesoVentaDeProductos.setBounds(10, 460, 80, 80);

        jLabel7.setText("LOG");

        txtLog.setBackground(new java.awt.Color(0, 0, 0));
        txtLog.setColumns(20);
        txtLog.setEditable(false);
        txtLog.setForeground(new java.awt.Color(0, 255, 102));
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        javax.swing.GroupLayout pnlLogLayout = new javax.swing.GroupLayout(pnlLog);
        pnlLog.setLayout(pnlLogLayout);
        pnlLogLayout.setHorizontalGroup(
            pnlLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addGroup(pnlLogLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlLogLayout.setVerticalGroup(
            pnlLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlPrincipal.add(pnlLog);
        pnlLog.setBounds(100, 280, 530, 350);

        btnActualizar.setText("Actualizar");
        btnActualizar.setToolTipText("");
        btnActualizar.setActionCommand("FPV 2");
        btnActualizar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnActualizar);
        btnActualizar.setBounds(10, 60, 80, 23);

        btnFacturaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoVentaDeProductos.png"))); // NOI18N
        btnFacturaCliente.setText("00");
        btnFacturaCliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnFacturaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturaClienteActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnFacturaCliente);
        btnFacturaCliente.setBounds(10, 240, 80, 25);

        btnFacturasProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoCompraDeProductos.png"))); // NOI18N
        btnFacturasProveedor.setText("00");
        btnFacturasProveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnFacturasProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturasProveedorActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnFacturasProveedor);
        btnFacturasProveedor.setBounds(10, 210, 80, 25);

        btnReparaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovmientoOrdenDeServicio.png"))); // NOI18N
        btnReparaciones.setText("00");
        btnReparaciones.setActionCommand("FPV 2");
        btnReparaciones.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReparaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReparacionesActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnReparaciones);
        btnReparaciones.setBounds(10, 180, 80, 25);

        btnGarantiaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoGarantiaCliente.png"))); // NOI18N
        btnGarantiaCliente.setText("00");
        btnGarantiaCliente.setActionCommand("FPV 2");
        btnGarantiaCliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGarantiaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGarantiaClienteActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnGarantiaCliente);
        btnGarantiaCliente.setBounds(10, 150, 80, 25);

        btnGarantiaProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoGarantiaProveedor.png"))); // NOI18N
        btnGarantiaProveedor.setText("00");
        btnGarantiaProveedor.setActionCommand("FPV 2");
        btnGarantiaProveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGarantiaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGarantiaProveedorActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnGarantiaProveedor);
        btnGarantiaProveedor.setBounds(10, 120, 80, 25);

        btnAnticipos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAnticipos.png"))); // NOI18N
        btnAnticipos.setText("00");
        btnAnticipos.setActionCommand("FPV 2");
        btnAnticipos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAnticipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnticiposActionPerformed(evt);
            }
        });
        pnlPrincipal.add(btnAnticipos);
        btnAnticipos.setBounds(10, 90, 80, 25);

        jLabel1.setText("jLabel1");

        lblUsuario.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Usuario: ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Rol:");

        lblRol.setText("Rol");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Nombre:");

        lblNombre.setText("Nombre");

        lblSucursal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSucursal.setText("Sucursal");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Sucursal: ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Terminal:");

        lblTerminal.setText("Terminal");

        menuUsuario.setText("Usuario");

        menuFondoDeEscritorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgUsuarioFondo.png"))); // NOI18N
        menuFondoDeEscritorio.setText("Fondo de Escruitorio");
        menuFondoDeEscritorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFondoDeEscritorioActionPerformed(evt);
            }
        });
        menuUsuario.add(menuFondoDeEscritorio);

        menuCambioDeClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMenuCambiarClave.png"))); // NOI18N
        menuCambioDeClave.setText("Cambiar Clave Usuario");
        menuCambioDeClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCambioDeClaveActionPerformed(evt);
            }
        });
        menuUsuario.add(menuCambioDeClave);

        menu.add(menuUsuario);

        menuMovimiento.setText("Movimiento");
        menuMovimiento.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Movimiento </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Aqui se registran todos los movimientos de inventario,"
            + " como ventas, compras, cobros, etc."
            + "</td></tr> <table> </html>");

        menuMovimientoCompras.setText("Compras");

        menuAbonoProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAbonoProveedores.png"))); // NOI18N
        menuAbonoProveedores.setText("Abono a Proveedores");
        menuAbonoProveedores.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Abono a Proveedores</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Esta pantalla se utiliza para ingresar los Abonos correspondientes a las Facturas a Proveedores que hayan sido almacenadas al Crédito."
            + "</td></tr> <table> </html>");
        menuAbonoProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbonoProveedoresActionPerformed(evt);
            }
        });
        menuMovimientoCompras.add(menuAbonoProveedores);

        menuCompraDeProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoCompraDeProductos.png"))); // NOI18N
        menuCompraDeProductos.setText("Compra de Productos");
        menuCompraDeProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Compra de Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Aqui se ingresan las facturas de compra para actualizar el inventario de productos. "
            + "</td></tr> <table> </html>");
        menuCompraDeProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCompraDeProductosActionPerformed(evt);
            }
        });
        menuMovimientoCompras.add(menuCompraDeProductos);

        menuRetencionProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imbMovimientoRetencionProveedor.png"))); // NOI18N
        menuRetencionProveedor.setText("Retención Proveedor");
        menuRetencionProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRetencionProveedorActionPerformed(evt);
            }
        });
        menuMovimientoCompras.add(menuRetencionProveedor);

        menuMovimiento.add(menuMovimientoCompras);

        menuMovimientoInventario.setText("Inventario");

        menuAjusteInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAjusteInventario.png"))); // NOI18N
        menuAjusteInventario.setText("Ajuste Inventario");
        menuAjusteInventario.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Abono de Clientes </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de abonos de ventas al credito a clientes."
            + "</td></tr> <table> </html>");
        menuAjusteInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAjusteInventarioActionPerformed(evt);
            }
        });
        menuMovimientoInventario.add(menuAjusteInventario);

        menuIntercambiarSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoIntercambiarSerie.png"))); // NOI18N
        menuIntercambiarSerie.setText("Intercambiar Serie");
        menuIntercambiarSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIntercambiarSerieActionPerformed(evt);
            }
        });
        menuMovimientoInventario.add(menuIntercambiarSerie);

        menuTraslado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaTrasladoDeProducto.png"))); // NOI18N
        menuTraslado.setText("Traslado de Productos");
        menuTraslado.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Traslado de Productos</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Tralado de Productos a otra Sucursal a precio costo."
            + "</td></tr> <table> </html>");
        menuTraslado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrasladoActionPerformed(evt);
            }
        });
        menuMovimientoInventario.add(menuTraslado);

        menuMovimiento.add(menuMovimientoInventario);

        menuMovimientoGarantia.setText("Garantia");

        menuMovimientoGarantiaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoGarantiaCliente.png"))); // NOI18N
        menuMovimientoGarantiaCliente.setText("Garantia Cliente");
        menuMovimientoGarantiaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMovimientoGarantiaClienteActionPerformed(evt);
            }
        });
        menuMovimientoGarantia.add(menuMovimientoGarantiaCliente);

        menuMovimientoGarantiaProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoGarantiaProveedor.png"))); // NOI18N
        menuMovimientoGarantiaProveedor.setText("Garantia Proveedor");
        menuMovimientoGarantiaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMovimientoGarantiaProveedorActionPerformed(evt);
            }
        });
        menuMovimientoGarantia.add(menuMovimientoGarantiaProveedor);

        menuMovimiento.add(menuMovimientoGarantia);

        menuMovimientoVentas.setText("Ventas");

        menuAbonoDeClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAbonos.png"))); // NOI18N
        menuAbonoDeClientes.setText("Abono de Clientes");
        menuAbonoDeClientes.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Anular Venta </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Anular una venta y regresar todos los productos al inventario "
            + "</td></tr> <table> </html>");
        menuAbonoDeClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbonoDeClientesActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuAbonoDeClientes);

        menuAnulacionVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAnularVenta.png"))); // NOI18N
        menuAnulacionVentas.setText("Anular Venta");
        menuAnulacionVentas.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Anular Venta </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Anular una venta y regresar todos los productos al inventario "
            + "</td></tr> <table> </html>");
        menuAnulacionVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAnulacionVentasActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuAnulacionVentas);

        menuAnticipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoAnticipos.png"))); // NOI18N
        menuAnticipo.setText("Anticipos");
        menuAnticipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAnticipoActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuAnticipo);

        menuCierreDeCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/bntBotonEfectivo.png"))); // NOI18N
        menuCierreDeCaja.setText("Cierre De Caja");
        menuCierreDeCaja.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Cierre de Caja</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingresar el efectivo en la apertura y en cierre de Caja."
            + "</td></tr> <table> </html>");
        menuCierreDeCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCierreDeCajaActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuCierreDeCaja);

        menuProforma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoProforma.png"))); // NOI18N
        menuProforma.setText("Crear Proforma");
        menuProforma.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Crear Proforma </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion de proformas o cotizaciones si afectar invenario."
            + "</td></tr> <table> </html>");
        menuProforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProformaActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuProforma);

        menuVentasActivas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaVentasActivas.png"))); // NOI18N
        menuVentasActivas.setText("Ventas Activas");
        menuVentasActivas.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ventas Activas </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion de facturas y cobros de ventas emitadas en el mostrador."
            + "</td></tr> <table> </html>");
        menuVentasActivas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentasActivasActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuVentasActivas);

        menuVentaDeProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoVentaDeProductos.png"))); // NOI18N
        menuVentaDeProductos.setText("Venta de Productos");
        menuVentaDeProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Venta de Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Generacion de ventas al credito o contado de productos."
            + "</td></tr> <table> </html>");
        menuVentaDeProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentaDeProductosActionPerformed(evt);
            }
        });
        menuMovimientoVentas.add(menuVentaDeProductos);

        menuMovimiento.add(menuMovimientoVentas);

        menuMovimientoTaller.setText("Taller");

        menuOrdenDeServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovmientoOrdenDeServicio.png"))); // NOI18N
        menuOrdenDeServicio.setText("Orden de Servicio");
        menuOrdenDeServicio.setToolTipText("\"<html> <table width=\\\"200px\\\" height=\\\"75px\\\">\"\n+ \"<tr bgcolor=\\\"#FE642E\\\">\"\n+ \"<td font color=\\\"#FFFFFF\\\">\"\n+ \"<h2>Orde de Servicio</h2>\"\n+ \"</td>\"\n+ \"<img src=\\\"\" + this.getClass().getResource(\"/inventory/imagenes/imgTooltipMessage.png\") + \"\\\" />\"\n+ \"<td>\"\n+ \"</td\"\n+ \"</tr colspan=\\\"2\\\">\"\n+ \"<tr><td>\"\n+ \"Creación de Ordenes de Servicio para los equipos recibidos para reparación o revisión.\"\n+ \"</td></tr> <table> </html>\"\n");
        menuOrdenDeServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOrdenDeServicioActionPerformed(evt);
            }
        });
        menuMovimientoTaller.add(menuOrdenDeServicio);

        menuMovimiento.add(menuMovimientoTaller);

        menu.add(menuMovimiento);

        menuCatalogo.setText("Catalogo");
        menuCatalogo.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Catalogo </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Informacion necesaria para el correcto "
            + "funcionamiento del sistema."
            + "</td></tr> <table> </html>");

        menuCategorias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoCategorias.png"))); // NOI18N
        menuCategorias.setText("Categorias");
        menuCategorias.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Categorias</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Creación de categorias o tipos de producto. En algunos casos tambien llamado familias."
            + "</td></tr> <table> </html>");
        menuCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCategoriasActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuCategorias);

        menuMarcas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoMarca.png"))); // NOI18N
        menuMarcas.setText("Marcas");
        menuMarcas.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Marcas</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Se utiliza para crear las distintas marcas que existen en el Inventario."
            + "</td></tr> <table> </html>");
        menuMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMarcasActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuMarcas);

        menuProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoProductos.png"))); // NOI18N
        menuProductos.setText("Productos");
        menuProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Gestion del catalogo de productos, precios, costo, etc. "
            + "</td></tr> <table> </html>");
        menuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuProductos);

        menuFallas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoFalla.png"))); // NOI18N
        menuFallas.setText("Fallas");
        menuFallas.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Fallas </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Creación de tipos de Fallas para las Ordenes de Servicio."
            + "</td></tr> <table> </html>");
        menuFallas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFallasActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuFallas);

        menuSeries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCodigoDeBarraMini.png"))); // NOI18N
        menuSeries.setText("Series");
        menuSeries.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Serie </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Consulta las Series que existen dentro del Inventario."
            + "</td></tr> <table> </html>");
        menuSeries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSeriesActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuSeries);

        menuConsultaProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoConsulta.png"))); // NOI18N
        menuConsultaProductos.setText("Consulta");
        menuConsultaProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Consulta</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Consulta de precio y existencia para los vendedores. "
            + "</td></tr> <table> </html>");
        menuConsultaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConsultaProductosActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuConsultaProductos);
        menuCatalogo.add(sepMenuCatalogoUno);

        menuSucursales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoSucursales.png"))); // NOI18N
        menuSucursales.setText("Sucursales");
        menuSucursales.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Sucursales </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Informacion de las distintas sucursales. "
            + "</td></tr> <table> </html>");
        menuSucursales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSucursalesActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuSucursales);

        menuEmpleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoEmpleados.png"))); // NOI18N
        menuEmpleados.setText("Empleados");
        menuEmpleados.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Empleados </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Gestion de empleados, informacion y bonificaciones. "
            + "</td></tr> <table> </html>");
        menuEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEmpleadosActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuEmpleados);

        menuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalgoUsuarios.png"))); // NOI18N
        menuUsuarios.setText("Usuarios");
        menuUsuarios.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Usuarios </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Creacion de usuarios para ingreso al sistema."
            + "</td></tr> <table> </html>");
        menuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUsuariosActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuUsuarios);

        menuBonificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoBonificaciones.png"))); // NOI18N
        menuBonificaciones.setText("Bonificaciones");
        menuBonificaciones.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Bonificaciones </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Creacion de bonificaciones especiales por producto"
            + "</td></tr> <table> </html>");
        menuBonificaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBonificacionesActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuBonificaciones);
        menuCatalogo.add(sepMenuCatalogoDos);

        menuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoClientes.png"))); // NOI18N
        menuClientes.setText("Clientes");
        menuClientes.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Clientes </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Registro de nuevos clientes y gestion de dias de credito."
            + "</td></tr> <table> </html>");
        menuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientesActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuClientes);

        menuProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalgoProveedores.png"))); // NOI18N
        menuProveedores.setText("Proveedores");
        menuProveedores.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Proveedores </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Informacion de proveedores para la compra de productos."
            + "</td></tr> <table> </html>");
        menuProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProveedoresActionPerformed(evt);
            }
        });
        menuCatalogo.add(menuProveedores);

        menu.add(menuCatalogo);

        menuContabilidad.setText("Contabilidad");
        menuContabilidad.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Contabilidad </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de cuentas varias para generacion del"
            + "estado de resultados del negocio."
            + "</td></tr> <table> </html>");

        menuSalario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgContabilidadSalarios.png"))); // NOI18N
        menuSalario.setText("Planilla");
        menuSalario.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ingreso de Salarios </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de salarios pagados a los empleados por fecha."
            + "</td></tr> <table> </html>");
        menuSalario.setActionCommand("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ingreso de Salarios </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/tooltip.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de salarios pagados a empleados"
            + "</td></tr> <table> </html>");
        menuSalario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalarioActionPerformed(evt);
            }
        });
        menuContabilidad.add(menuSalario);

        menuContabilidadEgresoPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgContabilidadPagos.png"))); // NOI18N
        menuContabilidadEgresoPago.setText("Gastos");
        menuContabilidadEgresoPago.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ingreso de Pagos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de gastos administrativos por fecha y concepto para el estado de resultados. "
            + "</td></tr> <table> </html>");
        menuContabilidadEgresoPago.setActionCommand("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ingreso de Gastos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/tooltip.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Ingreso de gastos administrativos para el estado de resultados. "
            + "</td></tr> <table> </html>");
        menuContabilidadEgresoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuContabilidadEgresoPagoActionPerformed(evt);
            }
        });
        menuContabilidad.add(menuContabilidadEgresoPago);

        menuContabilidadMobiliarioEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgContabilidadEquipo.png"))); // NOI18N
        menuContabilidadMobiliarioEquipo.setText("Equipo");
        menuContabilidadMobiliarioEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuContabilidadMobiliarioEquipoActionPerformed(evt);
            }
        });
        menuContabilidad.add(menuContabilidadMobiliarioEquipo);
        menuContabilidad.add(jSeparator2);

        menuContabilidadReportes.setText("Reportes");

        menuContabilidadReportePagosFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuContabilidadReportePagosFecha.setText("Gastos por Fecha");
        menuContabilidadReportePagosFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuContabilidadReportePagosFechaActionPerformed(evt);
            }
        });
        menuContabilidadReportes.add(menuContabilidadReportePagosFecha);

        menuContabilidad.add(menuContabilidadReportes);

        menu.add(menuContabilidad);

        menuReportes.setText("Reportes");
        menuReportes.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Reportes </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion de reportes varios generados"
            + " atraves del ingreso de movimientos en"
            + " el inventario."
            + "</td></tr> <table> </html>");

        menuComisiones.setText("Comisiones");

        menuReportesComisionesSucursal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesComisionesSucursal.setText("Comisiones por Empleado");
        menuReportesComisionesSucursal.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Comisiones por Empleado </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Cantidad total de comisiones generadas"
            + " por cada empleado en un rango de fechas."
            + "</td></tr> <table> </html>");
        menuReportesComisionesSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesComisionesSucursalActionPerformed(evt);
            }
        });
        menuComisiones.add(menuReportesComisionesSucursal);

        menuReportesComisionesSucursal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesComisionesSucursal1.setText("Comisiones por Sucursal");
        menuReportesComisionesSucursal1.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Comisiones por Sucursal </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Total de las comisiones pagadas en la sucursal."
            + "</td></tr> <table> </html>");
        menuReportesComisionesSucursal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesComisionesSucursal1ActionPerformed(evt);
            }
        });
        menuComisiones.add(menuReportesComisionesSucursal1);

        menuReportesComisionesSucursal3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesComisionesSucursal3.setText("Comisiones sin Abonos");
        menuReportesComisionesSucursal3.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Comisiones sin Abonos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Cantidad total de comisiones generadas"
            + " por cada empleado en un rango de fechas, de"
            + " ventas al contado sin incluir abonos a"
            + " facturas pendientes de pago."
            + "</td></tr> <table> </html>");
        menuReportesComisionesSucursal3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesComisionesSucursal3ActionPerformed(evt);
            }
        });
        menuComisiones.add(menuReportesComisionesSucursal3);

        menuReportesBonificacionesPorEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesBonificacionesPorEmpleado.setText("Bonificaciones por Empleado");
        menuReportesBonificacionesPorEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesBonificacionesPorEmpleadoActionPerformed(evt);
            }
        });
        menuComisiones.add(menuReportesBonificacionesPorEmpleado);

        menuReportes.add(menuComisiones);

        menuCompras.setText("Compras");

        menuReportesComprasPorFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesComprasPorFecha.setText("Compras por Fecha");
        menuReportesComprasPorFecha.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Compras por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Monto total de facturas emitidas por proveedor, "
            + "ordenadas por fecha y nombre de proveedor."
            + "</td></tr> <table> </html>");
        menuReportesComprasPorFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesComprasPorFechaActionPerformed(evt);
            }
        });
        menuCompras.add(menuReportesComprasPorFecha);

        menuReportesDetalleVenta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesDetalleVenta1.setText("Detalle de Compras por Fecha");
        menuReportesDetalleVenta1.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Detalle de Compras por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra el total compras realizadas a cada "
            + "proveedor organizadas por nombre de proveedor "
            + " y fecha. Se detalla cada producto por numero de"
            + " factura con el su respectivo precio de compra."
            + "</td></tr> <table> </html>");
        menuReportesDetalleVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesDetalleVenta1ActionPerformed(evt);
            }
        });
        menuCompras.add(menuReportesDetalleVenta1);

        menuReportesDetalleAbonosProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesDetalleAbonosProveedor.setText("Detalle de Abonos a Proveedor");
        menuReportesDetalleAbonosProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesDetalleAbonosProveedorActionPerformed(evt);
            }
        });
        menuCompras.add(menuReportesDetalleAbonosProveedor);

        menuReportes.add(menuCompras);

        menuVentas.setText("Ventas");

        menuReportesAbonosDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesAbonosDetalle.setText("Abonos por Cliente");
        menuReportesAbonosDetalle.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Abonos por Cliente Detallado </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Historial de abonos realizados por cliente."
            + "</td></tr> <table> </html>");
        menuReportesAbonosDetalle.setActionCommand("Abonos por Cliente Detale");
        menuReportesAbonosDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesAbonosDetalleActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesAbonosDetalle);

        menuReportesVentasPorFechaFEL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesVentasPorFechaFEL.setText("Ventas FEL por Fecha");
        menuReportesVentasPorFechaFEL.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ventas por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Monto total de facturas emitidas a cada cliente"
            + " ordenadas por fecha y nombre de cliente."
            + "</td></tr> <table> </html>");
        menuReportesVentasPorFechaFEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesVentasPorFechaFELActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesVentasPorFechaFEL);

        menuReportesVentasPorFechaSinFEL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesVentasPorFechaSinFEL.setText("Ventas Sin Factura FEL");
        menuReportesVentasPorFechaSinFEL.setActionCommand("Ventas Sin Factura FEL");
        menuReportesVentasPorFechaSinFEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesVentasPorFechaSinFELActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesVentasPorFechaSinFEL);

        menuReportesVentasPorFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesVentasPorFecha.setText("Ventas por Fecha");
        menuReportesVentasPorFecha.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ventas por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Monto total de facturas emitidas a cada cliente"
            + " ordenadas por fecha y nombre de cliente."
            + "</td></tr> <table> </html>");
        menuReportesVentasPorFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesVentasPorFechaActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesVentasPorFecha);

        menuReportesDetalleVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesDetalleVenta.setText("Detalle de Ventas por Fecha");
        menuReportesDetalleVenta.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Detalle de Ventas por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra cada producto vendido por factura, el sub total"
            + " y el total de todas las ventas realizadas "
            + " en el rango de fecha ingresado."
            + "</td></tr> <table> </html>");
        menuReportesDetalleVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesDetalleVentaActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesDetalleVenta);

        menuReportesDetalleGanancia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesDetalleGanancia.setText("Detalle de Ganancias por Fecha");
        menuReportesDetalleGanancia.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Detalle de Ganancias por Fecha </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra la cantidad total de productos vendidos"
            + " y la ganancia alcanzada por cada producto, totalizado"
            + " mediante el rango de fecha ingresado."
            + "</td></tr> <table> </html>");
        menuReportesDetalleGanancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesDetalleGananciaActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesDetalleGanancia);

        menuReportesComisionesSucursal2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesComisionesSucursal2.setText("Ventas por Cliente");
        menuReportesComisionesSucursal2.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ventas por Cliente </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra el historico de ventas realizadas "
            + " a cada cliente y el estado en el que se "
            + " encuentra cada una de ellas, ordenadas por fecha."
            + "</td></tr> <table> </html>");
        menuReportesComisionesSucursal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesComisionesSucursal2ActionPerformed(evt);
            }
        });
        menuVentas.add(menuReportesComisionesSucursal2);

        menuReportes.add(menuVentas);

        menuInventario.setText("Inventario");

        menuInventarioImpreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuInventarioImpreso.setText("Inventario Impreso");
        menuInventarioImpreso.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Inventario Impreso </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion del inventario por proveedor, "
            + "muestra las existencias el costo del "
            + "producto total segun lo registrado en el "
            + "catalogo de productos."
            + "</td></tr> <table> </html>");
        menuInventarioImpreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInventarioImpresoActionPerformed(evt);
            }
        });
        menuInventario.add(menuInventarioImpreso);

        menuReportesMovimientoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuReportesMovimientoProducto.setText("Movimiento de Productos");
        menuReportesMovimientoProducto.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Movimiento de Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Cantidad comprada y vendida de productos"
            + " en un rango de fechas"
            + "</td></tr> <table> </html>");
        menuReportesMovimientoProducto.setActionCommand("Movimiento de los productos por fecha");
        menuReportesMovimientoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesMovimientoProductoActionPerformed(evt);
            }
        });
        menuInventario.add(menuReportesMovimientoProducto);

        menuExistenciaPorLinea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuExistenciaPorLinea.setText("Existencia por Linea");
        menuExistenciaPorLinea.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Existencia por Lineas </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion del inventario con una linea para ingresar cantidad actual."
            + "</td></tr> <table> </html>");
        menuExistenciaPorLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExistenciaPorLineaActionPerformed(evt);
            }
        });
        menuInventario.add(menuExistenciaPorLinea);

        menuAjusteInventarioReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuAjusteInventarioReporte.setText("Ajuste de Inventario");
        menuAjusteInventarioReporte.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Ajuste de Inventario </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion ajustes realizados al inventario. "
            + "</td></tr> <table> </html>");
        menuAjusteInventarioReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAjusteInventarioReporteActionPerformed(evt);
            }
        });
        menuInventario.add(menuAjusteInventarioReporte);

        menuTopProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuTopProductos.setText("Top Productos");
        menuTopProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Top Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion de los productos mas vendidos, "
            + "durante un periodo especifico."
            + "</td></tr> <table> </html>");
        menuTopProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTopProductosActionPerformed(evt);
            }
        });
        menuInventario.add(menuTopProductos);

        menuTopGanancia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuTopGanancia.setText("Top Ganancia");
        menuTopGanancia.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Top Ganancia</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion de los productos que generan ganancia, "
            + "durante un periodo especifico."
            + "</td></tr> <table> </html>");
        menuTopGanancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTopGananciaActionPerformed(evt);
            }
        });
        menuInventario.add(menuTopGanancia);

        menuProductosAlMinimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuProductosAlMinimo.setText("Productos Al Minimo");
        menuProductosAlMinimo.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Productos al Minimo </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Impresion del listado de productos que "
            + "se encuentran al minimo hasta el dia de hoy "
            + "este incluye los valores negativos. "
            + "</td></tr> <table> </html>");
        menuProductosAlMinimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosAlMinimoActionPerformed(evt);
            }
        });
        menuInventario.add(menuProductosAlMinimo);

        menuProductosEnNegativo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuProductosEnNegativo.setText("Productos En Negativo");
        menuProductosEnNegativo.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Productos en Negativo </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Listado de productos que deben ser "
            + "verificados, ya que al estar en negativo "
            + "tienen algun problema en el valor que "
            + "poseen en el campo existencia."
            + "</td></tr> <table> </html>");
        menuProductosEnNegativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosEnNegativoActionPerformed(evt);
            }
        });
        menuInventario.add(menuProductosEnNegativo);

        menuTrazabilidadSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        menuTrazabilidadSerie.setText("Trazabilidad Serie");
        menuTrazabilidadSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTrazabilidadSerieActionPerformed(evt);
            }
        });
        menuInventario.add(menuTrazabilidadSerie);

        menuReportes.add(menuInventario);

        menu.add(menuReportes);

        menuBaseDeDatos.setText("Base de Datos");
        menuBaseDeDatos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Base de Datos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Gestion de la informacion contnida en el sistema."
            + " Algunas opciones unicamente puede realizarlas "
            + " el desarrolador o usuario lider."
            + "</td></tr> <table> </html>");

        menuExportarCatalogoDeProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBaseDeDatosExportar.png"))); // NOI18N
        menuExportarCatalogoDeProductos.setText("Exportar Catalogo de Productos");
        menuExportarCatalogoDeProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Exportar Catalogo de Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Exporta en formato CSV el catalogo de productos."
            + "</td></tr> <table> </html>");
        menuExportarCatalogoDeProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExportarCatalogoDeProductosActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuExportarCatalogoDeProductos);

        menuImportarCatalogoDeProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBaseDeDatosImportar.png"))); // NOI18N
        menuImportarCatalogoDeProductos.setText("Importar Catalogo de Productos");
        menuImportarCatalogoDeProductos.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Importar Catalogo de Productos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Importa el catalogo de productos "
            + "desde un archivo CSV."
            + "</td></tr> <table> </html>");
        menuImportarCatalogoDeProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuImportarCatalogoDeProductosActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuImportarCatalogoDeProductos);
        menuBaseDeDatos.add(sepExportar);

        menuBaseDeDatosHistorialAnticipos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialAnticipos.png"))); // NOI18N
        menuBaseDeDatosHistorialAnticipos.setText("Historial de Anticipos");
        menuBaseDeDatosHistorialAnticipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialAnticiposActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialAnticipos);

        menuBaseDeDatosHistorialDeCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialCompras.png"))); // NOI18N
        menuBaseDeDatosHistorialDeCompras.setText("Historial de Compras de Productos");
        menuBaseDeDatosHistorialDeCompras.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Historial de Compras </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra el historial de compras en un rango "
            + "de fechas y permite visualizar las facturas "
            + "o comprobante de dichas compras."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosHistorialDeCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialDeComprasActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialDeCompras);

        menuBaseDeDatosHistorialDeVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialVentas.png"))); // NOI18N
        menuBaseDeDatosHistorialDeVentas.setText("Historial de Ventas de Productos");
        menuBaseDeDatosHistorialDeVentas.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Historial de Ventas </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra el historial de ventas en un rango "
            + "de fechas y permite visualizar las facturas "
            + "o comprobante de dichas ventas."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosHistorialDeVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialDeVentasActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialDeVentas);

        menuBaseDeDatosHistorialDeProforma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialProformas.png"))); // NOI18N
        menuBaseDeDatosHistorialDeProforma.setText("Historial de Proformas");
        menuBaseDeDatosHistorialDeProforma.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Historial de Proformas </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Buscar una proforma generada por Fecha."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosHistorialDeProforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialDeProformaActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialDeProforma);

        menuBaseDeDatosHistorialTraslado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/hitorialTraslados.png"))); // NOI18N
        menuBaseDeDatosHistorialTraslado.setText("HIstorial de Traslados");
        menuBaseDeDatosHistorialTraslado.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Historial de Traslados </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Buscar todos los traslados hechos por Fecha."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosHistorialTraslado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialTrasladoActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialTraslado);

        menuBaseDeDatosHistorialOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/hitorialOrdenes.png"))); // NOI18N
        menuBaseDeDatosHistorialOrdenes.setText("Historial de Ordenes de Servicio");
        menuBaseDeDatosHistorialOrdenes.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Historial de Ordenes de Servicio</h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra todos las ordenes de servicios por rango de Fechas."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosHistorialOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialOrdenesActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialOrdenes);

        menuBaseDeDatosHistorialGarantiaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialGarantiaCliente.png"))); // NOI18N
        menuBaseDeDatosHistorialGarantiaCliente.setText("Historial de Garantía Cliente");
        menuBaseDeDatosHistorialGarantiaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialGarantiaClienteActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialGarantiaCliente);

        menuBaseDeDatosHistorialGarantiaProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/historialGarantiaProveedor.png"))); // NOI18N
        menuBaseDeDatosHistorialGarantiaProveedor.setText("Historial de Garantía Proveedor");
        menuBaseDeDatosHistorialGarantiaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosHistorialGarantiaProveedorActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosHistorialGarantiaProveedor);
        menuBaseDeDatos.add(sepMenuBaseDeDatos);

        menuBaseDeDatosMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBaseDeDatosMantenimiento.gif"))); // NOI18N
        menuBaseDeDatosMantenimiento.setText("Mantenimiento Base de Datos");
        menuBaseDeDatosMantenimiento.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Mantenimiento Base de Datos </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Permite gestionar toda la informacion, "
            + "contenida en la base de datos y hacer "
            + "operacion de mantenimiento."
            + "</td></tr> <table> </html>");
        menuBaseDeDatosMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBaseDeDatosMantenimientoActionPerformed(evt);
            }
        });
        menuBaseDeDatos.add(menuBaseDeDatosMantenimiento);

        menu.add(menuBaseDeDatos);

        menuAyuda.setText("Ayuda");
        menuAyuda.setToolTipText("");

        menuManualDeUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgAyudaManual.png"))); // NOI18N
        menuManualDeUsuario.setText("Manual de Usuario");
        menuManualDeUsuario.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Manual de Usuario </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra el uso de cada opcion del sistema."
            + "</td></tr> <table> </html>");
        menuManualDeUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuManualDeUsuarioActionPerformed(evt);
            }
        });
        menuAyuda.add(menuManualDeUsuario);

        menuAcercaDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaAcercaDe.png"))); // NOI18N
        menuAcercaDe.setText("Acerca De Inventory");
        menuAcercaDe.setToolTipText("<html> <table width=\"200px\" height=\"75px\">"
            + "<tr bgcolor=\"#FE642E\">"
            + "<td font color=\"#FFFFFF\">"
            + "<h2> Acerca de Inventory </h2>"
            + "</td>"
            + "<img src=\"" + this.getClass().getResource("/inventory/imagenes/imgTooltipMessage.png") + "\" />"
            + "<td>"
            + "</td"
            + "</tr colspan=\"2\">"
            + "<tr><td>"
            + "Muestra informacion y version del sistema actual."
            + "</td></tr> <table> </html>");
        menuAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAcercaDeActionPerformed(evt);
            }
        });
        menuAyuda.add(menuAcercaDe);

        menu.add(menuAyuda);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlPrincipal))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRol, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSucursal, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(lblTerminal)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRol)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombre)
                    .addComponent(lblSucursal)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(lblTerminal))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientesActionPerformed
        wdwCatalogoClientes CatalogoClientes = new wdwCatalogoClientes();
        ventana.abrirPantalla(CatalogoClientes);       
    }//GEN-LAST:event_menuClientesActionPerformed

    private void menuProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProveedoresActionPerformed
        wdwCatalogoProveedores CatalogoProveedores = new wdwCatalogoProveedores();
        ventana.abrirPantalla(CatalogoProveedores);
    }//GEN-LAST:event_menuProveedoresActionPerformed

    private void menuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosActionPerformed
        wdwCatalogoProductos CatalogoProductos = new wdwCatalogoProductos();
        ventana.abrirPantalla(CatalogoProductos);
    }//GEN-LAST:event_menuProductosActionPerformed

    private void menuAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAcercaDeActionPerformed
        wdwAyudaAcercaDeInventario AcercaDeInventario = new wdwAyudaAcercaDeInventario();
        ventana.abrirPantalla(AcercaDeInventario);
    }//GEN-LAST:event_menuAcercaDeActionPerformed

    private void menuCompraDeProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCompraDeProductosActionPerformed
        wdwMovimientoCompraDeProductos CompraDeProductos = new wdwMovimientoCompraDeProductos();
        ventana.abrirPantalla(CompraDeProductos);
    }//GEN-LAST:event_menuCompraDeProductosActionPerformed

    private void menuVentaDeProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentaDeProductosActionPerformed
        wdwMovimientoVentaDeProductosNueva VentaDeProductos = new wdwMovimientoVentaDeProductosNueva();
        ventana.abrirPantalla(VentaDeProductos);
    }//GEN-LAST:event_menuVentaDeProductosActionPerformed

    private void menuImportarCatalogoDeProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuImportarCatalogoDeProductosActionPerformed
        wdwBaseDeDatosImportarCatalogoDeProductos ImportarCatalogoDeProductos = new wdwBaseDeDatosImportarCatalogoDeProductos();
        ventana.abrirPantalla(ImportarCatalogoDeProductos);
    }//GEN-LAST:event_menuImportarCatalogoDeProductosActionPerformed

    private void menuExportarCatalogoDeProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExportarCatalogoDeProductosActionPerformed
        wdwBaseDeDatosExportarCatalogoDeProductos ExportarCatalogoDeProductos = new wdwBaseDeDatosExportarCatalogoDeProductos();
        ventana.abrirPantalla(ExportarCatalogoDeProductos);
    }//GEN-LAST:event_menuExportarCatalogoDeProductosActionPerformed

    private void menuManualDeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuManualDeUsuarioActionPerformed
        wdwAyudaManualDeUsuario ManualDeUsuario = new wdwAyudaManualDeUsuario();
        ventana.abrirPantalla(ManualDeUsuario);
    }//GEN-LAST:event_menuManualDeUsuarioActionPerformed

    private void menuBaseDeDatosHistorialDeVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialDeVentasActionPerformed
        wdwBaseDeDAtosHistorialDeVentas HistorialVentas = new wdwBaseDeDAtosHistorialDeVentas();
        ventana.abrirPantalla(HistorialVentas);
        
    }//GEN-LAST:event_menuBaseDeDatosHistorialDeVentasActionPerformed

    private void menuBaseDeDatosHistorialDeComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialDeComprasActionPerformed
        wdwBaseDeDAtosHistorialDeCompras HistorialCompras = new wdwBaseDeDAtosHistorialDeCompras();
        ventana.abrirPantalla(HistorialCompras);        
    }//GEN-LAST:event_menuBaseDeDatosHistorialDeComprasActionPerformed

    private void menuInventarioImpresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuInventarioImpresoActionPerformed
        wdwReportesInventarioImpreso InventarioImpreso = new wdwReportesInventarioImpreso();
        ventana.abrirPantalla(InventarioImpreso);
    }//GEN-LAST:event_menuInventarioImpresoActionPerformed

    private void menuReportesVentasPorFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesVentasPorFechaActionPerformed
        wdwReportesVentasPorFecha VentasPorFecha = new wdwReportesVentasPorFecha ();
        ventana.abrirPantalla(VentasPorFecha);
    }//GEN-LAST:event_menuReportesVentasPorFechaActionPerformed

    private void menuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUsuariosActionPerformed
        wdwCatalogoUsuarios usuarios = new wdwCatalogoUsuarios();
        ventana.abrirPantalla(usuarios);
    }//GEN-LAST:event_menuUsuariosActionPerformed

    private void menuSucursalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSucursalesActionPerformed
        wdwCatalogoSucursales sucursales = new wdwCatalogoSucursales();
        ventana.abrirPantalla(sucursales);
    }//GEN-LAST:event_menuSucursalesActionPerformed

    private void menuEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmpleadosActionPerformed
        wdwCatalogoEmpleados empleado = new wdwCatalogoEmpleados();
        ventana.abrirPantalla(empleado);
    }//GEN-LAST:event_menuEmpleadosActionPerformed

    private void menuReportesComisionesSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesComisionesSucursalActionPerformed
        wdwReportesComisionesEmpleado reporte = new wdwReportesComisionesEmpleado();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesComisionesSucursalActionPerformed

    private void menuReportesComisionesSucursal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesComisionesSucursal1ActionPerformed
        wdwReportesComisionPorSucursal reporte = new wdwReportesComisionPorSucursal();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesComisionesSucursal1ActionPerformed

    private void menuReportesComisionesSucursal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesComisionesSucursal2ActionPerformed
        wdwReporteVentasCliente reporte = new wdwReporteVentasCliente();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesComisionesSucursal2ActionPerformed

    private void menuBonificacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBonificacionesActionPerformed
        wdwCatalogoBonificacion bonificacion = new wdwCatalogoBonificacion();
        ventana.abrirPantalla(bonificacion);
    }//GEN-LAST:event_menuBonificacionesActionPerformed

    private void menuProformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProformaActionPerformed
        wdwMovimientoProforma proforma = new wdwMovimientoProforma();
        ventana.abrirPantalla(proforma);
    }//GEN-LAST:event_menuProformaActionPerformed

    private void menuVentasActivasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasActivasActionPerformed
        wdwMovimientoVentasActivas VentasActivas = new wdwMovimientoVentasActivas();
        ventana.abrirPantalla(VentasActivas);
    }//GEN-LAST:event_menuVentasActivasActionPerformed

    private void menuAnulacionVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAnulacionVentasActionPerformed
        wdwMovimientoAnulacionFacturas AnulacionDeVentas = new wdwMovimientoAnulacionFacturas();
        ventana.abrirPantalla(AnulacionDeVentas);
    }//GEN-LAST:event_menuAnulacionVentasActionPerformed

    private void menuConsultaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConsultaProductosActionPerformed
        wdwConsultaProductos ConsultaProductos = new wdwConsultaProductos("Ninguno");
        ventana.abrirPantalla(ConsultaProductos);
    }//GEN-LAST:event_menuConsultaProductosActionPerformed

    private void menuReportesComisionesSucursal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesComisionesSucursal3ActionPerformed
        wdwReportesComisionesNoAbonos reporte = new wdwReportesComisionesNoAbonos();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesComisionesSucursal3ActionPerformed

    private void menuReportesAbonosDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesAbonosDetalleActionPerformed
        wdwReporteAbonosClienteDetalle reporte = new wdwReporteAbonosClienteDetalle();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesAbonosDetalleActionPerformed

    private void menuReportesDetalleVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesDetalleVentaActionPerformed
        wdwReporteDetalleVentasFecha reporte = new wdwReporteDetalleVentasFecha();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesDetalleVentaActionPerformed

    private void menuReportesDetalleGananciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesDetalleGananciaActionPerformed
        wdwReportesDetalleGanancia reporte = new wdwReportesDetalleGanancia();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesDetalleGananciaActionPerformed

    private void menuReportesComprasPorFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesComprasPorFechaActionPerformed
        wdwReportesComprasPorFecha VentasPorFecha = new wdwReportesComprasPorFecha ();
        ventana.abrirPantalla(VentasPorFecha);
    }//GEN-LAST:event_menuReportesComprasPorFechaActionPerformed

    private void menuReportesMovimientoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesMovimientoProductoActionPerformed
        wdwReportesMovimientoDeProductos reporte = new wdwReportesMovimientoDeProductos();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesMovimientoProductoActionPerformed

    private void menuBaseDeDatosMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosMantenimientoActionPerformed
        wdwBaseDeDatosMantenimiento Mantenimiento = new wdwBaseDeDatosMantenimiento();
        ventana.abrirPantalla(Mantenimiento);
    }//GEN-LAST:event_menuBaseDeDatosMantenimientoActionPerformed

    private void menuAjusteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAjusteInventarioActionPerformed
        wdwMovimientoAjusteInventario AbonoClientes = new wdwMovimientoAjusteInventario();
        ventana.abrirPantalla(AbonoClientes);
    }//GEN-LAST:event_menuAjusteInventarioActionPerformed

    private void menuExistenciaPorLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExistenciaPorLineaActionPerformed
        wdwReportesPorLinea reporte = new wdwReportesPorLinea();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuExistenciaPorLineaActionPerformed

    private void menuReportesDetalleVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesDetalleVenta1ActionPerformed
        wdwReporteDetalleComprasFecha reporte = new wdwReporteDetalleComprasFecha();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesDetalleVenta1ActionPerformed

    private void menuContabilidadEgresoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuContabilidadEgresoPagoActionPerformed
        wdwContabilidadPagos Pagos = new wdwContabilidadPagos();
        ventana.abrirPantalla(Pagos);
    }//GEN-LAST:event_menuContabilidadEgresoPagoActionPerformed

    private void menuContabilidadReportePagosFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuContabilidadReportePagosFechaActionPerformed
        wdwReportesPagosPorFecha reporte = new wdwReportesPagosPorFecha();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuContabilidadReportePagosFechaActionPerformed

    private void menuAjusteInventarioReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAjusteInventarioReporteActionPerformed
        wdwReportesAjustes reporte = new wdwReportesAjustes();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuAjusteInventarioReporteActionPerformed

    private void menuBaseDeDatosHistorialDeProformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialDeProformaActionPerformed
        wdwBaseDeDAtosHistorialDeProformas HistorialProformas = new wdwBaseDeDAtosHistorialDeProformas();
        ventana.abrirPantalla(HistorialProformas);
    }//GEN-LAST:event_menuBaseDeDatosHistorialDeProformaActionPerformed

    private void menuTopProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTopProductosActionPerformed
        wdwReportesTopProductos reporte = new wdwReportesTopProductos();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuTopProductosActionPerformed

    private void menuProductosAlMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosAlMinimoActionPerformed
        wdwReportesMinimoProducto reporte = new wdwReportesMinimoProducto();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuProductosAlMinimoActionPerformed

    private void menuProductosEnNegativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosEnNegativoActionPerformed
        wdwReportesNegativoProducto reporte = new wdwReportesNegativoProducto();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuProductosEnNegativoActionPerformed

    private void menuTopGananciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTopGananciaActionPerformed
        wdwReportesTopGanancia reporte = new wdwReportesTopGanancia();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuTopGananciaActionPerformed

    private void btnAccesoProformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoProformaActionPerformed
        wdwMovimientoProforma proforma = new wdwMovimientoProforma();
        ventana.abrirPantalla(proforma);
    }//GEN-LAST:event_btnAccesoProformaActionPerformed

    private void btnAccesoOrdenDeServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoOrdenDeServicioActionPerformed
        wdwMovimientoOrdenServicioDetalle Orden = new wdwMovimientoOrdenServicioDetalle();
        ventana.abrirPantalla(Orden);
    }//GEN-LAST:event_btnAccesoOrdenDeServicioActionPerformed

    private void btnAccesoConsultaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoConsultaProductosActionPerformed
        wdwConsultaProductos ConsultaProductos = new wdwConsultaProductos("Ninguno");
        ventana.abrirPantalla(ConsultaProductos);
    }//GEN-LAST:event_btnAccesoConsultaProductosActionPerformed

    private void menuAbonoDeClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbonoDeClientesActionPerformed
        wdwMovimientoAbonos abonos = new wdwMovimientoAbonos();
        ventana.abrirPantalla(abonos);
    }//GEN-LAST:event_menuAbonoDeClientesActionPerformed

    private void menuFondoDeEscritorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFondoDeEscritorioActionPerformed
        wdwUsuarioFondoDeEscritorio FondoDeUsuario = new wdwUsuarioFondoDeEscritorio();
        ventana.abrirPantalla(FondoDeUsuario); 
        //Informacion del fondo de escritorio
        wdwUsuarioFondoDeEscritorio.txtIDUsuario.setText(lblUsuario.getText());
        wdwUsuarioFondoDeEscritorio.txtNombreUsuario.setText(lblNombre.getText());
        wdwUsuarioFondoDeEscritorio.txtNombreFondo.setText(archivo_fondo);
    }//GEN-LAST:event_menuFondoDeEscritorioActionPerformed

    private void menuSeriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSeriesActionPerformed
        wdwCatalogoSerieConsulta Serie = new wdwCatalogoSerieConsulta();
        ventana.abrirPantalla(Serie);
    }//GEN-LAST:event_menuSeriesActionPerformed

    private void menuTrasladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrasladoActionPerformed
        wdwMovimientoTrasladoDeProductos Traslado = new wdwMovimientoTrasladoDeProductos();
        ventana.abrirPantalla(Traslado);
    }//GEN-LAST:event_menuTrasladoActionPerformed

    private void menuBaseDeDatosHistorialTrasladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialTrasladoActionPerformed
        wdwBaseDeDAtosHistorialDeTraslados Traslado = new wdwBaseDeDAtosHistorialDeTraslados();
        ventana.abrirPantalla(Traslado);
    }//GEN-LAST:event_menuBaseDeDatosHistorialTrasladoActionPerformed

    private void menuCierreDeCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCierreDeCajaActionPerformed
        wdwMovimientoCierreDeCaja VentasActivas = new wdwMovimientoCierreDeCaja();
        ventana.abrirPantalla(VentasActivas);
    }//GEN-LAST:event_menuCierreDeCajaActionPerformed

    private void menuOrdenDeServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOrdenDeServicioActionPerformed
        wdwMovimientoOrdenServicioDetalle Orden = new wdwMovimientoOrdenServicioDetalle();
        ventana.abrirPantalla(Orden);
    }//GEN-LAST:event_menuOrdenDeServicioActionPerformed

    private void menuBaseDeDatosHistorialOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialOrdenesActionPerformed
        wdwBaseDeDAtosHistorialOrdenesDeServicio Ordenes = new wdwBaseDeDAtosHistorialOrdenesDeServicio();
        ventana.abrirPantalla(Ordenes);
    }//GEN-LAST:event_menuBaseDeDatosHistorialOrdenesActionPerformed

    private void menuCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCategoriasActionPerformed
        wdwCatalogoCategoriaProducto Categoria = new wdwCatalogoCategoriaProducto();
        ventana.abrirPantalla(Categoria);
    }//GEN-LAST:event_menuCategoriasActionPerformed

    private void menuFallasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFallasActionPerformed
        wdwCatalogoFallas Fallas = new wdwCatalogoFallas();
        ventana.abrirPantalla(Fallas);
    }//GEN-LAST:event_menuFallasActionPerformed

    private void menuMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMarcasActionPerformed
        wdwCatalogoMarcas Marcas = new wdwCatalogoMarcas();
        ventana.abrirPantalla(Marcas);
    }//GEN-LAST:event_menuMarcasActionPerformed

    private void menuAbonoProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbonoProveedoresActionPerformed
        wdwMovimientoAbonoProveedor AbonoProveedores = new wdwMovimientoAbonoProveedor();
        ventana.abrirPantalla(AbonoProveedores);
    }//GEN-LAST:event_menuAbonoProveedoresActionPerformed

    private void btnAccesoVentaDeProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoVentaDeProductosActionPerformed
        wdwMovimientoVentaDeProductosNueva VentaDeProductos = new wdwMovimientoVentaDeProductosNueva();
        ventana.abrirPantalla(VentaDeProductos);
    }//GEN-LAST:event_btnAccesoVentaDeProductosActionPerformed

    private void menuCambioDeClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambioDeClaveActionPerformed
        wdwMenuCambioClave Clave = new wdwMenuCambioClave();
        ventana.abrirPantalla(Clave); 
    }//GEN-LAST:event_menuCambioDeClaveActionPerformed

    private void menuIntercambiarSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIntercambiarSerieActionPerformed
        wdwMovimientoIntercambiarSerie Intercambio = new wdwMovimientoIntercambiarSerie();
        ventana.abrirPantalla(Intercambio);
    }//GEN-LAST:event_menuIntercambiarSerieActionPerformed

    private void menuSalarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalarioActionPerformed
        wdwContabilidadSalarios Salarios = new wdwContabilidadSalarios();
        ventana.abrirPantalla(Salarios);
    }//GEN-LAST:event_menuSalarioActionPerformed

    private void menuMovimientoGarantiaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMovimientoGarantiaClienteActionPerformed
        wdwMovimientoGarantiaCliente GarantiaCliente = new wdwMovimientoGarantiaCliente();
        ventana.abrirPantalla(GarantiaCliente);
    }//GEN-LAST:event_menuMovimientoGarantiaClienteActionPerformed

    private void menuBaseDeDatosHistorialGarantiaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialGarantiaClienteActionPerformed
        wdwBaseDeDatosHistorialGarantiaCliente HistorialGarantiaCliente = new wdwBaseDeDatosHistorialGarantiaCliente();
        ventana.abrirPantalla(HistorialGarantiaCliente);
    }//GEN-LAST:event_menuBaseDeDatosHistorialGarantiaClienteActionPerformed

    private void menuAnticipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAnticipoActionPerformed
        wdwMovimientoAnticipo Anticipo = new wdwMovimientoAnticipo();
        ventana.abrirPantalla(Anticipo);
    }//GEN-LAST:event_menuAnticipoActionPerformed

    private void menuMovimientoGarantiaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMovimientoGarantiaProveedorActionPerformed
        wdwMovimientoGarantiaProveedor GarantiaProveedor = new wdwMovimientoGarantiaProveedor();
        ventana.abrirPantalla(GarantiaProveedor);
    }//GEN-LAST:event_menuMovimientoGarantiaProveedorActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        consultaNotificaciones();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnFacturasProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturasProveedorActionPerformed
        abrirReporte("rptFacturaProveedorPendiente");
    }//GEN-LAST:event_btnFacturasProveedorActionPerformed

    private void btnFacturaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturaClienteActionPerformed
        abrirReporte("rptFacturaClientePendiente");
    }//GEN-LAST:event_btnFacturaClienteActionPerformed

    private void btnReparacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReparacionesActionPerformed
        abrirReporte("rptOrdenesTallerPendiente");
    }//GEN-LAST:event_btnReparacionesActionPerformed

    private void btnGarantiaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGarantiaClienteActionPerformed
        abrirReporte("rptGarantiaProveedorPendiente");
    }//GEN-LAST:event_btnGarantiaClienteActionPerformed

    private void btnGarantiaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGarantiaProveedorActionPerformed
        abrirReporte("rptGarantiaClientePendiente");
    }//GEN-LAST:event_btnGarantiaProveedorActionPerformed

    private void menuBaseDeDatosHistorialAnticiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialAnticiposActionPerformed
        wdwBaseDeDAtosHistorialAnticipos HistorialAnticipos = new wdwBaseDeDAtosHistorialAnticipos();
        ventana.abrirPantalla(HistorialAnticipos);
    }//GEN-LAST:event_menuBaseDeDatosHistorialAnticiposActionPerformed

    private void menuBaseDeDatosHistorialGarantiaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBaseDeDatosHistorialGarantiaProveedorActionPerformed
        wdwBaseDeDAtosHistorialGarantiaProveedor HistorialGarantiaProveedor = new wdwBaseDeDAtosHistorialGarantiaProveedor();
        ventana.abrirPantalla(HistorialGarantiaProveedor);
    }//GEN-LAST:event_menuBaseDeDatosHistorialGarantiaProveedorActionPerformed

    private void btnAnticiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnticiposActionPerformed
        abrirReporte("rptAnticiposPendientes");
    }//GEN-LAST:event_btnAnticiposActionPerformed

    private void menuReportesBonificacionesPorEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesBonificacionesPorEmpleadoActionPerformed
        wdwReportesBonificacionesPorFecha reporte = new wdwReportesBonificacionesPorFecha();        
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesBonificacionesPorEmpleadoActionPerformed

    private void menuReportesDetalleAbonosProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesDetalleAbonosProveedorActionPerformed
        wdwReportesDetalleAbonosProveedor reporte = new wdwReportesDetalleAbonosProveedor();
        ventana.abrirPantalla(reporte);
    }//GEN-LAST:event_menuReportesDetalleAbonosProveedorActionPerformed

    private void menuContabilidadMobiliarioEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuContabilidadMobiliarioEquipoActionPerformed
        wdwContabilidadEquipo equipo = new wdwContabilidadEquipo();
        ventana.abrirPantalla(equipo);
    }//GEN-LAST:event_menuContabilidadMobiliarioEquipoActionPerformed

    private void menuRetencionProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRetencionProveedorActionPerformed
        wdwMovimientoRetencionProveedor retencion = new wdwMovimientoRetencionProveedor();
        ventana.abrirPantalla(retencion);
    }//GEN-LAST:event_menuRetencionProveedorActionPerformed

    private void menuReportesVentasPorFechaFELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesVentasPorFechaFELActionPerformed
        wdwReportesVentasFEL VentasPorFecha = new wdwReportesVentasFEL();
        ventana.abrirPantalla(VentasPorFecha);
    }//GEN-LAST:event_menuReportesVentasPorFechaFELActionPerformed

    private void menuReportesVentasPorFechaSinFELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesVentasPorFechaSinFELActionPerformed
        wdwReportesVentasSinFacturaFEL VentasPorFecha = new wdwReportesVentasSinFacturaFEL();
        ventana.abrirPantalla(VentasPorFecha);
    }//GEN-LAST:event_menuReportesVentasPorFechaSinFELActionPerformed

    private void menuTrazabilidadSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTrazabilidadSerieActionPerformed
        wdwReportesTrazabilidadSerie VentasPorFecha = new wdwReportesTrazabilidadSerie();
        ventana.abrirPantalla(VentasPorFecha);
    }//GEN-LAST:event_menuTrazabilidadSerieActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inventory().setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAccesoConsultaProductos;
    public static javax.swing.JButton btnAccesoOrdenDeServicio;
    public static javax.swing.JButton btnAccesoProforma;
    public static javax.swing.JButton btnAccesoVentaDeProductos;
    public static javax.swing.JButton btnActualizar;
    public static javax.swing.JButton btnAnticipos;
    public static javax.swing.JButton btnFacturaCliente;
    public static javax.swing.JButton btnFacturasProveedor;
    public static javax.swing.JButton btnGarantiaCliente;
    public static javax.swing.JButton btnGarantiaProveedor;
    public static javax.swing.JButton btnReparaciones;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    public static javax.swing.JLabel lblNombre;
    public static javax.swing.JLabel lblRol;
    public static javax.swing.JLabel lblSucursal;
    public static javax.swing.JLabel lblTerminal;
    public static javax.swing.JLabel lblUsuario;
    public static javax.swing.JMenuBar menu;
    public static javax.swing.JMenuItem menuAbonoDeClientes;
    public static javax.swing.JMenuItem menuAbonoProveedores;
    private javax.swing.JMenuItem menuAcercaDe;
    public static javax.swing.JMenuItem menuAjusteInventario;
    private javax.swing.JMenuItem menuAjusteInventarioReporte;
    public static javax.swing.JMenuItem menuAnticipo;
    public static javax.swing.JMenuItem menuAnulacionVentas;
    public static javax.swing.JMenu menuAyuda;
    public static javax.swing.JMenu menuBaseDeDatos;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialAnticipos;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialDeCompras;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialDeProforma;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialDeVentas;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialGarantiaCliente;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialGarantiaProveedor;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialOrdenes;
    public static javax.swing.JMenuItem menuBaseDeDatosHistorialTraslado;
    public static javax.swing.JMenuItem menuBaseDeDatosMantenimiento;
    public static javax.swing.JMenuItem menuBonificaciones;
    private javax.swing.JMenuItem menuCambioDeClave;
    public static javax.swing.JMenu menuCatalogo;
    public static javax.swing.JMenuItem menuCategorias;
    public static javax.swing.JMenuItem menuCierreDeCaja;
    public static javax.swing.JMenuItem menuClientes;
    public static javax.swing.JMenu menuComisiones;
    public static javax.swing.JMenuItem menuCompraDeProductos;
    public static javax.swing.JMenu menuCompras;
    public static javax.swing.JMenuItem menuConsultaProductos;
    public static javax.swing.JMenu menuContabilidad;
    private javax.swing.JMenuItem menuContabilidadEgresoPago;
    private javax.swing.JMenuItem menuContabilidadMobiliarioEquipo;
    private javax.swing.JMenuItem menuContabilidadReportePagosFecha;
    private javax.swing.JMenu menuContabilidadReportes;
    public static javax.swing.JMenuItem menuEmpleados;
    private javax.swing.JMenuItem menuExistenciaPorLinea;
    public static javax.swing.JMenuItem menuExportarCatalogoDeProductos;
    public static javax.swing.JMenuItem menuFallas;
    public static javax.swing.JMenuItem menuFondoDeEscritorio;
    public static javax.swing.JMenuItem menuImportarCatalogoDeProductos;
    public static javax.swing.JMenuItem menuIntercambiarSerie;
    public static javax.swing.JMenu menuInventario;
    private javax.swing.JMenuItem menuInventarioImpreso;
    private javax.swing.JMenuItem menuManualDeUsuario;
    public static javax.swing.JMenuItem menuMarcas;
    public static javax.swing.JMenu menuMovimiento;
    public static javax.swing.JMenu menuMovimientoCompras;
    public static javax.swing.JMenu menuMovimientoGarantia;
    public static javax.swing.JMenuItem menuMovimientoGarantiaCliente;
    public static javax.swing.JMenuItem menuMovimientoGarantiaProveedor;
    public static javax.swing.JMenu menuMovimientoInventario;
    public static javax.swing.JMenu menuMovimientoTaller;
    public static javax.swing.JMenu menuMovimientoVentas;
    public static javax.swing.JMenuItem menuOrdenDeServicio;
    public static javax.swing.JMenuItem menuProductos;
    private javax.swing.JMenuItem menuProductosAlMinimo;
    private javax.swing.JMenuItem menuProductosEnNegativo;
    public static javax.swing.JMenuItem menuProforma;
    public static javax.swing.JMenuItem menuProveedores;
    public static javax.swing.JMenu menuReportes;
    public static javax.swing.JMenuItem menuReportesAbonosDetalle;
    private javax.swing.JMenuItem menuReportesBonificacionesPorEmpleado;
    private javax.swing.JMenuItem menuReportesComisionesSucursal;
    private javax.swing.JMenuItem menuReportesComisionesSucursal1;
    private javax.swing.JMenuItem menuReportesComisionesSucursal2;
    private javax.swing.JMenuItem menuReportesComisionesSucursal3;
    private javax.swing.JMenuItem menuReportesComprasPorFecha;
    public static javax.swing.JMenuItem menuReportesDetalleAbonosProveedor;
    public static javax.swing.JMenuItem menuReportesDetalleGanancia;
    private javax.swing.JMenuItem menuReportesDetalleVenta;
    private javax.swing.JMenuItem menuReportesDetalleVenta1;
    private javax.swing.JMenuItem menuReportesMovimientoProducto;
    private javax.swing.JMenuItem menuReportesVentasPorFecha;
    protected static javax.swing.JMenuItem menuReportesVentasPorFechaFEL;
    public static javax.swing.JMenuItem menuReportesVentasPorFechaSinFEL;
    public static javax.swing.JMenuItem menuRetencionProveedor;
    private javax.swing.JMenuItem menuSalario;
    public static javax.swing.JMenuItem menuSeries;
    public static javax.swing.JMenuItem menuSucursales;
    private javax.swing.JMenuItem menuTopGanancia;
    private javax.swing.JMenuItem menuTopProductos;
    public static javax.swing.JMenuItem menuTraslado;
    private javax.swing.JMenuItem menuTrazabilidadSerie;
    public static javax.swing.JMenu menuUsuario;
    public static javax.swing.JMenuItem menuUsuarios;
    public static javax.swing.JMenuItem menuVentaDeProductos;
    public static javax.swing.JMenu menuVentas;
    public static javax.swing.JMenuItem menuVentasActivas;
    public static javax.swing.JPanel pnlLog;
    public static javax.swing.JDesktopPane pnlPrincipal;
    public static javax.swing.JPopupMenu.Separator sepExportar;
    public static javax.swing.JPopupMenu.Separator sepMenuBaseDeDatos;
    public static javax.swing.JPopupMenu.Separator sepMenuCatalogoDos;
    public static javax.swing.JPopupMenu.Separator sepMenuCatalogoUno;
    public static javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}

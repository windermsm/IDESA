package inventory.acceso;

import inventory.librerias.WindowController;
import inventory.vistas.Inventory;
import inventory.vistas.wdwMovimientoCierreDeCaja;
import inventory.vistas.wdwMovimientoCierreDeCajaModificar;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import objetos.ObjetosCaja;

/**
 *
 * @author FERNANDO
 */
public class AccesoCaja {

    private inventory.vistas.wdwMovimientoCierreDeCajaModificar vmcaja;
    private Date fechaSistema;
    private wdwMovimientoCierreDeCaja vsaldo;
    private int posicion;
    private ObjetosCaja caja = new ObjetosCaja();
    private String operacion;
    private String id;
    private double saldoInicial;
    private double saldoVentas;
    private double saldoCierre;
    private double saldoCreditosCierre;
    private double saldoAbonosCierre;
    private static String db_aperturas = "db";
    private static String db_facturas = "";
    private static String db_cierres = "";
    private DefaultTableModel tablemodel = new DefaultTableModel();
    private AccesoInventario conexion = new AccesoInventario();
    private AccesoExcepciones mensaje = new AccesoExcepciones();
    
    private WindowController ventana = new WindowController();

    public AccesoCaja(wdwMovimientoCierreDeCaja vsaldo) {
        this.vsaldo = vsaldo;
        tablemodel.addColumn("ID");
        tablemodel.addColumn("Fecha");
        tablemodel.addColumn("Hora");
        tablemodel.addColumn("Cantidad");
        tablemodel.addColumn("Usuario");
        tablemodel.addColumn("Caja");
        this.vsaldo.tablaListarSaldos.setModel(tablemodel);
        vsaldo.txtFecha.setText(fechaActual());
        vsaldo.txtFecha.setEnabled(true);
        vsaldo.txtHora.setText(horaActual());
        deshabilitarApertura();
    }

    public AccesoCaja() {
    }

    public AccesoCaja(wdwMovimientoCierreDeCajaModificar vmcaja) {
        this.vmcaja = vmcaja;
        deshabilitarCampos();
        deshabilitarEdicion();
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void limpiarCampos() {
        vsaldo.txtCantidad.setText("0.00");
        vsaldo.txtUsuario.setText(null);
        vsaldo.txtFecha.setText(fechaActual());
        vsaldo.txtCantidad.setText(null);
        vsaldo.txtHora.setText(null);
    }

    public void deshabilitarApertura() {
        vsaldo.txtCantidad.setEnabled(false);
        vsaldo.txtUsuario.setEnabled(false);
        vsaldo.txtFecha.setEnabled(false);
        vsaldo.txtCantidad.setEnabled(false);
        vsaldo.txtHora.setEnabled(false);
        vsaldo.btnAperturar.setEnabled(true);
        vsaldo.btnAceptar.setEnabled(false);
    }

    public void habilitarApertura() {
        vsaldo.txtCantidad.setEnabled(true);
        vsaldo.txtUsuario.setEnabled(true);
        vsaldo.txtFecha.setEnabled(true);
        vsaldo.txtCantidad.setEnabled(true);
        vsaldo.txtHora.setEnabled(true);
        vsaldo.btnAceptar.setEnabled(true);
        vsaldo.btnAperturar.setEnabled(false);
        vsaldo.txtCantidad.setText("0.00");
        vsaldo.txtFecha.setText(fechaActual());
        vsaldo.txtHora.setText(horaActual());
    }

    public int camposVacios() {
        if (vsaldo.txtCantidad.getText().isEmpty() | vsaldo.txtFecha.getText().isEmpty()
                | vsaldo.txtHora.getText().isEmpty() | vsaldo.txtUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vsaldo, "Hay campos vacíos.");
            return 0;
        } else {
            return 1;
        }

    }

    /**
     * obtener fecha del sistema
     *
     * @return
     */
    public String fechaActual() {
        Date date = new Date();
        String fecha = new SimpleDateFormat("YYYY-MM-dd").format(date);
        return String.valueOf(fecha);
    }

    public String horaActual() {
        Date hora = new Date();
        String horaActual = new SimpleDateFormat("HH:mm:ss").format(hora);
        return horaActual;
    }

    /**
     * devuelve la fecha en formato dd/MM/YYYY
     *
     * @return
     */
    public String obtenerFechaInicial() {
        try {
            //String formato = vsaldo.txtFechaInicio.getDateFormatString();
            //Formato
            Date date = vsaldo.txtFechaInicio.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            return sdf.format(date);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una fecha.");
            return null;
        }
    }

    /**
     * cargar todas las aperturas de caja
     */
    public void cargarTodo(String pUsuario, String pTerminal) {
        PreparedStatement statement;
        String sql = "SELECT * FROM m_apertura";
        try {

            ResultSet resultados = conexion.listarRegistros(sql, "Cierre de Caja", "Mostrar Todo", pUsuario, pTerminal);
            tablemodel.setRowCount(0);
            while (resultados.next()) {
                tablemodel.addRow(new Object[]{resultados.getString("id"), resultados.getString("Fecha"),
                            resultados.getString("Hora"), resultados.getString("Cantidad"), resultados.getString("Usuario"), resultados.getString("Estado")});
            }
            if (!resultados.first()) {
                mensaje.manipulacionExcepciones("informacion", "No hay datos almacenados.", "Cierre de Caja");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error--> " + e);
        } finally {
            conexion.desconectar();
        }
    }

    /**
     * listar los estados por fecha
     *
     * @param inicio
     * @param fin
     */
    public void listarPorFecha(String inicio) {
        PreparedStatement statement;
        ResultSet resultados;
        String sql = "SELECT * FROM m_apertura WHERE Fecha = '" + inicio + "'";

        try {

            statement = conexion.conectar().prepareStatement(sql);
            resultados = statement.executeQuery();
            tablemodel.setRowCount(0);//limpiar la tabla
            while (resultados.next()) {
                tablemodel.addRow(new Object[]{resultados.getString("id"),
                            resultados.getString("Fecha"),
                            resultados.getString("Hora"), resultados.getString("Cantidad"),
                            resultados.getString("Usuario"), resultados.getString("Estado")});
            }
            if (!resultados.first()) {
                JOptionPane.showMessageDialog(vsaldo, "No hay registros encontrados.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error--> " + e);
        } finally {
            conexion.desconectar();
        }
    }

    /**
     * aperturar caja
     *
     * @param caja
     */
    public void aperturarCaja(ObjetosCaja caja) {
        if (camposVacios() != 0) {
            PreparedStatement statement;
            int registrado = 0;
            String sql = "INSERT INTO m_apertura(Fecha,Usuario,Hora,Cantidad,Estado)"
                    + "VALUES(?,?,?,?,?) ";
            try {

                statement = conexion.conectar().prepareStatement(sql);
                statement.setString(1, caja.getFechaApertura());
                statement.setString(2, caja.getUsuario());
                statement.setString(3, caja.getHora());
                statement.setDouble(4, caja.getCantidadApertura());
                statement.setString(5, "Abierta");
                registrado = statement.executeUpdate();

                if (registrado == 1) {
                    JOptionPane.showMessageDialog(vsaldo, "La apertura de Caja fue realizada con exito.");
                    vsaldo.btnAceptar.setEnabled(false);
                    vsaldo.btnAperturar.setEnabled(true);
                    limpiarCampos();
                    deshabilitarApertura();
                    //cargarTodo();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(vsaldo, "Ucurrio un error al aperturar la Caja." + e);
            } finally {
                conexion.desconectar();
            }
        }
    }

    public void detallarApertura(String id) {
        
        PreparedStatement ps;
        String sql = "SELECT * FROM m_apertura WHERE id ='" + id + "'";
        ResultSet rs;
        
        try {
            
            ps = conexion.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.first()) {
                vmcaja = new wdwMovimientoCierreDeCajaModificar(rs.getString("id"));
                ventana.abrirPantalla(vmcaja);
            }
            
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(vsaldo, "Error  en el detalle de la apertura \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    /**
     * ################################################################# todo
     * este bloque corresponde a la manipulacion de modificiaciones *
     * ###################################################################
     */
    /**
     * * carga la informacion de la apertura de caja seleccionada
     *
     * @param id
     */
    public void cargarData(String id) {
        PreparedStatement pstatement;
        ResultSet resultados;
        String sql = "SELECT * FROM m_apertura WHERE id='" + id + "'";
        try {
            pstatement = conexion.conectar().prepareStatement(sql);
            resultados = pstatement.executeQuery();
            if (resultados.first()) {

                vmcaja.txtCantidadM.setText(resultados.getString("Cantidad"));
                vmcaja.txtFechaM.setText(resultados.getString("Fecha"));
                vmcaja.txtHoraM.setText(resultados.getString("Hora"));
                vmcaja.txtUsuarioM.setText(resultados.getString("Usuario"));
                vmcaja.txtIdM.setText(resultados.getString("id"));
                vmcaja.txtEstado.setText(resultados.getString("Estado"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error buscarInformacion de la Apertura \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }

    }

    private void deshabilitarCampos() {
        vmcaja.txtCantidadM.setEnabled(false);
        vmcaja.txtFechaM.setEnabled(false);
        vmcaja.txtHoraM.setEnabled(false);
        vmcaja.txtUsuarioM.setEnabled(false);
        vmcaja.txtIdM.setEnabled(false);
        vmcaja.txtEstado.setEnabled(false);
    }

    /**
     * habilita los btones para modificar la caja
     */
    public void habilitarEdicion() {
        vmcaja.btnModificar.setEnabled(true);
        //vmcaja.btnBorrarM.setEnabled(true);
        //vmcaja.btnOk.setEnabled(false);
    }

    public void deshabilitarEdicion() {
        vmcaja.btnModificar.setEnabled(false);
        //vmcaja.btnBorrarM.setEnabled(false);
        //vmcaja.btnOk.setEnabled(true);
    }

    public void habilitarCampos() {
        vmcaja.txtCantidadM.setEnabled(true);
        vmcaja.txtFechaM.setEnabled(true);
        vmcaja.txtHoraM.setEnabled(true);
        vmcaja.txtUsuarioM.setEnabled(true);
        vmcaja.txtIdM.setEnabled(false);
        vmcaja.txtEstado.setEnabled(false);
    }

    /**
     * elimina la apertura de caja realizada
     *
     * @param id
     */
    public void eliminar(String id) {
        PreparedStatement ps;
        int eliminado = 0;
        String sql = "DELETE FROM m_apertura WHERE id = '" + id + "'";
        try {
            ps = conexion.conectar().prepareStatement(sql);
            eliminado = ps.executeUpdate();
            if (eliminado == 1) {
                JOptionPane.showMessageDialog(vmcaja, "Registro eliminado.");
                vmcaja.dispose();
                deshabilitarEdicion();
            } else {
                JOptionPane.showMessageDialog(vmcaja, "Registro no encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error al eliminar apertura. \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }

    }

    /**
     * modifica toda la apertura seleccionada
     *
     * @param vm
     */
    public void actualizarApertura(wdwMovimientoCierreDeCajaModificar vm) {

        PreparedStatement pst;
        int actualizado = 0;
        String sql = "UPDATE m_apertura SET Fecha ='" + vm.txtFechaM.getText() + "',"
                + "Usuario ='" + vm.txtUsuarioM.getText() + "', Hora ='" + vm.txtHoraM.getText() + "',"
                + " Cantidad ='" + vm.txtCantidadM.getText() + "', Estado ='" + vm.txtEstado.getText() + "' WHERE id='" + vm.txtIdM.getText() + "';";
        try {
            pst = conexion.conectar().prepareStatement(sql);
            actualizado = pst.executeUpdate();
            if (actualizado == 1) {
                JOptionPane.showMessageDialog(vmcaja, "Modificación exitosa.");
                //vmcaja.dispose();
                deshabilitarCampos();
                deshabilitarEdicion();
                consultarSaldoFinal(vm.txtFechaM.getText(), vm.txtIdM.getText());
                cargarData(vm.txtIdM.getText());
                actualizarCierre(vm);
            } else {
                JOptionPane.showMessageDialog(vmcaja, "Error al actualizar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error al actualizar apertura. \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    /**
     * actualiza el cierre en caso haya cambios al generar el reporte
     *
     * @param vm
     */
    public void actualizarCierre(wdwMovimientoCierreDeCajaModificar vm) {

        PreparedStatement pst;
        int actualizado = 0;
        
        
        double credito = vm.txtTotalCredito.getText().isEmpty() ? 0.0 : Double.parseDouble(vm.txtTotalCredito.getText());
        double abonos = vm.txtTotalAbonos.getText().isEmpty() ? 0.0 : Double.parseDouble(vm.txtTotalAbonos.getText());
        double gastos = vm.txtTotalGastos.getText().isEmpty() ? 0.0 : Double.parseDouble(vm.txtTotalGastos.getText());
        double anticipos = vm.txtTotalAnticipos.getText().isEmpty() ? 0.0 : Double.parseDouble(vm.txtTotalAnticipos.getText());
        double devoluciones = vm.txtTotalDevoluciones.getText().isEmpty() ? 0.0 : Double.parseDouble(vm.txtTotalDevoluciones.getText());   
        
        String sql = "UPDATE m_cierre SET fecha ='" + vm.txtFechaM.getText() + "',"
                + " usuario ='" + vm.txtUsuarioM.getText() + "', hora ='" + vm.txtHoraM.getText() + "',"
                + " saldo_inicial ='" + vm.txtSaldoAperturado.getText() + "', saldo_ventas = '" + vm.txtTotalVentas.getText() + "',"
                + " saldo_total_cierre = '" + vm.txtSaldoCierre.getText() 
                + "', saldo_creditos_cierre = " + credito  
                + ", saldo_abonos_cierre = " + abonos + ", saldo_gastos_cierre = " + abonos 
                + ", saldo_anticipos_cierre = "  + anticipos + ", saldo_devoluciones_cierre = " + devoluciones
                + " WHERE id_apertura='" + vm.txtIdM.getText() + "';";
        try {
            pst = conexion.conectar().prepareStatement(sql);
            actualizado = pst.executeUpdate();
            if (actualizado == 1) {
                //JOptionPane.showMessageDialog(vmcaja, "Modificación exitosa.");
                //vmcaja.dispose();
                deshabilitarCampos();
                deshabilitarEdicion();
                consultarSaldoFinal(vm.txtFechaM.getText(), vm.txtIdM.getText());
                cargarData(vm.txtIdM.getText());
            } else {
                //JOptionPane.showMessageDialog(vmcaja, "Error al actualizar!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error al actualizar datos del Cierre de Ventas. \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    /**
     * consulta la sumatoria de todo lo vendido consulta la cantidad de dinero
     * aperturado, genera el total
     *
     * @param fecha
     * @param id
     */
    public void consultarSaldoFinal(String fecha, String id) {
        
        PreparedStatement pst;
        ResultSet resultado;
        
        String sql = "SELECT SUM(total_Factura) AS total from m_factura WHERE estado_factura = 'Cancelado' AND \n"
                + "tipo_venta = 'Contado' AND str_to_date(date_format(fecha_cancelacion_factura, "
                + "'%Y-%m-%d'),'%Y-%m-%d') = str_to_date('" + fecha + "','%Y-%m-%d')";
        
        String sqlApertura = "SELECT Cantidad FROM m_apertura WHERE Fecha = '" + fecha + "' AND id = '" + id + "'";
        
        try {
            pst = conexion.conectar().prepareStatement(sql);
            resultado = pst.executeQuery();
            if (resultado.first()) {
                try {
                    saldoVentas = resultado.getDouble("total");
                } catch (NumberFormatException ex) {
                    saldoVentas = 0.0;
                }
            } else {
                saldoVentas = 0.0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error al consultar saldo. \n" + e.getMessage());
        } finally {
            conexion.desconectar();
        }
        
        try {
            pst = conexion.conectar().prepareStatement(sqlApertura);
            resultado = pst.executeQuery();
            if (resultado.first()) {
                try {
                    saldoInicial = Double.parseDouble(resultado.getString("Cantidad"));
                } catch (NumberFormatException ex) {
                    System.out.println("Error al parsear cantidad del saldo. \n" + ex.toString());
                }
            } else {
                saldoInicial = 0.0;
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(vmcaja, "Error al consultar el saldo de apertura." + e.getMessage());
        } finally {
            conexion.desconectar();
            saldoCierre = saldoInicial + saldoVentas;
            vmcaja.txtTotalVentas.setText(String.valueOf(saldoVentas));
            vmcaja.txtSaldoAperturado.setText(String.valueOf(saldoInicial));
            vmcaja.txtSaldoCierre.setText(String.valueOf(saldoCierre));
        }
        
        //Consultar datos adicionales creditos y abonos
        vmcaja.txtTotalCredito.setText(consultaTotalCreditos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha));
        vmcaja.txtTotalAbonos.setText(consultaTotalAbonos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha));
        vmcaja.txtTotalGastos.setText(consultaTotalGastos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha));
        vmcaja.txtTotalDevoluciones.setText(consultaTotalDevoluciones(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha));
        vmcaja.txtTotalAnticipos.setText(String.valueOf(consultaTotalAnticiposNegativos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha)));
        vmcaja.txtTotalAnticiposPositivos.setText(String.valueOf(consultaTotalAnticiposPositivos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), fecha)));
        
    }
    
    public String consulataAnticipos(String pUsuario, String pTerminal, String pFecha) {
        double entrantes = consultaTotalAnticiposPositivos(pUsuario, pTerminal, pFecha);
        double salientes = consultaTotalAnticiposNegativos(pUsuario, pTerminal, pFecha);
        double total = entrantes - salientes;
        return String.valueOf(total);
    }
    
    public double consultaTotalAnticiposPositivos(String pUsuario, String pTerminal, String pFecha) {
        double valor = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT  IFNULL(SUM(total_anticipo), 0) AS total "
                + "FROM    m_anticipo "
                + "WHERE   DATE_FORMAT(fecha_entrega_persona_aticipo, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d') "
                + "    AND estado_anticipo IN ('Creado')";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getDouble("total");
            }
        } catch (Exception Error) {
            valor = 0;
        }
        return valor;
    }
    
    public double consultaTotalAnticiposNegativos(String pUsuario, String pTerminal, String pFecha) {
        double valor = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT  IFNULL(SUM(total_anticipo), 0) AS total "
                + "FROM    m_anticipo "
                + "WHERE   DATE_FORMAT(fecha_uso_anticipo, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d') "
                + "    AND estado_anticipo IN ('Despachado', 'Devuelto')";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getDouble("total");
            }
        } catch (Exception Error) {
            valor = 0;
        }
        return valor;
    }
    
    public String consultaTotalDevoluciones(String pUsuario, String pTerminal, String pFecha) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT IFNULL(SUM(precio_producto_devolucion), 0) AS total "
                + "FROM m_devolucion "
                + "WHERE DATE_FORMAT(fecha_hora_devolucion, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d')";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("total");
            }
        } catch (Exception error) {
            return null;
        }
        return valor;
    }
    
    public String consultaTotalCreditos(String pUsuario, String pTerminal, String pFecha) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT IFNULL(SUM(monto_credito), 0) AS total "
                + "FROM m_factura "
                + "WHERE estado_factura IN ('Credito') "
                + "AND DATE_FORMAT(fecha_emision_factura, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d')";
        //System.out.println(sql);
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("total");
            }
        } catch (Exception error) {
            return null;
        }
        return valor;
    }
    
    public String consultaTotalAbonos(String pUsuario, String pTerminal, String pFecha) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT IFNULL(SUM(monto_d_abono), 0) AS total "
                + " FROM d_abono "
                + " WHERE DATE_FORMAT(fecha_d_abono, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d')";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("total");
            }
        } catch (Exception error) {
            return null;
        }
        return valor;
    }
    
    public String consultaTotalGastos(String pUsuario, String pTerminal, String pFecha) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT IFNULL(SUM(monto_doc_pago), 0) AS total "
                + "FROM m_pago "
                + "WHERE DATE_FORMAT(fecha_pago, '%Y-%m-%d') = DATE_FORMAT('" + pFecha + "', '%Y-%m-%d') AND tipo_pago = 'Operativo'";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("total");
            }
        } catch (Exception error) {
            return null;
        }
        return valor;
    }
    
    public int consultarFacturasPendientes(String pUsuario, String pTerminal) {
        int valor = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = " SELECT COUNT(id_factura) AS total "
                + "FROM m_factura "
                + "WHERE estado_factura = 'Pendiente'";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getInt("total");
            }
        } catch (Exception error) {
            return 0;
        }
        return valor;
    }
    
    
    public int consultarCajasAbiertas(String pUsuario, String pTerminal) {
        int valor = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select COUNT(id) as total from m_apertura where estado = 'Abierta'";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getInt("total");
            }
        } catch (Exception error) {
            return 0;
        }
        return valor;
    }

    /**
     * cambia el estado de la caja, siempre y cuando la fecha lo permita
     *
     * @param vmodificar
     */
    public void cerrar_caja(wdwMovimientoCierreDeCajaModificar vmodificar, String tipo_usuario) {
        
        String cerrar_caja = "UPDATE m_apertura SET Estado = 'Cerrada' WHERE id='" + vmodificar.txtIdM.getText() + "'";
        PreparedStatement cerrar;
        int cerrado = 0;

        if (validarCierre(vmodificar, tipo_usuario) == 1) {
            try {
                cerrar = conexion.conectar().prepareStatement(cerrar_caja);
                cerrado = cerrar.executeUpdate();
                if (cerrado == 1) {
                    JOptionPane.showMessageDialog(vmodificar, "Caja cerrada correctamente.");
                    cargarData(vmodificar.txtIdM.getText());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(vmodificar, "Error al cerrar Caja. \n" + e.getMessage());
            } finally {
                conexion.desconectar();
            }
        } else {
            JOptionPane.showMessageDialog(vmodificar, "Solo se puede cerrar la caja abierta hoy.");
        }
        
    }
    
    public void guardarFechaHoraCierre(wdwMovimientoCierreDeCajaModificar vmodificar, String tipo_usuario) {

        String cerrar_caja = "UPDATE m_cierre "
                + "SET   fecha = CONCAT(DATE_FORMAT(NOW(), '%Y-%m-%d')), "
                + "      hora = CONCAT(DATE_FORMAT(NOW(), '%H:%i:%S')) "
                + "WHERE id_apertura = " + vmodificar.txtIdM.getText() ;
        
        PreparedStatement cerrar;
        int cerrado = 0;
        
        try {
            cerrar = conexion.conectar().prepareStatement(cerrar_caja);
            cerrado = cerrar.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmodificar, "Error al guardar la Fecha y Hora de cierre.");
        } finally {
            conexion.desconectar();
        }
        
    }

    public int validarCierre(wdwMovimientoCierreDeCajaModificar vmodificar, String tipo_usuario) {
        PreparedStatement validarcierre;
        ResultSet existe;
        String sql_cierre = "SELECT * from m_apertura WHERE id='" + vmodificar.txtIdM.getText() + "'";
        try {
            validarcierre = conexion.conectar().prepareStatement(sql_cierre);
            existe = validarcierre.executeQuery();
            if (existe.first()) {
                if(tipo_usuario.equals("Administrador")) {
                    return 1;
                } else {
                    if (existe.getString("fecha").equals(fechaActual()) && existe.getString("Estado").equals("Abierta")) {
                        vmodificar.btnCerrarCaja.setEnabled(true);
                        return 1;
                    } else {
                        if (tipo_usuario.equals("Administrador")) {
                            vmodificar.btnCerrarCaja.setEnabled(true);
                        } else {
                            vmodificar.btnCerrarCaja.setEnabled(false);
                        }
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vmodificar, "Error al validar cierre de Caja." + e);
        } finally {
            conexion.desconectar();
        }
        return 0;
    }

    /**
     * registra los datos del cierre de la caja o los actualiza
     *
     * @param vmodificar
     */
    public void guardarCierre(wdwMovimientoCierreDeCajaModificar vmodificar) {

        //validar si el reporte ya existe
        PreparedStatement registro;
        int registrado = 0;
        ResultSet resultados;
        
        String validar = "SELECT * FROM m_cierre WHERE id_apertura ='" + vmodificar.txtIdM.getText() + "'";
        
        String registrar = "INSERT INTO m_cierre( id_apertura, usuario, fecha, hora,"
                + "saldo_inicial, saldo_ventas, saldo_total_cierre, saldo_creditos_cierre, saldo_abonos_cierre, saldo_gastos_cierre, saldo_anticipos_cierre, saldo_devoluciones_cierre) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
        
        
        double credito = vmodificar.txtTotalCredito.getText().isEmpty() ? 0.0 : Double.parseDouble(vmodificar.txtTotalCredito.getText());
        double abonos = vmodificar.txtTotalAbonos.getText().isEmpty() ? 0.0 : Double.parseDouble(vmodificar.txtTotalAbonos.getText());
        double gastos = vmodificar.txtTotalGastos.getText().isEmpty() ? 0.0 : Double.parseDouble(vmodificar.txtTotalGastos.getText());
        double anticipos = vmodificar.txtTotalAnticipos.getText().isEmpty() ? 0.0 : Double.parseDouble(vmodificar.txtTotalAnticipos.getText());
        double devoluciones = vmodificar.txtTotalDevoluciones.getText().isEmpty() ? 0.0 : Double.parseDouble(vmodificar.txtTotalDevoluciones.getText());        

        try {

            registro = conexion.conectar().prepareStatement(validar);
            resultados = registro.executeQuery();

            if (resultados.first()) {
                actualizarCierre(vmodificar);

            } else {
                registro = conexion.conectar().prepareStatement(registrar);
                registro.setString(1, vmodificar.txtIdM.getText());
                registro.setString(2, vmodificar.txtUsuarioM.getText());
                registro.setString(3, vmodificar.txtFechaM.getText());
                registro.setString(4, vmodificar.txtHoraM.getText());
                registro.setDouble(5, Double.parseDouble(vmodificar.txtSaldoAperturado.getText()));
                registro.setDouble(6, Double.parseDouble(vmodificar.txtTotalVentas.getText()));
                registro.setDouble(7, Double.parseDouble(vmodificar.txtSaldoCierre.getText()));
                registro.setDouble(8, credito);
                registro.setDouble(9, abonos);
                registro.setDouble(10, gastos);
                registro.setDouble(11, anticipos);
                registro.setDouble(12, devoluciones);
                registrado = registro.executeUpdate();

                if (registrado == 1) {
                    System.out.println("El reporte de caja fue registrado con exito.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar reporte de Caja. \n" + e.toString());
        } finally {
            conexion.desconectar();
        }

    }
    
}
package inventory.acceso;

import inventory.vistas.wdwMovimientoCompraDeProductos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author FERNANDO
 */
public class AccesoTipoPagoCompra {

    AccesoInventario acceso = new AccesoInventario();
    private wdwMovimientoCompraDeProductos wdw_compra;

    //constructor para compra credito
    public AccesoTipoPagoCompra(wdwMovimientoCompraDeProductos wdw_compra) {
        this.wdw_compra = wdw_compra;
    }

    /**
     * registro de compra al credito
     *
     * @param vnota_credito
     */
    private void registrar_pago_nota_credito(String idCompra, String tipoPago, String diasCredito, String serieNC, String numeroNC, String total) {
        if ((campos_vacios_notacredito(diasCredito, serieNC) == 1) && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst_registro;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst_registro = acceso.conectar().prepareStatement(sql);
                pst_registro.setString(1, idCompra);
                pst_registro.setString(2, tipoPago);
                pst_registro.setDouble(3, 0.0);
                pst_registro.setDouble(4, 0.0);
                pst_registro.setString(5, null);
                pst_registro.setString(6, null);
                pst_registro.setString(7, null);
                pst_registro.setString(8, diasCredito);
                pst_registro.setString(9, serieNC);
                pst_registro.setString(10, numeroNC);
                pst_registro.setDouble(11, Double.parseDouble(total));
                pst_registro.setDouble(12, Double.parseDouble(total));

                guardado = pst_registro.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Nota de crédito registrada con éxito.");
                    this.limpiar_casilla_notaCredito();

                } else {

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar nota credito. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        }

    }

    private void registrar_pago_tarjeta(String idCompra, String tipoPago, String tarjeta, String banco, String total) {
        if ((campos_vacios_tarjeta(tarjeta, banco) == 1) && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst_registro;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst_registro = acceso.conectar().prepareStatement(sql);
                pst_registro.setString(1, idCompra);
                pst_registro.setString(2, tipoPago);
                pst_registro.setDouble(3, 0.0);
                pst_registro.setDouble(4, 0.0);
                pst_registro.setString(5, null);
                pst_registro.setString(6, banco);
                pst_registro.setString(7, tarjeta);
                pst_registro.setString(8, null);
                pst_registro.setString(9, null);
                pst_registro.setString(10, null);
                pst_registro.setDouble(11, 0.0);
                pst_registro.setDouble(12, Double.parseDouble(total));
                guardado = pst_registro.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Nota compra con tarjeta regisrado con éxito.");
                    this.limpiar_casillaTarjeta();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar pago tarjeta. " + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        }

    }

    private void registrar_pago_creditoContado(String idCompra, String tipoPago, String contado, String credito, String total) {
        if ((campos_vacios_pagoMixto(contado, credito) == 1) && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst_registro;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst_registro = acceso.conectar().prepareStatement(sql);
                pst_registro.setString(1, idCompra);
                pst_registro.setString(2, tipoPago);
                pst_registro.setDouble(3, Double.parseDouble(contado));
                pst_registro.setDouble(4, Double.parseDouble(credito));
                pst_registro.setString(5, null);
                pst_registro.setString(6, null);
                pst_registro.setString(7, null);
                pst_registro.setString(8, null);
                pst_registro.setString(9, null);
                pst_registro.setString(10, null);
                pst_registro.setDouble(11, 0.0);
                pst_registro.setDouble(12, Double.parseDouble(total));
                guardado = pst_registro.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Nota compra pago Credito/Contado registrado con éxito!");
                    this.limpiar_casilla_pagoMixto();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Rgistrar_pago_credito/contado. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        }

    }

    private void registrar_pago_cheque(String idCompra, String tipoPago, String numeroCheque, String banco, String total) {
        if ((campos_vacios_cheque(numeroCheque, banco)) == 1 && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, idCompra);
                pst.setString(2, tipoPago);
                pst.setDouble(3, 0.0);
                pst.setDouble(4, 0.0);
                pst.setString(5, numeroCheque);
                pst.setString(6, banco);
                pst.setString(7, null);
                pst.setString(8, null);
                pst.setString(9, null);
                pst.setString(10, null);
                pst.setDouble(11, 0.0);
                pst.setDouble(12, Double.parseDouble(total));

                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Registro de compra con cheque éxitoso.");
                    this.limpiar_casillaCheque();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar pago chque. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }
    }

    private void registrar_pago_credito(String idCompra, String tipoPago, String diasCredito, String total) {
        if ((campos_vacios_credito(diasCredito)) == 1 && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra, tipo_pago, contado, credito, numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, idCompra);
                pst.setString(2, tipoPago);
                pst.setDouble(3, 0.0);
                pst.setDouble(4, Double.parseDouble(total));
                pst.setString(5, null);
                pst.setString(6, null);
                pst.setString(7, null);
                pst.setString(8, diasCredito);
                pst.setString(9, null);
                pst.setString(10, null);
                pst.setDouble(11, 0.0);
                pst.setDouble(12, Double.parseDouble(total));

                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Registro de compra al crédito éxitoso.");
                    this.limpiar_casillaCheque();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar pago credito. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }
    }

    private void registrar_pago_nc_contado(String idCompra, String tipoPago, String diasCredito, String serieNC, String numeroNC, String contado, String total) {
        if ((campos_vacios_nc_contado(diasCredito, serieNC, contado)) == 1 && (validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst;
            int guardado = 0;
            double totalNC = (Double.parseDouble(total) - Double.parseDouble(contado));
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, idCompra);
                pst.setString(2, tipoPago);
                pst.setDouble(3, Double.parseDouble(contado));
                pst.setDouble(4, 0.0);
                pst.setString(5, null);
                pst.setString(6, null);
                pst.setString(7, null);
                pst.setString(8, diasCredito);
                pst.setString(9, serieNC);
                pst.setString(10, numeroNC);
                pst.setDouble(11, totalNC);
                pst.setDouble(12, Double.parseDouble(total));

                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Registro de compra éxitoso.");
                    this.limpiar_casilla_notaCredito();
                    this.limpiar_casilla_pagoMixto();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar pago nc contado. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }

    }

    private void registrar_pago_contado(String idCompra, String tipoPago, String total) {
        if ((validar_tipoPago(tipoPago) == 1)) {
            PreparedStatement pst;
            int guardado = 0;
            String sql = "INSERT INTO m_formas_pago(id_compra,tipo_pago,contado,credito,numero_cheque,banco_emisor,"
                    + "numero_tarjeta,dias_credito,serie_nota_credito,numero_nota_credito,total_nota_credito,total_factura)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, idCompra);
                pst.setString(2, tipoPago);
                pst.setDouble(3, Double.parseDouble(total));
                pst.setDouble(4, 0.0);
                pst.setString(5, null);
                pst.setString(6, null);
                pst.setString(7, null);
                pst.setString(8, null);
                pst.setString(9, null);
                pst.setString(10, null);
                pst.setDouble(11, 0.0);
                pst.setDouble(12, Double.parseDouble(total));

                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_compra, "Registro de compra éxitoso.");
                    this.limpiar_casilla_pagoMixto();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_compra, "Error Registrar pago contado. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }
    }

    /**
     * seleccion del pago adecuado para registrarlo
     *
     * @param idCompra
     * @param tipoPago
     * @param diasCredito
     * @param serieNC
     * @param tarjeta
     * @param banco
     * @param contado
     * @param credito
     * @param numeroCheque
     * @param bancoCheque
     * @param total
     */
    public void registar_pago(String idCompra, String tipoPago, String diasCredito,
            String serieNC, String numeroNC, String tarjeta, String banco, String contado,
            String credito, String numeroCheque, String bancoCheque, String total) {

        switch (tipoPago) {
            case "Crédito":
                this.registrar_pago_credito(idCompra, tipoPago, diasCredito, total);
                break;
            case "Cheque":
                this.registrar_pago_cheque(idCompra, tipoPago, numeroCheque, bancoCheque, total);
                break;
            case "Tarjeta":
                this.registrar_pago_tarjeta(idCompra, tipoPago, tarjeta, banco, total);
                break;
            case "Crédito/Contado":
                this.registrar_pago_creditoContado(idCompra, tipoPago, contado, credito, total);
                break;
            case "Nota Crédito":
                this.registrar_pago_nota_credito(idCompra, tipoPago, diasCredito, serieNC, numeroNC, total);
                break;
            case "NotaCrédito/Contado":
                this.registrar_pago_nc_contado(idCompra, tipoPago, diasCredito, serieNC, numeroNC, contado, total);
                break;
            case "Contado":
                this.registrar_pago_contado(idCompra, tipoPago, total);
                break;
            default:
                break;
        }

    }

    private int campos_vacios_cheque(String numeroCheque, String banco) {
        if (numeroCheque.equals("") || banco.equals("")) {
            JOptionPane.showMessageDialog(wdw_compra, "Los datos del cheque son obligatorios.");
            return 0;
        } else {
            return 1;
        }
    }

    private int campos_vacios_tarjeta(String numeroTarjeta, String banco) {
        if (numeroTarjeta.equals("") || banco.equals("")) {

            JOptionPane.showMessageDialog(wdw_compra, "Los datos de tarjeta son obligatorios.");
            return 0;
        } else {
            return 1;
        }
    }

    private int campos_vacios_notacredito(String dias, String serie) {
        if (dias.equals("") || serie.equals("")) {
            JOptionPane.showMessageDialog(wdw_compra, "Los campos de crédito están vacíos.");
            return 0;
        } else {
            return 1;
        }

    }

    private int campos_vacios_nc_contado(String dias, String serie, String contado) {
        if (dias.equals("") || serie.equals("") || contado.equals("")) {
            JOptionPane.showMessageDialog(wdw_compra, "Datos incompletos.");
            return 0;
        } else {
            return 1;
        }
    }

    private int campos_vacios_credito(String diasCredito) {
        if (diasCredito.equals("")) {
            JOptionPane.showMessageDialog(wdw_compra, "Dias credito no han sido ingresados.");
            return 0;
        } else {
            return 1;
        }
    }

    private int campos_vacios_pagoMixto(String contado, String credito) {
        if (contado.equals("") || credito.equals("")) {
            JOptionPane.showMessageDialog(wdw_compra, "Completa los campos de pago Contado/Credito");
            return 0;
        } else {
            return 1;
        }
    }

    public void habilitar_casillaCheque() {
        wdw_compra.txt_banco_emisor_cheque.setEnabled(true);
        wdw_compra.txt_no_cheque.setEnabled(true);
    }

    public void habilitar_casillaTarjeta() {
        wdw_compra.txt_banco_emisor_tarjeta.setEnabled(true);
        wdw_compra.txt_numero_tarjeta.setEnabled(true);
    }

    public void habilitar_casillaNotaCredito() {
        wdw_compra.txt_dias_credito.setEnabled(true);
        wdw_compra.txt_serie_ncredito.setEnabled(true);
        wdw_compra.txt_numeroNC.setEnabled(true);
    }

    public void habilitar_casilla_creditoContado() {
        wdw_compra.txt_cant_contado.setEnabled(true);
        wdw_compra.txt_cant_credito.setEnabled(true);
    }

    public void habilitar_soloCredito() {
        wdw_compra.txt_dias_credito.setEnabled(true);
    }

    public void deshabilitar_soloCredito() {
        wdw_compra.txt_dias_credito.setEnabled(false);
    }

    public void habilitar_contado() {
        wdw_compra.txt_cant_contado.setEnabled(true);
    }

    public void deshabilitar_contado() {
        wdw_compra.txt_cant_contado.setEnabled(false);

    }

    public void deshabilitar_casillaCheque() {
        wdw_compra.txt_banco_emisor_cheque.setEnabled(false);
        wdw_compra.txt_no_cheque.setEnabled(false);
    }

    public void deshabilitar_casillaTarjeta() {
        wdw_compra.txt_banco_emisor_tarjeta.setEnabled(false);
        wdw_compra.txt_numero_tarjeta.setEnabled(false);
    }

    public void deshabilitar_casillaNotaCredito() {
        wdw_compra.txt_dias_credito.setEnabled(false);
        wdw_compra.txt_serie_ncredito.setEnabled(false);
        wdw_compra.txt_numeroNC.setEnabled(false);
    }

    public void deshabilitar_casilla_pagoMixto() {
        wdw_compra.txt_cant_contado.setEnabled(false);
        wdw_compra.txt_cant_credito.setEnabled(false);
    }

    public void limpiar_casillaCheque() {
        wdw_compra.txt_banco_emisor_cheque.setText("");
        wdw_compra.txt_no_cheque.setText("");
    }

    public void limpiar_casillaTarjeta() {
        wdw_compra.txt_banco_emisor_tarjeta.setText("");
        wdw_compra.txt_numero_tarjeta.setText("");
    }

    public void limpiar_casillaCredito() {
        wdw_compra.txt_dias_credito.setText("");
        wdw_compra.txt_serie_ncredito.setText("");
    }

    public void limpiar_casilla_pagoMixto() {
        wdw_compra.txt_cant_contado.setText("");
        wdw_compra.txt_cant_credito.setText("");
    }

    public void limpiar_casilla_notaCredito() {
        wdw_compra.txt_dias_credito.setText("");
        wdw_compra.txt_serie_ncredito.setText("");
        wdw_compra.txt_numeroNC.setText("");
    }

    private int validar_tipoPago(String tipo) {
        if (!tipo.equals("Contado")) {
            return 1;
        } else {
            return 0;
        }
    }
}

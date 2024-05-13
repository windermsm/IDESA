package inventory.acceso;

import inventory.vistas.wdwPantallaErrorBaseDeDatos;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class AccesoExcepciones {

    public int valor = 0;

    public void manipulacionExcepciones(String pTipoVentana, String pMensaje, String pTitulo) {
        
        int vTipoVentana = 0;
        int vTipoMensaje = 0;

        //SELECCIONAR EL TIPO DE VENTANA ELEJIDA
        if (pTipoVentana.equals("critico")) {
            vTipoVentana = 0;
            vTipoMensaje = 1;
        }

        if (pTipoVentana.equals("advertencia")) {
            vTipoVentana = 1;
            vTipoMensaje = 1;
        }

        if (pTipoVentana.equals("informacion")) {
            vTipoVentana = 2;
            vTipoMensaje = 1;
        }

        if (pTipoVentana.equals("interrogante")) {
            vTipoVentana = 3;
            vTipoMensaje = 2;
        }

        if (vTipoMensaje == 1) {
            javax.swing.JOptionPane.showMessageDialog(null, pMensaje, pTitulo, vTipoVentana, null);
        } else {
            if (vTipoMensaje == 2) {
                valor = javax.swing.JOptionPane.showConfirmDialog(null, pMensaje, pTitulo, vTipoMensaje);
            }
        }

    }
    
    public void mostrarError(String pPantalla, String pOpcion, String pSQL, String pError, String pUsuario, String pTerminal) {
        try {
            wdwPantallaErrorBaseDeDatos error = new wdwPantallaErrorBaseDeDatos(pPantalla, pOpcion, pSQL, pError, pUsuario, pTerminal);
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int x = ancho - error.getWidth();
            int y = 0;
            error.setLocation(x, y);
            error.setVisible(true);
            error.toFront();
            error.show();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }
}

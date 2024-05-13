/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.librerias;
import inventory.vistas.Inventory;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author DSANTACRUZ
 */
public class WindowController {
    
    private static String Log = "";
    
    private static void agregarLog(String texto) {
        Log = Log + "ORDEN SERVICIO" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }

    public void abrirPantalla(JInternalFrame pantalla) {
        
        String nombre = pantalla.getTitle();
        JInternalFrame[] activos = Inventory.pnlPrincipal.getAllFrames();
        int cantidad = Inventory.pnlPrincipal.getComponentCount();
        boolean mostrar = true;
        
        agregarLog("Componentes en pantalla: " + cantidad);
        agregarLog("Nombre: " + nombre);
        
        for (int i = 0; i < cantidad; i++) {
            agregarLog("Validar " + Inventory.pnlPrincipal.getComponent(i).getClass().getName());
            if (pantalla.getClass().getName().toString().equals(Inventory.pnlPrincipal.getComponent(i).getClass().getName().toString())) {
                mostrar = false;
            }
        }

        if (mostrar) {
            agregarLog("Mostrar pantalla " + nombre);
            int ancho = Inventory.pnlPrincipal.getWidth();
            int alto = Inventory.pnlPrincipal.getHeight();
            int x = (ancho / 2) - (pantalla.getWidth() / 2);
            int y = (alto / 2) - (pantalla.getHeight() / 2);
            Inventory.pnlPrincipal.add(pantalla);
            pantalla.setVisible(true);
            pantalla.toFront();
            pantalla.setLocation(x, y);
        } else {
            agregarLog("Esta pantalla se encuentra abierta.");
            JOptionPane.showMessageDialog(null, "La pantalla " + nombre + " ya se encuentra abierta.");
        }
    }
    
}

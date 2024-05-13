/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class ObjetosLoggin {
    int id_loggin;
    String user_loggin;
    String estado_loggin;
    
    public int getId_loggin() {
        int vId_cliente = 0;
        try {
            vId_cliente = id_loggin;
        } catch (Error error) {
            System.out.println("EC LOGGIN:GETID_LOGGIN " + error);
        }
        return vId_cliente;
    }

    public void setId_loggin(int pId_loggin) {
        try {
            id_loggin = pId_loggin;
        } catch (Error error) {
            System.out.append("EC LOGGIN:SETID_LOGGIN " + error);
        }
    }
    
    public String getUser_loggin() {
        String vUser_loggin = null;
        try {
            vUser_loggin = user_loggin;
        } catch (Error error) {
            System.out.println("EC LOGGIN:GETUSER_LOGGIN " + error);
        }
        return vUser_loggin;
    }

    public void setUser_loggin(String pUser_loggin) {
        try {
            user_loggin = pUser_loggin;
        } catch (Error error) {
            System.out.append("EC LOGGIN:SETUSER_LOGGIN " + error);
        }
    }
    
     public String getEstado_loggin() {
        String vEstado_loggin = null;
        try {
            vEstado_loggin = estado_loggin;
        } catch (Error error) {
            System.out.println("EC LOGGIN:GETUESTADO_LOGGIN " + error);
        }
        return vEstado_loggin;
    }

    public void setEstado_loggin(String pEstado_loggin) {
        try {
            estado_loggin = pEstado_loggin;
        } catch (Error error) {
            System.out.append("EC LOGGIN:SETESTADO_LOGGIN " + error);
        }
    }
}

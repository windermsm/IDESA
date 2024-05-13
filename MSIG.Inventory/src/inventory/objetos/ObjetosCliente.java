/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */

public class ObjetosCliente {
    
    private int id_cliente;
    private int id_persona;
    private String dir_cliente;
    private int tel_cliente;
    private int celular_cliente;
    private String email_cliente;
    private float limite_credito;
    private int dias_de_credito;
    private String tipo_cliente;
    
    public int getId_cliente(){
        int vId_cliente = 0;
        try{
            vId_cliente = id_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETID_CLIENTE "+error);
        }
        return vId_cliente;
    }
    
    public void setId_cliente(int pId_cliente){
        try{
            id_cliente = pId_cliente;
        }catch(Error error){
            System.out.append("EC CLIENTE:SETID_CLIENTE "+error);
        }
    }
    
    public int getId_persona(){
        int vId_persona = 0;
        try{
            vId_persona = id_persona;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETID_PERSONA "+error);
        }
        return vId_persona;
    }
    
    public void setId_persona(int pId_persona){
        try{
            id_persona = pId_persona;
        }catch(Error error){
            System.out.println("EC CLIENTE:SETID_PERSONA "+error);
        }
    }
    
    public String getDir_cliente(){
        String vDir_cliente = null;
        try{
            vDir_cliente = dir_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETDIR_CLIENTE " + error);
        }
        return vDir_cliente;
    }
    
    public void setDir_cliente(String pDir_cliente){
        try{
            dir_cliente = pDir_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:SETDIR_CLIENTE "+error);
        }
    }
    
    public int getTel_cliente(){
        int vTel_cliente = 0;
        try{
            vTel_cliente = tel_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETTEL_CLIENTE "+error);
        }
        return vTel_cliente;
    }
    
    public void setTel_cliente(int pTel_cliente){
        try{
            tel_cliente = pTel_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:SETTEL_CLIENTE "+error);
        }
    }
    
    public int getCelular_cliente(){
        int vCelular_cliente = 0;
        try{
            vCelular_cliente = celular_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETCELULAR_CLIENTE "+error);
        }
        return vCelular_cliente;
    }
    
    public void setCelular_cliente(int pCelular_cliente){
        try{
            celular_cliente = pCelular_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:SETCELULAR_CLIENTE "+error);
        }
    }
    
    public String getEmail_cliente(){
        String vEmail_cliente = null;
        try{
            vEmail_cliente = email_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:GETEMAIL_CLIENTE " + error);
        }
        return vEmail_cliente;
    }
    
    public void setEmail_cliente(String pEmail_cliente){
        try{
            email_cliente = pEmail_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:SETEMAIL_CLIENTE "+error);
        }
    }
    
    public float getLimite_credito(){
        float vLimite_credito = 0;
        try{
            vLimite_credito = limite_credito;
        }catch(Error error){
            System.out.println("EC CLIENTE:getLimite_credito "+error);
        }
        return vLimite_credito;
    }
    
    public void setLimite_credito(float pLimite_credito){
        try{
            limite_credito = pLimite_credito;
        }catch(Error error){
            System.out.append("EC CLIENTE:setLimite_credito "+error);
        }
    }
    
    public int getDias_de_credito(){
        int vDias_de_credito = 0;
        try{
            vDias_de_credito = dias_de_credito;
        }catch(Error error){
            System.out.println("EC CLIENTE:getDias_de_credito "+error);
        }
        return vDias_de_credito;
    }
    
    public void setDias_de_credito(int pDias_de_credito){
        try{
            dias_de_credito = pDias_de_credito;
        }catch(Error error){
            System.out.println("EC CLIENTE:setDias_de_credito "+error);
        }
    }
    
    public String getTipo_cliente(){
        String vTipo_cliente = null;
        try{
            vTipo_cliente = tipo_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:getTipo_cliente " + error);
        }
        return vTipo_cliente;
    }
    
    public void setTipo_cliente(String pTipo_cliente){
        try{
            tipo_cliente = pTipo_cliente;
        }catch(Error error){
            System.out.println("EC CLIENTE:setTipo_cliente "+error);
        }
    }
    
    
}

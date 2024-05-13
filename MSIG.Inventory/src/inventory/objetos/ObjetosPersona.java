/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class ObjetosPersona {
    
    private int id_persona;
    private String nit_persona;
    private String nom_persona;
    
    public int getId_persona(){
        int vId_persona = 0;
        try{
            vId_persona = id_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETID_PERSONA "+error);
        }
        return vId_persona;
    }
    
    public void setId_persona(int pId_persona){
        try{
            id_persona = pId_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETID_PERSONA "+error);
        }
    }
    
    public String getNit_persona(){
        String vNit_persona = null;
        try{
            vNit_persona = nit_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETNIT "+error);
        }
        return vNit_persona;
    }
    
    public void setNit_persona(String pNit_persona){
        try{
            nit_persona = pNit_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETNIT "+error);
        }
    }
    
    public String getNom_persona(){
        String vNom_persona = null;
        try{
            vNom_persona = nom_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETNOM_PERSONA "+error);
        }
        return vNom_persona;
    }
    
    public void setNom_persona(String pNom_persona){
        try{
            nom_persona = pNom_persona;
        }catch(Error error){
            System.out.println("EC PERSONA:GETNOM_PERSONA "+error);
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class ObjetosDetalleProforma {
    
    private int id_d_proforma;
    private int id_proforma;
    private String id_producto;
    private float cant_d_proforma;
    private float precio_venta_d_proforma;
    private float sub_total_d_proforma;
    
    public int getId_d_proforma(){
        int vId_d_proforma = 0;
        try{
            vId_d_proforma = id_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:getId_d_proforma "+error);
        }
        return vId_d_proforma;
    }
    
    public void setId_d_proforma(int pId_d_proforma){
        try{
            id_d_proforma = pId_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:setId_d_proforma "+error);
        }
    }
    
    public int getId_proforma(){
        int vId_proforma = 0;
        try{
            vId_proforma = id_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:getId_proforma "+error);
        }
        return vId_proforma;
    }
    
    public void setId_proforma(int pId_proforma){
        try{
            id_proforma = pId_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:setId_proforma "+error);
        }
    }
    
    public String getId_producto(){
        String vId_producto = null;
        try{
            vId_producto = id_producto;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:GETID_FPRODUTO "+error);
        }
        return vId_producto;
    }
    
    public void setId_producto(String pId_producto){
        try{
            id_producto = pId_producto;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:SETID_PRODUCTO "+error);
        }
    }
    
    public float getCant_d_proforma(){
        float vCant_d_proforma = 0;
        try{
            vCant_d_proforma = cant_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:getCant_d_proforma "+error);
        }
        return vCant_d_proforma;
    }
    
    public void setCant_d_proforma(float pCant_d_proforma){
        try{
            cant_d_proforma = pCant_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:setCant_d_factura "+error);
        }
    }
    
    public float getPrecio_venta_d_proforma(){
        float vPrecio_venta_d_proforma = 0;
        try{
            vPrecio_venta_d_proforma = precio_venta_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:getPrecio_venta_d_proforma "+error); 
        }
        return vPrecio_venta_d_proforma;
    }
    
    public void setPrecio_venta_d_proforma(float pPrecio_venta_d_proforma){
        try{
            precio_venta_d_proforma = pPrecio_venta_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:setPrecio_venta_d_proforma " +error);
        }
    }
    
    public float getSub_total_proforma(){
        float vSub_total_proforma = 0;
        try{
            vSub_total_proforma = sub_total_d_proforma;
        }catch(Error error){
            System.out.println("EC DETALLE_PROFORMA:getSub_total_proforma "+error);
        }
        return vSub_total_proforma;
    }
    
    public void setSub_total_proforma(float pSub_total_proforma){
        try {
            sub_total_d_proforma = pSub_total_proforma;
        } catch (Error error) {
            System.out.println("EC DETALLE_PROFORMA:setSub_total_proforma "+error);
        }
    }
    
}

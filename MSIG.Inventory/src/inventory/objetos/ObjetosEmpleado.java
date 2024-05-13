package inventory.objetos;

public class ObjetosEmpleado {
    private int     id_empleado;
    private int     id_sucursal;
    private String  tipo_empleado;
    private String  nombre_empleado;
    private String  estado_empleado;
    private double  bono_empleado;
    private String  fecha_ingreso;
    private String  fecha_salida;
    private String  nombre_sucursal;
    private double  salario_empleado;
    private double  porcentaje_comision;
    
    public int getId_empleado(){
        int vId_empleado = 0;
        try{
            vId_empleado = id_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getId_empleado" + error);
        }
        return vId_empleado;
    }
    
    public void setId_empleado(int pId_empleado){
        try{
            id_empleado = pId_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setId_empleado " + error);
        }
    }
    
    public int getId_sucursal(){
        int vId_sucursal = 0;
        try{
            vId_sucursal = id_sucursal;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getId_sucursal" + error);
        }
        return vId_sucursal;
    }
    
    public void setId_sucursal(int pId_sucursal){
        try{
            id_sucursal = pId_sucursal;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setId_sucursal " + error);
        }
    }
    
    public String getTipo_empleado(){
        String vTipo_empleado = "";
        try{
            vTipo_empleado = tipo_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getTipo_empleado " + error);
        }
        return vTipo_empleado;
    }
    
    public void setTipo_empleado(String pTipo_empleado){
        try{
            tipo_empleado = pTipo_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setTipo_empleado" + error);
        }
    }
    
    public String getNombre_empleado(){
        String vNombre_empleado = "";
        try{
            vNombre_empleado = nombre_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getNombre_empleado " + error);
        }
        return vNombre_empleado;
    }
    
    public void setNombre_empleado(String pNombre_empleado){
        try{
            nombre_empleado = pNombre_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setNombre_empleado " + error);
        }
    }
    
    public String getEstado_empleado(){
        String vEstado_empleado = "";
        try{
            vEstado_empleado = estado_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getEstado_empleado " + error);
        }
        return vEstado_empleado;
    }
    
    public void setEstado_empleado(String pEstado_empleado){
        try{
            estado_empleado = pEstado_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setEstado_empleado " + error);
        }
    }
    
    public double getBono_empleado(){
        double vBono_empleado = 0;
        try{
            vBono_empleado = bono_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getBono_empleado " + error);
        }
        return vBono_empleado;
    }
    
    public void setBono_empleado(double pBono_empleado){
        try{
            bono_empleado = pBono_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setBono_empleado " + error);
        }
    }
    
    public String getFecha_ingreso(){
        String vFecha_ingreso = "";
        try{
            vFecha_ingreso = fecha_ingreso;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getFecha_ingreso " + error);
        }
        return vFecha_ingreso;
    }
    
    public void setFecha_ingreso(String pFecha_ingreso){
        try{
            fecha_ingreso = pFecha_ingreso;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setFecha_ingreso " + error);
        }
    }
    
    public String getFecha_salida(){
        String vFecha_salida = "";
        try{
            vFecha_salida = fecha_salida;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getFecha_salida " + error);
        }
        return vFecha_salida;
    }
    
    public void setFecha_salida(String pFecha_salida){
        try{
            fecha_salida = pFecha_salida;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setFecha_salida " + error);
        }
    }
    
    public String getNombre_sucursal(){
        String vNombre_sucursal = "";
        try{
            vNombre_sucursal = nombre_sucursal;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getNombre_sucursal " + error);
        }
        return vNombre_sucursal;
    }
    
    public void setNombre_sucursal(String pNombre_sucursal){
        try{
            nombre_sucursal = pNombre_sucursal;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setNombre_sucursal " + error);
        }
    }
    
    public double getSalario_empleado(){
        double vSalario_empleado = 0;
        try{
            vSalario_empleado = salario_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getSalario_empleado " + error);
        }
        return vSalario_empleado;
    }
    
    public void setSalario_empleado(double pSalario_empleado){
        try{
            salario_empleado = pSalario_empleado;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setSalario_empleado " + error);
        }
    }
    
    public double getPorcentaje_comision(){
        double vPorcentaje_comision = 0;
        try{
            vPorcentaje_comision = porcentaje_comision;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: getPorcentaje_comision " + error);
        }
        return vPorcentaje_comision;
    }
    
    public void setPorcentaje_comision(double pPorcentaje_comision){
        try{
            porcentaje_comision = pPorcentaje_comision;
        }catch(Error error){
            System.out.println("OBJETO EMPLEADO: setPorcentaje_comision " + error);
        }
    }
}

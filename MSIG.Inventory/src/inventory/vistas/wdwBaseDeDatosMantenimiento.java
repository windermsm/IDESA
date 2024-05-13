package inventory.vistas;

import inventory.acceso.AccesoExcepciones;
import inventory.acceso.AccesoInventario;
import inventory.servicios.ServicioLeerArchivoExcel;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class wdwBaseDeDatosMantenimiento extends javax.swing.JInternalFrame {
    int fila = 0;
    AccesoInventario conexion = new AccesoInventario();
    AccesoExcepciones mensaje = new AccesoExcepciones();
    
    /**
     * Creates new form wdwBaseDeDatosMantenimiento
     */
    public wdwBaseDeDatosMantenimiento() {
        initComponents();
        listarTablas();
        listarVistas();
        listarFunciones();
        listarProcedimientos();
        listarTriggers();
        listarProcesos();
        listarUsuarios();
    }

    private void listarTablas(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre");
            //modelo.addColumn("Descripcion");
            ResultSet resultado = conexion.listarRegistros("show full tables from inventory where table_type = 'BASE TABLE'", "Base de Datos", "Listar Tablas", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("Se obtuvieron " + 50 + " tablas");
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                String nombre = resultado.getString("Tables_in_inventory");
                System.out.println(nombre);
                modelo.setValueAt(nombre, fila, 0);
                fila++;
            }
            tblTablas.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar las tablas de la base de datos");
            System.out.println(error.toString());
        }
    }
    
    private void listarProcedimientos(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre");
            ResultSet resultado = conexion.listarRegistros("select * from information_schema.ROUTINES where ROUTINE_TYPE = 'PROCEDURE' and ROUTINE_SCHEMA = 'inventory'", "Base de Datos", "Listar Procedimientos", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("Se obtuvieron " + 50 + " procedimientos");
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                String nombre = resultado.getString("SPECIFIC_NAME");
                System.out.println(nombre);
                modelo.setValueAt(nombre, fila, 0);
                fila++;
            }
            tblProcedimientos.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar las procedimientos de la base de datos");
            System.out.println(error.toString());
        }
    }
    
    private void listarFunciones(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre");
            ResultSet resultado = conexion.listarRegistros("select * from information_schema.ROUTINES where ROUTINE_TYPE = 'FUNCTION' and ROUTINE_SCHEMA = 'inventory'", "Base de Datos", "Listar Funciones", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("Se obtuvieron " + 50 + " funciones");
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                String nombre = resultado.getString("SPECIFIC_NAME");
                System.out.println(nombre);
                modelo.setValueAt(nombre, fila, 0);
                fila++;
            }
            tblFunciones.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar las funciones de la base de datos");
            System.out.println(error.toString());
        }
    }
    
    private void listarTriggers(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre");
            ResultSet resultado = conexion.listarRegistros("select * from information_schema.TRIGGERS where TRIGGER_SCHEMA = 'inventory'", "Base de Datos", "Listar Triggers", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("Se obtuvieron " + 50 + " triggers");
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                String nombre = resultado.getString("TRIGGER_NAME");
                System.out.println(nombre);
                modelo.setValueAt(nombre, fila, 0);
                fila++;
            }
            tblTriggers.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar las triggers de la base de datos");
            System.out.println(error.toString());
        }
    }
    
    private void listarVistas(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre");
            ResultSet resultado = conexion.listarRegistros("select table_name from information_schema.VIEWS where TABLE_SCHEMA = 'inventory'", "Base de Datos", "Listar Vistas", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("Se obtuvieron " + 50 + " vistas");
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                String nombre = resultado.getString("table_name");
                System.out.println(nombre);
                modelo.setValueAt(nombre, fila, 0);
                fila++;
            }
            tblVistas.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar las vistas de la base de datos");
            System.out.println(error.toString());
        }
    }
    
    private void listarContenido(String nombre_tabla){
        try {
            ResultSet resultado = conexion.listarRegistros("select * from " + nombre_tabla, "Base de Datos", "Listar Contenido", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //mostrar en la pantalla de codigo la consulta
            txtInstruccionSQL.setText("select * from " + nombre_tabla);
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(numero_de_columnas);
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x);
                modelo.addColumn(nombre_columna); 
                System.out.println("Se la columna " + nombre_columna + " tablas");
            }
            modelo.setRowCount(100000);
            fila = 0;
            while(resultado.next()){
                for(int r = 1; r<=numero_de_columnas; r++) {
                    String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r));
                    System.out.println("Fila " + fila + " Columna " +  r + " = " +valor);
                    modelo.setValueAt(valor, fila, r-1);
                }
                fila++;
            }
            tblRegistros.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar registros de la tabla " + nombre_tabla);
            System.out.println(error.toString());
        }
    }
    
    private void listarDescripcion(String nombre_tabla){
        try {
            ResultSet resultado = conexion.listarRegistros("desc " + nombre_tabla, "Base de Datos", "Listar Descripcion", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(numero_de_columnas);
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x);
                modelo.addColumn(nombre_columna); 
                System.out.println("Se la columna " + nombre_columna + " tablas");
            }
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                for(int r = 1; r<=numero_de_columnas; r++) {
                    String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r));
                    System.out.println("Fila " + fila + " Columna " +  r + " = " +valor);
                    modelo.setValueAt(valor, fila, r-1);
                }
                fila++;
            }
            tblDescripcion.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar la descripcion de la tabla " + nombre_tabla);
            System.out.println(error.toString());
        }
    }
    
    private void listarDescripcionTriggers(String nombre){
        try {
            String sql = "select * from information_schema.TRIGGERS where TRIGGER_SCHEMA = 'inventory' and TRIGGER_NAME = '"+nombre+"'";
            ResultSet resultado = conexion.listarRegistros(sql, "Base de Datos", "Listar Descripcion Triggers", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(2);
            modelo.setRowCount(50);
            modelo.addColumn("Propiedad");
            modelo.addColumn("Valor"); 
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x); 
                System.out.println("Se encontro la columna " + nombre_columna);
                modelo.setValueAt(nombre_columna, x-1, 0);
            }
            fila = 0;
            while(resultado.next()){
                for(int r = 1; r<=numero_de_columnas; r++) {
                    String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r));
                    System.out.println("Se encontro el valor " +  r + " = " +valor);
                    modelo.setValueAt(valor, r-1, 1);
                }
                fila++;
            }
            tblDescripcion.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar la descripcion del trigger " + nombre);
            System.out.println(error.toString());
        }
    }
    
    private void listarProcesos(){
        try {
            ResultSet resultado = conexion.listarRegistros("show status like 'Threads%'", "Base de Datos", "Listar Procesos", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            System.out.println("show status like 'Threads%'");
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(numero_de_columnas);
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x);
                modelo.addColumn(nombre_columna); 
                System.out.println("Se encontro la columna " + nombre_columna);
            }
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                for(int r=1; r<=numero_de_columnas; r++) {
                    try {
                        System.out.println("Asignando valores a la lista de procesos columna " + r);
                        String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r)).toString();
                        System.out.println("Fila " + fila + " Columna " +  r  + " = " + valor);
                        modelo.setValueAt(valor, fila, r-1);
                        System.out.println("Valor asignado: " + valor);
                    } catch(Exception error) {
                        System.out.println("Error en le indice del modelo");
                    }
                }
                fila++;
            }
            System.out.println("Asignando modelo, filas: " + modelo.getRowCount() + " columnas: " + modelo.getColumnCount());
            tblProcesos.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar procesos ");
            System.out.println(error.toString());
        }
    }
    
    private void listarUsuarios(){
        try {
            ResultSet resultado = conexion.listarRegistros("show processlist", "Base de Datos", "Listar Usuarios", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(numero_de_columnas);
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x);
                modelo.addColumn(nombre_columna); 
                System.out.println("Se la columna " + nombre_columna + " tablas");
            }
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                for(int r = 1; r<=numero_de_columnas; r++) {
                    String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r));
                    System.out.println("Fila " + fila + " Columna " +  r + " = " +valor);
                    modelo.setValueAt(valor, fila, r-1);
                }
                fila++;
            }
            tblUsuarios.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al listar usuarios conectados ");
            System.out.println(error.toString());
        }
    }
    
    private void mostrarCodigoTabla(String nombre_tabla){
        try {
            ResultSet resultado = conexion.listarRegistros("show create table " + nombre_tabla, "Base de Datos", "Mostrar Codigo Tabla", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            while(resultado.next()){
                String codigo = resultado.getString("Create Table");
                System.out.println("Codigo de la tabla " + nombre_tabla);
                System.out.println(codigo);
                txtCodigo.setText(codigo);
            }
        } catch (SQLException error) {
            System.out.println("Error al listar codigo de la tabla " + nombre_tabla);
            System.out.println(error.toString());
        }
    }
    
    private void listarResultadoSQL(String instruccion_sql){
        try {
            ResultSet resultado = conexion.listarRegistros(instruccion_sql, "Base de Datos", "Listar Resultado SQL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //crear las columnas correspondientes
            DefaultTableModel modelo = new DefaultTableModel();
            int numero_de_columnas = resultado.getMetaData().getColumnCount();
            System.out.println("Numero de columnas " + numero_de_columnas);
            modelo.setRowCount(numero_de_columnas);
            for(int x = 1; x<=numero_de_columnas; x++) {
                String nombre_columna = resultado.getMetaData().getColumnLabel(x);
                modelo.addColumn(nombre_columna); 
                System.out.println("Se la columna " + nombre_columna + " tablas");
            }
            modelo.setRowCount(50);
            fila = 0;
            while(resultado.next()){
                for(int r = 1; r<=numero_de_columnas; r++) {
                    String valor = resultado.getString(resultado.getMetaData().getColumnLabel(r));
                    System.out.println("Fila " + fila + " Columna " +  r + " = " +valor);
                    modelo.setValueAt(valor, fila, r-1);
                }
                fila++;
            }
            tblRegistros.setModel(modelo);
        } catch (SQLException error) {
            System.out.println("Error al ejecutar consulta " + instruccion_sql);
            System.out.println(error.toString());
        }
    }
    
    public void crearArchivoCSV(String consulta, String direccion) {
        
        String ruta = direccion;
        String cadena = "";
        File archivo = new File(ruta);
        BufferedWriter bw;
        
        //ejecutar consulta y contaner lo
        //datos para crear el archivo csv
        try {
            ResultSet rs = conexion.listarRegistros(consulta, "Base de Datos", "Crear Archivo CSV", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            while(rs.next()){
                System.out.println("Incializando sub cadena vacia");
                String sub_cadena = "";
                //contateno la informacion de todas las
                //columnas que traiga el resultado
                //de la consulta ejecutada
                for(int c=1; c<=rs.getMetaData().getColumnCount(); c++){
                    System.out.println("Concatenando columna " + rs.getMetaData().getColumnName(c));
                    sub_cadena = sub_cadena + rs.getString(c)  + ",";
                    System.out.println("Sub Cadena: " + sub_cadena);
                }
                cadena = cadena + sub_cadena.substring(1, sub_cadena.length() - 1) + "\n";
                System.out.println("Agregado a cadena: " + cadena);
            }
            rs.close();
            System.out.println("Cadena Obtenida: " + cadena);
        } catch(Exception e) {
            System.out.println("Error al ejeccutar consulta para obtener la cadena que se enviara al archivo csv");
            System.out.println(e.toString());
        }
        
        System.out.println("Cadena Total " + cadena);
        
        try {
            if (archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(cadena);
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(cadena);
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error al escribir arhivo: " + e.toString());
        }
        
    }
    
    public void buscarPalabra(JTextArea area, String texto) {
        
        if (texto.length() >= 1) {
            
            //marcar las palabras
            DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
            Highlighter h = area.getHighlighter();
            //h.removeAllHighlights();
            
            //buscar en todo el texto la palabra
            String text = area.getText();
            System.out.println("Buscando palabra en el texto " + text);
            String caracteres = texto;
            Pattern p = Pattern.compile("(?i)" + caracteres);
            Matcher m = p.matcher(text);
            
            while (m.find()) {
                try {
                    h.addHighlight(m.start(), m.end(), highlightPainter);
                } catch (BadLocationException ex) {
                    System.out.println("Error al buscar el texto " + texto);
                }
            }
            
        } else {
            JOptionPane.showMessageDialog(area, "La palabra a buscar no puede ser vacia");
        }

    }
    
    public void limpiarTablasDeInformacion(){
        DefaultTableModel modelo = new DefaultTableModel();
        tblDescripcion.setModel(modelo);
        txtCodigo.setText("");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTablas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblVistas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblProcedimientos = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblFunciones = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblTriggers = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRegistros = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblDescripcion = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtError = new javax.swing.JTextArea();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInstruccionSQL = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblProcesos = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnExportar = new javax.swing.JButton();
        btnCrearDB = new javax.swing.JButton();
        btnEjecutar = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Mantenimiento Base de Datos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBaseDeDatosMantenimiento.gif"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblTablas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTablas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTablasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblTablas);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Tabla", jPanel3);

        tblVistas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblVistas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVistasMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblVistas);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Vista", jPanel4);

        tblProcedimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProcedimientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProcedimientosMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblProcedimientos);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Procedimiento", jPanel5);

        tblFunciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblFunciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFuncionesMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblFunciones);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Funcion", jPanel6);

        tblTriggers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTriggers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTriggersMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblTriggers);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Triggers", jPanel7);

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane2.setViewportView(tblRegistros);

        jTabbedPane2.addTab("Registros", jScrollPane2);

        txtCodigo.setColumns(20);
        txtCodigo.setRows(5);
        jScrollPane5.setViewportView(txtCodigo);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Codigo", jPanel8);

        jScrollPane7.setViewportView(tblDescripcion);

        jTabbedPane2.addTab("Descripcion", jScrollPane7);

        txtError.setBackground(new java.awt.Color(102, 102, 102));
        txtError.setColumns(20);
        txtError.setEditable(false);
        txtError.setForeground(new java.awt.Color(255, 255, 255));
        txtError.setRows(5);
        txtError.setAutoscrolls(false);
        jScrollPane6.setViewportView(txtError);

        jTabbedPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtInstruccionSQL.setColumns(20);
        txtInstruccionSQL.setLineWrap(true);
        txtInstruccionSQL.setRows(1000);
        txtInstruccionSQL.setToolTipText("Ingrese las instrucciones SQL");
        txtInstruccionSQL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInstruccionSQLKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtInstruccionSQL);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Instucciones SQL", jPanel9);

        tblProcesos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProcesos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProcesosMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblProcesos);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Procesos Activos", jPanel10);

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblUsuarios);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Usuarios Conectados", jPanel11);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6)
                            .addComponent(jTabbedPane2)))
                    .addComponent(jTabbedPane3))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("Descripcion");
        jTabbedPane3.getAccessibleContext().setAccessibleName("");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonExportar.png"))); // NOI18N
        btnExportar.setText("Exportar");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });

        btnCrearDB.setText("Crear Base de Datos");
        btnCrearDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearDBActionPerformed(evt);
            }
        });

        btnEjecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEjecutar.png"))); // NOI18N
        btnEjecutar.setText("Ejecutar");
        btnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCrearDB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEjecutar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportar)
                    .addComponent(btnCrearDB)
                    .addComponent(btnEjecutar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblTablasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTablasMouseClicked
        listarContenido(tblTablas.getValueAt(tblTablas.getSelectedRow(), 0).toString());
        mostrarCodigoTabla(tblTablas.getValueAt(tblTablas.getSelectedRow(), 0).toString());
        listarDescripcion(tblTablas.getValueAt(tblTablas.getSelectedRow(), 0).toString());
    }//GEN-LAST:event_tblTablasMouseClicked

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarActionPerformed
        txtError.setText("");
        listarResultadoSQL(txtInstruccionSQL.getText());
        limpiarTablasDeInformacion();
    }//GEN-LAST:event_btnEjecutarActionPerformed

    private void tblVistasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVistasMouseClicked
        listarContenido(tblVistas.getValueAt(tblVistas.getSelectedRow(), 0).toString());
    }//GEN-LAST:event_tblVistasMouseClicked

    private void txtInstruccionSQLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInstruccionSQLKeyPressed
        //buscarPalabra(txtInstruccionSQL, "select");
    }//GEN-LAST:event_txtInstruccionSQLKeyPressed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        String consulta = txtInstruccionSQL.getText();
        JFileChooser flcArchivo = new JFileChooser(".");
        flcArchivo.setApproveButtonText("Guardar");
        int estado = flcArchivo.showOpenDialog(null);        
        if (estado == JFileChooser.APPROVE_OPTION) {
            File archivo_seleccionado = flcArchivo.getSelectedFile();
            String direccion = archivo_seleccionado.getParent()+"\\"+archivo_seleccionado.getName();            
            ServicioLeerArchivoExcel archivo = new ServicioLeerArchivoExcel();
            crearArchivoCSV(consulta, direccion);
            mensaje.manipulacionExcepciones("informacion", "El archivo fue generado con exito.", "Boton Exportar");
        } else if (estado == JFileChooser.CANCEL_OPTION) {
            System.out.println("Se ha cancelado la operacion");
        }
    }//GEN-LAST:event_btnExportarActionPerformed

    private void btnCrearDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearDBActionPerformed
        //TODO Agregar tabla para ver actividad de Base de Datos
    }//GEN-LAST:event_btnCrearDBActionPerformed

    private void tblProcesosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProcesosMouseClicked
        // TODO Agrerar opcion para ver Procesos Activos
    }//GEN-LAST:event_tblProcesosMouseClicked

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuariosMouseClicked
        // TODO Agregar opcion para ver Usuarios Conectados
    }//GEN-LAST:event_tblUsuariosMouseClicked

    private void tblProcedimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProcedimientosMouseClicked
        // TODO Agregar tabla para ver lista de Procedimientos
    }//GEN-LAST:event_tblProcedimientosMouseClicked

    private void tblFuncionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFuncionesMouseClicked
        // TODO Agregar lista para ver lista de Funciones.
    }//GEN-LAST:event_tblFuncionesMouseClicked

    private void tblTriggersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTriggersMouseClicked
        listarDescripcionTriggers(tblTriggers.getValueAt(tblTriggers.getSelectedRow(), 0).toString());
    }//GEN-LAST:event_tblTriggersMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrearDB;
    private javax.swing.JButton btnEjecutar;
    private javax.swing.JButton btnExportar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable tblDescripcion;
    private javax.swing.JTable tblFunciones;
    private javax.swing.JTable tblProcedimientos;
    private javax.swing.JTable tblProcesos;
    private javax.swing.JTable tblRegistros;
    private javax.swing.JTable tblTablas;
    private javax.swing.JTable tblTriggers;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTable tblVistas;
    private javax.swing.JTextArea txtCodigo;
    public static javax.swing.JTextArea txtError;
    private javax.swing.JTextArea txtInstruccionSQL;
    // End of variables declaration//GEN-END:variables
}

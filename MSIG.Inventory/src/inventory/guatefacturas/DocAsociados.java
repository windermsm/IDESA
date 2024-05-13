/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.guatefacturas;

public class DocAsociados {
    private String DASerie;         /* @param DASerie serie del documento asociado a la nota de Credito o Debito */
    private String DAPreimpreso;    /* @param DAPreimpreso Preimpreso del documento asociado a la nota de Credito o Debito */

    public String getDASerie() {
        return DASerie;
    }

    public void setDASerie(String DASerie) {
        this.DASerie = DASerie;
    }

    public String getDAPreimpreso() {
        return DAPreimpreso;
    }

    public void setDAPreimpreso(String DAPreimpreso) {
        this.DAPreimpreso = DAPreimpreso;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.librerias;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author DSANTACRUZ
 */
public class SaveCellAdministrador extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (column == 11) {
            if (value.toString().equals("SI")) {
                setBackground(Color.GREEN);
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.RED);
                setForeground(Color.BLACK);
            }
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}

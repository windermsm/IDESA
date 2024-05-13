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
public class NormalCell extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (row%2 == 0) {
            setBackground(new Color(204, 255, 204));
            setForeground(Color.BLACK);
        } else {
            setBackground(new Color(255, 255, 224));
            setForeground(Color.BLACK);
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}

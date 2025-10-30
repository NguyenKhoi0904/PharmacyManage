/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author aries
 */

import java.awt.*;
import javax.swing.*;
public class Convert_JFrame_To_JPanel {
    public static JPanel convert_jframe_to_jpanel(JFrame jframe){
        Container container = jframe.getContentPane();
        
        JPanel jpanel = new JPanel();
        jpanel.setLayout(jframe.getLayout());
        jpanel.setBackground(container.getBackground());
        jpanel.setPreferredSize(container.getPreferredSize());
        jpanel.setBorder(BorderFactory.createEmptyBorder());
        for(Component c: container.getComponents()){
            container.remove(c);
            jpanel.add(c);
        }
        return jpanel;
    }
    
    public static Container get_components_from_jFrame(JFrame jframe){
        Container container = jframe.getContentPane();
        jframe.remove(container);
        return container;
    }
}

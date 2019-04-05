package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import settings.SettingsManager;

/**
 * This panel will dynamically filled up using the combos (settings=value) loaded from config file.
 * @author Luca Di Bello
 */
public class SettingsPanelDynamic extends JPanel{

    private SettingsManager manager = new SettingsManager();
    
    public SettingsPanelDynamic() {
        setBackground(Color.red);
        build();
    }
    
    public void build(){
        //Load options
        Map<String, String> options = manager.getSettings();
        setLayout(new java.awt.BorderLayout());
        
        setLayout(new GridLayout(options.size()+1,2));
        for(int i = 0; i < options.size();i++){
            List<String> keys = new ArrayList<>(options.keySet());
            List<String> values = new ArrayList<>(options.values());

            System.out.println("key: " + keys.get(i) + " Value: " + values.get(i));
        
            JLabel settingName = new JLabel();
            settingName.setText("pota");
            //settingName.setText(keys.get(i));
            
            //JTextField settingField = new JTextField(values.get(i));
            
            //dd(settingField);
            add(settingName, java.awt.BorderLayout.CENTER);
        }
        //Apply button
        JButton apply = new JButton("Apply changes");
        add(apply,java.awt.BorderLayout.CENTER);
    }
}

package gui;

import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import settings.SettingsManager;

/**
 * This panel will dynamically filled up using the combos (settings=value) loaded from config file.
 * @author Luca Di Bello
 */
public class SettingsPanel extends JPanel{

    private SettingsManager manager = new SettingsManager();
    
    public SettingsPanel() {
        build();
    }
    
    
    private void build(){
        //Load options
        Map<String, String> options = manager.getSettings();
        setLayout(new GridLayout(options.size()+1,2));
        
        for(int i = 0; i < options.size();i++){
            List<String> keys = new ArrayList<String>(options.keySet());
            List<String> values = new ArrayList<String>(options.values());
            
            JLabel settingName = new JLabel(keys.get(i));
            JTextField settingField = new JTextField(values.get(i));
            
            add(settingName);
            add(settingField);
        }
        
        //Apply button
        JButton apply = new JButton("Apply changes");
        add(apply);
    }
}

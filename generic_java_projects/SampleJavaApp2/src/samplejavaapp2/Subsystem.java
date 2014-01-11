/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp2;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsystem Class
 * Contains collections of Widget objects
 * @author Rob
 */
public class Subsystem {
    
    private String name;
    private List<SpecialtyWidget> myWidgets;
    
    // no argument constructor
    public Subsystem() {
        this.name = "defaultSubsystemName";
                
        // create an empty list of widgets
        myWidgets = new ArrayList<SpecialtyWidget>();
        
        System.out.println("Subsystem constructor:  name = " + name + ", size = " + myWidgets.size());
    }
    
    // argument constructor
    public Subsystem(String name, int size) {
        this.name = name;
        
        // create and fill widget list
        myWidgets = new ArrayList<SpecialtyWidget>();
        for (int i=0; i<size; i++)
        {
            myWidgets.add(new SpecialtyWidget(new String(this.name + "_Widget_"+i), i , i+10, i+20));
        }
        
        System.out.println("Subsystem constructor:  name = " + this.name + ", size = " + myWidgets.size());

    }
    
    // set and get - name
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // set and get - size
    public int getSize() {
        return myWidgets.size();
    }
    
    public void setSize(int size) {
        // create and fill widget list
        myWidgets = new ArrayList<SpecialtyWidget>();
        for (int i=0; i<size; i++)
        {
            myWidgets.add(new SpecialtyWidget(new String("Widget_"+i), i , i+10, i+20));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp2;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget Class 
 * Shows base class
 * See also SpecialtyWidget derived class
 * Contains collections of Cog objects
 * @author Rob
 */
public class Widget {
    
    private String name;
    private List<Cog> myCogs;
    
    // no argument constructor
    public Widget() {
        name = "defaultWidgetName";
        myCogs = new ArrayList<Cog>();
        
        System.out.println("Widget constructor:  name = " + name + ", size = " + myCogs.size());
    }
    
    // argument constructor
    public Widget(String name, int size) {
        this.name = name;
        
        myCogs = new ArrayList<Cog>();
        for (int i=0; i<size; i++) {
            myCogs.add(new Cog(new String(this.name + "_Cog_" + i),i));
        }

        System.out.println("Widget constructor:  name = " + this.name + ", size = " + myCogs.size());
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
        return myCogs.size();
    }
    
    public void setSize(int size) {
        myCogs = new ArrayList<Cog>();
        for (int i=0; i<size; i++) {
            myCogs.add(new Cog(new String(this.name + "_Cog_" + i),i));
        }
    }
    
}

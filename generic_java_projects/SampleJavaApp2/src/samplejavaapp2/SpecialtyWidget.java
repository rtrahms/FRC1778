/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rob
 */
public class SpecialtyWidget extends Widget {
    
    private int color;
    private int weight;
        
    // default constructor
    public SpecialtyWidget() {

        this.color = 20;
        this.weight = 30;
        
        System.out.println("SpecialtyWidget constructor: name = " + this.getName() + ", size = " + this.getSize() + ", color = " + this.color + ", weight = " + this.weight); 
    }
    
    // constructor
    public SpecialtyWidget(String name, int size, int color, int weight) {
        super.setName(name);
        super.setSize(size);
        this.color = color;
        this.weight = weight;
        
        System.out.println("SpecialtyWidget constructor: name = " + this.getName() + ", size = " + this.getSize() + ", color = " + this.color + ", weight = " + this.weight);
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return this.weight;
    }
}

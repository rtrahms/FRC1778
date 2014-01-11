/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package samplejavaapp2;

/**
 *
 * @author Rob
 */
public class Cog {
    
    private String name;
    private int size;
    
    // no argument constructor
    public Cog() {
        this.name = "defaultCogName";
        this.size = 10;
        
        System.out.println("Cog constructor:  name = " + name + ", size = " + size);
    }
    
    // argument constructor
    public Cog(String name, int size) {
        this.name = name;
        this.size = size;
        
        System.out.println("Cog constructor:  name = " + name + ", size = " + size);
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
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
}

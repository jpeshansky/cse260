/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Set;

public class MoleculeBucket extends HashMap<String, Molecule> {
	
	public Molecule getMolecule(String name) {
		return this.get(name);
	}
	public void putMolecule(String name, Molecule m) {
		this.put(name, m);
	}
	
	public String getName(Molecule m) {
		for (String s: this.keySet()) {
			if (this.get(s).equals(m)) return s;
		}
		return null;
	}
	
	public String[] getNames() {
		return (String[]) this.keySet().toArray();
	}
}

/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import graphs.Vertex;

public class Radical implements Vertex<String>{
	private String name;
	private HashMap<Radical, Integer> neighbors;
	
	public Radical(String name) {
		this.name = name;
		neighbors = new HashMap<Radical, Integer>();
	}

	@Override
	public String getLabel() {
		return name;
	}
	
	public void addNeighbor(Radical r, int bonds) {
		neighbors.put(r, (Integer)bonds);
	}
	
	public int getBonds(Radical r) {
		return neighbors.get(r);
	}
	
	public ArrayList<Radical> getNeighbors() {
		ArrayList<Radical> n = new ArrayList<Radical>();
		for (Entry<Radical, Integer> r: neighbors.entrySet()) {
			n.add(r.getKey());
		}
		return n;
	}
}

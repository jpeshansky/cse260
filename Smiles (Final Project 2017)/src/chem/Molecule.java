/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.ArrayList;
import java.util.HashMap;

import graphs.Graph;
import graphs.Vertex;

public class Molecule implements Graph<String, Integer>{
	private ArrayList<Radical> nodes;
	
	public Molecule(){
		nodes = new ArrayList<Radical>();
	}

	@Override
	public Vertex<String> createNode(String label) {
		Radical r = new Radical(label);
		nodes.add(r);
		return r;
	}

	@Override
	public void addEdge(Vertex<String> n1, Vertex<String> n2, Integer label) {
		((Radical)n1).addNeighbor((Radical)n2, (Integer)label);
		((Radical)n2).addNeighbor((Radical)n1, (Integer)label);
	}
	
	public void addEdge(Integer label) { //automatically adds bond between last two atoms
		addEdge(nodes.get(nodes.size()-1), nodes.get(nodes.size()-2), label);
	}

	@Override
	public ArrayList<? extends Vertex<String>> getNodes() {
		return nodes;
	}

	@Override
	public Integer getEdgeLabel(Vertex<String> n1, Vertex<String> n2) {
		return ((Radical)n1).getBonds((Radical)n2);
	}

	@Override
	public ArrayList<? extends Vertex<String>> getNeighbors(Vertex<String> n) {
		return ((Radical)n).getNeighbors();
	}
	
	public String toString() {
		String result = "";
		for (Radical r: nodes) {
			result += r.getLabel() + " "; 
		}
		return result;
	}
	
}

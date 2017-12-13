/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) graphs
 */
package graphs;

import java.util.List;

public interface Graph<NL, EL> {
	
	Vertex<NL> createNode(NL label);
	
	void addEdge(Vertex<NL> n1, Vertex<NL> n2, EL label);
	
	List<? extends Vertex<NL>> getNodes();
	
	EL getEdgeLabel(Vertex<NL> n1, Vertex<NL> n2);
	
	List<? extends Vertex<NL>> getNeighbors(Vertex<NL> n);
}

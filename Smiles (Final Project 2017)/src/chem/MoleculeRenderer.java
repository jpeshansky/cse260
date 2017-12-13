/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.HashSet;

//import static guru.nidi.graphviz.model.Factory.*
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MoleculeRenderer {
	private HashSet<Radical> renderedAtoms;
	private Molecule m;

	public MoleculeRenderer(Molecule m) {
		renderedAtoms = new HashSet<Radical>();
		this.m = m;
		//Graph g = graph("example1").undirected().with(node("a").link(node("b")));
	}

	public void drawMolecule(GraphicsContext gc) {
		drawMolecule(gc, 20, 60, null);
	}

	public void drawMolecule(GraphicsContext gc, int x, int y, Radical r) {
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLUE);

		if (r == null) r = (Radical)m.getNodes().get(0);
		drawAtom(gc, x, y);
		System.out.println("Drew an atom at " + x + ", "+y + ", and also it was the atom " + r.getLabel());
		renderedAtoms.add(r);
		System.out.println("Added " + r.getLabel() + " to renderedAtoms.");
		int count = 0; 
		System.out.println("Set count to 0.");
		for (Radical n: r.getNeighbors()) {
			if (!(renderedAtoms.contains(n))) {
				count++;
				if (count >= 1) {
					for (int j = 0; j < m.getEdgeLabel(r, n); j++) {
						int shiftY = (-1*(j%2))*(j+1/2);
						System.out.println("shiftY: "+ shiftY + " Count = " + count);
						drawBond(gc, x, y, x+20, y+shiftY);
					}
					drawMolecule(gc, x+20, y, n);
				}
				if (count >= 2) {
					for (int j = 0; j < m.getEdgeLabel(r, n); j++) {
						int shiftX = (-1*(j%2))*(j+1/2);
						System.out.println("shiftX: "+ shiftX + " Count = " + count);
						drawBond(gc, x, y, x+shiftX, y-30);
					}
					drawMolecule(gc, x, y-30, n);
				}
				if (count >= 3) {
					for (int j = 0; j < m.getEdgeLabel(r, n); j++) {
						int shiftX = (-1*(j%2))*(j+1/2);
						System.out.println("shiftX: "+ shiftX + " Count = " + count);
						
						drawBond(gc, x, y, x+shiftX, y+30);
					}						
					drawMolecule(gc, x, y+30, n);
				}

			}
		}
	}

	private void drawAtom(GraphicsContext gc, int x, int y) {
		gc.fillOval(x, y, 30, 30);
	}

	private void drawBond(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		gc.setLineWidth(5);
		gc.strokeLine(x1, y1, x2, y2);
	}
}

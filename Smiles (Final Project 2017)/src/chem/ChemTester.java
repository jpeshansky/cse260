/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 30, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class ChemTester extends Application {
	public static void main(String[] args) {
		/*
		String smiles = "[CH3]-[CH2]-[N]([CH2]-[CH3])[CH2-]-[CH3+]";
		Parser p = new Parser();
		Molecule m = p.parse(smiles);
		System.out.println("The entered molecule is " + m.toString());
		MoleculeBucket bucket = new MoleculeBucket();
		bucket.put(smiles, m);
		Molecule m1 = bucket.get(smiles);
		
		System.out.println(bucket.getName(m1));
		MoleculeRenderer renderer = new MoleculeRenderer(m1);
		*/
		launch(args);
		
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Draw Molecule");
		Group root = new Group();
		Canvas canvas = new Canvas(300, 250);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		String smiles = "[CH3]-[CH2]-[N]([CH2]-[CH3])[CH2-]-[CH3+]";
		Parser p = new Parser();
		Molecule m = p.parse(smiles);
		MoleculeBucket bucket = new MoleculeBucket();
		System.out.println("Molecule input: "+ m.toString());
		bucket.put(smiles, m);
		Molecule m1 = bucket.get(smiles);
		
		System.out.println(bucket.getName(m1));
		MoleculeRenderer renderer = new MoleculeRenderer(m1);
		renderer.drawMolecule(gc);
		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}

/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class SmileyFace extends Application {
	private Stage myStage;
	private Scene introScene;
	private Scene mainScene;
	private Scene helpScene;
	private Scene goodbyeScene;
	private MoleculeBucket bucket;
	private MoleculeRenderer renderer;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		myStage = primaryStage;
		bucket = new MoleculeBucket();
		Parser p = new Parser();
		bucket.put("[CH3]-[CH2]-[N]([CH2]-[CH3])[CH2-]-[CH3+]", p.parse("[CH3]-[CH2]-[N]([CH2]-[CH3])[CH2-]-[CH3+]"));
		bucket.put("[NH4]", p.parse("[NH4]"));


		openSplashScreen();
	}

	private void openSplashScreen() {
		BorderPane intro = new BorderPane();
		introScene = new Scene(intro, 375, 250);
		myStage.setTitle("SmileyFace");
		myStage.setScene(introScene);
		VBox vbox = new VBox();
		intro.setCenter(vbox);
		vbox.setPadding(new Insets(15,12,15,12));
		vbox.setSpacing(30);
		Text title = new Text("Welcome to SmileyFace! :)");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
		vbox.getChildren().add(title);
		Button btnOpenNew = new Button("Open New File");
		Button btnLoadSaved = new Button("Open Saved File");
		Button btnQuit = new Button("Quit");

		btnOpenNew.setOnAction(e -> openMainScreen());	
		btnLoadSaved.setOnAction(e -> {
			//TODO set up the rest
			openMainScreen();
			//doOtherStuff();
		});	
		btnQuit.setOnAction(e -> myStage.close()); //anyone closing from the splash screen doesn't need to save

		vbox.getChildren().addAll(btnOpenNew, btnLoadSaved, btnQuit);

		myStage.show();		
	}
	private void openMainScreen() {
		BorderPane root = new BorderPane();
		mainScene = new Scene(root, 700, 500);
		myStage.setTitle("SmileyFace");
		myStage.setScene(mainScene);
		VBox menu = new VBox();
		menu.setPadding(new Insets(15,12,15,12));
		menu.setSpacing(10);
		menu.setMaxWidth(200);
		FlowPane drawSpace = new FlowPane();
		root.setRight(menu);
		root.setCenter(drawSpace);
		drawSpace.setStyle("-fx-background-color: white;");
		
		// create an ListView for molecules 
		ListView<String> moleculeSelect = new ListView<>(); 
		ArrayList<String> moleculesToSelect = new ArrayList<String>();
		moleculeSelect.getItems().setAll(moleculesToSelect); 

		//ArrayList<String> moleculeList = new ArrayList<String>();
		//final ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, moleculeList);
		
		moleculeSelect.setPrefSize(Double.MAX_VALUE, 75);
		Button btnDisplayMolecule = new Button("Display");
		btnDisplayMolecule.setPrefWidth(Double.MAX_VALUE);
		Button btnClearAll = new Button("Clear All");
		btnClearAll.setPrefWidth(Double.MAX_VALUE);
		HBox deleteFirstLast = new HBox();
		deleteFirstLast.setPrefWidth(Double.MAX_VALUE);
		TextField textEnterMolecule = new TextField();
		textEnterMolecule.setPromptText("Enter SMILES string");
		TextField textMoleculeName = new TextField();
		textMoleculeName.setPromptText("Enter name (optional)");
		textEnterMolecule.setPrefWidth(Double.MAX_VALUE);
		textMoleculeName.setPrefWidth(Double.MAX_VALUE);
		Button btnEnterMolecule = new Button("Enter New Molecule");
		btnEnterMolecule.setPrefWidth(Double.MAX_VALUE);
		HBox helpSaveExit = new HBox();
		menu.getChildren().addAll(moleculeSelect, btnDisplayMolecule, btnClearAll,
				deleteFirstLast, textMoleculeName, textEnterMolecule, btnEnterMolecule, helpSaveExit);


		btnDisplayMolecule.setOnAction(e -> {
			String s1 = moleculeSelect.getAccessibleText();//gets text currently selected 
			Molecule m = bucket.getMolecule(s1);
			/*renderer = new MoleculeRenderer(m);
			Canvas c = new Canvas(100,100);
			GraphicsContext gc = c.getGraphicsContext2D();
			renderer.drawMolecule(gc);
			*/
			Text c = new Text(s1);
			drawSpace.getChildren().add(c);
		});
		btnEnterMolecule.setOnAction(e -> {
			try { 
				String userMoleculeName = textMoleculeName.getText(); //gets text in text field, if not empty
				String userSmiles = textEnterMolecule.getText(); //gets text currently in text field
				if (userMoleculeName.length() < 1) userMoleculeName = userSmiles;
				String name = addMolecule(userMoleculeName, userSmiles); //adds molecule to the system, and gets the name
				moleculesToSelect.add(name);
				moleculeSelect.getItems().setAll(moleculesToSelect); 
				textEnterMolecule.clear();
			} catch (Exception ex) {
				System.out.println("That's not a valid SMILES string.");
			}

		});

		btnClearAll.setOnAction(e -> drawSpace.getChildren().clear());
		//clear all molecules being displayed
		Button btnDeleteFirst = new Button("Delete First");
		btnDeleteFirst.setOnAction(e -> drawSpace.getChildren().remove(0));
		//delete the first molecule being displayed and shift the rest up
		Button btnDeleteLast = new Button("Delete Last");
		btnDeleteLast.setOnAction(e -> 
		drawSpace.getChildren().remove(drawSpace.getChildren().size()-1));
		//delete the last molecule being displayed
		deleteFirstLast.getChildren().addAll(btnDeleteFirst, btnDeleteLast);
		deleteFirstLast.setSpacing(25);
		
		Button btnHelp = new Button("Help");
		btnHelp.setOnAction(e -> openHelpScreen()); //open the help screen
		Button btnSave = new Button("Save");
		btnSave.setOnAction(e -> saveCurrentFile()); //save all molecules in the system
		Button btnExit = new Button("Quit");
		btnExit.setOnAction(e -> openGoodbyeScreen()); //open the goodbye screen
		helpSaveExit.getChildren().addAll(btnHelp, btnSave, btnExit);
		helpSaveExit.setPrefWidth(Double.MAX_VALUE);
		helpSaveExit.setSpacing(26);


	}
	private void openHelpScreen() {
		BorderPane root = new BorderPane();
		helpScene = new Scene(root, 700, 500);
		myStage.setTitle("Help");
		myStage.setScene(helpScene);
		ScrollPane scroll = new ScrollPane();
		root.setCenter(scroll);
		scroll.setFitToHeight(true);		
		scroll.setFitToWidth(true);
		VBox content = new VBox();
		scroll.setContent(content);
		Button btnBack = new Button("Back"); //close the help screen
		btnBack.setOnAction(e -> openMainScreen());
		content.setPadding(new Insets(10, 10, 20, 20));
		
		TextFlow instructions = new TextFlow();

				Text i1 = new Text("So what the heck is this weird smiling thing, and how do I use it?\n"
				+ "\n"
				+ "A SMILES string is comprised of letters, numbers, symbols, and no spaces. Note that due to the \n"
				+ "nature of SMILES, there can be multiple ways to represent the same molecule. \n" 
				+ "\n"
				+ "Atoms are represented by their atomic symbol, which is the only use of letters in Smiles. This can \n"
				+ "be any element on the periodic table. The first letter of the atomic symbol must be uppercase, and \n"
				+ "the second, if there is one, must be lowercase.\n"
				+ "\n"
				+ "Radicals are enclosed in square brackets. A single atom would be specified by its atomic symbol \n"
				+ "enclosed in square brackets; for example, [C] is elemental carbon, [Au] is elemental gold, [Fe] is \n"
				+ "elemental iron. These atoms have no charge, no isotopes, and are in their simplest forms.\n"
				+ "\n"
				+ "Elements in the organic subset, B, C, N, O, P, S, F, Cl, Br, and I, can be written without brackets \n"
				+ "if all of their normal bonds are filled. This means B has 3 attachments, C (4), N (3,5), O (2), P \n"
				+ "(3,5), S (2,4,6), and 1 for the halogens. Any unfilled bonds will be filled with hydrogens. Every \n"
				+ "element that doesn't fit these requirements must be specified in brackets.\n"
				+ "/r/n"
				);
				
				Text i2 = new Text("A radical can be a single atom within the square brackets, but it also can contain more \n"
				+ "specifications. The number of hydrogens attached to the atom is shown by H followed by an \n"
				+ "optional digit. A formal charge is specified by a + or -, followed by an optional digit. If not \n"
				+ "specified, the number of hydrogens and charge are both assumed to be 0. [Fe+++] means the same \n"
				+ "thing as [Fe+3], and can be written either way.\n"
				+ "\n"
				);
				
				Text i3 = new Text("Bonds are represented by -, =, and # for single, double, and triple bonds respectively, and : for \n"
				+ "aromatic bonds. However, single and aromatic bonds do not have to be specified. Adjacent atoms \n"
				+ "are assumed by default to be connected by one of these bonds. \n"
				+ "\n"
				+ "Branching is represented by enclosing the branch in parentheses. A branch off a branch can be \n"
				+ "parentheses within parentheses. The parentheses go right after the atom that the branch is \n"
				+ "connected to. After the parentheses close, the following atoms continue from the same atom where \n"
				+ "the branch began.\n"
				+ "\n"				
				);
				
				instructions.getChildren().addAll(i1, i2, i3);
		
		i1.setFont(Font.font("Times New Roman", 16));
		i2.setFont(Font.font("Times New Roman", 16));
		i3.setFont(Font.font("Times New Roman", 16));

		
		content.getChildren().add(instructions);
		
		
		content.getChildren().add(btnBack);
		
		
	}

	private void openGoodbyeScreen() {		
		BorderPane root = new BorderPane();
		goodbyeScene = new Scene(root, 500, 250);
		myStage.setTitle("Goodbye");
		myStage.setScene(goodbyeScene);
		VBox vbox = new VBox();
		root.setCenter(vbox);
		vbox.setPadding(new Insets(15,15,15,15));
		vbox.setSpacing(30);
		Text title = new Text("Thank you for using SmileyFace.");
		Text smile = new Text(":)");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
		smile.setTextAlignment(TextAlignment.CENTER);
		smile.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
		vbox.getChildren().addAll(title, smile);
		HBox hbox = new HBox();
		root.setBottom(hbox);
		hbox.setPadding(new Insets(15,12,15,12));
		hbox.setSpacing(30);
		Button btnSave = new Button("Save And Quit");
		Button btnQuit = new Button("Quit Without Saving");
		Button btnCancel = new Button("Cancel");

		btnSave.setOnAction(e -> {
			saveCurrentFile(); //save file
			myStage.close(); //close program
		});	
		btnQuit.setOnAction(e -> myStage.close()); //close program
		btnCancel.setOnAction(e -> openMainScreen()); //go back to main screen

		vbox.getChildren().addAll(btnSave, btnQuit, btnCancel);
	}

	private void saveCurrentFile() {

	}

	private String addMolecule(String name, String smiles) throws Exception {
		Parser p = new Parser(); //creates parser
		Molecule m = p.parse(smiles); //assigns parsed string to a molecule
		bucket.put(name, m); //puts the molecule into the bucket.
		return bucket.getName(m);//returns name of molecule in moleculeBucket
	}

}

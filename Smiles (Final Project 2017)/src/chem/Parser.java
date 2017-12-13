/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Nov 25, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import graphs.Vertex;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Parser {
	public HashMap<String, Pattern> patterns;

	private void addPatterns() {
		add("atom", "A[cglmrstu]|B[aehikr]?"
				+ "|C[adeflmorsu]?|D[bsy]|E[rsu]|F[emr]?|G[ade]|H[efgos]?"
				+ "|I[nr]?|Kr?|L[airu]|M[dgnot]|N[abdeiop]?|Os?|P[abdmortu]?"
				+ "|R[abefghnu]|S[bcegimnr]?|T[abcehilm]|Uu[bhopqstx]|U|V|W|Xe|Yb?|Z[nr]");
		add("radical", "(\\[)"      //[ (12?)? C (H3?)? (+3?)?]
				+"([0-9][0-9]?)?" //isotope
				+ "(" + patterns.get("atom") + ")" //atomic symbol
				+ "([H][0-9]?)?" //hydrogens
				+ "((\\+\\d+)|(\\++)|(-\\d+)|(-+))?" //charge
				+ "(\\])"); //end radical
		add("bond", "[-=#$]");
		add("ring closure", patterns.get("radical") + "([0-9]|[%][0-9][0-9])");
		add("branch opening", "[(]");
		add("branch closure", "[)]");
		add("dot disconnect", "[.]");
	}
	public void add(String name, String regex) throws PatternSyntaxException {
		Pattern token = Pattern.compile(regex);
		patterns.put(name, token);
	}

	public Molecule parse(String formula) {
		Molecule result = new Molecule();

		String smiles = formula;

		patterns = new HashMap<String,Pattern>();
		addPatterns();

		ArrayList<Token> tokens = new ArrayList<Token>();

		for(Map.Entry<String,Pattern> entry : patterns.entrySet()) {
			Pattern token = entry.getValue();
			Matcher matcher = token.matcher(formula);
			while(matcher.find()) {
				tokens.add(new Token(entry.getKey(), matcher.group()));
				//System.out.printn(entry.getKey() + " ==> " + matcher.group());
			}
		}
		//System.out.printn(tokens.toString());

		ArrayList<Vertex<String>> addedNodes = new ArrayList<Vertex<String>>();
		for (int i = 0; i < tokens.size(); i++) {
			addedNodes.add(null);
		}

		int beforeBranch = -1; //index of last vertex before branch opening
		int beginningOfBranch = -1;
		int afterBranch = -1;
		int edgeLabelDuringBranch = 1;

		for (int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if (t.getType().equals("radical")) {
				addedNodes.add(i, result.createNode(t.getText()));
			}
		}
		for (int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if ((beginningOfBranch == i || afterBranch == i)&& t.getType().equals("radical")) {
				result.addEdge(((Vertex<String>)addedNodes.get(beforeBranch)), ((Vertex<String>)addedNodes.get(i)), edgeLabelDuringBranch);
				edgeLabelDuringBranch = 0;
			}

			if (t.getType().equals("bond")) {
				if (beginningOfBranch == i) { //if this bond begins a branch
					beginningOfBranch++; //
					edgeLabelDuringBranch = Integer.parseInt(t.getText());
				}
				Vertex<String> last =  getLastAtom(addedNodes, i);
				Vertex<String> next =  getNextAtom(addedNodes, i);
				if (last != null && next != null) {
					result.addEdge(last, next, Integer.parseInt(t.getText()));
				}
				else {
					System.out.println("Last or next was null");
				}
			}
			else if (t.getType().equals("branch opening")) {
				beforeBranch = i-1;
				beginningOfBranch = i+1;
			}
			else if (t.getType().equals("branch closing")) {
				afterBranch = i+1;
			}
		}

		return result;
	}
	private Vertex<String> getNextAtom(ArrayList<Vertex<String>> addedNodes, int bond) {
		for (int i = bond; i < addedNodes.size(); i++) {
			Vertex<String> t = addedNodes.get(i);
			if (t != null) return addedNodes.get(i);		}
		return null;
	}
	private Vertex<String> getLastAtom(ArrayList<Vertex<String>> addedNodes, int bond) {
		for (int i = bond; i > 0; i--) {
			Vertex<String> t = addedNodes.get(i);
			if (t != null) return addedNodes.get(i);
		}
		return null;
	}
}

/*
	private boolean canFollow(Pattern previous, Pattern current) {
		if (previous == ' ') //if this is the start of the string
		{
			if (isAtom(current)) {
				return true; //must start with an atom
			}
			else return false; //nothing else is allowed
		}
		else if (isAtom(previous)) //an atom {
		{
			return true; //atom can be followed by anything

		}
		else if (previous == '-' || previous == '=' || previous == '#' || previous == ':') {
			if (isAtom(current)) {
				return true; //bond can be followed by atom
			}
			else if (current >= '0' && current <= '9') {
				return true; //bond can be followed by a ring closure
			}
			else return false; //bonds, open and close branches, and dots not allowed
		}
		else if (previous == '(') {
			if (current == '(') {
				return false; //cannot follow open branch with open branch
			}
			else if (current == ')') {
				return false; //cannot close branch immediately
			}
			else if (current >= '0' && current <= '9') {
				return false; //cannot follow open branch with ring closure
			}
			else return true; //atoms, bonds, and dots are allowed
		}
		else if (previous == ')') {
			return true; //anything is allowed to follow branch closure
		}
		else if (previous >= '0' && previous <= '9') {
			return true; //anything is allowed to follow a ring closure
		}
		else if (previous == '.') {
			if (isAtom(current)) {
				return true; //atom can follow a dot disconnect
			}
			else return false; //bonds, open/close branches, ring closures, & dots not allowed
		}
		return false;
	}

 */



/**
 * Specifications:
 * Within brackets [], from the opening bracket
   to the closing bracket, is one radical.
 * No spaces.
 * Bonds: - = # : (single double triple aromatic)
 * Parentheses () for branches - CCN(CC)CC. After
   the parentheses, "keeps going" along a line
 * Cyclic structures: Break an edge, label it with a 
   "flag" number 1-9, that will be used to signify where
   the ring closure should be. C1CCCCC1
 *  A dot . implies disconnected structures, unless there
    is a flag digit indicating a bond. C1.C1 == CC

 * Optional implementation:
 * Organic subset can be written without brackets, 
   all empty spaces will be filled in with hydrogens.
   B, C, N, O, P, S, F, Cl, Br, and I
 * When breaking/labeling an edge, use % to indicate
   that the following edge label will be 2 digits
 * Specifying atomic mass for isotopes: [12C] is carbon-12, 
   [13C] is carbon-13. [C] is unspecified.
 * "Directional bonds": / or \. Around a double bond
 * Extension to handle reactions
 * 
 * http://www.dalkescientific.com/writings/diary/archive/2004/01/05/tokens.html
 * SO I MUST:
 * Break things up into an array of tokens, and work from there.
 * 
 */

/*
public Molecule parseMolecule() throws ParserException {
	Token t = nextToken();
	if (t == null || t.type() != RADICAL)
		throw new ParserException("A molecule starts with a radical, not " + t);
	Molecule result = new Molecule();
	result.addRadical(new Radical(t.value()));
	while ((t = nextToken()) != null) {
		switch (t.type()) {
		case BRANCH_START: {
			Token t = nextToken();
			if (t == null || t.type() != BOND)
				throw new ParserException("A branch starts with a bond, not " + t);
			Molecule m = parseMolecule();
			Radical branchPoint = result.lastRadical();
			result.addAll(m);
			result.addBond(branchPoint, m.firstRadical(), t.value());
		}
		break;
		case BOND: {
			...
		}
 */
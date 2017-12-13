/* Jennifer Peshansky
 * 111049923
 * CSE 260
 * Dec 4, 2017 - Smiles (Final Project 2017) chem
 */
package chem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
	private String type;
	private String text;
	public Token(String type, String text) {
		this.type = type;
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public String getText() {
		/*
		if (type.equals("radical")) {
			Pattern atom = Pattern.compile("A[cglmrstu]|B[aehikr]?"
					+ "|C[adeflmorsu]?|D[bsy]|E[rsu]|F[emr]?|G[ade]|H[efgos]?"
					+ "|I[nr]?|Kr?|L[airu]|M[dgnot]|N[abdeiop]?|Os?|P[abdmortu]?"
					+ "|R[abefghnu]|S[bcegimnr]?|T[abcehilm]|Uu[bhopqstx]|U|V|W|Xe|Yb?|Z[nr]");
			Matcher matcher = atom.matcher(text);
			//System.out.println(matcher.group().toString());
			return matcher.group().toString(); //returns just the atom to label the radical with
		}
		else*/ if (type.equals("bond")) {
			if (text.equals("-")) return "1";
			else if (text.equals("=")) return "2";
			else if (text.equals("#")) return "3";
			else if (text.equals("$")) return "4"; //returns bond for edgelabel
		}
		else if (type.equals("ring closure")) {
			Pattern closure = Pattern.compile("([0-9]|[%][0-9][0-9])");
			Matcher matcher = closure.matcher(text);
			System.out.println(matcher.group().toString());
			return matcher.group().toString(); //returns just number of actual ring closure
		}
	    return text;
	}

	public String toString() {
		return type + " ==> " + text;
	}
}

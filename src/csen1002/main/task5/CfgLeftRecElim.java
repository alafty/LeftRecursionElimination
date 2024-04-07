package csen1002.main.task5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Write your info here
 *
 * @name Mohamed Moustafa
 * @id 49-3603
 * @labNumber 21
 */

public class CfgLeftRecElim {

    ArrayList<String> variables = new ArrayList<>();
	ArrayList<String> variablesDash = new ArrayList<>();
    ArrayList<String> terminals = new ArrayList<>();

    HashMap<String, ArrayList<String>> rules = new HashMap<>();
    HashMap<String, ArrayList<String>> newRules = new HashMap<>();

    /**
     * Constructs a Context Free Grammar
     *
     * @param cfg A formatted string representation of the CFG. The string
     *            representation follows the one in the task description
     */
    public CfgLeftRecElim(String cfg) {
        String[] cfgArray = cfg.split("#");

        variables.addAll(List.of(cfgArray[0].split(";")));

        terminals.addAll(List.of(cfgArray[1].split(";")));

        for (int i = 0; i < variables.size(); i++) {
            ArrayList<String> rightHand = new ArrayList<>();
            String[] temp = cfgArray[2].split(";")[i].split("/")[1].split(",");
            rightHand.addAll(List.of(temp));
            rules.put(variables.get(i), rightHand);
        }


        //eliminateLeftRecursion();
        System.out.println(variables);
        System.out.println(terminals);
        System.out.println(rules);
    }

    /**
     * @return Returns a formatted string representation of the CFG. The string
     *         representation follows the one in the task description
     */
    @Override
    public String toString() {
        String returnString = "";

        for (String var : variables) {
            returnString += var + ";";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        returnString += "#";

        for (String terminal : terminals) {
            returnString += terminal + ";";
        }
        returnString = returnString.substring(0, returnString.length() - 1);
        returnString += "#";

        for (String key : variables) {
            returnString += key + "/";
            for (String rule : rules.get(key)) {
                returnString += rule + ",";
            }
            returnString = returnString.substring(0, returnString.length() - 1);
            returnString += ";";
        }

        return returnString.substring(0, returnString.length() - 1);
    }

    /**
     * Eliminates Left Recursion from the grammar
     */
    public void eliminateLeftRecursion() {
		for(String var : variables) {
			RecursivelyEliminateLeftRecursion(var);
		}
		variables.addAll(variablesDash);
    }

    public void RecursivelyEliminateLeftRecursion(String var) {
        ArrayList<String> replacementArray = new ArrayList<>();
        //loop over the rules of the input variable
        for (String rule : rules.get(var)) {
            //check if the first char is a variable
            if (variables.contains(rule.charAt(0) + "")) {
                //check if this variable occurs before my current var
                if (variables.indexOf(rule.charAt(0) + "") < variables.indexOf(var)) {
                    //add all of this variable's rules as suffixes to my current rule
                    for (String previousVarRule : rules.get(rule.charAt(0) + "")) {
                        String temp = previousVarRule + rule.substring(1);
                        replacementArray.add(temp);
                    }
                }
				// first char is a variable that is yet to come then add it normally
				else {
					replacementArray.add(rule);
				}
			} // first char is a literal then add it normally
			else {
				replacementArray.add(rule);
			}

        }
        rules.get(var).clear();
        rules.get(var).addAll(replacementArray);
		//TODO:recursive call needed here
		ArrayList<String> loopOverRules = new ArrayList<>(rules.get(var));
		for(String rule : loopOverRules) {
			if (variables.contains(rule.charAt(0) + "")) {
				if (variables.indexOf(rule.charAt(0) + "") < variables.indexOf(var)) {
					RecursivelyEliminateLeftRecursion(var);
				}
			}
		}
		//////////////////////Immediate Left Recursion//////////////////////////
        ArrayList<String> tempRules = new ArrayList<>();
        for (String rule : rules.get(var)) {
            if (rule.charAt(0) == var.charAt(0)) {
                tempRules.add(rule);
            }
        }
        if (!tempRules.isEmpty()) {
            //remove all rules that start with the same var
            rules.get(var).removeAll(tempRules);
            //create a new var
            String varDash = var + "'";
            variablesDash.add(varDash);
            //amend the existing rules with var dash at the end
            ArrayList<String> amendedRules = new ArrayList<>();
            for (String rule : rules.get(var)) {
                rule += varDash;
                amendedRules.add(rule);
            }
            rules.get(var).clear();
            rules.get(var).addAll(amendedRules);
            //remove the first var and add the var dash at the end
            amendedRules.clear();
            for (int l = 0; l < tempRules.size(); l++) {
                String temp = tempRules.get(l).substring(1) + varDash;
                amendedRules.add(temp);
            }
            amendedRules.add("e");
            rules.put(varDash, amendedRules);
        }
    }

//	public void eliminateLeftRecursion() {
//		for(int i = 0; i < variables.size(); i++){
//			for(int j = 0; j < i; j ++){
//				ArrayList<String> toBeReplaced = new ArrayList<>();
//				for(String rule : rules.get(variables.get(i))){
//					if(rule.charAt(0) != variables.get(j).charAt(0)){
//						toBeReplaced.add(rule);
//					} else {
//						String temp = rule.substring(1);
//						for(String originalRule : rules.get(variables.get(j))){
//							String modifiedRule = originalRule + temp;
//							ArrayList<String> previousReplacements = CheckForPreviousRules(modifiedRule, i);
//							if(previousReplacements.isEmpty()) {
//								toBeReplaced.add(modifiedRule);
//							} else {
//								toBeReplaced.addAll(previousReplacements);
//							}
//						}
//					}
//				}
//				rules.get(variables.get(i)).clear();
//				rules.get(variables.get(i)).addAll(toBeReplaced);
//			}
//
//			////////////////////////Immediate Left Recursion////////////////////////////////
//			ArrayList<String> tempRules = new ArrayList<>();
//			for(String rule : rules.get(variables.get(i))){
//				if(rule.charAt(0) == variables.get(i).charAt(0)){
//					tempRules.add(rule);
//				}
//			}
//			if(!tempRules.isEmpty()) {
//				//remove all rules that start with the same var
//				rules.get(variables.get(i)).removeAll(tempRules);
//				//create a new var
//				String varDash = variables.get(i) + "'";
//				variables.add(varDash);
//				//amend the existing rules with var dash at the end
//				ArrayList<String> amendedRules = new ArrayList<>();
//				for(String rule : rules.get(variables.get(i))){
//					rule += varDash	;
//					amendedRules.add(rule);
//				}
//				rules.get(variables.get(i)).clear();
//				rules.get(variables.get(i)).addAll(amendedRules);
//				//remove the first var and add the var dash at the end
//				amendedRules.clear();
//				for(int l = 0; l < tempRules.size(); l++){
//					String temp = tempRules.get(l).substring(1);
//					temp += varDash;
//					amendedRules.add(temp);
//				}
//				amendedRules.add("e");
//				rules.put(varDash, amendedRules);
//			}
//		}
//	}

public ArrayList<String> CheckForPreviousRules(String rule, int x) {
    ArrayList<String> returnArray = new ArrayList<>();
    for (int i = 0; i < x; i++) {
        if (variables.get(i).charAt(0) == rule.charAt(0)) {
            return rules.get(variables.get(i));
        }
    }
    return returnArray;
}

public static void main(String[] args) {
    String test0 = "S;T;L#a;b;c;d;i#S/ScTi,La,Ti,b;T/aSb,LabS,i;L/SdL,Si";
	String test1 = "S;I;D;W;K#c;g;n;t#S/nKS,gDKS,nIg,ScWSW;I/WSWK,DcWn,IcSgS,InS;D/IIcWS,gKWS,g;W/DK,WWnI,t,cS,DtIt,WKtD;K/gStSI,WIg,Sn";
    CfgLeftRecElim test = new CfgLeftRecElim(test1);
    System.out.println(test);
    System.out.println(test.toString().equals("S;I;D;W;K;S';I';D';W'#c;g;n;t#S/nKSS',gDKSS',nIgS';I/WSWKI',DcWnI';D/WSWKI'IcWSD',gKWSD',gD';W/gKWSD'KW',gD'KW',tW',cSW',gKWSD'tItW',gD'tItW';K/gStSI,gKWSD'KW'Ig,gD'KW'Ig,tW'Ig,cSW'Ig,gKWSD'tItW'Ig,gD'tItW'Ig,nKSS'n,gDKSS'n,nIgS'n;S'/cWSWS',e;I'/cSgSI',nSI',e;D'/cWnI'IcWSD',e;W'/SWKI'IcWSD'KW',WnIW',SWKI'IcWSD'tItW',KtDW',e"));

}

}

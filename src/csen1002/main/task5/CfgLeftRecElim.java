package csen1002.main.task5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Write your info here
 * 
 * @name Jane Smith
 * @id 49-0234
 * @labNumber 06
 */

public class CfgLeftRecElim {

	ArrayList<String> variables = new ArrayList<>();
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

		for(int i= 0; i < variables.size(); i++){
			ArrayList<String> rightHand = new ArrayList<>();
			String[] temp = cfgArray[2].split(";")[i].split("/")[1].split(",");
			rightHand.addAll(List.of(temp));
			rules.put(variables.get(i), rightHand);
		}


		eliminateLeftRecursion();
		//	RemoveImmediateLeftRecursion();
		System.out.println(variables);
		System.out.println(terminals);
		System.out.println(rules);
	}

	public void RemoveImmediateLeftRecursion() {
		ArrayList<String> newVars = new ArrayList<>();
		for(String var: rules.keySet()) {
			ArrayList<String> tempRules = new ArrayList<>();
			for(String rule : rules.get(var)){
				if(rule.charAt(0) == var.charAt(0)){
					tempRules.add(rule);
				}
			}
			if(!tempRules.isEmpty()) {
				//remove all rules that start with the same var
				rules.get(var).removeAll(tempRules);
				//create a new var
				String varDash = var + "'";
				newVars.add(varDash);
				//ammend the existing rules with var dash at the end
				ArrayList<String> amendedRules = new ArrayList<>();
				for(String rule : rules.get(var)){
					rule += varDash	;
					amendedRules.add(rule);
				}
				rules.get(var).clear();
				rules.get(var).addAll(amendedRules);
				//remove the first var and add the var dash at the end
				for(int i = 0; i <tempRules.size(); i++){
					String temp = tempRules.get(i).substring(1);
					temp += varDash;
					tempRules.remove(i);
					tempRules.add(temp);
				}
				tempRules.add("e");
				newRules.put(varDash, tempRules);
			}
		}
		rules.putAll(newRules);
		variables.addAll(newVars);
	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
		for(int i = 0; i < variables.size(); i++){
			for(int j = 0; j < i; j ++){
				ArrayList<String> toBeReplaced = new ArrayList<>();
				for(String rule : rules.get(variables.get(i))){
					if(rule.charAt(0) == variables.get(j).charAt(0)){
						toBeReplaced.add(rule);
					}
				}
				rules.get(variables.get(i)).removeAll(toBeReplaced);
				for(String rule : toBeReplaced){
					rule = rule.substring(1);
					for(String originalRule : rules.get(variables.get(j))){
						String modifiedRule = originalRule + rule;
						rules.get(variables.get(i)).add(modifiedRule);
					}
				}
			}

			////////////////////////Immediate Left Recursion////////////////////////////////
			ArrayList<String> tempRules = new ArrayList<>();
			for(String rule : rules.get(variables.get(i))){
				if(rule.charAt(0) == variables.get(i).charAt(0)){
					tempRules.add(rule);
				}
			}
			if(!tempRules.isEmpty()) {
				//remove all rules that start with the same var
				rules.get(variables.get(i)).removeAll(tempRules);
				//create a new var
				String varDash = variables.get(i) + "'";
				//amend the existing rules with var dash at the end
				ArrayList<String> amendedRules = new ArrayList<>();
				for(String rule : rules.get(variables.get(i))){
					rule += varDash	;
					amendedRules.add(rule);
				}
				rules.get(variables.get(i)).clear();
				rules.get(variables.get(i)).addAll(amendedRules);
				//remove the first var and add the var dash at the end
				amendedRules.clear();
				for(int l = 0; l < tempRules.size(); l++){
					String temp = tempRules.get(l).substring(1);
					temp += varDash;
					amendedRules.add(temp);
				}
				amendedRules.add("e");
				rules.put(varDash, amendedRules);
			}
		}
	}

	public static void main (String[] args){
		String test1 = "S;T;L#a;b;c;d;i#S/ScTi,La,Ti,b;T/aSb,LabS,i;L/SdL,Si";
		CfgLeftRecElim test = new CfgLeftRecElim(test1);

	}

}

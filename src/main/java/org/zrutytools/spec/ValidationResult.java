package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * accumulated result of a validate() call, can contain any number of Problems
 */
public class ValidationResult {

	List<Problem> problems = new ArrayList<>();

	public boolean isOK() {
	  return problems.isEmpty();
	}

	public boolean hasProblems(){
		return ! problems.isEmpty();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Problem p : problems){
			sb.append(p.getError());
			sb.append("\t");
			sb.append(p.getError());
			sb.append("\n");
		}
		return sb.toString();
	}

}

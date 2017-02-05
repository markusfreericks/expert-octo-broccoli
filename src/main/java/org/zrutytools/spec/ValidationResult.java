package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	List<Problem> problems = new ArrayList<>();
	
	public void add(Problem p){
		this.problems.add(p);
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

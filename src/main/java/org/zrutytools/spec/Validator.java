package org.zrutytools.spec;

import java.util.List;
import java.util.Map;

public class Validator {

	ParsedSpec spec;
	
	public Validator(ParsedSpec parsedSpec) {
		this.spec = parsedSpec;
	}

	
	public ValidationResult validate(Object ob) {
		ValidationResult r = new ValidationResult();
		
		Problem problem = spec.getRootDocInfo().getProblem(ob);
		if(problem != null){
			r.add(problem);
		}
		
		return r;
	}

}

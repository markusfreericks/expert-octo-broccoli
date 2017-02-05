package org.zrutytools.spec;

import java.util.Objects;

public class RootDocumentInfo {

	private String coreType;
	private String name;

	public RootDocumentInfo(String type, String name) {
		this.coreType = type;
		this.name = name;
	}
	
	public String getCoreType() {
		return coreType;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "the document is a " + coreType + " of type " + name;
	}

	/**
	 * 
	 * @param ob
	 * @return the error message
	 */
	public Problem getProblem(Object ob) {
		String ct = ParsedSpec.getBaseType(ob);
		if(! Objects.equals(coreType, ct)){
			return new Problem("", "expected root object of type " + coreType + " but got " + ct);
		}
		return null;
	}

}

package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 
 * specify the form of a json object
 * 
 * - the root has a type
 * - lists contain elements of a type
 * - a type is either "object", "list", "number", "string", or "boolean"
 * 
 * there is a registry of "types"; an object can belong to multiple "types"
 * 
 */
public class ParsedSpec {

	// build-in types
	public static String TYPE_STRING = "string";
	public static String TYPE_OBJECT = "object";
	public static String TYPE_LIST = "list";
	public static String TYPE_NUMBER = "number";
	public static String TYPE_BOOLEAN = "boolean";
	public static String TYPE_NULL = "null";

	private List<String> buildinTypes = Arrays.asList(TYPE_STRING, TYPE_OBJECT, TYPE_NUMBER, TYPE_BOOLEAN, TYPE_NULL);
	private List<String> declaredTypes = new ArrayList<String>();
	private List<TypeDetector> detectors = new ArrayList<TypeDetector>();
	
	private RootDocumentInfo rootDocInfo;
	
	public List<String> getTypes(){
		List<String> rs = new ArrayList<>();
		rs.addAll(buildinTypes);
		rs.addAll(declaredTypes);
		return rs;
	}

	public static String getBaseType(Object ob){
		if(ob == null){
			return ParsedSpec.TYPE_NULL;
		}
		if(ob instanceof Map){
			return ParsedSpec.TYPE_OBJECT;
		}
		if(ob instanceof List){
			return ParsedSpec.TYPE_LIST;
		}
		if(ob instanceof Number){
			return ParsedSpec.TYPE_NUMBER;
		}
		throw new IllegalArgumentException("no base type for " + ob);
	}
	
	/**
	 * use the type predicates to detect a type
	 * @param object
	 * @return
	 */
	public List<String> getTypes(Map<Object, Object> object){
		List<String> rs = new ArrayList<>();
		rs.add(TYPE_OBJECT);
		if(object == null){
			rs.add(TYPE_NULL);
		} else {
			
		}
		return rs;
	}

	public Validator createValidator() {
		return new Validator(this);
	}

	public void add(Object x) {
		if(x instanceof RootDocumentInfo){
			this.rootDocInfo = (RootDocumentInfo) x;
		}
	}

	public RootDocumentInfo getRootDocInfo() {
		return rootDocInfo;
	}
	
}

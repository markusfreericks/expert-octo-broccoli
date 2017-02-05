package org.zrutytools.spec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the parser reads a list of rules. each rule is matched against a number of syntactic patterns, each of which constructs a list of Classifications and/or Rules.
 * 
 * a Classification defines a name for a json object
 * a Rule defines some property a json object must have
 * 
 * @author mfx
 */
public class Parser {

	ParsedSpec parse(Reader src) throws IOException{
		BufferedReader lines = new BufferedReader(src);
		ParsedSpec r = new ParsedSpec();

		String line;
		while((line = lines.readLine()) != null){

			Object ob = parseLine(line);
			if(ob instanceof List){
				for(Object x : (List<Object>)ob){
					r.add(x);
				}
			}
		}
		
		return r;
	}

	private Object parseLine(String line) {

		Object r = null;
		Method match = null;
		
		for(Method m : getClass().getDeclaredMethods()){
			Syntax syntax = m.getAnnotation(Syntax.class);
			if(syntax != null){
				System.out.println("testing syntax " + syntax);
				if(matches(syntax.value(), line)){
					if(r != null){
						throw new IllegalArgumentException("line `" + line + "` has non-unique matches " + match.getName() + " vs " + m.getName());
					}
					
					r = createResult(syntax.value(), line, m);
					match = m;
				}
			} else {
				System.out.println("no syntax for " + m);
			}
		}
		if(match == null){
			throw new IllegalArgumentException("no match found for line `" + line + "`");
		}
		
		return r;
	}

	private Object createResult(String value, String line, Method m) {
		Matcher matcher = Pattern.compile(value).matcher(line);
		if(! matcher.matches()){
			// matches() muss einmal aufgerufen werden
			throw new IllegalArgumentException("unable to parse argument line `" + line + "` against syntax `" + value + "`");
		}
		int groupCount = matcher.groupCount();
		String[] groups = new String[groupCount];
		for(int g=0;g<groups.length;g++){
			try {
				// gruppen werden ab 1 gezählt; 0 ist der gesamt-input
				groups[g] = matcher.group(g+1);
			} catch (Exception ex) {
				throw new IllegalArgumentException("unable to parse argument " + g + " in line `" + line + "` against syntax `" + value + "`", ex);
			}
				
		}
		try {
			return m.invoke(this, groups);
		} catch (Exception ex) {
			throw new IllegalArgumentException("unable to parse line `" + line + "` against syntax `" + value + "`", ex);
		}
	}

	private boolean matches(String syntax, String line) {
		return line.matches(syntax);
	}

	/**
	 * ignore comments
	 * @return
	 */
	@Syntax("#.*")
	List<Object> parseComment(){
		return Collections.emptyList();
	}

	/**
	 * ignore empty lines
	 * @return
	 */
	@Syntax(" *")
	List<Object> parseEmpty(){
		return Collections.emptyList();
	}
	
	/**
	 * 
	 * @param name
	 * @return a type definition and a name definition
	 */
	@Syntax("the document contains an object called (.*)")
	List<Object> announceDocumentName(String name){
		List<Object> rs = new ArrayList<Object>();
		rs.add(new RootDocumentInfo(ParsedSpec.TYPE_OBJECT, name));
		return rs ;
	}
		
	@Syntax("the (.+) MUST contain a field (.+) that contains a list of (.+) elements.")
	List<Object> announceFieldElement(String object, String fieldName, String subObjectName){
		List<Object> rs = new ArrayList<Object>();
		// TODO:
		return rs ;
	}
	
	@Syntax("a (.+) MUST contain a field (.+) that contains a unique (.+) string.")
	List<Object> announceUniqueElement(String object, String fieldName, String idName){
		List<Object> rs = new ArrayList<Object>();
		// TODO:
		return rs ;
	}

	
}

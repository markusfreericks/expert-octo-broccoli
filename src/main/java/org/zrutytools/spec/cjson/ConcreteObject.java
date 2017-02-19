package org.zrutytools.spec.cjson;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConcreteObject extends ConcreteNode  {

	Map<ConcreteValue<?>, ConcreteNode> elements;
	Map<String, ConcreteNode> withUnwrappedKeys;

	public ConcreteObject(int startLine, int startChar, int endLine, int endChar, Map<ConcreteValue<?>, ConcreteNode> elements) {
		super(startLine, startChar, endLine, endChar);
		this.elements = elements;
		this.withUnwrappedKeys = new HashMap<>();
		for(Entry<ConcreteValue<?>, ConcreteNode> e : elements.entrySet()){
			withUnwrappedKeys.put((String) e.getKey().getValue(), e.getValue());
		}
	}
	
	public Map<ConcreteValue<?>, ConcreteNode> getElements() {
		return elements;
	}
	
	public ConcreteNode get(String key){
		return withUnwrappedKeys.get(key);
	}
	
}

package org.zrutytools.spec.cjson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ConcreteObject extends SourceLocation  {

	Map<ConcreteValue<?>, SourceLocation> map;
	List<String> keys;
	Map<String, SourceLocation> withUnwrappedKeys;

	public ConcreteObject(int startLine, int startChar, int endLine, int endChar, List<String> keys, Map<ConcreteValue<?>, SourceLocation> map) {
		super(startLine, startChar, endLine, endChar);
		this.map = map;
		this.keys = keys;
		this.withUnwrappedKeys = new HashMap<>();
		for(Entry<ConcreteValue<?>, SourceLocation> e : map.entrySet()){
			withUnwrappedKeys.put((String) e.getKey().getValue(), e.getValue());
		}
	}

	public Map<ConcreteValue<?>, SourceLocation> getElements() {
		return map;
	}

	/**
	 * keys, in their original order
	 * @return
	 */
	public List<String> getKeys() {
      return keys;
    }

    /**
     * lookup value by its key
     * @return
     */
	public SourceLocation get(String key){
		return withUnwrappedKeys.get(key);
	}

}

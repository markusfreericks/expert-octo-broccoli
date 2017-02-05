package org.zrutytools.spec;

import java.util.Map;

public interface TypeDetector {

	/**
	 * @param object
	 * @return the id of a detected object, or null (no match)
	 */
	String detectType(Map<Object, Object> object);
	
}

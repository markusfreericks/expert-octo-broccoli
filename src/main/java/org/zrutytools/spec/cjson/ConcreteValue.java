package org.zrutytools.spec.cjson;

public class ConcreteValue<T> extends ConcreteNode {

	T value;
	
	public ConcreteValue(int startLine, int startChar, int endLine, int endChar, T value) {
		super(startLine, startChar, endLine, endChar);
		this.value = value;
	}

	public T getValue() {
		return value;
	}
	
}

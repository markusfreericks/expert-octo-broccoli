package org.zrutytools.spec.cjson;

public abstract class ConcreteNode {
	int startLine;
	int endLine;
	int startChar;
	int endChar;
	public int getStartLine() {
		return startLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public int getStartChar() {
		return startChar;
	}
	public int getEndChar() {
		return endChar;
	}
	public ConcreteNode(int startLine, int startChar, int endLine, int endChar) {
		this.startLine = startLine;
		this.startChar = startChar;
		this.endLine = endLine;
		this.endChar = endChar;
	}
	
	
	
}

package org.zrutytools.spec.cjson;

public class SourceLocation {
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
	public SourceLocation(int startLine, int startChar, int endLine, int endChar) {
		this.startLine = startLine;
		this.startChar = startChar;
		this.endLine = endLine;
		this.endChar = endChar;
	}

	@Override
	public String toString() {
	  if(startLine == endLine) {
	    if(startLine == -1) {
	      return "";
	    }
	    return startLine + ":" + startChar + ".." + endChar;
	  }
	  return startLine + ":" + startChar + ".." + endLine + ":" + endChar;
	}

	// needed when there is no known location
	public static final SourceLocation UNKNOWN = new SourceLocation(-1, -1, -1, -1);

}

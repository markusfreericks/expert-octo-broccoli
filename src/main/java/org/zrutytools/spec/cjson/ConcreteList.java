package org.zrutytools.spec.cjson;

import java.util.List;

public class ConcreteList extends SourceLocation  {

	List<SourceLocation> nodes;

	public List<SourceLocation> getNodes() {
		return nodes;
	}

	public ConcreteList(int startLine, int startChar, int endLine, int endChar, List<SourceLocation> nodes) {
		super(startLine, startChar, endLine, endChar);
		this.nodes = nodes;
	}

	
}

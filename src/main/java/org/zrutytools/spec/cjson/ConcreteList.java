package org.zrutytools.spec.cjson;

import java.util.List;

public class ConcreteList extends ConcreteNode  {

	List<ConcreteNode> nodes;

	public List<ConcreteNode> getNodes() {
		return nodes;
	}

	public ConcreteList(int startLine, int startChar, int endLine, int endChar, List<ConcreteNode> nodes) {
		super(startLine, startChar, endLine, endChar);
		this.nodes = nodes;
	}

	
}

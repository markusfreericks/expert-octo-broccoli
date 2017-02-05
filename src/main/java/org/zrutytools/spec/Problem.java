package org.zrutytools.spec;

public class Problem {

	String path;
	String error;
	String rule;

	public Problem(String path, String error) {
		this.path = path;
		this.error = error;
	}

	/**
	 * @return the path to the offending element
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return what kind of problem/violation exists
	 */
	public String getError() {
		return error;
	}

}

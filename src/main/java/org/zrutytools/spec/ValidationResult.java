package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * accumulated result of a validate() call, can contain any number of Problems
 */
public class ValidationResult {

  private List<Problem> problems = new ArrayList<>();
  private ParsedSpec spec;

  public ValidationResult(ParsedSpec spec, List<Problem> problems) {
    this.spec = spec;
    this.problems = problems;
  }

  public boolean isOK() {
    return problems.isEmpty();
  }

  public boolean hasProblems() {
    return !problems.isEmpty();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Problem p : problems) {
      sb.append(p);
      sb.append("\n");
    }
    return sb.toString();
  }


}

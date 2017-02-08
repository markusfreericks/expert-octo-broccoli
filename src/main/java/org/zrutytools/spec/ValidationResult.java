package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * accumulated result of a validate() call, can contain any number of Problems
 */
public class ValidationResult {

  private List<Problem> problems = new ArrayList<>();

  public ValidationResult(List<Problem> problems) {
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
      sb.append(p.getError());
      sb.append(" caused by:" );
      Object ob = p.getOb();
      if(ob == null) {
        sb.append(ob);
      } else {
        sb.append(ob.getClass().getSimpleName());
        sb.append(" ");
        sb.append(ob);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}

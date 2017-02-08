package org.zrutytools.spec;

public class Problem {

  public enum Kind {
    UNEXPECTED_TYPE("object has unexpected type"), MISSING_PROPERTY("property missing"), EMPTY_LIST("list must not be empty"), NULL_PROPERTY("property must not be null");

    private String message;
    Kind(String msg){
      this.message = msg;
    }
    public String getMessage() {
      return message;
    }
  }

  Kind kind;
  Object ob;
  String error;

  public Problem(Kind kind, Object ob, String error) {
    this.kind = kind;
    this.ob = ob;
    this.error = error;
  }

  /**
   * @return the offending object
   */
  public Object getOb() {
    return ob;
  }

  /**
   * @return what kind of problem/violation exists
   */
  public String getError() {
    return error;
  }

}

package org.zrutytools.spec;

public class Problem {

  public enum Kind {
    UNEXPECTED_OBJECT_TYPE("object has unexpected type"),
    UNEXPECTED_PROPERTY_TYPE("property has unexpected type"),
    MISSING_PROPERTY("property missing"),
    EMPTY_LIST("list must not be empty"),
    EMPTY_PROPERTY("property must not be empty list"),
    PREDICATE_FAIL("predicate not fulfilled"),
    NULL_OBJECT("object must not be null"),
    NULL_PROPERTY("property must not be null");

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

  public Kind getKind() {
    return kind;
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(kind.name());
    sb.append(" (");
    sb.append(kind.getMessage());
    sb.append(") ");
    sb.append("error");
    sb.append(" ob=");
    sb.append(ob);

    return sb.toString();
  }

}

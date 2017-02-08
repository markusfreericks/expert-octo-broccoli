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

  private Kind kind;
  private String message;
  private ObjectContext ctx;

  public Problem(Kind kind, ObjectContext ctx, String message) {
    this.ctx = ctx;
    this.kind = kind;
    this.message = message;
  }

  public Kind getKind() {
    return kind;
  }

  public ObjectContext getContext() {
    return ctx;
  }

  /**
   * @return what kind of problem/violation exists
   */
  public String getError() {
    return message;
  }

  static final int MAX_OBJ_TOSTRING = 40;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
//    sb.append(kind.name());
//    sb.append(" (");
//    sb.append(kind.getMessage());
//    sb.append(") ");
    sb.append(message);

    sb.append(" ");
    sb.append(ctx);
    sb.append(" ");

    Object ob = ctx.getObject();

    if(ob == null) {
      sb.append(" found null");
    } else {
      sb.append(" found ");
      String obs = ob.toString();
      sb.append(ob.getClass().getSimpleName());
      sb.append(" ");
      if(obs.length() > MAX_OBJ_TOSTRING) {
        sb.append(obs.substring(0, MAX_OBJ_TOSTRING) + "...");
      } else {
        sb.append(obs);
      }
    }

    return sb.toString();
  }

}

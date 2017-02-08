package org.zrutytools.spec;

import org.zrutytools.spec.types.Type;

public class ObjectContext {

  // path relative to parent
  private String path;
  // parent context
  private ObjectContext parent;
  // object to test
  private Object object;
  // expected type of object
  private Type expectedType;

  public ObjectContext(String path, Type expectedType, Object object, ObjectContext parent) {
    this.object = object;
    this.path = path;
    this.parent = parent;
    this.expectedType = expectedType;
  }

  public Object getObject() {
    return object;
  }

  public Type getExpectedType() {
    return expectedType;
  }

  public ObjectContext element(int n, Type expectedType, Object object) {
    return new ObjectContext("[" + n + "]", expectedType, object, this);
  }

  public ObjectContext element(String prop, Type expectedType, Object object) {
    return new ObjectContext("." + prop, expectedType, object, this);
  }

  @Override
  public String toString() {
    return getAbsolutePath();
  }

  public String getPath() {
    return path;
  }

  /**
   * @return cumulative path
   */
  public String getAbsolutePath() {
    if(parent == null) {
      return path;
    } else {
      return parent.getAbsolutePath() + path;
    }
  }

  /**
   * lookup the innermost parent that has the expected type
   * @param type
   * @return parent, or null
   */
  public ObjectContext lookupParent(Type type) {
    if(parent == null) {
      return null;
    }
    if(parent.getExpectedType() == type) {
      return parent;
    }
    return parent.lookupParent(type);
  }
}

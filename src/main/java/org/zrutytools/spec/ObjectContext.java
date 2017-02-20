package org.zrutytools.spec;

import java.util.List;
import java.util.Map;

import org.zrutytools.spec.cjson.ConcreteList;
import org.zrutytools.spec.cjson.ConcreteObject;
import org.zrutytools.spec.cjson.ConcreteValue;
import org.zrutytools.spec.cjson.SourceLocation;
import org.zrutytools.spec.types.Type;

/**
 * holds an object and its context
 * @author mfx
 *
 */
public class ObjectContext {

  // path relative to parent
  private String path;
  // parent context
  private ObjectContext parent;
  // object to test
  private Object object;
  // expected type of object
  private Type expectedType;

  private SourceLocation location;

  public ObjectContext(String path, Type expectedType, Object object, ObjectContext parent) {
    this.object = unwrap(object);
    this.location = locate(object);
    this.path = path;
    this.parent = parent;
    this.expectedType = expectedType;
  }

  private SourceLocation locate(Object x) {
    if(x instanceof SourceLocation) {
      return (SourceLocation) x;
    }
    return SourceLocation.UNKNOWN;
  }

  public SourceLocation getLocation() {
    return location;
  }

  private Object unwrap(Object x) {
    if(x instanceof ConcreteValue){
      return ((ConcreteValue) x).getValue();
    }
    if(x instanceof ConcreteList){
      return ((ConcreteList) x).getNodes();
    }
    if(x instanceof ConcreteObject){
      return ((ConcreteObject) x).getWithUnwrappedKeys();
    }
    if(x instanceof String || x instanceof Number || x instanceof Boolean || x instanceof List || x instanceof Map || x == null){
      return x;
    }
    throw new IllegalArgumentException("don't know how to unwrap " + x.getClass().getSimpleName() + " " + x);
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

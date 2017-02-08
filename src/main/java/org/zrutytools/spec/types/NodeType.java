package org.zrutytools.spec.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zrutytools.spec.ObjectContext;
import org.zrutytools.spec.Problem;

public class NodeType implements Type {

  static class ListMap<T,V> extends HashMap<T, List<V>> {

    // access to a map of key->list

    // unchecked: the compiler has problems with
    //   List<V> get(T type)
    //
    @SuppressWarnings("unchecked")
    @Override
    public List<V> get(Object type) {
      List<V> list = super.get(type);
      if (list == null) {
        list = new ArrayList<>();
        super.put((T) type, list);
      }
      return list;
    }

  }

  private String typeId;

  private ListMap<String, Type> mandatoryFields = new ListMap<>();
  private ListMap<String, Type> optionalFields = new ListMap<>();

  private boolean nullAllowed = false;

  public NodeType(String typeId) {
    this.typeId = typeId;
  }

  @Override
  public String getId() {
    return typeId;
  }

  public void setNullAllowed(boolean nullAllowed) {
    this.nullAllowed = nullAllowed;
  }

  public void addMandatoryField(String id, Type... type) {
    List<Type> types = mandatoryFields.get(id);
    for (Type t : type) {
      types.add(t);
    }
  }

  public void addOptionalField(String id, Type... type) {
    List<Type> types = optionalFields.get(id);
    for (Type t : type) {
      types.add(t);
    }
  }

  @Override
  public List<Problem> validate(ObjectContext ctx) {
    List<Problem> rs = new ArrayList<>();
    Object x = ctx.getObject();
    if (x == null) {
      if (!nullAllowed) {
        rs.add(new Problem(Problem.Kind.NULL_PROPERTY, ctx, typeId));
      }
      return rs;
    }

    if (x instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) x;
      rs.addAll(validateMandantories(ctx, map));
      rs.addAll(validateOptionals(ctx, map));
    } else {
      rs.add(new Problem(Problem.Kind.UNEXPECTED_OBJECT_TYPE, ctx, typeId + " expected Map"));
    }
    return rs;
  }

  private Collection<? extends Problem> validateOptionals(ObjectContext ctx, Map<?, ?> map) {
    List<Problem> rs = new ArrayList<>();

    for (Entry<String, List<Type>> e : optionalFields.entrySet()) {
      String fieldName = e.getKey();
      if (map.containsKey(fieldName)) {
        Object object = map.get(fieldName);
        for (Type t : e.getValue()) {
          rs.addAll(t.validate(ctx.element(fieldName, t, object)));
        }
      }
    }

    return rs;
  }

  private List<Problem> validateMandantories(ObjectContext ctx, Map<?, ?> map) {
    List<Problem> rs = new ArrayList<>();

    for (Entry<String, List<Type>> e : mandatoryFields.entrySet()) {
      String fieldName = e.getKey();
      if (map.containsKey(fieldName)) {
        Object object = map.get(fieldName);
        for (Type t : e.getValue()) {
          List<Problem> tProblems = t.validate(ctx.element(fieldName, t, object));
          if(! tProblems.isEmpty()) {
            rs.addAll(tProblems);
            // beim ersten problem eines feldes stoppen; folgefehler bringen nichts
            break;
          }
        }
      } else {
        rs.add(new Problem(Problem.Kind.MISSING_PROPERTY, ctx, "missing mandatory field \"" + fieldName + "\" found " + map.keySet()));
      }
    }

    return rs;
  }

  public Map<String, List<Type>> getMandatoryFields() {
    return mandatoryFields;
  }

  public Map<String, List<Type>> getOptionalFields() {
    return optionalFields;
  }

}

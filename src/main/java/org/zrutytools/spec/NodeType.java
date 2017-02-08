package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NodeType implements Type {

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

  public void addMandatoryField(String id, Type type) {
    mandatoryFields.get(id).add(type);
  }

  public void addOptionalField(String id, Type type) {
    optionalFields.get(id).add(type);
  }

  @Override
  public List<Problem> validate(Object x) {
    List<Problem> rs = new ArrayList<>();

    if (x == null) {
      if (!nullAllowed) {
        rs.add(new Problem(Problem.Kind.NULL_PROPERTY, x, typeId));
      }
      return rs;
    }

    if (x instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) x;
      rs.addAll(validateMandantories(map));
      rs.addAll(validateOptionals(map));
    } else {
      rs.add(new Problem(Problem.Kind.UNEXPECTED_TYPE, x, typeId));
    }
    return rs;
  }

  private Collection<? extends Problem> validateOptionals(Map<?, ?> map) {
    List<Problem> rs = new ArrayList<>();

    for (Entry<String, List<Type>> e : optionalFields.entrySet()) {
      String fieldName = e.getKey();
      if (map.containsValue(fieldName)) {
        Object object = map.get(fieldName);
        for (Type t : e.getValue()) {
          rs.addAll(t.validate(object));
        }
      }
    }

    return rs;
  }

  private List<Problem> validateMandantories(Map<?, ?> map) {
    List<Problem> rs = new ArrayList<>();

    for (Entry<String, List<Type>> e : mandatoryFields.entrySet()) {
      String fieldName = e.getKey();
      if (map.containsValue(fieldName)) {
        Object object = map.get(fieldName);
        for (Type t : e.getValue()) {
          rs.addAll(t.validate(object));
        }
      } else {
        rs.add(new Problem(Problem.Kind.MISSING_PROPERTY, map, "missing mandatory field " + fieldName));
      }
    }

    return rs;
  }

  public ListMap<String, Type> getMandatoryFields() {
    return mandatoryFields;
  }
  public ListMap<String, Type> getOptionalFields() {
    return optionalFields;
  }

}

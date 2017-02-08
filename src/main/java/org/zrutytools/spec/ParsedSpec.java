package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zrutytools.spec.types.NodeType;
import org.zrutytools.spec.types.Type;

/**
 *
 * specification of a json object. can be used to test any tree build of maps, lists, numbers, strings, and booleans.
 *
 * - the root has a type
 * - lists contain elements of a type
 * - a type is either "object", "list", "number", "string", or "boolean"
 *
 * there is a registry of "types"; an object can belong to multiple "types"
 *
 */
public class ParsedSpec {

  private Map<String, NodeType> nodeTypes = new HashMap<>();
  private Type rootType;

  // get, create if missing
  public NodeType getNodeType(String id) {

    NodeType r = nodeTypes.get(id);
    if (r == null) {
      r = new NodeType(id);
      nodeTypes.put(id, r);
    }

    return r;
  }

  public boolean knowsType(String id) {
    return nodeTypes.containsKey(id);
  }

  public void setRootType(Type rootType) {
    this.rootType = rootType;
  }

  public ValidationResult validateDocument(Object ob) {
    List<Problem> rs;
    if (rootType != null) {
      rs = rootType.validate(new ObjectContext("", rootType, ob, null));
    } else {
      rs = Collections.emptyList();
    }

    return new ValidationResult(this, rs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (rootType != null) {
      sb.append("the document is of type " + q(rootType.getId()));
      sb.append("\n");
    }

    List<String> typeNames = new ArrayList<>(nodeTypes.keySet());
    Collections.sort(typeNames);
    for (String typeName : typeNames) {
      NodeType type = nodeTypes.get(typeName);
      sb.append("there is an object type " + q(typeName));
      sb.append("\n");
      for(Entry<String, List<Type>> m : type.getMandatoryFields().entrySet()) {
        sb.append("\t required field " + q(m.getKey()));
        for(Type t : m.getValue()) {
          sb.append(" is a ").append(q(t.getId()));
        }
        sb.append("\n");
      }
      for(Entry<String, List<Type>> m : type.getOptionalFields().entrySet()) {
        sb.append("\t optional field " + q(m.getKey()));
        for(Type t : m.getValue()) {
          sb.append(" is a ").append(q(t.getId()));
        }
        sb.append("\n");
      }
    }

    return sb.toString();
  }

  private String q(String typeName) {
    return '"' + typeName + '"';
  }

}

package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  private List<String> buildinTypes = Arrays.asList(Types.TYPE_STRING, Types.TYPE_OBJECT, Types.TYPE_NUMBER, Types.TYPE_BOOLEAN, Types.TYPE_NULL);
  private List<String> declaredTypes = new ArrayList<>();

  private Map<String, List<Validator>> validators = new HashMap<>();

  private RootDocumentInfo rootDocInfo;

  public List<String> getTypes() {
    List<String> rs = new ArrayList<>();
    rs.addAll(buildinTypes);
    rs.addAll(declaredTypes);
    return rs;
  }

  // access to a map of key->list
  public static <T,V> List<V> getOrCreate(Map<T, List<V>> map, T type) {
    List<V> list = map.get(type);
    if (list == null) {
      list = new ArrayList<>();
      map.put(type, list);
    }
    return list;
  }

  /**
   * use the type predicates to detect a type
   *
   * @param object
   * @return
   */
  public List<String> getTypes(Map<Object, Object> object) {
    List<String> rs = new ArrayList<>();
    rs.add(Types.TYPE_OBJECT);
    if (object == null) {
      rs.add(Types.TYPE_NULL);
    } else {

    }
    return rs;
  }

  public void addValidatorForType(String type, Validator x) {
    List<Validator> list = getOrCreate(validators, type);
    list.add(x);
  }


  public ValidationResult validate(Object ob) {
    ValidationResult r = new ValidationResult();

    List<Validator> vs = getOrCreate(validators, Types.TYPE_DOCUMENT);
    for(Validator v : vs) {
      r.problems.addAll(v.validate(ob));
    }

    return r;
  }

}

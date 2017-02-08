package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMap<T,V> extends HashMap<T, List<V>> {

  // access to a map of key->list

  @Override
  public List<V> get(Object type) {
    List<V> list = super.get(type);
    if (list == null) {
      list = new ArrayList<>();
      super.put((T)type, list);
    }
    return list;
  }



}

package org.zrutytools.spec;

public class Spec1Syntax extends EmptySyntax {
  /**
   *
   * @param name
   * @return a type definition and a name definition
   */
  @Syntax("the document contains an object called (.*)")
  public void announceDocumentName(String name) {
    NodeType nodeType = introduceType(name);
    setRootType(nodeType);
  }

  @Syntax("the (.+) MUST contain a field (.+) that contains a list of (.+) elements")
  public void announceFieldElement(String objectType, String fieldName, String subObjectName) {
    NodeType t = lookupType(objectType);
    NodeType s = introduceType(subObjectName);
    addMandatoryField(fieldName, t, s);
  }

  @Syntax("a (.+) MUST contain a nonempty field (.+)")
  public void announceUniqueElement(String objectType, String fieldName) {
    NodeType t = lookupType(objectType);
    // TODO: not empty
    t.addMandatoryField(unq(fieldName), PrimitiveType.STRING);
  }

  @Syntax("The document is a nonempty list of (\\S+)s")
  public void announceRootNonEmptyListElements(String type) {
    NodeType t = introduceType(type);
    ListType listType = new ListType(t);
    listType.setMinSize(1);
    spec.setRootType(listType);
  }

  @Syntax("The document is a (\\S+)")
  public void announceRootType(String type) {
    NodeType t = introduceType(type);
    setRootType(t);
  }

  @Syntax("The document is a list of (\\S+)s")
  public void announceRootListElements(String type) {
    NodeType t = introduceType(type);
    ListType listType = new ListType(t);
    listType.setMinSize(0);
    spec.setRootType(listType);
  }

  @Syntax("Each (\\S+) has a unique ID")
  public void announceUniqueId(String type) {
    NodeType t = lookupType(type);
    t.addMandatoryField("id", PrimitiveType.STRING);
    // TODO: uniqueness
  }

  @Syntax("Each (\\S+) has a nonempty (\\S+)")
  public void announceNonemptyField(String type, String name) {
    NodeType t = lookupType(type);
    t.addMandatoryField(unq(name), PrimitiveType.STRING);
    // TODO: nonempty
  }

  @Syntax("Each (\\S+) has a nonempty list of (\\S+)s")
  public void announceNonemptyListField(String type, String name) {
    NodeType t = lookupType(type);
    NodeType s = introduceType(name);
    ListType list = new ListType(s);
    list.setMinSize(1);
    String fieldName = unq(name).toLowerCase() + "s";
    t.addMandatoryField(fieldName, list);
  }

  @Syntax("Each (\\S+) has a list of (\\S+)s")
  public void announceListField(String type, String name) {
    NodeType t = lookupType(type);
    NodeType s = introduceType(name);
    ListType list = new ListType(s);
    list.setMinSize(0);
    String fieldName = name.toLowerCase() + "s";
    t.addMandatoryField(fieldName, list);
  }
}

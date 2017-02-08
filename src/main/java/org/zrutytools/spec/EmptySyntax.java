package org.zrutytools.spec;

import org.zrutytools.spec.types.NodeType;
import org.zrutytools.spec.types.Type;

/**
 * subclass this for your own syntax
 */
public abstract class EmptySyntax {

  protected ParsedSpec spec;

  public EmptySyntax() {
  }

  public void setSpec(ParsedSpec spec) {
    this.spec = spec;
  }

  /**
   * ignore comments
   *
   * @return
   */
  @Syntax("#.*")
  public void parseComment() {
    // NOP
  }

  /**
   * ignore empty lines
   *
   * @return
   */
  @Syntax(" *")
  public void parseEmpty() {
    // NOP
  }

  /**
   * throws an error of the type is not already known
   * @param type
   * @return
   */
  protected NodeType lookupType(String type) {
    String unquoted = unq(type);
    if(! spec.knowsType(unquoted)) {
      throw new IllegalStateException("type is not known: " + type);
    }
    return spec.getNodeType(unquoted);
  }

  protected NodeType introduceType(String type) {
    String unquoted = unq(type);
    return spec.getNodeType(unquoted);
  }

  /**
   * unquote
   *
   * @param id
   * @return
   */
  protected String unq(String id) {
    return id.replace("\"", "");
  }

  protected void setRootType(Type nodeType) {
    spec.setRootType(nodeType);
  }

  protected void addMandatoryField(NodeType t, String fieldName, Type... s) {
    t.addMandatoryField(unq(fieldName), s);
  }

  protected void addOptionalField(NodeType t, String fieldName, Type... s) {
    t.addOptionalField(unq(fieldName), s);
  }
}

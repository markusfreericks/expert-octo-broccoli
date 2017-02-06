package org.zrutytools.spec;

import java.util.List;
import java.util.Map;

public class Types {

  // build-in types
  public static String TYPE_STRING = "string";
  public static String TYPE_OBJECT = "object";
  public static String TYPE_LIST = "list";
  public static String TYPE_NUMBER = "number";
  public static String TYPE_BOOLEAN = "boolean";
  public static String TYPE_NULL = "null";

  // synthetic type for the root document
  public static final String TYPE_DOCUMENT = "document";


  public static String getBaseType(Object ob){
  	if(ob == null){
  		return TYPE_NULL;
  	}
  	if(ob instanceof Map){
  		return TYPE_OBJECT;
  	}
  	if(ob instanceof List){
  		return TYPE_LIST;
  	}
  	if(ob instanceof Number){
  		return TYPE_NUMBER;
  	}
  	throw new IllegalArgumentException("no base type for " + ob);
  }

}

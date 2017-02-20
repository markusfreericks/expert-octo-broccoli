package org.zrutytools.spec.cjson;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ConcreteParserTest {

  private static final double DOUBLE_EPS = 0.00000001;

  @Test
  public void testThatParses_aString() throws Exception {

    // ein paar newlines, um das whitespace-handling zu testen
    // alles fängt in zeile 3 an
    String SAMPLE = "   \"hello\"       ";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    // expectPosition(3,1,3,8, nd);
    displayRegion(nd, SAMPLE);
    assertEquals("hello", cs.getValue());
  }

  private void displayRegion(SourceLocation nd, String text) {
    String carets = "";
    StringBuilder digits = new StringBuilder();
    StringBuilder digits10 = new StringBuilder();
    for (int i = 1; i <= 99; i++) {
      if (nd.getStartChar() <= i && i <= nd.getEndChar()) {
        carets += "^";
      } else {
        carets += "_";
      }
      digits.append((char) ('0' + (i % 10)));
      digits10.append((char) ('0' + (i / 10)));
    }
    System.out.println("");
    System.out.println(text + "¶");
    System.out.println(carets);
    System.out.println(digits.toString());
    System.out.println(digits10.toString());
    System.out.println(nd.getStartLine() + "@" + nd.getStartChar() + " .. " + nd.getEndLine() + "@" + nd.getEndChar());
  }

  @Test
  public void testThatParses_aNull() throws Exception {

    String SAMPLE = "null";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertNull(cs.getValue());
  }

  @Test
  public void testThatParses_aNull_spaced() throws Exception {

    String SAMPLE = " null ";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertNull(cs.getValue());
  }

  @Test
  public void testThatParses_true() throws Exception {

    String SAMPLE = "true";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Boolean.TRUE, cs.getValue());
  }

  @Test
  public void testThatParses_true_spaced() throws Exception {

    String SAMPLE = "  true    ";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Boolean.TRUE, cs.getValue());
  }

  @Test
  public void testThatParses_false() throws Exception {

    String SAMPLE = "false";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Boolean.FALSE, cs.getValue());
  }

  @Test
  public void testThatParses_42() throws Exception {

    String SAMPLE = "42";

    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Long.valueOf(42), cs.getValue());
  }

  @Test
  public void testThatParses_42_spaced() throws Exception {

    String SAMPLE = "   8978    ";

    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Long.valueOf(8978), cs.getValue());
  }

  @Test
  public void testThatParses_Pi() throws Exception {

    String SAMPLE = "3.14159265";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();
    assertTrue(nd instanceof ConcreteValue);
    ConcreteValue<?> cs = (ConcreteValue<?>) nd;

    displayRegion(nd, SAMPLE);

    assertEquals(Double.valueOf(3.14159265), (double) cs.getValue(), DOUBLE_EPS);
  }

  @Test
  public void testThatParses_Array() throws Exception {

    String SAMPLE = "[1234, true , false, \"hello\", 3.14159265, null]";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();

    displayRegion(nd, SAMPLE);

    assertTrue(nd instanceof ConcreteList);
    ConcreteList cs = (ConcreteList) nd;
    assertEquals(6, cs.getNodes().size());

    int curr = 0;

    ConcreteValue<?> value;
    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals(Long.valueOf(1234), value.getValue());
    displayRegion(value, SAMPLE);

    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals(Boolean.TRUE, value.getValue());
    displayRegion(value, SAMPLE);

    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals(Boolean.FALSE, value.getValue());
    displayRegion(value, SAMPLE);

    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals("hello", value.getValue());
    displayRegion(value, SAMPLE);

    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals(Double.valueOf(3.14159265), (double) value.getValue(), DOUBLE_EPS);
    displayRegion(value, SAMPLE);

    value = (ConcreteValue<?>) cs.getNodes().get(curr++);
    assertEquals(null, value.getValue());
    displayRegion(value, SAMPLE);
  }

  @Test
  public void testThatParses_EmptyObject() throws Exception {

    String SAMPLE = "      {        }        ";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();

    displayRegion(nd, SAMPLE);

    assertTrue(nd instanceof ConcreteObject);
    ConcreteObject ob = (ConcreteObject) nd;
    assertTrue(ob.getElements().isEmpty());
    assertTrue(ob.getKeys().isEmpty());
  }

  @Test
  public void testThatParses_SimpleObject() throws Exception {

    String SAMPLE = "{  \"a\" : 4321, \"b\" : {  }      }";
    ConcreteParser p = new ConcreteParser(SAMPLE);
    SourceLocation nd = p.parse();

    displayRegion(nd, SAMPLE);

    assertTrue(nd instanceof ConcreteObject);
    ConcreteObject ob = (ConcreteObject) nd;

    assertEquals(2, ob.getElements().size());
    ConcreteValue<?> a = (ConcreteValue<?>) ob.get("a");
    assertNotNull(a);
    assertEquals(Long.valueOf(4321), a.getValue());

    displayRegion(a, SAMPLE);

    ConcreteObject b = (ConcreteObject) ob.get("b");
    assertNotNull(b);
    assertTrue(b.getElements().isEmpty());

    displayRegion(b, SAMPLE);

    assertEquals(Arrays.asList("a", "b"), ob.getKeys());

  }

}

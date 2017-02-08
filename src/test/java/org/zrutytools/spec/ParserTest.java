package org.zrutytools.spec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {

  private static ParsedSpec spec1;

  /**
   * the empty spec contains no constraints, and therefore allows any object
   */
  @Test
  public void testThatEmptySpecAllowsEverything() throws IOException {

    Parser p = new Parser(new Spec1Syntax());
    ParsedSpec emptySpec = p.parse(new StringReader(""));

    assertFalse(emptySpec.validateDocument(null).hasProblems());
    assertFalse(emptySpec.validateDocument(1).hasProblems());
    assertFalse(emptySpec.validateDocument("test").hasProblems());
    assertFalse(emptySpec.validateDocument(true).hasProblems());
    assertFalse(emptySpec.validateDocument(new ArrayList()).hasProblems());
    assertFalse(emptySpec.validateDocument(new HashMap()).hasProblems());

  }

  @BeforeClass
  public static void setup() throws UnsupportedEncodingException, IOException {

    InputStream is = ParsedSpec.class.getResourceAsStream("spec-1.txt");
    assertNotNull(is);

    Parser p = new Parser(new Spec1Syntax());
    spec1 = p.parse(new InputStreamReader(is, "utf-8"));
    assertNotNull(spec1);

    System.out.println("*** parsed spec: ***\n" + spec1);

  }

  @Test
  public void testThatTheRootTypeIsTested() {

    ValidationResult vr;

    vr = spec1.validateDocument(null);
    expectProblem("falscher typ: null", vr);

    vr = spec1.validateDocument(42);
    expectProblem("falscher typ: number", vr);

    vr = spec1.validateDocument(new ArrayList<>());
    expectProblem("falscher typ: List", vr);

  }

  private void expectProblem(String what, ValidationResult vr) {
    System.out.println("expect problem: " + what);
    System.out.println("found: " + vr);
    if(! vr.hasProblems()) {
      fail("expected problem: " + what);
    }
  }

  @Test
  public void testContentOfRootObject() {

    Map<String, Object> doc = new HashMap<>();
    List<Object> nodes = new ArrayList<>();

    ValidationResult vr;

    // richtiger typ: object. fehlendes feld: "list"
    vr = spec1.validateDocument(doc);
    expectProblem("fehlendes feld: list", vr);

    doc.put("list", nodes);

    // sollte richtig sein
    vr = spec1.validateDocument(doc);
    expectProblem("es fehlt ein list element", vr);

  }


}

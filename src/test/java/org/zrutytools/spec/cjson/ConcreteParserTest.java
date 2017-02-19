package org.zrutytools.spec.cjson;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;

public class ConcreteParserTest {


	@Test
	public void testThatParses_aString() throws Exception {
		
		// ein paar newlines, um das whitespace-handling zu testen
		// alles fängt in zeile 3 an
		String SAMPLE = "   \"hello\"       ";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;
	
		// expectPosition(3,1,3,8, nd);
		displayRegion(nd, SAMPLE);
		assertEquals("hello", cs.getValue());
	}

	private void displayRegion(ConcreteNode nd, String text) {
		String carets = "";
		StringBuilder digits = new StringBuilder();
		StringBuilder digits10 = new StringBuilder();
		for(int i=1;i<=99;i++){
			if(nd.getStartChar() <= i && i <= nd.getEndChar()){
				carets += "^";
			} else {
				carets += "_";
			}
			digits.append((char)('0' + (i % 10)));
			digits10.append((char)('0' + (i / 10)));
		}
		System.out.println("");
		System.out.println(text + "¶");
		System.out.println(carets);
		System.out.println(digits.toString());
		System.out.println(digits10.toString());
		System.out.println(nd.getStartLine() + "@" + nd.getStartChar() + " .. " + nd.getEndLine() + "@" + nd.getEndChar());
	}

	private void expectPosition(int startLine, int startPos, int endLine, int endPos, ConcreteNode nd) {
		assertEquals(startLine, nd.getStartLine());
		assertEquals(startPos, nd.getStartChar());
		assertEquals(endLine, nd.getEndLine());
		assertEquals(endPos, nd.getEndChar());
	}

	@Test
	public void testThatParses_aNull() throws Exception {
		
		String SAMPLE = "null";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;

		displayRegion(nd, SAMPLE);

		assertNull(cs.getValue());
	}

	
	@Test
	public void testThatParses_truel() throws Exception {
		
		String SAMPLE = "true";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;

		displayRegion(nd, SAMPLE);

		assertEquals(Boolean.TRUE, cs.getValue());
	}

	@Test
	public void testThatParses_false() throws Exception {
		
		String SAMPLE = "false";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;

		displayRegion(nd, SAMPLE);

		assertEquals(Boolean.FALSE, cs.getValue());
	}

	@Test
	public void testThatParses_42() throws Exception {
		
		String SAMPLE = "42";
		
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;

		displayRegion(nd, SAMPLE);

		assertEquals(Long.valueOf(42), cs.getValue());
	}
	
	@Test
	public void testThatParses_Pi() throws Exception {
		
		String SAMPLE = "3.14159265";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		assertTrue(nd instanceof ConcreteValue);
		ConcreteValue<?> cs = (ConcreteValue<?>) nd;

		displayRegion(nd, SAMPLE);

		assertEquals(Double.valueOf(3.14159265), (double)cs.getValue(), 0.00000001);
	}
	
	@Test
	public void testThatParses_Array() throws Exception {
		
		String SAMPLE = "[1, true , false, null]";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		
		displayRegion(nd, SAMPLE);

		assertTrue(nd instanceof ConcreteList);
		ConcreteList cs = (ConcreteList) nd;
		assertEquals(4, cs.getNodes().size());
		
		ConcreteValue<?> v1 = (ConcreteValue<?>) cs.getNodes().get(0);
		assertEquals(Long.valueOf(1), v1.getValue());
		
		displayRegion(v1, SAMPLE);

		ConcreteValue<?> v2 = (ConcreteValue<?>) cs.getNodes().get(1);
		assertEquals(Boolean.TRUE, v2.getValue());

		displayRegion(v2, SAMPLE);
		
		ConcreteValue<?> v3 = (ConcreteValue<?>) cs.getNodes().get(2);
		assertEquals(Boolean.FALSE, v3.getValue());

		displayRegion(v3, SAMPLE);

		ConcreteValue<?> v4 = (ConcreteValue<?>) cs.getNodes().get(3);
		assertNull(v4.getValue());
		
		displayRegion(v4, SAMPLE);
		
	}
	
	@Test
	public void testThatParses_EmptyObject() throws Exception {
		
		String SAMPLE = "      {        }        ";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		
		displayRegion(nd, SAMPLE);

		assertTrue(nd instanceof ConcreteObject);
		ConcreteObject ob = (ConcreteObject) nd;
		assertTrue(ob.getElements().isEmpty());
	}

	@Test
	public void testThatParses_SimpleObject() throws Exception {
		
		String SAMPLE = "{  \"a\" : 4321, \"b\" : {  }      }";
		ConcreteParser p = new ConcreteParser(SAMPLE);
		ConcreteNode nd = p.parse();
		
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

	}

}

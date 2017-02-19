package org.zrutytools.spec.cjson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * JSON parser that creates concrete objects
 *
 */
public class ConcreteParser {

	private JsonParser jp;

	public ConcreteParser(String src) throws JsonParseException, IOException {
		// jackson seems to have an error: when you parse a string like "3.14159265", the start location reported a the sole token will be wildly behind the real position. add a space and everything is fine.
		this(new ByteArrayInputStream((src + " ").getBytes("utf-8")));
	}
	
	public ConcreteParser(InputStream src) throws JsonParseException, IOException {
		
		 JsonFactory jsonFactory = new JsonFactory(); 
		 this.jp = jsonFactory.createParser(src);
		 
	}

	public ConcreteNode parse() throws IOException {
		JsonToken token = jp.nextToken();
		return parseToken(token);
	
	}

	static int NULL_LEN = 4;
	static int TRUE_LEN = 4;
	static int FALSE_LEN = 5;
	
	
	private ConcreteNode parseToken(JsonToken token) throws IOException {
		JsonLocation loc = jp.getCurrentLocation();
		int startLine = loc.getLineNr();
		int startChar = loc.getColumnNr();
		int len = jp.getTextLength();
		int endLine = startLine;
		int endChar = startChar + len;
		switch(token){
		case START_ARRAY:
			return parseArray();
		case START_OBJECT:
			return parseObject();
		case VALUE_NULL:
			return new ConcreteValue<Object>(startLine, startChar-NULL_LEN, endLine, endChar-NULL_LEN-1, null);
		case VALUE_TRUE:
			return new ConcreteValue<Boolean>(startLine, startChar-TRUE_LEN, endLine, endChar-TRUE_LEN-1, Boolean.TRUE);
		case VALUE_FALSE:
			return new ConcreteValue<Boolean>(startLine, startChar-FALSE_LEN, endLine, endChar-FALSE_LEN-1, Boolean.FALSE);
		case VALUE_NUMBER_FLOAT:
			return new ConcreteValue<Double>(startLine, startChar - len, endLine, startChar-1, jp.getDoubleValue());
		case VALUE_NUMBER_INT:
			return new ConcreteValue<Long>(startLine, startChar - len, endLine, startChar-1, jp.getLongValue());
		case VALUE_STRING:
			return new ConcreteValue<String>(startLine, startChar-1, endLine, endChar-1, jp.getText());
		default:
			throw new IllegalArgumentException("unable to parse token of type " + token + " at " + loc);
		}
	}

	private ConcreteObject parseObject() throws IOException {
		JsonLocation startLoc = jp.getCurrentLocation();

		Map<ConcreteValue<?>, ConcreteNode> values = new HashMap<>();

		JsonToken token = jp.nextToken();
		while(token != JsonToken.END_OBJECT){
			if(token != JsonToken.FIELD_NAME){
				throw new IllegalStateException("expected FIELD_NAME or END_OBJECT, got " + token);
			}
			String fieldName = jp.getText();
			JsonLocation loc = jp.getCurrentLocation();

			ConcreteValue<String> key = new ConcreteValue<>(loc.getLineNr(), loc.getColumnNr(), loc.getLineNr(), loc.getColumnNr() + fieldName.length(), fieldName);
			ConcreteNode  value = parse();
			values.put(key, value);
			token = jp.nextToken();
		}
		
		JsonLocation endLoc = jp.getCurrentLocation();

		return new ConcreteObject(startLoc.getLineNr(), startLoc.getColumnNr()-1, endLoc.getLineNr(), endLoc.getColumnNr()-1, values);
	}

	private ConcreteList parseArray() throws IOException {
		JsonLocation startLoc = jp.getCurrentLocation();

		List<ConcreteNode> values = new ArrayList<>();

		JsonToken token = jp.nextToken();
		while(token != JsonToken.END_ARRAY){
			values.add(parseToken(token));
			token = jp.nextToken();
		}
		
		JsonLocation endLoc = jp.getCurrentLocation();
		
		return new ConcreteList(startLoc.getLineNr(), startLoc.getColumnNr()-1, endLoc.getLineNr(), endLoc.getColumnNr()-1, values);
	}
}

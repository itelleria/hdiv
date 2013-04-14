package org.hdiv.hateoas.jackson;

import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.jackson.map.ObjectMapper;

public abstract class AbstractJackson1MarshallingIntegrationTests extends AbstractHDIVTestCase {

	protected ObjectMapper mapper;
	
	protected final void setUp() throws Exception {
		mapper = new ObjectMapper();
		
		super.setUp();
	}
	
	protected String write(Object object) throws Exception {

		Writer writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}

	protected <T> T read(String source, Class<T> targetType) throws Exception {
		return mapper.readValue(source, targetType);
	}
}

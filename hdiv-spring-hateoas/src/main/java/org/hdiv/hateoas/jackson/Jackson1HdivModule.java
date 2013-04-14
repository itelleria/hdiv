/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hdiv.hateoas.jackson;

import java.io.IOException;
import java.lang.reflect.Type;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.std.NonTypedScalarSerializerBase;
import org.hdiv.urlProcessor.LinkUrlProcessor;
import org.hdiv.util.HDIVUtil;
import org.springframework.hateoas.Link;

public class Jackson1HdivModule extends SimpleModule {

	/**
	 * Creates a new {@link Jackson1HdivModule}.
	 */
	public Jackson1HdivModule() {

		super("json-hdiv-module", new Version(1, 0, 0, null));

		setMixInAnnotation(Link.class, LinkMixin.class);
	}

	public final class UrlSerializer extends NonTypedScalarSerializerBase<String>
	{
		private LinkUrlProcessor linkUrlProcessor;
		
	    public UrlSerializer(LinkUrlProcessor linkUrlProcessor) {
	    	super(String.class);
	    	
	    	this.linkUrlProcessor = linkUrlProcessor;
	    }

	    @Override
	    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
	        throws IOException, JsonGenerationException
	    {
	    	String processedUrl = linkUrlProcessor.processUrl(HDIVUtil.getHttpServletRequest(), value);
	    	
	        jgen.writeString(processedUrl);
	    }

	    @Override
	    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
	    {
	        return createSchemaNode("string", true);
	    }
	}
}

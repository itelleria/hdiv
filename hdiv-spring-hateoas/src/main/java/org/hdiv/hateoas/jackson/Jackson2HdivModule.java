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

import org.hdiv.urlProcessor.LinkUrlProcessor;
import org.hdiv.util.HDIVUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.Link;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;

public class Jackson2HdivModule extends SimpleModule {

	private static final long serialVersionUID = 7806951456457932384L;

	@SuppressWarnings("deprecation")
	public Jackson2HdivModule() {

		super("json-hdiv-module", new Version(1, 0, 0, null));

		setMixInAnnotation(Link.class, LinkMixin.class);
	}
	
	public static class HrefSerializer extends NonTypedScalarSerializerBase<String>
	{
		private LinkUrlProcessor linkUrlProcessor;
		
	    public HrefSerializer() {
	    	super(String.class);
	    	
	    	ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(HDIVUtil.getHttpServletRequest().getServletContext());
	    	
	    	this.linkUrlProcessor = applicationContext.getBean(LinkUrlProcessor.class);
	    }

	    /**
	     * For Strings, both null and Empty String qualify for emptiness.
	     */
	    @Override
	    public boolean isEmpty(String value) {
	        return (value == null) || (value.length() == 0);
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
	    
	    @Override
	    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
	    {
	    	visitor.expectStringFormat(typeHint);
	    }
	}
}

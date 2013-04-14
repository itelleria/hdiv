package org.hdiv.hateoas.jackson;

import org.springframework.hateoas.Link;

public abstract class LinkMixin extends Link {

	private static final long serialVersionUID = 903175428883703163L;

	@Override
	@org.codehaus.jackson.map.annotate.JsonSerialize(using = org.hdiv.hateoas.jackson.Jackson1HdivModule.HrefSerializer.class)
	@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = org.hdiv.hateoas.jackson.Jackson2HdivModule.HrefSerializer.class)
	public abstract String getHref();
	
}

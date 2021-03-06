/*
 * Copyright 2012-2013 the original author or authors.
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

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

public class Jackson1HdivIntegrationTest extends AbstractJackson1MarshallingIntegrationTests {
	
	protected void onSetUp() throws Exception {
		mapper.registerModule(new Jackson1HdivModule());
	}

	public void testRendersSingleLinkAsObject() throws Exception {

		ResourceSupport resourceSupport = new ResourceSupport();
		resourceSupport.add(new Link("localhost"));

		assertTrue(write(resourceSupport).contains("_HDIV_STATE_"));
	}

	public void testRendersMultipleLinkAsArray() throws Exception {

		ResourceSupport resourceSupport = new ResourceSupport();
		resourceSupport.add(new Link("localhost"));
		resourceSupport.add(new Link("localhost2"));

		assertTrue(write(resourceSupport).contains("_HDIV_STATE_"));
	}
}

/*
 * Copyright 2004-2005 The Apache Software Foundation.
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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hdiv.config.HDIVConfig;
import org.hdiv.listener.InitListener;
import org.hdiv.util.HDIVUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;

/**
 * HDIV test parent class.
 * 
 * @author Gotzon Illarramendi
 */
public abstract class AbstractHDIVTestCase extends TestCase {

	private static Log log = LogFactory.getLog(AbstractHDIVTestCase.class);

	/**
	 * Spring Factory
	 */
	private ApplicationContext applicationContext = null;

	/**
	 * Hdiv config for this app.
	 */
	private HDIVConfig config;

	protected void setUp() throws Exception {
		
		String[] files = { 
				"/org/hdiv/config/hdiv-core-applicationContext.xml",
				"/org/hdiv/config/hdiv-config.xml", 
				"/org/hdiv/config/hdiv-validations.xml",
				"/org/hdiv/config/applicationContext-test.xml",
				"/org/hdiv/config/applicationContext-extra.xml"
		};

		if (this.applicationContext == null) {
			this.applicationContext = new ClassPathXmlApplicationContext(files);
		}

		// Servlet API mock
		HttpServletRequest request = (MockHttpServletRequest) this.applicationContext.getBean("mockRequest");
		HttpSession httpSession = request.getSession();
		ServletContext servletContext = httpSession.getServletContext();
		HDIVUtil.setHttpServletRequest(request);

		// Put Spring context on ServletContext
		StaticWebApplicationContext webApplicationContext = new StaticWebApplicationContext();
		webApplicationContext.setServletContext(servletContext);
		webApplicationContext.setParent(this.applicationContext);
		servletContext
				.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);

		// Initialize config
		this.config = (HDIVConfig) this.applicationContext.getBean("config");

		InitListener initListener = new InitListener();
		// Initialize ServletContext
		ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
		initListener.contextInitialized(servletContextEvent);
		// Initialize HttpSession
		HttpSessionEvent httpSessionEvent = new HttpSessionEvent(httpSession);
		initListener.sessionCreated(httpSessionEvent);
		// Initialize request
		ServletRequestEvent requestEvent = new ServletRequestEvent(servletContext, request);
		initListener.requestInitialized(requestEvent);

		if (log.isDebugEnabled()) {
			log.debug("Hdiv test context initialized");
		}

		onSetUp();
	}

	protected abstract void onSetUp() throws Exception;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @return the config
	 */
	public HDIVConfig getConfig() {
		return config;
	}
	
}

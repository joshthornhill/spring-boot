/*
 * Copyright 2012-2015 the original author or authors.
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

package org.springframework.boot.autoconfigure.amqp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RabbitProperties}.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Josh Thornhill
 */
public class RabbitPropertiesTests {

	private final RabbitProperties properties = new RabbitProperties();

	@Test
	public void addressesNotSet() {
		assertEquals("localhost", this.properties.getHost());
		assertEquals("localhost", this.properties.getUri().getHost());
		assertEquals(5672, this.properties.getPort());
		assertEquals(5672, this.properties.getUri().getPort());
	}

	@Test
	public void addressesSingleValued() {
		this.properties.setAddresses("myhost:9999");
		assertEquals("myhost", this.properties.getHost());
		assertEquals("myhost", this.properties.getUri().getHost());
		assertEquals(9999, this.properties.getPort());
		assertEquals(9999, this.properties.getUri().getPort());
	}

	@Test
	public void addressesDoubleValued() {
		this.properties.setAddresses("myhost:9999,otherhost:1111");
		assertNull(this.properties.getHost());
		assertEquals(9999, this.properties.getPort());
		assertNotNull(this.properties.getUri());
	}

	@Test
	public void addressesDoubleValuedWithCredentials() {
		this.properties.setAddresses("myhost:9999,root:password@otherhost:1111/host");
		assertNull(this.properties.getHost());
		assertEquals(9999, this.properties.getPort());
		assertNotNull(this.properties.getUri());
		assertEquals("root", this.properties.getUsername());
		assertEquals("root:password", this.properties.getUri().getUserInfo());
		assertEquals("host", this.properties.getVirtualHost());
		assertEquals("/host", this.properties.getUri().getPath());
	}

	@Test
	public void addressesDoubleValuedPreservesOrder() {
		this.properties.setAddresses("myhost:9999,ahost:1111/host");
		assertNull(this.properties.getHost());
		assertNotNull(this.properties.getUri());
		assertEquals("myhost:9999,ahost:1111", this.properties.getAddresses());
	}

	@Test
	public void addressesSingleValuedWithCredentials() {
		this.properties.setAddresses("amqp://root:password@otherhost:1111/host");
		assertEquals("otherhost", this.properties.getHost());
		assertEquals("otherhost", this.properties.getUri().getHost());
		assertEquals(1111, this.properties.getPort());
		assertEquals(1111, this.properties.getUri().getPort());
		assertEquals("root", this.properties.getUsername());
		assertEquals("root:password", this.properties.getUri().getUserInfo());
		assertEquals("host", this.properties.getVirtualHost());
		assertEquals("/host", this.properties.getUri().getPath());
	}

	@Test
	public void addressesSingleValuedWithCredentialsDefaultPort() {
		this.properties.setAddresses("amqp://root:password@lemur.cloudamqp.com/host");
		assertEquals("lemur.cloudamqp.com", this.properties.getHost());
		assertEquals("lemur.cloudamqp.com", this.properties.getUri().getHost());
		assertEquals(5672, this.properties.getPort());
		assertEquals(5672, this.properties.getUri().getPort());
		assertEquals("root", this.properties.getUsername());
		assertEquals("root:password", this.properties.getUri().getUserInfo());
		assertEquals("host", this.properties.getVirtualHost());
		assertEquals("/host", this.properties.getUri().getPath());
		assertEquals("lemur.cloudamqp.com:5672", this.properties.getAddresses());
	}

	@Test
	public void addressWithTrailingSlash() {
		this.properties.setAddresses("amqp://root:password@otherhost:1111/");
		assertEquals("otherhost", this.properties.getHost());
		assertEquals("otherhost", this.properties.getUri().getHost());
		assertEquals(1111, this.properties.getPort());
		assertEquals(1111, this.properties.getUri().getPort());
		assertEquals("root", this.properties.getUsername());
		assertEquals("root:password", this.properties.getUri().getUserInfo());
		assertEquals("/", this.properties.getVirtualHost());
		assertTrue("/%2F".equalsIgnoreCase(this.properties.getUri().getRawPath()));
	}

	@Test
	public void testDefaultVirtualHost() {
		this.properties.setVirtualHost("/");
		assertEquals("/", this.properties.getVirtualHost());
		assertTrue("/%2F".equalsIgnoreCase(this.properties.getUri().getRawPath()));
	}

	@Test
	public void testEmptyVirtualHost() {
		this.properties.setVirtualHost("");
		assertEquals("/", this.properties.getVirtualHost());
		assertTrue("/%2F".equalsIgnoreCase(this.properties.getUri().getRawPath()));
	}

	@Test
	public void testCustomVirtualHost() {
		this.properties.setVirtualHost("myvHost");
		assertEquals("myvHost", this.properties.getVirtualHost());
		assertEquals("/myvHost", this.properties.getUri().getPath());
	}

	@Test
	public void testCustomFalseVirtualHost() {
		this.properties.setVirtualHost("/myvHost");
		assertEquals("/myvHost", this.properties.getVirtualHost());
		assertTrue("/%2FmyvHost".equalsIgnoreCase(this.properties.getUri().getRawPath()));
	}

}

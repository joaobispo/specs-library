package org.specs.library;

import static org.junit.Assert.*;

import org.gradle.Person;
import org.junit.Test;

public class DummyTest {

	@Test
	public void test() {
		System.out.println(new Person("joao").getName());
		assertTrue(true);
		//fail("Not yet implemented");
	}

}

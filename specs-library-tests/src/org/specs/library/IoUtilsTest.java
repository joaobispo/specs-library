/**
 * 
 */
package org.specs.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author JoaoBispo
 *
 */
public class IoUtilsTest {

	@Test
	public void test() {
		assertEquals("file_contents", IoUtils.getResource("ioutils/iotest.txt"));
	}

}

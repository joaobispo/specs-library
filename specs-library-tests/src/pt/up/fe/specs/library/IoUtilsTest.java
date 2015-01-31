/**
 * 
 */
package pt.up.fe.specs.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.up.fe.specs.library.IoUtils;

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

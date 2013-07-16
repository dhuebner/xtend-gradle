/**
 * 
 */
package org.eclipse.xtend.gradle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Set;

import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dhuebner
 * 
 */
public class XtendCompilerTest extends AbstractGradleTest {

	@Before
	public void setUp() {
		File file = new File("src/test/resources/compile/simple");
		System.out.println(file.exists());
		project = (ProjectInternal) ProjectBuilder.builder().withProjectDir(file).build();
		project.evaluate();
		XtendCompile next = (XtendCompile) project.getTasksByName("compileXtend", true).iterator().next();
		assertNotNull(next);
		next.compile();
		
	}

	@Test
	public void testTaskPresent() {
		Set<XtendCompile> tasksByType = findCompileXtendTask();
		for (XtendCompile xtendCompile : tasksByType) {
			System.out.println(xtendCompile.getName());
		}
		assertEquals(
				"After applying of xtend plugin, two (one for 'main' and one for 'test' sourceset) compiler task should be avaiable",
				2, tasksByType.size());
	}

}

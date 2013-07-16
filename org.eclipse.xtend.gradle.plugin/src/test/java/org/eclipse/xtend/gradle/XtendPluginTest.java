package org.eclipse.xtend.gradle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Ignore;
import org.junit.Test;

public class XtendPluginTest extends AbstractGradleTest {

	@Override
	public void setUp() {
		project = (ProjectInternal) ProjectBuilder.builder().build();
		project.apply(Collections.singletonMap("plugin", "java"));
		project.apply(Collections.singletonMap("plugin", "xtend"));
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

	@Test
	public void testDefaultSettings() {
		XtendCompile xtendTask = findMainTask();
		assertTrue(xtendTask.getXtendGenTargetDir().getAbsolutePath().endsWith("main/xtend-gen"));
		assertEquals("xtend-main", xtendTask.getXtendTempDir().getName());

		XtendCompile xtendTestTask = findTestTask();
		assertTrue(xtendTestTask.getXtendGenTargetDir().getAbsolutePath().endsWith("test/xtend-gen"));
		assertEquals("xtend-test", xtendTestTask.getXtendTempDir().getName());
		assertNull(xtendTask.getEncoding());
	}

	@Test
	public void testSourceFolder() {
		XtendCompile xtendTask = findMainTask();
		assertTrue(xtendTask.getXtendGenTargetDir().getAbsolutePath().endsWith("main/xtend-gen"));
		assertEquals("xtend-main", xtendTask.getXtendTempDir().getName());

		XtendCompile xtendTestTask = findTestTask();
		assertTrue(xtendTestTask.getXtendGenTargetDir().getAbsolutePath().endsWith("test/xtend-gen"));
		assertEquals("xtend-test", xtendTestTask.getXtendTempDir().getName());
		assertNull(xtendTask.getEncoding());
	}

	@Test
	@Ignore
	public void testEncoding() {
		XtendCompile xtendTask = findMainTask();
		assertNull(xtendTask.getEncoding());
		project.getTasks().getByName(xtendTask.getName()).setProperty("encoding", "UTF-8");
		assertEquals("UTF-8", xtendTask.getEncoding());
	}

}

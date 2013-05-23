package org.eclipse.xtend.gradle;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

public class XtendPluginTest {
	private Project project;

	@Before
	public void setUp() {
		project = ProjectBuilder.builder().build();
		project.apply(Collections.singletonMap("plugin", "xtend"));
	}

	@Test
	public void testTaskPresent() {
		Set<CompileXtendTask> tasksByName = project.getTasks().withType(
				CompileXtendTask.class);
		assertEquals(
				"After applying of xtend plugin, an compileXtend task is available",
				1, tasksByName.size());
	}

	@Test
	public void testDefaultSettings() {
		CompileXtendTask xtendTask = findCompileXtendTask();
		assertEquals("java", xtendTask.getXtendSrcDir().getName());
		assertEquals("xtend-gen", xtendTask.getXtendGenTargetDir().getName());
		assertEquals("xtend-temp", xtendTask.getXtendTempDir().getName());
		assertNull(xtendTask.getEncoding());
	}

	@Test
	public void testSettings() {
//		CompileXtendTask xtendTask = findCompileXtendTask();
//		xtendTask.getProject().getConfigurations().getPlugins().findPlugin("xtend").setProperty("xtend.encoding", "UTF-8");
//		assertEquals("UTF-8", xtendTask.getEncoding());
	}

	private CompileXtendTask findCompileXtendTask() {
		return project.getTasks().withType(CompileXtendTask.class).iterator()
				.next();
	}
}

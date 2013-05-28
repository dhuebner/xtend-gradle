package org.eclipse.xtend.gradle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.gradle.api.internal.TaskInternal;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

public class XtendPluginTest {
	private ProjectInternal project;

	@Before
	public void setUp() {
		project = (ProjectInternal) ProjectBuilder.builder().build();
		project.apply(Collections.singletonMap("plugin", "xtend"));
	}

	@Test
	public void testTaskPresent() {
		Set<XtendCompile> tasksByName = project.getTasks().withType(
				XtendCompile.class);
		assertEquals(
				"After applying of xtend plugin, an compileXtend task is available",
				1, tasksByName.size());
	}

	@Test
	public void testDefaultSettings() {
		XtendCompile xtendTask = findCompileXtendTask();
		assertEquals("xtend-gen", xtendTask.getXtendGenTargetDir().getName());
		assertEquals("xtend-temp", xtendTask.getXtendTempDir().getName());
		assertNull(xtendTask.getEncoding());
	}

	@Test
	public void testSettings() {
		XtendCompile xtendTask = findCompileXtendTask();
		xtendTask.getProject().getTasks().getByName("compileXtend")
				.setProperty("encoding", "UTF-8");
		assertEquals("UTF-8", xtendTask.getEncoding());
	}

	@Test
	public void testCompiled() {
		XtendCompile xtendTask = findCompileXtendTask();
		String genPath = xtendTask.getXtendGenTargetDir().getName();
		((TaskInternal) xtendTask).execute();
		File genDirFile = project.getFileResolver()
				.withBaseDir(project.getBuildDir()).resolve(genPath);
		assertTrue("Gendir '" + genDirFile.getAbsolutePath() + "' exists",
				genDirFile.isDirectory());
		assertNotNull("Gen folder is empty", genDirFile.list());
	}

	private XtendCompile findCompileXtendTask() {
		return project.getTasks().withType(XtendCompile.class).iterator()
				.next();
	}
}

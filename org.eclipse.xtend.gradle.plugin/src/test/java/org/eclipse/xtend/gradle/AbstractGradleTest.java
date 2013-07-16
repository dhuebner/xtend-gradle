package org.eclipse.xtend.gradle;

import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.tasks.TaskCollection;
import org.junit.Before;

public abstract class AbstractGradleTest {

	protected ProjectInternal project;

	@Before
	public abstract void setUp();

	protected TaskCollection<XtendCompile> findCompileXtendTask() {
		return project.getTasks().withType(XtendCompile.class);
	}

	protected XtendCompile findMainTask() {
		return findCompileXtendTask().getByName("compileXtend");
	}

	protected XtendCompile findTestTask() {
		return findCompileXtendTask().getByName("compileTestXtend");
	}

}
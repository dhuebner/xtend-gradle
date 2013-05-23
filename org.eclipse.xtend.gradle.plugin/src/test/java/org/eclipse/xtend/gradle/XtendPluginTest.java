package org.eclipse.xtend.gradle;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import groovy.lang.Closure;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import com.google.common.collect.Maps;

public class XtendPluginTest {

	@Test
	public void testCompilation() {
		Project project = ProjectBuilder.builder().build();
		HashMap<Object, Object> map = Maps.newHashMap();
		map.put("xtend", Plugin.class);
		project.apply(map);

		// assertTrue(project.tasks.hello instanceof GreetingTask);
	}

}

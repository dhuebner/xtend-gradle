package org.eclipse.xtend.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/** 
 * @author Dennis Huebner
 */
class XtendPlugin implements Plugin<Project> {
	val PLUGIN_NAME = 'Xtend'
	val XTEND_DIR_PREFIX = 'xtend'
	val DEFAULT_OUTPUT = "xtend-gen"

	override apply(Project project) {
		project.plugins.apply(JavaPlugin)

		project.convention.getPlugin(JavaPluginConvention).sourceSets.all [ sourceSet |
			val taskName = sourceSet.getTaskName("compile", PLUGIN_NAME)
			val projectFileResolver = (project as ProjectInternal).fileResolver
			// 1) Add a new 'xtend-gen' directory
			val xtendGen = new DefaultSourceDirectorySet((sourceSet as DefaultSourceSet).displayName + " Xtend gen",
				projectFileResolver);
			xtendGen.srcDir(projectFileResolver.withBaseDir("src").withBaseDir(sourceSet.name).resolve(DEFAULT_OUTPUT))
			
			new DslObject(sourceSet).getConvention().getPlugins().put(XTEND_DIR_PREFIX, xtendGen);
			sourceSet.getAllSource().source(xtendGen);
			
			val XtendCompile xtendTask = project.getTasks().add(taskName, XtendCompile)
			xtendTask.setSource(sourceSet.java)
			xtendTask.xtendGenTargetDir = xtendGen.getSrcDirs.head
			xtendTask.xtendTempDir = projectFileResolver.withBaseDir(project.buildDir).resolve(
				XTEND_DIR_PREFIX + "-" + sourceSet.name)
			xtendTask.classpath = project.configurations.findByName('compile')
			// adding xtend-gen folder to javac input
			sourceSet.java.srcDir(xtendGen);
			//Execute xtend before java
			project.tasks.getByName(sourceSet.compileJavaTaskName).dependsOn(taskName)
		]

	}
}

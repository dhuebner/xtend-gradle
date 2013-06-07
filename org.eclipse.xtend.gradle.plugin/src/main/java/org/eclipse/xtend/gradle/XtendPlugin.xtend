package org.eclipse.xtend.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/** 
 * @author Dennis Huebner
 */
class XtendPlugin implements Plugin<Project> {
	val PLUGIN_NAME = 'Xtend'
	val TEMP_DIR_PREFIX = 'xtend'
	val DEFAULT_OUTPUT = "generated-src/xtend-gen/"

	override apply(Project project) {
		project.plugins.apply(typeof(JavaPlugin))

		project.convention.getPlugin(typeof(JavaPluginConvention)).sourceSets.all [ sourceSet |
			val taskName = sourceSet.getTaskName("compile", PLUGIN_NAME)
			val buildDirFileResolver = (project as ProjectInternal).fileResolver.withBaseDir(project.buildDir)
			val xtendOutputDirectory = buildDirFileResolver.withBaseDir(DEFAULT_OUTPUT).resolve(sourceSet.name)
			val xtendTask = project.getTasks().add(taskName, typeof(XtendCompile))
			xtendTask.setSource(sourceSet.java)
			xtendTask.xtendGenTargetDir = xtendOutputDirectory
			xtendTask.xtendTempDir = buildDirFileResolver.resolve(TEMP_DIR_PREFIX + "-" + sourceSet.name)
			xtendTask.classpath = project.configurations.findByName('compile')
			//Execute xtend before java
			project.tasks.getByName(sourceSet.compileJavaTaskName).dependsOn(taskName)
		]

	}
}

package org.eclipse.xtend.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.ProjectSourceSet

/** 
 * @author Dennis Huebner
 */
class XtendPlugin implements Plugin<Project> {
	val String COMPILER_TASK = 'compileXtend'
	val String PLUGIN_NAME = 'xtend'

	override apply(Project project) {
		project.plugins.apply(typeof(JavaPlugin))
		val xtendPluginConvention = new XtendPluginConvention(project as ProjectInternal)
		project.convention.plugins.put(PLUGIN_NAME, xtendPluginConvention)
		configureSourceSets(xtendPluginConvention)
		configureCompiler(xtendPluginConvention)
		configureBuild(project)
	}
	
	def configureSourceSets(XtendPluginConvention convention) {
		val srcSet  = convention.project.extensions.getByType(typeof(ProjectSourceSet))
		srcSet.getByName('main').getByName('java').source.srcDir(convention.xtendGenTargetDir)
	}

	def configureBuild(Project it) {
		var javaCompilerTasks = tasks.withType(typeof(JavaCompile))
		if (javaCompilerTasks.empty) {
			throw new GradleException(
				'compilerXtend task depends on missing ' + JavaPlugin::COMPILE_JAVA_TASK_NAME + ' task. Build failed');
		}
		val javaCompile = javaCompilerTasks.iterator.next
		var task = tasks.add(COMPILER_TASK, typeof(CompileXtendTask))
		javaCompile.dependsOn.add(task)
	}

	def configureCompiler(XtendPluginConvention it) {
		project.tasks.withType(typeof(CompileXtendTask)).all(
			[task|task.getConventionMapping().map("xtendSrcDir", [|xtendSrcDir])])
			
		project.tasks.withType(typeof(CompileXtendTask)).all(
			[task|task.getConventionMapping().map("xtendTempDir", [|xtendTempDir])])
			
		project.tasks.withType(typeof(CompileXtendTask)).all(
			[task|task.getConventionMapping().map("xtendGenTargetDir", [|xtendGenTargetDir])])
	}

}

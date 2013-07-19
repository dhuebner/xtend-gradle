/**
 * 
 */
package org.eclipse.xtend.gradle;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * @author Dennis Huebner
 * 
 */
public class XtendPlugin implements Plugin<Project> {
	public static final String PLUGIN_NAME = "Xtend";
	public static final String XTEND_DIR_PREFIX = "xtend";
	public static final String DEFAULT_OUTPUT = "xtend-gen";

	public void apply(final Project project) {
		project.getPlugins().apply(JavaPlugin.class);

		SourceSetContainer sourceSets = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();
		final Action<SourceSet> _function = new Action<SourceSet>() {
			public void execute(final SourceSet sourceSet) {
				String taskName = sourceSet.getTaskName("compile", PLUGIN_NAME);
				FileResolver projectFileResolver = ((ProjectInternal) project).getFileResolver();
				// Add a new "xtend-gen" src directory
				DefaultSourceDirectorySet xtendGen = new DefaultSourceDirectorySet(
						((DefaultSourceSet) sourceSet).getDisplayName() + " Xtend gen", projectFileResolver);
				xtendGen.srcDir(projectFileResolver.withBaseDir("src").withBaseDir(sourceSet.getName())
						.resolve(DEFAULT_OUTPUT));
				new DslObject(sourceSet).getConvention().getPlugins().put(XTEND_DIR_PREFIX, xtendGen);
				sourceSet.getAllSource().source(xtendGen);

				// Setup xtend compiler task
				XtendCompile xtendTask = project.getTasks().add(taskName, XtendCompile.class);
				xtendTask.setSource(sourceSet.getJava().getSrcDirs());
				xtendTask.setXtendGenTargetDir(xtendGen.getSrcDirs().iterator().next());
				xtendTask.setXtendTempDir(projectFileResolver.withBaseDir(project.getBuildDir()).resolve(
						XTEND_DIR_PREFIX + "-" + sourceSet.getName()));
				JavaCompile javacTask = project.getTasks().withType(JavaCompile.class)
						.getByName(sourceSet.getCompileJavaTaskName());
				xtendTask.setClasspath(project.getConfigurations().findByName("compile"));
				xtendTask.setJavaTask(javacTask);
				// adding xtend-gen folder to javac input
				sourceSet.getJava().srcDir(xtendGen);
				// Execute xtend before java
				javacTask.dependsOn(taskName);
			}
		};
		sourceSets.all(_function);
	}
}
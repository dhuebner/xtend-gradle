package org.eclipse.xtend.gradle;

import java.io.File;
import org.eclipse.xtend.gradle.XtendCompile;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;

/**
 * @author Dennis Huebner
 */
@SuppressWarnings("all")
public class XtendPlugin implements Plugin<Project> {
  private final String PLUGIN_NAME = "Xtend";
  
  private final String TEMP_DIR_PREFIX = "xtend";
  
  private final String DEFAULT_OUTPUT = "generated-src/xtend-gen/";
  
  public void apply(final Project project) {
    PluginContainer _plugins = project.getPlugins();
    _plugins.<JavaPlugin>apply(JavaPlugin.class);
    Convention _convention = project.getConvention();
    JavaPluginConvention _plugin = _convention.<JavaPluginConvention>getPlugin(JavaPluginConvention.class);
    SourceSetContainer _sourceSets = _plugin.getSourceSets();
    final Action<SourceSet> _function = new Action<SourceSet>() {
        public void execute(final SourceSet sourceSet) {
          final String taskName = sourceSet.getTaskName("compile", XtendPlugin.this.PLUGIN_NAME);
          FileResolver _fileResolver = ((ProjectInternal) project).getFileResolver();
          File _buildDir = project.getBuildDir();
          final FileResolver buildDirFileResolver = _fileResolver.withBaseDir(_buildDir);
          FileResolver _withBaseDir = buildDirFileResolver.withBaseDir(XtendPlugin.this.DEFAULT_OUTPUT);
          String _name = sourceSet.getName();
          final File xtendOutputDirectory = _withBaseDir.resolve(_name);
          TaskContainer _tasks = project.getTasks();
          final XtendCompile xtendTask = _tasks.<XtendCompile>add(taskName, XtendCompile.class);
          SourceDirectorySet _java = sourceSet.getJava();
          xtendTask.setSource(_java);
          xtendTask.setXtendGenTargetDir(xtendOutputDirectory);
          String _plus = (XtendPlugin.this.TEMP_DIR_PREFIX + "-");
          String _name_1 = sourceSet.getName();
          String _plus_1 = (_plus + _name_1);
          File _resolve = buildDirFileResolver.resolve(_plus_1);
          xtendTask.setXtendTempDir(_resolve);
          ConfigurationContainer _configurations = project.getConfigurations();
          Configuration _findByName = _configurations.findByName("compile");
          xtendTask.setClasspath(_findByName);
          TaskContainer _tasks_1 = project.getTasks();
          String _compileJavaTaskName = sourceSet.getCompileJavaTaskName();
          Task _byName = _tasks_1.getByName(_compileJavaTaskName);
          _byName.dependsOn(taskName);
        }
      };
    _sourceSets.all(_function);
  }
}

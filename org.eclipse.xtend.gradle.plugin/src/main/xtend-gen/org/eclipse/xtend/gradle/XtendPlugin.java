package org.eclipse.xtend.gradle;

import java.io.File;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend.gradle.XtendCompile;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.DefaultSourceSet;
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
  
  private final String XTEND_DIR_PREFIX = "xtend";
  
  private final String DEFAULT_OUTPUT = "xtend-gen";
  
  public void apply(final Project project) {
    PluginContainer _plugins = project.getPlugins();
    _plugins.<JavaPlugin>apply(JavaPlugin.class);
    Convention _convention = project.getConvention();
    JavaPluginConvention _plugin = _convention.<JavaPluginConvention>getPlugin(JavaPluginConvention.class);
    SourceSetContainer _sourceSets = _plugin.getSourceSets();
    final Action<SourceSet> _function = new Action<SourceSet>() {
        public void execute(final SourceSet sourceSet) {
          final String taskName = sourceSet.getTaskName("compile", XtendPlugin.this.PLUGIN_NAME);
          final FileResolver projectFileResolver = ((ProjectInternal) project).getFileResolver();
          String _displayName = ((DefaultSourceSet) sourceSet).getDisplayName();
          String _plus = (_displayName + " Xtend gen");
          DefaultSourceDirectorySet _defaultSourceDirectorySet = new DefaultSourceDirectorySet(_plus, projectFileResolver);
          final DefaultSourceDirectorySet xtendGen = _defaultSourceDirectorySet;
          FileResolver _withBaseDir = projectFileResolver.withBaseDir("src");
          String _name = sourceSet.getName();
          FileResolver _withBaseDir_1 = _withBaseDir.withBaseDir(_name);
          File _resolve = _withBaseDir_1.resolve(XtendPlugin.this.DEFAULT_OUTPUT);
          xtendGen.srcDir(_resolve);
          DslObject _dslObject = new DslObject(sourceSet);
          Convention _convention = _dslObject.getConvention();
          Map<String,Object> _plugins = _convention.getPlugins();
          _plugins.put(XtendPlugin.this.XTEND_DIR_PREFIX, xtendGen);
          SourceDirectorySet _allSource = sourceSet.getAllSource();
          _allSource.source(xtendGen);
          TaskContainer _tasks = project.getTasks();
          final XtendCompile xtendTask = _tasks.<XtendCompile>add(taskName, XtendCompile.class);
          SourceDirectorySet _java = sourceSet.getJava();
          xtendTask.setSource(_java);
          Set<File> _srcDirs = xtendGen.getSrcDirs();
          File _head = IterableExtensions.<File>head(_srcDirs);
          xtendTask.setXtendGenTargetDir(_head);
          File _buildDir = project.getBuildDir();
          FileResolver _withBaseDir_2 = projectFileResolver.withBaseDir(_buildDir);
          String _plus_1 = (XtendPlugin.this.XTEND_DIR_PREFIX + "-");
          String _name_1 = sourceSet.getName();
          String _plus_2 = (_plus_1 + _name_1);
          File _resolve_1 = _withBaseDir_2.resolve(_plus_2);
          xtendTask.setXtendTempDir(_resolve_1);
          ConfigurationContainer _configurations = project.getConfigurations();
          Configuration _findByName = _configurations.findByName("compile");
          xtendTask.setClasspath(_findByName);
          SourceDirectorySet _java_1 = sourceSet.getJava();
          _java_1.srcDir(xtendGen);
          TaskContainer _tasks_1 = project.getTasks();
          String _compileJavaTaskName = sourceSet.getCompileJavaTaskName();
          Task _byName = _tasks_1.getByName(_compileJavaTaskName);
          _byName.dependsOn(taskName);
        }
      };
    _sourceSets.all(_function);
  }
}

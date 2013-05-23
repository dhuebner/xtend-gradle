package org.eclipse.xtend.gradle;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.eclipse.xtend.gradle.CompileXtendTask;
import org.eclipse.xtend.gradle.XtendPluginConvention;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.TaskContainerInternal;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.FunctionalSourceSet;
import org.gradle.api.tasks.LanguageSourceSet;
import org.gradle.api.tasks.ProjectSourceSet;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * @author Dennis Huebner
 */
@SuppressWarnings("all")
public class XtendPlugin implements Plugin<Project> {
  private final String COMPILER_TASK = "compileXtend";
  
  private final String PLUGIN_NAME = "xtend";
  
  public void apply(final Project project) {
    PluginContainer _plugins = project.getPlugins();
    _plugins.<JavaPlugin>apply(JavaPlugin.class);
    XtendPluginConvention _xtendPluginConvention = new XtendPluginConvention(((ProjectInternal) project));
    final XtendPluginConvention xtendPluginConvention = _xtendPluginConvention;
    Convention _convention = project.getConvention();
    Map<String,Object> _plugins_1 = _convention.getPlugins();
    _plugins_1.put(this.PLUGIN_NAME, xtendPluginConvention);
    this.configureSourceSets(xtendPluginConvention);
    this.configureCompiler(xtendPluginConvention);
    this.configureBuild(project);
  }
  
  public SourceDirectorySet configureSourceSets(final XtendPluginConvention convention) {
    SourceDirectorySet _xblockexpression = null;
    {
      ProjectInternal _project = convention.getProject();
      ExtensionContainer _extensions = _project.getExtensions();
      final ProjectSourceSet srcSet = _extensions.<ProjectSourceSet>getByType(ProjectSourceSet.class);
      FunctionalSourceSet _byName = srcSet.getByName("main");
      LanguageSourceSet _byName_1 = _byName.getByName("java");
      SourceDirectorySet _source = _byName_1.getSource();
      File _xtendGenTargetDir = convention.getXtendGenTargetDir();
      SourceDirectorySet _srcDir = _source.srcDir(_xtendGenTargetDir);
      _xblockexpression = (_srcDir);
    }
    return _xblockexpression;
  }
  
  public boolean configureBuild(final Project it) {
    boolean _xblockexpression = false;
    {
      TaskContainer _tasks = it.getTasks();
      TaskCollection<JavaCompile> javaCompilerTasks = _tasks.<JavaCompile>withType(JavaCompile.class);
      boolean _isEmpty = javaCompilerTasks.isEmpty();
      if (_isEmpty) {
        String _plus = ("compilerXtend task depends on missing " + JavaPlugin.COMPILE_JAVA_TASK_NAME);
        String _plus_1 = (_plus + " task. Build failed");
        GradleException _gradleException = new GradleException(_plus_1);
        throw _gradleException;
      }
      Iterator<JavaCompile> _iterator = javaCompilerTasks.iterator();
      final JavaCompile javaCompile = _iterator.next();
      TaskContainer _tasks_1 = it.getTasks();
      CompileXtendTask task = _tasks_1.<CompileXtendTask>add(this.COMPILER_TASK, CompileXtendTask.class);
      Set<Object> _dependsOn = javaCompile.getDependsOn();
      boolean _add = _dependsOn.add(task);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public void configureCompiler(final XtendPluginConvention it) {
    ProjectInternal _project = it.getProject();
    TaskContainerInternal _tasks = _project.getTasks();
    TaskCollection<CompileXtendTask> _withType = _tasks.<CompileXtendTask>withType(CompileXtendTask.class);
    final Action<CompileXtendTask> _function = new Action<CompileXtendTask>() {
        public void execute(final CompileXtendTask task) {
          ConventionMapping _conventionMapping = task.getConventionMapping();
          final Callable<File> _function = new Callable<File>() {
              public File call() throws Exception {
                File _xtendSrcDir = it.getXtendSrcDir();
                return _xtendSrcDir;
              }
            };
          _conventionMapping.map("xtendSrcDir", _function);
        }
      };
    _withType.all(_function);
    ProjectInternal _project_1 = it.getProject();
    TaskContainerInternal _tasks_1 = _project_1.getTasks();
    TaskCollection<CompileXtendTask> _withType_1 = _tasks_1.<CompileXtendTask>withType(CompileXtendTask.class);
    final Action<CompileXtendTask> _function_1 = new Action<CompileXtendTask>() {
        public void execute(final CompileXtendTask task) {
          ConventionMapping _conventionMapping = task.getConventionMapping();
          final Callable<File> _function = new Callable<File>() {
              public File call() throws Exception {
                File _xtendTempDir = it.getXtendTempDir();
                return _xtendTempDir;
              }
            };
          _conventionMapping.map("xtendTempDir", _function);
        }
      };
    _withType_1.all(_function_1);
    ProjectInternal _project_2 = it.getProject();
    TaskContainerInternal _tasks_2 = _project_2.getTasks();
    TaskCollection<CompileXtendTask> _withType_2 = _tasks_2.<CompileXtendTask>withType(CompileXtendTask.class);
    final Action<CompileXtendTask> _function_2 = new Action<CompileXtendTask>() {
        public void execute(final CompileXtendTask task) {
          ConventionMapping _conventionMapping = task.getConventionMapping();
          final Callable<File> _function = new Callable<File>() {
              public File call() throws Exception {
                File _xtendGenTargetDir = it.getXtendGenTargetDir();
                return _xtendGenTargetDir;
              }
            };
          _conventionMapping.map("xtendGenTargetDir", _function);
        }
      };
    _withType_2.all(_function_2);
  }
}

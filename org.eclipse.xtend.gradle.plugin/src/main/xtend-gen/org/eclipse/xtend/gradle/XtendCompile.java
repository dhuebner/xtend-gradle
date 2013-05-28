package org.eclipse.xtend.gradle;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.xtend.core.XtendInjectorSingleton;
import org.eclipse.xtend.core.compiler.batch.XtendBatchCompiler;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.FileTree;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;

@SuppressWarnings("all")
public class XtendCompile extends AbstractCompile {
  private String _encoding;
  
  public String getEncoding() {
    return this._encoding;
  }
  
  public void setEncoding(final String encoding) {
    this._encoding = encoding;
  }
  
  @OutputDirectory
  private File _xtendGenTargetDir;
  
  public File getXtendGenTargetDir() {
    return this._xtendGenTargetDir;
  }
  
  public void setXtendGenTargetDir(final File xtendGenTargetDir) {
    this._xtendGenTargetDir = xtendGenTargetDir;
  }
  
  private File _xtendTempDir;
  
  public File getXtendTempDir() {
    return this._xtendTempDir;
  }
  
  public void setXtendTempDir(final File xtendTempDir) {
    this._xtendTempDir = xtendTempDir;
  }
  
  @TaskAction
  protected void compile() {
    BasicConfigurator.configure();
    Injector injector = XtendInjectorSingleton.INJECTOR;
    XtendBatchCompiler compiler = injector.<XtendBatchCompiler>getInstance(XtendBatchCompiler.class);
    Project _project = this.getProject();
    ConfigurationContainer _configurations = _project.getConfigurations();
    Configuration _findByName = _configurations.findByName(JavaPlugin.COMPILE_CONFIGURATION_NAME);
    String classpath = _findByName.getAsPath();
    FileTree _source = this.getSource();
    final Function1<File,String> _function = new Function1<File,String>() {
        public String apply(final File it) {
          String _absolutePath = it.getAbsolutePath();
          return _absolutePath;
        }
      };
    String _join = IterableExtensions.<File>join(_source, File.pathSeparator, _function);
    compiler.setSourcePath(_join);
    File _xtendGenTargetDir = this.getXtendGenTargetDir();
    String _absolutePath = _xtendGenTargetDir.getAbsolutePath();
    compiler.setOutputPath(_absolutePath);
    compiler.setClassPath(classpath);
    File _xtendTempDir = this.getXtendTempDir();
    String _absolutePath_1 = _xtendTempDir.getAbsolutePath();
    compiler.setTempDirectory(_absolutePath_1);
    String _encoding = this.getEncoding();
    boolean _notEquals = (!Objects.equal(_encoding, null));
    if (_notEquals) {
      String _encoding_1 = this.getEncoding();
      compiler.setFileEncoding(_encoding_1);
    }
    String _fileEncoding = compiler.getFileEncoding();
    String _plus = ("Encoding: " + _fileEncoding);
    InputOutput.<String>println(_plus);
    boolean _compile = compiler.compile();
    boolean _not = (!_compile);
    if (_not) {
      GradleException _gradleException = new GradleException("Xtend compilation failed.");
      throw _gradleException;
    }
    Logger _logger = this.getLogger();
    _logger.info("Miau!");
  }
}

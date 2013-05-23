package org.eclipse.xtend.gradle;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.xtend.core.XtendInjectorSingleton;
import org.eclipse.xtend.core.compiler.batch.XtendBatchCompiler;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

@SuppressWarnings("all")
public class CompileXtendTask extends ConventionTask {
  private String _encoding;
  
  public String getEncoding() {
    return this._encoding;
  }
  
  public void setEncoding(final String encoding) {
    this._encoding = encoding;
  }
  
  @InputDirectory
  private File _xtendSrcDir;
  
  public File getXtendSrcDir() {
    return this._xtendSrcDir;
  }
  
  public void setXtendSrcDir(final File xtendSrcDir) {
    this._xtendSrcDir = xtendSrcDir;
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
    File _xtendSrcDir = this.getXtendSrcDir();
    String _absolutePath = _xtendSrcDir.getAbsolutePath();
    compiler.setSourcePath(_absolutePath);
    File _xtendGenTargetDir = this.getXtendGenTargetDir();
    String _absolutePath_1 = _xtendGenTargetDir.getAbsolutePath();
    compiler.setOutputPath(_absolutePath_1);
    compiler.setClassPath(classpath);
    File _xtendTempDir = this.getXtendTempDir();
    String _absolutePath_2 = _xtendTempDir.getAbsolutePath();
    compiler.setTempDirectory(_absolutePath_2);
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

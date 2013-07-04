package org.eclipse.xtend.gradle;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import java.io.File;
import java.util.Set;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.xtend.core.XtendInjectorSingleton;
import org.eclipse.xtend.core.compiler.batch.XtendBatchCompiler;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

@SuppressWarnings("all")
public class XtendCompile extends SourceTask {
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
  
  private FileCollection _classpath;
  
  public FileCollection getClasspath() {
    return this._classpath;
  }
  
  public void setClasspath(final FileCollection classpath) {
    this._classpath = classpath;
  }
  
  private SourceDirectorySet sourceSet;
  
  @TaskAction
  protected void compile() {
    BasicConfigurator.configure();
    Injector injector = XtendInjectorSingleton.INJECTOR;
    final XtendBatchCompiler compiler = injector.<XtendBatchCompiler>getInstance(XtendBatchCompiler.class);
    Set<File> _srcDirs = this.sourceSet.getSrcDirs();
    final Function1<File,String> _function = new Function1<File,String>() {
        public String apply(final File it) {
          String _absolutePath = it.getAbsolutePath();
          return _absolutePath;
        }
      };
    final String sourceDir = IterableExtensions.<File>join(_srcDirs, File.pathSeparator, _function);
    Logger _logger = this.getLogger();
    String _plus = ("Source: " + sourceDir);
    _logger.info(_plus);
    Logger _logger_1 = this.getLogger();
    File _xtendGenTargetDir = this.getXtendGenTargetDir();
    String _absolutePath = _xtendGenTargetDir.getAbsolutePath();
    String _plus_1 = ("outputPath: " + _absolutePath);
    _logger_1.info(_plus_1);
    Logger _logger_2 = this.getLogger();
    FileCollection _classpath = this.getClasspath();
    String _asPath = _classpath.getAsPath();
    String _plus_2 = ("classPath: " + _asPath);
    _logger_2.info(_plus_2);
    Logger _logger_3 = this.getLogger();
    String _fileEncoding = compiler.getFileEncoding();
    String _plus_3 = ("Encoding: " + _fileEncoding);
    _logger_3.info(_plus_3);
    compiler.setSourcePath(sourceDir);
    File _xtendGenTargetDir_1 = this.getXtendGenTargetDir();
    String _absolutePath_1 = _xtendGenTargetDir_1.getAbsolutePath();
    compiler.setOutputPath(_absolutePath_1);
    FileCollection _classpath_1 = this.getClasspath();
    String _asPath_1 = _classpath_1.getAsPath();
    compiler.setClassPath(_asPath_1);
    File _xtendTempDir = this.getXtendTempDir();
    String _absolutePath_2 = _xtendTempDir.getAbsolutePath();
    compiler.setTempDirectory(_absolutePath_2);
    String _encoding = this.getEncoding();
    boolean _notEquals = (!Objects.equal(_encoding, null));
    if (_notEquals) {
      String _encoding_1 = this.getEncoding();
      compiler.setFileEncoding(_encoding_1);
    }
    boolean _compile = compiler.compile();
    boolean _not = (!_compile);
    if (_not) {
      GradleException _gradleException = new GradleException("Xtend compilation failed.");
      throw _gradleException;
    }
    Logger _logger_4 = this.getLogger();
    _logger_4.info("org.eclipse.xtend.gradle.XtendCompile.compile");
  }
  
  public void setSource(final Object source) {
    if ((source instanceof SourceDirectorySet)) {
      this.setSourceSet(((SourceDirectorySet) source));
    } else {
      String _simpleName = SourceDirectorySet.class.getSimpleName();
      String _plus = ("source should be at least a " + _simpleName);
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
      throw _illegalArgumentException;
    }
  }
  
  public void setSourceSet(final SourceDirectorySet set) {
    super.setSource(set);
    this.sourceSet = set;
  }
}

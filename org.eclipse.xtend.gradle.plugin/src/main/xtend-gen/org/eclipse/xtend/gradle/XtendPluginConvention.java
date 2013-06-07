package org.eclipse.xtend.gradle;

import java.io.File;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.project.ProjectInternal;

@SuppressWarnings("all")
public class XtendPluginConvention {
  private final ProjectInternal project;
  
  private FileCollection _xtendSrcDirs;
  
  public FileCollection getXtendSrcDirs() {
    return this._xtendSrcDirs;
  }
  
  public void setXtendSrcDirs(final FileCollection xtendSrcDirs) {
    this._xtendSrcDirs = xtendSrcDirs;
  }
  
  private String encoding;
  
  private String xtendGenTargetDirName;
  
  private String xtendTempDirName;
  
  public XtendPluginConvention(final ProjectInternal it) {
    this.project = it;
    this.xtendGenTargetDirName = "generated-src/xtend-gen";
    this.xtendTempDirName = "xtend-temp";
  }
  
  public ProjectInternal getProject() {
    return this.project;
  }
  
  public String getEncoding() {
    return this.encoding;
  }
  
  public File getXtendGenTargetDir() {
    FileResolver _fileResolver = this.project.getFileResolver();
    File _buildDir = this.project.getBuildDir();
    FileResolver _withBaseDir = _fileResolver.withBaseDir(_buildDir);
    File _resolve = _withBaseDir.resolve(this.xtendGenTargetDirName);
    return _resolve;
  }
  
  public File getXtendTempDir() {
    FileResolver _fileResolver = this.project.getFileResolver();
    File _buildDir = this.project.getBuildDir();
    FileResolver _withBaseDir = _fileResolver.withBaseDir(_buildDir);
    File _resolve = _withBaseDir.resolve(this.xtendTempDirName);
    return _resolve;
  }
}

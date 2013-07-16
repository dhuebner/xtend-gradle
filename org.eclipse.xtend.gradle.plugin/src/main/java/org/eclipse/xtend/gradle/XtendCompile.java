/**
 * 
 */
package org.eclipse.xtend.gradle;

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
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.JavaCompile;

import com.google.inject.Injector;

/**
 * @author Dennis Huebner
 * 
 */

public class XtendCompile extends SourceTask {
	String encoding;
	@OutputDirectory
	File xtendGenTargetDir;
	File xtendTempDir;
	FileCollection classpath;
	Set<File> sourceSet;
	private JavaCompile javacTask;

	@TaskAction
	protected void compile() {
		BasicConfigurator.configure();
		Injector injector = XtendInjectorSingleton.INJECTOR;
		XtendBatchCompiler compiler = injector.getInstance(XtendBatchCompiler.class);

		String sourceDir = IterableExtensions.join(sourceSet, File.pathSeparator,
				new Function1<File, String>() {

					public String apply(File p) {
						return p.getAbsolutePath();
					}
				});

		getLogger().info("Source: " + sourceDir);
		getLogger().info("outputPath: " + xtendGenTargetDir.getAbsolutePath());
		getLogger().info("classPath: " + classpath.getAsPath());
		getLogger().info("Encoding: " + compiler.getFileEncoding());

		compiler.setSourcePath(sourceDir);
		compiler.setOutputPath(xtendGenTargetDir.getAbsolutePath());
		compiler.setClassPath(classpath.getAsPath());
		compiler.setTempDirectory(xtendTempDir.getAbsolutePath());
		
		if (encoding != null) {
			compiler.setFileEncoding(encoding);
		}
		if (!compiler.compile()) {
			throw new GradleException("Xtend compilation failed.");
		}
		getLogger().info("org.eclipse.xtend.gradle.XtendCompile.compile");
	}

	@SuppressWarnings("unchecked")
	public void setSource(Object source) {
		if (source instanceof Set) {
			setSourceSet((Set<File>) source);
		} else
			throw new IllegalArgumentException("source should be at least a "
					+ SourceDirectorySet.class.getSimpleName());
	}

	public void setSourceSet(Set<File> set) {
		super.setSource(set);
		this.sourceSet = set;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public File getXtendGenTargetDir() {
		return xtendGenTargetDir;
	}

	public void setXtendGenTargetDir(File xtendGenTargetDir) {
		this.xtendGenTargetDir = xtendGenTargetDir;
	}

	public File getXtendTempDir() {
		return xtendTempDir;
	}

	public void setXtendTempDir(File xtendTempDir) {
		this.xtendTempDir = xtendTempDir;
	}

	public FileCollection getClasspath() {
		return classpath;
	}

	public void setClasspath(FileCollection classpath) {
		this.classpath = classpath;
	}

	public void setJavaTask(JavaCompile javaCompile) {
		this.javacTask = javaCompile;
	}

}

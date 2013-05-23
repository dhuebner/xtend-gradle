package org.eclipse.xtend.gradle

import java.io.File
import org.apache.log4j.BasicConfigurator
import org.eclipse.xtend.core.XtendInjectorSingleton
import org.eclipse.xtend.core.compiler.batch.XtendBatchCompiler
import org.gradle.api.GradleException
import org.gradle.api.internal.ConventionTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory

class CompileXtendTask extends ConventionTask {
	@Property String encoding
	@Property @InputDirectory File xtendSrcDir
	@Property @OutputDirectory File xtendGenTargetDir
	@Property File xtendTempDir

	@TaskAction
	def protected void compile() {
		BasicConfigurator::configure
		var injector = XtendInjectorSingleton::INJECTOR
		var compiler = injector.getInstance(typeof(XtendBatchCompiler))
		var classpath = project.configurations.findByName(JavaPlugin::COMPILE_CONFIGURATION_NAME).asPath
		compiler.sourcePath = xtendSrcDir.absolutePath
		compiler.outputPath = xtendGenTargetDir.absolutePath
		compiler.classPath = classpath
		compiler.tempDirectory = xtendTempDir.absolutePath
		if (encoding != null) {
			compiler.fileEncoding = encoding
		}
		println('Encoding: ' + compiler.fileEncoding)
		if (!compiler.compile) {
			throw new GradleException('Xtend compilation failed.');
		}
		logger.info("Miau!")
	}
}

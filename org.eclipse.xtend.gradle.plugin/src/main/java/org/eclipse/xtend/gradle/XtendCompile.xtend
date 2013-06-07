package org.eclipse.xtend.gradle

import java.io.File
import org.apache.log4j.BasicConfigurator
import org.eclipse.xtend.core.XtendInjectorSingleton
import org.eclipse.xtend.core.compiler.batch.XtendBatchCompiler
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class XtendCompile extends SourceTask {
	@Property String encoding
	@Property @OutputDirectory File xtendGenTargetDir
	@Property File xtendTempDir
	@Property FileCollection classpath

	@TaskAction
	def protected void compile() {
		BasicConfigurator::configure
		var injector = XtendInjectorSingleton::INJECTOR
		val compiler = injector.getInstance(typeof(XtendBatchCompiler))
		val sourceDir = getSource as ConfigurableFileTree

		logger.info('Source: ' + sourceDir.dir.absolutePath)
		logger.info('outputPath: ' + xtendGenTargetDir.absolutePath)
		logger.info('classPath: ' + classpath.asPath)
		logger.info('Encoding: ' + compiler.fileEncoding)

		compiler.sourcePath = sourceDir.dir.absolutePath
		compiler.outputPath = xtendGenTargetDir.absolutePath
		compiler.classPath = classpath.asPath
		compiler.tempDirectory = xtendTempDir.absolutePath
		if (encoding != null) {
			compiler.fileEncoding = encoding
		}
		if (!compiler.compile) {
			throw new GradleException('Xtend compilation failed.');
		}
		logger.info("org.eclipse.xtend.gradle.XtendCompile.compile")
	}
}

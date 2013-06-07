package org.eclipse.xtend.gradle

import java.io.File
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.ProjectInternal

class XtendPluginConvention {

	val ProjectInternal project

	@Property FileCollection xtendSrcDirs

	String encoding

	String xtendGenTargetDirName

	String xtendTempDirName

	new(ProjectInternal it) {
		this.project = it
		xtendGenTargetDirName = 'generated-src/xtend-gen'
		xtendTempDirName = 'xtend-temp'
	}

	def ProjectInternal getProject() {
		return project
	}

	def String getEncoding() {
		encoding
	}

	def File getXtendGenTargetDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(xtendGenTargetDirName)
	}

	def File getXtendTempDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(xtendTempDirName)
	}
}

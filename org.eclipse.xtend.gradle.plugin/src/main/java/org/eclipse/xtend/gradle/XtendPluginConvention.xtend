package org.eclipse.xtend.gradle

import java.io.File
import org.gradle.api.internal.project.ProjectInternal

class XtendPluginConvention {

	val ProjectInternal project

	String encoding

	String xtendGenTargetDirName

	String xtendTempDirName

	new(ProjectInternal it) {
		this.project = it
		xtendGenTargetDirName = 'xtend-gen'
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

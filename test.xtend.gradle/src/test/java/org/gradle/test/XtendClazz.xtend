package org.gradle.test

class XtendClazz {
	Person person
	@Property
	String name
	
	String name2s
	
	new(String name) {
		this.name = name
	}
}
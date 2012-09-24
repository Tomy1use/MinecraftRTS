package com.amoebaman.mcrts.executors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {

	String name();
	String[] aliases();
	String description();
	String usage();
	String permission();
	String permissionMessage();

}
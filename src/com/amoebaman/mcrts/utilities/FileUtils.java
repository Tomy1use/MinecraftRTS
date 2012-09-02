package com.amoebaman.mcrts.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.amoebaman.mcrts.RTSPlugin;

public class FileUtils {
	
	public static void save(Object object, File file){
		try{
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
			stream.writeObject(object);
			stream.flush();
			stream.close();
		}
		catch(Exception e){
			RTSPlugin.logger().info("Error occurred while saving to file: " + file.getPath());
			e.printStackTrace();
		}
	}
	
	public static Object load(File file){
		try{
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			Object toReturn = stream.readObject();
			stream.close();
			return toReturn;
		}
		catch(Exception e){
			RTSPlugin.logger().info("Error occurred while saving to file: " + file.getPath());
			e.printStackTrace();
			return null;
		}
	}

}

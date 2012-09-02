package com.amoebaman.mcrts.utilities;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class ChatUtils {

	public static float SCREEN_WIDTH;
	public static float REG_CHAR_WIDTH;
	private static HashMap<Character, Float> IRREG_CHAR_WIDTH;
	
	static{
		SCREEN_WIDTH = 318;
		REG_CHAR_WIDTH = 6;
		IRREG_CHAR_WIDTH = new HashMap<Character, Float>();
		IRREG_CHAR_WIDTH.put(' ', 2.3F);
		IRREG_CHAR_WIDTH.put('i', 2F);
		IRREG_CHAR_WIDTH.put('I', 4F);
		IRREG_CHAR_WIDTH.put('k', 5F);
		IRREG_CHAR_WIDTH.put('l', 3F);
		IRREG_CHAR_WIDTH.put('t', 4F);
		IRREG_CHAR_WIDTH.put('!', 2F);
		IRREG_CHAR_WIDTH.put('(', 5F);
		IRREG_CHAR_WIDTH.put(')', 5F);
		IRREG_CHAR_WIDTH.put('~', 7F);
		IRREG_CHAR_WIDTH.put(',', 2F);
		IRREG_CHAR_WIDTH.put('.', 2F);
		IRREG_CHAR_WIDTH.put('<', 5F);
		IRREG_CHAR_WIDTH.put('>', 5F);
		IRREG_CHAR_WIDTH.put(':', 2F);
		IRREG_CHAR_WIDTH.put(';', 2F);
		IRREG_CHAR_WIDTH.put('"', 5F);
		IRREG_CHAR_WIDTH.put('{', 5F);
		IRREG_CHAR_WIDTH.put('}', 5F);
		IRREG_CHAR_WIDTH.put('|', 2F);
		IRREG_CHAR_WIDTH.put('`', 0F);
		IRREG_CHAR_WIDTH.put('\'', 2F);
		IRREG_CHAR_WIDTH.put(ChatColor.COLOR_CHAR, 0F);
	}
	
	public static float getCharWidth(char value){
		if(IRREG_CHAR_WIDTH.containsKey(value))
			return IRREG_CHAR_WIDTH.get(value);
		return REG_CHAR_WIDTH;
	}
	
	public static float getStringWidth(String str){
		float length = 0;
		for(int i = 0; i < str.length(); i++)
			if(i == 0)
				length += getCharWidth(str.charAt(i));
			else if(str.charAt(i - 1) != ChatColor.COLOR_CHAR)
				length += getCharWidth(str.charAt(i));
		return length;
	}
	
	public static String fillerLine(String pattern){
		float length = getStringWidth(pattern);
		int iterations = (int) (SCREEN_WIDTH / length);
		String line = "";
		for(int i = 0; i < iterations; i++)
			line += pattern;
		return centerAlign(line);
	}

	public static String centerAlign(String text){
		for(int i = 0; i < (SCREEN_WIDTH - getStringWidth(text)) / 2; i += getCharWidth(' '))
			text = " " + text;
		return text;
	}
}

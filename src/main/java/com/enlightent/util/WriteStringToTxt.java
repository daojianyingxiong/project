package com.enlightent.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class WriteStringToTxt {
	public static void WriteStringToFile(String filePath, String text) {
		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.append(text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try  
	    {      
	      // 创建文件对象  
	      File fileText = new File(filePath);  
	      // 向文件写入对象写入信息  
	      FileWriter fileWriter = new FileWriter(fileText);  
	  
	      // 写文件        
	      fileWriter.write(text);  
	      // 关闭  
	      fileWriter.close();  
	    }  
	    catch (IOException e)  
	    {  
	      //  
	      e.printStackTrace();  
	    }  
	}
}

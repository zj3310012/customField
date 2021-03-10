package com.zj.customField.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {

	public static void lineCopy(String origin, String dest, String packageName) {
		File inputFile = new File(origin);
		File outputFile = new File(dest); // src下面的output.txt
		try {
			FileReader inputFileReader = new FileReader(inputFile); // 打开要读取的文件

			FileWriter outFileWriter = new FileWriter(outputFile); // 打开要写入的文件

			BufferedReader breBufferedReader = new BufferedReader(inputFileReader); // 打开读取文件的数据量读取

			BufferedWriter bwBufferedWriter = new BufferedWriter(outFileWriter); // 打开要写入文件的数据量写入

			int line = 1; // 定义整型数字作为行号

			String tmpString; // 定义临时字符型变量来存取读到的每行数据

			while ((tmpString = breBufferedReader.readLine()) != null) { // 判断读取的每一行数据为空时,则终止读取
				if (line == 1) {
					bwBufferedWriter.write(packageName + "\n");
				} else {
					bwBufferedWriter.write(tmpString + '\n'); // 执行写入操作
				}
				line++; // 行号自加

			}

			bwBufferedWriter.flush(); // 刷新写入流

			breBufferedReader.close(); // 关闭文件数据流读取句柄

			bwBufferedWriter.close(); // 关闭文件数据流写入句柄

			inputFileReader.close(); // 关闭文件读取句柄

			outFileWriter.close(); // 关闭文件写入句柄
		} catch (Exception e) {

		}
	}
}

package com.zj.customField.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class HotJava {

	public static <T> Class<T> convert(Class<T> clazz, String tableName) throws Exception{
		EntityMysql entity = new EntityMysql();
		String tempPath = entity.create(tableName);
		System.out.print("原"+clazz.getSimpleName()+":");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.print(field.getName()+",");
		}
		System.out.println();
		// 创建自定义类加载器
		String swapPath = clazz.getResource("").getPath();
		String className = clazz.getName();
		Set set = new HashSet();
		set.add(className);
		MyClassLoader myClassLoader = new MyClassLoader(swapPath, set);
		// 替换java文件
		String javaPath = System.getProperty("user.dir") + "\\src\\main\\java\\"
				+ clazz.getName().replace(".", "\\") + ".java";
		String packageName = "package "+className.substring(0,className.lastIndexOf("."))+";";
		FileUtil.lineCopy(tempPath, javaPath, packageName);
		// 动态编译
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		int status = javac.run(null, null, null, "-d", System.getProperty("user.dir") + "\\target\\classes", javaPath);
		//int status = javac.run(null, null, null,javaPath);
		if (status != 0) {
			System.out.println("没有编译成功！");
		}
		// 动态执行热替换
		Class clz = myClassLoader.loadClass(className);
		return clz;
	}
}

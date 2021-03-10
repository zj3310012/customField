package com.zj.customField.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @author zj
 * @desc 代码热替换，在不重启服务器的情况下可以修改类的代码并使之生效
 **/
public class MyClassLoader extends ClassLoader{

	// 定义.class路径
    private String swapPath;
    // 存储哪些类需要我自身去加载
    private Set<String> myselfLoader;

    public MyClassLoader(String swapPath, Set<String> myselfLoader) {
        this.swapPath = swapPath;
        this.myselfLoader = myselfLoader;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        // 需要我自己去加载
        if (loadedClass==null && myselfLoader.contains(name)) {
            loadedClass = findClass(name);
            if (loadedClass!=null){
                return loadedClass;
            }
        }
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 根据文件系统路径加载class文件，并返回byte数组
        byte[] b = getClassByte(name);
        // 调用ClassLoader提供的方法，将二进制数组转换成Class类的实例
        return defineClass(name,b,0,b.length);
    }

    private byte[] getClassByte(String name) {
        String className = name.substring(name.lastIndexOf(".")+1,
        				name.length()) + ".class";
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream(swapPath + className);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length=fis.read(buff))>0){
                baos.write(buff,0,length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return new byte[]{};
    }

}

package com.zj.customField.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityMysql {

	public String create(String tableName) {
		Connection con = null;
		try {
			String sql = "select * from " + tableName;
			con = ConnectionUtil.openConnection();
			PreparedStatement pStemt = con.prepareStatement(sql);
			ResultSetMetaData rsmd = pStemt.getMetaData();
			// 统计列
			int size = rsmd.getColumnCount();
			// 列名数组
			String[] colNames = new String[size];
			// 列名类型数组
			String[] colTypes = new String[size];
			// 返回字段注释
			List<String> colComments = new ArrayList<>(size);// 列名注释集合
			ResultSet rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				colComments.add(rs.getString("Comment"));
			}
			// 列名大小数组
			int[] colSizes = new int[size];
			// 是否需要导入包java.util.*
			boolean f_util = false;
			// 是否需要导入包java.sql.*
			boolean f_sql = false;
			for (int i = 0; i < size; i++) {
				colNames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);
				if (colTypes[i].equalsIgnoreCase("datetime") || colTypes[i].equalsIgnoreCase("date")
						|| colTypes[i].equalsIgnoreCase("timestamp")) {
					f_util = true;
				}
				if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")) {
					f_sql = true;
				}
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
			}
			String content = parse(colNames, colTypes, colSizes, tableName, colComments.toArray(new String[size]),
					f_util, f_sql);
			String path = File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator
					+ ConnectionUtil.prop.getProperty("entity.mysql.path").replace(".", File.separator) + File.separator;
			String filePath = saveFile(tableName+"DO", content, path, "java");
			return filePath;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 可以调用private方法原因是同在一个GenEntityMysql
	private String parse(String[] colnames, String[] colTypes, int[] colSizes, String tableName, String[] colComments,
			boolean f_util, boolean f_sql) {
		StringBuffer sb = new StringBuffer();

		sb.append("package " + ConnectionUtil.prop.getProperty("entity.mysql.path") + ";\r\n");
		sb.append("\r\n");

		// 判断是否导入工具包
		if (f_util) {
			sb.append("import java.util.Date;\r\n");
		}
		if (f_sql) {
			sb.append("import java.sql.*;\r\n");
		}
		sb.append("import java.io.Serializable;\r\n");
		sb.append("import com.baomidou.mybatisplus.annotation.IdType;\r\n");
		sb.append("import com.baomidou.mybatisplus.annotation.TableId;\r\n");
		sb.append("import com.baomidou.mybatisplus.annotation.TableName;\r\n");
		sb.append("import lombok.Data;\r\n");
		sb.append("import lombok.EqualsAndHashCode;\r\n");
		sb.append("import lombok.experimental.Accessors;\r\n");
		sb.append("\r\n");
		// 注释部分
		String className = initelsUppercap(tableName)+"DO";
		String typeName = "实体类";
		sb.append(appendNotes(className,typeName));
		// lombok
		sb.append("@Data\r\n"
				+ "@EqualsAndHashCode(callSuper = false)\r\n"
				+ "@Accessors(chain = true)\r\n"
				+ "@TableName(value = \""+tableName+"\")\r\n");
		// 实体部分
		sb.append("public class " + initelsUppercap(tableName)+ "DO implements Serializable " + "{\r\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\r\n");
		processAllAttrs(sb, colnames, colTypes, colComments);// 属性
		sb.append("}\r\n");

		// System.out.println(sb.toString());
		return sb.toString();

	}
	
	private void processAllAttrs(StringBuffer sb, String[] colNames, String[] colTypes, String[] colComments) {

		for (int i = 0; i < colNames.length; i++) {
			sb.append("\t/**");
			sb.append(colComments[i]);
			sb.append("*/ \r\n");
			if(colNames[i].equals("id")) {
				sb.append("\t@TableId(value = \"id\", type = IdType.AUTO)\r\n");
			}
			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + initLowcap(initelsUppercap(colNames[i]))
					+ ";\r\n");
		}

	}
	
	protected StringBuffer appendNotes(String className, String typeName) {
		StringBuffer sb = new StringBuffer();
		// 注释部分
		sb.append("/**\r\n");
		sb.append("* " + className + " " + typeName +"\r\n");
		sb.append("* " + new Date() + " " + ConnectionUtil.prop.getProperty("authorname.mysql.path") + "\r\n");
		sb.append("*/ \r\n");
		return sb;
	}
	
	protected String saveFile(String tableName, String content, String path, String suffix) {

		try {
			File directory = new File("");
			String dirPath = directory.getAbsolutePath() + path;
			String filePath = directory.getAbsolutePath() + path + initelsUppercap(tableName) + "."+suffix;
			System.out.println(String.format("输出路径-%s", filePath));
			File entityFile = new File(filePath);
			if (!entityFile.exists()) {
				File dir = new File(dirPath);
				if (!dir.isDirectory()) {
					dir.mkdir();
				}
				entityFile.createNewFile();
		
			}
			FileWriter fw = new FileWriter(filePath);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			pw.close();
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("bit")) {
			return "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "int";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney") || sqlType.equalsIgnoreCase("double")) {
			return "double";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
				|| sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")
				|| sqlType.equalsIgnoreCase("timestamp")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		}

		return null;
	}
	
	
	// split("_")，将分割元素首字母转为大写
	protected String initelsUppercap(String str) {
		String[] els = str.split("_");
		StringBuffer buffer = new StringBuffer();
		for (String el : els) {
			char[] ch = el.toCharArray();
			if (ch[0] >= 'a' && ch[0] <= 'z') {
				ch[0] = (char) (ch[0] - 32);
			}
			buffer.append(new String(ch));
		}
		return buffer.toString();
	}
	
	// 将首字母转为小写
	protected String initLowcap(String str) {

		char[] ch = str.toCharArray();
		if (ch[0] >= 'A' && ch[0] <= 'Z') {
			ch[0] = (char) (ch[0] + 32);
		}

		return new String(ch);
	}
}

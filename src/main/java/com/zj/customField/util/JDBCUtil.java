package com.zj.customField.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {

	// 新增列
	public static void insert(String tableName, String alterTableSql) {
		Connection conn = null;
		try {
			conn = ConnectionUtil.openConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(alterTableSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	// 删除列
	public static void delete(String tableName, String dropColumnTableSql) {
		Connection conn = null;
		try {
			conn = ConnectionUtil.openConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(dropColumnTableSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

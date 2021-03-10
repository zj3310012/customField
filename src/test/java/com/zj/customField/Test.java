package com.zj.customField;

import java.lang.reflect.Field;

import com.zj.customField.entity.MesSharedKeyDO;
import com.zj.customField.entity.MysqlModel;
import com.zj.customField.util.HotJava;
import com.zj.customField.util.JDBCUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		MysqlModel model = new MysqlModel();
		model.setField("add_test");
		model.setType("varchar");
		model.setLength("30");
		model.setComment("测试");
		String tableName = "mes_shared_key";
		// 修改表结构,新增字段
		String alterTableSql="alter TABLE "+tableName+" add "+model.getField()+
		" "+model.getType()+"("+model.getLength()+") comment\""
				+model.getComment()+"\";";
		JDBCUtil.insert(tableName, alterTableSql);
	//	String dropColumnTableSql = "alter TABLE "+tableName+" drop column "
	//	+model.getField()+";";
	//	JDBCUtil.delete(tableName, dropColumnTableSql);

		Class<MesSharedKeyDO> clz = HotJava.convert(MesSharedKeyDO.class, tableName);
		Field[] fields = clz.getDeclaredFields();
		System.out.print("现"+clz.getSimpleName()+":");
		for (Field field : fields) {
			System.out.print(field.getName() + ",");
		}
	}
}

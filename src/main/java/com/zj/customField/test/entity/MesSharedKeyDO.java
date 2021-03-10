package com.zj.customField.test.entity;

import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* MesSharedKeyDO 实体类
* Wed Mar 10 11:00:11 CST 2021 zhangjing
*/ 
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mes_shared_key")
public class MesSharedKeyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	/***/ 
	@TableId(value = "id", type = IdType.AUTO)
	private int id;
	/***/ 
	private String appKey;
	/***/ 
	private String sharedKey;
	/***/ 
	private Date expiredTime;
	/**测试*/ 
	private String addTest;
}


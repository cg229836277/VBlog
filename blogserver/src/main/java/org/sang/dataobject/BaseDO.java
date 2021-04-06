package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {
    @TableId(type = IdType.AUTO)//自增
    public long id;

    @TableField(value = "create_time")
    public Date createTime;
}

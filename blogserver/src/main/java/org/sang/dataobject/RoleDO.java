package org.sang.dataobject;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "roles")
public class RoleDO extends BaseDO {

    @TableField(exist = false)
    public static final String ROLE_ADMIN = "ADMIN";
    @TableField(exist = false)
    public static final String ROLE_NORMAL = "NORMAL";
    @TableField(exist = false)
    public static final String ROLE_TEST = "TEST";

    String name;
}

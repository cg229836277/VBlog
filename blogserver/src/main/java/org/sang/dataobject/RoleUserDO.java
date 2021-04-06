package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "roles_users")
public class RoleUserDO extends BaseDO {

    @TableField(value = "rid")
    public long roleId;

    @TableField(value = "uid")
    public long userId;
}

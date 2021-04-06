package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "users")
public class UserDO extends BaseDO {

    public String username;

    public String password;

    public String nickname;

    public boolean enable;

    @TableField(exist = false)
    public String roleName;

    @TableField(exist = false)
    public String token;
}

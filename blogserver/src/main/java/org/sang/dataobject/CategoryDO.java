package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "categories")
public class CategoryDO extends BaseDO {

    @TableField(value = "parent_name")
    String parentName;

    @TableField(value = "child_name")
    String childName;
}

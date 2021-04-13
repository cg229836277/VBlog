package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName(value = "categories")
public class CategoryDO extends BaseDO {

    @TableField(value = "parent_name")
    @NotBlank
    String parentName;

    @TableField(value = "child_name")
    @NotBlank
    String childName;

    @TableField(exist = false)
    @JsonDeserialize
    @JsonSerialize
    int[] ids;
}

package org.sang.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "music_list")
public class MusicListDO extends BaseDO {
    public String content;

    public String title;
}

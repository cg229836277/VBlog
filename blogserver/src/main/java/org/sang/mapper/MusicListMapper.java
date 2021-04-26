package org.sang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sang.dataobject.MusicListDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicListMapper extends BaseMapper<MusicListDO> {

    default List<MusicListDO> getAllMusicList() {
        return selectList(new QueryWrapper<MusicListDO>());
    }


    default boolean deleteMusicById(long id) {
        int result = deleteById(id);
        return result > 0;
    }


    default boolean insertMusic(MusicListDO musicListDO) {
        insert(musicListDO);
        return musicListDO.getId() >= 0;
    }
}

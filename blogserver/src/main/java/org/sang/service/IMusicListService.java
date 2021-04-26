package org.sang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sang.dataobject.MusicListDO;

import java.util.List;

public interface IMusicListService extends IService<MusicListDO> {
    List<MusicListDO> getAllMusicList();

    boolean deleteMusic(long id);

    boolean insert(MusicListDO musicListDO);
}
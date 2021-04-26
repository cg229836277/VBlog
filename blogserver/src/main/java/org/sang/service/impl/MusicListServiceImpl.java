package org.sang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sang.dataobject.MusicListDO;
import org.sang.mapper.MusicListMapper;
import org.sang.service.IMusicListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicListServiceImpl extends ServiceImpl<MusicListMapper, MusicListDO> implements IMusicListService {
    @Autowired
    private MusicListMapper musicListMapper;

    @Override
    public List<MusicListDO> getAllMusicList() {
        return musicListMapper.getAllMusicList();
    }

    @Override
    public boolean deleteMusic(long id) {
        return musicListMapper.deleteMusicById(id);
    }

    @Override
    public boolean insert(MusicListDO musicListDO) {
        return musicListMapper.insertMusic(musicListDO);
    }
}

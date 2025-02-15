package org.sang.controller;

import org.sang.dataobject.MusicListDO;
import org.sang.exception.CRUDResultEnum;
import org.sang.ratelimit.Limit;
import org.sang.ratelimit.LimitType;
import org.sang.service.IMusicListService;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.sang.ratelimit.LimitParas.DEFAULT_COUNT_LIMIT;
import static org.sang.ratelimit.LimitParas.DEFAULT_TIME_LIMIT;

@RestController
@RequestMapping(value = "/music")
public class MusicListController {
    @Autowired
    IMusicListService musicListService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @Limit(key = "music_all", period = DEFAULT_TIME_LIMIT, count = DEFAULT_COUNT_LIMIT, limitType = LimitType.IP)
    CommonResult<List<MusicListDO>> getAllMusicList() {
        List<MusicListDO> dataList = musicListService.getAllMusicList();
        return CommonResult.success(dataList);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    CommonResult deleteMusic(@RequestBody final MusicListDO musicListDO) {
        boolean result = musicListService.deleteMusic(musicListDO.getId());
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.DELETE_MUSIC_LIST_FAIL);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    CommonResult insert(@RequestBody final MusicListDO musicListDO) {
        boolean result = musicListService.insert(musicListDO);
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.ADD_MUSIC_LIST_FAIL);
    }
}

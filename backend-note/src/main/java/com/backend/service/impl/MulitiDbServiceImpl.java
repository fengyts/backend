package com.backend.service.impl;

import com.backend.config.mulitidb.DbSource;
import com.backend.config.mulitidb.GloableDatasourceEnum;
import com.backend.mybatis.entity.MulitiDb;
import com.backend.mybatis.mapper.MulitiMapper;
import com.backend.service.IMulitiDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MulitiDbServiceImpl extends ServiceImpl<MulitiMapper, MulitiDb> implements IMulitiDbService {

    @Override
    @DbSource(GloableDatasourceEnum.READ)
    public MulitiDb testRead(Long id) {
        MulitiDb mulitiDb = baseMapper.selectById(id);
        return mulitiDb;
    }

    @Override
    @DbSource(GloableDatasourceEnum.WRITE)
    public MulitiDb testWrite(Long id) {
        MulitiDb mulitiDb = baseMapper.selectById(id);
        return mulitiDb;
    }

    @Override
    public MulitiDb testOther(Long id) {
        MulitiDb mulitiDb = baseMapper.selectById(id);
        return mulitiDb;
    }


}

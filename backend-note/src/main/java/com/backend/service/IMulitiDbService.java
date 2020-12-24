package com.backend.service;

import com.backend.mybatis.entity.MulitiDb;

public interface IMulitiDbService {

    MulitiDb testRead(Long id);

    MulitiDb testWrite(Long id);

    MulitiDb testOther(Long id);

}

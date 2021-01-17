package com.backend.service;

import java.util.Map;

public interface IGeneraterService {

    Map<String, Object> initConfig();

    void generate();

    void clearCacheConfig();

}

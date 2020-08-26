package com.backend.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class WebPageQuery<T> extends Page<T> {

    protected Integer page;
    protected Integer rows;
    protected T data;

}

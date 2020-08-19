package com.backend.common;

import lombok.Data;

@Data
public class WebPageQuery<T> {

    protected Integer page;
    protected Integer rows;
    protected T data;

}

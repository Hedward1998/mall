package com.bs.mall.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface LastIDMapper {
    int selectLastID();
}

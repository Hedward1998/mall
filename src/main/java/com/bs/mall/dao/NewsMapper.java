package com.bs.mall.dao;

import com.bs.mall.entity.Address;
import com.bs.mall.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper {
    Integer insertOne(@Param("news") News news);
//    Integer updateOne(@Param("address") Address address);
//
//    List<Address> select(@Param("address_name") String address_name, @Param("address_regionId") String address_regionId);
//    Address selectOne(@Param("address_areaId") String address_areaId);
//    List<Address> selectRoot();
}
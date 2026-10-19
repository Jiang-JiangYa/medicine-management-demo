package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediDrugStoreInfo;

//药品储存信息表 - Mapper层
@Mapper
public interface MediDrugStoreInfoMapper extends BaseMapper<MediDrugStoreInfo> {
}

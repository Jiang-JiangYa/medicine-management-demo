package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediStockInfo;

//药库库存表---mapper
@Mapper
public interface MediStockInfoMapper extends BaseMapper<MediStockInfo> {
}

package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediStockBatchInfo;

//药库分批库存表---mapper
@Mapper
public interface MediStockBatchInfoMapper extends BaseMapper<MediStockBatchInfo> {
}

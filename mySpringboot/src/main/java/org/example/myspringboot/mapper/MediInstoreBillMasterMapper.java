package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediInstoreBillMaster;
//药品入库主表 - Mapper层
@Mapper
public interface MediInstoreBillMasterMapper  extends BaseMapper<MediInstoreBillMaster> {
}

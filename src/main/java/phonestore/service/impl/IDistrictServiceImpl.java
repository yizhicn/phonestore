package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.District;
import phonestore.mapper.DistrictMapper;
import phonestore.service.IDistrictService;

import java.util.List;


@Service
public class IDistrictServiceImpl implements IDistrictService {
    @Autowired
    private DistrictMapper districtMapper;

    /**
     * 根据父代号查询省市区信息
     *  parent 父代号
     **/
    @Override
    public List<District> getSpecifyDistrictByParent(String parent) {
        List<District> districts = districtMapper.queryDistrictByParent(parent);

        //过滤无效字段数据，提高传输效率
        for (District ad: districts) {
            ad.setId(null);
            ad.setParent(null);
        }

        //返回数据
        return districts;
    }

    /**
     * 根据code查询省市区名字
     *  code 省市区的code
     **/
    @Override
    public String getNameByCode(String code) {
        return districtMapper.queryDistrictByCode(code);
    }
}

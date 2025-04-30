package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phonestore.entity.District;
import phonestore.utils.JsonResult;
import phonestore.service.IDistrictService;

import java.util.List;


@RestController
@RequestMapping("/district")
public class DistrictController extends BaseController{
    @Autowired
    private IDistrictService districtService;


    /**
     * 处理父代号查询省市区的请求
     *  parent 父代号
     **/
    @GetMapping("/parent")
    public JsonResult<List<District>> queryDistrictByParent(String parent){
        //查询数据
        List<District> list = districtService.getSpecifyDistrictByParent(parent);
        //返回数据
        return new JsonResult<>(OK,list);
    }

    /**
     * 处理查询省市区名称的请求
     *  code 省市区的code
     **/
    @GetMapping
    public String queryDistrictNameByCode(String code){
        return districtService.getNameByCode(code);
    }
}

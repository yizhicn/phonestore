package phonestore.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phonestore.entity.Product;
import phonestore.utils.JsonResult;
import phonestore.service.IProductService;

import java.util.List;


@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {
    @Autowired
    private IProductService productService;

    /**
     * 处理热销商品的请求
     **/
    @GetMapping("/hotProduct")
    public JsonResult<List<Product>> queryBestProduct() {
        //查询对应商品
        List<Product> products = productService.queryPriorityProduct();

        return new JsonResult<>(OK, products);
    }

    /**
     * 处理展示最新商品的请求
     **/
    @GetMapping("/newProduct")
    public JsonResult<List<Product>> queryNewProduct() {
        //查询对应商品
        List<Product> products = productService.queryTheNewProduct();

        return new JsonResult<>(OK, products);
    }

    /**
     * 处理商品id查询该商品信息的请求
     * id 商品id
     **/
    @GetMapping("/{id}")
    public JsonResult<Product> queryProductById(@PathVariable(value = "id", required = false) Integer id) {
        Product product = productService.queryProductById(id);
        return new JsonResult<>(OK, product);
    }

    /**
     * 处理根据产品关键字进行模糊查询的请求
     * pageNum 当前页
     * pageSize 每页显示数
     * title 查询的关键字
     **/
    @GetMapping("/{pageNum}/{pageSize}/{title}")
    public JsonResult<PageInfo<Product>> quertByTitle(@PathVariable("pageNum") Integer pageNum,
                                                      @PathVariable("pageSize") Integer pageSize,
                                                      @PathVariable("title") String title) {
        PageInfo<Product> lists = productService.queryProductByTitle(pageNum, pageSize, title);
        return new JsonResult<>(OK, lists);
    }

    @GetMapping("/category/{pageNum}/{pageSize}/{categoryId}")
    public JsonResult<PageInfo<Product>> queryByCategory(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("categoryId") Integer categoryId) {
        PageInfo<Product> lists = productService.queryProductByCategory(pageNum, pageSize, categoryId);
        return new JsonResult<>(OK, lists);
    }

    /**
     * 处理展示全部商品的请求
     **/
    @GetMapping("/allProduct")
    public JsonResult<List<Product>> queryAllProduct() {
        //查询对应商品
        List<Product> products = productService.queryAllProduct();

        return new JsonResult<>(OK, products);
    }

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public JsonResult<Void> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return new JsonResult<>(OK);
    }

    /**
     * 更新商品信息
     */
    @PostMapping("/update")
    public JsonResult<Void> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return new JsonResult<>(OK);
    }

    /**
     * 删除商品
     */
    @PostMapping("/delete")
    public JsonResult<Void> deleteProduct(@RequestParam("id") Integer id) {
        productService.deleteProduct(id);
        return new JsonResult<>(OK);
    }
}

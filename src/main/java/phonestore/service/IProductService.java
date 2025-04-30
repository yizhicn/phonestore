package phonestore.service;

import com.github.pagehelper.PageInfo;
import phonestore.entity.Product;

import java.util.List;


public interface IProductService {

    //查询热销商品的前五项的抽象方法
    List<Product> queryPriorityProduct();

    //查询最新商品的前五项的抽象方法
    List<Product> queryTheNewProduct();

    //查询指定id商品的抽象方法
    Product queryProductById(Integer id);

    //根据名称进行模糊查询的抽象方法
    PageInfo<Product>  queryProductByTitle(Integer pageNum, Integer pageSize,String title);

    //根据分类id查对应商品
    PageInfo<Product> queryProductByCategory(Integer pageNum, Integer pageSize, Integer categoryId);

    //查询所有商品
    List<Product> queryAllProduct();

    // 新增商品
    void addProduct(Product product);

    // 更新商品
    void updateProduct(Product product);

    // 删除商品
    void deleteProduct(Integer id);
}

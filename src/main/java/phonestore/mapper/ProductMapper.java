package phonestore.mapper;

import phonestore.entity.Product;

import java.util.List;


public interface ProductMapper {

    /**
     * 查询优先权前五的商品进行展示
     **/
    List<Product> queryPriorityProduct();

    /**
     * 查询最新上架的商品进行展示
     **/
    List<Product> queryTheNewProduct();

    /**
     * 根据指定商品id进行商品查询
     *  id 商品id
     **/
    Product queryProductById(Integer id);

    /**
     * 根据指定的名称关键字进行模糊查询
     *  title 要查询的商品名称关键字
     **/
    List<Product> queryProductByTitle(String title);

//    根据分类id查对应商品
    List<Product> queryProductByCategory(Integer categoryId);


//    查询所有商品
    List<Product> queryAllProduct();

    /**
     * 插入商品
     */
    int insertProduct(Product product);

    /**
     * 更新商品
     */
    int updateProduct(Product product);

    /**
     * 删除商品
     */
    int deleteProduct(Integer id);
}

package phonestore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Product;
import phonestore.service.exception.ProductBadStatusException;
import phonestore.service.exception.ProductNotExistsException;
import phonestore.mapper.ProductMapper;
import phonestore.service.IProductService;

import java.util.Date;
import java.util.List;


@Service
public class IProductServiceImpl implements IProductService {
    @Autowired(required = false)
    private ProductMapper productMapper;

    /**
     * 处理查询热销商品的具体逻辑
     * @return java.util.List<phonestore.entity.Product>
     **/
    @Override
    public List<Product> queryPriorityProduct() {
        return productMapper.queryPriorityProduct();
    }

    /**
     * 处理查询最新商品的具体逻辑
     * @return java.util.List<phonestore.entity.Product>
     **/
    @Override
    public List<Product> queryTheNewProduct() {
        return productMapper.queryTheNewProduct();
    }

    /**
     * 根据商品id查询商品的具体逻辑
     *  id 商品id
     * @return phonestore.entity.Product
     **/
    @Override
    public Product queryProductById(Integer id) {
        Product product = productMapper.queryProductById(id);

        if (product == null){
            throw new ProductNotExistsException("无此商品信息，查询失败");
        }

        if (product.getStatus() == 2){
            throw new ProductBadStatusException("商品已下架");
        }

        if (product.getStatus() == 3){
            throw new ProductBadStatusException("商品已删除");
        }
        //无任何异常则返回数据
        return product;
    }

    /**
     * 根据名称进行模糊查询的具体逻辑

     *  title 查询的关键字
     * @return com.github.pagehelper.PageInfo<phonestore.entity.Product>
     **/
    @Override
    public PageInfo<Product> queryProductByTitle(Integer pageNum, Integer pageSize,String title) {
        //开启分页功能
        PageHelper.startPage(pageNum,pageSize);
        //调用持久层方法进行查询
        List<Product> products = productMapper.queryProductByTitle(title);
        //返回分页数据
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return pageInfo;
    }

//    根据分类查询商品
    @Override
    public PageInfo<Product> queryProductByCategory(Integer pageNum, Integer pageSize, Integer categoryId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.queryProductByCategory(categoryId);
        return new PageInfo<>(products);
    }

//    查询所有商品
    @Override
    public List<Product> queryAllProduct() {return productMapper.queryAllProduct();}


    @Override
    public void addProduct(Product product) {
        // 设置默认值
        if (product.getStatus() == null) {
            product.setStatus(1); // 默认上架
        }
        if (product.getPriority() == null) {
            product.setPriority(0); // 默认优先级
        }
        product.setCreatedTime(new Date());
        product.setModifiedTime(new Date());
        product.setCreatedUser("admin"); // 为管理员
        product.setModifiedUser("admin");

        // 调用持久层插入
        int rows = productMapper.insertProduct(product);
        if (rows != 1) {
            throw new RuntimeException("新增商品失败");
        }
    }

    @Override
    public void updateProduct(Product product) {
        // 验证商品是否存在
        Product existingProduct = productMapper.queryProductById(product.getId());
        if (existingProduct == null) {
            throw new ProductNotExistsException("商品不存在");
        }

        // 更新修改时间和修改人
        product.setModifiedTime(new Date());
        product.setModifiedUser("admin");

        // 调用持久层更新
        int rows = productMapper.updateProduct(product);
        if (rows != 1) {
            throw new RuntimeException("更新商品失败");
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        // 验证商品是否存在
        Product existingProduct = productMapper.queryProductById(id);
        if (existingProduct == null) {
            throw new ProductNotExistsException("商品不存在");
        }

        // 调用持久层删除
        int rows = productMapper.deleteProduct(id);
        if (rows != 1) {
            throw new RuntimeException("删除商品失败");
        }
    }
}

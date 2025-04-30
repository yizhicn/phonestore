package phonestore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Favorites;
import phonestore.entity.Product;
import phonestore.mapper.FavoritesMapper;
import phonestore.service.IFavoritesService;
import phonestore.service.exception.InsertException;
import phonestore.service.exception.UpdateException;
import phonestore.service.IProductService;

import java.util.List;


@Service
public class IFavoritesServiceImpl implements IFavoritesService {
    @Autowired
    private FavoritesMapper favoritesMapper;
    @Autowired
    private IProductService productService;

    /**
     * Description :
     *  favorites 实体类对象
     **/
    /**
     * 新增收藏商品的具体逻辑
     *  uid 用户uid
     *  pid 商品pid
     **/
    @Override
    public int addFavorites(Integer uid,Integer pid) {
        Favorites favorites = new Favorites();

        //根据pid查询商品信息
        Product product = productService.queryProductById(pid);

        //填充favorites对象空白字段
        favorites.setUid(uid);
        favorites.setPid(pid);
        favorites.setImage(product.getImage());
        favorites.setPrice(product.getPrice());
        favorites.setTitle(product.getTitle());
        favorites.setSellPoint(product.getSellPoint());
        favorites.setStatus(1);

        int result = favoritesMapper.addFavorites(favorites);
        if (result == 0){
            throw new InsertException("服务器异常，收藏商品失败");
        }

        //取出fid返回给前端页面，以便在搜索界面取消收藏使用
        return favorites.getFid();
    }

    /**
     * 根据uid和商品收藏状态查询收藏数据的具体逻辑

     *  uid 用户uid
     *  status 查询商品状态
     * @return java.util.List<phonestore.entity.Favorites>
     **/
    @Override
    public PageInfo<Favorites> queryFavorites(Integer uid, Integer pageNum,Integer pageSize,Integer status) {
        //开启分页功能，pageNum是当前页，pageSize是每页显示的数据量，这两个值都可以选择让前端传或者自己调整
        PageHelper.startPage(pageNum,pageSize);
        List<Favorites> favorites = favoritesMapper.queryFavoritesByUidAndStatus(uid, status);
        PageInfo<Favorites> pageInfo = new PageInfo<>(favorites);
        return pageInfo;
    }

    /**
     * 根据收藏商品pid和用户uid取消对应商品收藏的具体逻辑

     *  status 取消收藏的状态
     *  fid 收藏的fid
     *  uid 用户uid
     * @return int
     **/
    @Override
    public int updateFavoritesStatus(Integer status, Integer fid, Integer uid) {
        int result = favoritesMapper.updateFavoritesStatus(status, fid, uid);

        if (result == 0){
            throw new UpdateException("服务器异常，取消收藏失败");
        }
        return result;
    }
}

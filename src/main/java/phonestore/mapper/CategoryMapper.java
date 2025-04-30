package phonestore.mapper;

import phonestore.entity.Category;

import java.util.List;

public interface CategoryMapper {
    /**
     * 查询所有分类
     */
    List<Category> queryAllCategories();

    /**
     * 查询状态为正常的分类
     */
    List<Category> queryActiveCategories();

    /**
     * 根据ID查询分类
     */
    Category queryCategoryById(Integer id);

    /**
     * 插入分类
     */
    int insertCategory(Category category);

    /**
     * 更新分类
     */
    int updateCategory(Category category);

    /**
     * 删除分类
     */
    int deleteCategory(Integer id);
}
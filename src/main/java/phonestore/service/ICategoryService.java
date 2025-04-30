package phonestore.service;

import phonestore.entity.Category;

import java.util.List;

public interface ICategoryService {
    /**
     * 查询所有分类
     */
    List<Category> queryAllCategories();

    /**
     * 查询状态为正常的分类
     */
    List<Category> queryActiveCategories();

    /**
     * 新增分类
     */
    void addCategory(Category category);

    /**
     * 更新分类
     */
    void updateCategory(Category category);

    /**
     * 删除分类
     */
    void deleteCategory(Integer id);
}
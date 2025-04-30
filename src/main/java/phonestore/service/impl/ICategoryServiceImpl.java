package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Category;
import phonestore.service.exception.CategoryNotExistsException;
import phonestore.mapper.CategoryMapper;
import phonestore.service.ICategoryService;

import java.util.Date;
import java.util.List;

@Service
public class ICategoryServiceImpl implements ICategoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryAllCategories() {
        return categoryMapper.queryAllCategories();
    }

    @Override
    public List<Category> queryActiveCategories() {
        return categoryMapper.queryActiveCategories();
    }

    @Override
    public void addCategory(Category category) {
        // 设置默认值
        if (category.getStatus() == null) {
            category.setStatus(1); // 默认正常
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0); // 默认排序
        }
        if (category.getIsParent() == null) {
            category.setIsParent(0); // 默认非父分类
        }
        category.setCreatedTime(new Date());
        category.setModifiedTime(new Date());
        category.setCreatedUser("admin");
        category.setModifiedUser("admin");

        // 调用持久层插入
        int rows = categoryMapper.insertCategory(category);
        if (rows != 1) {
            throw new RuntimeException("新增分类失败");
        }
    }

    @Override
    public void updateCategory(Category category) {
        // 验证分类是否存在
        Category existingCategory = categoryMapper.queryCategoryById(category.getId());
        if (existingCategory == null) {
            throw new CategoryNotExistsException("分类不存在");
        }

        // 更新修改时间和修改人
        category.setModifiedTime(new Date());
        category.setModifiedUser("admin");

        // 调用持久层更新
        int rows = categoryMapper.updateCategory(category);
        if (rows != 1) {
            throw new RuntimeException("更新分类失败");
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        // 验证分类是否存在
        Category existingCategory = categoryMapper.queryCategoryById(id);
        if (existingCategory == null) {
            throw new CategoryNotExistsException("分类不存在");
        }

        // 调用持久层删除
        int rows = categoryMapper.deleteCategory(id);
        if (rows != 1) {
            throw new RuntimeException("删除分类失败");
        }
    }
}
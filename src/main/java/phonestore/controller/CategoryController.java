package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phonestore.entity.Category;
import phonestore.utils.JsonResult;
import phonestore.service.ICategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Autowired
    private ICategoryService categoryService;

    /**
     * 查询所有分类
     */
    @GetMapping("/allCategories")
    public JsonResult<List<Category>> queryAllCategories() {
        List<Category> categories = categoryService.queryAllCategories();
        return new JsonResult<>(OK, categories);
    }

    /**
     * 查询状态为正常的分类
     */
    @GetMapping("/list")
    public JsonResult<List<Category>> queryActiveCategories() {
        List<Category> categories = categoryService.queryActiveCategories();
        return new JsonResult<>(OK, categories);
    }

    /**
     * 新增分类
     */
    @PostMapping("/add")
    public JsonResult<Void> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return new JsonResult<>(OK);
    }

    /**
     * 更新分类信息
     */
    @PostMapping("/update")
    public JsonResult<Void> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return new JsonResult<>(OK);
    }

    /**
     * 删除分类
     */
    @PostMapping("/delete")
    public JsonResult<Void> deleteCategory(@RequestParam("id") Integer id) {
        categoryService.deleteCategory(id);
        return new JsonResult<>(OK);
    }
}
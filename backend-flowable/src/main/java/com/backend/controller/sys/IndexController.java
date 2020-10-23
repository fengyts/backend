package com.backend.controller.sys;

import com.backend.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转首页控制器
 * @author DELL
 */
@Controller
public class IndexController extends BaseController {

    /**
     * 因为集成了flowable-modeler-ui, 所以把"/"路径留出来用以跳转到 flowable-modeler-ui
     * 首页不能命名为 index, 因为index和flowable-modler-ui的 index冲突
     * @param
     * @return
     */
//    @RequestMapping({"/","index"})
    @RequestMapping({"/index"})
    public String index(){
//        return "/index";
        return "home";
    }

}

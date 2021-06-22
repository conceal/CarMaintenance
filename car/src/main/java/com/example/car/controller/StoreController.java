package com.example.car.controller;

import com.example.car.impl.StoreServiceImpl;
import com.example.car.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/store")
public class StoreController {
    @Autowired
    StoreServiceImpl storeService;
//    @Autowired
//    ProgramServiceImpl programService;

    /**
     *
     * @param servletRequest
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public int register(HttpServletRequest servletRequest) {
        return storeService.register(servletRequest);
    }

    /**
     * 保养店主登陆
     * @param account
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Store login(@RequestParam("account")String account) {
        return storeService.login(account);
    }

    /**
     * 查找一个保养店
     * @param id
     * @return
     */
    @RequestMapping("/selectStore")
    @ResponseBody
    public Store selectStore(int id) {
        return storeService.selectStore(id);
    }

    @RequestMapping("/selectAllStore")
    @ResponseBody
    public List<Store> selectAllStore() {
        return storeService.selectAllStore();
    }

    @RequestMapping("/updateScore")
    @ResponseBody
    public int updateScore(int id, double score) {
        return storeService.updateScore(id, score);
    }

    @RequestMapping("/updateStoreTradingVolume")
    @ResponseBody
    public int updateStoreTradingVolume(int id) {
        return storeService.updateStoreTradingVolume(id);
    }

    @RequestMapping("/deleteStore")
    @ResponseBody
    public int deleteStore(int id) {
        return storeService.deleteStore(id);
    }

    @RequestMapping("/selectStoreBusinessTime")
    @ResponseBody
    public String selectStoreBusinessTime(int id){
        return storeService.selectStoreBusinessTime(id);
    }
}

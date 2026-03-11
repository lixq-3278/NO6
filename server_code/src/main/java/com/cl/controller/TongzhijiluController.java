package com.cl.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.view.TongzhijiluView;

import com.cl.service.TongzhijiluService;
import com.cl.service.NotificationService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;

@RestController
@RequestMapping("/tongzhijilu")
public class TongzhijiluController {
    @Autowired
    private TongzhijiluService tongzhijiluService;
    
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
        PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }

	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
        PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/lists")
    public R list( TongzhijiluEntity tongzhijilu){
       	EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
        return R.ok().put("data", tongzhijiluService.selectListView(ew));
    }

    @RequestMapping("/query")
    public R query(TongzhijiluEntity tongzhijilu){
        EntityWrapper< TongzhijiluEntity> ew = new EntityWrapper< TongzhijiluEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
		TongzhijiluView tongzhijiluView =  tongzhijiluService.selectView(ew);
		return R.ok("查询通知记录成功").put("data", tongzhijiluView);
    }
	
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
		tongzhijilu = tongzhijiluService.selectView(new EntityWrapper<TongzhijiluEntity>().eq("id", id));
        return R.ok().put("data", tongzhijilu);
    }

    @RequestMapping("/save")
    @SysLog("新增通知记录")
    public R save(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }
    
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改通知记录")
    public R update(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        tongzhijiluService.updateById(tongzhijilu);
        return R.ok();
    }

    @RequestMapping("/delete")
    @SysLog("删除通知记录")
    public R delete(@RequestBody Long[] ids){
        tongzhijiluService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    @RequestMapping("/retry")
    @SysLog("手动重试通知")
    public R retry(@RequestBody Long[] ids){
        for(Long id : ids) {
            TongzhijiluEntity record = tongzhijiluService.selectById(id);
            if(record != null && ("发送失败".equals(record.getZhuangtai()) || "重试失败".equals(record.getZhuangtai()))) {
                try {
                    boolean success = true;
                    record.setChongshicishu(record.getChongshicishu() + 1);
                    record.setZuijinfasongshijian(new Date());
                    
                    if (success) {
                        record.setZhuangtai("发送成功");
                        record.setCuowuxinxi("");
                    } else {
                        if (record.getChongshicishu() >= 5) {
                            record.setZhuangtai("重试失败");
                            record.setCuowuxinxi("已达到最大重试次数");
                        }
                    }
                    tongzhijiluService.updateById(record);
                } catch (Exception e) {
                    record.setChongshicishu(record.getChongshicishu() + 1);
                    record.setZuijinfasongshijian(new Date());
                    if (record.getChongshicishu() >= 5) {
                        record.setZhuangtai("重试失败");
                        record.setCuowuxinxi("已达到最大重试次数: " + e.getMessage());
                    }
                    tongzhijiluService.updateById(record);
                }
            }
        }
        return R.ok("重试操作完成");
    }
}

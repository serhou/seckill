package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by think on 2016-05-28-0028.
 */
@Controller
@RequestMapping("/seckill") //代表模块:url:/模块/资源/{id}/细分
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list"; //  /WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill==null) {
            return "forword:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(exposer,true);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(e.getMessage(), false);
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "KillPhone", required = false) Long phone) {
       if(phone ==null){
           return new SeckillResult<SeckillExecution>("未注册",false);
       }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executteSeckill(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(execution,true);
        }catch (RepeatKillException e){
            return new SeckillResult<SeckillExecution>(new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL),false);
        }catch (SeckillCloseException e){
            return new SeckillResult<SeckillExecution>(new SeckillExecution(seckillId,SeckillStateEnum.END),false);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new SeckillResult<SeckillExecution>(new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR),false);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(now.getTime(),true);
    }
}

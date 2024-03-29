package org.bupt.caain.controller;

import com.itextpdf.text.DocumentException;
import org.bupt.caain.model.AwardModel;
import org.bupt.caain.pojo.po.Award;
import org.bupt.caain.pojo.po.Entry;
import org.bupt.caain.service.AdminService;
import org.bupt.caain.service.VoteService;
import org.bupt.caain.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;
    private final VoteService voteService;
    private final AwardModel awardModel;

    @Autowired
    public AdminController(AdminService adminService, VoteService voteService, AwardModel awardModel) {
        this.adminService = adminService;
        this.voteService = voteService;
        this.awardModel = awardModel;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminPage() {
        return "admin";
    }

    @RequestMapping(value = "result", method = RequestMethod.GET)
    public String VoteResultView(){
        return "result-view";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public @ResponseBody
    CommonResult getAwards() {
        List<Award> awards = adminService.getAwards();
        if (null != awards && awards.size() > 0) {
            return CommonResult.success("获取全部奖项", awards);
        } else {
            return CommonResult.failure("获取全部奖项失败");
        }
    }

    @RequestMapping(value = "vote/clear", method = RequestMethod.PUT)
    public @ResponseBody
    CommonResult clear() {
        adminService.clearVote();
        return CommonResult.success("成功清空投票结果");
    }

    @RequestMapping(value = "/pdf/{award_id}", method = RequestMethod.POST)
    public @ResponseBody
    CommonResult buildFinalPDF(@PathVariable int award_id) {
        Award award = adminService.getAward(award_id);
        if (award == null || !award.isVoted()) {
            return CommonResult.failure("该奖项投票还未开始");
        }
        if (!voteService.isVotedDown()) {
            return CommonResult.failure("还有" + voteService.getNotVotedExpertCount() + "名专家未投票");
        }
        try {
            adminService.printFinalPDF(award_id);
        } catch (DocumentException e) {
            return CommonResult.failure("PDF文件生成失败");
        }
//        adminService.stopVote(award_id);
        return CommonResult.success("最终文件生成成功");
    }

    @PostMapping(value = "start_vote/{award_id}")
    public @ResponseBody
    CommonResult startVote(@PathVariable int award_id) {
        int affectRows = adminService.startVote(award_id);
        if (affectRows > 0) {
            return CommonResult.success("开启投票成功");
        } else if (affectRows == -1) {
            return CommonResult.failure("该奖项投票已开启");
        } else {
            return CommonResult.failure("开启投票失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "result/view/{awardId}", method = RequestMethod.GET)
    public CommonResult voteResult(@PathVariable int awardId){
        // 构造返回信息
        Map<String, Object> ret = new HashMap<>();
        ret.put("award", awardModel.queryById(awardId));
        List<Entry> entries = adminService.getVoteResult(awardId);
        if(entries!=null&&entries.size()>0){
            // 排序
            entries.sort((a, b) -> {
                if (a.getLevel1() != b.getLevel1()) {
                    return b.getLevel1() - a.getLevel1();
                }

                if (a.getLevel2() != b.getLevel2()) {
                    return b.getLevel2() - a.getLevel2();
                }

                if (a.getLevel3() != b.getLevel3()) {
                    return b.getLevel3() - a.getLevel3();
                }

                return a.getId() - b.getId();
            });
            ret.put("vote", entries);
            return CommonResult.success("查询成功", ret);
        }else{
            return CommonResult.failure("没有参评该奖项的作品");
        }
    }

}

package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Constant;
import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import com.lazuardifachri.bps.lekdarjo.service.IndicatorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/indicator/economy")
public class AdmIndicatorEcoController {

    Logger log = LoggerFactory.getLogger(AdmIndicatorEcoController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IndicatorService indicatorService;

    @GetMapping("")
    public String indicator(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 1;

        if (number.isPresent()) {
            pageNumber = number.get() ;
        }

        Pageable paging = PageRequest.of(pageNumber-1, Constant.PAGE_NUMBER);
        Page<Indicator> pageTuts = indicatorService.readIndicatorBySubject("2",paging);

        model.addAttribute("indicatorPage", pageTuts);

        if (pageNumber + 3 <= pageTuts.getTotalPages() + 1) {
            log.info("not overlap");
            model.addAttribute("prev", pageNumber - 1);
            model.addAttribute("no1", pageNumber);
            model.addAttribute("no2", pageNumber + 1);
            model.addAttribute("no3", pageNumber + 2);
            model.addAttribute("next", pageNumber + 3);
        } else if (pageNumber + 3 > pageTuts.getTotalPages() + 1) {
            int overlap = pageNumber + 3 - pageTuts.getTotalPages() - 1;
            model.addAttribute("prev", pageNumber - 1 - overlap);
            model.addAttribute("no1", pageNumber - overlap);
            model.addAttribute("no2", pageNumber + 1 - overlap);
            model.addAttribute("no3", pageNumber + 2 - overlap);
            model.addAttribute("next", pageNumber + 3 - overlap);
        }

        return "indicator_economy";
    }

    @GetMapping("/add")
    public String indicatorAddForm(Model model) {
        model.addAttribute("indicator", new Indicator());
        return "indicator_economy_add";
    }

    @PostMapping("/add")
    public String indicatorAdd(@RequestParam("file") MultipartFile file, @Valid Indicator indicator, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.info(result.getFieldError().toString());
            return "indicator_economy_add";
        }

        if (file.isEmpty()) {
            model.addAttribute("message", "File input can not be empty");
            return "indicator_economy_add";
        }

        try {
            indicatorService.createIndicator(indicator.apiString(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/indicator/economy";
    }

    @GetMapping("/edit/{id}")
    public String indicatorEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("indicator", indicatorService.readIndicatorByid(id));
        return "indicator_economy_edit";
    }

    @PostMapping("/edit/{id}")
    public String indicatorEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid Indicator indicator, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "indicator_economy_edit";
        }

        try {
            if (!file.isEmpty()) {
                indicatorService.updateIndicator(id, indicator.apiString(), file);
            } else {
                indicatorService.updateIndicator(id, indicator.apiString(), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/indicator/economy";
    }

    @GetMapping("/delete/{id}")
    public String indicatorDelete(@PathVariable("id") String id) {
        indicatorService.deleteIndicator(id);
        return "redirect:/admin/indicator/economy";
    }
}

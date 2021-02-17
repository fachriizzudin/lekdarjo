package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import com.lazuardifachri.bps.lekdarjo.service.StatisticalNewsService;

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
@RequestMapping("/admin/brs")
public class AdmStatNewsController {

    Logger log = LoggerFactory.getLogger(AdmStatNewsController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StatisticalNewsService statisticalNewsService;
 
    @GetMapping("")
    public String brs(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 0;

        if (number.isPresent()) {
            pageNumber = number.get();
        }

        log.info(String.valueOf(pageNumber));

        Pageable paging = PageRequest.of(pageNumber, 5);
        Page<StatisticalNews> pageTuts = statisticalNewsService.readAllStatisticalNews(paging);

        log.info(String.valueOf(pageTuts.getTotalPages()));

        model.addAttribute("brsPage", pageTuts);

        if (pageNumber + 3 <= pageTuts.getTotalPages()) {
            model.addAttribute("prev", pageNumber - 1);
            model.addAttribute("no1", pageNumber);
            model.addAttribute("no2", pageNumber + 1);
            model.addAttribute("no3", pageNumber + 2);
            model.addAttribute("next", pageNumber + 3);
        } else if (pageNumber + 3 > pageTuts.getTotalPages()) {
            int overlap = pageNumber + 3 - pageTuts.getTotalPages();
            model.addAttribute("prev", pageNumber - 1 - overlap);
            model.addAttribute("no1", pageNumber - overlap);
            model.addAttribute("no2", pageNumber + 1 - overlap);
            model.addAttribute("no3", pageNumber + 2 - overlap);
            model.addAttribute("next", pageNumber + 3 - overlap);
        }

        return "brs";
    }

    @GetMapping("/add")
    public String brsAddForm(Model model) {
        model.addAttribute("statisticalNews", new StatisticalNews());
        return "brs_add";
    }

    @PostMapping("/add")
    public String brsAdd(@RequestParam("file") MultipartFile file, @Valid StatisticalNews brs, BindingResult result, 
            Model model) {
        if (result.hasErrors()) {
            return "brs_add";
        }

        if (file.isEmpty()) {
            model.addAttribute("message", "File input can not be empty");
            return "brs_add";
        }

        try {
            statisticalNewsService.createStatisticalNews(brs.apiString(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/brs";
    }

    @GetMapping("/edit/{id}")
    public String brsEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("statisticalNews", statisticalNewsService.readStatisticalNewsById(id));
        return "brs_edit";
    }

    @PostMapping("/edit/{id}")
    public String brsEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid StatisticalNews brs, BindingResult result, Model model) {

        if (result.hasErrors()) {
            log.info(result.getFieldError().toString());
            return "brs_edit";
        }

        log.info("no errors");

        try {
            if (!file.isEmpty()) {
                statisticalNewsService.updateStatisticalNews(id, brs.apiString(), file);
            } else {
                statisticalNewsService.updateStatisticalNews(id, brs.apiString(), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/brs";
    }

    @GetMapping("/delete/{id}")
    public String brsDelete(@PathVariable("id") String id) {
        statisticalNewsService.deleteStatisticalNews(id);
        return "redirect:/admin/brs";
    }

}

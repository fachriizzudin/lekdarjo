package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Constant;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/indicator/agriculture")
public class AdmIndicatorAgriController {

    Logger log = LoggerFactory.getLogger(AdmIndicatorAgriController.class);

    @Autowired
    IndicatorService indicatorService;

    @GetMapping("")
    public String indicator(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 1;

        if (number.isPresent()) {
            pageNumber = number.get() ;
        }

        Pageable paging = PageRequest.of(pageNumber-1, Constant.PAGE_NUMBER);
        Page<Indicator> pageTuts = indicatorService.readIndicatorBySubject("3",paging);

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

        return "indicator_agriculture";
    }

    @GetMapping("/add")
    public String indicatorAddForm(Model model) {
        model.addAttribute("indicator", new Indicator());
        return "indicator_agriculture_add";
    }

    @PostMapping("/add")
    public String indicatorAdd(@RequestParam("file") MultipartFile file, @Valid Indicator indicator, BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "indicator_agriculture_add";
        }

        redirectAttributes.addFlashAttribute("toast", true);

//        if (file.isEmpty()) {
//            model.addAttribute("message", "File input can not be empty");
//            return "indicator_agriculture_add";
//        }

        try {
            if (file.isEmpty()) {
                indicatorService.createIndicator(indicator.apiStringWithUri(), null);
            } else {
                indicatorService.createIndicator(indicator.apiString(), file);
            }
        }catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/indicator/agriculture/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Penambahan data gagal");
            return "redirect:/admin/indicator/agriculture";
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Penambahan data berhasil");

        return "redirect:/admin/indicator/agriculture";
    }

    @GetMapping("/edit/{id}")
    public String indicatorEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("indicator", indicatorService.readIndicatorByid(id));
        return "indicator_agriculture_edit";
    }

    @PostMapping("/edit/{id}")
    public String indicatorEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid Indicator indicator, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "indicator_agriculture_edit";
        }

        redirectAttributes.addFlashAttribute("toast", true);

        try {
            if (!file.isEmpty()) {
                indicatorService.updateIndicator(id, indicator.apiString(), file);
            } else {
                indicatorService.updateIndicator(id, indicator.apiStringWithUri(), null);
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Pembaruan data gagal");
            return "redirect:/admin/indicator/agriculture";
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Pembaruan data berhasil");

        return "redirect:/admin/indicator/agriculture";
    }

    @GetMapping("/delete/{id}")
    public String indicatorDelete(@PathVariable("id") String id) {
        indicatorService.deleteIndicator(id);
        return "redirect:/admin/indicator/agriculture";
    }
}

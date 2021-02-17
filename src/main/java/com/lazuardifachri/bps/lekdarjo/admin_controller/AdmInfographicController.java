package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.service.InfographicService;

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
@RequestMapping("/admin/infographic")
public class AdmInfographicController {
    
    Logger log = LoggerFactory.getLogger(AdmInfographicController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    InfographicService infographicService;

    @GetMapping("")
    public String infographic(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 0;

        if (number.isPresent()) {
            pageNumber = number.get();
        }

        log.info(String.valueOf(pageNumber));

        Pageable paging = PageRequest.of(pageNumber, 5);
        Page<Infographic> pageTuts = infographicService.readAllInfographic(paging);

        log.info(String.valueOf(pageTuts.getTotalPages()));

        model.addAttribute("infographicPage", pageTuts);

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

        return "infographic";
    }

    @GetMapping("/add")
    public String infographicAddForm(Model model) {
        model.addAttribute("infographic", new Infographic());
        return "infographic_add";
    }

    @PostMapping("/add")
    public String infographicAdd(@RequestParam("file") MultipartFile file, @Valid Infographic infographic, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.info(result.getFieldError().toString());
            return "infographic_add";
        }

        if (file.isEmpty()) {
            model.addAttribute("message", "File input can not be empty");
            return "brs_add";
        }

        try {
            infographicService.createInfographic(infographic.apiString(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/infographic";
    }

    @GetMapping("/edit/{id}")
    public String infographicEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("infographic", infographicService.readInfographicByid(id));
        return "infographic_edit";
    }

    @PostMapping("/edit/{id}")
    public String infographicEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid Infographic infographic, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "infographic_edit";
        }

        try {
            if (!file.isEmpty()) {
                infographicService.updateInfographic(id, infographic.apiString(), file);
            } else {
                infographicService.updateInfographic(id, infographic.apiString(), null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/infographic";
    }

    @GetMapping("/delete/{id}")
    public String infographicDelete(@PathVariable("id") String id) {
        infographicService.deleteInfographic(id);
        return "redirect:/admin/infographic";
    }

}

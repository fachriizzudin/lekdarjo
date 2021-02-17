package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.service.PublicationService;
import com.lazuardifachri.bps.lekdarjo.model.Publication;

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
@RequestMapping("/admin/publication")
public class AdmPublicationController {

    Logger log = LoggerFactory.getLogger(AdmPublicationController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PublicationService publicationService;


    @GetMapping("")
    public String publication(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 0;

        if (number.isPresent()) {
            pageNumber = number.get();
        }

        log.info(String.valueOf(pageNumber));

        Pageable paging = PageRequest.of(pageNumber, 5);
        Page<Publication> pageTuts = publicationService.readAllPublication(paging);

        log.info(String.valueOf(pageTuts.getTotalPages()));

        model.addAttribute("publicationsPage", pageTuts);

        if (pageNumber + 3 <= pageTuts.getTotalPages()) {
            model.addAttribute("prev", pageNumber - 1);
            model.addAttribute("no1", pageNumber);
            model.addAttribute("no2", pageNumber + 1);
            model.addAttribute("no3", pageNumber + 2);
            model.addAttribute("next", pageNumber + 3);
        } else if (pageNumber + 3 > pageTuts.getTotalPages()){
            int overlap = pageNumber+3 - pageTuts.getTotalPages();
            model.addAttribute("prev", pageNumber - 1 - overlap);
            model.addAttribute("no1", pageNumber - overlap);
            model.addAttribute("no2", pageNumber + 1 - overlap);
            model.addAttribute("no3", pageNumber + 2 - overlap);
            model.addAttribute("next", pageNumber + 3 - overlap);
        } 
       
        return "publication";
    }

    @GetMapping("/add")
    public String publicationAddForm(Model model) {
        model.addAttribute("publication", new Publication());
        return "publication_add";
    }

    @PostMapping("/add")
    public String publicationAdd(@RequestParam("file") MultipartFile file, @Valid Publication publication, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "publication_add";
        }

        if (file.isEmpty()) {
            model.addAttribute("message", "File input can not be empty");
            return "publication_add";
        }

        try {
            publicationService.createPublication(publication.apiString(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/publication";
    }

    @GetMapping("/edit/{id}")
    public String publicationEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("publication", publicationService.readPublicationByid(id));
        return "publication_edit";
    }

    @PostMapping("/edit/{id}")
    public String publicationEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid Publication publication, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "publication_edit";
        }
 
        try {
            if (!file.isEmpty()) {
                publicationService.updatePublication(id, publication.apiString(), file);
            } else {
                publicationService.updatePublication(id, publication.apiString(), null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/publication";
    }
    

    @GetMapping("/delete/{id}")
    public String publicationDelete(@PathVariable("id") String id) {
        publicationService.deletePublication(id);
        return "redirect:/admin/publication";
    }

}

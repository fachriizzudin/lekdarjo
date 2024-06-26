package com.lazuardifachri.bps.lekdarjo.admin_controller;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Constant;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/publication")
public class AdmPublicationController {

    Logger log = LoggerFactory.getLogger(AdmPublicationController.class);

    @Autowired
    PublicationService publicationService;


    @GetMapping("")
    public String publication(@RequestParam("page") Optional<Integer> number, Model model) {

        int pageNumber = 1;

        if (number.isPresent()) {
            pageNumber = number.get() ;
        }

        Pageable paging = PageRequest.of(pageNumber-1, Constant.PAGE_NUMBER);
        Page<Publication> pageTuts = publicationService.readAllPublication(paging);

        log.info(String.valueOf(pageTuts.getTotalPages()));

        model.addAttribute("publicationsPage", pageTuts);

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
       
        return "publication";
    }

    @GetMapping("/add")
    public String publicationAddForm(Model model) {
        model.addAttribute("publication", new Publication());
        return "publication_add";
    }

    @PostMapping("/add")
    public String publicationAdd(@RequestParam("file") MultipartFile file, @Valid Publication publication, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "publication_add";
        }

//        if (file.isEmpty()) {
//            model.addAttribute("message", "File input can not be empty");
//            return "publication_add";
//        }

        redirectAttributes.addFlashAttribute("toast", true);

        try {
            if (file.isEmpty()) {
                publicationService.createPublication(publication.apiStringWithUri(), null);
            } else {
                publicationService.createPublication(publication.apiString(), file);
            }
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/publication/add";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Penambahan data gagal");
            return "redirect:/admin/publication";
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Penambahan data berhasil");

        return "redirect:/admin/publication";
    }

    @GetMapping("/edit/{id}")
    public String publicationEditForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("publication", publicationService.readPublicationByid(id));
        return "publication_edit";
    }

    @PostMapping("/edit/{id}")
    public String publicationEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            @Valid Publication publication, BindingResult result, RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "publication_edit";
        }

        redirectAttributes.addFlashAttribute("toast", true);
 
        try {
            if (!file.isEmpty()) {
                publicationService.updatePublication(id, publication.apiString(), file);
            } else {
                publicationService.updatePublication(id, publication.apiStringWithUri(), null);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Pembaruan data gagal");
            return "redirect:/admin/publication";
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Pembaruan data berhasil");

        return "redirect:/admin/publication";
    }
    

    @GetMapping("/delete/{id}")
    public String publicationDelete(@PathVariable("id") String id) {
        publicationService.deletePublication(id);
        return "redirect:/admin/publication";
    }

}

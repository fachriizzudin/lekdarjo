
package com.lazuardifachri.bps.lekdarjo.admin_controller;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.Constant;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.model.GenericGraph;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import com.lazuardifachri.bps.lekdarjo.service.GraphMetaService;
import com.lazuardifachri.bps.lekdarjo.service.GraphService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/graph")
public class AdmGraphController {

    Logger log = LoggerFactory.getLogger(AdmGraphController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GraphService graphService;

    @Autowired
    GraphMetaService graphMetaService;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("graphMeta", graphMetaService.readAllGraphMeta());
        return "graph";
    }

    @GetMapping("/add")
    public String graphMetaAddForm(Model model) {
        List<Integer> options = getSerialNumber(graphMetaService.readAllSerialNumber());
        model.addAttribute("options", options);
        model.addAttribute("graphMeta", new GraphMeta());
        return "graph_meta_add";
    }

    @PostMapping("/add")
    public String graphMetaAdd(@RequestParam("file") MultipartFile file, @Valid GraphMeta graphMeta, BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            log.info(result.getFieldError().toString());
            return "graph_meta_add";
        }

        log.info(graphMeta.toString());
        redirectAttributes.addFlashAttribute("toast", true);

        try {
            if (file.isEmpty()) {
                graphMetaService.createGraphMeta(graphMeta.apiStringWithUri(), null);
                log.info(graphMeta.apiStringWithUri());
                log.info("empty file");
            } else {
                graphMetaService.createGraphMeta(graphMeta.apiString(), file);
                log.info(graphMeta.apiString());
                log.info("file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Penambahan data gagal");
            return "redirect:/admin/graph";
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Penambahan data berhasil");

        return "redirect:/admin/graph";
    }

    @GetMapping("/{id}/meta/edit")
    public String graphMetaEditForm(@PathVariable("id") String id, Model model) {
        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);
        List<Integer> options = getSerialNumber(graphMetaService.readAllSerialNumber(graphMeta.getSerialNumber()));
        log.info(graphMeta.toString());
        model.addAttribute("options", options);
        model.addAttribute("graphMeta", graphMeta);
        return "graph_meta_edit";
    }

    @PostMapping("/{id}/meta/edit")
    public String graphMetaEdit(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
                                @Valid GraphMeta graphMeta, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "graph_meta_edit";
        }

        log.info(graphMeta.toString());

        redirectAttributes.addFlashAttribute("toast", true);

        try {
            if (!file.isEmpty()) {
                graphMetaService.updateGraphMeta(id, graphMeta.apiString(), file);
            } else {
                graphMetaService.updateGraphMeta(id, graphMeta.apiStringWithUri(), null);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Pembaruan data gagal");
            return "redirect:/admin/graph/" + id;
        }

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Pembaruan data berhasil");

        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/delete/{id}")
    public String graphMetaDelete(@PathVariable("id") String id) {
        graphMetaService.deleteGraphMeta(id);
        return "redirect:/admin/graph";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        List<Graph> graphs = graphService.readAllGraph(id);
        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);
        model.addAttribute("graphMeta", graphMeta);

        if (graphMeta.getDataType() == 2) {
            model.addAttribute("graphs", convertToIntGraphs(graphs));
        } else {
            model.addAttribute("graphs", graphService.readAllGraph(id));
        }

        return "graph_list";
    }

    @GetMapping("/{id}/add")
    public String graphDataAddForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("graphData", new Graph());
        model.addAttribute("yearOptions", getYearOptions());
        return "graph_data_add";
    }

    @PostMapping("/{id}/add")
    public String graphDataAdd(@PathVariable("id") String id, @Valid Graph graphData, BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info(result.getFieldError().toString());
            redirectAttributes.addFlashAttribute("message", "Use decimal format with periods \".\" instead of commas");
            return "redirect:/admin/graph/" + id + "/add";
        }

        try {
            graphService.createGraph(objectMapper.writeValueAsString(graphData), id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/graph/" + id + "/add";
        }

        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/{id}/edit/{dataId}")
    public String graphDataEditForm(@PathVariable("id") String id, @PathVariable("dataId") String dataId, Model model) {
        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);
        Graph graphData = graphService.readById(dataId);
        model.addAttribute("id", id);
        model.addAttribute("yearOptions", getYearOptions());

        if (graphMeta.getDataType() == 2) {
            model.addAttribute("graphData", convertToIntGraph(graphData));
        } else {
            model.addAttribute("graphData", graphData);
        }

        return "graph_data_edit";
    }

    @PostMapping("/{id}/edit/{dataId}")
    public String graphDataEdit(@PathVariable("id") String id, @PathVariable("dataId") String dataId,
            @Valid Graph graphData, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Use decimal format with periods \".\" instead of commas");
            return "redirect:/admin/graph/" + id + "/edit/" + dataId;
        }

        try {
            graphService.updateGraph(dataId, objectMapper.writeValueAsString(graphData), id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/graph/" + id + "/edit/" + dataId;
        }

        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/{id}/delete/{dataId}")
    public String graphDataDelete(@PathVariable("id") String id, @PathVariable("dataId") String dataId) {
        graphService.deleteGraph(dataId);
        return "redirect:/admin/graph/" + id;
    }

    private List<Integer> getSerialNumber(List<Integer> numberOwned) {
        List<Integer> options = new ArrayList<>();
        int i = 1;
        while (options.size() <=  10) {
            if (!numberOwned.contains(i)) {
                options.add(i);
            }
            i++;
        }
        return options;
    }

    private List<GenericGraph<Integer>> convertToIntGraphs(List<Graph> graphs) {
        List<GenericGraph<Integer>> genericGraphs = new ArrayList<>();
        for (Graph g : graphs) {
            genericGraphs.add(convertToIntGraph(g));
        }
        return genericGraphs;
    }

    private GenericGraph<Integer> convertToIntGraph(Graph graph) {
        return new GenericGraph<>(graph.getId(), (int) graph.getValue().doubleValue(), graph.getYear());
    }

    private int[] getYearOptions() {
        int year = Year.now().getValue();
        int[] yearOptions = new int[10];
        for (int i = 0; i < yearOptions.length; i++) {
            yearOptions[i] = year - i;
        }
        return yearOptions;
    }
}

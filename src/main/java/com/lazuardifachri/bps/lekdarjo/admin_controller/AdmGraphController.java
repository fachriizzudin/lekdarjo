
package com.lazuardifachri.bps.lekdarjo.admin_controller;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.model.GenericGraph;
import com.lazuardifachri.bps.lekdarjo.model.Graph;
import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import com.lazuardifachri.bps.lekdarjo.service.GraphMetaService;
import com.lazuardifachri.bps.lekdarjo.service.GraphService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdmGraphController {

    Logger log = LoggerFactory.getLogger(AdmGraphController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GraphService graphService;

    @Autowired
    GraphMetaService graphMetaService;

    @GetMapping("/admin/graph")
    public String list() {
        return "graph";
    }

    @GetMapping("/admin/graph/{id}")
    public String graph(@PathVariable("id") String id, Model model) {
        List<Graph> graphs = graphService.readAllGraph(id);

        if (id.equals("5") || id.equals("7") || id.equals("8") || id.equals("10") || id.equals("12")) {
            model.addAttribute("graphs", convertToIntGraphs(graphs));
        } else {
            model.addAttribute("graphs", graphService.readAllGraph(id));
        }

        model.addAttribute("graphMeta", graphMetaService.readGraphMetaByid(id));

        return "graph_list";
    }

    @GetMapping("/admin/graph/{id}/add")
    public String graphDataAddForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("graphData", new Graph());
        model.addAttribute("yearOptions", getYearOptions());
        return "graph_data_add";
    }

    @PostMapping("/admin/graph/{id}/add")
    public String graphDataAdd(@PathVariable("id") String id, @Valid Graph graphData, BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Use decimal format with periods \".\" instead of commas");
            return "redirect:/admin/graph/" + id + "/add";
        }

        try {
            graphService.createGraph(objectMapper.writeValueAsString(graphData), id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Use decimal format with periods \".\" instead of commas");
            return "redirect:/admin/graph/" + id + "/add";
        }

        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/admin/graph/{id}/edit/{dataId}")
    public String graphDataEditForm(@PathVariable("id") String id, @PathVariable("dataId") String dataId, Model model) {

        Graph graphData = graphService.readById(dataId);
        model.addAttribute("id", id);

        if (id.equals("5") || id.equals("7") || id.equals("8") || id.equals("10") || id.equals("12")) {
            model.addAttribute("graphData", convertToIntGraph(graphData));
        } else {
            model.addAttribute("graphData", graphData);
        }

        model.addAttribute("yearOptions", getYearOptions());

        return "graph_data_edit";
    }

    @PostMapping("/admin/graph/{id}/edit/{dataId}")
    public String graphDataEdit(@PathVariable("id") String id, @PathVariable("dataId") String dataId,
            @Valid Graph graphData, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Use decimal format with periods \".\" instead of commas");
            return "redirect:/admin/graph/" + id + "/edit/" + dataId;
        }

        try {
            graphService.updateGraph(dataId, objectMapper.writeValueAsString(graphData), id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", ExceptionMessage.YEAR_EXIST);
            return "redirect:/admin/graph/" + id + "/edit/" + dataId;
        }

        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/admin/graph/{id}/delete/{dataId}")
    public String graphDataDelete(@PathVariable("id") String id, @PathVariable("dataId") String dataId) {
        graphService.deleteGraph(dataId);
        return "redirect:/admin/graph/" + id;
    }

    @GetMapping("/admin/graph/{id}/meta/edit")
    public String graphMetaEditForm(@PathVariable("id") String id, Model model) {
        GraphMeta graphMeta = graphMetaService.readGraphMetaByid(id);
        model.addAttribute("graphMeta", graphMeta);
        return "graph_meta_edit";
    }

    @PostMapping("/admin/graph/{id}/meta/edit")
    public String graphMetaEdit(@PathVariable("id") String id, @Valid GraphMeta graphMeta, BindingResult result) {
        if (result.hasErrors()) {
            return "graph_meta_edit";
        }

        try {
            graphMetaService.updateGraphMeta(id, graphMeta.apiString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/graph/" + id;
    }

    private List<GenericGraph<Integer>> convertToIntGraphs(List<Graph> graphs) {
        List<GenericGraph<Integer>> genericGraphs = new ArrayList<>();
        for (Graph g : graphs) {
            genericGraphs.add(new GenericGraph<>(g.getId(), (int) g.getValue().doubleValue(), g.getYear()));
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

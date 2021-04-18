package com.lazuardifachri.bps.lekdarjo.admin_controller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdmGraphControllerTest {

    @Test
    void graphMetaAddForm() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(7);

        List<Integer> options = new ArrayList<>();

        int i = 1;
        while (options.size() <=  10) {
            if (!list.contains(i)) {
                options.add(i);
            }
            i++;
        }

        for (Integer j : options) {
            System.out.println(j);
        }
    }
}
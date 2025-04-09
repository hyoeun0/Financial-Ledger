package com.FinancialLedger.money.controller;

import com.FinancialLedger.money.dto.HistoryDTO;
import com.FinancialLedger.money.service.HistoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/money/main")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/money/index")
    public String indexForm() {
        return "index";
    }

    @PostMapping("/money/main")
    public String historySave(@ModelAttribute HistoryDTO historyDTO, HttpSession session){
        historyService.historySave(historyDTO);

        return "redirect:/money/main";
    }



}

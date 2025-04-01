package com.example.wallet_example1.balance.controller;

import com.example.wallet_example1.balance.controller.port.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BalanceController {
    private final BalanceService balanceService;
}

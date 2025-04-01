package com.example.wallet_example1.balance.controller;

import com.example.wallet_example1.balance.controller.port.BalanceService;
import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BalanceController {
    private final BalanceService balanceService;

    @PostMapping("/balance/create")
    public String createBalance(@RequestBody BalanceCreateReqDto dto) {
        try {
            balanceService.initializeBalance(dto.getBalanceId(), dto.getMemberId(), dto.getAmount());
            return "ok";
        } catch (Exception e) {
            return "not ok";
        }
    }

    @PostMapping("/balance/transfer")
    public BalanceTransferResDto transferBalance(@RequestBody BalanceTransferReqDto dto) {
        BalanceEvent event = BalanceEvent.builder()
                .eventId(dto.getEventId())
                .amount(dto.getAmount())
                .senderBalanceId(dto.getSenderBalanceId())
                .receiverBalanceId(dto.getReceiverBalanceId())
                .balanceOperation(BalanceOperation.SUBTRACT)
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .build();
        try {
            balanceService.publishBalanceEvent(event);
            return new BalanceTransferResDto("S", "transfer message created");
        } catch (Exception e) {
            String message = e.getMessage();
            return new BalanceTransferResDto("F", message);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BalanceTransferReqDto {
        private Long eventId;
        private String senderBalanceId;
        private String receiverBalanceId;
        private String amount;
        private Long senderId;
        private Long receiverId;
        @Builder
        private BalanceTransferReqDto(Long eventId, String senderBalanceId, String receiverBalanceId, BalanceOperation balanceOperation, String amount, Long senderId, Long receiverId) {
            this.eventId = eventId;
            this.senderBalanceId = senderBalanceId;
            this.receiverBalanceId = receiverBalanceId;
            this.balanceOperation = balanceOperation;
            this.amount = amount;
            this.senderId = senderId;
            this.receiverId = receiverId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BalanceTransferResDto {
        private String status;
        private String message;
        public BalanceTransferResDto(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BalanceCreateReqDto {
        private Long balanceId;
        private Long memberId;
        private String amount;
        @Builder
        private BalanceCreateReqDto(Long balanceId, Long memberId, String amount) {
            this.balanceId = balanceId;
            this.memberId = memberId;
            this.amount = amount;
        }
    }

}

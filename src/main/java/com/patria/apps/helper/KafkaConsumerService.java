package com.patria.apps.helper;

import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.request.BasketCreateRequest;
import com.patria.apps.transaction.request.BasketUpdateRequest;
import com.patria.apps.transaction.request.BasketDestroyRequest;
import com.patria.apps.transaction.request.TransactionsCreateRequest;
import com.patria.apps.transaction.request.TransactionsDestroyRequest;
import com.patria.apps.transaction.request.TransactionsPayRequest;
import com.patria.apps.transaction.request.TransactionsProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateRequest;
import com.patria.apps.transaction.request.TransactionsUpdateStatusRequest;
import com.patria.apps.transaction.service.BasketService;
import com.patria.apps.transaction.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BasketService basketService;
    private final TransactionsService transactionsService;

    @KafkaListener(topics = "basket-create-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload BasketCreateRequest request) {
        ReadResponse response = basketService.createBasket(request);
    }
    
    @KafkaListener(topics = "basket-destroy-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload BasketDestroyRequest request) {
        ReadResponse response = basketService.destroyBasket(request);
    }

    @KafkaListener(topics = "basket-update-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload BasketUpdateRequest request) {
        ReadResponse response = basketService.updateBasket(request);
    }

    @KafkaListener(topics = "transactions-create-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsCreateRequest request) {
        ReadResponse response = transactionsService.createTransactions(request);
    }

    @KafkaListener(topics = "transactions-destroy-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsDestroyRequest request) {
        ReadResponse response = transactionsService.destroyTransactions(request);
    }

    @KafkaListener(topics = "transactions-update-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsUpdateRequest request) {
        ReadResponse response = transactionsService.updateTransactions(request);
    }

    @KafkaListener(topics = "transactions-update-status-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsUpdateStatusRequest request) {
        ReadResponse response = transactionsService.updateStatusTransactions(request);
    }
    
    @KafkaListener(topics = "transactions-pay-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsPayRequest request) {
        ReadResponse response = transactionsService.payTransactions(request);
    }
    
    @KafkaListener(topics = "transactions-product-review-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsProductReviewRequest request) {
        ReadResponse response = transactionsService.productReview(request);
    }
    
    @KafkaListener(topics = "transactions-update-product-review-topic", groupId = "sonareclipse", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload TransactionsUpdateProductReviewRequest request) {
        ReadResponse response = transactionsService.updateProductReview(request);
    }
}

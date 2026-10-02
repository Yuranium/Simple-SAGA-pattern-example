package com.yuranium.service;

import com.yuranium.core.commands.*;
import com.yuranium.core.events.*;
import com.yuranium.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSagaService
{
    private final OrderHistoryService historyService;

    private final OutboxService outboxService;

    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event)
    {

        historyService.addNewHistory(
                event.orderId(),
                OrderStatus.CREATED
        );

        outboxService.createEvent(
                "good-command-topic",
                new GoodReserveCommand(
                        event.goodId(),
                        event.orderId(),
                        event.goodQuantity()
                )
        );
    }

    @Transactional
    public void handleGoodReserved(GoodReservedEvent event)
    {

        historyService.addNewHistory(
                event.orderId(),
                OrderStatus.RESERVED
        );

        outboxService.createEvent(
                "payment-command-topic",
                new PayGoodsCommand(
                        event.goodId(),
                        event.orderId(),
                        event.goodQuantity(),
                        event.goodPrice()
                )
        );

        outboxService.createEvent(
                "order-command-topic",
                new OrderStatusChangedEvent(
                        event.orderId(),
                        OrderStatus.RESERVED.name()
                )
        );
    }

    public void handleGoodReserveFailed(GoodReserveFailedEvent event)
    {
        outboxService.createEvent(
                "order-command-topic", new RejectOrderCommand(
                        event.orderId()
                )
        );
    }

    public void handlePaymentSuccessful(PaymentSuccessfulEvent event)
    {
        historyService.addNewHistory(event.orderId(), OrderStatus.PAYED);

        outboxService.createEvent(
                "good-command-topic", new GoodCompleteReserveCommand(
                        event.goodId(),
                        event.orderId(),
                        event.goodQuantity(),
                        event.goodPrice()
                )
        );
    }

    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event)
    {
        outboxService.createEvent(
                "good-command-topic",
                new CancelGoodReserveCommand(
                        event.goodId(),
                        event.orderId(),
                        event.goodQuantity()
                )
        );
    }

    @Transactional
    public void handleGoodCompleteReserve(GoodCompleteReserveEvent event)
    {
        outboxService.createEvent(
                "order-command-topic",
                new OrderStatusChangedEvent(
                        event.orderId(),
                        OrderStatus.APPROVED.name()
                )
        );

        historyService.addNewHistory(
                event.orderId(),
                OrderStatus.APPROVED
        );
    }

    @Transactional
    public void handleGoodFailedCompleteReserve(
            GoodFailedCompleteReserveEvent event
    )
    {
        outboxService.createEvent(
                "payment-command-topic",
                new CancelPaymentCommand(
                        event.goodId(),
                        event.orderId(),
                        event.goodQuantity(),
                        event.goodPrice()
                )
        );
    }

    @Transactional
    public void handleGoodReserveCancelled(
            GoodReserveCancelledEvent event
    )
    {
        outboxService.createEvent(
                "order-command-topic",
                new RejectOrderCommand(
                        event.orderId()
                )
        );
    }

    @Transactional
    public void handleOrderRejected(OrderRejectedEvent event)
    {
        historyService.addNewHistory(
                event.orderId(),
                OrderStatus.REJECTED
        );
    }
}
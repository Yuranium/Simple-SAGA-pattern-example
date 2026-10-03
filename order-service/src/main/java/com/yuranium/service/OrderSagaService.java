package com.yuranium.service;

import com.yuranium.core.MessageType;
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
                ),
                MessageType.GOOD_RESERVE_COMMAND
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
                ),
                MessageType.PAY_GOODS_COMMAND
        );

        outboxService.createEvent(
                "order-command-topic",
                new OrderStatusChangedEvent(
                        event.orderId(),
                        OrderStatus.RESERVED.name()
                ),
                MessageType.ORDER_STATUS_CHANGED_EVENT
        );
    }

    public void handleGoodReserveFailed(GoodReserveFailedEvent event)
    {
        outboxService.createEvent(
                "order-command-topic", new RejectOrderCommand(
                        event.orderId()
                ),
                MessageType.REJECT_ORDER_COMMAND
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
                ),
                MessageType.GOOD_COMPLETE_RESERVE_COMMAND
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
                ),
                MessageType.CANCEL_GOOD_RESERVE_COMMAND
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
                ),
                MessageType.ORDER_STATUS_CHANGED_EVENT
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
                ),
                MessageType.CANCEL_GOOD_RESERVE_COMMAND
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
                ),
                MessageType.REJECT_ORDER_COMMAND
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
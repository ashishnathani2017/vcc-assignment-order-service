package com.example.orderservice.service;

import com.example.orderservice.domain.CustomerOrder;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.repository.CustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;

    public OrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    public CustomerOrder saveOrder(CreateOrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        order.setRestaurantId(request.getRestaurantId());
        order.setCustomerName(request.getCustomerName());
        order.setItemName(request.getItemName());
        order.setQuantity(request.getQuantity());
        order.setStatus("SAVED");
        order.setCreatedAt(Instant.now());
        return customerOrderRepository.save(order);
    }

    public List<CustomerOrder> getAllOrders(Long restaurantId) {
        if (restaurantId != null) {
            return customerOrderRepository.findByRestaurantId(restaurantId);
        }
        return customerOrderRepository.findAll();
    }

    public CustomerOrder getOrderById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
    }

    public CustomerOrder updateOrder(Long id, UpdateOrderRequest request) {
        CustomerOrder order = getOrderById(id);
        order.setCustomerName(request.getCustomerName());
        order.setItemName(request.getItemName());
        order.setQuantity(request.getQuantity());
        order.setStatus(request.getStatus());
        return customerOrderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        customerOrderRepository.delete(getOrderById(id));
    }
}

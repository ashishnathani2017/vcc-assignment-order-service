package com.example.orderservice.repository;

import com.example.orderservice.domain.CustomerOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByRestaurantId(Long restaurantId);
}

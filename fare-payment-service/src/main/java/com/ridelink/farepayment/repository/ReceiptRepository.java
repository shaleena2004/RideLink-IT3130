package com.ridelink.farepayment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.farepayment.model.Receipt;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {
}
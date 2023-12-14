package tpalmerjr.springframework.spring6restmvc.services;

import tpalmerjr.springframework.spring6restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
}

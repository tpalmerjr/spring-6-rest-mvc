package tpalmerjr.springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tpalmerjr.springframework.spring6restmvc.model.Beer;
import tpalmerjr.springframework.spring6restmvc.model.Customer;
import tpalmerjr.springframework.spring6restmvc.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        return customerService.getAllCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getBeerById(@PathVariable("customerId") UUID customerId) {

        log.debug("Get Customer by id - in controller");

        return customerService.getCustomerById(customerId);
    }
}

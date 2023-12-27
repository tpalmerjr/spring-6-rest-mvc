package tpalmerjr.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tpalmerjr.springframework.spring6restmvc.model.Customer;
import tpalmerjr.springframework.spring6restmvc.services.CustomerService;
import tpalmerjr.springframework.spring6restmvc.services.CustomerServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl;

    Customer customer;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
        customer = customerServiceImpl.getAllCustomers().get(0);
    }

    @Test
    void testPatchCustomer() throws Exception {
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Customer");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(Customer.class));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        customer.setVersion(null);
        customer.setId(null);

        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.getAllCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        given(customerService.getCustomerById(customer.getId())).willReturn(customer);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.name", is(customer.getName())));
    }
}

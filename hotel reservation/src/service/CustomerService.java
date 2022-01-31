package service;

import model.Customer;
import java.util.ArrayList;
import java.util.Collection;

public class CustomerService {

    private static CustomerService INSTANCE;
    private static Collection<Customer> customers;

    private CustomerService()
    {
        customers = new ArrayList<>();
    }


    public static CustomerService getInstance(){
        if (INSTANCE == null){
            INSTANCE = new CustomerService();
        }
        return INSTANCE;
    }

    /**
     * create a customer account and added to the collection
     * @param email
     * @param firstName
     * @param lastName
     */
    public static void addCustomer(String email,String firstName,String lastName){
        Customer customer = new Customer(email,firstName,lastName);
        customers.add(customer);
    }

    /**
     *
     * @param email
     * @return customer with the email given
     */
    public static Customer getCustomer(String email){
        Customer customer = null;
        for (Customer c:customers){
            if (c.getEmail().equalsIgnoreCase(email)){
                customer = c;
            }
        }
        return customer;
    }

    /**
     *
     * @return collection of Customer
     */
    public static Collection<Customer> getAllCustomers(){
        return customers;
    }

}

package model;

import java.util.Objects;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String email,String firstName,String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (!emailRegex(email)){
            throw new IllegalArgumentException("Email Format incorrect");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public boolean emailRegex (String email){
        String emailRegex = "^(.+)@(.+).(.+)$";
        if (!email.matches(emailRegex)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer: " +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }
}

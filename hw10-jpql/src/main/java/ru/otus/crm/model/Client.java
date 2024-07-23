package ru.otus.crm.model;

import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        phones.forEach(phone -> phone.setClient(this));
        address.setClient(this);
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Client client = new Client(this.id, this.name);
        try {
            client.phones = getClonedPhones();
            client.phones.forEach(phone -> phone.setClient(client));
            client.address = this.address.clone();
            client.address.setClient(client);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    private List<Phone> getClonedPhones()
    {
        return this.phones.stream().map(phone -> {
            try {
                return phone.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}

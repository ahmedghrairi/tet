package com.mycompany.myapp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "dossier_client",
               joinColumns = @JoinColumn(name = "dossier_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Dossier nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Dossier clients(Set<Client> clients) {
        this.clients = clients;
        return this;
    }

    public Dossier addClient(Client client) {
        this.clients.add(client);
        client.getDossiers().add(this);
        return this;
    }

    public Dossier removeClient(Client client) {
        this.clients.remove(client);
        client.getDossiers().remove(this);
        return this;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

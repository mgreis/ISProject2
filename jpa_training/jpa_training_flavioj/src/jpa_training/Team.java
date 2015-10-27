/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa_training;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Flávio
 */
@Entity
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String president;
    @OneToMany(mappedBy = "team")
    private Collection<Player> players;

    public Team() {
    }

    public Team(String name, String address, String president) {
        this.name = name;
        this.address = address;
        this.president = president;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public Collection<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder desc = new StringBuilder();
        desc.append(getClass().getSimpleName());
        desc.append("[ id=");
        desc.append(getId());
        desc.append(" ; name=");
        desc.append(getName());
        desc.append(" ; address=");
        desc.append(getAddress());
        desc.append(" ; president=");
        desc.append(getPresident());
        desc.append(" ; players={");
        boolean appendComma = false;
        for (final Player player : getPlayers()){
            if (appendComma) {
                desc.append(",");
            }
            desc.append(player.getId());
            appendComma = true;
        }
        desc.append("} ]");
        return desc.toString();
    }

}

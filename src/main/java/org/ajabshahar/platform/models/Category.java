package org.ajabshahar.platform.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
@NamedQueries({
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAll",
                query = "SELECT p FROM Song p"
        )
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
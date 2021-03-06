package org.ajabshahar.platform.models;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY")
@NamedQueries({
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAll",
                query = "SELECT p FROM Category p"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAllSongCategories",
                query = "SELECT p FROM Category p where p.categoryType='song'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAllMediaCategories",
                query = "SELECT p FROM Category p where p.categoryType='media'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAllCoupletCategories",
                query = "SELECT p FROM Category p where p.categoryType='couplet'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAllWordCategories",
                query = "SELECT p FROM Category p where p.categoryType='word'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findUmbrellaTitleCategory",
                query = "SELECT p FROM Category p where p.categoryType='Umbrella Title'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findSongTitleCategory",
                query = "SELECT p FROM Category p where p.categoryType='Song Title'"
        ),
        @NamedQuery(
                name = "org.ajabshahar.platform.models.Category.findAllPersonCategories",
                query = "SELECT p FROM Category p where p.categoryType='person'"
        )

})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CATEGORY_TYPE", nullable = false)
    private String categoryType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}

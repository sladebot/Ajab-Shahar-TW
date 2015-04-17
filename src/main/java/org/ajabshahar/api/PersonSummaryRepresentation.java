package org.ajabshahar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.ajabshahar.platform.models.Category;
import org.ajabshahar.platform.models.PersonDetails;

import java.util.*;

import static java.lang.String.format;

public class PersonSummaryRepresentation {
    @Setter
    private long id;
    private String name;
    private String hindiName;
    private String primaryOccupation;

    public PersonSummaryRepresentation() {
    }

    public PersonSummaryRepresentation(long id, String name, String hindiName, String primaryOccupation) {
        this.id = id;
        this.name = name;
        this.hindiName = hindiName;
        this.primaryOccupation = primaryOccupation;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return format("id: %s, name: %2s", getId(), getName());
    }

    @JsonProperty("hindiName")
    public String getHindiName() {
        return hindiName;
    }


    public static PersonSummaryRepresentation createFrom(PersonDetails personDetails) {
        if (personDetails != null) {
            Category primaryOccupation = personDetails.getPrimaryOccupation();
            String primaryOccupationName = (primaryOccupation != null) ? primaryOccupation.getName() : "";
            return new PersonSummaryRepresentation(personDetails.getId(), personDetails.getName(), personDetails.getHindiName(), primaryOccupationName);
        }
        return null;
    }


    @JsonProperty("primaryOccupation")
    public String getPrimaryOccupation() {
        return primaryOccupation;
    }

    public static List<PersonSummaryRepresentation> toPersonSummaries(Set<PersonDetails> people) {
        List<PersonSummaryRepresentation> personSummaryRepresentations = null;
        if(people != null){
            personSummaryRepresentations = new ArrayList<>();
            for (PersonDetails person : people) {
                personSummaryRepresentations.add(PersonSummaryRepresentation.createFrom(person));
            }
        }
        return personSummaryRepresentations;
    }
}

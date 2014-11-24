package org.ajabshahar.platform.resources;

import org.ajabshahar.api.PersonRepresentation;
import org.ajabshahar.platform.models.PersonDetails;

import java.util.List;

public class PersonRepresentationFactory {

    public PersonRepresentation create(PersonDetails person) {
        PersonRepresentation personRepresentation = new PersonRepresentation(person.getFirstName(), person.getMiddleName(), person.getLastName(), person.getCategory());
        return personRepresentation;
    }

    public PeopleRepresentation create(List<PersonDetails> personDetails) {
        PeopleRepresentation peopleRepresentation = new PeopleRepresentation();
        for (PersonDetails person : personDetails){
            peopleRepresentation.add(person);
        }
        return  peopleRepresentation;
    }
}
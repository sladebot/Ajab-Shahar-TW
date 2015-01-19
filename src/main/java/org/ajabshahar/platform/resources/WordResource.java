package org.ajabshahar.platform.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.ajabshahar.api.WordRepresentationFactory;
import org.ajabshahar.core.Words;
import org.ajabshahar.platform.models.Word;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/words")
@Produces(MediaType.APPLICATION_JSON)
public class WordResource {

    private final Words words;
    private final WordRepresentationFactory wordRepresentationFactory;

    public WordResource(Words words, WordRepresentationFactory wordRepresentationFactory) {
        this.words = words;
        this.wordRepresentationFactory = wordRepresentationFactory;
    }


    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWord(String jsonWord) {
        Word word = wordRepresentationFactory.create(jsonWord);
        word = words.create(word);
        return Response.status(200).entity(word.getId()).build();
    }

    @POST
    @Path("/edit")
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWord(String jsonWord) {
        Word word = wordRepresentationFactory.create(jsonWord);
        word = words.update(word);
        return Response.status(200).entity(word.toString()).build();
    }

    @GET
    @UnitOfWork
    public List<Word> listAllWordDetails(@DefaultValue("false") @QueryParam("showOnWordsLandingPage") Boolean showOnWordLandingPage, @DefaultValue("false") @QueryParam("showOnMainLandingPage") Boolean showOnMainLandingPage) {
        return words.findBy(showOnWordLandingPage, showOnMainLandingPage);
    }


    @GET
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public Word getWordById(@QueryParam("id") int wordId) {
        return words.findBy(wordId);
    }


}

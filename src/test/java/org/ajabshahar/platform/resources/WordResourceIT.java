package org.ajabshahar.platform.resources;

import com.google.gson.Gson;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.minidev.json.JSONObject;
import org.ajabshahar.DataSetup;
import org.ajabshahar.api.*;
import org.ajabshahar.platform.PlatformApplication;
import org.ajabshahar.platform.PlatformConfiguration;
import org.ajabshahar.platform.models.Song;
import org.ajabshahar.platform.models.WordIntroduction;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.NewCookie;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class WordResourceIT {

    @ClassRule
    public static final DropwizardAppRule<PlatformConfiguration> RULE =
            new DropwizardAppRule<>(PlatformApplication.class, resourceFilePath("test-config.yaml"));

    private Client client;
    private JdbcDataSource dataSource;
    private JSONObject jsonObject;
    private ArrayList wordIntroductions = new ArrayList();
    private static final String API_TO_EDIT_THE_WORD_WITH_ID_ONE = "http://localhost:%d/api/words/edit?id=1";

    private static String resourceFilePath(String resource) {
        return ClassLoader.getSystemClassLoader().getResource(resource).getFile();
    }

    @Before
    public void setUp() throws Exception {
        client = new Client();
        dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./test");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        jsonObject = new JSONObject();
        jsonObject.put("wordOriginal", "शून्य");
        jsonObject.put("wordTranslation", "Emptiness");
        jsonObject.put("wordTransliteration", "Shoonya");
        jsonObject.put("englishIntroExcerpt", "Shoonya is literally zero ");
        jsonObject.put("hindiIntroExcerpt", "");
        jsonObject.put("diacritic", "");
        jsonObject.put("isRootWord", false);
        jsonObject.put("showOnLandingPage", false);
        jsonObject.put("displayAjabShaharTeam", false);
        jsonObject.put("meaning", "meaning");
        jsonObject.put("wordIntroductions", new ArrayList<>());

        jsonObject.put("relatedWords", new ArrayList<>());
        jsonObject.put("synonyms", new ArrayList<>());
        jsonObject.put("songs", new ArrayList<>());
        jsonObject.put("writers", new ArrayList<>());

        JSONObject jsonWordIntroductions = new JSONObject();
        jsonWordIntroductions.put("contentType", "text");
        jsonWordIntroductions.put("wordIntroEnglish", "intro english");
        jsonWordIntroductions.put("wordIntroHindi", "intro hindi");

        wordIntroductions.add(jsonWordIntroductions);
    }

    @Test
    public void shouldHaveWordIntroduction() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS,
                DataSetup.INSERT_WORD_INTRODUCTION);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse response = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation responseEntity = getWord(response);

        assertEquals(1, responseEntity.getWordIntroductions().size());
    }

    @Test
    public void shouldHaveWordIntroductions() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS,
                DataSetup.INSERT_WORD_INTRODUCTION, DataSetup.INSERT_WORD_INTRODUCTION);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse response = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation responseEntity = getWord(response);

        assertEquals(2, responseEntity.getWordIntroductions().size());
    }

    @Test
    public void shouldHaveWordIntroductionWithContentType() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS,
                DataSetup.INSERT_WORD_INTRODUCTION);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse response = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation responseEntity = getWord(response);

        Set<WordIntroduction> wordIntroductions = responseEntity.getWordIntroductions();

        for (WordIntroduction wordIntroduction : wordIntroductions) {
            assertEquals("text", wordIntroduction.getContentType());
        }
    }

    @Test
    public void shouldHaveWordIntroductionWithOtherContentType() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL,
                DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS,
                DataSetup.INSERT_WORD_INTRODUCTION_WITH_COUPLET_CONTENT_TYPE);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse response = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation responseEntity = getWord(response);

        Set<WordIntroduction> wordIntroductions = responseEntity.getWordIntroductions();

        for (WordIntroduction wordIntroduction : wordIntroductions) {
            assertEquals("couplet", wordIntroduction.getContentType());
        }
    }

    @Test
    public void shouldSaveWordWithOutIntroduction() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        assertThat(wordResponse.getStatus(), is(200));
    }

    @Test
    public void shouldSaveWordWithIntroductions() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        jsonObject.put("wordIntroductions", wordIntroductions);

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        assertThat(wordResponse.getStatus(), is(200));

    }

    @Test
    public void shouldEditWordByAddingIntroductionsForTheFirstTime() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();


        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        jsonObject.put("id", getWord(wordResponse).getId());
        jsonObject.put("wordIntroductions", wordIntroductions);

        ClientResponse wordEditResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        assertThat(wordEditResponse.getStatus(), is(200));

    }

    @Test
    public void shouldSaveReflections() {

        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        PersonSummaryRepresentation speaker = new PersonSummaryRepresentation();
        ReflectionSummaryRepresentation reflectionSummaryRepresentation = new ReflectionSummaryRepresentation(1, "Oh that wonderful song!", speaker, false);
        List<ReflectionSummaryRepresentation> reflections = new ArrayList<>();
        reflections.add(reflectionSummaryRepresentation);
        jsonObject.put("reflections", reflections);

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        WordIntermediateRepresentation word = wordResponse.getEntity(WordIntermediateRepresentation.class);

        assertThat(wordResponse.getStatus(), is(200));
        assertThat(word.getReflections().size(), is(1));
    }

    @Test
    public void shouldGetSelectedReflectionsWithWord() throws Exception {

        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS, DataSetup.INSERT_WORD_REFLECTIONS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse wordResponse = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation word = wordResponse.getEntity(WordIntermediateRepresentation.class);

        assertThat(word.getReflections().size(), is(1));
        assertThat(word.getReflections().get(0).getId(), is(1L));

    }

    @Test
    public void shouldHaveShowAjabShaharTeamTextFlag() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse response = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation responseEntity = getWord(response);

        assertEquals(true, responseEntity.getDisplayAjabShaharTeam());
    }

    @Test
    public void shouldSaveDefaultReflection() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS, DataSetup.INSERT_WORD_REFLECTIONS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        jsonObject.put("defaultReflection", new ReflectionSummaryRepresentation(2, "I hate that word!", null, false));

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);
        WordIntermediateRepresentation word = wordResponse.getEntity(WordIntermediateRepresentation.class);

        wordResponse = httpGet("http://localhost:%d/api/words/edit?id=" + word.getId());
        word = wordResponse.getEntity(WordIntermediateRepresentation.class);

        assertThat(word.getDefaultReflection().getId(), is(2L));
    }

    @Test
    public void shouldEditDefaultReflection() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS, DataSetup.INSERT_WORD_REFLECTIONS);


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        //get existing word
        ClientResponse wordResponse = httpGet("http://localhost:%d/api/words/edit?id=1");
        WordIntermediateRepresentation word = wordResponse.getEntity(WordIntermediateRepresentation.class);
        assertNull(word.getDefaultReflection());

        //change the word - set a default reflection
        word.setDefaultReflection(new ReflectionSummaryRepresentation(2, "I hate that word!", null, false));
        wordResponse = loginAndPost("http://localhost:%d/api/words", word);
        wordResponse = httpGet("http://localhost:%d/api/words/edit?id=1");

        //check the default reflection in DB
        word = wordResponse.getEntity(WordIntermediateRepresentation.class);
        assertThat(word.getDefaultReflection().getId(), is(2L));
    }

    @Test
    public void shouldRemoveDefaultReflection() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS, DataSetup.INSERT_WORD_REFLECTIONS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        //get existing word
        ClientResponse wordResponse = httpGet("http://localhost:%d/api/words/edit?id=2");
        WordIntermediateRepresentation word = wordResponse.getEntity(WordIntermediateRepresentation.class);
        assertEquals(2l, word.getDefaultReflection().getId());

        //change the word - remove a default reflection
        word.setDefaultReflection(null);
        wordResponse = loginAndPost("http://localhost:%d/api/words", word);
        wordResponse = httpGet("http://localhost:%d/api/words/edit?id=2");

        //check the default reflection in DB
        word = wordResponse.getEntity(WordIntermediateRepresentation.class);
        assertNull(word.getDefaultReflection());
    }


    private ClientResponse httpGet(String getUrl) {
        return client.resource(
                String.format(getUrl, RULE.getLocalPort()))
                .header("Content-type", "application/json")
                .get(ClientResponse.class);
    }


    private ClientResponse loginAndPost(String postUrl, Object jsonObj) {
        String userCredentials = "{\"username\":\"admin\",\"password\":\"password\"}";
        ClientResponse response = client.resource(
                String.format("http://localhost:%d/api/login", RULE.getLocalPort())).header("Content-type", "application/json")
                .post(ClientResponse.class, userCredentials);

        NewCookie sessionCookie = geCookie(response);
        return client.resource(
                String.format(postUrl, RULE.getLocalPort()))
                .header("Content-type", "application/json")
                .cookie(sessionCookie)
                .post(ClientResponse.class, jsonObj);
    }

    @Test
    public void shouldSaveWordAlongWithRelatedWords() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse words = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation word = getWord(words);
        List<WordSummaryRepresentation> wordSummaryRepresentations = getWordSummaryRepresentations(word);

        jsonObject.put("relatedWords", wordSummaryRepresentations);

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);
        WordIntermediateRepresentation wordIntermediateRepresentation = getWord(wordResponse);

        assertThat(wordIntermediateRepresentation.getRelatedWords().size(), is(1));
    }

    @Test
    public void shouldSaveWordAlongWithSynonyms() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse words = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);

        WordIntermediateRepresentation word = getWord(words);
        List<WordSummaryRepresentation> wordSummaryRepresentations = getWordSummaryRepresentations(word);

        jsonObject.put("synonyms", wordSummaryRepresentations);

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);
        WordIntermediateRepresentation wordIntermediateRepresentation = getWord(wordResponse);

        assertThat(wordIntermediateRepresentation.getSynonyms().size(), is(1));
        assertThat((long) wordIntermediateRepresentation.getSynonyms().get(0).getId(), is(word.getId()));
    }

    private List<WordSummaryRepresentation> getWordSummaryRepresentations(WordIntermediateRepresentation word) {
        List<WordSummaryRepresentation> wordSummaryRepresentations = new ArrayList<>();
        WordSummaryRepresentation wordSummaryRepresentation = new WordSummaryRepresentation((int) word.getId(), word.getWordOriginal(), word.getWordTranslation(),
                word.getWordTransliteration(), word.getHindiIntroExcerpt(), word.getEnglishIntroExcerpt(), new ArrayList<>(), word.getIsRootWord());
        wordSummaryRepresentations.add(wordSummaryRepresentation);
        return wordSummaryRepresentations;
    }

    @Test
    public void shouldEditRelatedWordsAndSynonyms() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse words = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);
        List<WordSummaryRepresentation> wordSummaryRepresentations = new ArrayList<>();

        WordIntermediateRepresentation word = getWord(words);
        WordSummaryRepresentation wordSummaryRepresentation = new WordSummaryRepresentation((int) word.getId(), word.getWordOriginal(), word.getWordTranslation(),
                word.getWordTransliteration(), word.getHindiIntroExcerpt(), word.getEnglishIntroExcerpt(), new ArrayList<>(), word.getIsRootWord());
        wordSummaryRepresentations.add(wordSummaryRepresentation);

        jsonObject.put("relatedWords",wordSummaryRepresentations);

         words = loginAndPost("http://localhost:%d/api/words", jsonObject);
         word = getWord(words);

        assertThat(word.getRelatedWords().size(), is(1));

        words = httpGet("http://localhost:%d/api/words/edit?id=2");

        word = getWord(words);
        wordSummaryRepresentation = new WordSummaryRepresentation((int) word.getId(), word.getWordOriginal(), word.getWordTranslation(),
                word.getWordTransliteration(), word.getHindiIntroExcerpt(), word.getEnglishIntroExcerpt(), new ArrayList<>(), word.getIsRootWord());
        wordSummaryRepresentations.add(wordSummaryRepresentation);

        word.setRelatedWords(wordSummaryRepresentations);
        word.setSynonyms(wordSummaryRepresentations);

        words = loginAndPost("http://localhost:%d/api/words", word);
        word = getWord(words);

        assertThat(word.getRelatedWords().size(), is(2));
        assertThat(word.getSynonyms().size(), is(2));
    }

    @Test
    public void shouldDeleteRelatedWordsAndSynonyms() {
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL, DataSetup.INSERT_REFLECTIONS, DataSetup.INSERT_WORDS);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        ClientResponse words = httpGet(API_TO_EDIT_THE_WORD_WITH_ID_ONE);
        List<WordSummaryRepresentation> wordSummaryRepresentations = new ArrayList<>();

        WordIntermediateRepresentation word = getWord(words);
        WordSummaryRepresentation wordSummaryRepresentation = new WordSummaryRepresentation((int) word.getId(), word.getWordOriginal(), word.getWordTranslation(),
                word.getWordTransliteration(), word.getHindiIntroExcerpt(), word.getEnglishIntroExcerpt(), new ArrayList<>(), word.getIsRootWord());
        wordSummaryRepresentations.add(wordSummaryRepresentation);

        jsonObject.put("relatedWords",wordSummaryRepresentations);

        words = loginAndPost("http://localhost:%d/api/words", jsonObject);
        word = getWord(words);

        assertThat(word.getRelatedWords().size(), is(1));

        word.setRelatedWords(new ArrayList<>());
        word.setSynonyms(new ArrayList<>());

        words = loginAndPost("http://localhost:%d/api/words", word);
        word = getWord(words);

        assertThat(word.getRelatedWords().size(), is(0));
        assertThat(word.getSynonyms().size(), is(0));

    }

    @Test
    public void shouldBeAbleToSaveASongHavingSingers(){
        Operation operation = Operations.sequenceOf(DataSetup.DELETE_ALL,
                DataSetup.INSERT_CATEGORY,
                DataSetup.INSERT_REFLECTIONS,
                DataSetup.INSERT_PERSON,
                DataSetup.INSERT_SONGS,
                DataSetup.INSERT_SONG_SINGER);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        dbSetup.launch();

        PersonSummaryRepresentation personSummaryRepresentation = new PersonSummaryRepresentation(1, "singerName", "", null);
        List <PersonSummaryRepresentation> personSummaryRepresentations = new ArrayList<>();
        personSummaryRepresentations.add(personSummaryRepresentation);
        SongSummaryRepresentation songSummaryRepresentation = new SongSummaryRepresentation();
        songSummaryRepresentation.setId(1);
        songSummaryRepresentation.setSingers(personSummaryRepresentations);

        jsonObject.put("songs", Arrays.asList(songSummaryRepresentation));

        ClientResponse wordResponse = loginAndPost("http://localhost:%d/api/words", jsonObject);

        WordIntermediateRepresentation responseEntity = getWord(wordResponse);

        WordIntermediateRepresentation wordIntermediateRepresentation = getWord(httpGet("http://localhost:%d/api/words/edit?id=" + responseEntity.getId()));

        SongSummaryRepresentation songs = wordIntermediateRepresentation.getSongs().iterator().next();

        assertEquals("Ravi Das", songs.getSingers().iterator().next().getName());
    }

    private NewCookie geCookie(ClientResponse response) {
        return getCookie(response, "JSESSIONID");
    }

    private NewCookie getCookie(ClientResponse response, String name) {
        NewCookie sessionCookie = null;
        for (NewCookie cookie : response.getCookies()) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                sessionCookie = cookie;
            }
        }
        return sessionCookie;
    }

    private WordIntermediateRepresentation getWord(ClientResponse response) {
        return response.getEntity(WordIntermediateRepresentation.class);
    }
}

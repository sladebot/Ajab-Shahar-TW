package org.ajabshahar.platform.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.sessions.Session;
import org.ajabshahar.api.*;
import org.ajabshahar.core.Songs;
import org.ajabshahar.platform.daos.SongDAO;
import org.ajabshahar.platform.models.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.LinkedHashSet;
import java.util.Set;

@Path("/songs")
@Produces(MediaType.APPLICATION_JSON)
public class SongResource {

    private final static Logger logger = LoggerFactory.getLogger(SongResource.class);
    private final SongDAO songDAO;
    private final SongsRepresentationFactory songsRepresentationFactory;
    private final Songs songs;

    public SongResource(SongDAO songDAO, Songs songs, SongsRepresentationFactory songsRepresentationFactory) {
        this.songDAO = songDAO;
        this.songsRepresentationFactory = songsRepresentationFactory;
        this.songs = songs;
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSong(String jsonSong) {
        Song song = songsRepresentationFactory.create(jsonSong);
        song = songs.save(song);
        return Response.ok().entity(song).build();
    }

    @POST
    @Path("/edit")
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSong(String jsonSong) {
        Song song = songsRepresentationFactory.create(jsonSong);
        songs.update(song);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SongRepresentation getSongById(@PathParam("id") Long id) {
        try {
            Song song = songDAO.findById(id);
            return songsRepresentationFactory.create(song);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GET
    @UnitOfWork
    @Path("/getPublishedSongs")
    public Response getPublishedSongs(@QueryParam("singerId") int singerId, @QueryParam("poetId") int poetId) {
        Set<Song> songList = songs.findBy(singerId, poetId);
        if (songList == null || songList.size() == 0) {
            return Response.status(Status.NO_CONTENT).build();
        }
        SongsRepresentation songsRepresentation = songsRepresentationFactory.createSongsRepresentation(songList);
        return Response.ok(songsRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @UnitOfWork
    @Path("/getPublishedSongs/{id}")
    public Response getPublishedSong(@PathParam("id") int songId) {
        logger.debug("Get song with id: {}", songId);
        Song song = songs.findBy(songId);
        if (song == null) {
            logger.debug("No song with id: {}", songId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        SongRepresentation songRepresentation = songsRepresentationFactory.create(song);
        logger.debug("Details of song with id {}:  {} ", songId, songRepresentation.toString());
        return Response.ok(songRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @UnitOfWork
    @Path("/versions")
    public Response getSongVersions(@QueryParam("id") int songId) {
        Set<Song> songList = songs.getVersions(songId);
        SongsSummaryRepresentation songsSummaryRepresentation = songsRepresentationFactory.create(songList);
        return Response.ok(songsSummaryRepresentation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @UnitOfWork
    public Response getSongs(@Session HttpSession httpSession) {
        Set<Song> songList = songs.findAll();
        SongsSummaryRepresentation songsSummaryRepresentation = songsRepresentationFactory.create(songList);
        return Response.ok(songsSummaryRepresentation, MediaType.APPLICATION_JSON).build();
    }
}

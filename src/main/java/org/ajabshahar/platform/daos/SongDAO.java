package org.ajabshahar.platform.daos;

import io.dropwizard.hibernate.AbstractDAO;
import org.ajabshahar.platform.models.Song;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class SongDAO extends AbstractDAO<Song> {
    private final SessionFactory sessionFactory;

    public SongDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Song findById(Long id) {
        return (Song) sessionFactory.openSession().get(Song.class, id);
    }

    public Song create(Song song) {
        return persist(song);
    }

    public List<Song> findAll() {
        return list(namedQuery("org.ajabshahar.platform.models.Song.findAll"));
    }

    public List<Song> findAllOnLandingPage() {
        return list(namedQuery("org.ajabshahar.platform.models.Song.findAllOnLandingPage").setMaxResults(15));
    }

    public void updateSong(Long id, Song updatableSongData) {
        Song originalSongData = (Song) sessionFactory.openSession().get(Song.class, id);
        originalSongData = invokeAllSetters(originalSongData, updatableSongData);
        sessionFactory.openStatelessSession().update(persist(originalSongData));
    }

    public Song invokeAllSetters(Song originalSongData, Song updatableSongData) {

        originalSongData.setShowOnLandingPage(updatableSongData.getShowOnLandingPage());
        originalSongData.setDuration(updatableSongData.getDuration());
        originalSongData.setYoutubeVideoId(updatableSongData.getYoutubeVideoId());
        originalSongData.setThumbnail_url(updatableSongData.getThumbnail_url());
        originalSongData.setIsAuthoringComplete(updatableSongData.getIsAuthoringComplete());
        originalSongData.setSingers(updatableSongData.getSingers());
        originalSongData.setPoets(updatableSongData.getPoets());
        originalSongData.setSongCategory(updatableSongData.getSongCategory());
        originalSongData.setMediaCategory(updatableSongData.getMediaCategory());
        originalSongData.setTitle(updatableSongData.getTitle());
        originalSongData.setSongTitle(updatableSongData.getSongTitle());
        return originalSongData;
    }


    public List<Song> findSongWithRenditions(Long id) {
        return list(namedQuery("org.ajabshahar.platform.models.Song.findSongWithRenditions").setParameter("id", id));
    }

    public int getCountOfSongsThatStartWith(String letter) {
        return list(namedQuery("org.ajabshahar.platform.models.Song.findAllFilteredBy").setParameter("letter", letter + "%")).size();
    }

    public List<Song> findBy(int singerId, int poetId, int startFrom, String filteredLetter) {
        Session currentSession = sessionFactory.getCurrentSession();
        Criteria findSongs = currentSession.createCriteria(Song.class);

        if (singerId != 0) {
            findSongs.createAlias("singers", "singersAlias");
            findSongs.add(Restrictions.eq("singersAlias.id", Long.valueOf(singerId)));
        }
        if (poetId != 0) {
            findSongs.createAlias("poets", "poetsAlias");
            findSongs.add(Restrictions.eq("poetsAlias.id", Long.valueOf(poetId)));
        }
        if (startFrom != 0) {
            findSongs.setFirstResult(startFrom);
        }
        if (filteredLetter != null) {
            findSongs.createAlias("songTitle", "songTitleAlias");
            findSongs.add(Restrictions.like("songTitleAlias.englishTranslation", filteredLetter, MatchMode.START));
        }
        try {
            return findSongs.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

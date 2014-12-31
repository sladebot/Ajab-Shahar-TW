var contentService = function ($http) {
  var getMainLandingPageThumbnails = function () {
    return $http.get('/api/songs/getPublishedSongs?randomSongsEnabled=true');
  };

  var getSongsLandingPageThumbnails = function () {
    return $http.get('/api/songs/landingPage');
  };

  return {
    getMainLandingPageThumbnails: getMainLandingPageThumbnails,
    getSongsLandingPageThumbnails: getSongsLandingPageThumbnails,
  };
};
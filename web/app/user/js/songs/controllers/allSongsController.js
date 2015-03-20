var allSongsController = function($scope,$window,songsContentService,songMapper){
    $scope.songs=[];
    $scope.allSongs = null;
    $scope.totalFilteredSongs = 0;
    $scope.detailsService = {open:function(id){
//                                          var songId = $scope.getSongId(id);
                                          $window.location.href = '/user/partials/songs/details.html?id='+id;
                                      }};
    $scope.activeLetter = '';
    $scope.scrollIndex = 12;
    $scope.singerNameInFilter = {id:-1};
    $scope.poetNameInFilter = {id:-1};
    $scope.songCount = 0;
    $scope.songsList = [];
    $scope.expandFilter = false;
    var i = 0;

    $scope.toggleExpandFilter = function(){
        $scope.expandFilter = !$scope.expandFilter;
    }

    $scope.getAllSongs = function(){
        songsContentService.getAllSongs().then(function(songsList){
            $scope.songs = songMapper.getThumbnails(songsList.data.songs);
            $scope.songCount = songsList.data.songs.length;
        });
    }

    $scope.songStartsWithComparator = function (actual, expected) {
       if (!$scope.activeLetter && $scope.activeLetter=='') {
          return true;
       }
       return $scope.strStartsWith(actual.englishTransliterationTitle.toUpperCase(),$scope.activeLetter.toUpperCase());
    };

    $scope.loadSongFromRange = function(){
        if($scope.scrollIndex>$scope.songs.length)
            return;
        $scope.scrollIndex += 12;
    }
    $scope.getAllSongs();
};

allSongsApp.controller('allSongsController',['$scope','$window','songsContentService','songMapper',allSongsController]);

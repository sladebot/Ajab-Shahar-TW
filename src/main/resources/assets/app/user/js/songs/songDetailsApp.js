var songDetailsApp = angular.module('songDetailsApp',['thumbnailModule','mediaPlayer','htmlGenerator', 'headerModule','animationModule']);

songDetailsApp.config(resourceUrlWhiteList);
songDetailsApp.factory('songsContentService', ['$http', songsContentService]);
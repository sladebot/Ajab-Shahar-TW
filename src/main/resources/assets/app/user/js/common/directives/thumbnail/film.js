'use strict';

thumbnailModule.directive("film", function() {
    return {
        replace : true,
        restrict: 'E',
        scope: {
            context:'@',
            imgSrc:'@',
            name:'@',
            customStyle:'@',
            overlayId:'@'
        },
        templateUrl:'/user/js/common/templates/thumbnail/film.html',
        controller:function($scope){
            $scope.shouldBeOpen = false;

            $scope.open = function(){
                $scope.shouldBeOpen = true;
            }

            $scope.onClose = function(){
                $scope.shouldBeOpen = false;
            }
        }
    }
});
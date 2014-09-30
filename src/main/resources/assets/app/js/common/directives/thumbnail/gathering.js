'use strict';

thumbnailModule.directive("gatheringWithDetails", function() {
    return {
        restrict: 'E',
        scope: {
            name:'@',
            imgSrc:'@',
            location:'@',
            date:'@',
            customStyle:'@',
            overlayId:'@'
        },
        templateUrl:'js/common/templates/thumbnail/gathering.html',
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
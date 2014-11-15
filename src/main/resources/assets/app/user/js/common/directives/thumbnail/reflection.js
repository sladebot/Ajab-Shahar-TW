'use strict';

thumbnailModule.directive("reflection", function() {
    return {
        replace : true,
        restrict: 'E',
        scope: {
            name:'@',
            imgSrc:'@',
            by:'@',
            customStyle:'@',
            overlayId:'@'
        },
        templateUrl:'/user/js/common/templates/thumbnail/reflection.html',
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
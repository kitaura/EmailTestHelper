angular.module('eth', ['ngResource', 'ngSanitize' , 'ui.bootstrap'])
    .controller('EthController', ['$scope', '$resource', '$http', '$sce', function($scope, $resource, $http, $sce){
    var Message = $resource(
        'messages/:id.:format',
        {id: '@id'},
        {

        }
    );
    // all messages
    $scope.messages = Message.query();

    // delete message
    $scope.deleteMessage = function(id){
        Message.delete(
            {id: id},
            function(){
                $scope.messages = Message.query();
            }
        );
    };

    $scope.selectMessage = function(id){
        $scope.id = id;
        $scope.selectedMessage = Message.get({id: id});

        $http({method: 'GET',url: 'messages/' + id + '.text'})
            .success(function(data){
                $scope.text = data;
            }
        );

       $http({method: 'GET',url: 'messages/' + id + '.source'})
            .success(function(data){
                $scope.source = data;
            }
        );

        $scope.url = $sce.trustAsResourceUrl('messages/' + id + '.html');


    };

    }]);


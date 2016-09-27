angular.module('eth', ['ngResource', 'ngSanitize' , 'ui.bootstrap'])
    .controller('EthController', ['$scope', '$resource', '$http', '$sce', function($scope, $resource, $http, $sce){
    var Message = $resource(
        'messages/:id.:format',
        {id: '@id'},
        {

        }
    );
    // all messages
    var messages = Message.get();
    console.log(messages);
    messages.$promise.then(function() {
        $scope.messages = messages.content;
        // page items
        $scope.totalItems = messages.totalElements;
        $scope.currentPage = 1;
        $scope.maxSize = 10;
    });

    $scope.pageChanged = function() {
        console.log('Page changed to: ' + $scope.currentPage);
        var messages = Message.get({ pageNum: $scope.currentPage - 1 });
        messages.$promise.then(function() {
                $scope.messages = messages.content;
        });
     };


    // delete message
    $scope.deleteMessage = function(id){
        Message.delete(
            {id: id},
            function(){
                $scope.messages = Message.query();
            }
        );
    };

    $scope.search = function(){
        var messages = Message.get({ searchKey: $scope.searchKey });
        messages.$promise.then(function() {
                $scope.messages = messages.content;
            $scope.totalItems = messages.totalElements;
            $scope.currentPage = 1;
            $scope.maxSize = 10;
        });
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


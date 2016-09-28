angular.module('eth', ['ngResource', 'ngSanitize' , 'ui.bootstrap'])
    .factory('MessageService', ['$resource', '$http', function($resource, $http){
        var Message = $resource(
                'messages/:id.:format',
                {id: '@id'},{}
            );
        var httpGet = function(id, format) {
            return $http.get('messages/' + id + '.' + format)
                .then(function(response) {
                return response.data;
            });
         };

        return {
            delete: function(id, action) {
                Message.delete({id: id}, action);
            },
            get: function(id) {
                return Message.get({ id: id});
            },
            page: function(pageNum) {
                return Message.get({ pageNum: pageNum -1});
            },
            search: function(searchKey){
                return Message.get({ searchKey: searchKey });
            },
            getSource: function(id) {
                return httpGet(id, 'source');
             },
             getText: function(id) {
                return httpGet(id, 'text');
             }

        }
    }])

    .controller('EthController', ['$scope', '$sce', 'MessageService', function($scope, $sce, MessageService){

    $scope.currentPage = 1;
    $scope.maxSize = 10;

    var loadMailList = function(pageNum){
        var messages = MessageService.page($scope.currentPage);
        messages.$promise.then(function() {
            $scope.messages = messages.content;
            $scope.totalItems = messages.totalElements;
        });
    };

    loadMailList(1);

    $scope.pageChanged = function() {
        // console.log('Page changed to: ' + $scope.currentPage);
        loadMailList($scope.currentPage);
     };


    // delete message
    $scope.deleteMessage = function(id){
        MessageService.delete(id, function(){loadMailList($scope.currentPage);});
    };

    $scope.search = function(){
        var messages = MessageService.search($scope.searchKey);
        messages.$promise.then(function() {
            $scope.messages = messages.content;
            $scope.totalItems = messages.totalElements;
            $scope.currentPage = 1;
        });
    };

    $scope.selectMessage = function(id){
        $scope.id = id;
        $scope.selectedMessage = MessageService.get(id);

        MessageService.getText(id).then(function(data) {
            $scope.text = data;
        });


        MessageService.getSource(id).then(function(data) {
            $scope.source = data;
        });

        $scope.url = $sce.trustAsResourceUrl('messages/' + id + '.html');


    };

    }]);


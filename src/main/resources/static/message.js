angular.module('eth', ['ngResource'])
    .controller('EthController', ['$scope', '$resource', function($scope, $resource){
    var Message = $resource(
        'messages/:id',
        {id: '@id'},
        {update: {method: 'PUT'}}
    );
    // all messages
    $scope.messages = Message.query();

    // delete message
    $scope.ondelete = function(id){
        Book.delete(
            {id: id},
            function(){
                $scope.messages = Message.query();
            }
        );
    };

    }]);

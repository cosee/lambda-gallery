angular.module('appMaps', ['uiGmapgoogle-maps'])
    .controller('mainCtrl', function ($scope, uiGmapIsReady) {


        var map;

        uiGmapIsReady.promise(1).then(function(instances) {
            instances.forEach(function(inst) {

                console.log("map map map ", inst);
                map = inst.map;
                var uuid = map.uiGmap_id;
                var mapInstanceNumber = inst.instance; // Starts at 1.


                google.maps.event.trigger(map, 'resize');


                setTimeout(updateMarkers, 5000);

            });
        });

        $scope.map = {
            center: {
                latitude: 49.866667,
                longitude: 8.65
            },
            zoom: 15,
            bounds: {},
            events: {
                tilesloaded: function (map) {
                    $scope.$apply(function () {
                        $scope.mapInstance = map;
                    });
                }
            }
        };
        $scope.options = {
            scrollwheel: true
        };

        $scope.randomMarkers = [];

        var since = new Date().getTime();

        var apigClient = apigClientFactory.newClient();


        function updateMarkers() {
            console.log("Updating markers", since);


            apigClient.feedGet({since: since}, {}, {}).then(function (result) {
                var arrayLength = result.data.entries.length;


                for (var i = 0; i < arrayLength; i++) {

                    console.log(result.data.entries[i]);

                    var marker = {
                        latitude: result.data.entries[i].lat,
                        longitude: result.data.entries[i].lon,
                        title: "m" + result.data.entries[i].id,
                        id: result.data.entries[i].id
                    };



                    console.log("Push", marker)
                    $scope.randomMarkers.push(marker);
                }



                if (result.data.entries.length > 0) {
                    since = result.data.entries[0].ts;
                }


                console.log("refreshing ", map);
                map.refresh = true;
                google.maps.event.trigger(map, 'resize');


                setTimeout(updateMarkers, 2000);

            }).catch(function (result) {

                setTimeout(updateMarkers, 2000);

                console.log(result);
            });
        }


    });

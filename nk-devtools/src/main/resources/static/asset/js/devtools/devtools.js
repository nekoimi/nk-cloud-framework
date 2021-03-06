app.controller('generatorController', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    function ($scope, $http, Messager, $modal) {
        let localStorageDataKey = '_nk_dt_d';
        $scope.database = '';
        $scope.schemas = [];
        $scope.tables = [];
        $scope.data = {
            location: '',
            author: '',
            packageName: '',
            moduleName: '',

            schemaName: '',
            tableName: '',
            tablePrefix: '',
            fieldPrefix: '',
            entityName: '',
            onlyGenEntity: false,

            router: {
                module: '',
                info: '',
                version: '',
                prefix: '',
                name: ''
            }
        }

        function loadFromLocalStorage() {
            let data = localStorage.getItem(localStorageDataKey);
            if (data != null) {
                try {
                    $scope.data = JSON.parse(data);
                } catch (e) {
                    localStorage.removeItem(localStorageDataKey);
                    console.error(e);
                }
            }
        }

        function saveToLocalStorage(data) {
            localStorage.removeItem(localStorageDataKey);
            localStorage.setItem(localStorageDataKey, JSON.stringify(data))
        }

        function loadBootstrap() {
            $scope.loading = true;
            loadFromLocalStorage();
            $http.get("generator/env").success(
                function (result, status, headers, config) {
                    $scope.loading = false;
                    if (result.code === 0) {
                        $scope.database = result.data.database;
                        $scope.schemas = result.data.schemas;
                    } else {
                        $scope.error = result.msg;
                    }
                }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = result.msg || status;
            });
        }

        loadBootstrap();

        $scope.onSchemaClink = function (schema) {
            $scope.data.schemaName = schema;
            $http.get(`generator/env/${schema}/tables`).success(
                function (result, status, headers, config) {
                    $scope.loading = false;
                    if (result.code === 0) {
                        $scope.tables = result.data.tables;
                    } else {
                        $scope.error = result.msg;
                    }
                }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = result.msg || status;
            });
        }

        $scope.onTableClick = function (table) {
            $scope.data.tableName = table.name;
        }


        $scope.submitGenerate = function () {
            Messager.confirm("??????", "??????????????????????").then(function () {
                $scope.generating = true;
                var toast = $scope.toaster.wait("????????????...");
                $http.post('generator/generate', $scope.data).success(
                    function (result, status, headers, config) {
                        $scope.generating = false;
                        if (result.code === 0) {
                            saveToLocalStorage($scope.data);
                            toast.doSuccess("????????????");
                        } else {
                            toast.doError("???????????????" + (result.msg || status));
                        }
                    }).error(function (result, status) {
                    $scope.generating = false;
                    toast.doError("???????????????" + (result.msg || status));
                });
            });
        }
    }]);

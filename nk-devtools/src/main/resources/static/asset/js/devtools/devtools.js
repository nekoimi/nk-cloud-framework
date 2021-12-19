app.controller('generatorController', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    function ($scope, $http, Messager, $modal) {
        let localStorageDataKey = '_nk_dt_d';
        $scope.tables = [];
        $scope.data = {
            location: '',
            author: 'nekoimi',
            packageName: '',
            moduleName: '',

            tableName: '',
            tablePrefix: '',
            fieldPrefix: '',
            entityName: '',
            onlyGenEntity: '',

            router: {
                version: '',
                prefix: '',
                name: ''
            }
        }

        function loadFromLocalStorage() {
            let data = localStorage.getItem(localStorageDataKey);
            $scope.data = JSON.parse(data)
        }

        function saveToLocalStorage(data) {
            localStorage.setItem(localStorageDataKey, JSON.stringify(data))
        }

        function loadBootstrap() {
            $scope.loading = true;
            loadFromLocalStorage();
            $http.get("generator/env").success(
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

        loadBootstrap();

        $scope.onTableClick = function (table) {
            $scope.current = table;
            if (table.tableComment) {
                $scope.data.entityName = table.tableComment;
            }
        }

        $scope.parseEnityName = function (table) {
            if (!table) {
                return;
            }
            table = table.toLowerCase();
            if ($scope.data.tablePrefix) {
                table = table.replace($scope.data.tablePrefix, "");
            }
            var names = table.split("_");
            var entityName = [];
            for (var i = 0; i < names.length; i++) {
                var name = names[i];
                if (name.length > 0) {
                    var upperName = name.substring(0, 1).toUpperCase() + name.substring(1, name.length);
                    entityName.push(upperName);
                }
            }
            return entityName.join("");
        }


        $scope.generate = function () {

            Messager.confirm("提示", "确定要生成代码?").then(function () {
                var setting = $scope.data;
                var params = {
                    projectLocation: setting.projectLocation,
                    developer: setting.developer,
                    module: setting.module,
                    entityName: setting.entityName,
                    tableName: $scope.current.tableName,
                    tablePrefix: setting.tablePrefix,
                    packageName: setting.packageName,
                    onlyGenerateEntity: setting.onlyGenerateEntity
                };
                $scope.generating = true;
                var toast = $scope.toaster.wait("正在生成...");
                $http.post('generator/generate', params).success(
                    function (data, status, headers, config) {
                        $scope.generating = false;
                        if (data.success) {
                            /* $scope.dataForm.$setPristine();
                             $scope.dataForm.$setUntouched();*/
                            toast.doSuccess("生成成功");
                            $scope.forward(0);
                        } else {
                            toast.doError("生成失败：" + (result.msg || status));
                        }
                    }).error(function (result, status) {
                    $scope.generating = false;
                    toast.doError("生成失败：" + (result.msg || status));
                });
            });


        }
    }]);

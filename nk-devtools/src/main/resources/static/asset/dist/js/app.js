var app = angular.module('app', [
       	'ngAnimate',
        'ngResource',
        'ui.router',
        'ui.bootstrap',
        'ui.validate',
        'pascalprecht.translate',
        'messager',
        'toaster',
        'hc.util',
        'hc.ui',
        'hc.common'
    ]);
app.config(
	    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
	    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
	        // lazy controller, directive and service
	        app.controller = $controllerProvider.register;
	        app.directive  = $compileProvider.directive;
	        app.filter     = $filterProvider.register;
	        app.factory    = $provide.factory;
	        app.service    = $provide.service;
	        app.constant   = $provide.constant;
	        app.value      = $provide.value;
	    }
	  ])
	  .config(['$translateProvider', function($translateProvider){
	    $translateProvider.useStaticFilesLoader({
	      prefix: 'asset/l10n/',
	      suffix: '.js'
	    });
	    $translateProvider.preferredLanguage('zh_CN');
	  }]).factory('responseObserver', ['$q','$window',function responseObserver($q, $window) {
		    return {
		        'responseError': function(errorResponse) {
		            switch (errorResponse.status) {
		            case 401:
		                $window.location.href = '/login';
		                break;
		            }
		            return $q.reject(errorResponse);
		        }
		    };
		}]).config(['$httpProvider',function($httpProvider){  
		  $httpProvider.interceptors.push('responseObserver');
		  /*$httpProvider.defaults.transformRequest = function(obj){  
			     var params = [];  
			     for(var p in obj){  
			    	 var value = obj[p];
			    	 if(angular.isObject(value)){
			    		 transformObject(value,p,params);
			    	 }else{
			    		 params.push(encodeURIComponent(p) + "=" + encodeURIComponent(angular.isUndefined(value) ? "" : value));   
			    	 }
			     }  
			     return params.join("&");  
			   }; 
		   function transformObject(obj,name,params){
			   for(var pro in obj){
				   var value = obj[pro];
				   var paramName = name + "." + pro;
				   if(angular.isObject(value)){
					   transformObject(value,paramName,params);
				   }else{
					   params.push(encodeURIComponent(paramName) + "=" + encodeURIComponent(angular.isUndefined(value) ? "" : value));  
				   }
			   }
		   }*/
		  
		   $httpProvider.defaults.headers.post = {  
		       // 'Content-Type': 'application/x-www-form-urlencoded',
				'Content-Type': 'application/json',
		        'X-Requested-With' : 'XMLHttpRequest'
		   };  
		   $httpProvider.defaults.headers.get = {  
		        'X-Requested-With' : 'XMLHttpRequest'
		   }; 
	  }]);
app.run(['$rootScope', '$state', '$stateParams','$http','Messager',
	 function ($rootScope,   $state,   $stateParams,$http,Messager) {
	    $rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;        
}])
.config(['$stateProvider', '$urlRouterProvider','$locationProvider',
	function ($stateProvider,   $urlRouterProvider,$locationProvider) {
		  //$locationProvider.html5Mode(true);
      $urlRouterProvider.otherwise('/index');
      $stateProvider.state('/index', {
              url: '/index',
              templateUrl: 'asset/tpl/index.html',
              controller:'IndexCtrl'
          }).state('changePassword', {
	            url: '/index/changePassword',
	            templateUrl: 'asset/tpl/change_password.html',
	            controller : 'ChangePasswordCtrl'
	        }).state('profile', {
	            url: '/index/profile',
	            templateUrl: 'asset/tpl/profile.html',
	            controller : 'ProfileCtrl'
	        })
	}
]);
app.controller('AppCtrl', ['$rootScope','$scope', 'toaster','Messager','$http','$modal','$location','$state','userData','setting','menus',
           function( $rootScope,$scope,toaster,Messager,$http,$modal,$location,$state,userData,setting,menus) {
	$scope.ie9 = /msie 9/.test(window.navigator.userAgent.toLowerCase());
	
    $scope.supportWebkitDirectory = ('webkitdirectory' in HTMLInputElement.prototype);
    $rootScope.toaster = toaster;
	$rootScope.resourceServerUrl = setting.resourceServceUrl + "/";
    $rootScope.staticResourceServerUrl = setting.staticResourceServerUrl + "/";
    //??????????????????
    $rootScope.user = userData.user;
	$rootScope.organization = userData.organization;
	$rootScope.menus = menus;
	
    //??????????????????
    $scope.updateUser = function(){
    	$http.post("/organization/user/set",{
    		name:$scope.user.name,
    		mode:$scope.user.mode,
    		sortField:$scope.user.sortField,
    		sortDesc:$scope.user.sortDesc
    	});
    };
	$rootScope.started = true;
	
}]);

angular.element(document).ready(function() {
	
	loadEnv();
	
	 //??????????????????
    function loadEnv(){
    	$.get("/env").success(function(result){
			if(result.success){
				delete result.success;
				app.value("userData",result.data);
				app.value("setting",{
					staticResourceServerUrl : '',
					resourceServceUrl : ''
				});
				bootstrap();
			}else{
				alert("??????????????????????????????????????????????????????");
			}
		}).error(function(){
			alert("??????????????????????????????????????????????????????");
		});
    }
    
    var menus = [{
    	code : 'organization',
    	name : '????????????',
    	icon : 'fa fa-group',
    	url : 'asset/tpl/organization/organization.html'
    },{
    	code : 'role',
    	name : '????????????',
    	icon : 'fa fa-user',
    	url : 'asset/tpl/role/role.html'
    },{
    	code : 'resource',
    	name : '????????????',
    	icon : 'fa fa-folder',
    	url : 'asset/tpl/resource/resource.html'
    }];
    
    app.config(['$stateProvider', function ($stateProvider, ) {
        menus.forEach(function(menu){
        	$stateProvider.state(menu.code, {
                url: '/' + menu.code,
                templateUrl: menu.url
            });
        });   
    }]);
    
    app.value("menus",menus);
    
    //????????????
    /*function loadSetting(state,stateParam){
    	$.get("/setting").success(function(result){
    		if(result.success){
    			delete result.success ;
				app.value("setting",result);
				bootstrap();
    		}else{
    			alert("????????????????????????????????????????????????????????????");
    		}
    	}).error(function(){
			alert("????????????????????????????????????????????????????????????");
		});
    }*/
    
    
    function bootstrap(){
    	angular.bootstrap(document, ['app']);
    	window.clearInterval(window.preloadInterval);
    }
});
app.controller('SettingsCtrl', [
		'$scope',
		'$http',
		'$state',
		'toaster',
		function($scope, $http, $state, toaster) {
			$http.get("setting/app").success(function(result) {
				if (result.success) {
					/*result.setting.appLogo = app.resourceServceUrl + "/" + result.setting.appLogo;
					result.setting.appIcon = app.resourceServceUrl + "/" + result.setting.appIcon;*/
					$scope.setting = result.setting;
				} else {
					$scope.toaster.pop('error', '??????', "?????????????????????" + data.message);
				}

			}).error(function() {
				$scope.toaster.pop('error', '??????', '?????????????????????' + status.statusCode);
			});

			$scope.logo = {
				operation : "??????LOGO",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};

			$scope.icon = {
				operation : "????????????",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};

			$scope.resource = {
				logo : '',
				icon : ''
			};
			$scope.save = function() {
				$http.post("setting/app", $scope.setting).success(
						function(data, status, headers, config) {
							if (data.success) {
								$scope.settingForm.$setPristine();
								$scope.settingForm.$setUntouched();
								$scope.toaster.pop('success', '??????', "????????????");
							} else {
								$scope.toaster.pop('error', '??????', "???????????????"
										+ data.message);
							}
						}).error(function() {
							$scope.toaster.pop('error', '??????', '?????????????????????' + status.statusCode);
				});
			}
			$scope.selectLogo = function() {
				angular.element("#logoFileInput").click();
			}
			
			$scope.selectImage = function(type) {
				$scope.uploadType = type;
				angular.element("#imageFileInput").click();
			}
			
			$scope.uploadContext = {};
			
			$scope.upload = function() {
				var files = angular.element("#imageFileInput")[0].files;
				var fd = new FormData();
				fd.append("image", files[0]);
				fd.append("field",$scope.uploadType);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.uploading = false;
					if (xhr.status != 200) {
						$scope.uploadResult = {
							success : false,
							message : "?????????????????????????????????" + xhr.status
						}
					}else{
						var result = angular.fromJson(xhr.response);
						$scope.uploadResult = result;
						if (!result.success) {
							$scope.uploadResult.message = "???????????????" + result.message
						} else {
							$scope.setting[$scope.uploadType] = result.resource;
							var img = angular.element("#" + $scope.uploadType + "Image")[0];
							img.src = img.src + "?_dc=" + new Date().getTime();
						}
					}
					$scope.$apply();
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.uploading = false;
					$scope.uploadResult = {
						success : false,
						message : "??????????????????????????????."
					}
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload");
				$scope.uploading = true;
				xhr.setRequestHeader('X-Requested-With','XMLHttpRequest');
				xhr.send(fd);
			}

			$scope.uploadLogo = function() {
				var files = angular.element("#logoFileInput")[0].files;
				var fd = new FormData();
				fd.append("logo", files[0]);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.logo.uploading = false;
					if (xhr.status != 200) {
						$scope.logo.result = {
							success : false,
							message : "?????????????????????????????????" + xhr.status
						}
						return;
					}
					var result = angular.fromJson(xhr.response);
					$scope.logo.result = result;
					if (!result.success) {
						$scope.logo.message = "???????????????" + result.message
					} else {
						var img = angular.element("#appLogo")[0];
						img.src = img.src + "?_dc=" + new Date().getTime();
					}
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.logo.uploading = false;
					$scope.logo.result = {
						success : false,
						message : "??????????????????????????????."
					}
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload/logo");
				$scope.logo.uploading = true;
				xhr.send(fd);
			}

			$scope.selectIcon = function() {
				angular.element("#iconFileInput").click();
			}

			$scope.uploadIcon = function() {
				var files = angular.element("#iconFileInput")[0].files;
				var fd = new FormData();
				fd.append("icon", files[0]);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "????????????";
					if (xhr.status != 200) {
						$scope.icon.uploadError = true;
						$scope.logo.uploadErrorText = "?????????????????????????????????"
								+ xhr.status;
						return;
					}
					var result = angular.fromJson(xhr.response);
					if (!result.success) {

						$scope.icon.uploadError = true;
						$scope.icon.uploadErrorText = "???????????????" + result.message
					} else {
						$scope.icon.uploadError = false;
						$scope.setting.appIcon = result.icon;
						var img = angular.element("#appIcon")[0];
						img.src = img.src + "?_dc=" + new Date().getTime();
					}
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "????????????"
					$scope.icon.uploadError = true;
					$scope.icon.uploadErrorText = "??????????????????????????????.";
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload/icon");
				$scope.icon.uploading = true;
				$scope.icon.operation = "????????????...";
				xhr.send(fd);
			}
		} ]);

app.controller('MappingConfigCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		'toaster',
		function($scope, $http, Messager, $modal, toaster) {

			$scope.status = {
				isopen : false,
				loading : false
			};

			function load() {
				$scope.status.loading = true;
				$http.get("conf/mapping").success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.mappings = data.mappings;
							} else {
								$scope.status.loadError = true;
								$scope.status.loadErrorText = "?????????????????????????????????"
										+ data.message;
								toaster.pop('error', '??????', data.message);
							}
						}).error(function() {
					$scope.status.loading = false;
					$scope.status.loadError = true;
					$scope.status.loadErrorText = "??????????????????????????????";
					toaster.pop('error', '??????', '???????????????' + status.statusCode);
				});
			}

			load();

			$http.get("conf/category").success(function(result) {
				$scope.categories = result;
				$scope.categories.splice(0,0,{
					name : '??????',
					value : ''
				});
			});
			
			$scope.clearSelected = function() {
				if ($scope.selected) {
					$scope.selected.selected = false;
					$scope.selected = null;
				}
			}

			$scope.setSelected = function(storage) {
				if ($scope.selected && $scope.selected != storage) {
					$scope.selected.selected = false;
				}
				if (storage.selected !== true) {
					storage.selected = true;
					$scope.selected = storage;
				} else {
					storage.selected = false;
					$scope.selected = null;
				}
			}

			$scope.remove = function() {
				if (!$scope.selected)
					return;
				var url = "conf/mapping/delete";
				Messager.confirm("??????", "????????????????").then(
						function(result) {
							$http.post(url, {
								id : $scope.selected.id
							}).success(
									function(result, status, headers, config) {
										if (result.success) {
											load();
										} else {
											$scope.toaster.pop('error', '??????', '???????????????'
													+ result.message);
										}
									}).error(function(result) {
										$scope.toaster.pop('error', '??????', '???????????????????????????');
							});
						});
			}

			$scope.edit = function() {
				if (!$scope.selected)
					return;
				open({
					tpl : 'asset/tpl/app/mapping_form.html',
					ctrl : 'MappingConfigFormCtrl',
					id : $scope.selected.id
				});
			}

			$scope.add = function() {
				open({
					tpl : 'asset/tpl/app/mapping_form.html',
					ctrl : 'MappingConfigFormCtrl'
				});
			}

			function open(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						id : function() {
							return config.id;
						}
					}
				});

				modalInstance.result.then(function() {
					load();
				}, function() {

				});
			}
			
			$scope.condition = {
				extensions : '',
				category : undefined
			}
			
			$scope.search = function(mapping){
				if($scope.condition.extensions.length == 0 && !$scope.condition.category){
					return true;
				}
				
				var matchExt = false;
				if($scope.condition.extensions.length > 0 ){
					var extensions = $scope.condition.extensions.split(",") , len = extensions.length;
					for(var i = 0;i < len ; i ++){
						if(extensions[i].length > 0 && mapping.extensions.indexOf(extensions[i]) >= 0){
							matchExt = true;
						} 
					}
				}else{
					matchExt = true;
				}
				var matchCate = (!$scope.condition.category || mapping.category == $scope.condition.category );
				
				return matchCate && matchExt;
			}
		} ]);

app.controller('MappingConfigFormCtrl', [
		'$scope',
		'$http',
		'id',
		'$modalInstance',
		'Messager',
		function($scope, $http, id, $modalInstance, Messager) {
			$scope.icon = {
				operation : "????????????",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};
			
			$http.get("conf/category").success(function(result) {
				$scope.categories = result;
			});
			
			$scope.mapping = {};
			$scope.operation = '??????'
			if (id != null) {
				$http.get("conf/mapping/" + id).success(function(result) {
					$scope.mapping = result.mapping;
				});
				$scope.operation = '??????';
			}

			$scope.selectIcon = function() {
				$("#iconFileInput").click();
			}
			
			$scope.save = function() {
				var url = 'conf/mapping/save';
				if (id != null) {
					url = 'conf/mapping/update';
				}
				$http.post(url, $scope.mapping).success(function(result) {
					if (result.success) {
						$modalInstance.close();
					} else {
						$scope.result = result;
					}
				}).error(function(data,status) {
					$scope.result = {
						success : false,
						message : '????????????:' + status
					};
				});
			}
			
			$scope.uploadIcon = function() {
				var files = angular.element("#iconFileInput")[0].files;
				var fd = new FormData();
				fd.append("icon", files[0]);
				if($scope.mapping.icon){
					fd.append("name", $scope.mapping.icon);
				}
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "????????????";
					if (xhr.status != 200) {
						$scope.icon.uploadError = true;
						$scope.icon.uploadErrorText = "?????????????????????????????????" + xhr.status;
						return;
					}else{
						var result = angular.fromJson(xhr.response);
						if (!result.success) {
							$scope.icon.uploadError = true;
							$scope.icon.uploadErrorText = "???????????????" + result.message;
						} else {
							$scope.icon.uploadError = false;
							$scope.mapping.icon = result.icon;
							var img = angular.element("#mappingIcon")[0];
							img.src = img.src + "?_dc=" + new Date().getTime();
							$scope.mappingConfigForm.$dirty = true;
						}
					}
					$scope.$apply();
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "????????????"
					$scope.icon.uploadError = true;
					$scope.icon.uploadErrorText = "??????????????????????????????.";
					$scope.$apply();
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/conf/mapping/upload/icon");
				$scope.icon.uploading = true;
				$scope.icon.uploadError = false;
				$scope.icon.operation = "????????????...";
				$scope.$apply();
				xhr.send(fd);
			}

			$scope.cancel = function() {
				if ($scope.mappingConfigForm.$dirty) {
					Messager.confirm("??????", "???????????????????????????????????????????").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);

app.controller('ChangePasswordCtrl', ['$scope','$http', function ($scope,$http) {
      $scope.check = function(){
		    var modes = 0;
		    //????????????????????????????????????
		    if ($scope.password.length < 1) return modes;
		    if (/\d/.test($scope.password)) modes++; //??????
		    if (/[a-z]/.test($scope.password)) modes++; //??????
		    if (/[A-Z]/.test($scope.password)) modes++; //??????  
		    if (/\W/.test($scope.password)) modes++; //????????????
		   $scope.strength = modes;
      };
      
      $scope.submit = function(){
    	  $http.post('/organization/user/change/password',{
    		  password : $scope.password,
    		  oldPassword:$scope.oldPassword
    	  }).success(function(result){
    		  if(result.success){
    			  $scope.password = null;
    			  $scope.oldPassword = null;
    			  $scope.confirmPassword = null;
    			  $scope.success = true;
    			  $scope.cause = null;
    			  $scope.user.changePassword = false;
    		  }else{
    			  $scope.cause = result.message;
    			  $scope.success = false;
    		  }
    	  }).error(function(result,status){
    		  $scope.error  = result.message || status;
    		  $scope.success = false;
    	  });
      };
  }]);
app.controller('IndexCtrl', [
		'$scope',
		'$http',
		'$state',
		'toaster',function($scope, $http, $state, toaster) {
			/*$scope.report = {
				sumOfSize : '-',
				countOfFile : '-'
			}
			$http.get("/statictis/report").success(function(result){
				if(result.success){
					$scope.report = result.report
				}else{
					toaster.error("????????????????????????");
				}
			}).error(function(){
				toaster.error("????????????????????????");
			});*/
		}]);
app.controller('OrganizationCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		function($scope, $http, Messager, $modal) {
			
			$scope.tree = {};
			
			$scope.status = {};
			
			$scope.onSelectionChange = function(selection){
				$scope.current = selection;
				$scope.loadUser();
			}
			
			$scope.onTreeInit = function(){
				var root = $scope.tree.getRoot();
				$scope.tree.select(root.children[0]);
			}
			
			$scope.onTreeOptionClick = function(node,event){
				event.stopPropagation();
				var menu = angular.element("#departmentMenu");
				menu.css({
					top : event.pageY,
					left : event.pageX
				})
				menu.show();
				$scope.selected = node;
			}	
			
			function hideMenu(event){
				//var menu = $(event.target).closest(".dropdown-menu");
				var contextmenu = angular.element("#departmentMenu");
				//if(menu.length == 0){
					contextmenu.hide();
					//delete $scope.selected;
				//}else if(menu[0] != contextmenu[0]){
					//contextmenu.hide();
				//}
			}
				
			$(window).on("click",function(event){
				hideMenu(event);
			});
			
			$scope.$on("$destroy",function(){
				$(window).unbind("click",hideMenu);
			});
			
			
			$scope.addDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						parent : selected
					},
					success : function(){
						$scope.tree.reload(selected);
					}
				});
			}
			
			$scope.editDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						id : selected.id,
						parent : selected.$parent
					},
					success : function(department){
						var node = $scope.tree.get(department.id);
						console.log(node);
						node.name = department.name;
					}
				});
			}
			
			$scope.moveDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_move.html',
					ctrl : 'DepartmentMoveCtrl',
					data : {
						department : selected,
						newParent : selected.$parent
					},
					success : function(result){
						var oldNode = $scope.tree.get(result.oldParent.id);
						$scope.tree.reload(oldNode);
						var newNode = $scope.tree.get(result.newParent);
						$scope.tree.reload(newNode);
						//var node = $scope.tree.get(department.id);
						//node.name = department.name;
					}
				});
			}
			
			$scope.deleteDepartment = function(){
				var selected = $scope.selected;
				var url = "/tenant/organization/delete?id=" + selected.id;
				Messager.confirm("??????", "????????????????").then(
					function(result) {
						$http.post(url).success(
							function(result, status, headers, config) {
								if (result.success) {
									$scope.tree.remove(selected);
								} else {
									$scope.toaster.error('???????????????'+ result.msg);
								}
							}).error(function(result) {
								$scope.toaster.error('???????????????????????????');
						});
					});
			}
			
			
			$scope.loadUser = function(){
				$scope.status.loading = true;
				$scope.$broadcast("beforeLoadUsers");
				$scope.users = null;
				$http.get("/tenant/user/listByDept",{
					params : {
						deptId : $scope.current.id
					}
				}).success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.users = data.data;
							} else {
								$scope.status.error =  "???????????????????????????" + data.message;
							}
						}).error(function(data,status) {
						$scope.status.loading = false;
						$scope.status.error =  "???????????????????????????" + (data.message || status);
				});
			}
			
			$scope.open = function(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						data : function() {
							return config.data;
						}
					}
				});

				modalInstance.result.then(function(result) {
					if(angular.isFunction(config.success)){
						config.success(result);
					}
					//load();
				}, function() {

				});
			}
}]);

app.controller('DepartmentCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.showResult = false;
			$scope.operation = '??????';
			$scope.department = {};
			if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
				});
				$scope.operation = '??????';
			}
			$scope.parent = data.parent;
			
			$scope.save = function() {
				var url = '/tenant/organization/save';
				/*if ($scope.department.id) {
					url = '/organization/department/update';
				}*/
				/*if($scope.department.parent){
					$scope.department.parent = $scope.department.parent.id;
				}*/
				if($scope.parent){
					$scope.department.pid = $scope.parent.id;
				}
				$http.post(url, $scope.department).success(function(result) {
					if (result.success) {
						$modalInstance.close($scope.department);
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "????????????:" + data.msg || status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.departmentForm.$dirty) {
					Messager.confirm("??????", "???????????????????????????????????????????").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);
//????????????
app.controller('DepartmentMoveCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.operation = '??????';
			$scope.invalid = false;
			$scope.changed = false;
			$scope.treeConfig = {
				'load-url' : '/tenant/organization/list',
				'load-asyn' : true,
				'open-folder-icon':'fa fa-users',
			    'folder-icon':'fa fa-users',
			    'leaf-icon':'fa fa-users',
			}
			
			$scope.department = data.department;
			$scope.newParent = data.newParent;
			/*if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
					$scope.newParent = result.data.parent;
				});
			}*/
			
			$scope.onChange = function(node){
				$scope.changed = true;
				var id = $scope.department.id;
				var current = node;
				$scope.invalid = false;
				while(current && !current.$root){
					if(current.id == $scope.department.id){
						$scope.invalid = true;
						break;
					}
					current = current.$parent;
				}
				/*if(node.path.indexOf(id) >= 0){
					$scope.invalid = true;
				}else{
					$scope.invalid = false;
				}*/
			}
			
			$scope.save = function() {
				var url = '/tenant/organization/move';
				$http.post(url, {
					id : $scope.department.id,
					f : $scope.id
				}).success(function(result) {
					if (result.success) {
						$modalInstance.close({
							oldParent : $scope.department.pid,
							newParent : $scope.newParent
						});
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "????????????:" + status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.changed) {
					Messager.confirm("??????", "???????????????????????????????????????????").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
} ]);
app.controller('ProfileCtrl', ['$scope','$http', function ($scope,$http) {
	  //$scope.sortType = $scope.user.sortDesc === true ? 'desc' : 'asc';
      $scope.save = function(){
    	  //$scope.user.sortDesc = $scope.sortType == 'desc' ? true : false;
    	  var toast = $scope.toaster.wait("????????????");
    	  $http.post("/organization/user/set",{
    		  	avatar:$scope.user.avatar,
				name:$scope.user.name,
				mode:$scope.user.mode,
				sortField:$scope.user.sortField,
				sortDesc:$scope.user.sortDesc
			}).success(function(result){
				if(result.success){
					$scope.userSettingForm.$setPristine();
     				$scope.userSettingForm.$setUntouched();
					toast.doSuccess("?????????????????????")
				}else{
					toast.doError("???????????????" + result.message);
				}
			}).error(function(result,status){
				toast.doError("???????????????" + (result.message || status));
			});
      }
      
      $scope.selectAvatar = function() {
			angular.element("#avatarInput").click();
		}
      
      $scope.upload = function() {
			var files = angular.element("#avatarInput")[0].files;
			var fd = new FormData();
			fd.append("avatar", files[0]);
			var xhr = new XMLHttpRequest();
			xhr.onload = function(event) {
				$scope.uploading = false;
				if (xhr.status != 200) {
					$scope.result = {
						success : false,
						message : "?????????????????????????????????" + xhr.status
					}
				}else{
					var result = angular.fromJson(xhr.response);
					$scope.result = result;
					if (!result.success) {
						$scope.message = "???????????????" + result.message
					} else {
						$scope.user.avatar = result.avatar;
						var avatarImg = angular.element(".avatar-img");
						avatarImg.attr("src",avatarImg.attr("src") + "?_dc=" +  new Date().getTime());
					}
				}
				$scope.$apply();
			};
			xhr.onerror = function(response) {
				$scope.uploading = false;
				$scope.result = {
					success : false,
					message : "?????????????????????????????????" + xhr.status
				}
			};
			xhr.open("POST", "/organization/user/upload/avatar");
			xhr.setRequestHeader('X-Requested-With','XMLHttpRequest');
			$scope.uploading = true;
			xhr.send(fd);
		}
  }]);
app.controller('ResourceCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		function($scope, $http, Messager, $modal) {
			
			$scope.tree = {};
			
			$scope.status = {};
			
			$scope.onSelectionChange = function(selection){
				if(!selection.$root){
					$scope.parent = {};
					$scope.selected = selection;
					$scope.current = {
						id : selection.id,
						code : selection.code,
						name : selection.name,
						url : selection.url,
						loadType : selection.loadType,
						isMenu : selection.isMenu,
						description : selection.description,
						icon : selection.icon
					};
					if(selection.$parent && !selection.$parent.$root){
						$scope.parent = selection.$parent;
					}
					$scope.origin = angular.copy($scope.current);
				}
				//$scope.loadUser();
			}
			
			$scope.onTreeInit = function(){
				var root = $scope.tree.getRoot();
				//$scope.tree.select(root.children[0]);
			}
			
			$scope.onTreeOptionClick = function(node,event){
				event.stopPropagation();
				var menu = angular.element("#contextmenu");
				menu.css({
					top : event.pageY,
					left : event.pageX
				})
				menu.show();
				$scope.selected = node;
			}	
			
			function hideMenu(event){
				//var menu = $(event.target).closest(".dropdown-menu");
				var contextmenu = angular.element("#contextmenu");
				//if(menu.length == 0){
					contextmenu.hide();
					//delete $scope.selected;
				//}else if(menu[0] != contextmenu[0]){
					//contextmenu.hide();
				//}
			}
				
			$(window).on("click",function(event){
				hideMenu(event);
			});
			
			$scope.$on("$destroy",function(){
				$(window).unbind("click",hideMenu);
			});
			
			$scope.reset = function(){
				$scope.current = angular.copy($scope.origin);
				$scope.resourceForm.$setPristine();
				$scope.resourceForm.$setUntouched();
			}
			
			$scope.addResouce = function() {
				var selected = $scope.selected;
				$scope.current = {
						loadType:"2",
						isMenu:"1",
						name : "<????????????>"
				};
				$scope.parent = {};
				if(selected && !selected.$root){
					$scope.parent = selected;
				}
				/*$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						parent : selected
					},
					success : function(){
						$scope.tree.reload(selected);
					}
				});*/
			}
			
			$scope.save = function(){
				var toast = $scope.toaster.wait("????????????...");
				if($scope.parent){
					$scope.current.pid = $scope.parent.id;
				}
				$http.post("/tenant/resource/save",$scope.current).success(
						function(data, status, headers, config) {
							if (data.success) {
								//??????
								if($scope.current.id){
									$scope.selected.icon = $scope.current.icon;
									$scope.selected.url = $scope.current.url;
									$scope.selected.loadType = $scope.current.loadType;
									$scope.selected.isMenu = $scope.current.isMenu;
									$scope.selected.description = $scope.current.description;
									$scope.selected.name = $scope.current.name;
								}else{
									//??????
									$scope.tree.reload($scope.selected);
									$scope.tree.select($scope.selected);
								}
								
								$scope.resourceForm.$setPristine();
			     				$scope.resourceForm.$setUntouched();
			     				toast.doSuccess("????????????");
							} else {
								toast.doError("???????????????" + (result.message || status));
							}
						}).error(function(result,status) {
							toast.doError("???????????????" + (result.message || status));
					});
			}
			
			$scope.moveResource = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/resource/resource_move.html',
					ctrl : 'ResourceMoveCtrl',
					data : {
						department : selected,
						newParent : selected.$parent
					},
					success : function(result){
						console.log(result);
						var oldNode = $scope.tree.get(result.oldParent);
						$scope.tree.reload(oldNode);
						var newNode = $scope.tree.get(result.newParent);
						$scope.tree.reload(newNode);
						//var node = $scope.tree.get(department.id);
						//node.name = department.name;
					}
				});
			}
			
			$scope.deleteResource = function(){
				var selected = $scope.selected;
				var url = "/tenant/resource/delete?id=" + selected.id;
				Messager.confirm("??????", "?????????????????????????????????????????????????").then(
					function(result) {
						var toast = $scope.toaster.wait("????????????...");
						$http.post(url).success(
							function(result, status, headers, config) {
								if (result.success) {
									$scope.tree.remove($scope.selected);
								} else {
									toast.doError('???????????????'+ result.msg);
								}
							}).error(function(result,status) {
								toast.doError('????????????:' + (result.msg || status));
						});
					});
			}
			
			
			$scope.loadUser = function(){
				$scope.status.loading = true;
				$scope.$broadcast("beforeLoadUsers");
				$scope.users = null;
				$http.get("/tenant/user/listByDept",{
					params : {
						deptId : $scope.current.id
					}
				}).success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.users = data.data;
							} else {
								$scope.status.error =  "???????????????????????????" + data.message;
							}
						}).error(function(data,status) {
						$scope.status.loading = false;
						$scope.status.error =  "???????????????????????????" + (data.message || status);
				});
			}
			
			$scope.open = function(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						data : function() {
							return config.data;
						}
					}
				});

				modalInstance.result.then(function(result) {
					if(angular.isFunction(config.success)){
						config.success(result);
					}
					//load();
				}, function() {

				});
			}
}]);
//????????????
app.controller('ResourceMoveCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.operation = '??????';
			$scope.invalid = false;
			$scope.changed = false;
			$scope.treeConfig = {
				'load-url' : '/tenant/resource/list',
				'load-asyn' : true,
				'open-folder-icon':'fa fa-users',
			    'folder-icon':'fa fa-users',
			    'leaf-icon':'fa fa-users',
			}
			
			$scope.resource = data.department;
			$scope.newParent = data.newParent;
			/*if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
					$scope.newParent = result.data.parent;
				});
			}*/
			
			$scope.onChange = function(node){
				$scope.changed = true;
				var id = $scope.resource.id;
				var current = node;
				$scope.invalid = false;
				while(current && !current.$root){
					if(current.id == $scope.resource.id || current.id == $scope.resource.pid){
						$scope.invalid = true;
						break;
					}
					current = current.$parent;
				}
				/*if(node.path.indexOf(id) >= 0){
					$scope.invalid = true;
				}else{
					$scope.invalid = false;
				}*/
			}
			
			$scope.save = function() {
				var url = '/tenant/resource/move';
				$http.post(url, {
					id : $scope.resource.id,
					pid : $scope.newParent.id
				}).success(function(result) {
					if (result.success) {
						$modalInstance.close({
							oldParent : $scope.resource.pid,
							newParent : $scope.newParent.id
						});
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "????????????:" + status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.changed) {
					Messager.confirm("??????", "???????????????????????????????????????????").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
} ]);

app.controller('RoleCtrl', [
       		'$scope',
       		'$http',
       		'Messager',
       		'$modal',
       		'toaster',
       		function($scope, $http, Messager, $modal, toaster) {
       			function load() {
       				$scope.loading = true;
       				$http.get("tenant/role/list").success(
       						function(result, status, headers, config) {
       							$scope.loading = false;
       							if (result.success) {
       								$scope.roles = result.data;
       							} else {
       								$scope.error =  result.message;
       							}
       						}).error(function(result,status) {
       						$scope.loading = false;
       						$scope.error =  result.message || status;
       				});
       			}

       			load();

       			$scope.remove = function(role) {
       				var url = "/tenant/role/delete";
       				Messager.confirm("??????", "???????????????????????????????????????????????????????????????????").then(function(result) {
       					var toast = $scope.toaster.wait("????????????...");
       					$http.post(url, {
       						id : role.id
       					}).success(function(result, status, headers, config) {
       						if(result.success){
       							toast.doSuccess("?????????????????????");
       							load();
       						}else{
       							toast.doError("???????????????" + result.message);
       						}
       					}).error(function(result,status){
       						toast.doError("???????????????" + (result.message || status));
       					});
       				});
       			}

       			$scope.edit = function(role) {
       				open({
       					tpl : 'asset/tpl/role/role_form.html',
       					ctrl : 'RoleFormCtrl',
       					role : role
       				});
       			}

       			$scope.add = function() {
       				open({
       					tpl : 'asset/tpl/role/role_form.html',
       					ctrl : 'RoleFormCtrl'
       				});
       			}

       			function open(config) {
       				var modalInstance = $modal.open({
       					templateUrl : config.tpl,
       					controller : config.ctrl,
       					backdrop : 'static',
       					resolve : {
       						role : function() {
       							return config.role;
       						}
       					}
       				});

       				modalInstance.result.then(function() {
       					load();
       				}, function() {
       					
       				});
       			}
       		} ]);

app.controller('RoleFormCtrl', [
		'$scope',
		'$http',
		'role',
		'$modalInstance',
		'Messager',
		function($scope, $http, role, $modalInstance, Messager) {
			
			
			$scope.role = role;
			$scope.operation = role ? '??????' : '??????'
			$scope.save = function() {
				var url = '/tenant/role/save';
				if(role){
					url = '/tenant/role/update';
				}
				var toast = $scope.toaster.wait("????????????...");
				$http.post(url, $scope.role).success(function(result) {
					$scope.saving = false;
					if (result.success) {
						toast.doSuccess("????????????");
						$modalInstance.close();
					} else {
						toast.doError("???????????????" + result.message);
					}
				}).error(function(result,status) {
					$scope.saving = false;
					toast.doError("???????????????" + (result.message || status) );
				});
			}
			
			$scope.cancel = function() {
				if ($scope.roleForm.$dirty) {
					Messager.confirm("??????", "???????????????????????????????????????????").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);

app.controller('UserCtrl', [
      		'$scope',
      		'$http',
      		'Messager',
      		'$modal',
      		function($scope, $http, Messager, $modal) {
      			
      			$scope.$on('beforeLoadUsers',function(){
      				$scope.selected = null;
      			});
      			
      			$scope.select = function(user) {
    				if ($scope.selected && $scope.selected != user) {
    					$scope.selected.selected = false;
    				}
    				if (user.selected !== true) {
    					user.selected = true;
    					$scope.selected = user;
    				} else {
    					user.selected = false;
    					$scope.selected = null;
    				}
    			}
      			
      			$scope.addUser = function() {
      				$scope.open({
      					tpl : 'asset/tpl/organization/user_form.html',
      					ctrl : 'UserFormCtrl',
      					data : {
      						department : $scope.current
      					},
      					success : function(){
      						$scope.loadUser();
      					}
      				});
      			}
      			
      			$scope.operation = function(type,user,event){
      				event.stopPropagation();
      				user.executing = true;
    				var url = '/tenant/user/' + type + "?id=" + user.id;
    				var toast = $scope.toaster.wait("????????????...");
    				$http.post(url).success(function(result) {
    					user.executing = false;
    					if (result.success) {
    						toast.doSuccess("????????????");
    						user.status = result.msg;
    					} else {
    						toast.doError('???????????????' + result.message);
    					}
    				}).error(function (data, status) {
    					user.executing = false;
    					toast.doError('???????????????' + (data.message || status));
    				});
      			}
      			
      			$scope.reset = function(event){
      				Messager.confirm("??????","???????????????????????????????????????????????????????").then(function(){
      					var user = $scope.selected;
      					user.executing = true;
        				var url = '/tenant/user/resetPassword';
        				var toast = $scope.toaster.wait("??????????????????...");
        				$http.post(url + '?id=' + user.id).success(function(result) {
        					user.executing = false;
        					if (result.success) {
        						toast.doSuccess("??????????????????");
        					} else {
        						toast.doError("?????????????????????" + result.message);
        					}
        				}).error(function (data, status, headers, config, statusText) {
        					user.executing = false;
        					toast.doError("?????????????????????" + (data.message || status));
        				});
      				});
      			}
      			
      			$scope.disable = function(user,event){
      				Messager.confirm("??????","??????????????????????").then(function(){
      					$scope.operation('disable',user,event);
      				});
      			}
      			
      			$scope.enable = function(user,event){
      				Messager.confirm("??????","??????????????????????").then(function(){
      					$scope.operation('enable',user,event);
      				});
      			}
      			
      			/*$scope.lock = function(user,event){
      				Messager.confirm("??????","??????????????????????").then(function(){
      					$scope.operation('lock',user,event);
      				});
      			}
      			$scope.unlock = function(user,event){
      				Messager.confirm("??????","??????????????????????").then(function(){
      					$scope.operation('unlock',user,event);
      				});
      			}*/
      			
      			$scope.move = function() {
    				var selected = $scope.selected;
    				$scope.open({
    					tpl : 'asset/tpl/organization/user_move.html',
    					ctrl : 'UserMoveCtrl',
    					data : {
    						user : $scope.selected,
    						department : $scope.current
    					},
    					success : function(result){
    						$scope.toaster.pop('success','??????????????????');
    						$scope.loadUser();
    					}
    				});
    			}   
      			
      			
      			
      			$scope.transferAdmin = function(user) {
      				  if($scope.selected.id == $scope.user.id){
      					  Messager.alert("??????","?????????????????????");
      					  return ;
      				  }
              		  Messager.confirm("??????","??????????????????????????????????????????????????????????????????????????????").then(function(){
              			  if(!$scope.selected) return;
                  		  var params = {
                  			 id:$scope.selected.id
                  		  }
                  		  var toast = $scope.toaster.wait('??????????????????...');
                  		  $scope.saving = true;
                  		  $http.post('/organization/user/admin/transfer',params).success(function(result){
                  			  $scope.saving = false;
                  			  if(result.success){
                  				toast.doClose();
                  				Messager.alert("??????","??????????????????").then(function(){
                  					window.location.href = "/logout";
                  				});
                  			  }else{
                  				  toast.doError("?????????????????????" + result.message);
                  				  //$scope.toaster.error('???????????????' + result.message);
                  			  }
                  		  }).error(function(result,status){
                  			  $scope.saving = false;
                  			  toast.doError("?????????????????????" + (result.message || status));
                  		  });
              		  });
    			}
      			
      			
      			$scope.editUser = function() {
      				var selected = $scope.selected;
      				$scope.open({
      					tpl : 'asset/tpl/organization/user_form.html',
      					ctrl : 'UserFormCtrl',
      					data : {
      						id : selected.id,
      						department : $scope.current
      					},
      					success : function(department){
      						$scope.loadUser();
      					}
      				});
      			}
      }]);


//????????????
app.controller('UserMoveCtrl', [
          		'$scope',
          		'$http',
          		'data',
          		'$modalInstance',
          		'Messager',
          		function($scope, $http, data, $modalInstance, Messager) {
          			$scope.operation = '??????';
          			$scope.treeConfig = {
          				'load-url' : '/tenant/organization/list',
          				'load-asyn' : true,
          				'open-folder-icon':'fa fa-users',
          			    'folder-icon':'fa fa-users',
          			    'leaf-icon':'fa fa-users',
          			}
          			
          			$scope.user = data.user;
          			$scope.department = data.department;
          			
          			$scope.onChange = function(node){
          				$scope.changed = true;
          			}
          			
          			$scope.save = function() {
          				var url = '/tenant/user/setDept';
          				$http.post(url, {
          					id : $scope.user.id,
          					deptId : $scope.department.id
          				}).success(function(result) {
          					if (result.success) {
          						$modalInstance.close();
          					} else {
          						$scope.result = result;
          					}
          				}).error(function (data, status, headers, config, statusText) {
          					$scope.result = {
          						success : false,
          						message : "????????????:" + status
          					}
          				});
          			}

          			$scope.cancel = function() {
          				if ($scope.changed) {
          					Messager.confirm("??????", "???????????????????????????????????????????").then(
          							function(result) {
          								$modalInstance.dismiss('cancel');
          							}, function() {
          							});
          				} else {
          					$modalInstance.dismiss('cancel');
          				}
          			}
          } ]);


//????????????
app.controller('UserFormCtrl', [
  		'$scope',
  		'$http',
  		'data',
  		'$modalInstance',
  		'Messager',
  		function($scope, $http, data, $modalInstance, Messager) {
  			$scope.operation = '??????';
  			$scope.user = {};
  			if (data.id != null) {
  				$http.get("/tenant/user/get?id=" + data.id).success(function(result) {
  					$scope.user = result.data;
  					/*if(!$scope.user.department){
  						delete $scope.user.department;
  					}*/
  				});
  				$scope.operation = '??????';
  			}
  			$scope.department = data.department;
  			$scope.user.deptId = $scope.department.id;
  			$scope.save = function() {
  				$scope.saving = true;
  				var url = '/tenant/user/save';
  				if ($scope.user.id) {
  					url = '/tenant/user/update';
  				}
  				$http.post(url, $scope.user).success(function(result) {
  					$scope.saving = false;
  					if (result.success) {
  						$modalInstance.close($scope.user);
  					} else {
  						$scope.result = result;
  					}
  				}).error(function (data, status, headers, config, statusText) {
  					$scope.saving = false;
  					$scope.result = {
  						success : false,
  						message : "????????????:" + status
  					}
  				});
  			}

  			$scope.cancel = function() {
  				if ($scope.userForm.$dirty) {
  					Messager.confirm("??????", "???????????????????????????????????????????").then(
  							function(result) {
  								$modalInstance.dismiss('cancel');
  							}, function() {
  							});
  				} else {
  					$modalInstance.dismiss('cancel');
  				}
  			}
  		} ]);
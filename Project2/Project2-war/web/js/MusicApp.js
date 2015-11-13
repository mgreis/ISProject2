/* global angular */
/* 
 Author : Flávio J. Saraiva
 */
(function () {
    'use strict';
    var app = angular.module('MusicApp', ['angular-loading-bar']);

    app.config([
        'cfpLoadingBarProvider',
        function (cfpLoadingBarProvider) {
            cfpLoadingBarProvider.latencyThreshold = 0;
        }
    ]);

    app.factory('self', [
        '$rootScope', '$http',
        function ($rootScope, $http) {
            var self = {
                beforeLoginViews: ['login', 'register'],
                afterLoginViews: ['playlists', 'catalog', 'logout'],
                view: '',
                isLoggedIn: false
            };

            /**
             * Returns all possible views.
             * @returns {Array}
             */
            self.getViews = function () {
                return self.isLoggedIn ? self.afterLoginViews : self.beforeLoginViews;
            };

            /**
             * Sets the current view.
             * @param {String|null} view
             */
            self.setView = function (view) {
                if (self.view) {
                    self.errors = null; // clear errors of previous view
                }
                var views = self.getViews();
                self.view = views.indexOf(view) !== -1 ? view : views[0]; // defaults to the first view
                self.broadcast('view', self.view);
            };

            /**
             * Load something from the server.
             * @param {String} url
             * @param {Array|null} data
             * @returns {promise}
             */
            self.load = function (url, data) {
                return $http({
                    method: 'POST',
                    url: url,
                    data: data
                }).then(null, function (response) {
                    console.log(response);
                    alert("error");
                });
            };

            /**
             * Upload something to the server.
             * @param {String} url
             * @param {Array|null} data
             * @returns {promise}
             */
            self.upload = function (url, data) {
                return $http.post(url, data, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).then(null, function (response) {
                    console.log(response);
                    alert("error");
                });

            };

            /**
             * Listens on events of a given type.
             * @param {string} name Event name to listen on.
             * @param {function(event, ...args)} listener Function to call when the event is emitted.
             * @returns {function()} Returns a deregistration function for this listener.
             */
            self.on = function (name, listener) {
                return $rootScope.$on(name, listener);
            };

            /**
             * Dispatches an event name downwards to all child scopes (and their children).
             * @param {String} name Event name to broadcast.
             * @param {Any} args Arguments which will be passed onto the event listeners.
             * @returns {Object} Event object.
             */
            self.broadcast = function (name, args) {
                return $rootScope.$broadcast(name, args);
            };

            self.setup = function () {
                self.load("account/resume.json", {
                }).then(function (response) {
                    self.isLoggedIn = response.data.result === 'success';
                    self.setView(self.view);
                });
            };

            $rootScope.logout = function () {
                self.load("account/logout.json", {
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                    } else {
                        self.isLoggedIn = false;
                        self.setView(self.view);
                    }
                });
            };

            return self;
        }
    ]);

    app.controller('ErrorsController', [
        'self',
        function (self) {
            var errors = this;

            errors.list = function () {
                return self.errors || [];
            };
        }
    ]);

    /**
     * Control the views
     */
    app.controller('ViewsController', [
        'self',
        function (self) {
            var views = this;
            views.self = self; // @todo remove

            views.setup = self.setup;

            views.isVisible = function (view) {
                return self.view === view;
            };

            views.switchTo = function (view) {
                self.setView(view);
            };

            views.navbarClasses = function (view) {
                return {
                    active: self.view === view,
                    hidden: self.getViews().indexOf(view) === -1
                };
            };
        }
    ]);

    app.controller('LoginController', [
        'self',
        function (self) {
            var login = this;

            login.email = null;
            login.password = null;

            login.submit = function () {
                self.load("account/login.json", {
                    email: login.email,
                    password: login.password
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    login.email = null;
                    login.password = null;
                    self.isLoggedIn = true;
                    self.setView();
                });
            };
        }
    ]);

    app.controller('RegisterController', [
        'self',
        function (self) {
            var register = this;

            register.email = null;
            register.password = null;
            register.passwordRepeat = null;

            register.submit = function () {
                self.load("account/register.json", {
                    email: register.email,
                    password: register.password,
                    passwordRepeat: register.passwordRepeat
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    register.email = null;
                    register.password = null;
                    register.passwordRepeat = null;
                    self.isLoggedIn = true;
                    self.setView();
                });
            };
        }
    ]);

    app.controller('PlaylistsController', [
        'self',
        '$filter',
        function (self, $filter) {
            var playlists = this;

            self.on('view', function (event, view) {
                if (view === 'playlists') {
                    playlists.loadList();
                }
            });

            playlists.loadList = function () {
                playlists.list;
                playlists.playlist = null;
                playlists.music = null;
                playlists.playing = null;
                self.load("playlist/load.json", {
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    playlists.list = response.data.playlists;
                });
            };

            playlists.playlistBtnClass = function (id) {
                return playlists.playlist && playlists.playlist.id === id ? 'btn-primary' : 'btn-default';
            };

            playlists.newPlaylist = function () {
                playlists.playlist = {};
                playlists.music = [];
                playlists.editPlaylist();
            };

            playlists.editPlaylist = function () {
                playlists.playlist = JSON.parse(JSON.stringify(playlists.playlist)); // clone
                playlists.isEditing = true;
                delete playlists.tmpMusic;
                delete playlists.tmpMusicQuery;
            };

            playlists.loadPlaylist = function (playlist) {
                delete playlists.playlist;
                delete playlists.music;
                delete playlists.playing;
                delete playlists.tmpMusic;
                delete playlists.tmpMusicQuery;
                playlists.isEditing = false;
                self.load("music/load.json", {
                    ids: playlist.music
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    playlists.playlist = playlist;
                    playlists.music = response.data.music;
                });
            };

            playlists.getMusicSrc = function () {
                var music = playlists.music[playlists.playing];
                return music ? music.url : null;
            };

            playlists.musicRowClass = function (index) {
                return playlists.playing === index ? 'info' : '';
            };

            playlists.playMusic = function (index) {
                // @todo validate?
                playlists.playing = index;
            };
            
            playlists.updateMusicIds = function () {
                var musicIds = [];
                angular.forEach(playlists.music, function (music) {
                    musicIds.push(music.id);
                });
                playlists.playlist.music = musicIds;
            };

            playlists.savePlaylist = function () {
                // @todo save (receives id)
                var id = playlists.playlist.id;
                var found = false;
                for (var index = 0; index < playlists.list.length; index++) {
                    if (playlists.list[index].id === id) {
                        playlists.list[index] = playlists.playlist; // replace original with modified clone
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    playlists.list.push(playlists.playlist);
                }
                delete playlists.tmpMusic;
                delete playlists.tmpMusicQuery;
                playlists.isEditing = false;
            };

            playlists.removeMusic = function (index) {
                playlists.music.splice(index, 1);
                playlists.updateMusicIds();
            };

            playlists.moveMusicUp = function (index) {
                var music = playlists.music.splice(index, 1)[0];
                playlists.music.splice(index - 1, 0, music);
                playlists.updateMusicIds();
            };

            playlists.moveMusicDown = function (index) {
                var music = playlists.music.splice(index, 1)[0];
                playlists.music.splice(index + 1, 0, music);
                playlists.updateMusicIds();
            };

            playlists.findMyMusic = function () {
                self.load("music/load/mine.json", {
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    playlists.tmpMusic = response.data.music;
                });
            };

            playlists.findOtherMusic = function () {
                self.load("music/load/other.json", {
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    playlists.tmpMusic = response.data.music;
                });
            };

            playlists.findQueryMusic = function () {
                self.load("music/find.json", {
                    query: playlists.tmpMusicQuery
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    playlists.tmpMusic = response.data.music;
                });
            };

            playlists.addMusic = function (music) {
                playlists.music.push(JSON.parse(JSON.stringify(music))); // clone
                playlists.updateMusicIds();
            };

        }
    ]);

    app.controller('CatalogController', [
        'self',
        function (self) {
            var catalog = this;

            self.on('view', function (event, view) {
                if (view === 'catalog') {
                    catalog.loadMusic();
                }
            });

            catalog.musicRowClass = function (index) {
                return catalog.tmpMusic && catalog.tmpMusic.id === catalog.music[index].id ? 'info' : '';
            };

            catalog.loadMusic = function () {
                self.load("music/load/mine.json", {
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    catalog.closeMusic();
                    catalog.music = response.data.music;
                });
            };

            catalog.newMusic = function () {
                catalog.editMusic({});
            };

            catalog.editMusic = function (music) {
                catalog.tmpMusic = JSON.parse(JSON.stringify(music)); // clone
                catalog.isEditing = true;
            };

            catalog.uploadMusic = function () {
                var inputMusicFile = $('#inputMusicFile');
                var data = new FormData;
                data.append("filename", inputMusicFile.val().replace(/^.*[\\\/]/, ''));
                data.append("data", inputMusicFile.get(0).files[0]);
                data.append("title", catalog.tmpMusic.title);
                data.append("artist", catalog.tmpMusic.artist);
                data.append("album", catalog.tmpMusic.album);
                data.append("releaseYear", catalog.tmpMusic.releaseYear);
                self.upload("music/upload.json", data).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    catalog.loadMusic();
                });
            };

            catalog.updateMusic = function () {
                self.load("music/update.json", {
                    title: catalog.tmpMusic.title,
                    artist: catalog.tmpMusic.artist,
                    album: catalog.tmpMusic.album,
                    releaseYear: catalog.tmpMusic.releaseYear
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    catalog.loadMusic();
                });
            };

            catalog.detachMusic = function () {
                self.load("music/detach.json", {
                    id: catalog.tmpMusic.id
                }).then(function (response) {
                    if (response.data.result === 'error') {
                        self.errors = response.data.errors;
                        return;
                    }
                    // success
                    catalog.loadMusic();
                });
            };

            catalog.closeMusic = function () {
                delete catalog.isEditing;
                delete catalog.tmpMusic;
            };

            catalog.getMimetype = function (music) {
                if (music && music.url) {
                    var url = music.url.split('?')[0];
                    url = url.substr(url.lastIndexOf('/'));
                    if (url.lastIndexOf(".") !== -1) {
                        return 'audio/' + url.substr(url.lastIndexOf(".") + 1);
                    }
                }
                return 'audio/wav'; // assumir que é um wav
            }

        }
    ]);

    /*
     * Multipart/form-data File Upload with AngularJS
     * @see https://uncorkedstudios.com/blog/multipartformdata-file-upload-with-angularjs
     */

    app.directive('fileModel', [
        '$parse',
        function ($parse) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;

                    element.bind('change', function () {
                        scope.$apply(function () {
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }
    ]);
    app.service('fileUpload', [
        '$http',
        function ($http) {
            this.uploadFileToUrl = function (file, uploadUrl) {
                var fd = new FormData();
                fd.append('file', file);
                $http.post(uploadUrl, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).success(function () {
                }).error(function () {
                });
            };
        }
    ]);
})();

/**
 * URLs to be used for REST Requests
 */
var getProjectURL = '/api/project'
var postProjectURL = '/api/project'
var postFolderURL = '/api/folder'
var postFeatureURL = '/api/feature'
/**
 * Trigger focus on forms
 */
$(document).ready(function () {
    $('#createProjectModal').on('shown.bs.modal', function () {
        $('#projectId').trigger('focus')
    })
    $('#createFolderModal').on('shown.bs.modal', function () {
        $('#folderFileName').trigger('focus')
    })
    $('#createFeatureModal').on('shown.bs.modal', function () {
        $('#featureFileName').trigger('focus')
    })
})

/**
 * home page project list
 */
Vue.component('homepage-projects', {
    template: '#homepage-projects',
    props: {
        allprojects: Array
    }
})

Vue.component('header-projects', {
    template: '#header-projects',
    props: {
        allprojects: Array
    }
})


Vue.component('create-project-modal', {
    template: '#create-project-modal',
    data: function () {
        return {
            emptyProject: {
                id: null
            },
            inputProject: {
                id: null
            },
            errorResult: null
        }
    },
    props: {
        allprojects: Array
    },
    methods: {
        processForm: function () {
            var self = this
            $.ajax({
                url: postProjectURL,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(this.inputProject),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    self.inputProject = $.extend(true, {}, self.emptyProject)
                    self.allprojects.push(result);
                    $(self.$refs["createProjectModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})
Vue.component('create-folder-modal', {
    template: '#create-folder-modal',
    data: function () {
        return {
            emptyFolder: {
                fileName: null,
                projectId: null,
                path: null
            },
            inputFolder: {
                fileName: null,
                projectId: null,
                path: null
            },
            errorResult: null
        }
    },
    methods: {
        processForm: function () {
            this.inputFolder.projectId = this.$root.currentProject.id;
            this.inputFolder.path = Utils.getConcatenatedPath(this.$root.selectedTreeElement.model)
            var self = this
            $.ajax({
                url: postFolderURL,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(this.inputFolder),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    self.inputFolder = $.extend(true, {}, self.emptyFolder);
                    newFolder = {model: result, type: 'FOLDER', children: []}
                    self.$root.selectedTreeElement.children.push(newFolder)
                    $(self.$refs["createFolderModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})

Vue.component('create-feature-modal', {
    template: '#create-feature-modal',
    data: function () {
        return {
            emptyFeature: {
                fileName: null,
                projectId: null,
                path: null,
                name: null,
                description: null
            },
            inputFeature: {
                fileName: null,
                projectId: null,
                path: null,
                name: null,
                description: null
            },
            errorResult: null
        }
    },
    methods: {
        processForm: function () {
            this.inputFeature.projectId = this.$root.currentProject.id;
            this.inputFeature.path = Utils.getConcatenatedPath(this.$root.selectedTreeElement.model)
            featureToSubmit = $.extend(true, {}, this.inputFeature)

            if (featureToSubmit.fileName) {
                featureToSubmit.fileName = featureToSubmit.fileName + '.feature'
            }
            var self = this
            $.ajax({
                url: postFeatureURL,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(featureToSubmit),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    self.inputFeature = $.extend(true, {}, self.emptyFeature)
                    newFeature = {model: result, type: 'FEATURE', children: null}
                    self.$root.selectedTreeElement.children.push(newFeature)
                    $(self.$refs["createFeatureModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})

var vueBody = new Vue({

    el: '#vue-support',

    data: {
        allprojects: null,
        currentProject: currentProject,
        selectedTreeElement: {model: {path: '/', fileName: ''}}
    },
    created: function () {
        this.fetchProjects()
    },
    methods: {
        fetchProjects: function () {
            var self = this
            $.ajax({
                url: getProjectURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.allprojects = result._embedded.project
                },

            });
        }
    }
})
/**
 * URLs to be used for REST Requests
 */
var getProjectURL = '/rest/api/project'
var postProjectURL = '/rest/api/project'
var postFolderURL = '/rest/api/folder'
var postFeatureURL = '/rest/api/feature'
var getTreeStructureURL = '/rest/api/treeStructure'
var getFeaturesRecursiveURL = '/rest/api/feature/search/findChildrenRecursive'
var getScenariosURL = '/rest/api/scenario/search/findChildren'

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

Vue.component('tree-sidebar', {
    template: '#tree-sidebar',
    data: function () {
        return {
            roots: null
        }
    },
    computed: {
        selectedTreeItem: function () {
            return this.$root.selectedTreeElement;
        }
    },
    created: function () {
        this.fetchTreeStructure(this.$root.currentProject.id)
    },
    watch: {
        selectedTreeItem: function (val) {
            switch (this.selectedTreeItem.type) {
                case 'FOLDER':
                    url = '/' + this.$root.currentProject.id + /folder/
                    if (this.selectedTreeItem.model.id !== 'folderRoot') {
                        url = url + this.selectedTreeItem.model.id
                    }
                    history.pushState({}, '', url);
                    break
                case 'FEATURE':
                    url = Utils.getUrlForFeature(this.$root.currentProject, this.selectedTreeItem.model)
                    history.pushState({}, '', url);
                    break
            }
        }
    },
    methods: {
        fetchTreeStructure: function (projectId) {
            var self = this
            $.ajax({
                url: getTreeStructureURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId},
                success: function (result) {
                    rootFolder = {
                        model: {id: 'folderRoot', path: '', fileName: '/'},
                        children: result,
                        type: 'FOLDER'
                    }
                    rootFolderList = [rootFolder]
                    if (selectedTreeItem) {
                        self.$root.selectedTreeElement = selectedTreeItem
                    } else {
                        self.$root.selectedTreeElement = rootFolder
                    }
                    self.roots = rootFolderList
                },

            });
        }
    }
})
// define the item component
Vue.component('tree-sidebar-item', {
    name: 'tree-sidebar-item',
    template: '#tree-sidebar-item',
    props: {
        item: Object,
    },
    data: function () {
        return {
            open: false
        }
    },
    created: function () {
        if (this.isSelected && this.$parent.setOpen) {
            this.$parent.setOpen()
        }
    },
    computed: {
        isSelected: function () {
            return this.item.model.id === this.$root.selectedTreeElement.model.id
        }
    },
    watch: {
        open: function (val) {
            if (this.open && this.$parent.setOpen) {
                this.$parent.setOpen()
            }
        },
        isSelected: function (val) {
            if (this.isSelected && this.$parent.setOpen) {
                this.$parent.setOpen()
            }
        }
    },
    methods: {
        toggle: function () {
            if (this.item.type === 'FOLDER') {
                this.open = !this.open
            }
        },
        select: function () {
            this.$root.selectedTreeElement = this.item
        },
        setOpen: function () {
            this.open = true;
            if (this.$parent.setOpen) {
                this.$parent.setOpen()
            }
        }
    }
})

Vue.component('feature-list', {
    template: '#feature-list',
    data: function () {
        return {
            features: null
        }
    },
    computed: {
        selectedTreeItem: function () {
            return this.$root.selectedTreeElement;
        }
    },
    watch: {
        selectedTreeItem: function (val) {

            this.fetchFeatures(this.$root.currentProject.id, this.selectedTreeItem.model.path + this.selectedTreeItem.model.fileName)
        }
    },
    created: function () {
        this.fetchFeatures(this.$root.currentProject.id, this.selectedTreeItem.model.path + this.selectedTreeItem.model.fileName)
    },
    methods: {
        fetchFeatures: function (projectId, path) {
            console.log('selected:' + projectId + '###' + path)
            var self = this
            $.ajax({
                url: getFeaturesRecursiveURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId, path: path},
                success: function (result) {
                    self.features = result._embedded.feature
                },

            });
        }
    }
})

Vue.component('feature-card', {
    template: '#feature-card',
    props: {
        feature: Object
    },
    computed: {
        url: function () {
            return Utils.getUrlForFeature(this.$root.currentProject, this.feature);
        }
    },
    methods: {
        select: function () {
            this.$root.selectedTreeElement = {type: 'FEATURE', model: this.feature}
        }
    }
})

Vue.component('scenario-list', {
    template: '#scenario-list',
    props: {
        scenarios: Array
    }
})

Vue.component('scenario-card', {
    template: '#scenario-card',
    props: {
        scenario: Object
    },
    computed: {
        url: function () {
            return Utils.getUrlForScenario(this.$root.currentProject, this.scenario);
        }
    },
    methods: {
        select: function () {
            this.$root.selectedTreeElement = {type: 'SCENARIO', model: this.scenario}
        }
    }
})

Vue.component('feature-detail', {
    template: '#feature-detail',
    data: function () {
        return {
            scenarios: []
        }
    },
    props: {
        feature: Object
    },
    computed: {
        url: function () {
            return Utils.getUrlForFeature(this.$root.currentProject, this.feature);
        }
    }, created: function () {
        this.fetchScenarios(this.$root.currentProject.id, this.feature.path + this.feature.fileName)
    },
    watch: {
        feature: function (val) {
            this.fetchScenarios(this.$root.currentProject.id, this.feature.path + this.feature.fileName)
        }
    },
    methods: {
        fetchScenarios: function (projectId, path) {
            var self = this
            $.ajax({
                url: getScenariosURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId, path: path},
                success: function (result) {
                    self.scenarios = result._embedded.scenario
                },

            });
        }
    }
})

Vue.component('scenario-detail', {
    template: '#scenario-detail',
    props: {
        scenario: Object
    },
    computed: {
        url: function () {
            return Utils.getUrlForScenario(this.$root.currentProject, this.scenario);
        }
    }
})

var vueBody = new Vue({

    el: '#vue-support',

    data: {
        allprojects: null,
        currentProject: currentProject,
        selectedTreeElement: selectedTreeItem
    },
    created: function () {
        this.fetchProjects()
        if (!this.selectedTreeElement) {
            this.selectedTreeElement = {model: {path: '/', fileName: ''}}
        }
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
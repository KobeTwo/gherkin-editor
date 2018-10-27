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
var deleteScenarioURL = '/rest/api/scenario/'
var deleteFeatureURL = '/rest/api/feature/'

var vueBus = new Vue();

var treeItemMixins = {
    methods: {
        getConcatenatedPathByPathAndFileName: function (path, fileName) {
            concatenatedPath = null;
            if (path && !path.endsWith('/')) {
                path = path + '/'
            }
            if (fileName) {
                if (!fileName.endsWith('/')) {
                    fileName = fileName + '/'
                }
            } else {
                fileName = ''
            }

            return path + fileName;

        },
        getConcatenatedPath: function (pathItem) {
            return this.getConcatenatedPathByPathAndFileName(pathItem.path, pathItem.fileName)
        },
        getUrlForFeature: function (project, feature) {
            return "/" + project.id + "/feature/" + feature.id
        },
        getFileNameWithoutSuffix: function (folderItem) {
            return folderItem.fileName.replace('.feature', '')
        },
        getUrlForScenario: function (project, scenario) {
            return "/" + project.id + "/scenario/" + scenario.id
        },
        getRootFolder: function () {
            rootFolder = {
                model: {id: 'folderRoot', path: '', fileName: '/'},
                type: 'FOLDER'
            }
            return rootFolder
        }
    }
}


/**
 * enable tooltips
 */
$(document).ready(function () {
    $("body").tooltip({selector: '[data-toggle=tooltip]'});
});

/**
 * Trigger focus on forms
 */
$(document).ready(function () {
    $('#createProjectModal').on('shown.bs.modal', function () {
        $('#projectId').trigger('focus')
    })
    $('#importProjectModal').on('shown.bs.modal', function () {
        $('#projectId').trigger('focus')
    })
    $('#createFolderModal').on('shown.bs.modal', function () {
        $('#folderFileName').trigger('focus')
    })
    $('#createFeatureModal').on('shown.bs.modal', function () {
        $('#featureFileName').trigger('focus')
    })

    $('#project-archive').on('change', function () {
        fileName = $(this).val().split('\\').pop();
        $(this).next('#project-archive-label').addClass("selected").html(fileName);
    });
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
                    vueBus.$emit("createdProject", result)
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})

Vue.component('import-project-modal', {
    template: '#import-project-modal',
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
            this.inputFolder.path = this.getConcatenatedPath(this.$root.selectedTreeElement.model)
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
            this.inputFeature.path = this.getConcatenatedPath(this.$root.selectedTreeElement.model)
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
    mixins: [treeItemMixins],
    props: {
        roots: Array
    },
    created: function () {
        this.fetchTreeStructure(this.$root.currentProject.id)


        vueBus.$on('deletedScenario', (scenario) => {
            this.$root.selectedTreeElement = this.getRootFolder()
        });
        vueBus.$on('deletedFeature', (feature) => {
            this.$root.selectedTreeElement = this.getRootFolder()
        });
        vueBus.$on('deletedFolder', (folder) => {
            this.$root.selectedTreeElement = this.getRootFolder()
        });
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
                    rootFolder = self.getRootFolder();
                    rootFolder.children = result
                    rootFolderList = [rootFolder]
                    if (selectedTreeItem) {
                        self.$root.selectedTreeElement = selectedTreeItem
                    } else {
                        self.$root.selectedTreeElement = rootFolder
                    }
                    self.$root.treeRoots = rootFolderList
                },

            });
        }
    },
    computed: {
        selectedTreeItem: function () {
            return this.$root.selectedTreeElement;
        }
    },
    watch: {
        selectedTreeItem: function (val) {
            switch (this.selectedTreeItem.type) {
                case 'FOLDER':
                    url = '/' + this.$root.currentProject.id + /folder/
                    if (this.selectedTreeItem.model.id !== this.getRootFolder().model.id) {
                        url = url + this.selectedTreeItem.model.id
                    }
                    history.pushState({}, '', url);
                    break
                case 'FEATURE':
                    url = this.getUrlForFeature(this.$root.currentProject, this.selectedTreeItem.model)
                    history.pushState({}, '', url);
                    break
                case 'SCENARIO':
                    url = this.getUrlForScenario(this.$root.currentProject, this.selectedTreeItem.model)
                    history.pushState({}, '', url);
                    break
            }
        }
    }
})
// define the item component
Vue.component('tree-sidebar-item', {
    name: 'tree-sidebar-item',
    mixins: [treeItemMixins],
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
        if (this.$parent.item && this.$parent.item.pathItems != null) {
            this.item.pathItems = new Array(this.$parent.item.pathItems)
            this.item.pathItems.push(this.item)
        } else {
            this.item.pathItems = new Array
        }

        vueBus.$on('deletedFeature', (feature) => {
            if (this.item.children) {
                this.item.children = this.item.children.filter(function (value, index, arr) {
                    return value.model.id !== feature.id
                });
            }

        });

        vueBus.$on('deletedScenario', (scenario) => {
            if (this.item.children) {
                this.item.children = this.item.children.filter(function (value, index, arr) {
                    return value.model.id !== scenario.id
                });
            }

        });

    },
    computed: {
        isSelected: function () {
            return this.item.model.id === this.$root.selectedTreeElement.model.id
        },
        fileNameWithoutSuffix: function (val) {
            switch (this.item.type) {
                case 'FOLDER':
                    return this.model.fileName
                case 'FEATURE':
                    return this.getFileNameWithoutSuffix(this.item.model)
            }
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
            return this.getUrlForFeature(this.$root.currentProject, this.feature);
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
            return this.getUrlForScenario(this.$root.currentProject, this.scenario);
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
    mixins: [treeItemMixins],
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
            return this.getUrlForFeature(this.$root.currentProject, this.feature);
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
            return this.getUrlForScenario(this.$root.currentProject, this.scenario);
        }
    },
    methods: {
        addTag: function () {
            this.scenario.tags.push('')
        },
        removeTag: function (name) {
            this.scenario.tags = this.scenario.tags.filter(function (value, index, arr) {
                return value !== name
            });
        },
        removeScenario: function () {
            alert('TODO')
        },
        saveScenario: function () {
            alert('TODO')
        }
    }
})

Vue.component('tag-input', {
    template: '#tag-input',
    props: {
        tag: String
    },
    methods: {
        remove: function () {
            this.$parent.removeTag(this.tag)
        }
    }
})

Vue.component('step-list', {
    template: '#step-list',
    props: {
        steps: Array
    }
})

Vue.component('delete-scenario-modal', {
    template: '#delete-scenario-modal',
    data: function () {
        return {
            error: false
        }
    },
    props: {
        scenario: Object
    },
    methods: {
        processForm: function () {
            var self = this
            $.ajax({
                url: deleteScenarioURL + this.scenario.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("deletedScenario", self.scenario)
                    $(self.$refs["deleteScenarioModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})

Vue.component('delete-feature-modal', {
    template: '#delete-feature-modal',
    data: function () {
        return {
            error: false
        }
    },
    props: {
        feature: Object
    },
    methods: {
        processForm: function () {
            var self = this
            $.ajax({
                url: deleteFeatureURL + this.feature.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("deletedFeature", self.feature)
                    $(self.$refs["deleteFeatureModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                }
            });
        }
    }
})

Vue.component('global-alert-box', {
    template: '#global-alert-box',
    data: function () {
        return {
            alerts: []
        }
    },
    methods: {
        addAlert: function (type, text, dismissible) {
            alert = {};
            alert.type = type
            alert.text = text
            alert.dismissible = dismissible
            this.alerts.push(alert)
        }
    },
    created: function () {
        vueBus.$on('createdProject', (project) => {
            this.addAlert("alert-success", "Project " + project.id + " was successfully created", true)
        });

        vueBus.$on('deletedScenario', (scenario) => {
            this.addAlert("alert-success", "Scenario " + scenario.name + " was successfully deleted", true)
        });

        vueBus.$on('deletedFeature', (feature) => {
            this.addAlert("alert-success", "Feature " + feature.fileName + " was successfully deleted", true)
        });

        vueBus.$on('deletedFolder', (folder) => {
            this.addAlert("alert-success", "Folder " + folder.fileName + " was successfully deleted", true)
        });


    }
})
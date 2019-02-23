/**
 * URLs to be used for REST Requests
 */
var getProjectURL = '/rest/api/project'
var importProjectURL = '/rest/api/project/import'
var postProjectURL = '/rest/api/project'
var postFolderURL = '/rest/api/folder'
var postFeatureURL = '/rest/api/feature'
var getTreeStructureURL = '/rest/api/treeStructure'
var getFeaturesRecursiveURL = '/rest/api/feature/search/findChildrenRecursive'
var getScenariosURL = '/rest/api/scenario/search/findChildren'
var patchFeatureURL = '/rest/api/feature/'
var patchScenarioURL = '/rest/api/scenario/'
var deleteScenarioURL = '/rest/api/scenario/'
var deleteFeatureURL = '/rest/api/feature/'
var deleteFolderURL = '/rest/api/folder/'
var suggestStepsURL = '/rest/api/suggest'

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
                    vueBus.$emit("addAlert", "alert-success", "Project " + result.id + " was successfully created", true)
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
                id: null,
                file: null
            },
            errorResult: null
        }
    },
    props: {
        allprojects: Array
    },
    methods: {
        processFile(event) {
            this.inputProject.file = event.target.files[0]
        },
        processForm: function () {
            var self = this
            var data = new FormData();
            data.append('file', this.inputProject.file)
            data.append('projectId', this.inputProject.id)
            $.ajax({
                url: importProjectURL,
                type: 'POST',
                cache: false,
                contentType: false,
                processData: false,
                data: data,
                success: function (result) {
                    self.errorResult = null
                    self.inputProject = $.extend(true, {}, self.emptyProject)
                    self.allprojects.push(result);
                    $(self.$refs["importProjectModal"]).modal('hide')
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

        vueBus.$on('deletedFolder', (folder) => {
            if (this.item.children) {
                this.item.children = this.item.children.filter(function (value, index, arr) {
                    return value.model.id !== folder.id
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
        },
        saveFeature: function () {
            var self = this
            $.ajax({
                url: patchFeatureURL + self.feature.id,
                type: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(this.feature),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("saveFeature", result)
                    vueBus.$emit("addAlert", "alert-success", "Feature " + self.feature.name + " was saved", true)
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", "Error while saving feature " + self.feature.name, true)
                }
            });
        },
        removeTag: function (name) {
            this.feature.tags.splice(this.feature.tags.indexOf(name), 1);
        },
        addTag: function () {
            this.feature.tags.push('')
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
            this.scenario.tags.splice(this.scenario.tags.indexOf(name), 1);
        },
        removeScenario: function () {
            alert('TODO')
        },
        saveScenario: function () {
            var self = this
            $.ajax({
                url: patchScenarioURL + self.scenario.id,
                type: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(this.scenario),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("patchScenario", result)
                    vueBus.$emit("addAlert", "alert-success", "Scenario " + self.scenario.name + " was saved", true)
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", "Error while saving scenario " + self.scenario.name, true)
                }
            });
        }
    }
})

Vue.component('tag-list-input', {
    template: '#tag-list-input',
    props: {
        tags: Array
    },
    methods: {
        removeTag: function (tag) {
            this.$parent.removeTag(tag)
        }
    }
})

Vue.component('step-list', {
    template: '#step-list',
    props: {
        steps: Array
    },
    methods: {
        deleteStep: function (step) {
            this.steps.splice(this.steps.indexOf(step), 1);

        }
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
                    vueBus.$emit("addAlert", "alert-success", "Scenario " + self.scenario + " was successfully deleted", true)
                    $(self.$refs["deleteScenarioModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", "Error while deleting scenario " + self.scenario, true)
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
                    vueBus.$emit("addAlert", "alert-success", "Feature " + self.feature + " was successfully deleted", true)
                    $(self.$refs["deleteFeatureModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", "Error while deleting feature " + self.feature, true)
                }
            });
        }
    }
})

Vue.component('delete-folder-modal', {
    template: '#delete-folder-modal',
    data: function () {
        return {
            error: false
        }
    },
    props: {
        folder: Object
    },
    methods: {
        processForm: function () {
            var self = this
            $.ajax({
                url: deleteFolderURL + this.folder.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("deletedFolder", self.folder)
                    vueBus.$emit("addAlert", "alert-success", "Folder " + self.folder + " was successfully deleted", true)
                    $(self.$refs["deleteFolderModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", "Error while deleting folder " + self.folder, true)
                }
            });
        }
    }
})

Vue.component('global-alert-box', {
    template: '#global-alert-box',
    data: function () {
        return {
            alerts: new Map()
        }
    },
    methods: {
        addAlert: function (type, text, dismissible) {
            alert = {};
            alert.type = type
            alert.text = text
            alert.dismissible = dismissible
            this.alerts.set(text, alert)
            this.$forceUpdate()
        }
    },
    created: function () {
        vueBus.$on('addAlert', (type, text, dismissible) => {
            this.addAlert(type, text, dismissible)
        });
    }
})

Vue.component('data-table', {
    template: '#data-table',
    data: function () {
        return {
            datatable: [[1, 2], [3, 4]]
        }
    },
    methods: {
        addHorizontal: function () {
            alert('added hor')
        },
        addVertical: function () {
            alert('added ver')
        }
    }
})

Vue.component('step-input', {
    template: '#step-input',
    data: function () {
        return {
            suggestions: [],
            focused: false
        }
    },
    props: {
        step: Object
    },
    computed: {
        text: function () {
            return this.step.text;
        }
    },
    watch: {
        text: function (val) {
            if (this.focused) {
                var self = this
                $.ajax({
                    url: suggestStepsURL,
                    type: 'GET',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: {text: val},
                    success: function (result) {
                        self.suggestions = result
                    },
                });
            }
        },
        focused: function (val) {
            if (!this.focused) {
                this.suggestions = []
            }
        }
    }
})
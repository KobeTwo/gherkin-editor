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
var postScenarioURL = '/rest/api/scenario'

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
        getUrlForFeature: function (feature) {
            return "/" + feature.projectId + "/feature/" + feature.id
        },
        getFileNameWithoutSuffix: function (folderItem) {
            return folderItem.fileName.replace('.feature', '')
        },
        getUrlForScenario: function (scenario) {
            return "/" + scenario.projectId + "/scenario/" + scenario.id
        },
        getRootFolder: function () {
            rootFolder = {
                model: {id: 'folderRoot', path: '', fileName: '/', projectId: this.$root.currentProject.id},
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
    mixins: [treeItemMixins],
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
            errorResult: null,
            selectedTreeElement: null
        }
    },
    created: function () {
        vueBus.$on('selectTreeElement', (selectedItem) => {
            this.selectedTreeElement = selectedItem
        });
    },
    methods: {
        processForm: function () {
            this.inputFolder.projectId = this.$root.currentProject.id;
            this.inputFolder.path = this.getConcatenatedPath(this.selectedTreeElement.model)
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
                    self.selectedTreeElement.children.push(newFolder)
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
    mixins: [treeItemMixins],
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
            errorResult: null,
            selectedTreeElement: null
        }
    },
    created: function () {
        vueBus.$on('selectTreeElement', (selectedItem) => {
            this.selectedTreeElement = selectedItem
        });
    },
    methods: {
        processForm: function () {
            this.inputFeature.projectId = this.$root.currentProject.id;
            this.inputFeature.path = this.getConcatenatedPath(this.selectedTreeElement.model)
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
                    self.selectedTreeElement.children.push(newFeature)
                    vueBus.$emit("createdFeature", newFeature.model)
                    vueBus.$emit("addAlert", "alert-success", txtFeatureCreateSuccess + newFeature.model.name, true)
                    $(self.$refs["createFeatureModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", txtFeatureCreateError, true)
                }
            });
        }
    }
})

Vue.component('create-scenario-modal', {
    template: '#create-scenario-modal',
    mixins: [treeItemMixins],
    data: function () {
        return {
            emptyScenario: {
                projectId: null,
                path: null,
                name: null,
                description: null
            },
            inputScenario: {
                projectId: null,
                path: null,
                name: null,
                description: null
            },
            errorResult: null,
            selectedTreeElement: null
        }
    },
    created: function () {
        vueBus.$on('selectTreeElement', (selectedItem) => {
            this.selectedTreeElement = selectedItem
        });
    },
    methods: {
        processForm: function () {
            this.inputScenario.projectId = this.$root.currentProject.id;
            this.inputScenario.path = this.getConcatenatedPath(this.selectedTreeElement.model)
            scenarioToSubmit = $.extend(true, {}, this.inputScenario)
            var self = this
            $.ajax({
                url: postScenarioURL,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(scenarioToSubmit),
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    self.inputScenario = $.extend(true, {}, self.emptyScenario)
                    newScenario = {model: result, type: 'SCENARIO', children: null}
                    self.selectedTreeElement.children.push(newScenario)
                    vueBus.$emit("createdScenario", newScenario.model)
                    vueBus.$emit("addAlert", "alert-success", txtScenarioCreateSuccess + newScenario.model.name, true)
                    $(self.$refs["createScenarioModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", txtScenarioCreateError, true)
                }
            });
        }
    }
})

Vue.component('tree-sidebar', {
    template: '#tree-sidebar',
    mixins: [treeItemMixins],
    data: function () {
        return {
            roots: null
        }
    },
    created: function () {
        this.fetchTreeStructure(this.$root.currentProject.id)


        vueBus.$on('deletedScenario', (scenario) => {
            vueBus.$emit("selectTreeElement", this.getRootFolder())
        });
        vueBus.$on('deletedFeature', (feature) => {
            vueBus.$emit("selectTreeElement", this.getRootFolder())
        });
        vueBus.$on('deletedFolder', (folder) => {
            vueBus.$emit("selectTreeElement", this.getRootFolder())
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
                    self.roots = rootFolderList
                },

            });
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
            open: false,
            selected: false,
            highlighted: false
        }
    },
    created: function () {

        vueBus.$on('selectTreeElement', (selectedItem) => {
            if (selectedItem.model.id == this.item.model.id) {
                this.selected = true;
                this.setOpen();
                if (this.item.type == 'SCENARIO') {
                    this.$parent.highlighted = true
                } else {
                    this.highlighted = true
                }
            } else {
                this.selected = false;
                this.highlighted = false
            }
        });

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

        if (initialTreeItem && initialTreeItem.model.id == this.item.model.id) {
            vueBus.$emit("selectTreeElement", this.item)
        }

        if (this.selected && this.$parent.setOpen) {
            this.$parent.setOpen()
        }
        if (this.$parent.item && this.$parent.item.pathItems != null) {
            this.item.pathItems = new Array(this.$parent.item.pathItems)
            this.item.pathItems.push(this.item)
        } else {
            this.item.pathItems = new Array
        }

    },
    computed: {
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
        }
    },
    methods: {
        toggle: function () {
            if (this.item.type === 'FOLDER') {
                this.open = !this.open
            }
        },
        select: function () {
            vueBus.$emit("selectTreeElement", this.item)
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
            folder: null,
            features: null,
            show: false,
        }
    },
    created: function () {
        if (initialTreeItem && initialTreeItem.type == 'FOLDER') {
            let folder = initialTreeItem.model
            this.fetchFeatures(folder.projectId, folder.path + folder.fileName)
        }

        vueBus.$on('selectTreeElement', (selectedItem) => {
            if (selectedItem && selectedItem.type == 'FOLDER') {
                this.show = true
                this.folder = selectedItem.model
                this.fetchFeatures(this.folder.projectId, this.folder.path + this.folder.fileName)
            } else {
                this.show = false
            }
        });
        vueBus.$on('createdFeature', (feature) => {
            this.fetchFeatures(this.folder.projectId, this.folder.path + this.folder.fileName)
        });
    },
    methods: {
        fetchFeatures: function (projectId, path) {
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
            return this.getUrlForFeature(this.feature);
        }
    },
    methods: {
        select: function () {
            vueBus.$emit("selectTreeElement", {type: 'FEATURE', model: this.feature})
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
            return this.getUrlForScenario(this.scenario);
        }
    },
    methods: {
        select: function () {
            vueBus.$emit("selectTreeElement", {type: 'SCENARIO', model: this.scenario})
        }
    }
})

Vue.component('feature-detail', {
    template: '#feature-detail',
    mixins: [treeItemMixins],
    data: function () {
        return {
            scenarios: [],
            feature: Object,
            show: false
        }
    },
    computed: {
        url: function () {
            return this.getUrlForFeature(this.feature);
        }
    },
    created: function () {
        if (initialTreeItem && initialTreeItem.type == 'FEATURE') {
            this.feature = initialTreeItem.model
            this.fetchScenarios(this.feature.projectId, this.feature.path + this.feature.fileName)
        }

        vueBus.$on('selectTreeElement', (selectedItem) => {
            if (selectedItem && selectedItem.type == 'FEATURE') {
                this.show = true
                this.feature = selectedItem.model
                this.fetchScenarios(this.feature.projectId, this.feature.path + this.feature.fileName)
            } else {
                this.show = false
            }

        });
        vueBus.$on('createdScenario', (scenario) => {
            this.fetchScenarios(this.feature.projectId, this.feature.path + this.feature.fileName)
        });
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
    data: function () {
        return {
            scenario: Object,
            show: false
        }
    },
    computed: {
        url: function () {
            return this.getUrlForScenario(this.scenario);
        }
    },
    created: function () {
        if (initialTreeItem && initialTreeItem.type == 'SCENARIO') {
            this.scenario = initialTreeItem.model
        }

        vueBus.$on('selectTreeElement', (selectedItem) => {
            if (selectedItem && selectedItem.type == 'SCENARIO') {
                this.show = true
                this.scenario = selectedItem.model
            } else {
                this.show = false
            }

        });
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
    data: function () {
        return {
            inputStep: {
                type: "GIVEN",
                text: ""
            },
            suggestions: [],
            focused: false
        }
    },
    computed: {
        text: function () {
            return this.inputStep.text;
        }
    },
    props: {
        steps: Array
    },
    methods: {
        addStep: function (inputStep) {
            this.steps.push(JSON.parse(JSON.stringify(inputStep)));
            this.inputStep.text = "";
            this.$refs.inputText.focus()
        }
    },
    watch: {
        text: function (val) {
            var self = this
            $.ajax({
                url: suggestStepsURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {text: val},
                success: function (result) {
                    if (self.focused) {
                        self.suggestions = result
                    }
                },
            });
        },
        focused: function (val) {
            if (!this.focused) {
                this.suggestions = []
            }
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
    methods: {
        deleteStep: function (step) {
            this.steps.splice(this.steps.indexOf(step), 1);
        },
    },
    props: {
        step: Object,
        steps: Array
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
            let name = self.scenario.path + self.scenario.fileName
            $.ajax({
                url: deleteScenarioURL + this.scenario.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null
                    vueBus.$emit("deletedScenario", self.scenario)
                    vueBus.$emit("addAlert", "alert-success", txtScenarioDeleteSuccess + name, true)
                    $(self.$refs["deleteScenarioModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", txtScenarioDeleteError + name, true)
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
            let name = self.feature.path + self.feature.fileName
            $.ajax({
                url: deleteFeatureURL + this.feature.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null

                    vueBus.$emit("deletedFeature", self.feature)
                    vueBus.$emit("addAlert", "alert-success", txtFeatureDeleteSuccess + name, true)
                    $(self.$refs["deleteFeatureModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", txtFeatureDeleteError + name, true)
                }
            });
        }
    }
})

Vue.component('delete-folder-modal', {
    template: '#delete-folder-modal',
    data: function () {
        return {
            error: false,
            folder: Object
        }
    },
    created: function () {
        if (initialTreeItem && initialTreeItem.type == 'FOLDER') {
            this.folder = initialTreeItem.model
        }
        vueBus.$on('selectTreeElement', (selectedItem) => {
            this.folder = selectedItem.model
        });
    },
    methods: {
        processForm: function () {
            var self = this
            let name = self.folder.path + self.folder.fileName
            $.ajax({
                url: deleteFolderURL + this.folder.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.errorResult = null

                    vueBus.$emit("deletedFolder", self.folder)
                    vueBus.$emit("addAlert", "alert-success", txtFolderDeleteSuccess + name, true)
                    $(self.$refs["deleteFolderModal"]).modal('hide')
                },
                error: function (result) {
                    self.errorResult = result.responseJSON
                    vueBus.$emit("addAlert", "alert-danger", txtFolderDeleteError + name, true)
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
            sizeX: 0,
            sizeY: 0
        }
    },
    props: {
        datatable: Array
    },
    methods: {
        addHorizontal: function () {
            this.sizeX++
            this.resizeArray()
        },
        addVertical: function () {
            this.sizeY++
            this.resizeArray()
        },
        deleteRow(y) {
            alert('delete row')
        },
        resizeArray() {
            for (y = 0; y < this.sizeY; y++) {
                if (!this.datatable[y]) {
                    Vue.set(this.datatable, y, [])
                }
                for (x = 0; x < this.sizeX; x++) {
                    if (!this.datatable[y][x]) {
                        Vue.set(this.datatable[y], x, '')
                    }
                }
            }
        },
        recalculateSize() {
            this.sizeY = this.datatable.length
            var index;
            for (index = 0; index < this.datatable.length; ++index) {
                this.sizeX = this.datatable[index].length
            }
        }
    },
    watch: {
        datatable: function (val) {
            this.recalculateSize()
        }
    },
    created: function () {
        this.recalculateSize()
    }
})


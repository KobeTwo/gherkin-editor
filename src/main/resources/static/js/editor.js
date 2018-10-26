var vueBody = new Vue({

    el: '#vue-editor',

    data: {
        allprojects: null,
        currentProject: currentProject,
        selectedTreeElement: selectedTreeItem,
        treeRoots: null,
    },
    created: function () {
        this.fetchProjects()
        if (!this.selectedTreeElement) {
            this.selectedTreeElement = {model: {path: '/', fileName: ''}}
        }
        this.fetchTreeStructure(this.$root.currentProject.id)

        vueBus.$on('deletedScenario', (scenario) => {
            this.selectedTreeElement = Utils.getRootFolder()
        });
        vueBus.$on('deletedFeature', (feature) => {
            this.selectedTreeElement = Utils.getRootFolder()
        });
        vueBus.$on('deletedFolder', (folder) => {
            this.selectedTreeElement = Utils.getRootFolder()
        });
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
        },
        fetchTreeStructure: function (projectId) {
            var self = this
            $.ajax({
                url: getTreeStructureURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId},
                success: function (result) {
                    rootFolder = Utils.getRootFolder();
                    rootFolder.children = result
                    rootFolderList = [rootFolder]
                    if (selectedTreeItem) {
                        self.selectedTreeElement = selectedTreeItem
                    } else {
                        self.selectedTreeElement = rootFolder
                    }
                    self.treeRoots = rootFolderList
                },

            });
        }
    }
})
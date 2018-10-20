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
                    rootFolder = {
                        model: {id: 'folderRoot', path: '', fileName: '/'},
                        children: result,
                        type: 'FOLDER'
                    }
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
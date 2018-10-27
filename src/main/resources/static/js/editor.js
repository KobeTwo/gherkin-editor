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
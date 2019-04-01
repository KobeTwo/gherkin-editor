var vueBody = new Vue({

    el: '#vue-editor',
    data: {
        allprojects: null,
        currentProject: currentProject,
        selectedTreeElement: null
    },
    mixins: [treeItemMixins],
    created: function () {
        if (!initialTreeItem) {
            initialTreeItem = this.getRootFolder();
        }
        this.fetchProjects()

        vueBus.$on('selectedTreeElementChanged', (selectedItem) => {
            switch (selectedItem.type) {
                case 'FOLDER':
                    url = '/' + selectedItem.model.projectId + /folder/
                    if (selectedItem.model.id !== this.getRootFolder().model.id) {
                        url = url + selectedItem.model.id
                    }
                    history.pushState({}, '', url);
                    break
                case 'FEATURE':
                    url = this.getUrlForFeature(selectedItem.model)
                    history.pushState({}, '', url);
                    break
                case 'SCENARIO':
                    url = this.getUrlForScenario(selectedItem.model)
                    history.pushState({}, '', url);
                    break
            }
        });
    },
    watch: {
        selectedTreeElement: function (val) {
            vueBus.$emit("selectedTreeElementChanged", val)
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